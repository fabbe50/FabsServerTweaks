package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class CauldronConversions extends JsonCodecProvider<CauldronConversionData> {
    public CauldronConversions(PackOutput output, PackOutput.Target target, Codec<CauldronConversionData> codec, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, target, Fabsservertweaks.location("cauldron_conversion").getPath(), codec, lookupProvider, Fabsservertweaks.MOD_ID);
    }

    @Override
    protected void gather() {
        cauldronConversion("white_concrete_powder_to_concrete", Items.WHITE_CONCRETE_POWDER, Items.WHITE_CONCRETE);
        cauldronConversion("light_gray_concrete_powder_to_concrete", Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE);
        cauldronConversion("gray_concrete_powder_to_concrete", Items.GRAY_CONCRETE_POWDER, Items.GRAY_CONCRETE);
        cauldronConversion("black_concrete_powder_to_concrete", Items.BLACK_CONCRETE_POWDER, Items.BLACK_CONCRETE);
        cauldronConversion("brown_concrete_powder_to_concrete", Items.BROWN_CONCRETE_POWDER, Items.BROWN_CONCRETE);
        cauldronConversion("red_concrete_powder_to_concrete", Items.RED_CONCRETE_POWDER, Items.RED_CONCRETE);
        cauldronConversion("orange_concrete_powder_to_concrete", Items.ORANGE_CONCRETE_POWDER, Items.ORANGE_CONCRETE);
        cauldronConversion("yellow_concrete_powder_to_concrete", Items.YELLOW_CONCRETE_POWDER, Items.YELLOW_CONCRETE);
        cauldronConversion("lime_concrete_powder_to_concrete", Items.LIME_CONCRETE_POWDER, Items.LIME_CONCRETE);
        cauldronConversion("green_concrete_powder_to_concrete", Items.GREEN_CONCRETE_POWDER, Items.GREEN_CONCRETE);
        cauldronConversion("cyan_concrete_powder_to_concrete", Items.CYAN_CONCRETE_POWDER, Items.CYAN_CONCRETE);
        cauldronConversion("light_blue_concrete_powder_to_concrete", Items.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE);
        cauldronConversion("blue_concrete_powder_to_concrete", Items.BLUE_CONCRETE_POWDER, Items.BLUE_CONCRETE);
        cauldronConversion("purple_concrete_powder_to_concrete", Items.PURPLE_CONCRETE_POWDER, Items.PURPLE_CONCRETE);
        cauldronConversion("magenta_concrete_powder_to_concrete", Items.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_CONCRETE);
        cauldronConversion("pink_concrete_powder_to_concrete", Items.PINK_CONCRETE_POWDER, Items.PINK_CONCRETE);
    }

    private void cauldronConversion(String location, Item input, Item output) {
        this.unconditional(Fabsservertweaks.location(location), new CauldronConversionData(ModRegistry.ITEMS.getId(input), ModRegistry.ITEMS.getId(output)));
    }
}
