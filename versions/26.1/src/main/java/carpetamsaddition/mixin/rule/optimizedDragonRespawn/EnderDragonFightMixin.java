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

package carpetamsaddition.mixin.rule.optimizedDragonRespawn;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.optimizedDragonRespawn.BlockPatternHelper;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.List;

@GameVersion(version = "mc >= 26.1")
@Mixin(value = EnderDragonFight.class, priority = 888)
public abstract class EnderDragonFightMixin {

    @Shadow
    private ServerLevel level;

    @Shadow
    @Final
    private BlockPattern exitPortalPattern;

    @Shadow
    private boolean needsStateScanning;

    @Shadow
    private @Nullable BlockPos exitPortalLocation;

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
    @WrapMethod(method = "findExitPortal")
    private @Nullable BlockPattern.BlockPatternMatch findEndPortal(Operation<BlockPattern.BlockPatternMatch> original) {
        if(!CarpetAMSAdditionSettings.optimizedDragonRespawn) {
            original.call();
        } else {
            int i, j;

            for (i = cacheChunkIteratorX; i <= 8; ++i) {
                for (j = cacheChunkIteratorZ; j <= 8; ++j) {
                    LevelChunk worldChunk = this.level.getChunk(i, j);
                    for (BlockEntity blockEntity : worldChunk.getBlockEntities().values()) {
                        if (CarpetAMSAdditionSettings.optimizedDragonRespawn && blockEntity instanceof TheEndGatewayBlockEntity) {
                            continue;
                        }

                        if (blockEntity instanceof TheEndPortalBlockEntity) {
                            BlockPattern.BlockPatternMatch result = this.exitPortalPattern.find(this.level, blockEntity.getBlockPos());
                            if (result != null) {
                                BlockPos blockPos = result.getBlock(3, 3, 3).getPos();

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
            if (this.needsStateScanning || this.exitPortalLocation == null){
                if(CarpetAMSAdditionSettings.optimizedDragonRespawn && cacheOriginIteratorY != -1) {
                    i = cacheOriginIteratorY;
                } else {
                    i = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(BlockPos.ZERO)).getY();
                }

                boolean notFirstSearch = false;

                for (j = i; j >= 0; --j) {
                    BlockPattern.BlockPatternMatch result2;

                    if (CarpetAMSAdditionSettings.optimizedDragonRespawn && notFirstSearch) {
                        result2 = BlockPatternHelper.partialSearchAround(this.exitPortalPattern, this.level, new BlockPos(EndPodiumFeature.getLocation(BlockPos.ZERO).getX(), j, EndPodiumFeature.getLocation(BlockPos.ZERO).getZ()));
                    } else {
                        result2 = this.exitPortalPattern.find(this.level, new BlockPos(EndPodiumFeature.getLocation(BlockPos.ZERO).getX(), j, EndPodiumFeature.getLocation(BlockPos.ZERO).getZ()));
                    }

                    if (result2 != null) {
                        if (this.exitPortalLocation == null) {
                            this.exitPortalLocation = result2.getBlock(3, 3, 3).getPos();
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

    @Inject(method = "respawnDragon(Ljava/util/List;)V", at = @At("HEAD"))
    private void resetCache(List<EndCrystal> crystals, CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.optimizedDragonRespawn) {
            cacheChunkIteratorX = -8;
            cacheChunkIteratorZ = -8;
            cacheOriginIteratorY = -1;
        }
    }
}
