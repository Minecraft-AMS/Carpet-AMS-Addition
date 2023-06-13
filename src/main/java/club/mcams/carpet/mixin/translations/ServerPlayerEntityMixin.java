package club.mcams.carpet.mixin.translations;

import club.mcams.carpet.translations.ServerPlayerEntityWithClientLanguage;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityWithClientLanguage {
    private String clientLanguage$AMS = "en_US";

    //#if MC>=11800
    @Inject(method = "setClientSettings", at = @At("HEAD"))
    private void recordClientLanguage(ClientSettingsC2SPacket packet, CallbackInfo ci) {
        this.clientLanguage$AMS = packet.language();
    }
    //#else
    //$$ @Inject(method = "setClientSettings", at = @At("HEAD"))
    //$$ private void recordClientLanguage(ClientSettingsC2SPacket packet, CallbackInfo ci) {
    //$$     this.clientLanguage$AMS = ((ClientSettingsC2SPacketAccessor) packet).getLanguage$AMS();
    //$$ }
    //#endif

    @Override
    public String getClientLanguage$AMS() {
        return this.clientLanguage$AMS;
    }
}
