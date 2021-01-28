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
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sn2.timecraft.ITimeCraftPlayer;
import sn2.timecraft.util.CraftingDifficultyHelper;

@Mixin(CraftingScreen.class)
public abstract class MixinCraftingScreen extends HandledScreen<CraftingScreenHandler> {

	private ITimeCraftPlayer player;

	public MixinCraftingScreen(CraftingScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	private static final Identifier CRAFT_OVERLAY_TEXTURE = new Identifier("timecraft:textures/gui/crafting_table.png");

	@Inject(method = "drawBackground", at = @At("TAIL"), cancellable = true)
	protected void timecraft$drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY,
			CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.client.player;
		this.client.getTextureManager().bindTexture(CRAFT_OVERLAY_TEXTURE);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		if (player.isCrafting() && player.getCraftPeriod() > 0) {
			int l = (int) (player.getCraftTime() * 24.0F / player.getCraftPeriod());
			MixinCraftingScreen.drawTexture(matrices, i + 90, j + 35, 0, 0, l + 1, 16, 24, 17);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void timecraft$tick(CallbackInfo info) {
		ItemStack resultStack = this.handler.getSlot(0).getStack();
		boolean finished = player.tick(resultStack);
		if (finished) {
			super.onMouseClick(this.handler.getSlot(0), 0, 0, SlotActionType.PICKUP);
			player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler));
		}
	}

	@Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType,
			CallbackInfo info) {
		if (slot != null) {
			invSlot = slot.id;
		}
		if (invSlot > 0 && invSlot < 10) {
			player.setCraftTime(0);
			player.setCrafting(false);
		}
		if (invSlot == 0) {
			if (!player.isCrafting()) {
				player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler));
				player.setCrafting(true);
			}
			info.cancel();
		}
	}
}
