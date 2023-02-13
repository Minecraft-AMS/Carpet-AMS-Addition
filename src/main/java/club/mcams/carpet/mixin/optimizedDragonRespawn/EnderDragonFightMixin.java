package club.mcams.carpet.mixin.optimizedDragonRespawn;

import club.mcams.carpet.AmsServerSettings;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EnderDragonFight.class, priority = 995)
public abstract class EnderDragonFightMixin {
    @Shadow @Final private ServerWorld world;

    @Shadow @Final private BlockPattern endPortalPattern;

    @Nullable @Shadow private BlockPos exitPortalLocation;

    private int cacheChunkIteratorX = -8;
    private int cacheChunkIteratorZ = -8;
    private int cacheOriginIteratorY = -1;

    /**
     * @author WenDavid
     * @reason Optimize the search of end portal
     */
    @Overwrite
    private @Nullable BlockPattern.Result findEndPortal() {
        int i,j;
        if(!AmsServerSettings.optimizedDragonRespawn) {
            cacheChunkIteratorX = -8;
            cacheChunkIteratorZ = -8;
        }
        for(i = cacheChunkIteratorX; i <= 8; ++i) {
            for(j = cacheChunkIteratorZ; j <= 8; ++j) {
                WorldChunk worldChunk = this.world.getChunk(i, j);
                for(BlockEntity blockEntity : worldChunk.getBlockEntities().values()) {
                    if(AmsServerSettings.optimizedDragonRespawn && blockEntity instanceof EndGatewayBlockEntity) continue;
                    if (blockEntity instanceof EndPortalBlockEntity) {
//                        TextUtil.broadcastToAllPlayers("Invoke searchAround "+ blockEntity.getPos().toString());
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

        if(AmsServerSettings.optimizedDragonRespawn && cacheOriginIteratorY != -1) {
            i = cacheOriginIteratorY;
        }
        else {
            i = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.ORIGIN).getY();
        }
        boolean notFirstSearch = false;
        for(j = i; j >= this.world.getBottomY(); --j) {
            BlockPattern.Result result2;
            if(AmsServerSettings.optimizedDragonRespawn && notFirstSearch) {
//                TextUtil.broadcastToAllPlayers("2Invoke partial searchAround "+ new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()).toString());
                result2 = this.endPortalPattern.partialSearchAround(this.world, new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()));
            }
            else{
//                TextUtil.broadcastToAllPlayers("2Invoke searchAround "+ new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()).toString());
                result2 = this.endPortalPattern.searchAround(this.world, new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()));
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

        return null;
    }

    @Inject(
            method = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;respawnDragon(Ljava/util/List;)V",
            at = @At("HEAD")
    )
    private void resetCache(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        cacheChunkIteratorX = -8;
        cacheChunkIteratorZ = -8;
        cacheOriginIteratorY = -1;
    }
}
