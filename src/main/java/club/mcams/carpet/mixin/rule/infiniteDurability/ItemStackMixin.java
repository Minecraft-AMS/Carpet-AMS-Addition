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

package club.mcams.carpet.mixin.rule.infiniteDurability;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
//#if MC>=12005
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(
        //#if MC>=12100
        //$$ method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V",
        //#elseif MC>=12005
        //$$ method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V",
        //#elseif MC>=11900
        //$$ method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
        //#else
        method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
        //#endif
        at = @At("HEAD"),
        cancellable = true
    )
    private void damage(
        //#if MC>=12005
        //$$CallbackInfo ci
        //#else
        CallbackInfoReturnable<Boolean> cir
        //#endif
    ) {
        if (AmsServerSettings.infiniteDurability) {
            //#if MC>=12005
            //$$ ci.cancel();
            //#else
            cir.setReturnValue(false);
            //#endif
        }
    }
}
