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

package club.mcams.carpet.mixin.network;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
//#if MC>=11900
//$$ import org.spongepowered.asm.mixin.Final;
//#endif
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"LoggingSimilarMessage", "PatternVariableCanBeUsed"})
@Mixin(SettingsManager.class)
public abstract class Carpet_SettingsManagerMixin {
    //#if MC>=11900
    //$$ @Final
    //#endif
    @Shadow
    private Map<String, ParsedRule<?>> rules;

    @Shadow
    private MinecraftServer server;

    @Shadow
    protected abstract int setDefault(ServerCommandSource source, ParsedRule<?> parsedRule, String value);

    @Unique
    private final Translator tr = new Translator("observer.amsNetworkProtocol");

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void collectAmsNetworkRuleNames(CallbackInfo ci) {
        NetworkUtil.collectAmsNetworkRuleNames();
    }

    @Inject(method = "loadConfigurationFromConf", at = @At("TAIL"))
    private void resetAmsNetworkRulesOnLoadConfig(CallbackInfo ci) {
        ParsedRule<?> amsNetworkProtocolRule = rules.get("amsNetworkProtocol");

        if (amsNetworkProtocolRule == null) {
            return;
        }

        Object ruleValue = amsNetworkProtocolRule.get();

        if (!(ruleValue instanceof Boolean)) {
            return;
        }

        Boolean isAmsNetworkProtocolEnabled = (Boolean) ruleValue;

        if (!isAmsNetworkProtocolEnabled) {
            ServerCommandSource source = server.getCommandSource();

            for (String ruleName : NetworkUtil.AMS_NETWORK_RULE_NAMES) {
                ParsedRule<?> targetRule = rules.get(ruleName);

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
                    AmsServer.LOGGER.error("Failed to set {} rule to default value", ruleName);
                }
            }
        }
    }

    @Inject(method = {"setRule", "setDefault"}, at = @At("HEAD"), cancellable = true)
    private void resetAmsNetworkRulesOnSetRule(ServerCommandSource source, ParsedRule<?> rule, String newValue, CallbackInfoReturnable<Integer> cir) {
        ParsedRule<?> amsNetworkProtocolRule = rules.get("amsNetworkProtocol");
        String ruleName = CarpetUtil.getRuleName(rule);

        if (!NetworkUtil.AMS_NETWORK_RULE_NAMES.contains(ruleName) || rule.equals(amsNetworkProtocolRule)) {
            return;
        }

        if (!AmsServerSettings.amsNetworkProtocol && !Objects.equals(newValue, CarpetUtil.getRuleDefaultValue(rule)) && MinecraftServerUtil.serverIsRunning()) {
            try {
                rule.set(source, CarpetUtil.getRuleDefaultValue(rule));
            } catch (Exception e) {
                AmsServer.LOGGER.error("Failed to set {} rule to default value", ruleName);
            }

            Messenger.tell(source, Messenger.f(tr.tr("need_enable_protocol", ruleName), Layout.YELLOW));

            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
