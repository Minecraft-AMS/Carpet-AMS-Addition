package club.mcams.carpet.mixin.rule.optimizedDragonRespawn;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.BlockPatternHelper;

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

@Mixin(value = EnderDragonFight.class, priority = 888)
public abstract class EnderDragonFightMixin {
    @Shadow @Final private ServerWorld world;
    @Shadow @Final private BlockPattern endPortalPattern;
    @Nullable @Shadow private BlockPos exitPortalLocation;
    @Shadow private boolean doLegacyCheck;
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
        if(this.doLegacyCheck || this.exitPortalLocation == null){
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
            for(j = i; j >= 0; --j) {
                //#if MC>=12000
                //$$ BlockPattern.Result result2 = null;
                //#else
                BlockPattern.Result result2;
                //#endif
                if(AmsServerSettings.optimizedDragonRespawn && notFirstSearch) {
                    //#if MC>=12000
                    //&& result2 = BlockPatternHelper.partialSearchAround(this.endPortalPattern, this.world, new BlockPos(EndPortalFeature.offsetOrigin(BlockPos.ORIGIN)).getY();, j, EndPortalFeature.offsetOrigin(BlockPos.ORIGIN).getZ()));
                    //#else
                    result2 = BlockPatternHelper.partialSearchAround(this.endPortalPattern, this.world, new BlockPos(EndPortalFeature.ORIGIN.getX(), j, EndPortalFeature.ORIGIN.getZ()));
                    //#endif
                }
                else{
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

        return null;
    }

    @Inject(
            method = "respawnDragon(Ljava/util/List;)V",
            at = @At("HEAD")
    )
    private void resetCache(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        cacheChunkIteratorX = -8;
        cacheChunkIteratorZ = -8;
        cacheOriginIteratorY = -1;
    }
}
