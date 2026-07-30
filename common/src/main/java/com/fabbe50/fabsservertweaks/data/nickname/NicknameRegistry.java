package com.fabbe50.fabsservertweaks.data.nickname;

import com.fabbe50.fabsservertweaks.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class NicknameRegistry {
    public static final String NICKNAMES_FILE = "nicknames.json";
    public static final List<NicknameData> NICKNAMES = new ArrayList<>();

    public static void setNickname(Player player, String nickname, ChatFormatting color) {
        NICKNAMES.removeIf(data -> data.playerID().equals(player.getUUID()));
        NicknameData data = new NicknameData(player.getUUID(), nickname, color);
        NICKNAMES.add(data);
        JsonUtil.save(NICKNAMES_FILE, NICKNAMES);
    }

    public static void clearNickname(Player player) {
        NICKNAMES.removeIf(data -> data.playerID().equals(player.getUUID()));
        JsonUtil.save(NICKNAMES_FILE, NICKNAMES);
    }

    public static String getNickname(Player player) {
        return NICKNAMES.stream().filter(data -> data.playerID().equals(player.getUUID())).findFirst().map(NicknameData::nickname).orElse(player.getName().getString());
    }

    public static ChatFormatting getNicknameColor(Player player) {
        return NICKNAMES.stream().filter(data -> data.playerID().equals(player.getUUID())).findFirst().map(NicknameData::color).orElse(ChatFormatting.RESET);
    }

    public static void loadNicknames() {
        NICKNAMES.clear();
        NICKNAMES.addAll(JsonUtil.loadOrCreate(NICKNAMES_FILE, new TypeToken<List<NicknameData>>() {}, ArrayList::new));
    }
}
