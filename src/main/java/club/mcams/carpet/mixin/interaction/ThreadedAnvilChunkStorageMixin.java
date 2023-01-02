package club.mcams.carpet.mixin.interaction;

import club.mcams.carpet.function.ChunkLoading;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Final
    @Shadow
    ServerWorld world;

    @Inject(method = "doesNotGenerateChunks", at = @At("HEAD"), cancellable = true)
    private void doesNotGenerateChunks(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        String playerName = player.getName().getString();
        if (!ChunkLoading.onlinePlayerMap.getOrDefault(playerName, true)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}