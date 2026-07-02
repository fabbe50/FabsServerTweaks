package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.neoforge.datagen.ModDataMapProvider;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;

import java.util.Optional;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        builder.addStartMix(ingredient, output);
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Item output) {
        builder.addContainerRecipe(PotionContents.createItemStack(Items.POTION, Potions.AWKWARD).getItem(), ingredient, output);
        builder.addMix(Potions.WATER, ingredient, Potions.MUNDANE);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        builder.addMix(input, ingredient, output);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Item input, Item ingredient, Item output) {
        builder.addContainerRecipe(input, ingredient, output);
    }

    public static void registerCompostable(float chance, Item item, boolean canVillagerCompost) {
        Optional<ResourceKey<Item>> itemKey = ModRegistry.ITEMS.getKey(item);
        itemKey.ifPresent(itemResourceKey -> ModDataMapProvider.addCompostable(itemResourceKey, new Compostable(chance, canVillagerCompost)));
    }

    public static boolean isDataGen() {
        return DatagenModLoader.isRunningDataGen();
    }
}
