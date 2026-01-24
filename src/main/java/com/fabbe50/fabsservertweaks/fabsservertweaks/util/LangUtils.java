package com.fabbe50.fabsservertweaks.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.fabsservertweaks.FabsServerTweaks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class LangUtils {
    public static final String MOD_NAME = LangUtils.getTextKey("name");
    public static final String EMPTY = LangUtils.getTextKey("empty");
    public static final String TRUE = LangUtils.getTextKey("true");
    public static final String FALSE = LangUtils.getTextKey("false");
    public static final String CONTAINS = LangUtils.getTextKey("contains");
    public static final String VALUE_BLOCKS = LangUtils.getTextKey("value.blocks");

    public static final Component MOD_NAME_C = Component.translatable(MOD_NAME);
    public static final Component EMPTY_C = Component.translatable(EMPTY);
    public static final Component TRUE_C = Component.translatable(TRUE);
    public static final Component TRUE_CS = Component.translatable(TRUE).withStyle(ChatFormatting.GREEN);
    public static final Component FALSE_C = Component.translatable(FALSE);
    public static final Component FALSE_CS = Component.translatable(FALSE).withStyle(ChatFormatting.RED);

    public static String getTextKey(String simpleKey) {
        return "text." + FabsServerTweaks.MOD_ID + "." + simpleKey;
    }

    public static String getConfigKey(String simpleKey) {
        return "config." + FabsServerTweaks.MOD_ID + "." + simpleKey;
    }

    public static String getConfigTooltipKey(String simpleKey) {
        return getConfigTooltipKey(simpleKey, -1);
    }

    public static String getConfigTooltipKey(String simpleKey, int index) {
        String tooltipBase = getConfigKey(simpleKey) + ".tooltip";
        if (index == -1) {
            return tooltipBase;
        }
        return tooltipBase + "[" + index + "]";
    }

    public static String getBlockKey(BlockState blockState) {
        return getBlockKey(blockState.getBlock());
    }

    public static String getBlockKey(Block block) {
        return block.getDescriptionId();
    }

    public static String getDescriptionKey(Block block) {
        return block.getDescriptionId() + ".desc";
    }

    public static String getDescriptionKey(Item item) {
        return item.getDescriptionId() + ".desc";
    }

    public static String getContainerKey(String simpleKey) {
        return "container." + FabsServerTweaks.MOD_ID + "." + simpleKey;
    }

    public static Component getTextComponent(String simpleKey) {
        return Component.translatable(getTextKey(simpleKey));
    }

    public static Component getTextComponent(String simpleKey, Object value) {
        return Component.translatable(getTextKey(simpleKey), value);
    }

    public static Component getComponent(BlockState blockState) {
        return getComponent(blockState.getBlock());
    }

    public static Component getComponent(Block block) {
        if (Minecraft.getInstance().options.advancedItemTooltips) {
            return Component.literal(block.getDescriptionId());
        } else {
            return Component.translatable(getBlockKey(block));
        }
    }

    public static Component getContainerComponent(String simpleKey) {
        return Component.translatable(getContainerKey(simpleKey));
    }

    public static Component getDescription(Block block) {
        return Component.translatable(getDescriptionKey(block)).withStyle(ChatFormatting.DARK_PURPLE);
    }

    public static List<Component> getDescription(Block block, int lines) {
        List<Component> descLines = new ArrayList<>();
        for (int i = 0; i < lines; i++) {
            descLines.add(Component.translatable(getDescriptionKey(block) + "[" + i + "]").withStyle(ChatFormatting.DARK_PURPLE));
        }
        return descLines;
    }

    public static Component getDescription(Item item) {
        return Component.translatable(getDescriptionKey(item)).withStyle(ChatFormatting.DARK_PURPLE);
    }

    public static Component getConfig(String simpleKey) {
        return Component.translatable(getConfigKey(simpleKey));
    }

    public static Component getConfigTooltip(String simpleKey) {
        return Component.translatable(getConfigTooltipKey(simpleKey));
    }

    public static Component getConfigTooltip(String simpleKey, int index) {
        return Component.translatable(getConfigTooltipKey(simpleKey, index));
    }

    public static Component withPlainLiteralValue(String key, String value) {
        return Component.translatable(key, Component.literal(value));
    }

    public static Component withValue(String key, String valueKey) {
        return Component.translatable(key, Component.translatable(valueKey).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY);
    }

    public static Component withValue(String key, Component value) {
        return Component.translatable(key, value.plainCopy().withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY);
    }

    public static Component condition(boolean condition) {
        return condition ? TRUE_C : FALSE_C;
    }

    public static Component conditionWithStyle(String key, boolean condition) {
        return Component.translatable(key, conditionWithStyle(condition)).withStyle(ChatFormatting.GRAY);
    }

    public static Component conditionWithStyle(boolean condition) {
        return condition ? TRUE_CS : FALSE_CS;
    }

    public static Component error(String key) {
        return Component.translatable(key).withStyle(ChatFormatting.RED);
    }
}
