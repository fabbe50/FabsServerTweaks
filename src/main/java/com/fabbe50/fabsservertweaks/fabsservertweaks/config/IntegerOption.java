package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;

import java.util.Properties;

public class IntegerOption extends AbstractRangedConfigOption<Integer, IntegerListEntry> {
    public IntegerOption(String name, Integer defaultValue) {
        this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerOption(String name, Integer defaultValue, Integer min, Integer max) {
        this(name, defaultValue, defaultValue, min, max);
    }

    public IntegerOption(String name, Integer defaultValue, Integer value, Integer min, Integer max) {
        super(name, defaultValue, value, min, max);
    }

    public Builder makeBuilder() {
        return new Builder(name, defaultValue, min(), max());
    }

    @Override
    public IntegerListEntry buildClothEntry(ConfigEntryBuilder builder) {
        IntFieldBuilder field = builder.startIntField(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setMin(min())
                .setMax(max())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue);
        field.requireRestart(requiresRestart);
        return field.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Integer.parseInt((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public class Builder extends AbstractRangedConfigOption<Integer, IntegerListEntry>.Builder<IntegerOption> {
        public Builder(String name, Integer defaultValue, Integer min, Integer max) {
            super(name, defaultValue, min, max);
        }

        @Override
        public IntegerOption build() {
            IntegerOption option = new IntegerOption(name, defaultValue, min, max);
            option.requiresRestart = requiresRestart;
            option.category = category;
            option.subCategory = subCategory;
            return option;
        }
    }
}
