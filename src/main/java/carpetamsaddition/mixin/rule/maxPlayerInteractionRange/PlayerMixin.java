/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.maxPlayerInteractionRange;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.MathUtil;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
//#if MC>=260102
//$$ import net.minecraft.world.item.ItemStack;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Objects;

@GameVersion(version = "Minecraft >= 1.20.5")
@Mixin(value = Player.class, priority = 1688)
public abstract class PlayerMixin {
    @ModifyReturnValue(method = "isWithinBlockInteractionRange", at = @At("RETURN"))
    private boolean enforceBlockInteractionRange(boolean original, BlockPos pos, double padding) {
        Player player = (Player) (Object) this;
        double range = CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange;

        if (player instanceof ServerPlayer && range != -1.0D && padding != Container.DEFAULT_DISTANCE_BUFFER && new AABB(pos).distanceToSqr(player.getEyePosition()) >= MathUtil.square(range)) {
            return false;
        }

        return original;
    }

    @ModifyReturnValue(method = "isWithinEntityInteractionRange(Lnet/minecraft/world/phys/AABB;D)Z", at = @At("RETURN"))
    private boolean enforceEntityInteractionRange(boolean original, AABB bounds, double padding) {
        Player player = (Player) (Object) this;
        double range = CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange;

        if (player instanceof ServerPlayer && range != -1.0D && bounds.distanceToSqr(player.getEyePosition()) >= MathUtil.square(range)) {
            return false;
        }

        return original;
    }

    //#if MC>=12111
    @ModifyReturnValue(method = "isWithinAttackRange", at = @At("RETURN"))
    private boolean enforceEntityAttackRange(
        boolean original,
        //#if MC>=260102
        //$$ ItemStack stack,
        //#endif
        AABB bounds, double padding
    ) {
        Player player = (Player) (Object) this;
        double range = CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange;

        if (player instanceof ServerPlayer && range != -1.0D && bounds.distanceToSqr(player.getEyePosition()) >= MathUtil.square(range)) {
            return false;
        }

        return original;
    }
    //#endif

    @WrapOperation(
        method = "isWithinBlockInteractionRange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;blockInteractionRange()D"
        )
    )
    private double canInteractWithBlockAt(Player player, Operation<Double> original) {
        if (CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange != -1.0D && Objects.equals(CarpetAMSAdditionSettings.maxPlayerBlockInteractionRangeScope, "server")) {
            return CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange;
        } else {
            return original.call(player);
        }
    }

    @WrapOperation(
        method = "isWithinEntityInteractionRange(Lnet/minecraft/world/phys/AABB;D)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;entityInteractionRange()D"
        )
    )
    private double canInteractWithEntityAt1(Player player, Operation<Double> original) {
        if (CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange != -1.0D && Objects.equals(CarpetAMSAdditionSettings.maxPlayerEntityInteractionRangeScope, "server")) {
            return CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange;
        } else {
            return original.call(player);
        }
    }
}
