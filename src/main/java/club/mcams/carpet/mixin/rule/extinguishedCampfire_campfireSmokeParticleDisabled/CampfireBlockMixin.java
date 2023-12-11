/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.extinguishedCampfire_campfireSmokeParticleDisabled;

import club.mcams.carpet.AmsServerSettings;

//#if MC>=11900
//$$ import net.minecraft.util.math.random.Random;
//#endif
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//#if MC<11900
import java.util.Random;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BlockWithEntity {
    protected CampfireBlockMixin(Settings builder) {
        super(builder);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    protected void initProxy(CallbackInfo info) {
        this.setDefaultState(this.getDefaultState().with(CampfireBlock.LIT, false));
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    protected void getPlacementStateProxy(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        if (cir.getReturnValue() != null && AmsServerSettings.extinguishedCampfire) {
            cir.setReturnValue(cir.getReturnValue().with(CampfireBlock.LIT, false));
            cir.cancel();
        }
    }

    @Inject(method = "spawnSmokeParticle", at = @At("HEAD"), cancellable = true)
    private static void spawnSmokeParticle(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo ci) {
        if(AmsServerSettings.campfireSmokeParticleDisabled) {
            Random random = world.getRandom();
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.0 + random.nextDouble() * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + 0.0, (double)pos.getZ() + 0.0 + random.nextDouble() * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.0, 0.0);
            ci.cancel();
        }
    }
}
