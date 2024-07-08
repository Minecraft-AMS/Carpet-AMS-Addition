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

package club.mcams.carpet.mixin.rule.stackableDiscount;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(VillageGossipType.class)
public class VillageGossipTypeMixin {
    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/village/VillageGossipType;<init>(Ljava/lang/String;ILjava/lang/String;IIII)V"
            )
    )
    private static void modifyVillageGossipInitialization(Args args) {
        if (AmsServerSettings.stackableDiscounts) {
            switch (args.<String>get(2)) {
                case "minor_positive":
                    args.set(4, 200);
                    break;
                case "major_positive":
                    args.set(4, 100);
                    args.set(6, 100);
                    break;
            }
        }
    }
}
