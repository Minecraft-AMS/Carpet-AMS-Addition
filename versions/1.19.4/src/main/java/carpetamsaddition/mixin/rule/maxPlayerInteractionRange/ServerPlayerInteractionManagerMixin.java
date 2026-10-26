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

import carpetamsaddition.helpers.rule.maxPlayerInteractionRange.InteractionRangeHelper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.util.Mth;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.20.5")
@Mixin(value = ServerPlayerGameMode.class, priority = 168)
public abstract class ServerPlayerInteractionManagerMixin {
    @Final
    @Shadow
    protected ServerPlayer player;

    @Shadow
    protected ServerLevel level;

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void enforceBlockBreakDistance(
        BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int worldHeight,
        //#if MC>=11900
        int sequence,
        //#endif
        CallbackInfo ci
    ) {
        if (action != ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK && !InteractionRangeHelper.withinBlockRange(this.player, pos)) {
            this.player.connection.send(new ClientboundBlockUpdatePacket(this.level, pos));
            ci.cancel();
        }
    }

    @WrapOperation(
        method = "handleBlockBreakAction",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D",
            opcode = Opcodes.GETSTATIC
        )
    )
    private double modifyBlockBreakDistance(Operation<Double> original) {
        if (InteractionRangeHelper.blockRange() != -1.0D) {
            return Mth.square(InteractionRangeHelper.blockRange());
        } else {
            return original.call();
        }
    }
}
