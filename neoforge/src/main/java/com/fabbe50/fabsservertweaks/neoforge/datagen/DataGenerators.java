package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData;
import com.fabbe50.fabsservertweaks.neoforge.datagen.Recipes.*;
import com.fabbe50.fabsservertweaks.util.BuiltinDatapack;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataGenerator.PackGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Fabsservertweaks.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();

        PackGenerator customRecipes = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.CUSTOM_RECIPES);
        PackOutput customRecipesOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.CUSTOM_RECIPES);
        PackOutput customRecipesOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.CUSTOM_RECIPES);
        PackGenerator mountArmorRecipes = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.MOUNT_ARMOR_RECIPES);
        PackOutput mountArmorRecipesOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.MOUNT_ARMOR_RECIPES);
        PackOutput mountArmorRecipesOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.MOUNT_ARMOR_RECIPES);
        PackGenerator rawBlockSmelting = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.RAW_BLOCK_SMELTING);
        PackOutput rawBlockSmeltingOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.RAW_BLOCK_SMELTING);
        PackOutput rawBlockSmeltingOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.RAW_BLOCK_SMELTING);
        PackGenerator stoneStairRecipes = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.STONE_STAIRS_RECIPES);
        PackOutput stoneStairRecipesOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.STONE_STAIRS_RECIPES);
        PackOutput stoneStairRecipesOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.STONE_STAIRS_RECIPES);
        PackGenerator woodOverrideRecipes = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.WOOD_OVERRIDE_RECIPES);
        PackOutput woodOverrideRecipesOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.WOOD_OVERRIDE_RECIPES);
        PackOutput woodOverrideRecipesOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.WOOD_OVERRIDE_RECIPES);
        PackGenerator combineSlabRecipes = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.COMBINE_SLABS_RECIPES);
        PackOutput combineSlabRecipesOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.COMBINE_SLABS_RECIPES);
        PackOutput combineSlabRecipesOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.COMBINE_SLABS_RECIPES);
        PackGenerator potterySherdDuplication = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.POTTERY_SHERD_DUPLICATION);
        PackOutput potterySherdDuplicationOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.POTTERY_SHERD_DUPLICATION);
        PackOutput potterySherdDuplicationOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.POTTERY_SHERD_DUPLICATION);

        PackGenerator cauldronConversions = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.CAULDRON_CONVERSIONS);
        PackOutput cauldronConversionsOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.CAULDRON_CONVERSIONS);
        PackOutput cauldronConversionsOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.CAULDRON_CONVERSIONS);
        PackGenerator durabilitySmelting = BuiltinDatapackOutputs.create(dataGenerator, BuiltinDatapack.DURABILITY_SMELTING);
        PackOutput durabilitySmeltingOutputFabric = BuiltinDatapackOutputs.packOutput(packOutput, "fabric", BuiltinDatapack.DURABILITY_SMELTING);
        PackOutput durabilitySmeltingOutputNeoForge = BuiltinDatapackOutputs.packOutput(packOutput, "neoforge", BuiltinDatapack.DURABILITY_SMELTING);

        customRecipes.addProvider(arg -> new Recipes.Runner(ModRecipes.class, "fabric", customRecipesOutputFabric, event.getLookupProvider()));
        customRecipes.addProvider(arg -> new Recipes.Runner(ModRecipes.class, "neoforge", customRecipesOutputNeoForge, event.getLookupProvider()));
        customRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(customRecipesOutputFabric, "fabric", BuiltinDatapack.CUSTOM_RECIPES));
        customRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(customRecipesOutputNeoForge, "neoforge", BuiltinDatapack.CUSTOM_RECIPES));
        mountArmorRecipes.addProvider(arg -> new Recipes.Runner(MountArmorRecipes.class, "fabric", mountArmorRecipesOutputFabric, event.getLookupProvider()));
        mountArmorRecipes.addProvider(arg -> new Recipes.Runner(MountArmorRecipes.class, "neoforge", mountArmorRecipesOutputNeoForge, event.getLookupProvider()));
        mountArmorRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(mountArmorRecipesOutputFabric, "fabric", BuiltinDatapack.MOUNT_ARMOR_RECIPES));
        mountArmorRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(mountArmorRecipesOutputNeoForge, "neoforge", BuiltinDatapack.MOUNT_ARMOR_RECIPES));
        rawBlockSmelting.addProvider(arg -> new Recipes.Runner(RawBlockSmeltingRecipes.class, "fabric", rawBlockSmeltingOutputFabric, event.getLookupProvider()));
        rawBlockSmelting.addProvider(arg -> new Recipes.Runner(RawBlockSmeltingRecipes.class, "neoforge", rawBlockSmeltingOutputNeoForge, event.getLookupProvider()));
        rawBlockSmelting.addProvider(arg -> BuiltinDatapackOutputs.metadata(rawBlockSmeltingOutputFabric, "fabric", BuiltinDatapack.RAW_BLOCK_SMELTING));
        rawBlockSmelting.addProvider(arg -> BuiltinDatapackOutputs.metadata(rawBlockSmeltingOutputNeoForge, "neoforge", BuiltinDatapack.RAW_BLOCK_SMELTING));
        stoneStairRecipes.addProvider(arg -> new Recipes.Runner(StoneStairRecipes.class, "fabric", stoneStairRecipesOutputFabric, event.getLookupProvider()));
        stoneStairRecipes.addProvider(arg -> new Recipes.Runner(StoneStairRecipes.class, "neoforge", stoneStairRecipesOutputNeoForge, event.getLookupProvider()));
        stoneStairRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(stoneStairRecipesOutputFabric, "fabric", BuiltinDatapack.STONE_STAIRS_RECIPES));
        stoneStairRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(stoneStairRecipesOutputNeoForge, "neoforge", BuiltinDatapack.STONE_STAIRS_RECIPES));
        woodOverrideRecipes.addProvider(arg -> new Recipes.Runner(WoodOverridesRecipes.class, "fabric", woodOverrideRecipesOutputFabric, event.getLookupProvider()));
        woodOverrideRecipes.addProvider(arg -> new Recipes.Runner(WoodOverridesRecipes.class, "neoforge", woodOverrideRecipesOutputNeoForge, event.getLookupProvider()));
        woodOverrideRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(woodOverrideRecipesOutputFabric, "fabric", BuiltinDatapack.WOOD_OVERRIDE_RECIPES));
        woodOverrideRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(woodOverrideRecipesOutputNeoForge, "neoforge", BuiltinDatapack.WOOD_OVERRIDE_RECIPES));
        combineSlabRecipes.addProvider(arg -> new Recipes.Runner(CombineSlabRecipes.class, "fabric", combineSlabRecipesOutputFabric, event.getLookupProvider()));
        combineSlabRecipes.addProvider(arg -> new Recipes.Runner(CombineSlabRecipes.class, "neoforge", combineSlabRecipesOutputNeoForge, event.getLookupProvider()));
        combineSlabRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(combineSlabRecipesOutputFabric, "fabric", BuiltinDatapack.COMBINE_SLABS_RECIPES));
        combineSlabRecipes.addProvider(arg -> BuiltinDatapackOutputs.metadata(combineSlabRecipesOutputNeoForge, "neoforge", BuiltinDatapack.COMBINE_SLABS_RECIPES));
        potterySherdDuplication.addProvider(arg -> new Recipes.Runner(PotterySherdDuplication.class, "fabric", potterySherdDuplicationOutputFabric, event.getLookupProvider()));
        potterySherdDuplication.addProvider(arg -> new Recipes.Runner(PotterySherdDuplication.class, "neoforge", potterySherdDuplicationOutputNeoForge, event.getLookupProvider()));
        potterySherdDuplication.addProvider(arg -> BuiltinDatapackOutputs.metadata(potterySherdDuplicationOutputFabric, "fabric", BuiltinDatapack.POTTERY_SHERD_DUPLICATION));
        potterySherdDuplication.addProvider(arg -> BuiltinDatapackOutputs.metadata(potterySherdDuplicationOutputNeoForge, "neoforge", BuiltinDatapack.POTTERY_SHERD_DUPLICATION));
        cauldronConversions.addProvider(arg -> new CauldronConversions(cauldronConversionsOutputFabric, "fabric", PackOutput.Target.DATA_PACK, CauldronConversionData.CODEC.codec(), event.getLookupProvider()));
        cauldronConversions.addProvider(arg -> new CauldronConversions(cauldronConversionsOutputNeoForge, "neoforge", PackOutput.Target.DATA_PACK, CauldronConversionData.CODEC.codec(), event.getLookupProvider()));
        cauldronConversions.addProvider(arg -> BuiltinDatapackOutputs.metadata(cauldronConversionsOutputFabric, "fabric", BuiltinDatapack.CAULDRON_CONVERSIONS));
        cauldronConversions.addProvider(arg -> BuiltinDatapackOutputs.metadata(cauldronConversionsOutputNeoForge, "neoforge", BuiltinDatapack.CAULDRON_CONVERSIONS));
        durabilitySmelting.addProvider(arg -> new DurabilitySmeltRecipes(durabilitySmeltingOutputFabric, "fabric", PackOutput.Target.DATA_PACK, DurabilitySmeltData.CODEC.codec(), event.getLookupProvider()));
        durabilitySmelting.addProvider(arg -> new DurabilitySmeltRecipes(durabilitySmeltingOutputNeoForge, "neoforge", PackOutput.Target.DATA_PACK, DurabilitySmeltData.CODEC.codec(), event.getLookupProvider()));
        durabilitySmelting.addProvider(arg -> BuiltinDatapackOutputs.metadata(durabilitySmeltingOutputFabric, "fabric", BuiltinDatapack.DURABILITY_SMELTING));
        durabilitySmelting.addProvider(arg -> BuiltinDatapackOutputs.metadata(durabilitySmeltingOutputNeoForge, "neoforge", BuiltinDatapack.DURABILITY_SMELTING));
    }
}
