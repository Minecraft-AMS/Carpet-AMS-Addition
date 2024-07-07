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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.block.Block;
//#if MC>=11904
//$$ import net.minecraft.registry.tag.TagKey;
//#elseif MC>=11800 && MC<11900
import net.minecraft.tag.TagKey;
//#else
//$$ import net.minecraft.tag.Tag;
//#endif
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

@Mixin(targets = "net/minecraft/entity/mob/EndermanEntity$PickUpBlockGoal")
public abstract class PickUpBlockGoalMixin {

    @Shadow
    @Final
    private EndermanEntity enderman;
    @WrapOperation(method = "tick()V",at = @At(value = "INVOKE", target =
            //#if MC<11700
            //$$ "Lnet/minecraft/block/Block;isIn(Lnet/minecraft/tag/Tag;)Z"
            //#elseif MC<11904 && MC>=11800
            "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/TagKey;)Z"
            //#elseif MC<11904
            //$$ "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/Tag;)Z"
            //#else
            //$$ "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
            //#endif
    ))
    private boolean isBlockInTag(
                                 //#if MC<11700
                                 //$$ Block instance,
                                 //#else
                                 BlockState instance,
                                 //#endif
                                 //#if MC>=11800 && MC<11900
                                 TagKey tag,
                                 //#elseif MC<11904
                                 //$$ Tag tag,
                                 //#else
                                 //$$ TagKey tag,
                                 //#endif
                                 Operation<Boolean> original) {
        return original.call(instance, tag);
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
