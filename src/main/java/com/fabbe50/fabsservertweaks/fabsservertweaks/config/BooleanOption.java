package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;

import java.util.Properties;

public class BooleanOption extends AbstractConfigOption<Boolean, BooleanListEntry> {
    public BooleanOption(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    public BooleanOption(String name, Boolean defaultValue, Boolean value) {
        super(name, defaultValue, value);
    }

    public Builder makeBuilder() {
        return new Builder(name, defaultValue);
    }

    @Override
    public BooleanListEntry buildClothEntry(ConfigEntryBuilder builder) {
        BooleanToggleBuilder booleanBuilder = builder.startBooleanToggle(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue);
        booleanBuilder.requireRestart(requiresRestart);
        return booleanBuilder.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Boolean.parseBoolean((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public class Builder extends AbstractConfigOption<Boolean, BooleanListEntry>.Builder<BooleanOption> {
        public Builder(String name, Boolean defaultValue) {
            super(name, defaultValue);
        }

        @Override
        public BooleanOption build() {
            BooleanOption option = new BooleanOption(name, defaultValue);
            option.requiresRestart = requiresRestart;
            option.category = category;
            option.subCategory = subCategory;
            return option;
        }
    }
}
