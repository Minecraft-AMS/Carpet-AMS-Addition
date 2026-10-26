/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.mixin.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.blockChunkLoader.BlockChunkLoaderManager;
import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderType;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = PistonBaseBlock.class, priority = 168)
public abstract class PistonBaseBlockMixin {
    @Inject(method = "triggerEvent", at = @At("HEAD"))
    private void onSyncedBlockEvent(BlockState state, Level level, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandPistonChunkloader, "false") && level instanceof ServerLevel) {
            BlockChunkLoaderManager.trigger(LoaderType.PISTON, (ServerLevel) level, pos, pos.relative(state.getValue(PistonBaseBlock.FACING)));
        }
    }
}
