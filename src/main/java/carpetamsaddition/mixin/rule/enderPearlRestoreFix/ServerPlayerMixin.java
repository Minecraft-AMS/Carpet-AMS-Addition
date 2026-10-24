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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.enderPearlRestoreFix.EntitySectionLookup;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
//#if MC>=12111
import net.minecraft.world.entity.EntityProcessor;
//#else
//$$ import java.util.function.Function;
//#endif
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.storage.ValueInput;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Iterator;
import java.util.UUID;

@GameVersion(version = "Minecraft >= 1.21.8")
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @WrapOperation(
        method = "loadAndSpawnEnderPearl",
        at = @At(
            value = "INVOKE",
            //#if MC>=12111
            target = "Lnet/minecraft/world/entity/EntityType;loadEntityRecursive(Lnet/minecraft/world/level/storage/ValueInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/EntityProcessor;)Lnet/minecraft/world/entity/Entity;"
            //#else
            //$$ target = "Lnet/minecraft/world/entity/EntityType;loadEntityRecursive(Lnet/minecraft/world/level/storage/ValueInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;Ljava/util/function/Function;)Lnet/minecraft/world/entity/Entity;"
            //#endif
        )
    )
    private Entity reuseRestoredEnderPearl(
        ValueInput input, Level level, EntitySpawnReason reason,
        //#if MC>=12111
        EntityProcessor loader,
        //#else
        //$$ Function<Entity, Entity> loader,
        //#endif
        Operation<Entity> original
    ) {
        if (!CarpetAMSAdditionSettings.enderPearlRestoreFix) {
            return original.call(input, level, reason, loader);
        }

        ServerPlayer player = (ServerPlayer) (Object) this;
        //#if MC>=12111
        EntityProcessor restore = entity -> {
        //#else
        //$$ Function<Entity, Entity> restore = entity -> {
        //#endif
            if (entity instanceof ThrownEnderpearl && ((ProjectileInvoker) entity).ams$invokeOwnedBy(player)) {
                Entity existing = ams$findExistingEntity((ServerLevel) level, entity);
                if (existing instanceof ThrownEnderpearl && !existing.isRemoved() && ((ProjectileInvoker) existing).ams$invokeOwnedBy(player)) {
                    ((ThrownEnderpearl) entity).setOwner(null);
                    player.deregisterEnderPearl((ThrownEnderpearl) entity);
                    ((ThrownEnderpearl) existing).setOwner(player);
                    player.registerEnderPearl((ThrownEnderpearl) existing);
                    return existing;
                }
            }
            //#if MC>=12111
            return loader.process(entity);
            //#else
            //$$ return loader.apply(entity);
            //#endif
        };

        return original.call(input, level, reason, restore);
    }

    @Unique
    private static Entity ams$findExistingEntity(ServerLevel level, Entity entity) {
        UUID uuid = entity.getUUID();
        Entity existing = level.getEntity(uuid);

        if (existing != null) {
            return existing;
        }

        EntitySectionLookup lookup = (EntitySectionLookup) level;

        if (!lookup.ams$isEntityLoaded(uuid)) {
            return null;
        }

        EntitySectionStorage<Entity> storage = lookup.ams$getSectionStorage();
        //#if MC>=260000
        //$$ long savedChunk = entity.chunkPosition().pack();
        //#else
        long savedChunk = entity.chunkPosition().toLong();
        //#endif
        existing = ams$findEntityInChunk(storage, savedChunk, uuid);

        if (existing != null) {
            return existing;
        }

        for (long chunk : storage.getAllChunksWithExistingSections()) {
            if (chunk == savedChunk) {
                continue;
            }

            existing = ams$findEntityInChunk(storage, chunk, uuid);

            if (existing != null) {
                return existing;
            }
        }

        return null;
    }

    @Unique
    private static Entity ams$findEntityInChunk(EntitySectionStorage<Entity> storage, long chunk, UUID uuid) {
        Iterator<EntitySection<Entity>> sections = storage.getExistingSectionsInChunk(chunk).iterator();

        while (sections.hasNext()) {
            Iterator<Entity> entities = sections.next().getEntities().iterator();
            while (entities.hasNext()) {
                Entity entity = entities.next();
                if (entity.getUUID().equals(uuid)) {
                    return entity;
                }
            }
        }

        return null;
    }
}
