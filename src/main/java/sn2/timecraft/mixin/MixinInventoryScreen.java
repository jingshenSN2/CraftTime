package sn2.timecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sn2.timecraft.util.CraftingDifficulty;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends DisplayEffectsScreen<PlayerContainer>{

	public MixinInventoryScreen(PlayerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	private static final ResourceLocation CRAFT_OVERLAY_TEXTURE = new ResourceLocation("timecraft:textures/gui/inventory.png");

	public boolean is_crafting = false;
	public int craft_time = 0;
	public int craft_period = 0;

	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawGuiContainerBackgroundLayer(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
	   this.minecraft.getTextureManager().bindTexture(CRAFT_OVERLAY_TEXTURE);
	   int i = this.guiLeft;
	   int j = this.guiTop;
	   if (this.is_crafting && this.craft_period > 0) {
		   int l = (int) (this.craft_time * 17.0F / this.craft_period);
		   MixinInventoryScreen.blit(matrices, i + 135, j + 29, 0, 0, l + 1, 14, 18, 15);
	   }
	   System.out.println('1');
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void timecraft$tick(CallbackInfo info) {
		if (this.is_crafting) {
			ItemStack cursorStack = this.minecraft.player.inventory.getCurrentItem();
			ItemStack resultStack = this.container.getSlot(0).getStack();
			if (cursorStack.getItem() != Items.AIR) {
				if (!cursorStack.isItemEqual(resultStack) || 
						cursorStack.getCount() + resultStack.getCount() > cursorStack.getMaxStackSize()) {					
					this.is_crafting = false;
				}
			}
			if (resultStack.getItem() == Items.AIR) {
				this.is_crafting = false;
			}
			if (this.craft_time < this.craft_period) {
				this.craft_time++;
			}
			if (this.craft_time >= this.craft_period) {
				super.handleMouseClick(this.container.getSlot(0), 0, 0, ClickType.PICKUP);
				this.craft_time = 0;
				this.craft_period = CraftingDifficulty.getCraftingDifficultyFromMatrix(this.container);
			}
		}
	}
	
	@Inject(method = "handleMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$handleMouseClick(Slot slot, int invSlot, int clickData, ClickType actionType, CallbackInfo info) {
		if (slot != null) {
	         invSlot = slot.slotNumber;
	    }
		if (invSlot > 0 && invSlot < 5) {
			this.craft_time = 0;
			this.is_crafting = false;
		}
		if (invSlot == 0) {
			if (!is_crafting) {
				this.craft_period = CraftingDifficulty.getCraftingDifficultyFromMatrix(this.container);
				this.is_crafting = true;
			}
			info.cancel();
		}
	}
}
