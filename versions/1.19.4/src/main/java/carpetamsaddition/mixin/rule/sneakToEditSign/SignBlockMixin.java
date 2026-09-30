/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023 A Minecraft Server and contributors
 */

package carpetamsaddition.mixin.rule.sneakToEditSign;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.20")
@Mixin(SignBlock.class)
public abstract class SignBlockMixin {
    @Inject(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
        ),
        cancellable = true
    )
    private void onUse(
        BlockState state, Level world, BlockPos pos, Player player,
        InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (
            CarpetAMSAdditionSettings.sneakToEditSign &&
            player.isShiftKeyDown() &&
            player.getMainHandItem().isEmpty() &&
            player.getOffhandItem().isEmpty()
        ) {
            player.openTextEdit((SignBlockEntity) world.getBlockEntity(pos));
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
