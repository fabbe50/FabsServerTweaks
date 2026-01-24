package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import net.minecraft.network.chat.Component;

import java.util.Properties;

public class IntegerSliderOption extends AbstractRangedConfigOption<Integer, IntegerSliderEntry> {
    private String textGetterValue;

    public IntegerSliderOption(String name, Integer defaultValue, Integer min, Integer max) {
        this(name, defaultValue, min, max, "Value: %s");
    }

    public IntegerSliderOption(String name, Integer defaultValue, Integer min, Integer max, String textGetterValue) {
        this(name, defaultValue, defaultValue, min, max, textGetterValue);
    }

    public IntegerSliderOption(String name, Integer defaultValue, Integer value, Integer min, Integer max, String textGetterValue) {
        super(name, defaultValue, value, min, max);
        this.textGetterValue = textGetterValue;
    }

    public Builder makeBuilder() {
        return new Builder(name, defaultValue, min(), max());
    }

    @Override
    public IntegerSliderEntry buildClothEntry(ConfigEntryBuilder builder) {
        IntSliderBuilder sliderBuilder = builder.startIntSlider(getTranslation(), getValue(), min(), max())
                .setDefaultValue(getDefaultValue())
                .setTooltip(getTooltipTranslation())
                .setTextGetter(integer -> Component.translatable(textGetterValue, integer))
                .setSaveConsumer(this::setValue);
        sliderBuilder.requireRestart(requiresRestart);
        return sliderBuilder.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Integer.parseInt((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public class Builder extends AbstractRangedConfigOption<Integer, IntegerSliderEntry>.Builder<IntegerSliderOption> {
        private String textGetter = "Value: %s";

        public Builder(String name, Integer defaultValue) {
            super(name, defaultValue);
        }

        public Builder(String name, Integer defaultValue, Integer min, Integer max) {
            super(name, defaultValue, min, max);
        }

        public Builder(String name, Integer defaultValue, boolean requiresRestart, String category, String subCategory) {
            super(name, defaultValue, requiresRestart, category, subCategory);
        }

        public Builder textGetter(String textGetter) {
            this.textGetter = textGetter;
            return this;
        }

        @Override
        public IntegerSliderOption build() {
            IntegerSliderOption sliderOption = new IntegerSliderOption(name, defaultValue, min, max);
            sliderOption.textGetterValue = textGetter;
            sliderOption.requiresRestart = requiresRestart;
            sliderOption.category = category;
            sliderOption.subCategory = subCategory;
            return sliderOption;
        }
    }
}
