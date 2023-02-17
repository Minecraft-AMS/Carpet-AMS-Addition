package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.BlockChunkLoader;

import net.minecraft.block.BellBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BellBlock.class)
public class BellBlockMixin {
    @Inject(
            method =
                    //#if MC>=11700
                    "ring(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
                    //#else
                    //$$ "ring(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
                    //#endif
            at = @At("HEAD")
    )
    private void ringByTriggeredMixin(
            //#if MC>=11700
            Entity entity,
            //#endif
            World world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.bellBlockChunkLoader && !world.isClient) {
            ChunkPos chunkPos = new ChunkPos(pos);
            ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
        }
    }
}
