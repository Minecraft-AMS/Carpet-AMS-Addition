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
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.block.BlockState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.command.argument.BlockStateArgumentType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomBlockBlastResistanceCommandRegistry {
    private static final Translator tr = new Translator("command.customBlockBlastResistance");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_BLAST_RESISTANCE_MAP = new ConcurrentHashMap<>();

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
                context.getSource(),
                BlockStateArgumentType.getBlockState(context, "block").getBlockState(),
                FloatArgumentType.getFloat(context, "resistance")
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
            .then(literal("removeAll").executes(context -> removeAll(context.getSource())))
            .then(literal("list").executes(context -> list(context.getSource())))
            .then(literal("help").executes(context -> help(context.getSource())))
        );
    }

    private static int set(ServerCommandSource source, BlockState state, float blastResistance) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float oldBlastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            Messenger.tell(source, Messenger.f(
                tr.tr(
                    "modify_set",
                    getBlockRegisterName(state),
                    oldBlastResistance,
                    getBlockRegisterName(state),
                    blastResistance
                ), Layout.GREEN, Layout.BOLD)
            );
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("set", getBlockRegisterName(state), blastResistance), Layout.GREEN, Layout.BOLD));
        }

        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, blastResistance);
        saveToJson();
        return 1;
    }

    private static int remove(ServerCommandSource source, BlockState state) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float blastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.remove(state);
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("remove", getBlockRegisterName(state), blastResistance), Layout.RED, Layout.BOLD));
            return 1;
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("not_found", getBlockRegisterName(state)), Layout.RED, Layout.BOLD));
            return 0;
        }
    }

    private static int removeAll(ServerCommandSource source) {
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.RED, Layout.BOLD));
        return 1;
    }

    private static int list(ServerCommandSource source) {
        Messenger.tell(source, Messenger.f(Messenger.c(tr.tr("list"), Messenger.endl(), Messenger.sline()), Layout.GREEN, Layout.BOLD));

        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float blastResistance = entry.getValue();
            String blockName = getBlockRegisterName(state);
            Messenger.tell(source, Messenger.f(Messenger.s(blockName + " / " + blastResistance), Layout.GREEN));
        }

        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(ServerCommandSource source) {
        Messenger.tell(source, Messenger.f(Messenger.c(
            tr.tr("help.set"), Messenger.endl(),
            tr.tr("help.remove"), Messenger.endl(),
            tr.tr("help.removeAll"), Messenger.endl(),
            tr.tr("help.list"), Messenger.endl()
        ), Layout.GRAY));
        return 1;
    }

    private static String getBlockRegisterName(BlockState state) {
        return RegexTools.getBlockRegisterName(state);
    }

    private static void saveToJson() {
        CustomBlockBlastResistanceConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP);
    }
}
