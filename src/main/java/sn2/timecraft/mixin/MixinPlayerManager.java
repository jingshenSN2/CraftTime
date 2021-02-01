package sn2.timecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import sn2.timecraft.Constants;
import sn2.timecraft.TimeCraftServer;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
	@Inject(method = "onPlayerConnect", at = @At("TAIL"), cancellable = true)
	private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
		TimeCraftServer.CRAFT_DIFFICULTY.difficultyMap.forEach((k, v) -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeVarInt(k);
			buf.writeVarInt(v);
			ServerPlayNetworking.send(player, Constants.DIFFICULTY_TABLE_PACKET_ID, buf);
		});
	}
}
