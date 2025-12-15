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

package carpetamsaddition.commands.rule.commandGoto;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.compat.DimensionWrapper;

import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class GotoCommandRegistry {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
        Commands.literal("goto")
        .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandGoto))
        .then(Commands.argument("dimension", DimensionArgument.dimension())
        .executes(
            context -> executeSimpleTeleport(
            context.getSource().getPlayerOrException(), DimensionArgument.getDimension(context, "dimension")
        ))
        .then(Commands.argument("destination", BlockPosArgument.blockPos())
        .executes(
            context -> executeTeleport(
            context.getSource().getPlayerOrException(),
            DimensionArgument.getDimension(context, "dimension"),
            BlockPosArgument.getSpawnablePos(context, "destination")
        )))));
    }

    private static int executeTeleport(ServerPlayer player, ServerLevel targetDimension, BlockPos destinationPos) {
        int x = destinationPos.getX();
        int y = destinationPos.getY();
        int z = destinationPos.getZ();
        player.teleportTo(targetDimension, x, y, z, Set.of(), player.getViewXRot(1), 1, false);
        return 1;
    }

    private static int executeSimpleTeleport(ServerPlayer player, ServerLevel targetWorld) {
        DimensionWrapper currentDimension = DimensionWrapper.of(player.level());
        DimensionWrapper targetDimension = DimensionWrapper.of(targetWorld);
        return executeTeleport(player, targetWorld, calculatePos(player, currentDimension, targetDimension));
    }

    private static BlockPos calculatePos(ServerPlayer player, DimensionWrapper currentDimension, DimensionWrapper targetDimension) {
        if (currentDimension.getValue().equals(ServerLevel.OVERWORLD) && targetDimension.getValue().equals(ServerLevel.NETHER)) {
            return createCompatPos(player.getX() / 8, player.getY(), player.getZ() / 8);
        } else if (currentDimension.getValue().equals(ServerLevel.NETHER) && targetDimension.getValue().equals(ServerLevel.OVERWORLD)) {
            return createCompatPos(player.getX() * 8, player.getY(), player.getZ() * 8);
        } else {
            return player.blockPosition();
        }
    }

    public static BlockPos createCompatPos(double x, double y, double z) {
        return new BlockPos((int) x, (int) y, (int) z);
    }
}
