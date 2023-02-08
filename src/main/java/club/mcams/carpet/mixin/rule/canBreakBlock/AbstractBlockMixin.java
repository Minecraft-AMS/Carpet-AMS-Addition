package club.mcams.carpet.mixin.rule.canBreakBlock;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    final float Netherite_pickaxe_Hardness = 666.0F;
    final float Diamond_pickaxe_Hardness = 888.0F;
    final float GeneralHardness = 5.0F;
    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFNE, shift = At.Shift.AFTER),
            method = "Lnet/minecraft/block/AbstractBlock;calcBlockBreakingDelta(" +
                    "Lnet/minecraft/block/BlockState;" +
                    "Lnet/minecraft/entity/player/PlayerEntity;" +
                    "Lnet/minecraft/world/BlockView;" +
                    "Lnet/minecraft/util/math/BlockPos;)F",
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void allowBedrockBreaking(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir, float hardness) {
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        if (state.getBlock() == Blocks.BEDROCK && (stack.getItem() == Items.NETHERITE_PICKAXE) && AmsServerSettings.canBreakBedRock) {
            cir.setReturnValue(player.getBlockBreakingSpeed(state) / Netherite_pickaxe_Hardness);
        }
        else if(state.getBlock() == Blocks.BEDROCK && (stack.getItem() == Items.DIAMOND_PICKAXE) && AmsServerSettings.canBreakBedRock) {
            cir.setReturnValue(player.getBlockBreakingSpeed(state) / Diamond_pickaxe_Hardness);
        }
        if (state.getBlock() == Blocks.END_PORTAL_FRAME && ((stack.getItem() == Items.NETHERITE_PICKAXE) || (stack.getItem() == Items.DIAMOND_PICKAXE)) && AmsServerSettings.canBreakEndPortalFrame) {
            cir.setReturnValue(player.getBlockBreakingSpeed(state) / GeneralHardness);
        }
    }
}

