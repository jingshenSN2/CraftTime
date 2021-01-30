package sn2.timecraft.mixin;

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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sn2.timecraft.ITimeCraftPlayer;
import sn2.timecraft.util.CraftingDifficultyHelper;

@Mixin(CraftingScreen.class)
public abstract class MixinCraftingScreen extends ContainerScreen<WorkbenchContainer>{

	private ITimeCraftPlayer player;

	public MixinCraftingScreen(WorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	private static final ResourceLocation CRAFT_OVERLAY_TEXTURE = new ResourceLocation("timecraft:textures/gui/crafting_table.png");
	
	@Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawGuiContainerBackgroundLayer(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
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
			super.handleMouseClick(this.container.getSlot(0), 0, 0, ClickType.PICKUP);
			player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.container));
		}
	}
	
	@Inject(method = "handleMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$handleMouseClick(Slot slot, int invSlot, int clickData, ClickType actionType, CallbackInfo info) {
		if (slot != null) {
	         invSlot = slot.slotNumber;
	    }
		if (invSlot > 0 && invSlot < 10) {
			player.setCraftTime(0);
			player.setCrafting(false);
		}
		if (invSlot == 0) {
			if (!player.isCrafting()) {
				player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.container));
				player.setCrafting(true);
			}
			info.cancel();
		}
	}
}
