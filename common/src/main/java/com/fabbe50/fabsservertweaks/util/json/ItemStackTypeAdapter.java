package com.fabbe50.fabsservertweaks.util.json;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ItemStackTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private final Supplier<Provider> lookupProviderSupplier;

    public ItemStackTypeAdapter(Supplier<Provider> lookupProviderSupplier) {
        this.lookupProviderSupplier = lookupProviderSupplier;
    }


    @Override
    public JsonElement serialize(ItemStack stack, Type type, JsonSerializationContext context) {
        HolderLookup.Provider lookupProvider = lookupProviderSupplier.get();
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, lookupProvider);
        DataResult<JsonElement> result = ItemStack.OPTIONAL_CODEC.encodeStart(registryOps, stack);
        return getResult(result, "Failed to serialize ItemStack");
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        HolderLookup.Provider lookupProvider = lookupProviderSupplier.get();
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, lookupProvider);
        DataResult<ItemStack> result = ItemStack.OPTIONAL_CODEC.parse(registryOps, jsonElement);
        return getResult(result, "Failed to deserialize ItemStack");
    }

    private static <T> T getResult(DataResult<T> result, String message) {
        AtomicReference<String> errorMessage = new AtomicReference<>();

        return result.resultOrPartial(errorMessage::set).orElseThrow(() -> new JsonParseException(message + ": " + errorMessage.get()));
    }
}
