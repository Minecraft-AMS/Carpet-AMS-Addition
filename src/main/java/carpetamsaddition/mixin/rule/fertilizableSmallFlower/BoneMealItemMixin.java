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

package carpetamsaddition.mixin.rule.fertilizableSmallFlower;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.WorldUtil;

import net.minecraft.core.BlockPos;
//#if MC>=12005
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
//#endif
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
//#if MC>11605
import net.minecraft.world.level.block.SporeBlossomBlock;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin {
    @Inject(method = "growCrop", at = @At("HEAD"), cancellable = true)
    private static void fertilizeSmallFlower(ItemStack stack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!CarpetAMSAdditionSettings.fertilizableSmallFlower) {
            return;
        }

        Block block = level.getBlockState(pos).getBlock();

        if (!(block instanceof FlowerBlock
            //#if MC>11605
            || block instanceof SporeBlossomBlock
            //#endif
        )) {
            return;
        }

        if (!WorldUtil.isClient(level)) {
            Block.popResource(level, pos, new ItemStack(block, 1));
            stack.shrink(1);
            //#if MC>=12005
            ((ServerLevel) level).sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                15, 0.25, 0.25, 0.25, 0.0
            );
            //#endif
        }

        cir.setReturnValue(true);
    }
}
