package club.mcams.carpet.mixin.rule.experimentalMinecart;

import club.mcams.carpet.utils.compat.DummyInterface;

import org.spongepowered.asm.mixin.Mixin;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.2")
@Mixin(DummyInterface.class)
public interface AbstractMinecartEntityAccessor {}
