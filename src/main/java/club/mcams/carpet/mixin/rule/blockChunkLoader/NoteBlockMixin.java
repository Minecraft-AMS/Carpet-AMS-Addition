package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.BlockChunkLoader;

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

import java.util.Objects;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Inject(at = @At("HEAD"), method = "playNote")
    private void loadChunk(
            //#if MC>=11900
            //$$ Entity entity,
            //#endif
            //#if MC>=11903
            //$$ BlockState blockState,
            //#endif
            World world, BlockPos pos, CallbackInfo info) {
        if (Objects.equals(AmsServerSettings.blockChunkLoader, "note_block")) {
            ChunkPos cp = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
            ((ServerWorld) world).getChunkManager().addTicket(BlockChunkLoader.NOTE_BLOCK, cp, 3, cp);
        }
    }
}
