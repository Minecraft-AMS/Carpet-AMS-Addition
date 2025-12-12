/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.commandSetPlayerPose;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.commands.rule.commandSetPlayerPose.SetPlayerPoseCommandRegistry;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private static final Map<String, Pose> POSE_MAPPING = new ConcurrentHashMap<>();

    @SuppressWarnings("PatternVariableCanBeUsed")
    @ModifyReturnValue(method = "getPose", at = @At(value = "RETURN"))
    private Pose doSomePose(Pose original) {
        Entity entity = (Entity) (Object) this;

        if (!Objects.equals(AmsServerSettings.commandSetPlayerPose, "false") && entity instanceof Player) {
            Player player = (Player) entity;
            String poseName = SetPlayerPoseCommandRegistry.DO_POSE_MAP.get(player.getUUID());
            if (poseName != null) {
                return POSE_MAPPING.getOrDefault(poseName, original);
            }
        }

        return original;
    }

    static {
        POSE_MAPPING.put("spin_attack", Pose.SPIN_ATTACK);
        POSE_MAPPING.put("swimming", Pose.SWIMMING);
        POSE_MAPPING.put("sleeping", Pose.SLEEPING);
        POSE_MAPPING.put("fall_flying", Pose.FALL_FLYING);
        POSE_MAPPING.put("standing", Pose.STANDING);
        POSE_MAPPING.put("crouching", Pose.CROUCHING);
        POSE_MAPPING.put("dying", Pose.DYING);
    }
}
