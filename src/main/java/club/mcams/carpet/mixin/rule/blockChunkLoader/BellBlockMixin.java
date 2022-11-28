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

import java.util.Objects;

@Mixin(BellBlock.class)
public class BellBlockMixin {
    @Inject(
            method = "ring(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
            at = @At("HEAD")
    )
    private void ringByTriggeredMixin(Entity entity, World world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(AmsServerSettings.blockChunkLoader, "bell_block") && world instanceof ServerWorld) {
            ChunkPos cp = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
            ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BELL_BLOCK, cp, 3, cp);
        }
    }
}
