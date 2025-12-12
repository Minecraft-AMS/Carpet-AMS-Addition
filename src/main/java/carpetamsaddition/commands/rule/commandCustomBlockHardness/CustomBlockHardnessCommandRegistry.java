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

package carpetamsaddition.commands.rule.commandCustomBlockHardness;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.config.rule.commandCustomBlockHardness.CustomBlockHardnessConfig;
import carpetamsaddition.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload_S2C;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.NetworkUtil;
import carpetamsaddition.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CustomBlockHardnessCommandRegistry {
    private final static Translator translator = new Translator("command.customBlockHardness");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_HARDNESS_MAP = new ConcurrentHashMap<>();
    public static final Map<Block, Float> DEFAULT_HARDNESS_MAP = new ConcurrentHashMap<>();
    private static final String MESSAGE_HEAD = "<customBlockHardness> ";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customBlockHardness")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomBlockHardness))
            .then(literal("set")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .then(argument("hardness", FloatArgumentType.floatArg())
            .executes(context -> set(
                BlockStateArgument.getBlock(context, "block").getState(),
                FloatArgumentType.getFloat(context, "hardness"),
                context.getSource().getServer(),
                context.getSource().getPlayerOrException()
            )))))
            .then(literal("remove")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> remove(
                BlockStateArgument.getBlock(context, "block").getState(),
                context.getSource().getServer(),
                context.getSource().getPlayerOrException()
            ))))
            .then(literal("removeAll").executes(
                context -> removeAll(context.getSource().getServer(),
                context.getSource().getPlayerOrException())
            ))
            .then(literal("list").executes(context -> list(context.getSource().getPlayerOrException())))
            .then(literal("help").executes(context -> help(context.getSource().getPlayerOrException())))
            .then(literal("getDefaultHardness")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> getDefaultHardness(
                context.getSource().getPlayerOrException(),
                BlockStateArgument.getBlock(context, "block").getState().getBlock()
            ))))
        );
    }

    private static int set(BlockState state, float hardness, MinecraftServer server, Player player) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float oldHardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            player.displayClientMessage(
                Messenger.s(
                    MESSAGE_HEAD + RegexTools.getBlockRegisterName(state) + "/" + oldHardness +
                    " -> " + RegexTools.getBlockRegisterName(state) + "/" + hardness
                ).withStyle(ChatFormatting.GREEN), false
            );
        } else {
            player.displayClientMessage(
                Messenger.s(
                    MESSAGE_HEAD + "+ " + RegexTools.getBlockRegisterName(state) + "/" + hardness
                ).withStyle(ChatFormatting.GREEN), false
            );
        }
        CUSTOM_BLOCK_HARDNESS_MAP.put(state, hardness);
        saveToJson();
        broadcastDataPack(server);
        return 1;
    }

    private static int remove(BlockState state, MinecraftServer server, Player player) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float hardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            CUSTOM_BLOCK_HARDNESS_MAP.remove(state);
            saveToJson();
            broadcastDataPack(server);
            player.displayClientMessage(
                Messenger.s(
                    MESSAGE_HEAD + "- " + RegexTools.getBlockRegisterName(state) + "/" + hardness
                ).withStyle(ChatFormatting.RED), false
            );
            return 1;
        } else {
            player.displayClientMessage(
                Messenger.s(
                    MESSAGE_HEAD + RegexTools.getBlockRegisterName(state) + translator.tr("not_found").getString()
                ).withStyle(ChatFormatting.RED), false
            );
            return 0;
        }
    }

    private static int removeAll(MinecraftServer server, Player player) {
        CUSTOM_BLOCK_HARDNESS_MAP.clear();
        saveToJson();
        broadcastDataPack(server);
        player.displayClientMessage(
            Messenger.s(
                MESSAGE_HEAD + translator.tr("removeAll").getString()
            ).withStyle(ChatFormatting.RED), false
        );
        return 1;
    }

    private static int getDefaultHardness(Player player, Block block) {
        float hardness = CustomBlockHardnessCommandRegistry.DEFAULT_HARDNESS_MAP.get(block);
        String blockName = RegexTools.getBlockRegisterName(block.defaultBlockState());
        player.displayClientMessage(
            Messenger.s(
                String.format("%s%s %s %s", MESSAGE_HEAD, blockName, translator.tr("default_hardness").getString(), hardness)
            ).withStyle(ChatFormatting.GREEN), false
        );
        return 1;
    }

    private static int list(Player player) {
        player.displayClientMessage(
            Messenger.s(
                translator.tr("list").getString() + "\n-------------------------------"
            ).withStyle(ChatFormatting.GREEN), false
        );
        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_HARDNESS_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float hardness = entry.getValue();
            Block block = state.getBlock();
            String blockName = RegexTools.getBlockRegisterName(block.toString());
            player.displayClientMessage(Messenger.s(blockName + "/" + hardness).withStyle(ChatFormatting.GREEN), false);
        }
        return 1;
    }

    private static int help(Player player) {
        final String setHelpText = translator.tr("help.set").getString();
        final String removeHelpText = translator.tr("help.remove").getString();
        final String removeAllHelpText = translator.tr("help.removeAll").getString();
        final String listHelpText = translator.tr("help.list").getString();
        final String getDefaultHardnessHelpText = translator.tr("help.get_default_hardness").getString();
        player.displayClientMessage(
            Messenger.s(
                setHelpText + "\n" +
                removeHelpText + "\n" +
                removeAllHelpText + "\n" +
                getDefaultHardnessHelpText + "\n" +
                listHelpText
            ).withStyle(ChatFormatting.GRAY), false
        );
        return 1;
    }

    private static void broadcastDataPack(MinecraftServer server) {
        NetworkUtil.broadcastDataPack(server, CustomBlockHardnessPayload_S2C.create(CUSTOM_BLOCK_HARDNESS_MAP));
    }

    private static void saveToJson() {
        CustomBlockHardnessConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_HARDNESS_MAP);
    }
}
