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

package carpetamsaddition.mixin.rule.maxPlayerInteractionRange;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.commands.AttributeCommand;
import net.minecraft.commands.CommandSourceStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AttributeCommand.class, priority = 1688)
public abstract class AttributeCommandMixin {
    @WrapOperation(
        method = "setAttributeBase",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V"
        )
    )
    private static void disableBlockInteractionRangeSet(AttributeInstance instance, double baseValue, Operation<Void> original, CommandSourceStack source) {
        if (CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange != -1.0D && instance.getAttribute().equals(Attributes.BLOCK_INTERACTION_RANGE) && source != null) {
            Messenger.tell(source, Messenger.f(Messenger.tr("rule.maxPlayerBlockInteractionRange.disable_command"), Layout.RED));
        } else {
            original.call(instance, baseValue);
        }
    }

    @WrapOperation(
        method = "setAttributeBase",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V"
        )
    )
    private static void disableEntityInteractionRangeSet(AttributeInstance instance, double baseValue, Operation<Void> original, CommandSourceStack source) {
        if (CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange != -1.0D && instance.getAttribute().equals(Attributes.ENTITY_INTERACTION_RANGE) && source != null) {
            Messenger.tell(source, Messenger.f(Messenger.tr("rule.maxPlayerEntityInteractionRange.disable_command"), Layout.RED));
        } else {
            original.call(instance, baseValue);
        }
    }
}
