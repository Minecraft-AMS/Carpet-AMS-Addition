package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.BlockChunkLoader;

import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.BlockState;
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

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"))
    private void onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if((Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bone_block") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all")) && !world.isClient) {
            Direction direction = state.get(FacingBlock.FACING);
            BlockState pistonBlock = world.getBlockState(pos.up(1));
            if (Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bone_block") && pistonBlock.isOf(Blocks.BONE_BLOCK)) {
                ChunkPos chunkPos = new ChunkPos(pos.offset(direction));
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }

        if((Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bedrock") || Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "all")) && !world.isClient) {
            Direction direction = state.get(FacingBlock.FACING);
            BlockState pistonBlock = world.getBlockState(pos.down(1));
            if (Objects.equals(AmsServerSettings.pistonBlockChunkLoader, "bedrock") && pistonBlock.isOf(Blocks.BEDROCK)) {
                ChunkPos chunkPos = new ChunkPos(pos.offset(direction));
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }
    }
}
