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

package carpetamsaddition.mixin.rule.customBlockUpdateSuppressor;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.customBlockUpdateSuppressor.BlockUpdateSuppressorExceptionHelper;
import carpetamsaddition.utils.RegexTools;
import carpetamsaddition.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommandRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(method = "neighborChanged", at = @At("HEAD"))
    private void neighborUpdate(BlockState state, Level world, BlockPos pos, Block block, Orientation wireOrientation, boolean notify, CallbackInfo ci) {
        if (!Objects.equals(CarpetAMSAdditionSettings.customBlockUpdateSuppressor, "none")) {
            if (AmsUpdateSuppressionCrashFixCommandRegistry.amsUpdateSuppressionCrashFixForceMode) {
                CarpetAMSAdditionSettings.amsUpdateSuppressionCrashFix = "true";
            }
            String blockName = RegexTools.getBlockRegisterName(state.getBlock().toString()); // Block{minecraft:bedrock} -> minecraft:bedrock
            if (Objects.equals(CarpetAMSAdditionSettings.customBlockUpdateSuppressor, blockName)) {
                BlockUpdateSuppressorExceptionHelper.getInstance().throwThrowableSuppression();
            }
        }
    }
}