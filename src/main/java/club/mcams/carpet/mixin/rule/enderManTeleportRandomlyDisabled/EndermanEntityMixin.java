package club.mcams.carpet.mixin.rule.enderManTeleportRandomlyDisabled;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.mob.EndermanEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin {
    @Inject(method = "teleportRandomly", at = @At("HEAD"), cancellable = true)
    private void teleportRandomly(CallbackInfoReturnable<Boolean> cir) {
        if(AmsServerSettings.enderManTeleportRandomlyDisabled) {
            cir.setReturnValue(false);
        }
    }
}
