package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.LogUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.VaultBlock;

import java.util.HashMap;
import java.util.Map;

public class ItemNameRegistry {
    private static final Map<Item, NameEntry> ENTRIES = new HashMap<>();

    private static final ChangeNameSupplier CAMPFIRE_NAME = (name, stack) -> {
        BlockItemStateProperties properties = stack.get(DataComponents.BLOCK_STATE);
        if (properties != null) {
            boolean lit = Boolean.TRUE.equals(properties.get(CampfireBlock.LIT));
            if (!lit) {
                return Component.translatableWithFallback(name.translationKey(), name.fallback(), stack.getItemName().getString());
            }
        }
        return null;
    };
    private static final ChangeNameSupplier VAULT_NAME = (name, stack) -> {
        BlockItemStateProperties properties = stack.get(DataComponents.BLOCK_STATE);
        if (properties != null) {
            boolean ominous = Boolean.TRUE.equals(properties.get(VaultBlock.OMINOUS));
            if (ominous) {
                return Component.translatableWithFallback(name.translationKey(), name.fallback(), stack.getItemName().getString());
            }
        }
        return null;
    };

    static {
        register(Items.CAMPFIRE, new Name("item.unlit", "Unlit %s", "Unlit"), CAMPFIRE_NAME);
        register(Items.SOUL_CAMPFIRE, new Name("item.unlit", "Unlit %s", "Unlit"), CAMPFIRE_NAME);
        register(Items.VAULT, new Name("item.ominous", "Ominous %s", "Ominous"), VAULT_NAME);
    }

    public static void register(Item item, Name name, ChangeNameSupplier changeNameSupplier) {
        ENTRIES.put(item, new NameEntry(name, changeNameSupplier));
    }

    public static boolean setItemName(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        NameEntry entry = ENTRIES.get(stack.getItem());
        if (entry == null) {
            return false;
        }

        Component newName = entry.changeNameSupplier().get(entry.name(), stack);
        if (newName == null) {
            return false;
        }
        boolean oldNameContains = stack.getItemName().getString().contains(entry.name().contains());
        LogUtil.debug("Old Name: " + stack.getItemName().getString());
        LogUtil.debug("New Name: " + newName.getString());
        LogUtil.debug("Old Name Contains Key: " + oldNameContains);
        if (oldNameContains) {
            return false;
        }

        stack.set(DataComponents.ITEM_NAME, newName);
        return true;
    }

    public record NameEntry(Name name, ChangeNameSupplier changeNameSupplier) {}

    public record Name(String translationKey, String fallback, String contains) {}

    public interface ChangeNameSupplier {
        Component get(Name name, ItemStack stack);
    }
}
