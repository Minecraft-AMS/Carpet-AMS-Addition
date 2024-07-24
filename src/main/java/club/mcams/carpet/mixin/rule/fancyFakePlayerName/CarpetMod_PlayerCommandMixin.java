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

package club.mcams.carpet.mixin.rule.fancyFakePlayerName;

import carpet.commands.PlayerCommand;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyNameHelper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.context.CommandContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(PlayerCommand.class)
public abstract class CarpetMod_PlayerCommandMixin {
    @WrapOperation(method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
        ),
        //#if MC<11700
        require = 2,
        //#endif
        remap = false
    )
    private static String spawn(CommandContext<?> context, String name, Operation<String> original) {
       return
           !Objects.equals(AmsServerSettings.fancyFakePlayerName, "false") ?
           FancyNameHelper.addBotNameSuffix(context, name, AmsServerSettings.fancyFakePlayerName) :
           original.call(context, name);
    }

    @WrapOperation(
        method = "cantSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
        ),
        require = 1,
        remap = false
    )
    private static String cantSpawn(CommandContext<?> context, String name, Operation<String> original) {
        return
            !Objects.equals(AmsServerSettings.fancyFakePlayerName, "false") ?
            FancyNameHelper.addBotNameSuffix(context, name, AmsServerSettings.fancyFakePlayerName) :
            original.call(context, name);
    }
}
