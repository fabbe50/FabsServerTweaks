package com.fabbe50.fabsservertweaks.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobLoot {
    public static final Map<EntityType<?>, List<Loot>> MOB_LOOT = new HashMap<>();

    public static void init() {
        registerLoot(EntityType.CHICKEN, new Loot(Items.EGG, 1, 0.2));
        registerLoot(EntityType.DROWNED, new Loot(Items.PRISMARINE_SHARD, 3, 0.2));
        registerLoot(EntityType.ENDER_DRAGON, new Loot(Items.ELYTRA, 1, 1));
        registerLoot(EntityType.ENDERMITE, new Loot(Items.ENDER_PEARL, 1, 0.01));
        registerLoot(EntityType.HUSK, new Loot(Items.SAND, 1, 1), new Loot(Items.SAND, 2, 0.5));
        registerLoot(EntityType.PIGLIN, new Loot(Items.PORKCHOP, 3, 0.2), new Loot(Items.GOLD_NUGGET, 4, 0.1), new Loot(Items.GOLD_INGOT, 1, 0.005));
        registerLoot(EntityType.PIGLIN_BRUTE, new Loot(Items.PORKCHOP, 5, 0.2), new Loot(Items.GOLDEN_APPLE, 1, 0.01), new Loot(Items.GOLD_BLOCK, 1, 0.005));
        registerLoot(EntityType.SILVERFISH, new Loot(Items.IRON_NUGGET, 2, 0.7));
        registerLoot(EntityType.SNIFFER, new Loot(Items.SNIFFER_EGG, 1, 0.1));
        registerLoot(EntityType.VILLAGER, new Loot(Items.EMERALD, 1, 0.5), new Loot(Items.EMERALD, 1, 0.01), new Loot(Items.EMERALD_BLOCK, 1, 0.005));
        registerLoot(EntityType.WANDERING_TRADER, new Loot(Items.EMERALD, 2, 0.5), new Loot(Items.EMERALD, 2, 0.01), new Loot(Items.EMERALD_BLOCK, 1, 0.005));
        registerLoot(EntityType.WARDEN, new Loot(Items.DISC_FRAGMENT_5, 1, 1));
    }

    public static void registerLoot(EntityType<?> entityType, Loot... loot) {
        MOB_LOOT.put(entityType, List.of(loot));
    }

    public static void registerLoot(EntityType<?> entityType, List<Loot> loot) {
        MOB_LOOT.put(entityType, loot);
    }

    public static List<Loot> getLoot(EntityType<?> entityType) {
        return MOB_LOOT.get(entityType);
    }

    public static List<ItemStack> getLootItems(ServerLevel level, EntityType<?> entityType) {
        List<ItemStack> items = new ArrayList<>();
        for (Loot loot : getLoot(entityType)) {
            for (int i = 0; i < loot.count(); i++) {
                if (level.getRandom().nextDouble() <= loot.chance()) {
                    items.add(new ItemStack(loot.item()));
                }
                if (level.getRandom().nextDouble() <= 0.05) {
                    return items;
                }
            }
        }
        return items;
    }

    public record Loot(Item item, int count, double chance) {
    }
}
