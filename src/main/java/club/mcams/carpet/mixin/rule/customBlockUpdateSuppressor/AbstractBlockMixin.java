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

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static club.mcams.carpet.commands.rule.amsUpdateSuppressionCrashFix.amsUpdateSuppressionCrashFixCommandRegistry.amsUpdateSuppressionCrashFixForceMode;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    private void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify, CallbackInfo ci) {
        if (!Objects.equals(AmsServerSettings.customBlockUpdateSuppressor, "none")) {
            if (amsUpdateSuppressionCrashFixForceMode) {
                AmsServerSettings.amsUpdateSuppressionCrashFix = true;
            }
            String blockName1 = state.getBlock().toString();
            String blockName2 = null;
            String regex = "\\{(.*?)\\}";   //Block{minecraft:bedrock} -> minecraft:bedrock
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(blockName1);
            if (matcher.find()) {
                blockName2 = matcher.group(1);
            }
            if (Objects.equals(AmsServerSettings.customBlockUpdateSuppressor, blockName2)) {
                //#if MC<11900
                throw new StackOverflowError("[Carpet-AMS-Addition]: StackOverflowError");
                //#else
                //$$ throw new ClassCastException("[Carpet-AMS-Addition]: ClassCastException");
                //#endif
            }
        }
    }
}