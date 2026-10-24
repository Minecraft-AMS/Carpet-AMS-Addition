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

package carpetamsaddition.mixin.rule.enderPearlRestoreFix;

import carpetamsaddition.helpers.rule.enderPearlRestoreFix.EntitySectionLookup;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.UUID;

@GameVersion(version = "Minecraft >= 1.21.8")
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements EntitySectionLookup {
    @Shadow
    @Final
    private PersistentEntitySectionManager<Entity> entityManager;

    @Override
    @Unique
    public boolean ams$isEntityLoaded(UUID uuid) {
        return this.entityManager.isLoaded(uuid);
    }

    @Override
    @Unique
    public EntitySectionStorage<Entity> ams$getSectionStorage() {
        return ((PersistentEntitySectionManagerAccessor) this.entityManager).ams$getSectionStorage();
    }
}
