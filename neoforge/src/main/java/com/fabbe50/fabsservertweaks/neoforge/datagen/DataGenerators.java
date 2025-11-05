package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.data.DurabilitySmeltData;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Fabsservertweaks.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();

        event.createProvider(Recipes.Runner::new);
        event.addProvider(new CauldronConversions(packOutput, PackOutput.Target.DATA_PACK, CauldronConversionData.CODEC.codec(), event.getLookupProvider()));
        event.addProvider(new DurabilitySmeltRecipes(packOutput, PackOutput.Target.DATA_PACK, DurabilitySmeltData.CODEC.codec(), event.getLookupProvider()));
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {

    }
}
