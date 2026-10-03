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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.api.command.suggestionProviders.PlayerNameSuggestionProvider;
import carpetamsaddition.helpers.rule.headHunter_commandGetPlayerSkull.SkullSkinHelper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GetPlayerSkullCommand implements AmsCommand {
    @Override
    public void define(CommandBuilder command) {
        command.name("getPlayerSkull")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandGetPlayerSkull)
            .route(Arguments.suggests(Arguments.string("player"), new PlayerNameSuggestionProvider()), Arguments.integer("count", 1, 64))
            .executes(c -> execute(c.player(), c.get(Arguments.string("player")), c.get(Arguments.integer("count", 1, 64))));
    }

    private static int execute(Player player, String name, int count) {
        ItemStack headStack = new ItemStack(Items.PLAYER_HEAD, count);
        SkullSkinHelper.writeNbtToPlayerSkull(name, headStack);
        player.addItem(headStack);
        return 1;
    }
}
