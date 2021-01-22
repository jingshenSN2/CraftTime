package sn2.timecraft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

public class CraftingDifficultyParser {

	private HashMap<Item, Integer> difficultyMap;
	private String filename;
	
	public CraftingDifficultyParser(String filename) {
		this.filename = filename;
		this.difficultyMap = new HashMap<>();
		this.genConfig();
		this.parserFrom();
	}
	
	public void genConfig() {
		File cfgPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(filename).toFile();
		if (cfgPath.exists()) {
			return;
		}
		else {
			Properties properties = new Properties();
			
			ForgeRegistries.ITEMS.getKeys().forEach(i -> properties.put(i.toString(), "20"));
			try {
				FileOutputStream oFile = new FileOutputStream(cfgPath);
				properties.store(oFile, "item_id=crafting_difficulty");
				oFile.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void parserFrom() {
		Properties properties = new Properties();
		try {
			Path cfgPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(filename);
			FileInputStream inputFile = new FileInputStream(cfgPath.toFile());
			properties.load(inputFile);
			inputFile.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		properties.forEach((prop, difficulty) -> this.putDifficulty((String) prop, Integer.valueOf((String) difficulty)));
	}
	
	public int getDifficulty(Item item) {
		if (difficultyMap.containsKey(item)) {
			return difficultyMap.get(item);
		}
		return 20;
	}
	
	private void putDifficulty(String registryString, int difficulty) {
		ResourceLocation location = new ResourceLocation(registryString);
		if (ForgeRegistries.ITEMS.containsKey(location)) {
			this.difficultyMap.put(ForgeRegistries.ITEMS.getValue(location), difficulty);
		}
		
	}
}