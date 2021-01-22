package sn2.timecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sn2.timecraft.util.CraftingDifficulty;

@Mixin(CraftingScreen.class)
public abstract class MixinCraftingScreen extends HandledScreen<CraftingScreenHandler>{
	
	public MixinCraftingScreen(CraftingScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	private static final Identifier CRAFT_OVERLAY_TEXTURE = new Identifier("timecraft:textures/gui/crafting_table.png");
	
	public boolean is_crafting = false;
	public int craft_time = 0;
	public int craft_period = 0;

	@Inject(method = "drawBackground", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
	   this.client.getTextureManager().bindTexture(CRAFT_OVERLAY_TEXTURE);
	   int i = this.x;
	   int j = (this.height - this.backgroundHeight) / 2;
	   if (this.is_crafting && this.craft_period > 0) {
		   int l = (int) (this.craft_time * 24.0F / this.craft_period);
		   MixinCraftingScreen.drawTexture(matrices, i + 90, j + 35, 0, 0, l + 1, 16, 24, 17);
	   }
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void timecraft$tick(CallbackInfo info) {
		if (this.is_crafting) {
			ItemStack cursorStack = this.client.player.inventory.getCursorStack();
			ItemStack resultStack = this.handler.getSlot(0).getStack();
			if (cursorStack.getItem() != Items.AIR) {
				if (!cursorStack.isItemEqual(resultStack) || 
						cursorStack.getCount() + resultStack.getCount() > cursorStack.getMaxCount()) {					
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
				super.onMouseClick(this.handler.getSlot(0), 0, 0, SlotActionType.PICKUP);
				this.craft_time = 0;
				this.craft_period = CraftingDifficulty.getCraftingDifficultyFromMatrix(this.handler);
			}
		}
	}
	
	@Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType, CallbackInfo info) {
		if (slot != null) {
	         invSlot = slot.id;
	    }
		if (invSlot > 0 && invSlot < 10) {
			this.craft_time = 0;
			this.is_crafting = false;
		}
		if (invSlot == 0) {
			if (!is_crafting) {
				this.craft_period = CraftingDifficulty.getCraftingDifficultyFromMatrix(this.handler);
				this.is_crafting = true;
			}
			info.cancel();
		}
	}
}
