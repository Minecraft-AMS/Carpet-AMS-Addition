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

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Shadow
    @Final
    private static Text CONTAINER_NAME;

    @Redirect(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"),
            require = 0
    )
	private OptionalInt onUse(PlayerEntity playerEntity, NamedScreenHandlerFactory factory) {
        return openHandledScreen(playerEntity);
    }

	@Unique
	private OptionalInt openHandledScreen(PlayerEntity playerEntity) {
        if(AmsServerSettings.largeEnderChest) {
            return playerEntity.openHandledScreen(
                    new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner)
                    -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), CONTAINER_NAME));
        }
        return playerEntity.openHandledScreen(
                new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerEntityInner)
                -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, playerEntityInner.getEnderChestInventory()), CONTAINER_NAME));
    }
}
