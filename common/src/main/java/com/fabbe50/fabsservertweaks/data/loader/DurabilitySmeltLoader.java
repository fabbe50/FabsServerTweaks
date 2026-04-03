package com.fabbe50.fabsservertweaks.data.loader;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData;
import com.google.gson.JsonElement;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DurabilitySmeltLoader extends SimpleJsonResourceReloadListener<JsonElement> {
    public static final DurabilitySmeltLoader INSTANCE = new DurabilitySmeltLoader();
    
    private final Map<Identifier, DurabilitySmeltData> dataMap = new HashMap<>();

    public DurabilitySmeltLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("durability_smelting"));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        dataMap.clear();
        for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
            try {
                DurabilitySmeltData data = DurabilitySmeltData.fromJson(entry.getValue().getAsJsonObject());
                dataMap.put(entry.getKey(), data);
            } catch (Exception e) {
                LogUtil.error("Failed to load durability recipe: " + entry.getKey());
                LogUtil.error(e.toString());
            }
        }
        LogUtil.log("Loaded " + dataMap.size() + " durability recipe entries");
    }

    public static @Nullable DurabilitySmeltData find(ItemStack input, String furnaceKind) {
        Map<Identifier, DurabilitySmeltData> data = INSTANCE.getDataMap();
        for (Identifier resourceLocation : data.keySet()) {
            if (data.get(resourceLocation).furnaceType().contains(furnaceKind) && data.get(resourceLocation).ingredient().test(input)) {
                return data.get(resourceLocation);
            }
        }
        return null;
    }

    public static @Nullable DurabilitySmeltData.Tier pickTier(DurabilitySmeltData data, ItemStack in) {
        int pct = 100;
        if (in.isDamageableItem() && in.getMaxDamage() > 0) {
            double rem = 1.0 - ((double)in.getDamageValue() / (double)in.getMaxDamage());
            pct = (int)Math.floor(rem * 100.0);
        }
        for (var t : data.tiers()) if (pct >= t.minPercent()) return t;
        return null;
    }

    public Map<Identifier, DurabilitySmeltData> getDataMap() {
        return dataMap;
    }
}
