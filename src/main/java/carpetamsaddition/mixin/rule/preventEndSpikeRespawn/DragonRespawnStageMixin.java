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

package carpetamsaddition.mixin.rule.preventEndSpikeRespawn;

import carpetamsaddition.CarpetAMSAdditionSettings;

//#if MC>12006
import carpetamsaddition.utils.Noop;
//#endif
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
//#if MC<=12006
//$$ import net.minecraft.world.level.Explosion;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(
    //#if MC>=260102
    //$$ targets ="net/minecraft/world/level/dimension/end/DragonRespawnStage$3"
    //#else
    targets = "net/minecraft/world/level/dimension/end/DragonRespawnAnimation$3"
    //#endif
)
public abstract class DragonRespawnStageMixin {
    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
        )
    )
    private boolean onRemoveBlock(ServerLevel serverWorld, BlockPos blockPos, boolean b, Operation<Boolean> original) {
        return Objects.equals(CarpetAMSAdditionSettings.preventEndSpikeRespawn, "false") ? original.call(serverWorld, blockPos, b) : false;
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            //#if MC>12006
            target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
            //#elseif MC>=11904
            //$$ target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            //#else
            //$$ target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;"
            //#endif
        )
    )
    //#if MC>12006
    private void onCreateExplosion(
    //#else
    //$$ private Explosion onCreateExplosion(
    //#endif
        ServerLevel serverWorld,
        Entity entity,
        double x, double y, double z, float power,
        //#if MC<11904
        //$$ Explosion.BlockInteraction destructionType,
        //#else
        Level.ExplosionInteraction destructionType,
        //#endif
        //#if MC>12006
        Operation<Void> original
        //#else
        //$$ Operation<Explosion> original
        //#endif
    ) {
        //#if MC>12006
        if (Objects.equals(CarpetAMSAdditionSettings.preventEndSpikeRespawn, "false")) {
            original.call(serverWorld, entity, x, y, z, power, destructionType);
        } else {
            Noop.noop();
        }
        //#else
        //$$ return Objects.equals(CarpetAMSAdditionSettings.preventEndSpikeRespawn, "false") ? original.call(serverWorld, entity, x, y, z, power, destructionType) : null;
        //#endif
    }
}
