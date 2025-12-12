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

package carpetamsaddition.mixin.rule.easyGetPitcherPod;

import carpetamsaddition.AmsServerSettings;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Random;

@GameVersion(version = "Minecraft >= 1.20")
@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void onBreak(Level world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
         if (AmsServerSettings.easyGetPitcherPod != 0 && state.getBlock().equals(Blocks.PITCHER_CROP)) {
             if (!player.isCreative()) {
                 Random random = new Random();
                 int minDrops = 2;
                 int maxDrops = AmsServerSettings.easyGetPitcherPod;
                 int dropCount = minDrops + random.nextInt(maxDrops - minDrops + 1);
                 for (int i = 0; i < dropCount; i++) {
                     ItemStack pitcherPodStack = new ItemStack(Items.PITCHER_POD);
                     Block.popResource(world, pos, pitcherPodStack);
                 }
             }
         }
    }
}
