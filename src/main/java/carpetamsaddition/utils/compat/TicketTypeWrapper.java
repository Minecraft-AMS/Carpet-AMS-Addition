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

package carpetamsaddition.utils.compat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
//#if MC>=12000 && MC<12105
//$$ import net.minecraft.server.level.ChunkLevel;
//$$ import net.minecraft.server.level.FullChunkStatus;
//#elseif MC>=11800 && MC<12000
//$$ import net.minecraft.server.level.ChunkMap;
//$$ import net.minecraft.world.level.chunk.ChunkStatus;
//#endif

import java.util.Comparator;
import java.util.Objects;

/**
 * 区块加载票兼容层，按版本转换用途和标志，不保证所有版本的能力完全等价。
 * 1.16.5：加载与模拟共用等级体系，任一用途均转换为加载和模拟。
 * 1.17.1：与 1.16.5 相同，无法独立控制加载和模拟。
 * 1.18.2：支持仅加载，仅模拟转换为加载和模拟。
 * 1.19.4：与 1.18.2 相同。
 * 1.20.6：与 1.18.2 相同。
 * 1.21.8：支持仅加载、仅模拟、加载和模拟，以及持久化开关。
 * 1.21.11：支持加载、模拟、持久化、维度保活、卸载后过期五种原生标志。
 * 26.1.2：加载票能力与 1.21.11 相同。
 * 26.2：加载票能力与 1.21.11 相同。
 * 26.3：加载票能力与 1.21.11 相同。
 * 1.21.5 之前不支持配置持久化、维度保活和卸载后过期，1.21.8 不支持配置后两项。
 * 这些标志保留旧版原生行为，不额外模拟。1.21.11 之前没有加载或模拟用途时不添加加载票, 之后交给原版处理，单独维度保活也可生效。
 * 1.21.5 之前使用 key 和比较器区分加载票，超时为 int，之后忽略 key 和比较器。
 * 零超时表示不自动过期，重复添加刷新计时。
 */
public final class TicketTypeWrapper<T> {
    public static final int FLAG_PERSIST = 1;
    public static final int FLAG_LOADING = 1 << 1;
    public static final int FLAG_SIMULATION = 1 << 2;
    public static final int FLAG_KEEP_DIMENSION_ACTIVE = 1 << 3;
    public static final int FLAG_CAN_EXPIRE_IF_UNLOADED = 1 << 4;
    public static final long NO_TIMEOUT = 0L;

    private static final int USE_FLAGS = FLAG_LOADING | FLAG_SIMULATION;
    private static final int ALL_FLAGS = FLAG_PERSIST | USE_FLAGS | FLAG_KEEP_DIMENSION_ACTIVE | FLAG_CAN_EXPIRE_IF_UNLOADED;

    public enum TicketUse {
        LOADING(FLAG_LOADING),
        SIMULATION(FLAG_SIMULATION),
        LOADING_AND_SIMULATION(USE_FLAGS);

        private final int flags;

        TicketUse(int flags) {
            this.flags = flags;
        }
    }

    //#if MC>=12105
    private final TicketType ticketType;
    //#else
    //$$ private final TicketType<T> ticketType;
    //#endif
    //#if MC<12111
    //$$ private final int effectiveFlags;
    //#endif

    private TicketTypeWrapper(
        //#if MC>=12111
        TicketType ticketType
        //#elseif MC>=12105
        //$$ TicketType ticketType, int effectiveFlags
        //#else
        //$$ TicketType<T> ticketType, int effectiveFlags
        //#endif
    ) {
        this.ticketType = ticketType;
        //#if MC<12111
        //$$ this.effectiveFlags = effectiveFlags;
        //#endif
    }

    public static <T> TicketTypeWrapper<T> register(String id, long timeoutTicks, int flags, Comparator<T> comparator) {
        Objects.requireNonNull(comparator, "comparator");
        if (timeoutTicks < NO_TIMEOUT) {
            throw new IllegalArgumentException("Ticket timeout must be nonnegative: " + timeoutTicks);
        }

        int effectiveFlags = convertFlags(flags);

        //#if MC>=12111
        TicketType ticketType = TicketType.register(id, timeoutTicks, effectiveFlags);
        //#elseif MC>=12105
        //$$ boolean persist = (effectiveFlags & FLAG_PERSIST) != 0;
        //$$ TicketType ticketType = TicketType.register(id, timeoutTicks, persist, toNativeTicketUse(effectiveFlags));
        //#else
        //$$ TicketType<T> ticketType = TicketType.create(id, comparator, Math.toIntExact(timeoutTicks));
        //#endif

        //#if MC>=12111
        return new TicketTypeWrapper<>(ticketType);
        //#else
        //$$ return new TicketTypeWrapper<>(ticketType, effectiveFlags);
        //#endif
    }

    //#if MC>=12105 && MC<12111
    //$$ private static TicketType.TicketUse toNativeTicketUse(int flags) {
    //$$     switch (flags & USE_FLAGS) {
    //$$         case FLAG_SIMULATION:
    //$$             return TicketType.TicketUse.SIMULATION;
    //$$         case USE_FLAGS:
    //$$             return TicketType.TicketUse.LOADING_AND_SIMULATION;
    //$$         default:
    //$$             return TicketType.TicketUse.LOADING;
    //$$     }
    //$$ }
    //#endif

    public static <T> TicketTypeWrapper<T> register(String id, long timeoutTicks, boolean persist, TicketUse use, Comparator<T> comparator) {
        Objects.requireNonNull(use, "use");
        return register(id, timeoutTicks, (persist ? FLAG_PERSIST : 0) | use.flags, comparator);
    }

    public static <T> TicketTypeWrapper<T> create(String id, Comparator<T> comparator, int timeoutTicks) {
        return register(id, timeoutTicks, USE_FLAGS, comparator);
    }

    public static <T> TicketTypeWrapper<T> create(String id, Comparator<T> comparator) {
        return create(id, comparator, (int) NO_TIMEOUT);
    }

    private static int convertFlags(int flags) {
        if ((flags & ~ALL_FLAGS) != 0) {
            throw new IllegalArgumentException("Unknown ticket flags: " + flags);
        }
        //#if MC>=12111
        return flags;
        //#elseif MC>=12105
        //$$ return (flags & USE_FLAGS) == 0 ? 0 : flags & (FLAG_PERSIST | USE_FLAGS);
        //#elseif MC>=11800
        //$$ return (flags & FLAG_SIMULATION) != 0 ? USE_FLAGS : flags & FLAG_LOADING;
        //#else
        //$$ return (flags & USE_FLAGS) == 0 ? 0 : USE_FLAGS;
        //#endif
    }

    public Object getNativeType() {
        return this.ticketType;
    }

    public void addTicket(ServerLevel world, ChunkPos chunkPos, int radius, T key) {
        Objects.requireNonNull(key, "key");
        //#if MC<12111
        //$$ if ((this.effectiveFlags & USE_FLAGS) == 0) {
        //$$     return;
        //$$ }
        //#endif
        //#if MC>=12105
        world.getChunkSource().addTicketWithRadius(this.ticketType, chunkPos, radius);
        //#elseif MC>=11800
        //$$ this.addLegacyTicket(world, chunkPos, radius, key);
        //#else
        //$$ world.getChunkSource().addRegionTicket(this.ticketType, chunkPos, radius, key);
        //#endif
    }

    //#if MC>=11800 && MC<12105
    //$$ private void addLegacyTicket(ServerLevel world, ChunkPos chunkPos, int radius, T key) {
    //$$     if ((this.effectiveFlags & FLAG_SIMULATION) == 0) {
    //$$         world.getChunkSource().chunkMap.getDistanceManager().addTicket(this.ticketType, chunkPos, getLegacyFullLevel() - radius, key);
    //$$     } else {
    //$$         world.getChunkSource().addRegionTicket(this.ticketType, chunkPos, radius, key);
    //$$     }
    //$$ }
    //#endif

    //#if MC>=12000 && MC<12105
    //$$ private static int getLegacyFullLevel() {
    //$$     return ChunkLevel.byStatus(FullChunkStatus.FULL);
    //$$ }
    //#elseif MC>=11800 && MC<12000
    //$$ private static int getLegacyFullLevel() {
    //$$     return ChunkMap.MAX_CHUNK_DISTANCE - ChunkStatus.maxDistance();
    //$$ }
    //#endif
}
