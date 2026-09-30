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

package carpetamsaddition.helpers.rule.headHunter_commandGetPlayerSkull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//#if MC>=12005
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
//#endif

public class SkullSkinHelper {
    public static void writeNbtToPlayerSkull(Player player, ItemStack headStack) {
        //#if MC>=12005
        headStack.set(
            DataComponents.PROFILE,
            //#if MC>=12111
            ResolvableProfile.createResolved(player.getGameProfile())
            //#else
            //$$ new ResolvableProfile(player.getGameProfile())
            //#endif
        );
        //#else
        //$$ headStack.getOrCreateTag().putString("SkullOwner", player.getGameProfile().getName());
        //#endif
    }

    public static void writeNbtToPlayerSkull(String name, ItemStack headStack) {
        //#if MC>=12005
        ResolvableProfile profileComponent = DataComponents.PROFILE.codecOrThrow().parse(NbtOps.INSTANCE, StringTag.valueOf(name)).getOrThrow();
        headStack.set(DataComponents.PROFILE, profileComponent);
        //#else
        //$$ headStack.getOrCreateTag().putString("SkullOwner", name);
        //#endif
    }
}
