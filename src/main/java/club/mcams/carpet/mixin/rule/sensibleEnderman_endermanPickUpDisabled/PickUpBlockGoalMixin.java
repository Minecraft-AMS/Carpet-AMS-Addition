/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.sensibleEnderman_endermanPickUpDisabled;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

//#if MC<11900
import java.util.Random;
//#else
//$$ import net.minecraft.util.math.random.Random;
//#endif

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.hit.BlockHitResult;
//#if MC>=11700
import net.minecraft.world.event.GameEvent;
//#endif
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal")
public abstract class PickUpBlockGoalMixin {

    @Shadow
    @Final
    private EndermanEntity enderman;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (AmsServerSettings.sensibleEnderman) {
            Random random = this.enderman.getRandom();
            //#if MC<11900
            World world = this.enderman.world;
            //#else
            //$$ World world = this.enderman.getWorld();
            //#endif
            int i = MathHelper.floor(this.enderman.getX() - 2.0 + random.nextDouble() * 4.0);
            int j = MathHelper.floor(this.enderman.getY() + random.nextDouble() * 3.0);
            int k = MathHelper.floor(this.enderman.getZ() - 2.0 + random.nextDouble() * 4.0);
            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPos);
            //#if MC>=11700
            Vec3d vec3d = new Vec3d((double)this.enderman.getBlockX() + 0.5, (double)j + 0.5, (double)this.enderman.getBlockZ() + 0.5);
            //#else
            //$$ Vec3d vec3d = new Vec3d((double)this.enderman.getX() + 0.5, (double)j + 0.5, (double)this.enderman.getZ() + 0.5);
            //#endif
            Vec3d vec3d2 = new Vec3d((double)i + 0.5, (double)j + 0.5, (double)k + 0.5);
            BlockHitResult blockHitResult = world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this.enderman));
            boolean isBlockHit = blockHitResult.getBlockPos().equals(blockPos);
            if ((blockState.getBlock() == Blocks.PUMPKIN || blockState.getBlock() == Blocks.MELON) && isBlockHit) {
                world.removeBlock(blockPos, false);
                //#if MC>=11700
                world.emitGameEvent(this.enderman, GameEvent.BLOCK_DESTROY, blockPos);
                //#endif
                this.enderman.setCarriedBlock(blockState.getBlock().getDefaultState());
            }
            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "canStart", at = @At("RETURN"))
    private boolean canStart(boolean original) {
        if (AmsServerSettings.endermanPickUpDisabled) {
            return false;
        } else {
            return original;
        }
    }
}
