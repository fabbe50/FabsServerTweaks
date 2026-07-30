package com.fabbe50.fabsservertweaks.data.stats;

import com.fabbe50.fabsservertweaks.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.entity.player.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatsRegistry {
    public static final String PLAYER_ONLINE_FILE = "player_online.json";

    public static final List<PlayerOnline> PLAYER_ONLINE = new ArrayList<>();

    public static void setPlayerLastOnline(Player player) {
        PLAYER_ONLINE.removeIf(data -> data.playerID().equals(player.getUUID()));
        PlayerOnline data = new PlayerOnline(player.getUUID(), player.level().dimension().identifier(), player.position(), LocalDateTime.now());
        PLAYER_ONLINE.add(data);
        JsonUtil.save(PLAYER_ONLINE_FILE, PLAYER_ONLINE);
    }

    public static PlayerOnline getPlayerOnlineStat(UUID uuid) {
        return PLAYER_ONLINE.stream().filter(data -> data.playerID().equals(uuid)).findFirst().orElse(null);
    }

    public static LocalDateTime getLastOnline(UUID uuid) {
        return PLAYER_ONLINE.stream().filter(data -> data.playerID().equals(uuid)).findFirst().map(PlayerOnline::lastSeen).orElse(null);
    }

    public static LocalDateTime getLastOnline(Player player) {
        return PLAYER_ONLINE.stream().filter(data -> data.playerID().equals(player.getUUID())).findFirst().map(PlayerOnline::lastSeen).orElse(null);
    }

    public static void loadStats() {
        PLAYER_ONLINE.clear();
        PLAYER_ONLINE.addAll(JsonUtil.loadOrCreate(PLAYER_ONLINE_FILE, new TypeToken<List<PlayerOnline>>() {}, ArrayList::new));
    }
}
