package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.impl.builders.DoubleFieldBuilder;

import java.util.Properties;

public class DoubleOption extends AbstractRangedConfigOption<Double, DoubleListEntry> {
    public DoubleOption(String name, Double defaultValue) {
        super(name, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleOption(String name, Double defaultValue, Double min, Double max) {
        super(name, defaultValue, min, max);
    }

    public DoubleOption(String name, Double defaultValue, Double value, Double min, Double max) {
        super(name, defaultValue, value, min, max);
    }

    public Builder makeBuilder() {
        return new Builder(name, defaultValue, min(), max());
    }

    @Override
    public DoubleListEntry buildClothEntry(ConfigEntryBuilder builder) {
        DoubleFieldBuilder fieldBuilder = builder.startDoubleField(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue);
        fieldBuilder.requireRestart(requiresRestart);
        return fieldBuilder.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Double.parseDouble((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public class Builder extends AbstractRangedConfigOption<Double, DoubleListEntry>.Builder<DoubleOption> {
        public Builder(String name, Double defaultValue, Double min, Double max) {
            super(name, defaultValue, min, max);
        }

        @Override
        public DoubleOption build() {
            DoubleOption option = new DoubleOption(name, defaultValue, min, max);
            option.requiresRestart = requiresRestart;
            option.category = category;
            option.subCategory = subCategory;
            return option;
        }
    }
}
