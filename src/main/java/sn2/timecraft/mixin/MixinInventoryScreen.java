package sn2.timecraft.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sn2.timecraft.ITimeCraftPlayer;
import sn2.timecraft.util.CraftingDifficultyHelper;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractInventoryScreen<PlayerScreenHandler> {

	private ITimeCraftPlayer player;

	public MixinInventoryScreen(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	private static final Identifier CRAFT_OVERLAY_TEXTURE = new Identifier("timecraft:textures/gui/inventory.png");

	@Inject(method = "drawBackground", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY,
			CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.client.player;
		this.client.getTextureManager().bindTexture(CRAFT_OVERLAY_TEXTURE);
		int i = this.x;
		int j = this.y;
		if (player.isCrafting() && player.getCraftPeriod() > 0) {
			int l = (int) (player.getCraftTime() * 17.0F / player.getCraftPeriod());
			MixinInventoryScreen.drawTexture(matrices, i + 134, j + 29, 0, 0, l + 1, 14, 18, 15);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void timecraft$tick(CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.client.player;
		ItemStack resultStack = this.handler.getSlot(0).getStack();
		boolean finished = player.tick(resultStack);
		if (finished) {
			ArrayList<Item> old_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, false);
			super.onMouseClick(this.handler.getSlot(0), 0, 0, SlotActionType.PICKUP);
			ArrayList<Item> new_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, false);
			if (old_recipe.equals(new_recipe))
				player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, false));
			else 
				player.stopCraft();
		}
	}

	@Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType,
			CallbackInfo info) {
		if (slot != null) {
			invSlot = slot.id;
		}
		if (invSlot > 0 && invSlot < 10) {
			player.stopCraft();
		}
		if (invSlot == 0) {
			if (!player.isCrafting()) {
				player.startCraftWithNewPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, false));
			}
			info.cancel();
		}
	}
	
	@Inject(method = "removed", at = @At("HEAD"), cancellable = true)
	public void timecraft$onClose(CallbackInfo info) {
		if (player != null) {			
			player.stopCraft();
		}
	}
}
