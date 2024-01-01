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

package club.mcams.carpet.commands.rule.customBlockBlastResistance;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.config.rule.customBlockHardnessAndBlastResistance.SaveToJson;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Colors;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.utils.compat.LiteralTextUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomBlockBlastResistanceCommandRegistry {
    private final static Translator translator = new Translator("command.customBlockBlastResistance");
    public final static Map<BlockState, Float> CUSTOM_BLOCK_BLAST_RESISTANCE_MAP = new HashMap<>();
    private final static String MESSAGE_HEAD = "<customBlockBlastResistance> ";

    //#if MC<11900
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    //#else
    //$$ public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
    //#endif
        dispatcher.register(
            CommandManager.literal("customBlockBlastResistance")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.customBlockBlastResistance))
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
        String CONFIG_FILE_PATH = getPath(server);
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float oldBlastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            player.sendMessage(
                LiteralTextUtil.createColoredText(
                MESSAGE_HEAD + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + oldBlastResistance +
                " -> " + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + blastResistance,
                Colors.GREEN, true, false
                ),
                false
            );
        } else {
            player.sendMessage(
                LiteralTextUtil.createColoredText(
                MESSAGE_HEAD + "+ " + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + blastResistance,
                Colors.GREEN, true, false
                ),
                false
            );
        }
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, blastResistance);
        SaveToJson.save(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP, CONFIG_FILE_PATH);
        return 1;
    }

    private static int remove(BlockState state, MinecraftServer server, PlayerEntity player) {
        String CONFIG_FILE_PATH = getPath(server);
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float hardness = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.remove(state);
            SaveToJson.save(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP, CONFIG_FILE_PATH);
            player.sendMessage(
                LiteralTextUtil.createColoredText(
                    MESSAGE_HEAD + "- " + RegexTools.getBlockRegisterName(state.getBlock().toString()) + "/" + hardness,
                    Colors.RED, true, false
                ),
                false
            );
            return 1;
        } else {
            player.sendMessage(
                LiteralTextUtil.createColoredText(
                    MESSAGE_HEAD + RegexTools.getBlockRegisterName(state.getBlock().toString()) + translator.tr("not_found").getString(),
                    Colors.RED, true
                ),
                false
            );
            return 0;
        }
    }

    private static int removeAll(MinecraftServer server, PlayerEntity player) {
        String CONFIG_FILE_PATH = getPath(server);
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        SaveToJson.save(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP, CONFIG_FILE_PATH);
        player.sendMessage(
            LiteralTextUtil.createColoredText(
                MESSAGE_HEAD + translator.tr("removeAll").getString(),
                Colors.RED, true
            ),
            false
        );
        return 1;
    }

    private static int list(PlayerEntity player) {
        player.sendMessage(
            LiteralTextUtil.createColoredText(
                translator.tr("list").getString() + "\n-------------------------------",
                Colors.GREEN, true, false
            ),
            false
        );
        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float hardness = entry.getValue();
            Block block = state.getBlock();
            String blockName = RegexTools.getBlockRegisterName(block.toString());
            player.sendMessage(
                LiteralTextUtil.createColoredText(
                    blockName + "/" + hardness,
                    Colors.GREEN
                ),
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
            LiteralTextUtil.createColoredText(
                setHelpText + "\n" +
                removeHelpText + "\n" +
                removeAllHelpText + "\n" +
                listHelpText,
                Colors.GRAY
            ),
            false
        );
        return 1;
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void loadFromJson(String configFilePath) {
        Gson gson = new Gson();
        Path path = Paths.get(configFilePath);
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        if (Files.exists(path)) {
            try {
                String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                Type type = new TypeToken<Map<String, Float>>(){}.getType();
                Map<String, Float> simplifiedMap = gson.fromJson(json, type);
                for (Map.Entry<String, Float> entry : simplifiedMap.entrySet()) {
                    BlockState state = Registry.BLOCK.get(new Identifier(entry.getKey())).getDefaultState();
                    CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, entry.getValue());
                }
            } catch (IOException e) {
                AmsServer.LOGGER.warn("Failed to load config", e);
            }
        }
    }

    public static String getPath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("carpetamsaddition/custom_block_Blast_Resistance" + ".json").toString();
    }
}
