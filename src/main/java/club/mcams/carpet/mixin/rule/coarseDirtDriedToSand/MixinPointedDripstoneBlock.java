package club.mcams.carpet.mixin.rule.coarseDirtDriedToSand;


import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.util.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Restriction(require=@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17.0"))
@Mixin(PointedDripstoneBlock.class)
public abstract class MixinPointedDripstoneBlock {
    @Inject(
            method="Lnet/minecraft/block/PointedDripstoneBlock;getFluid(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Ljava/util/Optional;",
            at=@At("RETURN")
    )
    private static void getFluidsWetCoarseDirt(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Optional<Fluid>> cir) {
        if (AmsServerSettings.coarseDirtDriedToSand && cir.getReturnValue().isPresent() && cir.getReturnValue().get()
                //#if MC >=11900
                //$$ .fluid
                //#endif
                        == Fluids.EMPTY) {
            cir.setReturnValue(
                    IMixinPointedDripstoneBlock.invoke_getSupportingPos(world, pos, state, 11)
                            .map(posSupported -> {
                                BlockPos blockPos = posSupported.up();
                                BlockState blockState = world.getBlockState(blockPos);
                                Biome biome = world.getBiome(blockPos).value();
                                Fluid fluid = blockState.isOf(Blocks.COARSE_DIRT) && biome.isHot(blockPos) ? Fluids.WATER : world.getFluidState(blockPos).getFluid();
                                //#if MC >=11900
                                //$$ return new DrippingFluid(blockPos, fluid, blockState);
                                //#else
                                return fluid;
                                //#endif
                            })
            );
        }
    }

    @Inject(
            method="Lnet/minecraft/block/PointedDripstoneBlock;dripTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;F)V",
            at=@At(
                    value="INVOKE",
                    target="Lnet/minecraft/block/PointedDripstoneBlock;getCauldronPos(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/Fluid;)Lnet/minecraft/util/math/BlockPos;"
            )
    )
    private static void dripTickDriedCoarseDirt(BlockState state, ServerWorld world, BlockPos pos, float dripChance, CallbackInfo ci) {
        if(AmsServerSettings.coarseDirtDriedToSand) {
            Optional<BlockPos> supportPos = IMixinPointedDripstoneBlock.invoke_getSupportingPos(world, pos, state, 11);
            if(supportPos.isPresent()) {
                BlockPos blockPos = supportPos.get();
                BlockState sourceState = world.getBlockState(blockPos);
                if(sourceState.isOf(Blocks.COARSE_DIRT)) {
                    BlockState blockState = Blocks.SAND.getDefaultState();
                    world.setBlockState(blockPos, blockState);
                    Block.pushEntitiesUpBeforeBlockChange(sourceState, blockState, world, blockPos);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos
                            //#if MC>=11900
                            //$$,GameEvent.Emitter.of(blockState)
                            //#endif
                    );
                    world.syncWorldEvent(WorldEvents.POINTED_DRIPSTONE_DRIPS, blockPos, 0);
                }
            }
        }
    }
}
