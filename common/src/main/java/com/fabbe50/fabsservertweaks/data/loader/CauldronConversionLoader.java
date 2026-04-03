package com.fabbe50.fabsservertweaks.data.loader;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.google.gson.JsonElement;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class CauldronConversionLoader extends SimpleJsonResourceReloadListener<JsonElement> {
    public static final CauldronConversionLoader INSTANCE = new CauldronConversionLoader();

    private final Map<Identifier, CauldronConversionData> dataMap = new HashMap<>();

    protected CauldronConversionLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("cauldron_conversion"));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        dataMap.clear();
        for (Map.Entry<Identifier, JsonElement> entry : object.entrySet()) {
            try {
                CauldronConversionData data = CauldronConversionData.fromJson(entry.getValue().getAsJsonObject());
                dataMap.put(entry.getKey(), data);
            } catch (Exception e) {
                LogUtil.error("Failed to load cauldron conversion data: " + entry.getKey());
            }
        }
        LogUtil.log("Loaded " + dataMap.size() + " cauldron conversion data entries");
    }

    public Map<Identifier, CauldronConversionData> getDataMap() {
        return dataMap;
    }
}
