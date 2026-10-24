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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = NoteBlock.class, priority = 168)
public abstract class NoteBlockMixin {
    @ModifyExpressionValue(
        method = "playNote",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"
        )
    )
    private boolean allowLoader(boolean original, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos) {
        if (Objects.equals(CarpetAMSAdditionSettings.commandNoteblockChunkloader, "false")) {
            return original;
        }

        return original || level instanceof ServerLevel && BlockChunkLoaderManager.matches(LoaderType.NOTEBLOCK, (ServerLevel) level, pos);
    }

    @Inject(method = "triggerEvent", at = @At("HEAD"), cancellable = true)
    private void onTriggerEvent(BlockState state, Level level, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(CarpetAMSAdditionSettings.commandNoteblockChunkloader, "false")) {
            return;
        }

        if (level instanceof ServerLevel) {
            BlockChunkLoaderManager.trigger(LoaderType.NOTEBLOCK, (ServerLevel) level, pos, pos);
        }

        //#if MC>=12000
        if (state.getValue(NoteBlock.INSTRUMENT).worksAboveNoteBlock()) {
            return;
        }
        //#elseif MC>=11904
        //$$ if (!state.getValue(NoteBlock.INSTRUMENT).requiresAirAbove()) {
        //$$     return;
        //$$ }
        //#endif

        if (!level.getBlockState(pos.above()).isAir()) {
            cir.setReturnValue(false);
        }
    }
}
