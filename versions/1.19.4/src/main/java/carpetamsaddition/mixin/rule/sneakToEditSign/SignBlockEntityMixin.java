/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023 A Minecraft Server and contributors
 */

package carpetamsaddition.mixin.rule.sneakToEditSign;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.level.block.entity.SignBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.20")
@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin {
    @Inject(method = "isEditable", at = @At("HEAD"), cancellable = true)
    private void isEditable(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAMSAdditionSettings.sneakToEditSign) {
            cir.setReturnValue(true);
        }
    }
}
