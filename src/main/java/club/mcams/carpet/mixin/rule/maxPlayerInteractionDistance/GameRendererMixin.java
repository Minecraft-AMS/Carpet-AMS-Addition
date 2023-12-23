/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.mixin.rule.maxPlayerInteractionDistance;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.maxPlayerInteractionDistance_maxClientInteractionReachDistance.MaxInteractionDistanceMathHelper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = GameRenderer.class, priority = 168)
public abstract class GameRendererMixin {

    //#if MC>11800
    //$$ @Shadow
    //$$ @Final
    //$$ MinecraftClient client;
    //#else
    @Shadow
    @Final
    private MinecraftClient client;
    //#endif

    @ModifyConstant(
            method = "updateTargetedEntity",
            require = 0,
            allow = 1,
            constant = @Constant(doubleValue = 6.0D)
    )
    private double updateTargetedEntity1(final double constant) {
        if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D && this.client.player != null) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        }
        return constant;
    }

    //#if MC<12003
    @ModifyConstant(
            method = "updateTargetedEntity",
            require = 0,
            constant = @Constant(doubleValue = 3.0D)
    )
    private double updateTargetedEntity2(final double constant) {
        if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D && this.client.player != null) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        } else {
            return constant;
        }
    }
    //#endif

    @ModifyConstant(
            method = "updateTargetedEntity",
            require = 0,
            constant = @Constant(doubleValue = 9.0D)
    )
    private double updateTargetedEntity3(final double constant) {
        if (AmsServerSettings.maxPlayerInteractionDistance != -1.0D && this.client.player != null) {
            return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
        } else {
            return constant;
        }
    }
}
