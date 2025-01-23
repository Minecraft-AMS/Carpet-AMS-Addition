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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.ItemEntity;
//#if MC>=12102
//$$ import net.minecraft.server.world.ServerWorld;
//#endif
import net.minecraft.item.Items;
//#if MC>=11900
//$$ import net.minecraft.registry.tag.DamageTypeTags;
//#endif
import net.minecraft.entity.damage.DamageSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements ItemEntityInvoker {
    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    private boolean isFireImmune(boolean original) {
        if (AmsServerSettings.antiFireTotem && this.invokeGetStack().getItem() == Items.TOTEM_OF_UNDYING) {
            return true;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "damage", at = @At("RETURN"))
    private boolean damage(boolean original, DamageSource source) {
        //#if MC>=11900
        //$$ if(AmsServerSettings.itemAntiExplosion && source.isIn(DamageTypeTags.IS_EXPLOSION)) {
        //#else
        if (AmsServerSettings.itemAntiExplosion && source.isExplosive()) {
        //#endif
            return false;
        } else {
            return original;
        }
    }
}