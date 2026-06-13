package com.fabbe50.fabsservertweaks.plugin.rei;

import com.fabbe50.fabsservertweaks.data.CauldronConversionData;
import com.fabbe50.fabsservertweaks.plugin.recipe_viewer_common.CauldronConversion;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CauldronConversionREI extends CauldronConversion<DisplayRegistry> {
    public static final CategoryIdentifier<CauldronConversionDisplay> CAULDRON_CONVERSION_DISPLAY = CategoryIdentifier.of(CAULDRON_CONVERSION);

    @Override
    protected void registerRecipes(DisplayRegistry registry, List<CauldronRecipe> cauldronRecipes) {
        for (CauldronRecipe recipe : cauldronRecipes) {
            registry.add(new CauldronConversionDisplay(List.of(EntryIngredients.of(recipe.input())), List.of(EntryIngredients.of(recipe.output()))));
        }
    }

    public static class CauldronConversionCategory implements DisplayCategory<CauldronConversionDisplay> {
        @Override
        public CategoryIdentifier<? extends CauldronConversionDisplay> getCategoryIdentifier() {
            return CAULDRON_CONVERSION_DISPLAY;
        }

        @Override
        public Component getTitle() {
            return NAME;
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(Blocks.WATER_CAULDRON);
        }

        @Override
        public List<Widget> setupDisplay(CauldronConversionDisplay display, Rectangle bounds) {
            Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
            List<Widget> widgets = new ArrayList<>();
            widgets.add(Widgets.createRecipeBase(bounds));
            widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                    .entries(display.getOutputEntries().getFirst())
                    .disableBackground()
                    .markOutput());
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y - 5))
                    .entries(display.getInputEntries().getFirst())
                    .markInput());
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 14))
                    .entries(List.of(EntryStacks.of(Blocks.WATER_CAULDRON)))
                    .markInput());

            return widgets;
        }
    }

    public static class CauldronConversionDisplay extends BasicDisplay {
        public static final MapCodec<CauldronConversionDisplay> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.list(EntryIngredient.codec()).fieldOf("inputs").forGetter(CauldronConversionDisplay::getInputEntries),
                        Codec.list(EntryIngredient.codec()).fieldOf("outputs").forGetter(CauldronConversionDisplay::getOutputEntries)
                ).apply(instance, CauldronConversionDisplay::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, CauldronConversionDisplay> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(Codec.list(EntryIngredient.codec())), BasicDisplay::getInputEntries,
                ByteBufCodecs.fromCodec(Codec.list(EntryIngredient.codec())), BasicDisplay::getOutputEntries,
                CauldronConversionDisplay::new
        );

        public CauldronConversionDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
            super(inputs, outputs);
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return CAULDRON_CONVERSION_DISPLAY;
        }

        @Override
        public @Nullable DisplaySerializer<? extends Display> getSerializer() {
            return DisplaySerializer.of(CODEC, STREAM_CODEC);
        }
    }
}
