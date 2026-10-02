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

package carpetamsaddition.mixin.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper;
import carpetamsaddition.utils.WorldUtil;

import net.minecraft.world.level.block.BellBlock;
//#if MC>=11700
import net.minecraft.world.entity.Entity;
//#endif
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BellBlock.class)
public abstract class BellBlockMixin {
    @Inject(
        //#if MC>=11700
        method = "attemptToRing(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
        //#else
        //$$ method = "attemptToRing(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
        //#endif
        at = @At("HEAD")
    )
    private void ring(
        //#if MC>=11700
        Entity entity,
        //#endif
        Level world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir
    ) {
        if (CarpetAMSAdditionSettings.bellBlockChunkLoader && !WorldUtil.isClient(world)) {
            BlockChunkLoaderHelper.addBellBlockTicket((ServerLevel) world, pos);
        }
    }
}
