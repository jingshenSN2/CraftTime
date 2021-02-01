package sn2.timecraft.networking;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sn2.timecraft.TimeCraft;

public class PacketCraftingDifficulty {

	private int item;
	private int difficulty;

	public PacketCraftingDifficulty(int item, int difficulty) {
		this.item = item;
		this.difficulty = difficulty;
	}

	public PacketCraftingDifficulty(PacketBuffer buffer) {
		this.item = buffer.readVarInt();
		this.difficulty = buffer.readVarInt();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeVarInt(this.item);
		buf.writeVarInt(this.difficulty);
	}

	public void handler(Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TimeCraft.map.setDifficulty(this.item, this.difficulty);
		});
		ctx.get().setPacketHandled(true);
	}

}
