package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.util.BuiltinDatapack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.DetectedVersion;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataGenerator.PackGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FeatureFlagsMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class BuiltinDatapackOutputs {
    private BuiltinDatapackOutputs() {
    }

    public static PackGenerator create(DataGenerator dataGenerator, BuiltinDatapack datapack) {
        LogUtil.log("Generating datapack: " + datapack.displayName());
        return dataGenerator.getPackGenerator(true, Fabsservertweaks.MOD_ID, datapack.path());
    }

    public static PackOutput packOutput(PackOutput packOutput, String type, BuiltinDatapack datapack) {
        Path path = packOutput.getOutputFolder()
                .resolve(type.equals("fabric") ? "resourcepacks" : "mod")
                .resolve(datapack.path())
                .toAbsolutePath();

        return new PackOutput(path);
    }

    public static CustomPackMetadataGenerator metadata(PackOutput packOutput, String type, BuiltinDatapack datapack) {
        return CustomPackMetadataGenerator.forFeaturePack(packOutput, type, datapack.displayName());
    }

    public static class CustomPackMetadataGenerator implements DataProvider {
        private final PackOutput output;
        private final String name;
        private final Map<String, Supplier<JsonElement>> elements = new HashMap<>();

        public CustomPackMetadataGenerator(PackOutput packOutput, String name) {
            this.output = packOutput;
            this.name = name;
        }

        public <T> CustomPackMetadataGenerator add(MetadataSectionType<T> arg, T object) {
            this.elements.put(arg.name(), () -> arg.codec().encodeStart(JsonOps.INSTANCE, object).getOrThrow(IllegalArgumentException::new).getAsJsonObject());
            return this;
        }

        public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cachedOutput) {
            JsonObject jsonobject = new JsonObject();
            this.elements.forEach((string, supplier) -> jsonobject.add(string, supplier.get()));
            return DataProvider.saveStable(cachedOutput, jsonobject, this.output.getOutputFolder().resolve("pack.mcmeta"));
        }

        public final @NonNull String getName() {
            return this.output.getOutputFolder().getFileName().toString() + "_" + this.name + "_metadata_provider";
        }

        public static CustomPackMetadataGenerator forFeaturePack(PackOutput packOutput, String name, Component component) {
            return (new CustomPackMetadataGenerator(packOutput, name)).add(PackMetadataSection.SERVER_TYPE, new PackMetadataSection(component, DetectedVersion.BUILT_IN.packVersion(PackType.SERVER_DATA).minorRange()));
        }

        public static CustomPackMetadataGenerator forFeaturePack(PackOutput packOutput, String name, Component component, FeatureFlagSet featureFlagSet) {
            return forFeaturePack(packOutput, name, component).add(FeatureFlagsMetadataSection.TYPE, new FeatureFlagsMetadataSection(featureFlagSet));
        }
    }
}
