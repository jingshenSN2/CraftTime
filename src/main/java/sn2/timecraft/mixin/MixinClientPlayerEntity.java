package sn2.timecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import sn2.timecraft.ITimeCraftPlayer;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements ITimeCraftPlayer {

	public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	public boolean is_crafting = false;
	public float craft_time = 0;
	public float craft_period = 0;

	@Override
	public void setCrafting(boolean is_crafting) {
		this.is_crafting = is_crafting;
	}

	@Override
	public boolean isCrafting() {
		return this.is_crafting;
	}

	@Override
	public void setCraftTime(float craft_time) {
		this.craft_time = craft_time;
	}

	@Override
	public float getCraftTime() {
		return this.craft_time;
	}

	@Override
	public void setCraftPeriod(float craft_period) {
		this.craft_period = craft_period;
	}

	@Override
	public float getCraftPeriod() {
		return this.craft_period;
	}
	
	@Override
	public void stopCraft() {
		this.is_crafting = false;
		this.craft_time = 0F;
	}

	@Override
	public void startCraftWithNewPeriod(float craft_period) {
		this.craft_period = craft_period;
		this.is_crafting = true;
	}
	
	@Override
	public boolean tick(ItemStack resultStack) {
		if (this.isCrafting()) {
			ItemStack cursorStack = this.inventory.getCursorStack();
			if (cursorStack.getItem() != Items.AIR) {
				if (!cursorStack.isItemEqual(resultStack)
						|| cursorStack.getCount() + resultStack.getCount() > cursorStack.getMaxCount()) {
					this.setCrafting(false);
					this.craft_time = 0F;
				}
			}
			if (this.getCraftTime() < this.getCraftPeriod()) {
				this.craft_time += 1F + 0.02F * Math.min(this.experienceLevel, 200);
			}
			if (this.getCraftTime() >= this.getCraftPeriod()) {
				this.craft_time = 0F;
				return true;
			}
		}
		return false;
	}

}
