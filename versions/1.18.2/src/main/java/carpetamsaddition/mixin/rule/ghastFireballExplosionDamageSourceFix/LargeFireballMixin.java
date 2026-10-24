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

package carpetamsaddition.mixin.rule.ghastFireballExplosionDamageSourceFix;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.LargeFireball;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.19.3")
@Mixin(LargeFireball.class)
public abstract class LargeFireballMixin {
    @ModifyArg(
        method = "onHit(Lnet/minecraft/world/phys/HitResult;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;"
        ),
        index = 0
    )
    private Entity fillUpExplosionOwner(Entity entity) {
        return CarpetAMSAdditionSettings.ghastFireballExplosionDamageSourceFix ? (LargeFireball) (Object) this : entity;
    }
}
