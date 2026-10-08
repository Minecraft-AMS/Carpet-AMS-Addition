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

package carpetamsaddition.mixin.rule.blockChunkLoader;

import carpetamsaddition.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper;

import carpetamsaddition.utils.compat.TicketTypeWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
//#if MC<11904
//$$ import net.minecraft.core.Registry;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import net.minecraft.resources.ResourceKey;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(value = BuiltInRegistries.class, priority = 16888)
public abstract class BuiltInRegistriesMixin {
    @Inject(
        //#if MC>=11904
        method = "bootStrap",
        //#else
        //$$ method = "createRegistryKey",
        //#endif
        at = @At(
            value = "INVOKE",
            //#if MC>=11904
            target = "Lnet/minecraft/core/registries/BuiltInRegistries;createContents()V",
            //#else
            //$$ target = "Lnet/minecraft/resources/ResourceKey;createRegistryKey(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceKey;",
            //#endif
            shift = At.Shift.AFTER
        )
    )
    private static void addAmsTicketType(
        //#if MC>=11904
        CallbackInfo ci
        //#else
        //$$ CallbackInfoReturnable<ResourceKey<Registry<?>>> cir
        //#endif
    ) {
        BlockChunkLoaderHelper.NOTE_BLOCK_TICKET_TYPE = BlockChunkLoaderHelper.registerTicketType(
            "carpetamsaddition:note_block_loader",
            TicketTypeWrapper.FLAG_PERSIST | TicketTypeWrapper.FLAG_LOADING | TicketTypeWrapper.FLAG_SIMULATION | TicketTypeWrapper.FLAG_KEEP_DIMENSION_ACTIVE
        );
        BlockChunkLoaderHelper.PISTON_BLOCK_TICKET_TYPE = BlockChunkLoaderHelper.registerTicketType(
            "carpetamsaddition:piston_block_loader",
            TicketTypeWrapper.FLAG_PERSIST | TicketTypeWrapper.FLAG_LOADING | TicketTypeWrapper.FLAG_SIMULATION | TicketTypeWrapper.FLAG_KEEP_DIMENSION_ACTIVE
        );
        BlockChunkLoaderHelper.BELL_BLOCK_TICKET_TYPE = BlockChunkLoaderHelper.registerTicketType(
            "carpetamsaddition:bell_block_loader",
            TicketTypeWrapper.FLAG_PERSIST | TicketTypeWrapper.FLAG_LOADING | TicketTypeWrapper.FLAG_SIMULATION | TicketTypeWrapper.FLAG_KEEP_DIMENSION_ACTIVE
        );
    }
}
