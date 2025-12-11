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

package club.mcams.carpet.commands.rule.commandCustomBlockBlastResistance;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CustomBlockBlastResistanceCommandRegistry {
    private static final Translator translator = new Translator("command.customBlockBlastResistance");
    private static final String MSG_HEAD = "<customBlockBlastResistance> ";
    public static final Map<BlockState, Float> CUSTOM_BLOCK_BLAST_RESISTANCE_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customBlockBlastResistance")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomBlockBlastResistance))
            .then(literal("set")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .then(argument("resistance", FloatArgumentType.floatArg())
            .executes(context -> set(
                BlockStateArgument.getBlock(context, "block").getState(),
                FloatArgumentType.getFloat(context, "resistance"),
                context.getSource().getPlayerOrException()
            )))))
            .then(literal("remove")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> remove(
                BlockStateArgument.getBlock(context, "block").getState(),
                context.getSource().getPlayerOrException()
            ))))
            .then(literal("removeAll").executes(context -> removeAll(context.getSource().getPlayerOrException())))
            .then(literal("list").executes(context -> list(context.getSource().getPlayerOrException())))
            .then(literal("help").executes(context -> help(context.getSource().getPlayerOrException())))
        );
    }

    private static int set(BlockState state, float blastResistance, Player player) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float oldBlastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            player.displayClientMessage(
                Messenger.s(
                    MSG_HEAD + getBlockRegisterName(state) + "/" + oldBlastResistance +
                    " -> " + getBlockRegisterName(state) + "/" + blastResistance
                ).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true)), false
            );
        } else {
            player.displayClientMessage(
                Messenger.s(
                    MSG_HEAD + "+ " + getBlockRegisterName(state) + "/" + blastResistance
                ).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true)), false
            );
        }
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, blastResistance);
        saveToJson();
        return 1;
    }

    private static int remove(BlockState state, Player player) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float blastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.remove(state);
            saveToJson();
            player.displayClientMessage(
                Messenger.s(
                    MSG_HEAD + "- " + getBlockRegisterName(state) + "/" + blastResistance
                ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), false
            );
            return 1;
        } else {
            player.displayClientMessage(
                Messenger.s(
                    MSG_HEAD + getBlockRegisterName(state) + translator.tr("not_found").getString()
                ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), false
            );
            return 0;
        }
    }

    private static int removeAll(Player player) {
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        saveToJson();
        player.displayClientMessage(
            Messenger.s(
                MSG_HEAD + translator.tr("removeAll").getString()
            ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), false
        );
        return 1;
    }

    private static int list(Player player) {
        player.displayClientMessage(
            Messenger.s(
                translator.tr("list").getString() + "\n-------------------------------"
            ).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true)),
            false
        );
        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float blastResistance = entry.getValue();
            String blockName = getBlockRegisterName(state);
            player.displayClientMessage(
                Messenger.s(blockName + " / " + blastResistance).
                setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)), false
            );
        }
        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(Player player) {
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
            ).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false
        );
        return 1;
    }

    private static String getBlockRegisterName(BlockState state) {
        return RegexTools.getBlockRegisterName(state.getBlock().toString());
    }

    private static void saveToJson() {
        CustomBlockBlastResistanceConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP);
    }
}
