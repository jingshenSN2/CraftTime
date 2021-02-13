package sn2.timecraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import sn2.timecraft.config.ConfigLoader;

public class TimeCraft implements ClientModInitializer {

	public static ConfigLoader map = new ConfigLoader();

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(Constants.DIFFICULTY_TABLE_PACKET_ID,
				(client, handler, packetBuf, responseSender) -> {
					int item = packetBuf.readVarInt();
					float value = packetBuf.readFloat();
					map.setDifficulty(item, value);
				});
	}

}
