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

package club.mcams.carpet.mixin.rule.commandCustomCommandPermissionLevel;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.server.command.ServerCommandSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = CommandDispatcher.class, priority = 1688)
public abstract class CommandDispatcherMixin {
    @Inject(method = "register", at = @At("HEAD"), remap = false)
    private void saveDefaultRequirement(LiteralArgumentBuilder<ServerCommandSource> command, CallbackInfoReturnable<LiteralCommandNode<ServerCommandSource>> cir) {
        CustomCommandPermissionLevelRegistry.DEFAULT_PERMISSION_MAP.putIfAbsent(command.getLiteral(), command.getRequirement());
    }

    @Inject(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;build()Lcom/mojang/brigadier/tree/LiteralCommandNode;"
        ),
        remap = false
    )
    private void modifyPermissionLevel(LiteralArgumentBuilder<ServerCommandSource> command, CallbackInfoReturnable<LiteralCommandNode<ServerCommandSource>> cir) {
        if (!Objects.equals(AmsServerSettings.commandCustomCommandPermissionLevel, "false") && CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.containsKey(command.getLiteral())) {
            int level = CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.get(command.getLiteral());
            command.requires(source -> source.hasPermissionLevel(level));
        }
    }
}
