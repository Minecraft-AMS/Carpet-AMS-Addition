package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper;

import net.minecraft.server.world.ChunkTicketType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import static club.mcams.carpet.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper.registerTicketType;

@GameVersion(version = "Minecraft >= 1.21.5")
@Mixin(ChunkTicketType.class)
public abstract class ChunkTicketTypeMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerAmsTicketType(CallbackInfo ci) {
        final String TICKET_NAMESPACE = AmsServer.compactName;

        BlockChunkLoaderHelper.NOTE_BLOCK_TICKET_TYPE = registerTicketType(
            String.format("%s:note_block_loader", TICKET_NAMESPACE),
            BlockChunkLoaderHelper.getLoadTime(),
            //#if MC>=12109
            //$$ 15
            //#else
            true, ChunkTicketType.Use.LOADING_AND_SIMULATION
            //#endif
        );

        BlockChunkLoaderHelper.PISTON_BLOCK_TICKET_TYPE = registerTicketType(
            String.format("%s:piston_block_loader", TICKET_NAMESPACE),
            BlockChunkLoaderHelper.getLoadTime(),
            //#if MC>=12109
            //$$ 15
            //#else
            true, ChunkTicketType.Use.LOADING_AND_SIMULATION
            //#endif
        );

        BlockChunkLoaderHelper.BELL_BLOCK_TICKET_TYPE = registerTicketType(
            String.format("%s:bell_block_loader", TICKET_NAMESPACE),
            BlockChunkLoaderHelper.getLoadTime(),
            //#if MC>=12109
            //$$ 15
            //#else
            true, ChunkTicketType.Use.LOADING_AND_SIMULATION
            //#endif
        );
    }
}
