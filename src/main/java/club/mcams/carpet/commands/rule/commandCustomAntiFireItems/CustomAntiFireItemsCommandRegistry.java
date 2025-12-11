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

package club.mcams.carpet.commands.rule.commandCustomAntiFireItems;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.config.rule.commandAntiFireItems.CustomAntiFireItemsConfig;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomAntiFireItemsCommandRegistry {
    private static final Translator translator = new Translator("command.customAntiFireItems");
    private static final String MSG_HEAD = "<customAntiFireItems> ";
    public static final List<String> CUSTOM_ANTI_FIRE_ITEMS = new ArrayList<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(
            CommandManager.literal("customAntiFireItems")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomAntiFireItems))
            .then(literal("add")
            .then(argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
            .executes(context -> add(
                context.getSource().getPlayerOrThrow(),
                ItemStackArgumentType.getItemStackArgument(context, "item").createStack(1, false)
            ))))
            .then(literal("remove")
            .then(argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
            .executes(context -> remove(
                context.getSource().getPlayerOrThrow(),
                ItemStackArgumentType.getItemStackArgument(context, "item").createStack(1, false)
            ))))
            .then(literal("removeAll")
            .executes(context -> removeAll(context.getSource().getPlayerOrThrow())))
            .then(literal("list")
            .executes(context -> list(context.getSource().getPlayerOrThrow())))
            .then(literal("help")
            .executes(context -> help(context.getSource().getPlayerOrThrow())))
        );
    }

    private static int add(PlayerEntity player, ItemStack itemStack) {
        if (!CUSTOM_ANTI_FIRE_ITEMS.contains(getItemName(itemStack))) {
            CUSTOM_ANTI_FIRE_ITEMS.add(getItemName(itemStack));
            saveToJson();
            player.sendMessage(Messenger.s(MSG_HEAD + "+ " + getItemName(itemStack)).formatted(Formatting.GREEN), false);
        } else {
            player.sendMessage(
                Messenger.s(
                    MSG_HEAD + getItemName(itemStack) + translator.tr("already_exists").getString()
                ).formatted(Formatting.YELLOW), false
            );
        }
        return 1;
    }

    private static int remove(PlayerEntity player, ItemStack itemStack) {
        if (CUSTOM_ANTI_FIRE_ITEMS.contains(getItemName(itemStack))) {
            CUSTOM_ANTI_FIRE_ITEMS.remove(getItemName(itemStack));
            saveToJson();
            player.sendMessage(Messenger.s(MSG_HEAD + "- " + getItemName(itemStack)).formatted(Formatting.RED), false);
        } else {
            player.sendMessage(Messenger.s(MSG_HEAD + getItemName(itemStack) + translator.tr("not_found").getString()).formatted(Formatting.RED), false);
        }
        return 1;
    }

    private static int removeAll(PlayerEntity player) {
        CUSTOM_ANTI_FIRE_ITEMS.clear();
        saveToJson();
        player.sendMessage(Messenger.s(MSG_HEAD + translator.tr("removeAll").getString()).formatted(Formatting.RED), false);
        return 1;
    }

    private static int list(ServerPlayerEntity player) {
        player.sendMessage(
            Messenger.s(
            translator.tr("list").getString() + "\n-------------------------------").formatted(Formatting.GREEN),
            false
        );
        for (String blockName : CUSTOM_ANTI_FIRE_ITEMS) {
            player.sendMessage(Messenger.s(blockName).formatted(Formatting.GREEN), false);
        }
        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(ServerPlayerEntity player) {
        String setHelpText = translator.tr("help.set").getString();
        String removeHelpText = translator.tr("help.remove").getString();
        String removeAllHelpText = translator.tr("help.removeAll").getString();
        String listHelpText = translator.tr("help.list").getString();
        player.sendMessage(
            Messenger.s(
                "\n" +
                setHelpText + "\n" +
                removeHelpText + "\n" +
                removeAllHelpText + "\n" +
                listHelpText
            ).setStyle(Style.EMPTY.withColor(Formatting.GRAY)),
            false
        );
        return 1;
    }

    private static void saveToJson() {
        CustomAntiFireItemsConfig.getInstance().saveToJson(CUSTOM_ANTI_FIRE_ITEMS);
    }

    private static String getItemName(ItemStack stack) {
        return RegexTools.getItemRegisterName(stack);
    }
}
