package com.fabbe50.fabsservertweaks.data.loader;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CauldronConversionLoader extends SimpleJsonResourceReloadListener<JsonElement> {
    public static final CauldronConversionLoader INSTANCE = new CauldronConversionLoader();

    private final Map<Identifier, CauldronConversionData> dataMap = new HashMap<>();

    protected CauldronConversionLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("cauldron_conversion"));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> object, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
        dataMap.clear();
        Frozen frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, frozen);
        for (Map.Entry<Identifier, JsonElement> entry : object.entrySet()) {
            try {
                CauldronConversionData.CODEC.codec().parse(ops, entry.getValue()).resultOrPartial(error -> LogUtil.error(String.format("Failed to load %s: %s", entry.getKey().toString(), error))).ifPresent(data -> dataMap.put(entry.getKey(), data));
            } catch (Exception e) {
                LogUtil.error("Failed to load cauldron conversion data: " + entry.getKey());
                LogUtil.error(e.getMessage(), e.fillInStackTrace());
            }
        }
        LogUtil.log("Loaded " + dataMap.size() + " cauldron conversion data entries");
    }

    public Map<Identifier, CauldronConversionData> getDataMap() {
        return dataMap;
    }
}
