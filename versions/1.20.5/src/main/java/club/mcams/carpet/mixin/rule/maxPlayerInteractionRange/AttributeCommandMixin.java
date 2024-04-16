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

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Messenger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.AttributeCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.20.5")
@Mixin(AttributeCommand.class)
public abstract class AttributeCommandMixin {

    @Unique
    private static final Translator translator = new Translator("rule");

    @Unique
    private static final String MSG_HEAD = "<Carpet AMS Addition> ";

    @Inject(method = "executeBaseValueSet", at = @At("HEAD"), cancellable = true)
    private static void executeBaseValueSet1(ServerCommandSource source, Entity target, RegistryEntry<EntityAttribute> attribute, double value, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.maxPlayerBlockInteractionRange != -1.0D && attribute.equals(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE)) {
            PlayerEntity player = source.getPlayer();
            if (player != null) {
                player.sendMessage(
                    Messenger.s(
                        MSG_HEAD + translator.tr("maxPlayerBlockInteractionRange.disable_command").getString()
                    ).formatted(Formatting.RED)
                );
            }
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "executeBaseValueSet", at = @At("HEAD"), cancellable = true)
    private static void executeBaseValueSet2(ServerCommandSource source, Entity target, RegistryEntry<EntityAttribute> attribute, double value, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.maxPlayerEntityInteractionRange != -1.0D && attribute.equals(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE)) {
            PlayerEntity player = source.getPlayer();
            if (player != null) {
                player.sendMessage(
                    Messenger.s(
                        MSG_HEAD + translator.tr("maxPlayerEntityInteractionRange.disable_command").getString()
                    ).formatted(Formatting.RED)
                );
            }
            cir.setReturnValue(null);
        }
    }
}
