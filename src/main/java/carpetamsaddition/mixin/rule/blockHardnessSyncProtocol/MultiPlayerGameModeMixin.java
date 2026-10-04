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

package carpetamsaddition.mixin.rule.blockHardnessSyncProtocol;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.network.payloads.rule.blockHardnessSyncProtocol.BlockHardnessSyncPayload_C2S;
import carpetamsaddition.utils.NetworkUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Inject(method = "startDestroyBlock", at = @At("HEAD"))
    private void requestServerHardness(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAMSAdditionSettings.blockHardnessSyncProtocol) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;

            if (player == null || minecraft.level == null) {
                return;
            }

            BlockState state = minecraft.level.getBlockState(pos);
            float clientHardness = state.getDestroySpeed(minecraft.level, pos);
            NetworkUtil.sendC2SPacket(player, BlockHardnessSyncPayload_C2S.create(player.getUUID(), pos, clientHardness), NetworkUtil.SendMode.NEED_SUPPORT);
        }
    }
}
