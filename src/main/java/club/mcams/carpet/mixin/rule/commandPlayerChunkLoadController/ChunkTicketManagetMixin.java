package club.mcams.carpet.mixin.rule.commandPlayerChunkLoadController;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.commandPlayerChunkLoadController.ChunkLoading;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkTicketManager.class)
public class ChunkTicketManagetMixin {
    @WrapOperation(
            method = "handleChunkEnter(Lnet/minecraft/util/math/ChunkSectionPos;Lnet/minecraft/server/network/ServerPlayerEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ChunkTicketManager;getPlayerSimulationLevel()I"
            )
    )
    private int tweakPlayerSimulationLevel(ChunkTicketManager instance, Operation<Integer> original, @Local(argsOnly = true) ServerPlayerEntity player) {
        int level = original.call(instance);
        if (AmsServerSettings.commandPlayerChunkLoadController && ChunkLoading.isPlayerUnLoading(player)) {
            level = Math.max(level, ChunkLoading.getSimulationLevel(player));
        }
        return level;
    }
}
