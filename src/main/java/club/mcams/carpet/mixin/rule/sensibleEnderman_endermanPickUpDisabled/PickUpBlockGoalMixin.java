/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.sensibleEnderman_endermanPickUpDisabled;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
//#if MC>=11904
//$$ import net.minecraft.registry.tag.TagKey;
//#elseif MC>=11800 && MC<11900
import net.minecraft.tag.TagKey;
//#else
//$$ import net.minecraft.tag.Tag;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/entity/mob/EndermanEntity$PickUpBlockGoal")
public abstract class PickUpBlockGoalMixin {
    @WrapOperation(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target =
            //#if MC<11700
            //$$ "Lnet/minecraft/block/Block;isIn(Lnet/minecraft/tag/Tag;)Z"
            //#elseif MC<11904 && MC>=11800
            "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/TagKey;)Z"
            //#elseif MC<11904
            //$$ "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/Tag;)Z"
            //#else
            //$$ "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
            //#endif
        )
    )
    private boolean isBlockInTag(
         //#if MC<11700
         //$$ Block block,
         //#else
         BlockState blockState,
         //#endif
         //#if MC>=11800 && MC<11900
         TagKey<Block> tag,
         //#elseif MC<11904
         //$$ Tag<Block> tag,
         //#else
         //$$ TagKey<Block> tag,
         //#endif
         Operation<Boolean> original
    ) {
        if (AmsServerSettings.sensibleEnderman) {
            //#if MC>=11700
            Block block = blockState.getBlock();
            //#endif
            return block.equals(Blocks.MELON) || block.equals(Blocks.CARVED_PUMPKIN);
        } else {
            //#if MC>=11700
            return original.call(blockState, tag);
            //#else
            //$$ return original.call(block, tag);
            //#endif
        }
    }

    @ModifyReturnValue(method = "canStart", at = @At("RETURN"))
    private boolean canStart(boolean original) {
        if (AmsServerSettings.endermanPickUpDisabled) {
            return false;
        } else {
            return original;
        }
    }
}
