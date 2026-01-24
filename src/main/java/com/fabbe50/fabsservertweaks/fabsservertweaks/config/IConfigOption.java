package com.fabbe50.fabsservertweaks.fabsservertweaks.config;

import com.fabbe50.fabsservertweaks.fabsservertweaks.util.LangUtils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.network.chat.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public interface IConfigOption<T, R> extends IClothBuilder<T, R> {
    String getKey();

    String getCategory();

    String getSubCategory();

    default Component getTranslation() {
        return LangUtils.getConfig(getKey());
    }

    default Component getTooltipTranslation() {
        return LangUtils.getConfigTooltip(getKey());
    }

    void setValue(T value);

    T getValue();

    T getDefaultValue();

    default void writeData(FileOutputStream fos) throws IOException {
        fos.write((getKey() + "=" + getValue()).getBytes());
        fos.write("\n".getBytes());
    }

    void readData(Properties properties);
}
