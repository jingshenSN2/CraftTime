package sn2.timecraft.util;

import harmonised.pmmo.skills.Skill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.ModList;

public class CraftingSpeedHelper {
	
	
	public static float getCraftingSpeed(PlayerEntity player) {
		float speed = 1F;
		speed += 0.02F * Math.min(200, player.experienceLevel);
		if (ModList.get().isLoaded("pmmo")) {
			speed += 0.05F * Math.min(150, Skill.getLevel(Skill.CRAFTING.toString(), player));
		}
		return speed;
	}
	
}
