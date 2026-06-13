package com.fabbe50.fabsservertweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final Logger LOGGER = LogManager.getLogger(Fabsservertweaks.MOD_ID);


    public static void log(String msg) {
        LOGGER.info(msg);
    }

    public static void debug(String msg) {
        LOGGER.debug(msg);
    }

    public static void warn(String msg) {
        LOGGER.warn(msg);
    }

    public static void error(String msg) {
        LOGGER.error(msg);
    }

    public static void error(String msg, Throwable throwable) {
        LOGGER.error(msg, throwable);
    }
}
