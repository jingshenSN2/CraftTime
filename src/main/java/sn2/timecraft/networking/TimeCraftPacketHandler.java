package sn2.timecraft.networking;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import sn2.timecraft.Constants;

public class TimeCraftPacketHandler {

	private static int packetId = 0;
	public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(Constants.DIFFICULTY_TABLE_PACKET_ID,
			() -> "1.0", (s) -> true, (s) -> true);

	public static int nextID() {
		return packetId++;
	}

	public static void registerMessage() {
		INSTANCE.registerMessage(nextID(), PacketCraftingDifficulty.class, (pack, buffer) -> {
			pack.toBytes(buffer);
		}, (buffer) -> {
			return new PacketCraftingDifficulty(buffer);
		}, (pack, ctx) -> {
			pack.handler(ctx);
		});
	}
}
