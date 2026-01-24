package com.fabbe50.fabsservertweaks.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.fabsservertweaks.FabsServerTweaks;
import com.fabbe50.fabsservertweaks.fabsservertweaks.ModConfig;

public class LogUtil {
    public static void log(String msg) {
        FabsServerTweaks.LOGGER.info(msg);
    }

    public static void debug(String msg) {
        if (ModConfig.debugMode.getValue()) {
            FabsServerTweaks.LOGGER.info("[DEBUG] {}", msg);
        } else {
            FabsServerTweaks.LOGGER.debug(msg);
        }
    }

    public static void warn(String msg) {
        FabsServerTweaks.LOGGER.warn(msg);
    }

    public static void error(String msg) {
        FabsServerTweaks.LOGGER.error(msg);
    }
}
