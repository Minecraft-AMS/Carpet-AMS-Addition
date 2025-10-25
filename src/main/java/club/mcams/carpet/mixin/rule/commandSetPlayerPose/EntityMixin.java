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

package club.mcams.carpet.mixin.rule.commandSetPlayerPose;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.rule.commandSetPlayerPose.SetPlayerPoseCommandRegistry;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private static final Map<String, EntityPose> POSE_MAPPING = new ConcurrentHashMap<>();

    @SuppressWarnings("PatternVariableCanBeUsed")
    @ModifyReturnValue(method = "getPose", at = @At(value = "RETURN"))
    private EntityPose doSomePose(EntityPose original) {
        Entity entity = (Entity) (Object) this;

        if (!Objects.equals(AmsServerSettings.commandSetPlayerPose, "false") && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            String poseName = SetPlayerPoseCommandRegistry.DO_POSE_MAP.get(player.getUuid());
            if (poseName != null) {
                return POSE_MAPPING.getOrDefault(poseName, original);
            }
        }

        return original;
    }

    static {
        POSE_MAPPING.put("spin_attack", EntityPose.SPIN_ATTACK);
        POSE_MAPPING.put("swimming", EntityPose.SWIMMING);
        POSE_MAPPING.put("sleeping", EntityPose.SLEEPING);
        POSE_MAPPING.put("fall_flying", EntityPose.FALL_FLYING);
        POSE_MAPPING.put("standing", EntityPose.STANDING);
        POSE_MAPPING.put("crouching", EntityPose.CROUCHING);
        POSE_MAPPING.put("dying", EntityPose.DYING);
        //#if MC>=11700
        POSE_MAPPING.put("long_jumping", EntityPose.LONG_JUMPING);
        //#endif
    }
}
