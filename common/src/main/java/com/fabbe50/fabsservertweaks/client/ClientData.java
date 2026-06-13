package com.fabbe50.fabsservertweaks.client;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.client.debug.DebugSlimeChunk;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;

public class ClientData {
    private static long currentSeed = 0L;

    public static void init() {
        ClientTooltipEvent.ITEM.register((stack, lines, tooltipContext, flag) -> {
            if (stack.has(DataComponents.TOOL) || stack.has(DataComponents.WEAPON)) {
                double baseDamage = 1.0;
                baseDamage += stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
                        .compute(Attributes.ATTACK_DAMAGE, 0, EquipmentSlot.MAINHAND);

                int sharpness = EnchantmentHelper.getItemEnchantmentLevel(tooltipContext.registries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS), stack);
                double totalDamage = Math.round((baseDamage + (sharpness > 0 ? 1.0 + (1.2222 * (sharpness - 1)) : 0)) * 10) / 10d;
                List<Component> newComponents = new ArrayList<>();
                lines.forEach(component -> {
                    if (component.getString().contains("Attack Damage")) {
                        newComponents.add(Component.literal(totalDamage - ((int)totalDamage) == 0 ? String.format(" %.0f Attack Damage", totalDamage) : String.format(" %.1f Attack Damage", totalDamage)).withStyle(ChatFormatting.DARK_GREEN));
                    } else {
                        newComponents.add(component);
                    }
                });
                lines.clear();
                lines.addAll(newComponents);
            }
        });
    }

    public static void setCurrentSeed(long seed) {
        currentSeed = seed;
    }

    public static long getCurrentSeed() {
        return currentSeed;
    }

    public static final Identifier SLIME_CHUNK = DebugScreenEntries.register(Fabsservertweaks.location("slime_chunk"), new DebugSlimeChunk());
}
