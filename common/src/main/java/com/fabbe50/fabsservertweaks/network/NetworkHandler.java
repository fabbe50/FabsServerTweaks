package com.fabbe50.fabsservertweaks.network;

import com.fabbe50.fabsservertweaks.network.packets.SeedPacket;

public class NetworkHandler {
    public static void registerServerHandlers() {
        SeedPacket.Client.registerServer();
    }

    public static void registerClientHandlers() {
        SeedPacket.Client.registerClient();
    }
}
