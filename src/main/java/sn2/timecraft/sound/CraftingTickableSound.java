package sn2.timecraft.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import sn2.timecraft.ITimeCraftPlayer;

public class CraftingTickableSound extends TickableSound {
	   private final ITimeCraftPlayer player;

	   public CraftingTickableSound(ITimeCraftPlayer player, BlockPos pos) {
	      super(SoundEventRegistry.craftingSound.get(), SoundCategory.PLAYERS);
	      this.player = player;
	      this.repeat = true;
	      this.repeatDelay = 3;
	      this.volume = 1.0F;
	      this.x = (double)((float)pos.getX());
	      this.y = (double)((float)pos.getY());
	      this.z = (double)((float)pos.getZ());
	   }

	   public boolean shouldPlaySound() {
	      return this.player.isCrafting();
	   }

	   public boolean canBeSilent() {
	      return true;
	   }

	   public void tick() {
		   if (!this.player.isCrafting()) {
			   this.finishPlaying();
		   }
	   }
}
