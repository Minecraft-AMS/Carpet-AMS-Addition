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

package carpetamsaddition.mixin.rule.easyMaxLevelBeacon;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
//#if MC>=11700
import net.minecraft.world.level.Level;
//#endif
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
//#if MC<11700
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC<11700
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity {
    private BeaconBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        //#if MC<11700
        //$$ super(type);
        //#else
        super(type, pos, state);
        //#endif
    }

    //#if MC<11700
    //$$ @Shadow
    //$$ private int levels;
    //#endif

    @Inject(method = "updateBase", at = @At("HEAD"), cancellable = true)
    //#if MC<11700
    //$$ private void updateLevel(int x, int y, int z, CallbackInfo ci) {
    //#else
    private static void updateLevel(Level world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
    //#endif
        if (CarpetAMSAdditionSettings.easyMaxLevelBeacon) {
            BlockPos pos = new BlockPos(x, y - 1, z);
            //#if MC<11700
            //$$ if (this.level != null && this.level.getBlockState(pos).is(BlockTags.BEACON_BASE_BLOCKS)) {
            //#else
            if (world.getBlockState(pos).is(BlockTags.BEACON_BASE_BLOCKS)) {
            //#endif
                //#if MC<11700
                //$$ this.levels = 4;
                //$$ ci.cancel();
                //#else
                cir.setReturnValue(4);
                //#endif
            }
        }
    }
}
