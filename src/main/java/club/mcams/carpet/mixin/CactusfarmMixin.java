package club.mcams.carpet.mixin;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CactusBlock.class)
public abstract class CactusfarmMixin extends Block {

    public CactusfarmMixin(Settings settings) {
        super(settings);
    }

    @Shadow
    public abstract void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @Inject(method = "scheduledTick", at = @At("TAIL"))
    private void Mixin(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (AmsServerSettings.Cactusfarm){
            randomTick(state, world, pos, random);
        }
    }
}
