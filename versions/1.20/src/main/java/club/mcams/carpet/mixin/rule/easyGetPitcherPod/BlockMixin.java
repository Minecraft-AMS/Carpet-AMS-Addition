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

package club.mcams.carpet.mixin.rule.easyGetPitcherPod;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC<12003
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Random;

import static net.minecraft.block.Block.dropStack;

@GameVersion(version = "Minecraft >= 1.20")
@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak(
        World world,
        BlockPos pos,
        BlockState state,
        PlayerEntity player,
        //#if MC<12003
        CallbackInfo ci
        //#else
        //$$ CallbackInfoReturnable<BlockState>cir
        //#endif
    ) {
         if (AmsServerSettings.easyGetPitcherPod != 0 && state.getBlock().equals(Blocks.PITCHER_CROP)) {
             if (!player.isCreative()) {
                 Random random = new Random();
                 int minDrops = 2;
                 int maxDrops = AmsServerSettings.easyGetPitcherPod;
                 int dropCount = minDrops + random.nextInt(maxDrops - minDrops + 1);
                 for (int i = 0; i < dropCount; i++) {
                     ItemStack cakeStack = new ItemStack(Items.PITCHER_POD);
                     dropStack(world, pos, cakeStack);
                 }
             }
         }
    }
}
