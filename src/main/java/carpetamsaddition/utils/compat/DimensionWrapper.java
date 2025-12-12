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

package carpetamsaddition.utils.compat;

import carpetamsaddition.utils.EntityUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DimensionWrapper {

    private final ResourceKey<@NotNull Level> dimensionType;

    public DimensionWrapper(ResourceKey<@NotNull Level> dimensionType) {
        this.dimensionType = dimensionType;
    }

    public static DimensionWrapper of(ResourceKey<@NotNull Level> dimensionType) {
        return new DimensionWrapper(dimensionType);
    }

    public static DimensionWrapper of(Level world) {
        return new DimensionWrapper(world.dimension());
    }

    public static DimensionWrapper of(Entity entity) {
        return of(EntityUtil.getEntityWorld(entity));
    }

    public ResourceKey<@NotNull Level> getValue() {
        return this.dimensionType;
    }

    public Identifier getIdentifier() {
        return this.dimensionType.identifier();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DimensionWrapper that = (DimensionWrapper) o;
        return Objects.equals(dimensionType, that.dimensionType);
    }

    @Override
    public int hashCode() {
        return this.dimensionType.hashCode();
    }

    public String getIdentifierString() {
        return this.getIdentifier().toString();
    }

    @Deprecated
    @Override
    public String toString() {
        return this.getIdentifierString();
    }
}
