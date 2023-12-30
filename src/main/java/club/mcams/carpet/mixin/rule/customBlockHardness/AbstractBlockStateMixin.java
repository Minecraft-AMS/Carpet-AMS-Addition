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

package club.mcams.carpet.mixin.rule.customBlockHardness;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static club.mcams.carpet.commands.rule.customBlockHardness.CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP;

@Mixin(AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getHardness", at = @At("HEAD"), cancellable = true)
    private void getHardness(CallbackInfoReturnable<Float> cir) {
        if (!Objects.equals(AmsServerSettings.customBlockHardness, "false")) {
            BlockState blockState = this.getBlock().getDefaultState();
            if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(blockState)) {
                cir.setReturnValue(CUSTOM_BLOCK_HARDNESS_MAP.get(blockState));
            }
        }
    }
}
