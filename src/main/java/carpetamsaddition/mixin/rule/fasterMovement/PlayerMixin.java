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

package carpetamsaddition.mixin.rule.fasterMovement;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.EntityUtil;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
    private void getMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (!Objects.equals(CarpetAMSAdditionSettings.fasterMovement, "VANILLA")) {
            Player player = (Player)(Object)this;
            Level world = EntityUtil.getEntityWorld(player);

            if (
                (CarpetAMSAdditionSettings.fasterMovementController == CarpetAMSAdditionSettings.fasterMovementDimension.END && world.dimension() == Level.END) ||
                (CarpetAMSAdditionSettings.fasterMovementController == CarpetAMSAdditionSettings.fasterMovementDimension.NETHER && world.dimension() == Level.NETHER) ||
                (CarpetAMSAdditionSettings.fasterMovementController == CarpetAMSAdditionSettings.fasterMovementDimension.OVERWORLD  && world.dimension() == Level.OVERWORLD) ||
                (CarpetAMSAdditionSettings.fasterMovementController == CarpetAMSAdditionSettings.fasterMovementDimension.ALL)
            ) {

                float speed = (float) player.getAttributeValue(Attributes.MOVEMENT_SPEED);

                switch (CarpetAMSAdditionSettings.fasterMovement) {
                    case "Ⅰ":
                        speed = 0.2F;
                        break;
                    case "Ⅱ":
                        speed = 0.3F;
                        break;
                    case "Ⅲ":
                        speed = 0.4F;
                        break;
                    case "Ⅳ":
                        speed = 0.5F;
                        break;
                    case "Ⅴ":
                        speed = 0.6F;
                        break;
                    default:
                        break;
                }

                cir.setReturnValue(speed);
            }
        }
    }
}
