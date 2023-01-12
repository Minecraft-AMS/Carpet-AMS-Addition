package club.mcams.carpet.mixin.rule.movableBlocks;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
    @Inject(method = "isMovable", at = @At("HEAD"), cancellable = true)
    private static void MovableBlocks(BlockState state, World world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.movableEnderChest && state.isOf(Blocks.ENDER_CHEST)) {
            cir.setReturnValue(true);
        }
        if (AmsServerSettings.movableEndPortalFrame && state.isOf(Blocks.END_PORTAL_FRAME)) {
            cir.setReturnValue(true);
        }
        if (AmsServerSettings.movableObsidian && state.isOf(Blocks.OBSIDIAN)) {
            cir.setReturnValue(true);
        }
        if (AmsServerSettings.movableCryingObsidian && state.isOf(Blocks.CRYING_OBSIDIAN)) {
            cir.setReturnValue(true);
        }
        if (AmsServerSettings.movableBedRock && state.isOf(Blocks.BEDROCK)) {
            cir.setReturnValue(true);
        }
        if (AmsServerSettings.movableEnchantingTable && state.isOf(Blocks.ENCHANTING_TABLE)) {
            cir.setReturnValue(true);
        }
        if (AmsServerSettings.movableBeacon && state.isOf(Blocks.BEACON)) {
            cir.setReturnValue(true);
        }
        //#if MC>=11900
        //$$if (AmsServerSettings.movableReinforcedDeepslate && state.isOf(Blocks.REINFORCED_DEEPSLATE)) {
        //$$    cir.setReturnValue(true);
        //$$}
        //#endif
        if (AmsServerSettings.movableAnvil && state.isOf(Blocks.ANVIL)) {
            cir.setReturnValue(true);
        }
    }
}
