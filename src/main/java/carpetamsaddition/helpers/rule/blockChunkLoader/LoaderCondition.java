/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.helpers.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.utils.BlockCondition;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
//#if MC>=11904
import net.minecraft.commands.CommandBuildContext;
//#endif
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.LevelReader;
//#if MC<11904
//$$ import net.minecraft.core.Registry;
//#endif

import java.util.function.Predicate;

public class LoaderCondition {
    public enum Result {
        MATCH, FAIL, UNKNOWN
    }

    public int id;
    public int dx;
    public int dy;
    public int dz;
    public boolean inverted;
    public String block;
    private transient BlockCondition condition;
    private transient Commands parsedWith;

    protected void compile(MinecraftServer server) throws CommandSyntaxException {
        BlockPredicateArgument parser = BlockPredicateArgument.blockPredicate(
            //#if MC>=11904
            CommandBuildContext.simple(server.registryAccess(), server.getWorldData().enabledFeatures())
            //#endif
        );
        StringReader reader = new StringReader(block);
        BlockPredicateArgument.Result parsed = parser.parse(reader);
        Predicate<BlockInWorld> predicate;
        //#if MC>=11904
        predicate = parsed;
        //#elseif MC>=11800
        //$$ predicate = parsed.create(Registry.BLOCK);
        //#else
        //$$ predicate = parsed.create(server.getTags());
        //#endif
        condition = BlockCondition.at(dx, dy, dz, (world, pos) -> predicate.test(new BlockInWorld((LevelReader) world, pos, false)));

        if (inverted) {
            condition = condition.not();
        }

        parsedWith = server.getCommands();
    }

    public Result evaluate(ServerLevel world, BlockPos origin) {
        BlockPos target = origin.offset(dx, dy, dz);

        if (world.isOutsideBuildHeight(target) || !world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(target.getX()), SectionPos.blockToSectionCoord(target.getZ()))) {
            return Result.UNKNOWN;
        }

        MinecraftServer server = world.getServer();

        if (parsedWith != server.getCommands()) {
            try {
                compile(server);
            } catch (CommandSyntaxException | IllegalArgumentException e) {
                parsedWith = server.getCommands();
                condition = null;
                CarpetAMSAdditionServer.LOGGER.warn("Invalid block chunk loader condition; treating the result as unknown: {}", block, e);
            }
        }

        if (condition == null) {
            return Result.UNKNOWN;
        }

        return condition.matches(world, origin) ? Result.MATCH : Result.FAIL;
    }
}
