package sn2.timecraft.sound;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import sn2.timecraft.ITimeCraftPlayer;

public class CraftingTickableSound extends MovingSoundInstance {
	   private final ITimeCraftPlayer player;

	   public CraftingTickableSound(ITimeCraftPlayer player, BlockPos pos) {
	      super(SoundEventRegistry.craftingSound, SoundCategory.PLAYERS);
	      this.player = player;
	      this.repeat = true;
	      this.repeatDelay = 3;
	      this.volume = 1.0F;
	      this.x = (double)((float)pos.getX());
	      this.y = (double)((float)pos.getY());
	      this.z = (double)((float)pos.getZ());
	   }

	   public boolean canPlay() {
	      return this.player.isCrafting();
	   }

	   public boolean shouldAlwaysPlay() {
	      return true;
	   }

	   public void tick() {
		   if (this.player.getCraftTime() >= this.player.getCraftPeriod()) {
			  this.setDone();
		   }
	   }
}
