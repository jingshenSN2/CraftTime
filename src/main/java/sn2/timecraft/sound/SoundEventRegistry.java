package sn2.timecraft.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventRegistry {
	
	public static final Identifier craftingSoundID = new Identifier("timecraft:crafting");
    public static SoundEvent craftingSound = new SoundEvent(craftingSoundID);
	public static final Identifier finishSoundID = new Identifier("timecraft:finish");
    public static SoundEvent finishSound = new SoundEvent(finishSoundID);
    
}
