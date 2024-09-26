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

package club.mcams.carpet.helpers.rule.headHunter_commandGetPlayerSkull;

import net.minecraft.entity.player.PlayerEntity;
//#if MC>=12005
//$$ import net.minecraft.component.DataComponentTypes;
//$$ import net.minecraft.component.type.ProfileComponent;
//$$ import com.mojang.authlib.properties.PropertyMap;
//$$ import java.util.Optional;
//$$ import net.minecraft.nbt.NbtOps;
//$$ import net.minecraft.nbt.NbtString;
//#endif
import net.minecraft.item.ItemStack;

public class SkullSkinHelper {
    public static void writeNbtToPlayerSkull(PlayerEntity player, ItemStack headStack) {
        //#if MC>=12005
        //$$ ProfileComponent profileComponent = new ProfileComponent(player.getGameProfile());
        //$$ headStack.set(DataComponentTypes.PROFILE, profileComponent);
        //#else
        headStack.getOrCreateNbt().putString("SkullOwner", player.getGameProfile().getName());
        //#endif
    }

    public static void writeNbtToPlayerSkull(String name, ItemStack headStack) {
        //#if MC>=12005
        //$$ ProfileComponent profileComponent = DataComponentTypes.PROFILE.getCodecOrThrow()
        //$$    .parse(NbtOps.INSTANCE, NbtString.of(name)).getOrThrow();
        //$$ headStack.set(DataComponentTypes.PROFILE, profileComponent);
        //#else
        headStack.getOrCreateNbt().putString("SkullOwner", name);
        //#endif
    }
}
