package com.fabbe50.fabsservertweaks.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record CauldronConversionData(Identifier input, Identifier output) {
    public static final MapCodec<CauldronConversionData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("input").forGetter(CauldronConversionData::input),
                    Identifier.CODEC.fieldOf("output").forGetter(CauldronConversionData::output)
            ).apply(instance, CauldronConversionData::new));

    public static CauldronConversionData fromJson(JsonObject json) {
        Identifier input = Identifier.parse(json.get("input").getAsString());
        Identifier output = Identifier.parse(json.get("output").getAsString());
        return new CauldronConversionData(input, output);
    }
}
