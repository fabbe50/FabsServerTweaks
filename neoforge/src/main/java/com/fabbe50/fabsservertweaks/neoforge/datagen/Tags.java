package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.LoreRegistry.ItemOrTag;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.BlockOrBlockTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tags {
    public static final Map<TagKey<Item>, List<ItemOrTag<Item>>> INFINITY_BUCKETS_ITEM_TAGS = Map.of(
            ModRegistry.INFINITY_COMPATIBLE, List.of(ItemOrTag.of(Items.BUCKET), ItemOrTag.of(Items.WATER_BUCKET))
    );

    public static final Map<TagKey<Block>, List<BlockOrBlockTag>> VAULT_BREAKING_BLOCK_TAGS = Map.of(
            BlockTags.MINEABLE_WITH_PICKAXE, List.of(BlockOrBlockTag.of(Blocks.VAULT), BlockOrBlockTag.of(Blocks.TRIAL_SPAWNER))
    );

    public static final Map<TagKey<Block>, List<BlockOrBlockTag>> BASE_MOD_BLOCK_TAGS;
    public static final Map<TagKey<EntityType<?>>, List<EntityType<?>>> BASE_MOD_ENTITY_TAGS;
    public static final Map<TagKey<Item>, List<ItemOrTag<Item>>> BASE_MOD_ITEM_TAGS;


    static {
        BASE_MOD_BLOCK_TAGS = new HashMap<>();
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.MOD_BONE_MEALABLE, List.of(
                BlockOrBlockTag.of(BlockTags.CORALS),
                BlockOrBlockTag.of(BlockTags.WALL_CORALS),
                BlockOrBlockTag.of(Blocks.SPORE_BLOSSOM),
                BlockOrBlockTag.of(Blocks.SUGAR_CANE),
                BlockOrBlockTag.of(Blocks.CACTUS),
                BlockOrBlockTag.of(Blocks.LILY_PAD),
                BlockOrBlockTag.of(Blocks.SEA_PICKLE),
                BlockOrBlockTag.of(Blocks.PUMPKIN_STEM),
                BlockOrBlockTag.of(Blocks.MELON_STEM),
                BlockOrBlockTag.of(Blocks.POPPY),
                BlockOrBlockTag.of(Blocks.BLUE_ORCHID),
                BlockOrBlockTag.of(Blocks.ALLIUM),
                BlockOrBlockTag.of(Blocks.AZURE_BLUET),
                BlockOrBlockTag.of(Blocks.RED_TULIP),
                BlockOrBlockTag.of(Blocks.ORANGE_TULIP),
                BlockOrBlockTag.of(Blocks.WHITE_TULIP),
                BlockOrBlockTag.of(Blocks.PINK_TULIP),
                BlockOrBlockTag.of(Blocks.OXEYE_DAISY),
                BlockOrBlockTag.of(Blocks.CORNFLOWER),
                BlockOrBlockTag.of(Blocks.LILY_OF_THE_VALLEY),
                BlockOrBlockTag.of(Blocks.TALL_GRASS),
                BlockOrBlockTag.of(Blocks.LARGE_FERN),
                BlockOrBlockTag.of(Blocks.NETHER_WART)
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.FIELD_GROWABLE, List.of(
                BlockOrBlockTag.of(BlockTags.CROPS),
                BlockOrBlockTag.of(Blocks.SWEET_BERRY_BUSH),
                BlockOrBlockTag.of(Blocks.SUGAR_CANE),
                BlockOrBlockTag.of(Blocks.CACTUS),
                BlockOrBlockTag.of(Blocks.NETHER_WART),
                BlockOrBlockTag.of(Blocks.TORCHFLOWER),
                BlockOrBlockTag.of(Blocks.PITCHER_CROP)
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.HARVESTABLE, List.of(
                BlockOrBlockTag.of(ModRegistry.FIELD_GROWABLE),
                BlockOrBlockTag.of(Blocks.COCOA)
        ));
        BASE_MOD_BLOCK_TAGS.put(BlockTags.MINEABLE_WITH_PICKAXE, List.of(
                BlockOrBlockTag.of(Blocks.GLOWSTONE),
                BlockOrBlockTag.of(TagKey.create(Registries.BLOCK, Fabsservertweaks.location("mineable/pickaxe")))
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.ORE_MINER_WHITELIST, List.of(
                BlockOrBlockTag.of(cBlockTag("ores")),
                BlockOrBlockTag.of(cBlockTag("clusters"))
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.PISTON_BREAKER_RODS, List.of(
                BlockOrBlockTag.of(Blocks.END_ROD),
                BlockOrBlockTag.of(BlockTags.LIGHTNING_RODS)
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.PISTON_MOVE_OVERRIDE, List.of(BlockOrBlockTag.of(Blocks.DECORATED_POT)));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.PISTON_PUSH_BLACKLIST, List.of(BlockOrBlockTag.of(Blocks.PISTON_HEAD)));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.PISTON_PUSH_WHITELIST, List.of());
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.SCYTHE_ABLE, List.of(
                BlockOrBlockTag.of(BlockTags.FLOWERS),
                BlockOrBlockTag.of(Blocks.SHORT_GRASS),
                BlockOrBlockTag.of(Blocks.TALL_GRASS),
                BlockOrBlockTag.of(Blocks.SEAGRASS),
                BlockOrBlockTag.of(Blocks.TALL_SEAGRASS),
                BlockOrBlockTag.of(Blocks.SHORT_DRY_GRASS),
                BlockOrBlockTag.of(Blocks.TALL_DRY_GRASS)
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.SPIDER_NOT_CLIMBABLE, List.of(
                BlockOrBlockTag.of(BlockTags.ICE),
                BlockOrBlockTag.of(Blocks.WHITE_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.ORANGE_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.MAGENTA_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.YELLOW_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.LIME_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.PINK_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.GRAY_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.CYAN_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.PURPLE_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.BLUE_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.BROWN_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.GREEN_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.RED_GLAZED_TERRACOTTA),
                BlockOrBlockTag.of(Blocks.BLACK_GLAZED_TERRACOTTA)
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.TREE_CHOPPER_ATTACHMENTS, List.of(
                BlockOrBlockTag.of(BlockTags.LEAVES),
                BlockOrBlockTag.of(Blocks.RED_MUSHROOM_BLOCK),
                BlockOrBlockTag.of(Blocks.BROWN_MUSHROOM_BLOCK),
                BlockOrBlockTag.of(Blocks.NETHER_WART_BLOCK),
                BlockOrBlockTag.of(Blocks.WARPED_WART_BLOCK),
                BlockOrBlockTag.of(Blocks.SHROOMLIGHT)
        ));
        BASE_MOD_BLOCK_TAGS.put(ModRegistry.TREE_CHOPPER_WHITELIST, List.of(
                BlockOrBlockTag.of(BlockTags.LOGS),
                BlockOrBlockTag.of(Blocks.MUSHROOM_STEM),
                BlockOrBlockTag.of(Blocks.MANGROVE_ROOTS)
        ));

        BASE_MOD_ENTITY_TAGS = new HashMap<>();
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.ANIMALS, List.of(
                EntityType.FOX,
                EntityType.CAT,
                EntityType.PARROT,
                EntityType.WOLF,
                EntityType.OCELOT,
                EntityType.RABBIT,
                EntityType.HORSE,
                EntityType.DONKEY,
                EntityType.MULE,
                EntityType.LLAMA,
                EntityType.MOOSHROOM,
                EntityType.TRADER_LLAMA,
                EntityType.STRIDER,
                EntityType.PIG,
                EntityType.SHEEP,
                EntityType.CHICKEN,
                EntityType.COW,
                EntityType.TURTLE,
                EntityType.ARMADILLO,
                EntityType.CAMEL,
                EntityType.BEE,
                EntityType.FROG,
                EntityType.BAT,
                EntityType.POLAR_BEAR,
                EntityType.SNIFFER,
                EntityType.SQUID,
                EntityType.GLOW_SQUID,
                EntityType.NAUTILUS,
                EntityType.PANDA,
                EntityType.DOLPHIN,
                EntityType.AXOLOTL,
                EntityType.ALLAY
        ));
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.BOSSES, List.of(
                EntityType.ENDER_DRAGON,
                EntityType.WITHER,
                EntityType.WARDEN,
                EntityType.ELDER_GUARDIAN
        ));
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.GOLEMS, List.of(
                EntityType.IRON_GOLEM,
                EntityType.SNOW_GOLEM,
                EntityType.COPPER_GOLEM
        ));
        /*BASE_MOD_ENTITY_TAGS.put(ModRegistry.HOSTILES, List.of(

        ));*/
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.LEAD_BLACKLIST, List.of(EntityType.CREAKING));
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.MOBS_WITH_POTION_EFFECTS_BLACKLIST, List.of(EntityType.WITHER, EntityType.ENDER_DRAGON, EntityType.WARDEN));
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.PETS, List.of(
                EntityType.CAT,
                EntityType.WOLF,
                EntityType.PARROT,
                EntityType.HORSE,
                EntityType.DONKEY,
                EntityType.MULE,
                EntityType.LLAMA
        ));
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.VILLAGER_TYPES, List.of(
                EntityType.VILLAGER,
                EntityType.WANDERING_TRADER
        ));
        BASE_MOD_ENTITY_TAGS.put(ModRegistry.RUNNING_FROM_CREEPER, List.of(
                EntityType.VILLAGER,
                EntityType.WANDERING_TRADER,
                EntityType.COPPER_GOLEM,
                EntityType.CAT,
                EntityType.SKELETON,
                EntityType.WITHER_SKELETON,
                EntityType.HORSE,
                EntityType.WOLF,
                EntityType.FOX,
                EntityType.LLAMA,
                EntityType.COW,
                EntityType.PIG,
                EntityType.CREEPER,
                EntityType.IRON_GOLEM,
                EntityType.MOOSHROOM,
                EntityType.TRADER_LLAMA,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.PARCHED,
                EntityType.STRAY,
                EntityType.WITCH
        ));

        BASE_MOD_ITEM_TAGS = new HashMap<>();
        BASE_MOD_ITEM_TAGS.put(ModRegistry.CHEST_PIECES, List.of(
                ItemOrTag.of(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                ItemOrTag.of(Items.ELYTRA)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.SOULBOUND_COMPATIBLE, List.of(
                ItemOrTag.of(ItemTags.DURABILITY_ENCHANTABLE),
                ItemOrTag.of(ItemTags.BUNDLES)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.SWIFTNESS_COMPATIBLE, List.of(
                ItemOrTag.of(ItemTags.LEG_ARMOR_ENCHANTABLE),
                ItemOrTag.of(ItemTags.HARNESSES)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.BLOCK_DISPENSE_BLACKLIST, List.of());
        BASE_MOD_ITEM_TAGS.put(ModRegistry.FITS_IN_BUNDLE_16, List.of(
                ItemOrTag.of(cItemTag("tools")),
                ItemOrTag.of(cItemTag("music_discs")),
                ItemOrTag.of(Items.TOTEM_OF_UNDYING),
                ItemOrTag.of(Items.LAVA_BUCKET),
                ItemOrTag.of(Items.WATER_BUCKET),
                ItemOrTag.of(ItemTags.SHULKER_BOXES),
                ItemOrTag.of(Items.SHIELD),
                ItemOrTag.of(Items.ELYTRA),
                ItemOrTag.of(ItemTags.HARNESSES),
                ItemOrTag.of(Items.CARROT_ON_A_STICK),
                ItemOrTag.of(Items.WARPED_FUNGUS_ON_A_STICK),
                ItemOrTag.of(ItemTags.BOATS),
                ItemOrTag.of(Items.GOAT_HORN),
                ItemOrTag.of(Items.SPYGLASS)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.FITS_IN_BUNDLE_64, List.of());
        BASE_MOD_ITEM_TAGS.put(ModRegistry.IMMUNE_TO_CACTUS_DAMAGE, List.of(
                ItemOrTag.of(Items.CACTUS),
                ItemOrTag.of(Items.CACTUS_FLOWER),
                ItemOrTag.of(cItemTag("netherite_items"))
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.MUSHROOM_HEAD_BLOCKS, List.of(
                ItemOrTag.of(Items.RED_MUSHROOM_BLOCK),
                ItemOrTag.of(Items.BROWN_MUSHROOM_BLOCK)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.PREVENTS_HOT_FLOOR_DAMAGE, List.of(
                ItemOrTag.of(Items.NETHERITE_BOOTS)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.STACK_4, List.of(
                ItemOrTag.of(Items.LAVA_BUCKET),
                ItemOrTag.of(Items.WATER_BUCKET)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.STACK_8, List.of());
        BASE_MOD_ITEM_TAGS.put(ModRegistry.STACK_16, List.of(
                ItemOrTag.of(ItemTags.BEDS),
                ItemOrTag.of(ItemTags.BOATS),
                ItemOrTag.of(Items.ENCHANTED_BOOK),
                ItemOrTag.of(Items.POTION),
                ItemOrTag.of(Items.SPLASH_POTION),
                ItemOrTag.of(Items.LINGERING_POTION),
                ItemOrTag.of(Items.SADDLE),
                ItemOrTag.of(Items.COPPER_HORSE_ARMOR),
                ItemOrTag.of(Items.IRON_HORSE_ARMOR),
                ItemOrTag.of(Items.GOLDEN_HORSE_ARMOR),
                ItemOrTag.of(Items.DIAMOND_HORSE_ARMOR),
                ItemOrTag.of(Items.NETHERITE_HORSE_ARMOR),
                ItemOrTag.of(Items.MUSHROOM_STEW),
                ItemOrTag.of(Items.RABBIT_STEW),
                ItemOrTag.of(Items.SUSPICIOUS_STEW),
                ItemOrTag.of(Items.BEETROOT_SOUP),
                ItemOrTag.of(Items.CAKE)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.STACK_64, List.of(
                ItemOrTag.of(ItemTags.SIGNS),
                ItemOrTag.of(ItemTags.EGGS),
                ItemOrTag.of(Items.ENDER_PEARL)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.VAULT_KEY, List.of(
                ItemOrTag.of(Items.TRIAL_KEY)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.BRAIN_CORALS, List.of(
                ItemOrTag.of(Items.BRAIN_CORAL),
                ItemOrTag.of(Items.BRAIN_CORAL_FAN)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.BUBBLE_CORALS, List.of(
                ItemOrTag.of(Items.BUBBLE_CORAL),
                ItemOrTag.of(Items.BUBBLE_CORAL_FAN)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.FIRE_CORALS, List.of(
                ItemOrTag.of(Items.FIRE_CORAL),
                ItemOrTag.of(Items.FIRE_CORAL_FAN)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.HORN_CORALS, List.of(
                ItemOrTag.of(Items.HORN_CORAL),
                ItemOrTag.of(Items.HORN_CORAL_FAN)
        ));
        BASE_MOD_ITEM_TAGS.put(ModRegistry.TUBE_CORALS, List.of(
                ItemOrTag.of(Items.TUBE_CORAL),
                ItemOrTag.of(Items.TUBE_CORAL_FAN)
        ));
        BASE_MOD_ITEM_TAGS.put(cItemTag("netherite_items"), List.of(
                ItemOrTag.of(Items.NETHERITE_INGOT),
                ItemOrTag.of(Items.NETHERITE_BLOCK),
                ItemOrTag.of(Items.NETHERITE_HELMET),
                ItemOrTag.of(Items.NETHERITE_CHESTPLATE),
                ItemOrTag.of(Items.NETHERITE_LEGGINGS),
                ItemOrTag.of(Items.NETHERITE_BOOTS),
                ItemOrTag.of(Items.NETHERITE_SWORD),
                ItemOrTag.of(Items.NETHERITE_PICKAXE),
                ItemOrTag.of(Items.NETHERITE_SHOVEL),
                ItemOrTag.of(Items.NETHERITE_AXE),
                ItemOrTag.of(Items.NETHERITE_HOE),
                ItemOrTag.of(Items.NETHERITE_SPEAR),
                ItemOrTag.of(Items.NETHERITE_SCRAP),
                ItemOrTag.of(Items.NETHERITE_HORSE_ARMOR),
                ItemOrTag.of(Items.NETHERITE_NAUTILUS_ARMOR),
                ItemOrTag.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        ));
    }

    private static TagKey<Block> cBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, Fabsservertweaks.location("c", path));
    }

    private static TagKey<Item> cItemTag(String path) {
        return TagKey.create(Registries.ITEM, Fabsservertweaks.location("c", path));
    }
}
