package club.mcams.carpet.mixin.rule.onlyPlayerCanCreateNetherPortal;

import club.mcams.carpet.utils.compat.DummyClass;

import org.spongepowered.asm.mixin.Mixin;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21")
@Mixin(DummyClass.class)
public abstract class NetherPortalBlockMixin {}
