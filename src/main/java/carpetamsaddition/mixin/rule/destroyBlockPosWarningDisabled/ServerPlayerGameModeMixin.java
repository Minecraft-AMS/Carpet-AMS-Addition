/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.mixin.rule.destroyBlockPosWarningDisabled;

import carpetamsaddition.CarpetAMSAdditionSettings;

import carpetamsaddition.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerPlayerGameMode;

//#if MC>=11800
import org.slf4j.Logger;
//#else
//$$ import org.apache.logging.log4j.Logger;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @WrapOperation(
        //#if MC<260300
        method = "handleBlockBreakAction",
        //#else
        //$$ method = "abortDestroyBlock",
        //#endif
        at = @At(
            value = "INVOKE",
            //#if MC>=11800
            target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            //#elseif MC>=11700
            //$$ target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
            //#else
            //$$ target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;)V",
            //#endif
            remap = false
        )
    )
    private void destroyBlockPosWarningDisabled(
        Logger logger, String message,
        //#if MC>=11700
        Object destroyPos, Object packetPos,
        //#endif
        Operation<Void> original
    ) {
        if (CarpetAMSAdditionSettings.destroyBlockPosWarningDisabled) {
            Noop.noop();
        } else {
            original.call(
                logger, message
                //#if MC>=11700
                , destroyPos, packetPos
                //#endif
            );
        }
    }
}
