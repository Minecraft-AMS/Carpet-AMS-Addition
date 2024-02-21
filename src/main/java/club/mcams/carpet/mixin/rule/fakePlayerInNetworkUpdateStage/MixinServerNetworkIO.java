/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

/*
This class and feature is directly migrated from Reden
 */

package club.mcams.carpet.mixin.rule.fakePlayerInNetworkUpdateStage;

//实际上是对carpet版本的约束
//#if MC>=11904
//$$ import carpet.fakes.ServerPlayerInterface;
//#else
import carpet.fakes.ServerPlayerEntityInterface;
//#endif
import carpet.helpers.EntityPlayerActionPack;
import carpet.patches.EntityPlayerMPFake;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.fakePlayerInNetworkUpdateStage.IEntityPlayerActionPack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerNetworkIo;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerNetworkIo.class)
public class MixinServerNetworkIO {
    @Shadow @Final
    MinecraftServer server;

    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void onTick(CallbackInfo ci) {
        if (AmsServerSettings.fakePlayerInNetworkUpdateStage) {
            // Player phase
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player instanceof EntityPlayerMPFake) {
                    // do player tick
                    player.playerTick();
                }
            }
            // Network phase
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                // tick action pack
                EntityPlayerActionPack actionPack = ((
                        //#if MC>=11904
                        //$$ ServerPlayerInterface
                        //#else
                        ServerPlayerEntityInterface
                        //#endif
                        ) player).getActionPack();
                ((IEntityPlayerActionPack) actionPack).setNetworkPhase$AMS(true);
                actionPack.onUpdate();
                ((IEntityPlayerActionPack) actionPack).setNetworkPhase$AMS(false);
            }
        }
    }
}