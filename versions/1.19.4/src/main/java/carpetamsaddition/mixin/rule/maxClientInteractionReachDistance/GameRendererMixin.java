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
 */

package carpetamsaddition.mixin.rule.maxClientInteractionReachDistance;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.20.5")
@Mixin(value = GameRenderer.class, priority = 1688)
public abstract class GameRendererMixin {
    @ModifyConstant(method = "pick", constant = @Constant(doubleValue = 6.0D))
    private double modifyCreativeEntityPickRange(double original) {
        if (CarpetAMSAdditionSettings.maxClientInteractionReachDistance != -1.0D) {
            return CarpetAMSAdditionSettings.maxClientInteractionReachDistance;
        }

        return original;
    }

    @ModifyConstant(method = "pick", constant = @Constant(doubleValue = 9.0D))
    private double modifySurvivalEntityPickRangeLimit(double original) {
        if (CarpetAMSAdditionSettings.maxClientInteractionReachDistance != -1.0D) {
            return Mth.square(CarpetAMSAdditionSettings.maxClientInteractionReachDistance);
        }

        return original;
    }
}
