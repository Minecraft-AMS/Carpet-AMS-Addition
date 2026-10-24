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

package carpetamsaddition.mixin.rule.commandLocationTag;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.commandLocationTag.LocationTagManager;

import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
    @Inject(method = "setLoaded", at = @At("TAIL"))
    private void locationTagLoaded(boolean loaded, CallbackInfo ci) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandLocationTag, "false")) {
            LocationTagManager.chunkLoaded((LevelChunk) (Object) this, loaded);
        }
    }

    @Inject(method = {"setBlockEntity", "removeBlockEntity"}, at = @At("TAIL"))
    private void locationTagChanged(CallbackInfo ci) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandLocationTag, "false")) {
            LocationTagManager.changed((LevelChunk) (Object) this);
        }
    }
}
