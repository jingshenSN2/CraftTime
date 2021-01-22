package sn2.timecraft.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import sn2.timecraft.TimeCraft;

public class CraftingDifficulty {
		
	public static int getCraftingDifficultyFromMatrix(PlayerScreenHandler handler) {
		ArrayList<Slot> slots = new ArrayList<Slot>();
		for (int i = 1; i < 5; i++) {
			slots.add(handler.getSlot(i));
		}
		int difficulty = getCraftingDifficultyFromMatrix(slots);
		return difficulty;
	}

	public static int getCraftingDifficultyFromMatrix(CraftingScreenHandler handler) {
		ArrayList<Slot> slots = new ArrayList<Slot>();
		for (int i = 1; i < 10; i++) {
			slots.add(handler.getSlot(i));
		}
		int difficulty = getCraftingDifficultyFromMatrix(slots);
		return difficulty;
	}
	
	public static int getCraftingDifficultyFromMatrix(ArrayList<Slot> slots) {
		int basic_difficulty = 20;
		int item_difficulty = 0;
		for (Slot s : slots) {
			Item item = s.getStack().getItem();
			if (item == Items.AIR)
				continue;
			item_difficulty += TimeCraft.craftingDifficultyMap.getDifficulty(item);
		}
		int difficulty = basic_difficulty + item_difficulty;
		return difficulty;
	}
	
}
