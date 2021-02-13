package sn2.timecraft.networking;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import sn2.timecraft.TimeCraft;

public class PacketCraftingDifficulty {

	private int item;
	private float difficulty;

	public PacketCraftingDifficulty(int item, float difficulty) {
		this.item = item;
		this.difficulty = difficulty;
	}

	public PacketCraftingDifficulty(PacketBuffer buffer) {
		this.item = buffer.readVarInt();
		this.difficulty = buffer.readFloat();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeVarInt(this.item);
		buf.writeFloat(this.difficulty);
	}

	public void handler(Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TimeCraft.map.setDifficulty(this.item, this.difficulty);
		});
		ctx.get().setPacketHandled(true);
	}

}
