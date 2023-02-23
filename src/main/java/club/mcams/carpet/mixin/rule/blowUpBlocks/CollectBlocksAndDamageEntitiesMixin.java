package club.mcams.carpet.mixin.rule.blowUpBlocks;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
//#if MC>=11700
import net.minecraft.world.event.GameEvent;
//#endif
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Invoker;
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

    //#if MC>11800
    //$$ @Mutable
    //$$ @Shadow
    //$$ @Final
    //$$ private ObjectArrayList<BlockPos> affectedBlocks;
    //#endif

    @Mutable
    @Shadow
    @Final
    private  Map<PlayerEntity, Vec3d> affectedPlayers;

    @Shadow
    @Final
    private Entity entity;

    @Invoker("getExposure")
    public static float getExposure(Vec3d source, Entity entity) {
        return 0;
    }

    @Shadow
    public abstract DamageSource getDamageSource();

    @Shadow
    @Final
    private ExplosionBehavior behavior;

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"), cancellable = true)
    public void collectBlocksAndDamageEntities(CallbackInfo ci) {
        //#if MC>=11700
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new BlockPos(this.x, this.y, this.z));
        //#endif
        Set<BlockPos> set = Sets.newHashSet();
        int k;
        int l;
        for(int j = 0; j < 16; ++j) {
            for(k = 0; k < 16; ++k) {
                for(l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (double)((float)j / 15.0F * 2.0F - 1.0F);
                        double e = (double)((float)k / 15.0F * 2.0F - 1.0F);
                        double f = (double)((float)l / 15.0F * 2.0F - 1.0F);
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double m = this.x;
                        double n = this.y;
                        double o = this.z;

                        for(float var21 = 0.3F; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = new BlockPos(m, n, o);
                            BlockState blockState = this.world.getBlockState(blockPos);
                            FluidState fluidState = this.world.getFluidState(blockPos);
                            if (!this.world.isInBuildLimit(blockPos)) {
                                break;
                            }

                            Optional<Float> optional = this.behavior.getBlastResistance((Explosion)(Object)this, this.world, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= ((Float)optional.get() + 0.3F) * 0.3F;
                            }

                            MinecraftServer server = this.world.getServer();
                            if ((h > 0.0F && this.behavior.canDestroyBlock(((Explosion)(Object)this), this.world, blockPos, blockState, h)) || (this.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN && AmsServerSettings.blowUpObsidian)) {
                                set.add(blockPos);
                            }
                            if ((h > 0.0F && this.behavior.canDestroyBlock(((Explosion)(Object)this), this.world, blockPos, blockState, h)) || (this.world.getBlockState(blockPos).getBlock() == Blocks.CRYING_OBSIDIAN && AmsServerSettings.blowUpCryingObsidian)) {
                                set.add(blockPos);
                            }
                            if ((h > 0.0F && this.behavior.canDestroyBlock(((Explosion)(Object)this), this.world, blockPos, blockState, h)) || (this.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK && AmsServerSettings.blowUpBedRock)) {
                                set.add(blockPos);
                            }
                            //#if MC>11800
                            //$$if ((h > 0.0F && this.behavior.canDestroyBlock(((Explosion)(Object)this), this.world, blockPos, blockState, h)) || (this.world.getBlockState(blockPos).getBlock() == Blocks.REINFORCED_DEEPSLATE && AmsServerSettings.blowUpReinforcedDeepslate)) {
                            //$$ set.add(blockPos);
                            //$$}
                            //#endif
                            m += d * 0.30000001192092896D;
                            n += e * 0.30000001192092896D;
                            o += f * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set);
        float j = this.power * 2.0F;
        k = MathHelper.floor(this.x - (double)j - 1.0D);
        l = MathHelper.floor(this.x + (double)j + 1.0D);
        int d = MathHelper.floor(this.y - (double)j - 1.0D);
        int q = MathHelper.floor(this.y + (double)j + 1.0D);
        int e = MathHelper.floor(this.z - (double)j - 1.0D);
        int r = MathHelper.floor(this.z + (double)j + 1.0D);
        List<Entity> f = this.world.getOtherEntities(this.entity, new Box((double)k, (double)d, (double)e, (double)l, (double)q, (double)r));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for(int g = 0; g < f.size(); ++g) {
            Entity entity = (Entity)f.get(g);
            if (!entity.isImmuneToExplosion()) {
                double h = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double)j;
                if (h <= 1.0D) {
                    double s = entity.getX() - this.x;
                    double t = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                    double u = entity.getZ() - this.z;
                    double blockPos = Math.sqrt(s * s + t * t + u * u);
                    if (blockPos != 0.0D) {
                        s /= blockPos;
                        t /= blockPos;
                        u /= blockPos;
                        double fluidState = (double)getExposure(vec3d, entity);
                        double v = (1.0D - h) * fluidState;
                        entity.damage(this.getDamageSource(), (float)((int)((v * v + v) / 2.0D * 7.0D * (double)j + 1.0D)));
                        double w = v;
                        if (entity instanceof LivingEntity) {
                            w = ProtectionEnchantment.transformExplosionKnockback((LivingEntity)entity, v);
                        }

                        entity.setVelocity(entity.getVelocity().add(s * w, t * w, u * w));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerEntity = (PlayerEntity)entity;
                            //#if MC>=11700
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                //#else if
                                //$$ if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.abilities.flying)) {
                                //#endif
                                this.affectedPlayers.put(playerEntity, new Vec3d(s * v, t * v, u * v));
                            }
                        }
                    }
                }
            }
        }
        ci.cancel();
    }
}