package sn2.timecraft.event;

import java.io.File;
import java.io.FileInputStream;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;
import sn2.timecraft.Constants;
import sn2.timecraft.TimeCraft;
import sn2.timecraft.config.ConfigLoader;
import sn2.timecraft.networking.PacketCraftingDifficulty;
import sn2.timecraft.networking.TimeCraftPacketHandler;

@Mod.EventBusSubscriber()
public class Events {

	@SubscribeEvent
	public static void onWorldLoad(Load event) {
		if (!event.getWorld().isRemote()) {
			ConfigLoader.genSampleConfig();
			try {
				File cfgFile = FMLPaths.GAMEDIR.get().resolve("config").resolve(Constants.CONFIG_FILENAME).toFile();
				FileInputStream inputFile = new FileInputStream(cfgFile);
				byte[] buf = new byte[inputFile.available()];
				inputFile.read(buf);
				inputFile.close();
				String json = new String(buf);
				TimeCraft.map.parserFrom(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		TimeCraft.map.difficultyMap.forEach((item, difficulty) -> {
			PacketCraftingDifficulty packet = new PacketCraftingDifficulty(item, difficulty);
			TimeCraftPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
					packet);
		});
	}

}
