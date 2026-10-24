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

package carpetamsaddition.mixin.setting;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.SettingsManager;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.settings.AmsRuleCategory;
import carpetamsaddition.settings.AmsRuleMetadata;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CarpetUtil;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.Noop;
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

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("LoggingSimilarMessage")
@Mixin(SettingsManager.class)
public abstract class Carpet_SettingsManagerMixin {
    @Unique
    private static final String AMS_NETWORK_PROTOCOL = "amsNetworkProtocol";

    @Unique
    private static final Translator tr = new Translator("observer.amsNetworkProtocol");

    @Unique
    private static final Translator lazyRuleTr = new Translator("observer.lazy_rule");

    @Final
    @Shadow
    private Map<String, CarpetRule<?>> rules;

    @Shadow
    private MinecraftServer server;

    @Shadow
    private boolean locked;

    @Shadow
    protected abstract int setDefault(CommandSourceStack source, CarpetRule<?> rule, String stringValue);

    @Inject(method = "setRule", at = @At("HEAD"), cancellable = true)
    private void persistRuleAsDefault(CommandSourceStack source, CarpetRule<?> rule, String newValue, CallbackInfoReturnable<Integer> cir) {
        if (AmsRuleMetadata.mustSetDefault(rule)) {
            cir.setReturnValue(this.setDefault(source, rule, newValue));
        } else if (this.rejectUnavailableNetworkRule(source, rule, newValue)) {
            cir.setReturnValue(0);
        }
    }

    @SuppressWarnings("LocalMayUseName")
    @Inject(method = "setDefault", at = @At("HEAD"), cancellable = true)
    private void prepareLazyDefault(
        CommandSourceStack source, CarpetRule<?> rule, String newValue, CallbackInfoReturnable<Integer> cir,
        @Local(argsOnly = true) LocalRef<String> valueRef
    ) {
        if (!AmsRuleMetadata.shouldDeferLazyRule(rule)) {
            if (this.rejectUnavailableNetworkRule(source, rule, newValue)) cir.setReturnValue(0);
            return;
        }

        String name = CarpetUtil.getRuleName(rule);
        if (this.locked || this.rules.get(name) != rule) {
            cir.setReturnValue(0);
            return;
        }

        if (this.rejectUnavailableNetworkRule(source, rule, newValue)) {
            cir.setReturnValue(0);
            return;
        }

        try {
            String value = AmsRuleMetadata.validateLazyRuleValue(source, rule, newValue);
            if (value == null) {
                cir.setReturnValue(0);
            } else {
                valueRef.set(value);
            }
        } catch (IllegalArgumentException e) {
            if (source != null) Messenger.tell(source, Messenger.f(lazyRuleTr.tr("invalid", name, newValue), Layout.RED));
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "setDefault", at = @At("RETURN"))
    private void notifyLazyDefaultSaved(CommandSourceStack source, CarpetRule<?> rule, String value, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() > 0 && source != null && AmsRuleMetadata.shouldDeferLazyRule(rule)) {
            Messenger.tell(source, Messenger.f(lazyRuleTr.tr("saved", CarpetUtil.getRuleName(rule), value, CarpetUtil.getRuleCurrentValue(rule)), Layout.YELLOW));
        }
    }

    @WrapOperation(
        method = "setDefault",
        at = @At(
            value = "INVOKE",
            //#if MC>=11904
            target = "Lcarpet/api/settings/CarpetRule;set(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)V"
            //#else
            //$$ target = "Lcarpet/settings/ParsedRule;set(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)Lcarpet/settings/ParsedRule;"
            //#endif
        )
    )
    //#if MC>=11904
    private void skipLazyDefaultApplication(CarpetRule<?> rule, CommandSourceStack source, String value, Operation<Void> original) {
        if (AmsRuleMetadata.shouldDeferLazyRule(rule)) {
            Noop.noop();
        } else {
            original.call(rule, source, value);
        }
    }
    //#else
    //$$ private ParsedRule<?> skipLazyDefaultApplication(ParsedRule<?> rule, CommandSourceStack source, String value, Operation<ParsedRule<?>> original) {
    //$$     return AmsRuleMetadata.shouldDeferLazyRule(rule) ? rule : original.call(rule, source, value);
    //$$ }
    //#endif

    @WrapOperation(
        method = "removeDefault",
        at = @At(
            value = "INVOKE",
            //#if MC>=11904
            target = "Lcarpet/api/settings/RuleHelper;resetToDefault(Lcarpet/api/settings/CarpetRule;Lnet/minecraft/commands/CommandSourceStack;)V"
            //#else
            //$$ target = "Lcarpet/settings/ParsedRule;resetToDefault(Lnet/minecraft/commands/CommandSourceStack;)V"
            //#endif
        )
    )
    private void skipLazyDefaultReset(CarpetRule<?> rule, CommandSourceStack source, Operation<Void> original) {
        if (AmsRuleMetadata.shouldDeferLazyRule(rule)) {
            Noop.noop();
        } else {
            original.call(rule, source);
        }
    }

    @Inject(method = "removeDefault", at = @At("RETURN"))
    private void notifyLazyDefaultRemoved(CommandSourceStack source, CarpetRule<?> rule, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() > 0 && source != null && AmsRuleMetadata.shouldDeferLazyRule(rule)) {
            Messenger.tell(source, Messenger.f(lazyRuleTr.tr("removed", CarpetUtil.getRuleName(rule), CarpetUtil.getRuleCurrentValue(rule)), Layout.YELLOW));
        }
    }

    @Inject(method = "loadConfigurationFromConf", at = @At("TAIL"))
    private void resetAmsNetworkRulesOnLoadConfig(CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.amsNetworkProtocol || !this.rules.containsKey(AMS_NETWORK_PROTOCOL)) {
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
                CarpetAMSAdditionServer.LOGGER.error("Failed to set {} rule to default value", ruleName, e);
            }
        }
    }

    @Unique
    private boolean rejectUnavailableNetworkRule(CommandSourceStack source, CarpetRule<?> rule, String newValue) {
        if (
            CarpetAMSAdditionSettings.amsNetworkProtocol
            || (!AmsRuleMetadata.shouldDeferLazyRule(rule) && !MinecraftServerUtil.serverIsRunning())
            || !CarpetUtil.hasCategory(rule, AmsRuleCategory.AMS_NETWORK)
        ) {
            return false;
        }

        String ruleName = CarpetUtil.getRuleName(rule);

        if (AMS_NETWORK_PROTOCOL.equals(ruleName) || Objects.equals(newValue, CarpetUtil.getRuleDefaultValue(rule))) {
            return false;
        }

        if (source != null) {
            Messenger.tell(source, Messenger.f(tr.tr("need_enable_protocol", ruleName), Layout.YELLOW));
        }

        return true;
    }
}
