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

package carpetamsaddition.mixin.rule.maxPlayerInteractionRange;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.MathUtil;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.20.5")
@Mixin(value = GameRenderer.class, priority = 168)
public abstract class GameRendererMixin {
    //#if MC>11800
    @Shadow
    @Final
    Minecraft minecraft;
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ private Minecraft minecraft;
    //#endif

    @ModifyExpressionValue(method = "pick", at = @At(value = "CONSTANT", args = "doubleValue=6.0D"))
    private double pick1(double constant) {
        if (CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange != -1.0D && this.minecraft.player != null) {
            return MathUtil.square(CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange);
        }
        return constant;
    }

    @ModifyExpressionValue(method = "pick", at = @At(value = "CONSTANT", args = "doubleValue=3.0D"))
    private double pick2(double constant) {
        if (CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange != -1.0D && this.minecraft.player != null) {
            return MathUtil.square(CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange);
        } else {
            return constant;
        }
    }

    @ModifyExpressionValue(method = "pick", at = @At(value = "CONSTANT", args = "doubleValue=9.0D"))
    private double pick3(double constant) {
        if (CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange != -1.0D && this.minecraft.player != null) {
            return MathUtil.square(CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange);
        } else {
            return constant;
        }
    }
}
