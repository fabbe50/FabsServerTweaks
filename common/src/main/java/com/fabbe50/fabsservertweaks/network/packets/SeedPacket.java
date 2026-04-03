package com.fabbe50.fabsservertweaks.network.packets;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.client.ClientData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class SeedPacket {
    public static final Identifier PACKET_ID = Fabsservertweaks.location("seed");

    public static class Client {
        private static final CustomPacketPayload.Type<PacketPayload> PACKET_TYPE = new CustomPacketPayload.Type<>(PACKET_ID);
        private static final StreamCodec<FriendlyByteBuf, PacketPayload> PACKET_CODEC = CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new);

        public static void registerServer() {
            NetworkManager.registerS2CPayloadType(PACKET_TYPE, PACKET_CODEC);
        }

        public static void registerClient() {
            NetworkManager.registerReceiver(NetworkManager.s2c(), PACKET_TYPE, PACKET_CODEC, Client::receive);
        }

        public static void receive(PacketPayload payload, NetworkManager.PacketContext context) {
            context.queue(() -> {
                ClientData.setCurrentSeed(payload.seed());
            });
        }

        public record PacketPayload(long seed) implements CustomPacketPayload {
            public PacketPayload(FriendlyByteBuf buf) {
                this(buf.readInt());
            }

            public void write(FriendlyByteBuf buf) {
                buf.writeLong(seed);
            }

            @Override
            public @NotNull Type<? extends CustomPacketPayload> type() {
                return PACKET_TYPE;
            }
        }
    }
}
