package sn2.timecraft.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sn2.timecraft.ITimeCraftPlayer;
import sn2.timecraft.util.CraftingDifficultyHelper;

@Mixin(CraftingScreen.class)
public abstract class MixinCraftingScreen extends ContainerScreen<WorkbenchContainer> {

	private ITimeCraftPlayer player;

	public MixinCraftingScreen(WorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	private static final ResourceLocation CRAFT_OVERLAY_TEXTURE = new ResourceLocation(
			"timecraft:textures/gui/crafting_table.png");

	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawGuiContainerBackgroundLayer(MatrixStack matrices, float delta, int mouseX, int mouseY,
			CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.minecraft.player;
		this.minecraft.getTextureManager().bindTexture(CRAFT_OVERLAY_TEXTURE);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		if (player.isCrafting() && player.getCraftPeriod() > 0) {
			int l = (int) (player.getCraftTime() * 24.0F / player.getCraftPeriod());
			MixinCraftingScreen.blit(matrices, i + 89, j + 35, 0, 0, l + 1, 16, 24, 17);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void timecraft$tick(CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.minecraft.player;
		ItemStack resultStack = this.container.getSlot(0).getStack();
		boolean finished = player.tick(resultStack);
		if (finished) {
			ArrayList<Item> old_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.container, true);
			super.handleMouseClick(this.container.getSlot(0), 0, 0, ClickType.PICKUP);
			ArrayList<Item> new_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.container, true);
			if (old_recipe.equals(new_recipe))
				player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.container, true));
			else 
				player.stopCraft();
		}
	}

	@Inject(method = "handleMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$handleMouseClick(Slot slot, int invSlot, int clickData, ClickType actionType,
			CallbackInfo info) {
		if (slot != null) {
			invSlot = slot.slotNumber;
		}
		if (invSlot > 0 && invSlot < 10) {
			player.stopCraft();
		}
		if (invSlot == 0) {
			if (!player.isCrafting()) {
				player.startCraftWithNewPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.container, true));
			}
			info.cancel();
		}
	}
	
	@Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
	public void timecraft$onClose(CallbackInfo info) {
		player.stopCraft();
	}
}
