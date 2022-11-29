package club.mcams.carpet.mixin.rule.scheduledRandomTick;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11900
//$$import net.minecraft.util.math.random.Random;
//#else
import java.util.Random;
//#endif

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin{
    @Shadow
    public abstract void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @Inject(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/server/world/ServerWorld;breakBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
            ),
            cancellable = true
    )
    private void scheduleTickMixinInvoke(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (AmsServerSettings.scheduledRandomTickChorusFlower || AmsServerSettings.scheduledRandomTickAllPlants)
            ci.cancel();
    }

    @Inject(
            method = "scheduledTick",
            at = @At("TAIL")
    )
    private void scheduleTickMixinTail(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (AmsServerSettings.scheduledRandomTickChorusFlower || AmsServerSettings.scheduledRandomTickAllPlants)
            this.randomTick(state, world, pos, random);
    }
}
