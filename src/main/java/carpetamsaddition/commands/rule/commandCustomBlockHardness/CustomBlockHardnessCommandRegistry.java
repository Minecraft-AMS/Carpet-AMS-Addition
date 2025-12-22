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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.config.rule.commandCustomBlockHardness.CustomBlockHardnessConfig;
import carpetamsaddition.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload_S2C;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
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
    private final static Translator tr = new Translator("command.customBlockHardness");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_HARDNESS_MAP = new ConcurrentHashMap<>();
    public static final Map<Block, Float> DEFAULT_HARDNESS_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customBlockHardness")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandCustomBlockHardness))

            // set
            .then(literal("set")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .then(argument("hardness", FloatArgumentType.floatArg())
            .executes(context -> set(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState(),
                FloatArgumentType.getFloat(context, "hardness"),
                context.getSource().getServer()
            )))))

            // remove
            .then(literal("remove")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> remove(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState(),
                context.getSource().getServer()
            ))))

            // removeAll
            .then(literal("removeAll").executes(
                context -> removeAll(
                context.getSource(),
                context.getSource().getServer()
            )))

            // list
            .then(literal("list").executes(context -> list(context.getSource())))

            // help
            .then(literal("help").executes(context -> help(context.getSource())))

            // getDefaultHardness
            .then(literal("getDefaultHardness")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> getDefaultHardness(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState().getBlock()
            ))))
        );
    }

    private static int set(CommandSourceStack source, BlockState state, float hardness, MinecraftServer server) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float oldHardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            Messenger.tell(
                source, tr.tr(
                    "modify_set",
                    RegexTools.getBlockRegisterName(state),
                    oldHardness,
                    RegexTools.getBlockRegisterName(state),
                    hardness
                ).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
            );
        } else {
            Messenger.tell(
                source, tr.tr(
                    "set",
                    RegexTools.getBlockRegisterName(state),
                    hardness
                ).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
            );
        }
        CUSTOM_BLOCK_HARDNESS_MAP.put(state, hardness);
        saveToJson();
        broadcastDataPack(server);
        return 1;
    }

    private static int remove(CommandSourceStack source, BlockState state, MinecraftServer server) {
        if (CUSTOM_BLOCK_HARDNESS_MAP.containsKey(state)) {
            float hardness = CUSTOM_BLOCK_HARDNESS_MAP.get(state);
            CUSTOM_BLOCK_HARDNESS_MAP.remove(state);
            saveToJson();
            broadcastDataPack(server);

            Messenger.tell(
                source, tr.tr(
                    "remove",
                    RegexTools.getBlockRegisterName(state),
                    hardness
                ).withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            );

            return 1;
        } else {
            Messenger.tell(
                source, tr.tr(
                    "not_found",
                    RegexTools.getBlockRegisterName(state)
                ).withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            );

            return 0;
        }
    }

    private static int removeAll(CommandSourceStack source, MinecraftServer server) {
        CUSTOM_BLOCK_HARDNESS_MAP.clear();
        saveToJson();
        broadcastDataPack(server);
        Messenger.tell(source, tr.tr("removeAll").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        return 1;
    }

    private static int getDefaultHardness(CommandSourceStack source, Block block) {
        float hardness = CustomBlockHardnessCommandRegistry.DEFAULT_HARDNESS_MAP.get(block);
        String blockName = RegexTools.getBlockRegisterName(block.defaultBlockState());
        Messenger.tell(source, tr.tr("default_hardness", blockName, hardness).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        return 1;
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(
            source, Messenger.c(
                tr.tr("list"),
                Messenger.endl(),
                Messenger.sline()
            ).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
        );

        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_HARDNESS_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float hardness = entry.getValue();
            Block block = state.getBlock();
            String blockName = RegexTools.getBlockRegisterName(block.toString());
            Messenger.tell(source, Messenger.s(blockName + "/" + hardness).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        }

        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(
            source, Messenger.c(
                tr.tr("help.set"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.list"), Messenger.endl(),
                tr.tr("help.get_default_hardness"), Messenger.endl()
            ).withColor(Colors.GRAY)
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
