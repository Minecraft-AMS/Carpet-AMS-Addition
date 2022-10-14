package club.mcams.carpet.mixin;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnderDragonFight.class)
public abstract class optimizedDragonRespawn {
    @Shadow
    ServerWorld world;
    @Shadow
    BlockPattern endPortalPattern;
    @Shadow
    boolean dragonKilled;
    @Shadow
    EnderDragonSpawnState dragonSpawnState;
    @Shadow
    int spawnStateTimer;
    @Shadow
    List<EndCrystalEntity> crystals;

    @Shadow
    protected abstract BlockPattern.Result findEndPortal();

    @Shadow
    protected abstract void generateEndPortal(boolean previouslyKilled);


    @Inject(at = @At("HEAD"), method = "respawnDragon(Ljava/util/List;)V", cancellable = true)
    private void respawnDragon(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        if (AmsServerSettings.optimizationDragonRespawn) {
            /* Remove the check for multiple portals to reduce the lag */
            if (this.dragonKilled && this.dragonSpawnState == null) {
                BlockPattern.Result result = this.findEndPortal();
                for (int i = 0; i < this.endPortalPattern.getWidth(); ++i) {
                    for (int j = 0; j < this.endPortalPattern.getHeight(); ++j) {
                        for (int k = 0; k < this.endPortalPattern.getDepth(); ++k) {
                            CachedBlockPosition cachedBlockPosition = result.translate(i, j, k);
                            if (!cachedBlockPosition.getBlockState().isOf(Blocks.BEDROCK) && !cachedBlockPosition.getBlockState().isOf(Blocks.END_PORTAL))
                                continue;
                            this.world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.END_STONE.getDefaultState());
                        }
                    }
                }
                this.dragonSpawnState = EnderDragonSpawnState.START;
                this.spawnStateTimer = 0;
                this.generateEndPortal(false);
                this.crystals = crystals;
            }
            ci.cancel();
        }
    }
}