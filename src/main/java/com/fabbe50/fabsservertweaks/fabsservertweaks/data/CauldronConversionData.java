package com.fabbe50.fabsservertweaks.fabsservertweaks.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record CauldronConversionData(ResourceLocation input, ResourceLocation output) {
    public static final MapCodec<CauldronConversionData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("input").forGetter(CauldronConversionData::input),
                    ResourceLocation.CODEC.fieldOf("output").forGetter(CauldronConversionData::output)
            ).apply(instance, CauldronConversionData::new));

    public static CauldronConversionData fromJson(JsonObject json) {
        ResourceLocation input = ResourceLocation.parse(json.get("input").getAsString());
        ResourceLocation output = ResourceLocation.parse(json.get("output").getAsString());
        return new CauldronConversionData(input, output);
    }
}
