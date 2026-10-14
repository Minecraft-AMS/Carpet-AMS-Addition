/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderType;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfigStore;
import carpetamsaddition.helpers.rule.blockChunkLoader.BlockLoaderTicketTypeAccess;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.server.level.TicketType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TicketType.class, priority = 168)
public abstract class TicketTypeMixin implements BlockLoaderTicketTypeAccess {
    @Unique
    private LoaderType ams$loaderType;

    @Override
    public void ams$setLoaderType(LoaderType type) {
        this.ams$loaderType = type;
    }

    @ModifyReturnValue(method = "timeout", at = @At("RETURN"))
    private long blockChunkLoaderTimeout(long original) {
        return this.ams$loaderType == null ? original : BlockLoaderConfigStore.get(this.ams$loaderType).time;
    }
}
