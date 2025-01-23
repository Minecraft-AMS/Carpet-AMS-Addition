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

package club.mcams.carpet.mixin.rule.optimizedDragonRespawn;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.optimizedDragonRespawn.BlockPatternHelper;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.feature.EndPortalFeature;

import org.spongepowered.asm.mixin.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EnderDragonFight.class, priority = 888)
public abstract class EnderDragonFightMixin {

    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private BlockPattern endPortalPattern;

    @Nullable
    @Shadow
    private BlockPos exitPortalLocation;

    @Shadow
    private boolean doLegacyCheck;

    @Unique
    private int cacheChunkIteratorX = -8;

    @Unique
    private int cacheChunkIteratorZ = -8;

    @Unique
    private int cacheOriginIteratorY = -1;

    /**
     * @author WenDavid
     * @reason Optimize the search of end portal
     */

    @WrapMethod(method = "findEndPortal")
    private @Nullable BlockPattern.Result findEndPortal(Operation<BlockPattern.Result> original) {
        int i, j;
        if(!AmsServerSettings.optimizedDragonRespawn) {
            original.call();
        } else {
            for (i = cacheChunkIteratorX; i <= 8; ++i) {
                for (j = cacheChunkIteratorZ; j <= 8; ++j) {
                    WorldChunk worldChunk = this.world.getChunk(i, j);
                    for (BlockEntity blockEntity : worldChunk.getBlockEntities().values()) {
                        if (AmsServerSettings.optimizedDragonRespawn && blockEntity instanceof EndGatewayBlockEntity) continue;
                        if (blockEntity instanceof EndPortalBlockEntity) {
                            BlockPattern.Result result = this.endPortalPattern.searchAround(this.world, blockEntity.getPos());
                            if (result != null) {
                                BlockPos blockPos = result.translate(3, 3, 3).getBlockPos();
                                if (this.exitPortalLocation == null) {
                                    this.exitPortalLocation = blockPos;
                                }
                                //No need to judge whether optimizing option is open
                                cacheChunkIteratorX = i;
                                cacheChunkIteratorZ = j;
                                return result;
                            }
                        }
                    }
                }
            }
            if (this.doLegacyCheck || this.exitPortalLocation == null){
                if(AmsServerSettings.optimizedDragonRespawn && cacheOriginIteratorY != -1) {
                    i = cacheOriginIteratorY;
                }
                else {
                    //#if MC>=12000
                    //$$ i = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.offsetOrigin(BlockPos.ORIGIN)).getY();
                    //#else
                    i = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.ORIGIN).getY();
                    //#endif
                }
                boolean notFirstSearch = false;
                for (j = i; j >= 0; --j) {
                    //#if MC>=12000
                    //$$ BlockPattern.Result result2 = null;
                    //#else
                    BlockPattern.Result result2;
                    //#endif
                    if (AmsServerSettings.optimizedDragonRespawn && notFirstSearch) {
                        //#if MC>=12000
                        //$$ result2 = BlockPatternHelper.partialSearchAround(this.endPortalPattern, this.world, new BlockPos(EndPortalFeature.offsetOrigin(BlockPos.ORIGIN).getX(), j, EndPortalFeature.offsetOrigin(BlockPos.ORIGIN).getZ()));
                        //#else
                        result2 = BlockPatternHelper.partialSearchAround(this.endPortalPattern, this.world, new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()));
                        //#endif
                    }
                    else {
                        //#if MC>=12000
                        //$$ result2 = this.endPortalPattern.searchAround(this.world, new BlockPos(EndPortalFeature.offsetOrigin(BlockPos.ORIGIN).getX(), j, EndPortalFeature.offsetOrigin(BlockPos.ORIGIN).getZ()));
                        //#else
                        result2 = this.endPortalPattern.searchAround(this.world, new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()));
                        //#endif
                    }
                    if (result2 != null) {
                        if (this.exitPortalLocation == null) {
                            this.exitPortalLocation = result2.translate(3, 3, 3).getBlockPos();
                        }
                        cacheOriginIteratorY = j;
                        return result2;
                    }
                    notFirstSearch = true;
                }
            }
        }
        return null;
    }

    @Inject(
        method = "respawnDragon(Ljava/util/List;)V",
        at = @At("HEAD")
    )
    private void resetCache(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        if (AmsServerSettings.optimizedDragonRespawn) {
            cacheChunkIteratorX = -8;
            cacheChunkIteratorZ = -8;
            cacheOriginIteratorY = -1;
        }
    }
}
