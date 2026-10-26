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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.MathUtil;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft <= 1.19")
@Mixin({
    AbstractFurnaceBlockEntity.class,
    BrewingStandBlockEntity.class,
    EnderChestBlockEntity.class,
    RandomizableContainerBlockEntity.class,
})
public abstract class BlockEntityContainerMixin {
    @ModifyReturnValue(method = "stillValid", at = @At("RETURN"))
    private boolean modifyStillValidDistance(boolean original, Player player) {
        if (CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange != -1.0D) {
            BlockEntity blockEntity = (BlockEntity) (Object) this;
            Level level = blockEntity.getLevel();
            BlockPos pos = blockEntity.getBlockPos();
            return level != null && level.getBlockEntity(pos) == blockEntity && player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= MathUtil.square(CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange + 4.0D);
        } else {
            return original;
        }
    }
}
