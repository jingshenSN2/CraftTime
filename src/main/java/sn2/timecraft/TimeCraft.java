package sn2.timecraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sn2.timecraft.config.ConfigLoader;
import sn2.timecraft.networking.TimeCraftPacketHandler;
import sn2.timecraft.sound.SoundEventRegistry;

@Mod("timecraft")
public class TimeCraft {

	public static ConfigLoader map = new ConfigLoader();

	public TimeCraft() {
		TimeCraftPacketHandler.registerMessage();
		SoundEventRegistry.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
