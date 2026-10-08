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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.blockChunkLoader.BlockLoaderTicketTypeAccess;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.server.level.TicketType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TicketType.class, priority = 168)
public abstract class TicketTypeMixin implements BlockLoaderTicketTypeAccess {
    @Unique
    private boolean ams$useBlockLoaderTimeout = false;

    @Override
    public void ams$enableBlockLoaderTimeout() {
        this.ams$useBlockLoaderTimeout = true;
    }

    @ModifyReturnValue(method = "timeout", at = @At("RETURN"))
    private long blockChunkLoaderTimeout(long original) {
        if (this.ams$useBlockLoaderTimeout) {
            return CarpetAMSAdditionSettings.blockChunkLoaderTimeController;
        }

        return original;
    }
}
