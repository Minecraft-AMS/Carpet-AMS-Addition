package club.mcams.carpet.mixin.translations;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("unused")
@Mixin(ClientSettingsC2SPacket.class)
public interface ClientSettingsC2SPacketAccessor {
    @Accessor(value = "language")
    String getLanguage$AMS();
}