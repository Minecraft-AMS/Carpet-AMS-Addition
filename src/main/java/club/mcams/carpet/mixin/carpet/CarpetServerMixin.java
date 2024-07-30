package club.mcams.carpet.mixin.carpet;

import carpet.CarpetServer;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CarpetServer.class)
public class CarpetServerMixin {
    //Just a fix for https://github.com/gnembon/fabric-carpet/issues/1908

    //#if MC>=11904
    //$$ @WrapWithCondition(
    //$$         method = "onServerClosed(Lnet/minecraft/server/MinecraftServer;)V",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lcarpet/script/external/Vanilla;MinecraftServer_getScriptServer(Lnet/minecraft/server/MinecraftServer;)Lcarpet/script/CarpetScriptServer;"
    //$$         )
    //$$ )
    //$$ private static boolean onlyCallifServerNotnull(MinecraftServer server) {
    //$$     return server != null;
    //$$ }
    //#endif
}
