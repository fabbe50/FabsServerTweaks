package com.fabbe50.fabsservertweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final Logger LOGGER = LogManager.getLogger(Fabsservertweaks.MOD_ID);


    public static void log(String msg) {
        log(LogLevel.INFO, msg);
    }

    public static void debug(String msg) {
        if (Fabsservertweaks.CONFIG.debugMode) {
            log(LogLevel.DEBUG, msg);
        }
    }

    public static void debugEventRun(String msg) {
        if (Fabsservertweaks.CONFIG.debugMode && Fabsservertweaks.CONFIG.debugEventRun) {
            log(LogLevel.DEBUG_EVENT_RUN, msg);
        }
    }

    public static void debugItemStackCreation(String message) {
        debugItemStackCreation(message, false);
    }

    public static void debugItemStackCreation(String message, boolean eventRun) {
        if (Fabsservertweaks.CONFIG.debugItemStackCreation) {
            if (eventRun) {
                LogUtil.debugEventRun(message);
            } else {
                LogUtil.debug(message);
            }
        }
    }

    public static void warn(String msg) {
        log(LogLevel.WARN, msg);
    }

    public static void error(String msg) {
        log(LogLevel.ERROR, msg);
    }

    public static void error(String msg, Throwable throwable) {
        log(LogLevel.ERROR, msg, throwable);
    }

    private static void log(LogLevel level, String msg) {
        log(level, msg, null);
    }

    private static void log(LogLevel level, String msg, Throwable throwable) {
        msg = "[" + level.getSymbol() + "] [" + getCallerClassName() + "] " + msg;
        switch (level) {
            case INFO, DEBUG, DEBUG_EVENT_RUN -> LOGGER.info(msg);
            case WARN -> LOGGER.warn(msg);
            case ERROR -> {
                if (throwable != null) {
                    LOGGER.error(msg, throwable);
                } else {
                    LOGGER.error(msg);
                }
            }
        }
    }

    private enum LogLevel {
        INFO("INFO"),
        WARN("WARN"),
        ERROR("ERROR"),
        DEBUG("DEBUG"),
        DEBUG_EVENT_RUN("DEBUG_EVENT_RUN"),
        ;

        final String symbol;
        LogLevel(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public static String getCallerClassName() {
        try {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for (int i = 1; i < stackTraceElements.length; i++) {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                if (!stackTraceElement.getClassName().equals(LogUtil.class.getName()) && stackTraceElement.getClassName().indexOf("java.lang.Thread") != 0) {
                    String className = stackTraceElement.getClassName();
                    int lastPeriod = className.lastIndexOf(".") + 1;
                    return className.substring(lastPeriod) + "." + stackTraceElement.getMethodName();
                }
            }
            return null;
        } catch (StackOverflowError e) {
            throw new RuntimeException(e);
        }
    }
}
