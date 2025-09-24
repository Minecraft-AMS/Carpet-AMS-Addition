/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.fakePlayerInteractLikeClient;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("PatternVariableCanBeUsed")
@Mixin(targets = "carpet.helpers.EntityPlayerActionPack$ActionType$1")
public abstract class EntityPlayerActionPackActionTypeMixin {
    @WrapOperation(
        method = "execute(Lnet/minecraft/server/network/ServerPlayerEntity;Lcarpet/helpers/EntityPlayerActionPack$Action;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;interactAt(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"
        )
    )
    private ActionResult onInteractAt(Entity instance, PlayerEntity player, Vec3d hitPos, Hand hand, Operation<ActionResult> original) {
        ActionResult originalResult = original.call(instance, player, hitPos, hand);
        if (AmsServerSettings.fakePlayerInteractLikeClient) {
            if (instance instanceof ArmorStandEntity) {
                ArmorStandEntity stand = (ArmorStandEntity) instance;
                ItemStack handItem = player.getStackInHand(hand);
                if (!stand.isMarker() && handItem.getItem() != Items.NAME_TAG && !player.isSpectator()) {
                    return ActionResult.PASS;
                }
            }
        }
        return originalResult;
    }

    @WrapOperation(
        method = "execute(Lnet/minecraft/server/network/ServerPlayerEntity;Lcarpet/helpers/EntityPlayerActionPack$Action;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;interact(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"
        )
    )
    private ActionResult onInteract(ServerPlayerEntity player, Entity entity, Hand hand, Operation<ActionResult> original) {
        ActionResult originalResult = original.call(player, entity, hand);
        if (AmsServerSettings.fakePlayerInteractLikeClient) {
            if (entity instanceof BoatEntity) {
                BoatEntity boat = (BoatEntity) entity;
                if (!player.shouldCancelInteraction() && ((BoatEntityInvoker) boat).getTicksUnderwater() < 60.0F) {
                    return ActionResult.SUCCESS;
                }
            } else if (entity instanceof MinecartEntity) {
                MinecartEntity minecart = (MinecartEntity) entity;
                if (!player.shouldCancelInteraction() && !minecart.hasPassengers()) {
                    return ActionResult.SUCCESS;
                }
            }
        }
        return originalResult;
    }
}