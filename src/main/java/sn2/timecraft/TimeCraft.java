package sn2.timecraft;

import net.fabricmc.api.ModInitializer;
import sn2.timecraft.util.CraftingDifficultyParser;

public class TimeCraft implements ModInitializer {
	
	public static CraftingDifficultyParser craftingDifficultyMap = new CraftingDifficultyParser("timecraft.properties");
	
	@Override
	public void onInitialize() {
	}

}
