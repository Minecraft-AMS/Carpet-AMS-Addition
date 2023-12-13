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

package club.mcams.carpet.mixin.rule.customBlowUpBlock;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Shadow
    @Final
    protected StateManager<Block, BlockState> stateManager;

    @Inject(method = "getBlastResistance", at = @At("HEAD"), cancellable = true)
    private void getBlastResistance(CallbackInfoReturnable<Float> cir) {
        if (!Objects.equals(AmsServerSettings.customBlowUpBlock, "VANILLA") && AmsServerSettings.enhancedWorldEater == -1.0F) {
            Set<String> moreCustomBlowUpBlock = new HashSet<>(Arrays.asList(AmsServerSettings.customBlowUpBlock.split(",")));
            String blockName = stateManager.getDefaultState().getBlock().toString();
            String regex = "\\{(.*?)}";   //Block{minecraft:bedrock} -> minecraft:bedrock
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(blockName);
            if (matcher.find()) {
                blockName = matcher.group(1);
            }
            if (moreCustomBlowUpBlock.contains(blockName)) {
                cir.setReturnValue(Blocks.STONE.getBlastResistance());
            }
        }
    }
}
