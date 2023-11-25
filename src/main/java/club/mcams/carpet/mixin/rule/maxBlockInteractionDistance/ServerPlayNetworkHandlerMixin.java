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

package club.mcams.carpet.mixin.rule.maxBlockInteractionDistance;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.maxBlockInteractionDistance.MaxInteractionDistanceMathHelper;

//#if MC>11800
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import org.spongepowered.asm.mixin.injection.At;
//#endif

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    private Vec3d requestedTeleportPos;

    @Shadow
    public ServerPlayerEntity player;

    @Inject(
            method = "onPlayerInteractBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;squaredDistanceTo(DDD)D"
            )
    )
    private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
        if (AmsServerSettings.maxBlockInteractionDistance != -1) {
            BlockHitResult blockHitResult = packet.getBlockHitResult();
            BlockPos blockPos = blockHitResult.getBlockPos();
            //#if MC>=12000
            //$$ ServerWorld serverWorld = this.player.getServerWorld();
            //#else
            ServerWorld serverWorld = this.player.getWorld();
            //#endif
            Hand hand = packet.getHand();
            ItemStack itemStack = this.player.getStackInHand(hand);
            if (this.requestedTeleportPos == null && this.player.squaredDistanceTo((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5) < MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance() && serverWorld.canPlayerModifyAt(this.player, blockPos)) {
                this.player.interactionManager.interactBlock(this.player, serverWorld, itemStack, hand, blockHitResult);
            }
        }
    }
    //#if MC>11800
    //$$ @WrapOperation(
    //$$         method = "onPlayerInteractBlock",
    //$$         at = @At(
    //$$         value = "FIELD",
    //$$         target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"
    //$$         )
    //$$ )
    //$$ private double getActualReachDistance(Operation<Double> original) {
    //$$     if (AmsServerSettings.maxBlockInteractionDistance == -1.0) {
    //$$         return original.call();
    //$$     } else {
    //$$         return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
    //$$     }
    //$$ }
    //#endif
}
