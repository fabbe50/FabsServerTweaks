package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import me.shedaniel.clothconfig2.impl.builders.FloatFieldBuilder;

import java.util.Properties;

public class FloatOption extends AbstractRangedConfigOption<Float, FloatListEntry> {
    public FloatOption(String name, Float defaultValue, Float min, Float max) {
        super(name, defaultValue, min, max);
    }

    public FloatOption(String name, Float defaultValue, Float value, Float min, Float max) {
        super(name, defaultValue, value, min, max);
    }

    public Builder makeBuilder() {
        return new Builder(name, defaultValue, min(), max());
    }

    @Override
    public FloatListEntry buildClothEntry(ConfigEntryBuilder builder) {
        FloatFieldBuilder fieldBuilder = builder.startFloatField(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue);
        fieldBuilder.requireRestart(requiresRestart);
        return fieldBuilder.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Float.parseFloat((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public class Builder extends AbstractRangedConfigOption<Float, FloatListEntry>.Builder<FloatOption> {
        public Builder(String name, Float defaultValue, Float min, Float max) {
            super(name, defaultValue, min, max);
        }

        @Override
        public FloatOption build() {
            FloatOption option = new FloatOption(name, defaultValue, min, max);
            option.requiresRestart = requiresRestart;
            option.category = category;
            option.subCategory = subCategory;
            return option;
        }
    }
}
