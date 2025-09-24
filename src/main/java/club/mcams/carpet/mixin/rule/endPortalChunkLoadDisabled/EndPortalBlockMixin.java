package club.mcams.carpet.mixin.rule.endPortalChunkLoadDisabled;

import club.mcams.carpet.utils.compat.DummyClass;

import org.spongepowered.asm.mixin.Mixin;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.20.5")
@Mixin(DummyClass.class)
public abstract class EndPortalBlockMixin {}
