package sn2.timecraft;

import java.io.File;
import java.io.FileInputStream;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import sn2.timecraft.config.ConfigLoader;

public class TimeCraftServer implements ModInitializer {

	public static ConfigLoader CRAFT_DIFFICULTY = new ConfigLoader();

	@Override
	public void onInitialize() {
		ConfigLoader.genSampleConfig();
		try {
			File cfgFile = FabricLoader.getInstance().getConfigDir().resolve(Constants.CONFIG_FILENAME).toFile();
			FileInputStream inputFile = new FileInputStream(cfgFile);
			byte[] buf = new byte[inputFile.available()];
			inputFile.read(buf);
			inputFile.close();
			String json = new String(buf);
			CRAFT_DIFFICULTY.parserFrom(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
