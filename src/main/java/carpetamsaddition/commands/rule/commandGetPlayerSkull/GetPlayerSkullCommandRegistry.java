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

package carpetamsaddition.commands.rule.commandGetPlayerSkull;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.helpers.rule.headHunter_commandGetPlayerSkull.SkullSkinHelper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

public class GetPlayerSkullCommandRegistry {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("getPlayerSkull")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetPlayerSkull))
            .then(Commands.argument("player", StringArgumentType.string())
            .suggests(
                (context, builder) ->
                SharedSuggestionProvider.suggest(context.getSource().getServer().getPlayerNames(), builder)
            )
            .then(Commands.argument("count", IntegerArgumentType.integer(1, 64))
            .executes(context -> execute(
                context.getSource().getPlayerOrException(),
                StringArgumentType.getString(context, "player"),
                IntegerArgumentType.getInteger(context, "count")
            ))))
        );
    }

    private static int execute(Player player, String name, int count) {
        ItemStack headStack = new ItemStack(Items.PLAYER_HEAD, count);
        SkullSkinHelper.writeNbtToPlayerSkull(name, headStack);
        player.addItem(headStack);
        return 1;
    }
}
