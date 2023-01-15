package club.mcams.carpet.mixin.rule.boneBlockUpdateSuppressor;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public abstract class boneBlockUpdateSuppressor {
    //#if MC<11900
    @Inject(method = "neighborUpdate", at = @At("TAIL"))
    private void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify, CallbackInfo ci) {
        if(AmsServerSettings.boneBlockUpdateSuppressor && state.isOf(Blocks.BONE_BLOCK)) {
            throw new StackOverflowError("Carpet-AMS-Addition UpdateSuppressor");
        }
    }
    //#endif
}