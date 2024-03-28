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

package club.mcams.carpet.mixin.rule.maxPlayerInteractionRange;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

//#if MC>=12005
//$$ import club.mcams.carpet.AmsServerSettings;
//$$ import net.minecraft.entity.attribute.EntityAttribute;
//$$ import net.minecraft.entity.attribute.EntityAttributes;
//$$ import net.minecraft.entity.attribute.EntityAttributeInstance;
//$$ import net.minecraft.registry.entry.RegistryEntry;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//#else
import club.mcams.carpet.utils.compat.DummyClass;
//#endif

import org.spongepowered.asm.mixin.Mixin;

@GameVersion(version = "Minecraft >= 1.20.5")
//#if MC>=12005
//$$ @Mixin(value = EntityAttributeInstance.class)
//#else
@Mixin(DummyClass.class)
//#endif
public abstract class EntityAttributeInstanceMixin {
//#if MC>=12005

    //$$ @Shadow
    //$$ public abstract RegistryEntry<EntityAttribute> getAttribute();

    //$$ @ModifyReturnValue(method = "getBaseValue", at = @At("RETURN"))
    //$$ private double getPlayerBlockInteractionBaseValue(double original) {
    //$$     return AmsServerSettings.maxPlayerBlockInteractionRange != -1.0D && getAttribute().equals(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE) ? AmsServerSettings.maxPlayerBlockInteractionRange : original;
    //$$ }

    //$$ @ModifyReturnValue(method = "getBaseValue", at = @At("RETURN"))
    //$$ private double getPlayerEntityInteractionBaseValue(double original) {
    //$$     return AmsServerSettings.maxPlayerEntityInteractionRange != -1.0D && getAttribute().equals(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE) ? AmsServerSettings.maxPlayerEntityInteractionRange : original;
    //$$ }

//#endif
}
