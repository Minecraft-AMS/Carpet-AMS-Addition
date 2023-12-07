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

package club.mcams.carpet.mixin.rule.enhancedWorldEater;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Shadow
    @Final
    protected StateManager<Block, BlockState> stateManager;

    @Inject(method = "getBlastResistance", at = @At("RETURN"), cancellable = true)
    private void getBlastResistance(CallbackInfoReturnable<Float> cir) {
        if (AmsServerSettings.enhancedWorldEater != -1.0D) {
            Block block = stateManager.getDefaultState().getBlock();
            float BOOM = (float) AmsServerSettings.enhancedWorldEater;
            float startBlastResistance = cir.getReturnValue();
            if (
                startBlastResistance >= 17.0F &&
                block != Blocks.BEDROCK &&
                block != Blocks.ANVIL &&
                block != Blocks.CHIPPED_ANVIL &&
                block != Blocks.DAMAGED_ANVIL &&
                block != Blocks.END_PORTAL_FRAME &&
                block != Blocks.END_PORTAL &&
                block != Blocks.END_GATEWAY
            ) {
                cir.setReturnValue(BOOM);
            }
        }
    }
}
