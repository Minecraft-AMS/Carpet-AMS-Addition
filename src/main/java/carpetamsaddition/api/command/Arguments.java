/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

/**
 * 内置类型化指令参数工厂。
 *
 * <p>返回的 {@link CommandArgument} 同时负责创建对应版本的 Brigadier 参数类型和按名称读取参数值。
 * 参数工厂可以直接内联在路由和执行器中，无需保存或复用同一个参数对象。</p>
 */
public final class Arguments {
    private Arguments() {}

    /**
     * 创建布尔参数。
     *
     * @param name 参数名称
     * @return 布尔参数
     */
    public static CommandArgument<Boolean> bool(String name) {
        return new CommandArgument<Boolean>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public ArgumentType<?> createType(CommandRegistrationContext context) {
                return BoolArgumentType.bool();
            }

            @Override
            public Boolean read(CommandContext<CommandSourceStack> context) {
                return BoolArgumentType.getBool(context, name);
            }
        };
    }

    /**
     * 创建无边界整数参数。
     *
     * @param name 参数名称
     * @return 整数参数
     */
    public static CommandArgument<Integer> integer(String name) {
        return integer(name, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * 创建具有最小值限制的整数参数。
     *
     * @param name 参数名称
     * @param minimum 允许的最小值
     * @return 整数参数
     */
    public static CommandArgument<Integer> integer(String name, int minimum) {
        return integer(name, minimum, Integer.MAX_VALUE);
    }

    /**
     * 创建具有取值范围限制的整数参数。
     *
     * @param name 参数名称
     * @param minimum 允许的最小值
     * @param maximum 允许的最大值
     * @return 整数参数
     */
    public static CommandArgument<Integer> integer(String name, int minimum, int maximum) {
        return simple(name, context -> IntegerArgumentType.integer(minimum, maximum), context -> IntegerArgumentType.getInteger(context, name));
    }

    /**
     * 创建无边界单精度浮点参数。
     *
     * @param name 参数名称
     * @return 浮点参数
     */
    public static CommandArgument<Float> decimal(String name) {
        return decimal(name, -Float.MAX_VALUE, Float.MAX_VALUE);
    }

    /**
     * 创建具有最小值限制的单精度浮点参数。
     *
     * @param name 参数名称
     * @param minimum 允许的最小值
     * @return 浮点参数
     */
    public static CommandArgument<Float> decimal(String name, float minimum) {
        return decimal(name, minimum, Float.MAX_VALUE);
    }

    /**
     * 创建具有取值范围限制的单精度浮点参数。
     *
     * @param name 参数名称
     * @param minimum 允许的最小值
     * @param maximum 允许的最大值
     * @return 浮点参数
     */
    public static CommandArgument<Float> decimal(String name, float minimum, float maximum) {
        return simple(name, context -> FloatArgumentType.floatArg(minimum, maximum), context -> FloatArgumentType.getFloat(context, name));
    }

    /**
     * 创建不允许空格的单词参数。
     *
     * @param name 参数名称
     * @return 单词参数
     */
    public static CommandArgument<String> word(String name) {
        return simple(name, context -> StringArgumentType.word(), context -> StringArgumentType.getString(context, name));
    }

    /**
     * 创建可使用引号包裹的字符串参数。
     *
     * @param name 参数名称
     * @return 字符串参数
     */
    public static CommandArgument<String> string(String name) {
        return simple(name, context -> StringArgumentType.string(), context -> StringArgumentType.getString(context, name));
    }

    /**
     * 创建读取后续全部输入的贪婪字符串参数。
     *
     * @param name 参数名称
     * @return 贪婪字符串参数
     */
    public static CommandArgument<String> greedyString(String name) {
        return simple(name, context -> StringArgumentType.greedyString(), context -> StringArgumentType.getString(context, name));
    }

    /**
     * 创建单个在线玩家参数。
     *
     * @param name 参数名称
     * @return 玩家参数
     */
    public static CommandArgument<ServerPlayer> player(String name) {
        return simple(name, context -> EntityArgument.player(), context -> EntityArgument.getPlayer(context, name));
    }

    /**
     * 创建可选择多个在线玩家的参数。
     *
     * @param name 参数名称
     * @return 多玩家参数
     */
    public static CommandArgument<Collection<ServerPlayer>> players(String name) {
        return simple(name, context -> EntityArgument.players(), context -> EntityArgument.getPlayers(context, name));
    }

    /**
     * 创建方块状态参数。
     *
     * <p>该方法会自动处理不同版本中 {@code BlockStateArgument.block} 是否需要
     * {@code CommandBuildContext} 的差异。</p>
     *
     * @param name 参数名称
     * @return 方块状态参数
     */
    public static CommandArgument<BlockState> block(String name) {
        return simple(
            name,
            context -> BlockStateArgument.block(
                //#if MC>=11904
                context.buildContext()
                //#endif
            ),
            context -> BlockStateArgument.getBlock(context, name).getState()
        );
    }

    /**
     * 创建物品参数，并将解析结果转换成数量为 1 的物品堆。
     *
     * @param name 参数名称
     * @return 物品堆参数
     */
    public static CommandArgument<ItemStack> item(String name) {
        return simple(
            name,
            context -> ItemArgument.item(
                //#if MC>=11904
                context.buildContext()
                //#endif
            ),
            context -> ItemArgument.getItem(context, name).createItemStack(
                1
                //#if MC<26000
                , false
                //#endif
            )
        );
    }

    /**
     * 创建维度参数。
     *
     * @param name 参数名称
     * @return 服务端维度参数
     */
    public static CommandArgument<ServerLevel> dimension(String name) {
        return simple(name, context -> DimensionArgument.dimension(), context -> DimensionArgument.getDimension(context, name));
    }

    /**
     * 创建可生成位置参数。
     *
     * @param name 参数名称
     * @return 方块坐标参数
     */
    public static CommandArgument<BlockPos> blockPos(String name) {
        return simple(name, context -> BlockPosArgument.blockPos(), context -> BlockPosArgument.getSpawnablePos(context, name));
    }

    /**
     * 为已有参数附加补全建议器，不改变参数的名称、类型或读取方式。
     *
     * @param argument 原始类型化参数
     * @param suggestions Brigadier 补全建议器
     * @param <T> 参数值类型
     * @return 带有补全建议的参数包装
     */
    public static <T> CommandArgument<T> suggests(CommandArgument<T> argument, SuggestionProvider<CommandSourceStack> suggestions) {
        return new CommandArgument<T>() {
            @Override
            public String getName() {
                return argument.getName();
            }

            @Override
            public ArgumentType<?> createType(CommandRegistrationContext context) {
                return argument.createType(context);
            }

            @Override
            public T read(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                return argument.read(context);
            }

            @Override
            public SuggestionProvider<CommandSourceStack> suggestions() {
                return suggestions;
            }
        };
    }

    private static <T> CommandArgument<T> simple(String name, TypeFactory typeFactory, ValueReader<T> valueReader) {
        return new CommandArgument<T>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public ArgumentType<?> createType(CommandRegistrationContext context) {
                return typeFactory.create(context);
            }

            @Override
            public T read(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                return valueReader.read(context);
            }
        };
    }

    @FunctionalInterface
    private interface TypeFactory {
        ArgumentType<?> create(CommandRegistrationContext context);
    }

    @FunctionalInterface
    private interface ValueReader<T> {
        T read(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
    }
}
