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

package carpetamsaddition.mixin.rule.renewableNetherScrap;

import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    @Nullable
    public abstract ItemEntity spawnAtLocation(ServerLevel par1, ItemStack par2);

    @Unique
    private boolean hasDroppedNetherScrap = false;

    @Unique
    private boolean isDroppingNetherScrap = false;

    @Inject(method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("TAIL"))
    private void dropNetheriteScrap(CallbackInfoReturnable<ItemEntity> cir) {
        if (CarpetAMSAdditionSettings.renewableNetheriteScrap != 0.0D && !this.isDroppingNetherScrap) {
            Entity entity = (Entity) (Object) this;
            if (entity instanceof ZombifiedPiglin && !((ZombifiedPiglin) entity).isBaby() && !this.hasDroppedNetherScrap) {
                this.hasDroppedNetherScrap = true;
                double random = Math.random();
                double rate = CarpetAMSAdditionSettings.renewableNetheriteScrap;
                if (random < rate) {
                    this.isDroppingNetherScrap = true;
                    ItemStack netherScrapStack = new ItemStack(Items.NETHERITE_SCRAP);
                    this.spawnAtLocation((ServerLevel) EntityUtil.getEntityWorld(entity), netherScrapStack);
                    this.isDroppingNetherScrap = false;
                }
            }
        }
    }
}
