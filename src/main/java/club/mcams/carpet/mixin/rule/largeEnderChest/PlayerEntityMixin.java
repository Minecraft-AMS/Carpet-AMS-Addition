package club.mcams.carpet.mixin.rule.largeEnderChest;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	private static final int EXPANDED_ENDERCHEST_SIZE = 9 * 6;

	@Shadow
	public abstract EnderChestInventory getEnderChestInventory();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void expandEnderChest(final World world, final BlockPos pos, final float yaw, final GameProfile gameProfile, final CallbackInfo ci) {
			SimpleInventoryAccessor accessor = (SimpleInventoryAccessor) getEnderChestInventory();
			accessor.setSize(EXPANDED_ENDERCHEST_SIZE);
			accessor.setStacks(DefaultedList.ofSize(EXPANDED_ENDERCHEST_SIZE, ItemStack.EMPTY));
	}
}
