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

package club.mcams.carpet.mixin.rule.largeEnderChest;

import java.util.OptionalInt;

import carpet.CarpetSettings;
import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMinix {
	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"), require = 0)
	private OptionalInt onUse(PlayerEntity playerEntity, NamedScreenHandlerFactory factory) {
		return openHandledScreen(playerEntity);
	}
	private OptionalInt openHandledScreen(PlayerEntity playerEntity) {
		if(AmsServerSettings.largeEnderChest) {
			if(CarpetSettings.language.equals("zh_cn")) {
				return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("末影�?")));
			} else if(CarpetSettings.language.equals("zh_tw")) {
				return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("終界�?")));
			} else {
				return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("Ender Chest")));
			}
		} else {
			if(CarpetSettings.language.equals("zh_cn")) {
				return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("末影�?")));
			} else if(CarpetSettings.language.equals("zh_tw")) {
				return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("終界�?")));
			} else {
				return playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), Text.of("Ender Chest")));
			}
		}
	}
}
