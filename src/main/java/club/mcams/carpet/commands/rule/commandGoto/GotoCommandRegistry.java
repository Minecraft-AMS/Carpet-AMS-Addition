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

package club.mcams.carpet.commands.rule.commandGoto;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class GotoCommandRegistry {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
        CommandManager.literal("goto")
        .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGoto))
        .then(CommandManager.argument("dimension", DimensionArgumentType.dimension())
        .executes(
            context -> executeSimpleTeleport(
            context.getSource().getPlayer(), DimensionArgumentType.getDimensionArgument(context, "dimension")
        ))
        .then(CommandManager.argument("destination", BlockPosArgumentType.blockPos())
        .executes(
            context -> executeTeleport(
            context.getSource().getPlayer(),
            DimensionArgumentType.getDimensionArgument(context, "dimension"),
            BlockPosArgumentType.getBlockPos(context, "destination")
        )))));
    }

    private static int executeTeleport(ServerPlayerEntity player, ServerWorld targetDimension, BlockPos destinationPos) {
        int x = destinationPos.getX();
        int y = destinationPos.getY();
        int z = destinationPos.getZ();
        player.teleport(targetDimension, x, y, z, player.getYaw(1), player.getPitch(1));
        return 1;
    }

    private static int executeSimpleTeleport(ServerPlayerEntity player, ServerWorld targetWorld) {
        DimensionWrapper currentDimension = DimensionWrapper.of(player.getWorld());
        DimensionWrapper targetDimension = DimensionWrapper.of(targetWorld);
        return executeTeleport(player, targetWorld, calculatePos(player, currentDimension, targetDimension));
    }

    private static BlockPos calculatePos(ServerPlayerEntity player, DimensionWrapper currentDimension, DimensionWrapper targetDimension) {
        if (currentDimension.getValue().equals(ServerWorld.OVERWORLD) && targetDimension.getValue().equals(ServerWorld.NETHER)) {
            return createCompatPos(player.getX() / 8, player.getY(), player.getZ() / 8);
        } else if (currentDimension.getValue().equals(ServerWorld.NETHER) && targetDimension.getValue().equals(ServerWorld.OVERWORLD)) {
            return createCompatPos(player.getX() * 8, player.getY(), player.getZ() * 8);
        } else {
            return player.getBlockPos();
        }
    }

    public static BlockPos createCompatPos(double x, double y, double z) {
        //#if MC<11900
        return new BlockPos(x, y, z);
        //#else
        //$$ return new BlockPos((int) x, (int) y, (int) z);
        //#endif
    }
}
