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

package club.mcams.carpet.mixin.rule.amsUpdateSuppressionCrashFix;

import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix.ThrowableSuppressionPosition;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @WrapOperation(
            method = "tickWorlds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
            )
    )
    private void tickWorlds(ServerWorld serverWorld, BooleanSupplier shouldKeepTicking, Operation<Void> original) {
        if (AmsServerSettings.amsUpdateSuppressionCrashFix) {
            try {
                original.call(serverWorld, shouldKeepTicking);
            } catch (CrashException e) {
                if (!(e.getCause() instanceof ThrowableSuppressionPosition)) {
                    throw e;
                }
            } catch (ThrowableSuppressionPosition ts) {
                String line = "§c§o------------------------------";
                String crashMessage = "Update Suppression in: ";
                String pos = ts.getPosition().toString();
                String dimension = ts.getDimension().toString();
                Pattern patternPos = Pattern.compile("\\{(.*)}");
                Matcher matcher = patternPos.matcher(pos);
                Pattern patternDim = Pattern.compile("minecraft:dimension / (.*?)]");
                Matcher matcherDim = patternDim.matcher(dimension);
                if (matcher.find()) {
                    pos = matcher.group(1); // BlockPos{x=14, y=4, z=46} -> x=14, y=4, z=46
                }
                if (matcherDim.find()) {
                    dimension = matcherDim.group(1); // ResourceKey[minecraft:dimension / minecraft:the_end] -> minecraft:the_end
                }
                Messenger.sendServerMessage((MinecraftServer) (Object) this,
        "\n" + line + "\n" +crashMessage + "\n" + "Location: " + pos + "\n" + "Dimension: " + dimension + "\n" + line);
            }
        } else {
            original.call(serverWorld, shouldKeepTicking);
        }
    }
}
