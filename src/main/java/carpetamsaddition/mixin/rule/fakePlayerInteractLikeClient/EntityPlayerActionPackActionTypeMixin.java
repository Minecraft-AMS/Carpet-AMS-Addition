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

package carpetamsaddition.mixin.rule.fakePlayerInteractLikeClient;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.Raft;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "carpet/helpers/EntityPlayerActionPack$ActionType$1")
public abstract class EntityPlayerActionPackActionTypeMixin {
    @WrapOperation(
        method = "execute(Lnet/minecraft/server/level/ServerPlayer;Lcarpet/helpers/EntityPlayerActionPack$Action;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/InteractionResult;"
        )
    )
    private InteractionResult onInteractAt(Entity entity, Player player, InteractionHand hand, Vec3 hitPos, Operation<InteractionResult> original) {
        InteractionResult originalResult = original.call(entity, player, hand, hitPos);

        if (CarpetAMSAdditionSettings.fakePlayerInteractLikeClient) {
            if (entity instanceof ArmorStand stand) {
                ItemStack handItem = player.getItemInHand(hand);
                if (!stand.isMarker() && handItem.getItem() != Items.NAME_TAG && !player.isSpectator()) {
                    return InteractionResult.PASS;
                }
            }
        }

        return originalResult;
    }

    @WrapOperation(
        method = "execute(Lnet/minecraft/server/level/ServerPlayer;Lcarpet/helpers/EntityPlayerActionPack$Action;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/InteractionResult;"
        )
    )
    private InteractionResult onInteract(ServerPlayer player, Entity entity, InteractionHand hand, Vec3 hitPos, Operation<InteractionResult> original) {
        InteractionResult originalResult = original.call(player, entity, hand, hitPos);

        if (CarpetAMSAdditionSettings.fakePlayerInteractLikeClient) {
            if (entity instanceof Boat boat) {
                if (!player.isSecondaryUseActive() && ((AbstractBoatInvoker) boat).getOutOfControlTicks() < 60.0F) {
                    return InteractionResult.SUCCESS;
                }
            } else if (entity instanceof Minecart minecart) {
                if (!player.isSecondaryUseActive() && !minecart.isVehicle()) {
                    return InteractionResult.SUCCESS;
                }
            } else if (entity instanceof Raft raft) {
                if (!player.isSecondaryUseActive() && ((AbstractBoatInvoker) raft).getOutOfControlTicks() < 60.0F) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return originalResult;
    }
}
