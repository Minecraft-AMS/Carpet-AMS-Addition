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

import java.util.OptionalInt;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {

    @Shadow
    @Final
    private static Component CONTAINER_TITLE;

    @WrapOperation(
        method = "useWithoutItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"
        ),
        require = 0
    )
	private OptionalInt onUse(Player playerEntity, MenuProvider namedScreenHandlerFactory, Operation<OptionalInt> original) {
        if (CarpetAMSAdditionSettings.largeEnderChest) {
            SimpleMenuProvider factory = new SimpleMenuProvider((syncId, playerInventory, playerEntityInner) ->
                ChestMenu.sixRows(
                    syncId, playerInventory, playerEntityInner.getEnderChestInventory()
                ), CONTAINER_TITLE
            );
            return playerEntity.openMenu(factory);
        } else {
            return original.call(playerEntity, namedScreenHandlerFactory);
        }
    }
}
