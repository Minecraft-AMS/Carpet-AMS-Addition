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

package club.mcams.carpet.mixin.rule.pleasantAnvil;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
//#if MC>12104
//$$ import net.minecraft.entity.Entity;
//#else
import net.minecraft.entity.player.PlayerEntity;
//#endif
import net.minecraft.item.BlockItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @WrapOperation(
        method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
        at = @At(
            value = "INVOKE",
            //#if MC>12104
            //$$ target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
            //#else
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
            //#endif
        )
    )
    private void modifyAnvilPlaceSound(
        World world,
        //#if MC>12104
        //$$ Entity entity,
        //#else
        PlayerEntity entity,
        //#endif
        BlockPos pos,
        SoundEvent sound, SoundCategory category, float volume, float pitch,
        Operation<Void> original
    ) {
        if (AmsServerSettings.pleasantAnvil && isAnvil(world.getBlockState(pos).getBlock())) {
            world.playSound(entity, pos, sound, SoundCategory.BLOCKS, 2.1024F, 10.24F);
        } else {
            original.call(world, entity, pos, sound, category, volume, pitch);
        }
    }

    @Unique
    private static boolean isAnvil(Block block) {
        return block.equals(Blocks.ANVIL) || block.equals(Blocks.CHIPPED_ANVIL) || block.equals(Blocks.DAMAGED_ANVIL);
    }
}
