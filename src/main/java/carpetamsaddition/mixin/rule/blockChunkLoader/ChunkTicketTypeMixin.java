/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.blockChunkLoader;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.MappedRegistry;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("SimplifiableConditionalExpression")
@Mixin(MappedRegistry.class)
public abstract class ChunkTicketTypeMixin {
    @ModifyExpressionValue(
        method = "validateWrite(Lnet/minecraft/resources/ResourceKey;)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/core/MappedRegistry;frozen:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean noFrozen(boolean original, ResourceKey<@NotNull String> key) {
        return key.identifier().getNamespace().equals("carpetamsaddition") ? false : original;
    }
}

