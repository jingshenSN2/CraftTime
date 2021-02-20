package sn2.timecraft.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventRegistry {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "timecraft");
    public static RegistryObject<SoundEvent> craftingSound = SOUNDS.register("crafting", () -> {
        return new SoundEvent(new ResourceLocation("timecraft", "crafting"));
    });
    public static RegistryObject<SoundEvent> finishSound = SOUNDS.register("finish", () -> {
        return new SoundEvent(new ResourceLocation("timecraft", "finish"));
    });
}
