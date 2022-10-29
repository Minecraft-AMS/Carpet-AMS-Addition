package club.mcams.carpet.mixin.rule.scheduledRandomTick;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractPlantPartBlock.class)
public abstract class AbstractPlantPartBlockMixin extends Block {

    public AbstractPlantPartBlockMixin(Settings settings) {
        super(settings);
    }


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
        if (AmsServerSettings.scheduledRandomTickStem || AmsServerSettings.scheduledRandomTickAllPlants) {
            ci.cancel();
        }
    }

    @Inject(
            method = "scheduledTick",
            at = @At("TAIL")
    )
    private void scheduleTickMixinTail(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        AbstractPlantPartBlock $this = (AbstractPlantPartBlock) (Object) this;
        if ($this instanceof AbstractPlantStemBlock && (AmsServerSettings.scheduledRandomTickStem || AmsServerSettings.scheduledRandomTickAllPlants)) {
            $this.randomTick(state, world, pos, random);
        }
    }
}
