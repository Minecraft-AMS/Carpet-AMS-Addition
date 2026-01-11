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
import club.mcams.carpet.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload_S2C;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgumentType;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomBlockHardnessCommandRegistry {
    private final static Translator tr = new Translator("command.customBlockHardness");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_HARDNESS_MAP = new ConcurrentHashMap<>();
    public static final Map<Block, Float> DEFAULT_HARDNESS_MAP = new ConcurrentHashMap<>();

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
                context.getSource(),
                BlockStateArgumentType.getBlockState(context, "block").getBlockState(),
                FloatArgumentType.getFloat(context, "hardness")
            )))))
            .then(literal("remove")
            //#if MC<11900
            .then(argument("block", BlockStateArgumentType.blockState())
            //#else
            //$$ .then(argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
            //#endif
            .executes(context -> remove(
                context.getSource(),
                BlockStateArgumentType.getBlockState(context, "block").getBlockState()
            ))))
            .then(literal("removeAll").executes(
                context -> removeAll(context.getSource()
            )))
            .then(literal("list").executes(context -> list(context.getSource())))
            .then(literal("help").executes(context -> help(context.getSource())))
            .then(literal("getDefaultHardness")
            //#if MC<11900
            .then(argument("block", BlockStateArgumentType.blockState())
            //#else
            //$$ .then(argument("block", BlockStateArgumentType.blockState(commandRegistryAccess))
            //#endif
            .executes(context -> getDefaultHardness(
                context.getSource(),
                BlockStateArgumentType.getBlockState(context, "block").getBlockState().getBlock()
            ))))
        );
    }

    private static int set(ServerCommandSource source, BlockState state, float hardness) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float oldHardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            Messenger.tell(
                source, Messenger.f(tr.tr(
                    "modify_set",
                    RegexTools.getBlockRegisterName(state),
                    oldHardness,
                    RegexTools.getBlockRegisterName(state),
                    hardness
                ), Layout.GREEN, Layout.BOLD)
            );
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("set", RegexTools.getBlockRegisterName(state), hardness), Layout.GREEN, Layout.BOLD));
        }
        CUSTOM_BLOCK_HARDNESS_MAP.put(state, hardness);
        saveToJson();
        broadcastDataPack();
        return 1;
    }

    private static int remove(ServerCommandSource source, BlockState state) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float hardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            CUSTOM_BLOCK_HARDNESS_MAP.remove(state);
            saveToJson();
            broadcastDataPack();

            Messenger.tell(source, Messenger.f(tr.tr("remove", RegexTools.getBlockRegisterName(state), hardness), Layout.RED, Layout.BOLD));

            return 1;
        } else {
            Messenger.tell(
                    source, Messenger.f(tr.tr("not_found", RegexTools.getBlockRegisterName(state)), Layout.RED, Layout.BOLD)
            );

            return 0;
        }
    }

    private static int removeAll(ServerCommandSource source) {
        CUSTOM_BLOCK_HARDNESS_MAP.clear();
        saveToJson();
        broadcastDataPack();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.GREEN, Layout.BOLD));
        return 1;
    }

    private static int getDefaultHardness(ServerCommandSource source, Block block) {
        float hardness = CustomBlockHardnessCommandRegistry.DEFAULT_HARDNESS_MAP.get(block);
        String blockName = RegexTools.getBlockRegisterName(block.getDefaultState());
        Messenger.tell(source, Messenger.f(tr.tr("default_hardness", blockName, hardness), Layout.GREEN, Layout.BOLD));
        return 1;
    }

    private static int list(ServerCommandSource source) {
        Messenger.tell(
            source, Messenger.f(Messenger.c(
                tr.tr("list"),
                Messenger.endl(),
                Messenger.sline()
            ), Layout.GREEN, Layout.BOLD)
        );

        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_HARDNESS_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float hardness = entry.getValue();
            Block block = state.getBlock();
            String blockName = RegexTools.getBlockRegisterName(block.toString());
            Messenger.tell(source, Messenger.f(Messenger.s(blockName + "/" + hardness), Layout.GREEN, Layout.BOLD));
        }

        return 1;
    }

    private static int help(ServerCommandSource source) {
        Messenger.tell(
            source, Messenger.f(Messenger.c(
                tr.tr("help.set"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.list"), Messenger.endl(),
                tr.tr("help.get_default_hardness"), Messenger.endl()
            ), Layout.GRAY)
        );

        return 1;
    }

    private static void broadcastDataPack() {
        NetworkUtil.broadcastDataPack(CustomBlockHardnessPayload_S2C.create(CUSTOM_BLOCK_HARDNESS_MAP), NetworkUtil.SendMode.NEED_SUPPORT);
    }

    private static void saveToJson() {
        CustomBlockHardnessConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_HARDNESS_MAP);
    }
}
