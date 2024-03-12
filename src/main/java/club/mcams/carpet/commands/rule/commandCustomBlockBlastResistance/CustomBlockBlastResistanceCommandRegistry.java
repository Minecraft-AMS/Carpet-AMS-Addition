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
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.utils.compat.LiteralTextUtil;
import club.mcams.carpet.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomBlockBlastResistanceCommandRegistry {
    private static final Translator translator = new Translator("command.customBlockBlastResistance");
    private static final String MSG_HEAD = "<customBlockBlastResistance> ";
    public static final Map<BlockState, Float> CUSTOM_BLOCK_BLAST_RESISTANCE_MAP = new HashMap<>();

    //#if MC<11900
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    //#else
    //$$ public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
    //#endif
        dispatcher.register(
            CommandManager.literal("customBlockBlastResistance")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomBlockBlastResistance))
            .then(literal("set")
            //#if MC<11900
            .then(argument("block", BlockStateArgumentType.blockState())
            //#else
            //$$ .then(argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
            //#endif
            .then(argument("resistance", FloatArgumentType.floatArg())
            .executes(context -> set(
                BlockStateArgumentType.getBlockState(context, "block").getBlockState(),
                FloatArgumentType.getFloat(context, "resistance"),
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
            .then(literal("removeAll").executes(context -> removeAll(context.getSource().getServer(), context.getSource().getPlayer())))
            .then(literal("list").executes(context -> list(context.getSource().getPlayer())))
            .then(literal("help").executes(context -> help(context.getSource().getPlayer())))
        );
    }

    private static int set(BlockState state, float blastResistance, MinecraftServer server, PlayerEntity player) {
        String CONFIG_FILE_PATH = CustomBlockBlastResistanceConfig.getPath(server);
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float oldBlastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            player.sendMessage(
                LiteralTextUtil.compatText(
                MSG_HEAD + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + oldBlastResistance +
                " -> " + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + blastResistance
                ).setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)),
                false
            );
        } else {
            player.sendMessage(
                LiteralTextUtil.compatText(
                MSG_HEAD + "+ " + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + blastResistance
                ).setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)),
                false
            );
        }
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, blastResistance);
        CustomBlockBlastResistanceConfig.saveToJson(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP, CONFIG_FILE_PATH);
        return 1;
    }

    private static int remove(BlockState state, MinecraftServer server, PlayerEntity player) {
        String CONFIG_FILE_PATH = CustomBlockBlastResistanceConfig.getPath(server);
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float blastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.remove(state);
            CustomBlockBlastResistanceConfig.saveToJson(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP, CONFIG_FILE_PATH);
            player.sendMessage(
                LiteralTextUtil.compatText(
                    MSG_HEAD + "- " + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + blastResistance
                ).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)),
                false
            );
            return 1;
        } else {
            player.sendMessage(
                LiteralTextUtil.compatText(
                    MSG_HEAD + RegexTools.getBlockRegisterName(state.getBlock().toString()) + translator.tr("not_found").getString()
                ).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)),
                false
            );
            return 0;
        }
    }

    private static int removeAll(MinecraftServer server, PlayerEntity player) {
        String CONFIG_FILE_PATH = CustomBlockBlastResistanceConfig.getPath(server);
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        CustomBlockBlastResistanceConfig.saveToJson(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP, CONFIG_FILE_PATH);
        player.sendMessage(
            LiteralTextUtil.compatText(
                MSG_HEAD + translator.tr("removeAll").getString()
            ).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)),
            false
        );
        return 1;
    }

    private static int list(PlayerEntity player) {
        player.sendMessage(
            LiteralTextUtil.compatText(
                translator.tr("list").getString() + "\n-------------------------------"
            ).setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)),
            false
        );
        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float blastResistance = entry.getValue();
            Block block = state.getBlock();
            String blockName = RegexTools.getBlockRegisterName(block.toString());
            player.sendMessage(
                LiteralTextUtil.compatText(blockName + " / " + blastResistance).
                setStyle(Style.EMPTY.withColor(Formatting.GREEN)),
                false
            );
        }
        return 1;
    }

    private static int help(PlayerEntity player) {
        String setHelpText = translator.tr("help.set").getString();
        String removeHelpText = translator.tr("help.remove").getString();
        String removeAllHelpText = translator.tr("help.removeAll").getString();
        String listHelpText = translator.tr("help.list").getString();
        player.sendMessage(
            LiteralTextUtil.compatText(
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
}
