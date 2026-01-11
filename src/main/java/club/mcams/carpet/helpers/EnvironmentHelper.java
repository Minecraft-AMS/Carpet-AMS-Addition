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

package club.mcams.carpet.helpers;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class EnvironmentHelper {
    private static final EnvType ENV_TYPE = FabricLoader.getInstance().getEnvironmentType();
    private static final Boolean IS_CLIENT = ENV_TYPE.equals(EnvType.CLIENT);
    private static final Boolean IS_SERVER = ENV_TYPE.equals(EnvType.SERVER);

    public static boolean isClient() {
        return IS_CLIENT;
    }

    public static boolean isServer() {
        return IS_SERVER;
    }

    public static EnvType getEnvType() {
        return ENV_TYPE;
    }
}
