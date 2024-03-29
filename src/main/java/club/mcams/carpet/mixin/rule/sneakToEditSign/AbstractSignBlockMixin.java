/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.mixin.rule.sneakToEditSign;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC>=12000
//$$ import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//#endif
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public abstract class AbstractSignBlockMixin {
    //#if MC<12000
    @Inject(
        method = "onUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"
        ),
        cancellable = true
    )
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (
            AmsServerSettings.sneakToEditSign &&
            player.isSneaking() &&
            player.getMainHandStack().isEmpty() &&
            player.getOffHandStack().isEmpty()
        ) {
            SignBlockEntity signBlockEntity = (SignBlockEntity) world.getBlockEntity(pos);
            player.openEditSignScreen(signBlockEntity);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
    //#else
    //$$ @ModifyExpressionValue(
    //$$     method = "onUse",
    //$$     at = @At(
    //$$         value = "INVOKE",
    //$$         target = "Lnet/minecraft/entity/player/PlayerEntity;canModifyBlocks()Z"
    //$$     )
    //$$ )
    //$$ public boolean onUse(
    //$$     boolean original,
    //$$     BlockState state,
    //$$     World world,
    //$$     BlockPos pos,
    //$$     PlayerEntity player,
    //$$     //#if MC<12005
    //$$     //$$ Hand hand,
    //$$     //#endif
    //$$     BlockHitResult hit
    //$$ ) {
    //$$     return AmsServerSettings.sneakToEditSign ? original && player.isSneaking() : original;
    //$$ }
    //#endif
}
