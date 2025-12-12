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

package carpetamsaddition.mixin.rule.jebSheepDropRandomColorWool;

import carpetamsaddition.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;
import java.util.Random;

@Mixin(Sheep.class)
public abstract class SheepEntityMixin {
    @WrapOperation(
        method = "shear",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/sheep/Sheep;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"
        )
    )
    private void redirectDropStack(Sheep sheepEntity, ServerLevel world, ResourceKey<@NotNull LootTable> registryKey, ItemStack shears, BiConsumer<ServerLevel, ItemStack> consumer, Operation<Void> original) {
        if (AmsServerSettings.jebSheepDropRandomColorWool && isJebSheep(sheepEntity)) {
            Random random = new Random();
            for (int i = 0; i < 1 + random.nextInt(3); ++i) {
                DyeColor randomColor = DyeColor.values()[random.nextInt(DyeColor.values().length)];
                Block coloredWoolBlock = getWoolBlockFromColor(randomColor);
                consumer.accept(world, new ItemStack(coloredWoolBlock.asItem()));
            }
        } else {
            original.call(sheepEntity, world, registryKey, shears, consumer);
        }
    }

    @Unique
    private static boolean isJebSheep(Sheep sheepEntity) {
        return sheepEntity.hasCustomName() && sheepEntity.getCustomName() != null && sheepEntity.getCustomName().getString().equals("jeb_");
    }

    @Unique
    private static Block getWoolBlockFromColor(DyeColor color) {
        return switch (color) {
            case WHITE -> Blocks.WHITE_WOOL;
            case ORANGE -> Blocks.ORANGE_WOOL;
            case MAGENTA -> Blocks.MAGENTA_WOOL;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_WOOL;
            case YELLOW -> Blocks.YELLOW_WOOL;
            case LIME -> Blocks.LIME_WOOL;
            case PINK -> Blocks.PINK_WOOL;
            case GRAY -> Blocks.GRAY_WOOL;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_WOOL;
            case CYAN -> Blocks.CYAN_WOOL;
            case PURPLE -> Blocks.PURPLE_WOOL;
            case BLUE -> Blocks.BLUE_WOOL;
            case BROWN -> Blocks.BROWN_WOOL;
            case GREEN -> Blocks.GREEN_WOOL;
            case RED -> Blocks.RED_WOOL;
            case BLACK -> Blocks.BLACK_WOOL;
        };
    }
}
