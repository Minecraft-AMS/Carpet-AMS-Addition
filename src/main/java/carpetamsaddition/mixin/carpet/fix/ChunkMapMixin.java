package carpetamsaddition.mixin.carpet.fix;

import carpetamsaddition.utils.compat.DummyClass;

import org.spongepowered.asm.mixin.Mixin;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft 1.16.5")
@Mixin(DummyClass.class)
public abstract class ChunkMapMixin {}
