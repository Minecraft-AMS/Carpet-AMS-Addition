/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.mixin.rule.commandAnvilInteractionDisabled;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.commands.rule.commandAnvilInteractionDisabled.AnvilInteractionDisabledCommand;
import carpetamsaddition.utils.Noop;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.resources.Identifier;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.InteractionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import java.util.OptionalInt;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {
    @ModifyReturnValue(
        //#if MC>=12005
        method = "useWithoutItem",
        //#else
        //$$ method = "use",
        //#endif
        at = @At("RETURN")
    )
    private InteractionResult passUse(InteractionResult original) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandAnvilInteractionDisabled, "false") && AnvilInteractionDisabledCommand.anvilInteractionDisabled) {
            return InteractionResult.PASS;
        }

        return original;
    }

    @WrapOperation(
        //#if MC>=12005
        method = "useWithoutItem",
        //#else
        //$$ method = "use",
        //#endif
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"
        )
    )
    private OptionalInt passOpenMenu(Player player, MenuProvider menuProvider, Operation<OptionalInt> original) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandAnvilInteractionDisabled, "false") && AnvilInteractionDisabledCommand.anvilInteractionDisabled) {
            return OptionalInt.empty();
        }

        return original.call(player, menuProvider);
    }

    @WrapOperation(
        //#if MC>=12005
        method = "useWithoutItem",
        //#else
        //$$ method = "use",
        //#endif
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/Identifier;)V"
        )
    )
    private void passAwardStat(Player player, Identifier identifier, Operation<Void> original) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandAnvilInteractionDisabled, "false") && AnvilInteractionDisabledCommand.anvilInteractionDisabled) {
            Noop.noop();
        }

        original.call(player, identifier);
    }
}
