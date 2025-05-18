/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.helpers.rule.preventAdministratorCheat;

import club.mcams.carpet.AmsServerSettings;

//#if MC>=12106
//$$ import net.minecraft.command.PermissionLevelPredicate;
//#endif
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionHelper {
    public static boolean canCheat(ServerCommandSource source) {
        return !(source.getEntity() instanceof ServerPlayerEntity) || !AmsServerSettings.preventAdministratorCheat;
    }

    //#if MC>=12106
    //$$ public static PermissionLevelPredicate<ServerCommandSource> createPermissionPredicate(PermissionLevelPredicate<ServerCommandSource> original) {
    //$$     return new PermissionLevelPredicate<>() {
    //$$         @Override
    //$$         public int requiredLevel() {
    //$$             return original.requiredLevel();
    //$$         }
    //$$
    //$$         @Override
    //$$         public boolean test(ServerCommandSource source) {
    //$$             return original.test(source) && PermissionHelper.canCheat(source);
    //$$         }
    //$$     };
    //$$ }
    //#endif
}
