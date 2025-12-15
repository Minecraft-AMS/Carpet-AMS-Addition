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

package carpetamsaddition.mixin.rule.commandCustomBlockHardness;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(value = BlockStateBase.class, priority = 16888)
public abstract class BlockStateBaseMixin implements BlockStateBaseInvoker {
    @ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
    private float modifyHardness(float original) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandCustomBlockHardness, "false")) {
            BlockState blockState = this.invokerGetBlock().defaultBlockState();
            return CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.getOrDefault(blockState, original);
        } else {
            return original;
        }
    }
}
