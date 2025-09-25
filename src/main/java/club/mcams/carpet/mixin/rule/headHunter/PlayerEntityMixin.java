/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.mixin.rule.headHunter;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.headHunter_commandGetPlayerSkull.SkullSkinHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
//#if MC>=12102
//$$ import net.minecraft.server.world.ServerWorld;
//$$ import club.mcams.carpet.utils.EntityUtil;
//#endif
import net.minecraft.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"
        )
    )
    private void dropPlayerSkull(CallbackInfo ci) {
        if (AmsServerSettings.headHunter) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            ItemStack headStack = new ItemStack(Items.PLAYER_HEAD);
            SkullSkinHelper.writeNbtToPlayerSkull(player, headStack);
            //#if MC>=12102
            //$$ player.dropStack((ServerWorld) EntityUtil.getEntityWorld(player), headStack);
            //#else
            player.dropStack(headStack);
            //#endif
        }
    }
}
