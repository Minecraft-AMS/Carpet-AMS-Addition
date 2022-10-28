package club.mcams.carpet.mixin.rule.optimizedDragonRespawn;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {
    @Final
    private ServerWorld world;
    @Final
    private BlockPattern endPortalPattern;
    @Shadow
    private boolean dragonKilled;
    @Shadow
    private EnderDragonSpawnState dragonSpawnState;
    @Shadow
    private int spawnStateTimer;
    @Shadow
    private List<EndCrystalEntity> crystals;

    @Shadow
    protected abstract BlockPattern.Result findEndPortal();

    @Shadow
    protected abstract void generateEndPortal(boolean previouslyKilled);

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "respawnDragon(Ljava/util/List;)V", cancellable = true)
    private void respawnDragon(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        if (AmsServerSettings.optimizedDragonRespawn) {
            /* Remove the check for multiple portals to reduce the lag */
            if (this.dragonKilled && this.dragonSpawnState == null) {
                BlockPattern.Result result = findEndPortal();
                if (result != null) {
                    logPortalFind();
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
                }
                this.dragonSpawnState = EnderDragonSpawnState.START;
                this.spawnStateTimer = 0;
                this.generateEndPortal(false);
                this.crystals = crystals;
            }
            ci.cancel();
        }
    }

    private void logPortalFind() {
        List<BaseText> messages = new ArrayList<>();
        LoggerRegistry.getLogger("dragonPortalLocation").log(() ->
        {
            messages.add(Messenger.c(
                    "w Located portals"));
            return messages.toArray(new BaseText[0]);
        });
    }
}
