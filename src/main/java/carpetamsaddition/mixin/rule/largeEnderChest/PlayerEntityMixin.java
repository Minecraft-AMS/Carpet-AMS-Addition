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

package carpetamsaddition.mixin.rule.largeEnderChest;

import carpetamsaddition.AmsServerSettings;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin implements PlayerEntityInvoker {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void largeEnderChest(CallbackInfo ci) {
		if (AmsServerSettings.largeEnderChest) {
			final int LARGE_ENDERCHEST_SIZE = 9 * 6;
			SimpleInventoryAccessor simpleInventoryAccessor = (SimpleInventoryAccessor) this.invokeGetEnderChestInventory();
			simpleInventoryAccessor.setSize(LARGE_ENDERCHEST_SIZE);
			simpleInventoryAccessor.setStacks(NonNullList.withSize(LARGE_ENDERCHEST_SIZE, ItemStack.EMPTY));
		}
	}
}
