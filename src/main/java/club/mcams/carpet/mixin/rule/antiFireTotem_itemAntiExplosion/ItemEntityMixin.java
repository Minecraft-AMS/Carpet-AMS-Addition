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

package club.mcams.carpet.mixin.rule.antiFireTotem_itemAntiExplosion;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
//#if MC>=11900
//$$ import net.minecraft.registry.tag.DamageTypeTags;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    public void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.antiFireTotem && this.getStack().getItem() == Items.TOTEM_OF_UNDYING) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        //#if MC>=11900
        //$$ if(AmsServerSettings.itemAntiExplosion && source.isIn(DamageTypeTags.IS_EXPLOSION)) {
        //#else
        if(AmsServerSettings.itemAntiExplosion && source.isExplosive()) {
            //#endif
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}