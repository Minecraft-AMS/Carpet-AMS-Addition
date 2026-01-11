/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.mixin.network;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.SettingsManager;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"LoggingSimilarMessage", "PatternVariableCanBeUsed"})
@Mixin(SettingsManager.class)
public abstract class Carpet_SettingsManagerMixin {
    @Final
    @Shadow
    private Map<String, CarpetRule<?>> rules;

    @Shadow
    private MinecraftServer server;

    @Shadow
    protected abstract int setDefault(CommandSourceStack source, CarpetRule<?> parsedRule, String value);

    @Unique
    private final Translator tr = new Translator("observer.amsNetworkProtocol");

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void collectAmsNetworkRuleNames(CallbackInfo ci) {
        NetworkUtil.collectAmsNetworkRuleNames();
    }

    @Inject(method = "loadConfigurationFromConf", at = @At("TAIL"))
    private void resetAmsNetworkRulesOnLoadConfig(CallbackInfo ci) {
        CarpetRule<?> amsNetworkProtocolRule = rules.get("amsNetworkProtocol");

        if (amsNetworkProtocolRule == null) {
            return;
        }

        Object ruleValue = amsNetworkProtocolRule.value();

        if (!(ruleValue instanceof Boolean)) {
            return;
        }

        Boolean isAmsNetworkProtocolEnabled = (Boolean) ruleValue;

        if (!isAmsNetworkProtocolEnabled) {
            CommandSourceStack source = server.createCommandSourceStack();

            for (String ruleName : NetworkUtil.AMS_NETWORK_RULE_NAMES) {
                CarpetRule<?> targetRule = rules.get(ruleName);

                if (targetRule == null) {
                    continue;
                }

                if (targetRule.equals(amsNetworkProtocolRule)) {
                    continue;
                }

                if (Objects.equals(CarpetUtil.getRuleCurrentValue(targetRule), CarpetUtil.getRuleDefaultValue(targetRule))) {
                    continue;
                }

                try {
                    this.setDefault(source, targetRule, CarpetUtil.getRuleDefaultValue(targetRule));
                } catch (Exception e) {
                    CarpetAMSAdditionServer.LOGGER.error("Failed to set {} rule to default value", ruleName);
                }
            }
        }
    }

    @Inject(method = {"setRule", "setDefault"}, at = @At("HEAD"), cancellable = true)
    private void resetAmsNetworkRulesOnSetRule(CommandSourceStack source, CarpetRule<?> rule, String newValue, CallbackInfoReturnable<Integer> cir) {
        CarpetRule<?> amsNetworkProtocolRule = rules.get("amsNetworkProtocol");
        String ruleName = CarpetUtil.getRuleName(rule);

        if (!NetworkUtil.AMS_NETWORK_RULE_NAMES.contains(ruleName) || rule.equals(amsNetworkProtocolRule)) {
            return;
        }

        if (!CarpetAMSAdditionSettings.amsNetworkProtocol && !Objects.equals(newValue, CarpetUtil.getRuleDefaultValue(rule)) && MinecraftServerUtil.serverIsRunning()) {
            try {
                rule.set(source, CarpetUtil.getRuleDefaultValue(rule));
            } catch (Exception e) {
                CarpetAMSAdditionServer.LOGGER.error("Failed to set {} rule to default value", ruleName);
            }

            Messenger.tell(source, Messenger.f(tr.tr("need_enable_protocol", ruleName), Layout.YELLOW));

            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
