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

package club.mcams.carpet.utils;

import net.minecraft.util.Identifier;

public class IdentifierUtil {
	public static Identifier of(String namespace, String path) {
		//#if MC>=12100
		//$$ return Identifier.of(namespace, path);
		//#else
		return new Identifier(namespace, path);
		//#endif
	}

	public static Identifier ofId(String id) {
		//#if MC>=12100
		//$$ return Identifier.of(id);
		//#else
		return new Identifier(id);
		//#endif
	}
}
