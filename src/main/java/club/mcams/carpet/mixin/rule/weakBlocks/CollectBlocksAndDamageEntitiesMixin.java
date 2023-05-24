package club.mcams.carpet.mixin.rule.weakBlocks;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//#if MC>=11700
import net.minecraft.world.event.GameEvent;
//#endif
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
//#if MC>=11900
//$$ import net.minecraft.util.math.Vec3d;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC>=11900
//$$ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//#endif

import com.google.common.collect.Sets;

import java.util.*;

@Mixin(value = Explosion.class, priority = 888)
public abstract class CollectBlocksAndDamageEntitiesMixin {

    @Shadow
    @Final
    private World world;

    @Shadow
    @Final
    private float power;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    //#if MC<11900
    @Mutable
    @Shadow
    @Final
    private List<BlockPos> affectedBlocks;
    //#endif

    //#if MC>=11900
    //$$ @Mutable
    //$$ @Shadow
    //$$ @Final
    //$$ private ObjectArrayList<BlockPos> affectedBlocks;
    //#endif

    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private ExplosionBehavior behavior;

    @SuppressWarnings("unused")
    @Inject(method = "collectBlocksAndDamageEntities", at = @At("RETURN"), cancellable = true)
    public void collectBlocksAndDamageEntities(CallbackInfo ci) {
        //#if MC<11900
        if(AmsServerSettings.weakBedRock || AmsServerSettings.weakObsidian || AmsServerSettings.weakCryingObsidian) {
            //#endif
            //#if MC>=11900
            //$$if(AmsServerSettings.weakBedRock || AmsServerSettings.weakObsidian || AmsServerSettings.weakCryingObsidian || AmsServerSettings.weakReinforcedDeepslate) {
            //#endif
            //#if MC>=11700 && MC<=11900
            this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new BlockPos(this.x, this.y, this.z));
            //#elseif MC>=11900
            //$$ this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
            //#endif
            Set<BlockPos> set = Sets.newHashSet();
            int k;
            int l;
            for (int j = 0; j < 16; ++j) {
                for (k = 0; k < 16; ++k) {
                    for (l = 0; l < 16; ++l) {
                        if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                            double d = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                            double e = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                            double f = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                            double g = Math.sqrt(d * d + e * e + f * f);
                            d /= g;
                            e /= g;
                            f /= g;
                            float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                            double m = this.x;
                            double n = this.y;
                            double o = this.z;

                            for (float var21 = 0.3F; h > 0.0F; h -= 0.22500001F) {
                                //#if MC<11900
                                BlockPos blockPos = new BlockPos(m, n, o);
                                //#else
                                //$$ BlockPos blockPos = BlockPos.ofFloored(m, n, o);
                                //#endif
                                BlockState blockState = this.world.getBlockState(blockPos);
                                FluidState fluidState = this.world.getFluidState(blockPos);
                                if (!this.world.isInBuildLimit(blockPos)) {
                                    break;
                                }
                                Optional<Float> optional = this.behavior.getBlastResistance((Explosion) (Object) this, this.world, blockPos, blockState, fluidState);
                                if (optional.isPresent()) {
                                    h -= ((Float) optional.get() + 0.3F) * 0.3F;
                                }
                                MinecraftServer server = this.world.getServer();
                                if ((
                                        h > 0.0F && this.behavior.canDestroyBlock(((Explosion) (Object) this), this.world, blockPos, blockState, h))
                                        || (this.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN && AmsServerSettings.weakObsidian)
                                        || (this.world.getBlockState(blockPos).getBlock() == Blocks.CRYING_OBSIDIAN && AmsServerSettings.weakCryingObsidian)
                                        || (this.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK && AmsServerSettings.weakBedRock)
                                    //#if MC>11800
                                    //$$ || (this.world.getBlockState(blockPos).getBlock() == Blocks.REINFORCED_DEEPSLATE && AmsServerSettings.weakReinforcedDeepslate)
                                    //#endif
                                ) {
                                    set.add(blockPos);
                                }
                                m += d * 0.30000001192092896D;
                                n += e * 0.30000001192092896D;
                                o += f * 0.30000001192092896D;
                            }
                        }
                    }
                }
            }
            this.affectedBlocks.addAll(set);
            ci.cancel();
        }
    }
}