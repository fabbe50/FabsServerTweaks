package com.fabbe50.fabsservertweaks.data.nickname;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import dev.architectury.platform.Platform;
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
        JsonUtil.save(Platform.getGameFolder(), Fabsservertweaks.MOD_ID, NICKNAMES_FILE, NICKNAMES);
    }

    public static void clearNickname(Player player) {
        NICKNAMES.removeIf(data -> data.playerID().equals(player.getUUID()));
        JsonUtil.save(Platform.getGameFolder(), Fabsservertweaks.MOD_ID, NICKNAMES_FILE, NICKNAMES);
    }

    public static String getNickname(Player player) {
        return NICKNAMES.stream().filter(data -> data.playerID().equals(player.getUUID())).findFirst().map(NicknameData::nickname).orElse(player.getName().getString());
    }

    public static ChatFormatting getNicknameColor(Player player) {
        return NICKNAMES.stream().filter(data -> data.playerID().equals(player.getUUID())).findFirst().map(NicknameData::color).orElse(ChatFormatting.RESET);
    }

    public static void loadNicknames() {
        NICKNAMES.clear();
        NICKNAMES.addAll(JsonUtil.loadOrCreate(Platform.getGameFolder(), Fabsservertweaks.MOD_ID, NICKNAMES_FILE, new TypeToken<List<NicknameData>>() {}, ArrayList::new));
    }
}
