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

package club.mcams.carpet.mixin.rule.maxBlockInteractionDistance;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.maxBlockInteractionDistance.MaxInteractionDistanceMathHelper;

import net.minecraft.server.network.ServerPlayerInteractionManager;

//#if MC>11800
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import org.spongepowered.asm.mixin.injection.At;
//#endif

//#if MC==11800
import net.minecraft.util.math.ChunkPos;
import net.minecraft.block.Blocks;
//#endif

//#if MC<11900
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
//#if MC>=11800
import org.slf4j.Logger;
//#else
//$$ import org.apache.logging.log4j.Logger;
//#endif
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;
//#endif
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    //#if MC<11900
    @Shadow
    private BlockPos miningPos;

    //#if MC<11700
    //$$ @Shadow
    //$$ public ServerPlayerEntity player;
    //#else
    @Final
    @Shadow
    protected ServerPlayerEntity player;
    //#endif

    //#if MC<11700
    //$$ @Shadow
    //$$ public ServerWorld world;
    //#else
    @Shadow
    protected ServerWorld world;
    //#endif

    @Shadow
    private boolean failedToMine;

    @Shadow
    private boolean mining;

    @Shadow
    private BlockPos failedMiningPos;

    @Shadow
    private int failedStartMiningTime;

    @Shadow
    private int startMiningTime;

    @Shadow
    private int blockBreakingProgress;

    @Shadow
    private int tickCounter;

    @Shadow
    private GameMode gameMode;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract void finishMining(BlockPos pos, PlayerActionC2SPacket.Action action, String reason);

    @Inject(
            method = "processBlockBreakingAction",
            at = @At(
                    value = "INVOKE",
                    //#if MC>=11800
                    target = "Lnet/minecraft/server/MinecraftServer;getPlayerManager()Lnet/minecraft/server/PlayerManager;"
                    //#else
                    //$$ target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"
                    //#endif
            ),
            cancellable = true
    )
    private void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        if (AmsServerSettings.maxBlockInteractionDistance != -1) {
            double d = this.player.getX() - ((double)pos.getX() + 0.5);
            double e = this.player.getY() - ((double)pos.getY() + 0.5) + 1.5;
            double f = this.player.getZ() - ((double)pos.getZ() + 0.5);
            double g = d * d + e * e + f * f;
            if (g > MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance()) {
                //#if MC>=11800
                BlockState blockState;
                if (this.player.world.getServer() != null && this.player.getChunkPos().getChebyshevDistance(new ChunkPos(pos)) < this.player.world.getServer().getPlayerManager().getViewDistance()) {
                    blockState = this.world.getBlockState(pos);
                } else {
                    blockState = Blocks.AIR.getDefaultState();
                }
                //#endif
                //#if MC>=11800
                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, blockState, action, false, "too far"));
                //#else
                //$$ this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "too far"));
                //#endif
            } else if (pos.getY() >= worldHeight) {
                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "too high"));
            } else {
                BlockState blockState2;
                if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                    if (!this.world.canPlayerModifyAt(this.player, pos)) {
                        this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "may not interact"));
                        return;
                    }
                    if (this.isCreative()) {
                        this.finishMining(pos, action, "creative destroy");
                        return;
                    }
                    if (this.player.isBlockBreakingRestricted(this.world, pos, this.gameMode)) {
                        this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "block action restricted"));
                        return;
                    }
                    this.startMiningTime = this.tickCounter;
                    float h = 1.0F;
                    blockState2 = this.world.getBlockState(pos);
                    if (!blockState2.isAir()) {
                        blockState2.onBlockBreakStart(this.world, pos, this.player);
                        h = blockState2.calcBlockBreakingDelta(this.player, this.player.world, pos);
                    }
                    if (!blockState2.isAir() && h >= 1.0F) {
                        this.finishMining(pos, action, "insta mine");
                    } else {
                        if (this.mining) {
                            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos), net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)"));
                        }
                        this.mining = true;
                        this.miningPos = pos.toImmutable();
                        int i = (int)(h * 10.0F);
                        //#if MC>=11700
                        this.world.setBlockBreakingInfo(this.player.getId(), pos, i);
                        //#else
                        //$$ this.world.setBlockBreakingInfo(this.player.getEntityId(), pos, i);
                        //#endif
                        this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "actual start of destroying"));
                        this.blockBreakingProgress = i;
                    }
                } else if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
                    if (pos.equals(this.miningPos)) {
                        int j = this.tickCounter - this.startMiningTime;
                        blockState2 = this.world.getBlockState(pos);
                        if (!blockState2.isAir()) {
                            float k = blockState2.calcBlockBreakingDelta(this.player, this.player.world, pos) * (float)(j + 1);
                            if (k >= 0.7F) {
                                this.mining = false;
                                //#if MC>=11700
                                this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
                                //#else
                                //$$ this.world.setBlockBreakingInfo(this.player.getEntityId(), pos, -1);
                                //#endif
                                this.finishMining(pos, action, "destroyed");
                                return;
                            }
                            if (!this.failedToMine) {
                                this.mining = false;
                                this.failedToMine = true;
                                this.failedMiningPos = pos;
                                this.failedStartMiningTime = this.startMiningTime;
                            }
                        }
                    }
                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "stopped destroying"));
                } else if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
                    this.mining = false;
                    if (!Objects.equals(this.miningPos, pos)) {
                        LOGGER.warn("Mismatch in destroy block pos: {} {}", this.miningPos, pos);
                        //#if MC>=11700
                        this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
                        //#else
                        //$$ this.world.setBlockBreakingInfo(this.player.getEntityId(), this.miningPos, -1);
                        //#endif
                        this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos), action, true, "aborted mismatched destroying"));
                    }
                    //#if MC>=11700
                    this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
                    //#else
                    //$$ this.world.setBlockBreakingInfo(this.player.getEntityId(), pos, -1);
                    //#endif
                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "aborted destroying"));
                }
            }
            ci.cancel();
        }
    }
    //#else
    //$$ @WrapOperation(
    //$$         method = "processBlockBreakingAction",
    //$$         at = @At(
    //$$                 value = "FIELD",
    //$$                 target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"
    //$$         )
    //$$ )
    //$$ private double getActualReachDistance(Operation<Double> original) {
    //$$     if (AmsServerSettings.maxBlockInteractionDistance != -1.0D) {
    //$$         return MaxInteractionDistanceMathHelper.getMaxSquaredReachDistance();
    //$$     } else {
    //$$         return original.call();
    //$$     }
    //$$ }
    //#endif
}