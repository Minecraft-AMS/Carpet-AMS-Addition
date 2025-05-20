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

package club.mcams.carpet.commands.rule.commandCustomBlockHardness;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.config.rule.commandCustomBlockHardness.CustomBlockHardnessConfig;
import club.mcams.carpet.network.rule.commandCustomBlockHardness.CustomBlockHardnessS2CPacket;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomBlockHardnessCommandRegistry {
    private final static Translator translator = new Translator("command.customBlockHardness");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_HARDNESS_MAP = new ConcurrentHashMap<>();
    @SuppressWarnings("unused") // Minecraft < 1.17
    public static final Map<BlockState, Float> DEFAULT_HARDNESS_MAP = new ConcurrentHashMap<>();
    private static final String MESSAGE_HEAD = "<customBlockHardness> ";

    //#if MC<11900
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    //#else
    //$$ public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
    //#endif
        dispatcher.register(
            CommandManager.literal("customBlockHardness")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomBlockHardness))
            .then(literal("set")
            //#if MC<11900
            .then(argument("block", BlockStateArgumentType.blockState())
            //#else
            //$$ .then(argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
            //#endif
            .then(argument("hardness", FloatArgumentType.floatArg())
            .executes(context -> set(
                BlockStateArgumentType.getBlockState(context, "block").getBlockState(),
                FloatArgumentType.getFloat(context, "hardness"),
                context.getSource().getServer(),
                context.getSource().getPlayer()
            )))))
            .then(literal("remove")
            //#if MC<11900
            .then(argument("block", BlockStateArgumentType.blockState())
            //#else
            //$$ .then(argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
            //#endif
            .executes(context -> remove(
                BlockStateArgumentType.getBlockState(context, "block").getBlockState(),
                context.getSource().getServer(),
                context.getSource().getPlayer()
            ))))
            .then(literal("removeAll").executes(
                context -> removeAll(context.getSource().getServer(),
                context.getSource().getPlayer())
            ))
            .then(literal("list").executes(context -> list(context.getSource().getPlayer())))
            .then(literal("help").executes(context -> help(context.getSource().getPlayer())))
            .then(literal("getDefaultHardness")
            //#if MC<11900
            .then(argument("block", BlockStateArgumentType.blockState())
            //#else
            //$$ .then(argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
            //#endif
            .executes(context -> getDefaultHardness(
                context.getSource().getPlayer(),
                BlockStateArgumentType.getBlockState(context, "block").getBlockState()
            ))))
        );
    }

    private static int set(BlockState state, float hardness, MinecraftServer server, PlayerEntity player) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float oldHardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            player.sendMessage(
                Messenger.s(
                    MESSAGE_HEAD + RegexTools.getBlockRegisterName(state) + "/" + oldHardness +
                    " -> " + RegexTools.getBlockRegisterName(state) + "/" + hardness
                ).formatted(Formatting.GREEN), false
            );
        } else {
            player.sendMessage(
                Messenger.s(
                    MESSAGE_HEAD + "+ " + RegexTools.getBlockRegisterName(state) + "/" + hardness
                ).formatted(Formatting.GREEN), false
            );
        }
        CUSTOM_BLOCK_HARDNESS_MAP.put(state, hardness);
        saveToJson();
        sendSyncPacketToAllOnlinePlayer(server);
        return 1;
    }

    private static int remove(BlockState state, MinecraftServer server, PlayerEntity player) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float hardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            CUSTOM_BLOCK_HARDNESS_MAP.remove(state);
            saveToJson();
            sendSyncPacketToAllOnlinePlayer(server);
            player.sendMessage(
                Messenger.s(
                    MESSAGE_HEAD + "- " + RegexTools.getBlockRegisterName(state) + "/" + hardness
                ).formatted(Formatting.RED), false
            );
            return 1;
        } else {
            player.sendMessage(
                Messenger.s(
                    MESSAGE_HEAD + RegexTools.getBlockRegisterName(state) + translator.tr("not_found").getString()
                ).formatted(Formatting.RED), false
            );
            return 0;
        }
    }

    private static int removeAll(MinecraftServer server, PlayerEntity player) {
        CUSTOM_BLOCK_HARDNESS_MAP.clear();
        saveToJson();
        sendSyncPacketToAllOnlinePlayer(server);
        player.sendMessage(
            Messenger.s(
                MESSAGE_HEAD + translator.tr("removeAll").getString()
            ).formatted(Formatting.RED), false
        );
        return 1;
    }

    private static int getDefaultHardness(PlayerEntity player, BlockState state) {
        //#if MC>=11700
        float hardness = state.getBlock().getHardness();
        //#else
        //$$ float hardness = CustomBlockHardnessCommandRegistry.DEFAULT_HARDNESS_MAP.get(state);
        //#endif
        String blockName = RegexTools.getBlockRegisterName(state);
        player.sendMessage(
            Messenger.s(
                String.format("%s%s %s %s", MESSAGE_HEAD, blockName, translator.tr("default_hardness").getString(), hardness)
            ).formatted(Formatting.GREEN), false
        );
        return 1;
    }

    private static int list(PlayerEntity player) {
        player.sendMessage(
            Messenger.s(
                translator.tr("list").getString() + "\n-------------------------------"
            ).formatted(Formatting.GREEN), false
        );
        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_HARDNESS_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float hardness = entry.getValue();
            Block block = state.getBlock();
            String blockName = RegexTools.getBlockRegisterName(block.toString());
            player.sendMessage(Messenger.s(blockName + "/" + hardness).formatted(Formatting.GREEN), false);
        }
        return 1;
    }

    private static int help(PlayerEntity player) {
        final String setHelpText = translator.tr("help.set").getString();
        final String removeHelpText = translator.tr("help.remove").getString();
        final String removeAllHelpText = translator.tr("help.removeAll").getString();
        final String listHelpText = translator.tr("help.list").getString();
        final String getDefaultHardnessHelpText = translator.tr("help.get_default_hardness").getString();
        player.sendMessage(
            Messenger.s(
                setHelpText + "\n" +
                removeHelpText + "\n" +
                removeAllHelpText + "\n" +
                getDefaultHardnessHelpText + "\n" +
                listHelpText
            ).formatted(Formatting.GRAY), false
        );
        return 1;
    }

    private static void sendSyncPacketToAllOnlinePlayer(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(CustomBlockHardnessS2CPacket::sendToPlayer);
    }

    private static void saveToJson() {
        CustomBlockHardnessConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_HARDNESS_MAP);
    }
}