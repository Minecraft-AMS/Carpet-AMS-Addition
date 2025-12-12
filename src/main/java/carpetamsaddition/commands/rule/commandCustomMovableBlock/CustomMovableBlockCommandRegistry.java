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

package carpetamsaddition.commands.rule.commandCustomMovableBlock;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.config.rule.commandCustomMovableBlock.CustomMovableBlockConfig;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CustomMovableBlockCommandRegistry {
    private static final Translator translator = new Translator("command.customMovableBlock");
    private static final String MSG_HEAD = "<customMovableBlock> ";
    public static final List<String> CUSTOM_MOVABLE_BLOCKS = new ArrayList<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customMovableBlock")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomMovableBlock))
            .then(literal("add")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> add(
                context.getSource().getPlayerOrException(),
                BlockStateArgument.getBlock(context, "block").getState()
            ))))
            .then(literal("remove")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> remove(
                context.getSource().getPlayerOrException(),
                BlockStateArgument.getBlock(context, "block").getState())
            )))
            .then(literal("removeAll")
            .executes(context -> removeAll(context.getSource().getPlayerOrException())))
            .then(literal("list")
            .executes(context -> list(context.getSource().getPlayerOrException())))
            .then(literal("help")
            .executes(context -> help(context.getSource().getPlayerOrException())))
        );
    }

    private static int add(Player player, BlockState blockState) {
        if (!CUSTOM_MOVABLE_BLOCKS.contains(getBlockName(blockState))) {
            CUSTOM_MOVABLE_BLOCKS.add(getBlockName(blockState));
            saveToJson();
            player.displayClientMessage(Messenger.s(MSG_HEAD + "+ " + getBlockName(blockState)).withStyle(ChatFormatting.GREEN), false);
        } else {
            player.displayClientMessage(
                Messenger.s(
                    MSG_HEAD + getBlockName(blockState) + translator.tr("already_exists").getString()
                ).withStyle(ChatFormatting.YELLOW), false
            );
        }
        return 1;
    }

    private static int remove(Player player, BlockState blockState) {
        if (CUSTOM_MOVABLE_BLOCKS.contains(getBlockName(blockState))) {
            CUSTOM_MOVABLE_BLOCKS.remove(getBlockName(blockState));
            saveToJson();
            player.displayClientMessage(Messenger.s(MSG_HEAD + "- " + getBlockName(blockState)).withStyle(ChatFormatting.RED), false);
        } else {
            player.displayClientMessage(Messenger.s(MSG_HEAD + getBlockName(blockState) + translator.tr("not_found").getString()).withStyle(ChatFormatting.RED), false);
        }
        return 1;
    }

    private static int removeAll(Player player) {
        CUSTOM_MOVABLE_BLOCKS.clear();
        saveToJson();
        player.displayClientMessage(Messenger.s(MSG_HEAD + translator.tr("removeAll").getString()).withStyle(ChatFormatting.RED), false);
        return 1;
    }

    private static int list(ServerPlayer player) {
        player.displayClientMessage(
            Messenger.s(
            translator.tr("list").getString() + "\n-------------------------------").withStyle(ChatFormatting.GREEN),
            false
        );
        for (String blockName : CUSTOM_MOVABLE_BLOCKS) {
            player.displayClientMessage(Messenger.s(blockName).withStyle(ChatFormatting.GREEN), false);
        }
        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(ServerPlayer player) {
        String setHelpText = translator.tr("help.set").getString();
        String removeHelpText = translator.tr("help.remove").getString();
        String removeAllHelpText = translator.tr("help.removeAll").getString();
        String listHelpText = translator.tr("help.list").getString();
        player.displayClientMessage(
            Messenger.s(
                "\n" +
                setHelpText + "\n" +
                removeHelpText + "\n" +
                removeAllHelpText + "\n" +
                listHelpText
            ).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)),
            false
        );
        return 1;
    }

    private static void saveToJson() {
        CustomMovableBlockConfig.getInstance().saveToJson(CUSTOM_MOVABLE_BLOCKS);
    }

    private static String getBlockName(BlockState blockState) {
        return RegexTools.getBlockRegisterName(blockState);
    }
}
