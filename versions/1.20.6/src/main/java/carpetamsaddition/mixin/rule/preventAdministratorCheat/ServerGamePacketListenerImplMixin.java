/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package carpetamsaddition.mixin.rule.preventAdministratorCheat;

import carpetamsaddition.utils.compat.DummyClass;

import org.spongepowered.asm.mixin.Mixin;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft <= 1.20.6")
@Mixin(DummyClass.class)
public abstract class ServerGamePacketListenerImplMixin {}
