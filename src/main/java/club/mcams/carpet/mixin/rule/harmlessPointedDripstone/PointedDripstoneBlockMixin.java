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

package club.mcams.carpet.mixin.rule.harmlessPointedDripstone;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import club.mcams.carpet.AmsServerSettings;

//#if MC<11700
//$$ import club.mcams.carpet.utils.compat.DummyClass;
//#endif

//#if MC>=11700
import net.minecraft.block.Block;
import net.minecraft.block.PointedDripstoneBlock;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

import org.spongepowered.asm.mixin.Mixin;

@GameVersion(version = "Minecraft >= 1.17")
//#if MC>=11700
@Mixin(PointedDripstoneBlock.class)
public abstract class PointedDripstoneBlockMixin extends Block {
    public PointedDripstoneBlockMixin(Settings settings) {
        super(settings);
    }
//#else
//$$ @Mixin(DummyClass.class)
//$$ public abstract class PointedDripstoneBlockMixin {
//#endif

//#if MC>=11700
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void onLandedUpon(CallbackInfo ci) {
        if (AmsServerSettings.harmlessPointedDripstone) {
            ci.cancel();
        }
    }
//#endif
}
