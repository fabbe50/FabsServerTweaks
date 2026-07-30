package com.fabbe50.fabsservertweaks.data.soulbound;

import com.fabbe50.fabsservertweaks.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SoulBoundRegistry {
    public static final String SOUL_BOUND_DATA_FILE = "soul_bound_data.json";
    public static final List<SoulBoundData> SOUL_BOUND_DATA = new ArrayList<>();

    public static void setSoulBoundItems(Player player, List<ItemStack> soulBoundItems) {
        SOUL_BOUND_DATA.removeIf(data -> data.playerID().equals(player.getUUID()));
        SOUL_BOUND_DATA.add(new SoulBoundData(player.getUUID(), soulBoundItems));
        JsonUtil.save(SOUL_BOUND_DATA_FILE, SOUL_BOUND_DATA);
    }

    public static List<ItemStack> getAndClearSoulBoundItems(Player player) {
        List<ItemStack> soulBoundItems = new ArrayList<>(getSoulBoundItems(player));
        clearSoulBoundItems(player);
        return soulBoundItems;
    }

    public static void clearSoulBoundItems(Player player) {
        SOUL_BOUND_DATA.removeIf(data -> data.playerID().equals(player.getUUID()));
        JsonUtil.save(SOUL_BOUND_DATA_FILE, SOUL_BOUND_DATA);
    }

    public static List<ItemStack> getSoulBoundItems(Player player) {
        return SOUL_BOUND_DATA.stream().filter(data -> data.playerID().equals(player.getUUID())).findFirst().map(SoulBoundData::soulBoundItems).orElse(new ArrayList<>());
    }

    public static void loadSoulBoundData() {
        SOUL_BOUND_DATA.clear();
        SOUL_BOUND_DATA.addAll(JsonUtil.loadOrCreate(SOUL_BOUND_DATA_FILE, new TypeToken<List<SoulBoundData>>(){}, ArrayList::new));
    }
}
