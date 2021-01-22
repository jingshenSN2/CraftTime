package sn2.timecraft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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
		File cfgPath = FabricLoader.getInstance().getConfigDir().resolve(filename).toFile();
		if (cfgPath.exists()) {
			return;
		}
		else {
			Properties properties = new Properties();
			Registry.ITEM.getIds().forEach(i -> properties.put(i.toString(), "20"));
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
			Path cfgPath = FabricLoader.getInstance().getConfigDir().resolve(filename);
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
		Optional<Item> item = Registry.ITEM.getOrEmpty(new Identifier(registryString));
		if (item.isPresent()) {
			this.difficultyMap.put(item.get(), difficulty);
		}
	}
}
