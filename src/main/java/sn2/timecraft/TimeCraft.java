package sn2.timecraft;

import net.minecraftforge.fml.common.Mod;
import sn2.timecraft.config.ConfigLoader;
import sn2.timecraft.networking.TimeCraftPacketHandler;

@Mod("timecraft")
public class TimeCraft{

	public static ConfigLoader map = new ConfigLoader();

	public TimeCraft() {
		TimeCraftPacketHandler.registerMessage();
	}
	
}
