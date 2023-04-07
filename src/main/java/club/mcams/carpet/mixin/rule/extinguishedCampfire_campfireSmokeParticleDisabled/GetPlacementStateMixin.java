package club.mcams.carpet.mixin.rule.extinguishedCampfire_campfireSmokeParticleDisabled;

import club.mcams.carpet.AmsServerSettings;

//#if MC>=11900
//$$ import net.minecraft.util.math.random.Random;
//#endif
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC<11900
import java.util.Random;
//#endif

@Mixin(CampfireBlock.class)
public abstract class GetPlacementStateMixin extends BlockWithEntity {
    protected GetPlacementStateMixin(Settings builder) {
        super(builder);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    protected void initProxy(CallbackInfo info) {
        this.setDefaultState(this.getDefaultState().with(CampfireBlock.LIT, false));
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    protected void getPlacementStateProxy(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        if (cir.getReturnValue() != null && AmsServerSettings.extinguishedCampfire) {
            cir.setReturnValue(cir.getReturnValue().with(CampfireBlock.LIT, false));
            cir.cancel();
        }
    }

    @Inject(method = "spawnSmokeParticle", at = @At("HEAD"), cancellable = true)
    private static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo ci) {
        if(AmsServerSettings.campfireSmokeParticleDisabled) {
            Random random = world.getRandom();
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.0 + random.nextDouble() * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + 0.0, (double)pos.getZ() + 0.0 + random.nextDouble() * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.0, 0.0);
            ci.cancel();
        }
    }
}
