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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sn2.timecraft.ITimeCraftPlayer;
import sn2.timecraft.util.CraftingDifficultyHelper;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends DisplayEffectsScreen<PlayerContainer> {

	private ITimeCraftPlayer player;

	public MixinInventoryScreen(PlayerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	private static final ResourceLocation CRAFT_OVERLAY_TEXTURE = new ResourceLocation(
			"timecraft:textures/gui/inventory.png");

	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawGuiContainerBackgroundLayer(MatrixStack matrices, float delta, int mouseX, int mouseY,
			CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.minecraft.player;
		this.minecraft.getTextureManager().bindTexture(CRAFT_OVERLAY_TEXTURE);
		int i = this.guiLeft;
		int j = this.guiTop;
		if (player.isCrafting() && player.getCraftPeriod() > 0) {
			int l = (int) (player.getCraftTime() * 17.0F / player.getCraftPeriod());
			MixinInventoryScreen.blit(matrices, i + 134, j + 29, 0, 0, l + 1, 14, 18, 15);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void timecraft$tick(CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.minecraft.player;
		ItemStack resultStack = this.container.getSlot(0).getStack();
		boolean finished = player.tick(resultStack);
		if (finished) {
			super.handleMouseClick(this.container.getSlot(0), 0, 0, ClickType.PICKUP);
			player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.container, false));
		}
	}

	@Inject(method = "handleMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$handleMouseClick(Slot slot, int invSlot, int clickData, ClickType actionType,
			CallbackInfo info) {
		if (slot != null) {
			invSlot = slot.slotNumber;
		}
		if (invSlot > 0 && invSlot < 5) {
			player.setCraftTime(0);
			player.setCrafting(false);
		}
		if (invSlot == 0) {
			if (!player.isCrafting()) {
				player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.container, false));
				player.setCrafting(true);
			}
			info.cancel();
		}
	}
}
