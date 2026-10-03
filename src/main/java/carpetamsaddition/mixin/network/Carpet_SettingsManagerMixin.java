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
import carpetamsaddition.settings.AmsRuleCategory;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CarpetUtil;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.messenger.Messenger;

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

@SuppressWarnings("LoggingSimilarMessage")
@Mixin(SettingsManager.class)
public abstract class Carpet_SettingsManagerMixin {
    @Unique
    private static final String AMS_NETWORK_PROTOCOL = "amsNetworkProtocol";

    @Unique
    private static final Translator tr = new Translator("observer.amsNetworkProtocol");

    @Final
    @Shadow
    private Map<String, CarpetRule<?>> rules;

    @Shadow
    private MinecraftServer server;

    @Shadow
    protected abstract int setDefault(CommandSourceStack source, CarpetRule<?> parsedRule, String value);

    @Inject(method = "loadConfigurationFromConf", at = @At("TAIL"))
    private void resetAmsNetworkRulesOnLoadConfig(CallbackInfo ci) {
        CarpetRule<?> protocolRule = this.rules.get(AMS_NETWORK_PROTOCOL);
        Object protocolValue = protocolRule == null ? null : protocolRule.value();
        if (!(protocolValue instanceof Boolean) || (Boolean) protocolValue) {
            return;
        }

        CommandSourceStack source = this.server.createCommandSourceStack();

        for (CarpetRule<?> rule : this.rules.values()) {
            String ruleName = CarpetUtil.getRuleName(rule);
            if (AMS_NETWORK_PROTOCOL.equals(ruleName) || !CarpetUtil.hasCategory(rule, AmsRuleCategory.AMS_NETWORK)) {
                continue;
            }

            String defaultValue = CarpetUtil.getRuleDefaultValue(rule);
            if (Objects.equals(CarpetUtil.getRuleCurrentValue(rule), defaultValue)) {
                continue;
            }

            try {
                this.setDefault(source, rule, defaultValue);
            } catch (Exception e) {
                CarpetAMSAdditionServer.LOGGER.error("Failed to set {} rule to default value", ruleName);
            }
        }
    }

    @Inject(method = {"setRule", "setDefault"}, at = @At("HEAD"), cancellable = true)
    private void resetAmsNetworkRulesOnSetRule(CommandSourceStack source, CarpetRule<?> rule, String newValue, CallbackInfoReturnable<Integer> cir) {
        String ruleName = CarpetUtil.getRuleName(rule);
        String defaultValue = CarpetUtil.getRuleDefaultValue(rule);

        if (
            CarpetAMSAdditionSettings.amsNetworkProtocol
            || !MinecraftServerUtil.serverIsRunning()
            || AMS_NETWORK_PROTOCOL.equals(ruleName)
            || !CarpetUtil.hasCategory(rule, AmsRuleCategory.AMS_NETWORK)
            || Objects.equals(newValue, defaultValue)
        ) {
            return;
        }

        try {
            rule.set(source, defaultValue);
        } catch (Exception e) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to set {} rule to default value", ruleName);
        }

        Messenger.tell(source, Messenger.f(tr.tr("need_enable_protocol", ruleName), Layout.YELLOW));

        cir.setReturnValue(0);
    }
}
