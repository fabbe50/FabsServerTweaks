package com.fabbe50.fabsservertweaks.plugin.recipe_viewer_common;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.data.loader.CauldronConversionLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CauldronConversion<T> {
    public static final Identifier CAULDRON_CONVERSION = Fabsservertweaks.location("cauldron_conversion");
    public static final Component NAME = Component.translatable("recipe_viewer.category.fabsservertweaks.cauldron_conversion");

    public void registerRecipes(T registry) {
        Map<Identifier, CauldronConversionData> cauldronConversions = CauldronConversionLoader.INSTANCE.getDataMap();
        List<CauldronRecipe> recipes = new ArrayList<>();
        for (CauldronConversionData entry : cauldronConversions.values()) {
            if (entry.input().isEmpty() || entry.output().isEmpty()) {
                continue;
            }
            recipes.add(new CauldronRecipe(entry.input().copy(), entry.output().copy()));
        }
        registerRecipes(registry, recipes);
    }

    protected abstract void registerRecipes(T registry, List<CauldronRecipe> cauldronRecipes);

    public record CauldronRecipe(ItemStack input, ItemStack output) {
    }
}
