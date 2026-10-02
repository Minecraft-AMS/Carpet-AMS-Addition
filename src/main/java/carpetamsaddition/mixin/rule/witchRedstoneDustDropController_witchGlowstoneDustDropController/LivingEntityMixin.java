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

package carpetamsaddition.mixin.rule.witchRedstoneDustDropController_witchGlowstoneDustDropController;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
//#if MC>12006
import net.minecraft.server.level.ServerLevel;
//#endif
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method =
        //#if MC>12006
        "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V",
        //#else
        //$$ "dropFromLootTable(Lnet/minecraft/world/damagesource/DamageSource;Z)V",
        //#endif
        at = @At("TAIL")
    )
    private void customRedstoneDustDrop(
        //#if MC>12006
        ServerLevel world,
        //#endif
        DamageSource source, boolean causedByPlayer, CallbackInfo ci
    ) {
        if (CarpetAMSAdditionSettings.witchRedstoneDustDropController != -1) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            int redstoneCount = CarpetAMSAdditionSettings.witchRedstoneDustDropController;
            compatWitchDropStack(Items.REDSTONE, redstoneCount,
                //#if MC>12006
                world,
                //#endif
                livingEntity
            );
        }
    }

    @Inject(method =
        //#if MC>12006
        "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V",
        //#else
        //$$ "dropFromLootTable(Lnet/minecraft/world/damagesource/DamageSource;Z)V",
        //#endif
        at = @At("TAIL")
    )
    private void customGlowstoneDustDrop(
        //#if MC>12006
        ServerLevel world,
        //#endif
        DamageSource source, boolean causedByPlayer, CallbackInfo ci
    ) {
        if (CarpetAMSAdditionSettings.witchGlowstoneDustDropController != -1) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            int glowstoneCount = CarpetAMSAdditionSettings.witchGlowstoneDustDropController;
            compatWitchDropStack(Items.GLOWSTONE_DUST, glowstoneCount,
                //#if MC>12006
                world,
                //#endif
                livingEntity
            );
        }
    }

    @Unique
    private static void compatWitchDropStack(
        Item item, int count,
        //#if MC>12006
        ServerLevel world,
        //#endif
        LivingEntity livingEntity
    ) {
        if (livingEntity instanceof Witch) {
            livingEntity.spawnAtLocation(
                //#if MC>12006
                world,
                //#endif
                new ItemStack(item, count)
            );
        }
    }
}
