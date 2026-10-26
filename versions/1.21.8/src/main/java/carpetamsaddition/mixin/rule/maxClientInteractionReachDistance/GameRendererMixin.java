/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.mixin.rule.maxClientInteractionReachDistance;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.renderer.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.20.5 && Minecraft < 1.21.11")
@Mixin(value = GameRenderer.class, priority = 1688)
public abstract class GameRendererMixin {
    @ModifyExpressionValue(
        method = "pick(F)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;blockInteractionRange()D")
    )
    private double modifyBlockInteractionRange(double original) {
        double range = CarpetAMSAdditionSettings.maxClientInteractionReachDistance;
        return range != -1.0D ? range : original;
    }

    @ModifyExpressionValue(
        method = "pick(F)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;entityInteractionRange()D")
    )
    private double modifyEntityInteractionRange(double original) {
        double range = CarpetAMSAdditionSettings.maxClientInteractionReachDistance;
        return range != -1.0D ? range : original;
    }
}
