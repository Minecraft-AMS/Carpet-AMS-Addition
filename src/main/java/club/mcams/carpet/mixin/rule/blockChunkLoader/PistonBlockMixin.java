package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.BlockChunkLoader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
{
    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"))
    private void load(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable info)
    {
        if(AmsServerSettings.pistonblockchunkloader || world instanceof ServerWorld)
        {
            Direction direction = state.get(FacingBlock.FACING);
            BlockPos nbp = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            Block block = world.getBlockState(nbp).getBlock();
            if (Registry.BLOCK.getId(block).hashCode() == BlockChunkLoader.BoneBlockHash)
            {
                int x = pos.getX() + direction.getOffsetX();
                int z = pos.getZ() + direction.getOffsetZ();
                ChunkPos cp = new ChunkPos(x >> 4, z >> 4);
                ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.PISTON_BLOCK_TICKET, cp, 3, cp);
            }
        }
    }
}
