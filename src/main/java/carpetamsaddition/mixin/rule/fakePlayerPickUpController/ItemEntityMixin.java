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

package carpetamsaddition.mixin.rule.fakePlayerPickUpController;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.FakePlayerHelper;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void onCanReplaceCurrentItem(Player player, CallbackInfo ci) {
        if (!Objects.equals(CarpetAMSAdditionSettings.fakePlayerPickUpController, "false") && FakePlayerHelper.isFakePlayer(player)) {
            ItemStack mainHandStack = player.getMainHandItem();
            if (Objects.equals(CarpetAMSAdditionSettings.fakePlayerPickUpController, "MainHandOnly") && !mainHandStack.isEmpty()) {
                ci.cancel();
            } else if (Objects.equals(CarpetAMSAdditionSettings.fakePlayerPickUpController, "NoPickUp")) {
                ci.cancel();
            }
        }
    }
}
