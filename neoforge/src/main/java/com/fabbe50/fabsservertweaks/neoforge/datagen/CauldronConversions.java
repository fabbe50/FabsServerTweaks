package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.util.ItemStackUtil;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class CauldronConversions extends JsonCodecProvider<CauldronConversionData> {
    private final String type;

    public CauldronConversions(PackOutput output, String type, PackOutput.Target target, Codec<CauldronConversionData> codec, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, target, Fabsservertweaks.location("cauldron_conversion").getPath(), codec, lookupProvider, Fabsservertweaks.MOD_ID);
        this.type = type;
    }

    @Override
    public @NonNull String getName() {
        return super.getName() + "_" + this.type;
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
        cauldronConversion("dirt_to_mud", Items.DIRT, Items.MUD);
        cauldronConversion(
                "lit_campfire_to_unlit_campfire",
                new ItemStackTemplate(Blocks.CAMPFIRE.asItem()),
                ItemStackUtil.createStackTemplateWithState(Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false))
        );
        cauldronConversion(
                "lit_soul_campfire_to_unlit_soul_campfire",
                new ItemStackTemplate(Blocks.SOUL_CAMPFIRE.asItem()),
                ItemStackUtil.createStackTemplateWithState(Blocks.SOUL_CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false))
        );
    }

    private void cauldronConversion(String location, Item input, Item output) {
        this.unconditional(Fabsservertweaks.location(location), new CauldronConversionData(new ItemStackTemplate(input), new ItemStackTemplate(output)));
    }

    private void cauldronConversion(String location, ItemStackTemplate input, ItemStackTemplate output) {
        this.unconditional(Fabsservertweaks.location(location), new CauldronConversionData(input, output));
    }
}
