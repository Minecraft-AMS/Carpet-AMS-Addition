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

package club.mcams.carpet.mixin.rule.customBlockUpdateSuppressor;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.customBlockUpdateSuppressor.BlockUpdateSuppressorExceptionHelper;
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommandRegistry;

import net.minecraft.block.AbstractBlock;
//#if MC>=12102
//$$ import net.minecraft.world.block.WireOrientation;
//#endif
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    private void neighborUpdate(
        BlockState state, World world, BlockPos pos, Block block,
        //#if MC>=12102
        //$$ WireOrientation wireOrientation,
        //#else
        BlockPos fromPos,
        //#endif
        boolean notify, CallbackInfo ci
    ) {
        if (!Objects.equals(AmsServerSettings.customBlockUpdateSuppressor, "none")) {
            if (AmsUpdateSuppressionCrashFixCommandRegistry.amsUpdateSuppressionCrashFixForceMode) {
                AmsServerSettings.amsUpdateSuppressionCrashFix = "true";
            }
            String blockName = RegexTools.getBlockRegisterName(state.getBlock().toString()); // Block{minecraft:bedrock} -> minecraft:bedrock
            if (Objects.equals(AmsServerSettings.customBlockUpdateSuppressor, blockName)) {
                BlockUpdateSuppressorExceptionHelper.getInstance().throwThrowableSuppression();
            }
        }
    }
}