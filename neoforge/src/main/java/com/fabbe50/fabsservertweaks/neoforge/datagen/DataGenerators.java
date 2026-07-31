package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData;
import com.fabbe50.fabsservertweaks.neoforge.datagen.Recipes.*;
import com.fabbe50.fabsservertweaks.registries.LoreRegistry.ItemOrTag;
import com.fabbe50.fabsservertweaks.util.BlockOrBlockTag;
import com.fabbe50.fabsservertweaks.util.BuiltinDatapack;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataGenerator.PackGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Fabsservertweaks.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();

        registerRecipeProvider(dataGenerator, packOutput, ModRecipes.class, BuiltinDatapack.CUSTOM_RECIPES, event.getLookupProvider());
        registerRecipeProvider(dataGenerator, packOutput, MountArmorRecipes.class, BuiltinDatapack.MOUNT_ARMOR_RECIPES, event.getLookupProvider());
        registerRecipeProvider(dataGenerator, packOutput, RawBlockSmeltingRecipes.class, BuiltinDatapack.RAW_BLOCK_SMELTING, event.getLookupProvider());
        registerRecipeProvider(dataGenerator, packOutput, StoneStairRecipes.class, BuiltinDatapack.STONE_STAIRS_RECIPES, event.getLookupProvider());
        registerRecipeProvider(dataGenerator, packOutput, WoodOverridesRecipes.class, BuiltinDatapack.WOOD_OVERRIDE_RECIPES, event.getLookupProvider());
        registerRecipeProvider(dataGenerator, packOutput, CombineSlabRecipes.class, BuiltinDatapack.COMBINE_SLABS_RECIPES, event.getLookupProvider());
        registerRecipeProvider(dataGenerator, packOutput, PotterySherdDuplication.class, BuiltinDatapack.POTTERY_SHERD_DUPLICATION, event.getLookupProvider());

        registerCauldronConversionsProvider(dataGenerator, packOutput, BuiltinDatapack.CAULDRON_CONVERSIONS, event.getLookupProvider());

        registerDurabilitySmeltingProvider(dataGenerator, packOutput, BuiltinDatapack.DURABILITY_SMELTING, event.getLookupProvider());

        registerItemTagProviders(dataGenerator, packOutput, BuiltinDatapack.INFINITY_BUCKETS, Tags.INFINITY_BUCKETS_ITEM_TAGS, event.getLookupProvider());

        dataGenerator.addProvider(true, new TagProviders.BlockTags(packOutput, "", Tags.BASE_MOD_BLOCK_TAGS, event.getLookupProvider()));
        dataGenerator.addProvider(true, new TagProviders.ItemTags(packOutput, "", Tags.BASE_MOD_ITEM_TAGS, event.getLookupProvider()));
        dataGenerator.addProvider(true, new TagProviders.EntityTags(packOutput, "", Tags.BASE_MOD_ENTITY_TAGS, event.getLookupProvider()));
        registerBlockTagProviders(dataGenerator, packOutput, BuiltinDatapack.VAULT_BREAKING, Tags.VAULT_BREAKING_BLOCK_TAGS, event.getLookupProvider());
    }

    private static void registerBlockTagProviders(DataGenerator dataGenerator, PackOutput output, BuiltinDatapack dataPack, Map<TagKey<Block>, List<BlockOrBlockTag>> blockTagMap, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new TagProviders.BlockTags(fabricOutput, dataPack.name() + "_fabric", blockTagMap, lookupProvider));
        packGenerator.addProvider(arg -> new TagProviders.BlockTags(neoForgeOutput, dataPack.name() + "_neoforge", blockTagMap, lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerItemTagProviders(DataGenerator dataGenerator, PackOutput output, BuiltinDatapack dataPack, Map<TagKey<Item>, List<ItemOrTag<Item>>> itemTagMap, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new TagProviders.ItemTags(fabricOutput, dataPack.name() + "_fabric", itemTagMap, lookupProvider));
        packGenerator.addProvider(arg -> new TagProviders.ItemTags(neoForgeOutput, dataPack.name() + "_neoforge", itemTagMap, lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerEntityTypeTagProviders(DataGenerator dataGenerator, PackOutput output, BuiltinDatapack dataPack, Map<TagKey<EntityType<?>>, List<EntityType<?>>> entityTypeTagMap, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new TagProviders.EntityTags(fabricOutput, dataPack.name() + "_fabric", entityTypeTagMap, lookupProvider));
        packGenerator.addProvider(arg -> new TagProviders.EntityTags(neoForgeOutput, dataPack.name() + "_neoforge", entityTypeTagMap, lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerEnchantmentTagProviders(DataGenerator dataGenerator, PackOutput output, BuiltinDatapack dataPack, Map<TagKey<Enchantment>, List<ResourceKey<Enchantment>>> enchantmentTagMap, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new TagProviders.EnchantmentTags(fabricOutput, dataPack.name() + "_fabric", enchantmentTagMap, lookupProvider));
        packGenerator.addProvider(arg -> new TagProviders.EnchantmentTags(neoForgeOutput, dataPack.name() + "_neoforge", enchantmentTagMap, lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerRecipeProvider(DataGenerator dataGenerator, PackOutput output, Class<? extends RecipeProvider> recipeProvider, BuiltinDatapack dataPack, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new Recipes.Runner(recipeProvider, "fabric", fabricOutput, lookupProvider));
        packGenerator.addProvider(arg -> new Recipes.Runner(recipeProvider, "neoforge", neoForgeOutput, lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerCauldronConversionsProvider(DataGenerator dataGenerator, PackOutput output, BuiltinDatapack dataPack, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new CauldronConversions(fabricOutput, "fabric", Target.DATA_PACK, CauldronConversionData.CODEC.codec(), lookupProvider));
        packGenerator.addProvider(arg -> new CauldronConversions(neoForgeOutput, "neoforge", Target.DATA_PACK, CauldronConversionData.CODEC.codec(), lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerDurabilitySmeltingProvider(DataGenerator dataGenerator, PackOutput output, BuiltinDatapack dataPack, CompletableFuture<Provider> lookupProvider) {
        PackGenerator packGenerator = BuiltinDatapackOutputs.create(dataGenerator, dataPack);
        PackOutput fabricOutput = BuiltinDatapackOutputs.packOutput(output, "fabric", dataPack);
        PackOutput neoForgeOutput = BuiltinDatapackOutputs.packOutput(output, "neoforge", dataPack);
        packGenerator.addProvider(arg -> new DurabilitySmeltRecipes(fabricOutput, "fabric", Target.DATA_PACK, DurabilitySmeltData.CODEC.codec(), lookupProvider));
        packGenerator.addProvider(arg -> new DurabilitySmeltRecipes(neoForgeOutput, "neoforge", Target.DATA_PACK, DurabilitySmeltData.CODEC.codec(), lookupProvider));
        registerPackMetaData(packGenerator, fabricOutput, neoForgeOutput, dataPack);
    }

    private static void registerPackMetaData(PackGenerator packGenerator, PackOutput fabricOutput, PackOutput neoForgeOutput, BuiltinDatapack dataPack) {
        packGenerator.addProvider(arg -> BuiltinDatapackOutputs.metadata(fabricOutput, "fabric", dataPack));
        packGenerator.addProvider(arg -> BuiltinDatapackOutputs.metadata(neoForgeOutput, "neoforge", dataPack));
    }
}
