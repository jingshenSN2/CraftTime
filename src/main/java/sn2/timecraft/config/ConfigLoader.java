package sn2.timecraft.config;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import sn2.timecraft.Constants;

public class ConfigLoader {

	public HashMap<Integer, Integer> difficultyMap = new HashMap<>();
	private static Path cfgPath = FabricLoader.getInstance().getConfigDir();

	public ConfigLoader() {
	}

	public static void genSampleConfig() {
		HashMap<String, JsonObject> nameSpaceMap = new HashMap<>();
		Registry.ITEM.getIds().forEach(rkey -> {
			String namespace = rkey.getNamespace();
			String path = rkey.getPath();
			if (!nameSpaceMap.containsKey(namespace)) {
				JsonObject array = new JsonObject();
				nameSpaceMap.put(namespace, array);
			}
			nameSpaceMap.get(namespace).addProperty(path, 20);
		});

		JsonObject all = new JsonObject();
		nameSpaceMap.forEach((name, array) -> {
			all.add(name, array);
		});

		try {
			File cfgSampleFile = cfgPath.resolve(Constants.CONFIG_FILENAME).toFile();
			if (cfgSampleFile.exists()) {
				cfgSampleFile = cfgPath.resolve(Constants.SAMPLE_CONFIG_FILENAME).toFile();
			}
			FileWriter writer = new FileWriter(cfgSampleFile);
			writer.write(all.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parserFrom(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(jsonString);
		object.entrySet().forEach(e -> {
			String namespace = e.getKey() + ':';
			JsonObject paths = (JsonObject) e.getValue();
			paths.entrySet().forEach(p -> {
				String item = namespace + p.getKey();
				int id = Item.getRawId(Registry.ITEM.get(new Identifier(item)));
				int value = p.getValue().getAsInt();
				this.setDifficulty(id, value);
			});
		});
	}

	public int getDifficulty(Item item) {
		int rkey = Item.getRawId(item);
		if (difficultyMap.containsKey(rkey)) {
			return difficultyMap.get(rkey);
		}
		return 20;
	}

	public void setDifficulty(int id, int difficulty) {
		this.difficultyMap.put(id, difficulty);
	}
}
