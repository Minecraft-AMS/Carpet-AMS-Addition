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

package club.mcams.carpet.mixin.rule.ghastFireballExplosionDamageSourceFix;

import org.spongepowered.asm.mixin.Mixin;
//#if MC<11900
import club.mcams.carpet.AmsServerSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//#else
//$$ import club.mcams.carpet.utils.compat.DummyClass;
//#endif

//For version < 1.19.3
//#if MC<11900
@Mixin(FireballEntity.class)
//#else
//$$ @Mixin(DummyClass.class)
//#endif
public class FireballEntityMixin {
    //#if MC<11900
    @ModifyArg(
        method= "onCollision(Lnet/minecraft/util/hit/HitResult;)V",
        at=@At(
            value="INVOKE",
            target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"
        ),
        index=0
    )
    public Entity fillUpExplosionOwner(@Nullable Entity entity) {
        if(AmsServerSettings.ghastFireballExplosionDamageSourceFix) {
            return (FireballEntity) (Object) this;
        } else {
            return entity;
        }
    }
    //#endif
}
