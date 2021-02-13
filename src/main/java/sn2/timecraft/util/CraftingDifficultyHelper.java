package sn2.timecraft.util;

import java.util.ArrayList;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import sn2.timecraft.TimeCraft;

public class CraftingDifficultyHelper {

	public static float getCraftingDifficultyFromMatrix(Container container, boolean is_craft_table) {
		ArrayList<Slot> slots = new ArrayList<Slot>();
		int index = is_craft_table ? 10 : 5;
		for (int i = 1; i < index; i++) {
			slots.add(container.getSlot(i));
		}
		return getCraftingDifficultyFromMatrix(slots);
	}

	public static float getCraftingDifficultyFromMatrix(ArrayList<Slot> slots) {
		float basic_difficulty = 20F;
		float item_difficulty = 0;
		for (Slot s : slots) {
			Item item = s.getStack().getItem();
			if (item == Items.AIR)
				continue;
			item_difficulty += TimeCraft.map.getDifficulty(item);
		}
		return basic_difficulty + item_difficulty;
	}
	
	public static ArrayList<Item> getItemFromMatrix(Container container, boolean is_craft_table) {
		ArrayList<Item> items = new ArrayList<Item>();
		int index = is_craft_table ? 10 : 5;
		for (int i = 1; i < index; i++) {
			items.add(container.getSlot(i).getStack().getItem());
		}
		return items;
	}

}
