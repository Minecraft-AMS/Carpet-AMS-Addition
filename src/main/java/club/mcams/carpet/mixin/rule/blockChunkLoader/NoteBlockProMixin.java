package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.BlockChunkLoader;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
//#if MC>=11903
//$$import net.minecraft.block.BlockState;
//#endif
//#if MC>=11900
//$$import net.minecraft.entity.Entity;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public abstract class NoteBlockProMixin {
    @Inject(at = @At("HEAD"), method = "playNote")
    private void playNoteMixin(
            //#if MC>=11900
            //$$ Entity entity,
            //#endif
            //#if MC>=11903
            //$$ BlockState blockState,
            //#endif
            World world, BlockPos pos, CallbackInfo info) {
        if (AmsServerSettings.noteBlockChunkLoaderPro && !world.isClient) {
            BlockState noteBlock = world.getBlockState(pos.up(1));
            if (AmsServerSettings.noteBlockChunkLoaderPro && noteBlock.isOf(Blocks.BONE_BLOCK)) {
            ChunkPos chunkPos = new ChunkPos(pos.up(1));
            ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.BLOCK_LOADER, chunkPos, 3, chunkPos);
            }
        }
    }
}
