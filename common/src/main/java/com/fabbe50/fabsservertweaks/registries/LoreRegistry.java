package com.fabbe50.fabsservertweaks.registries;

import com.fabbe50.fabsservertweaks.util.ItemStackUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class LoreRegistry {
    private static final List<Lore<?, ?>> itemLore = new ArrayList<>();

    private static final Function<ItemStack, Boolean> COMPASS_CONDITION = stack -> stack.get(DataComponents.LODESTONE_TRACKER) != null;
    private static final Function<ItemStack, Boolean> LEAD_CONDITION = stack -> stack.getEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(ModRegistry.ENDER));
    private static final Function<ItemStack, String> ENTITY_TYPE_FUNCTION = stack -> {
        TypedEntityData<EntityType<?>> entityData = stack.get(DataComponents.ENTITY_DATA);
        if (entityData == null) {
            return "None";
        }
        return Component.translatable(entityData.type().getDescriptionId()).getString();
    };
    private static final Function<ItemStack, Boolean> FIRE_IMMUNE_CONDITION = stack -> {
        DamageResistant damageResistant = stack.get(DataComponents.DAMAGE_RESISTANT);
        if (damageResistant != null) {
            return damageResistant.types().stream().allMatch(damageTypeHolder -> damageTypeHolder.is(DamageTypeTags.IS_FIRE));
        }
        return false;
    };

    static {
        register(ItemTags.BEDS, "Naming a bed \"Sleeping Bag\", let's you sleep in it without setting your spawn.");
        register(ModRegistry.IMMUNE_TO_CACTUS_DAMAGE, "Immune to cactus damage.");
        register(Items.SUGAR, "Can be used to reduce the min-delay of spawners.");
        register(Items.REDSTONE, "Can be used to reduce the max-delay of spawners.");
        register(Items.GHAST_TEAR, "Can be used to increase the spawn count of spawners.");
        register(Items.GLISTERING_MELON_SLICE, "Can be used to increase the max-nearby-entities of spawners.");
        register(Items.ENDER_EYE, "Can be used to increase the required player range of spawners.");
        register(Items.ECHO_SHARD, "Can be used to disable the required player range of spawners.");
        register(Items.AMETHYST_SHARD, "Can be used to increase the spawn range of spawners.");
        register(Items.QUARTZ, "Can be held in off-hand while using a spawner modifying item to reverse the result.");
        register(Items.LEAD, "If enchanted with Ender, the item will pick up entities inside the item.");
        register(Items.COMPASS, "Bind to a lodestone to be able to teleport to it using ender pearls.", stack -> !COMPASS_CONDITION.apply(stack));
        register(Items.COMPASS, "Right click to teleport to the set lodestone using ender pearls.", COMPASS_CONDITION);
        register(Items.RECOVERY_COMPASS, "Right click to teleport to death point using ender pearls.");
        register(Items.LEAD, "Holding Entity: %s", "Holding Entity: ", ENTITY_TYPE_FUNCTION, LEAD_CONDITION);
        register(ModRegistry.SPIDER_NOT_CLIMBABLE, "Cannot be climbed by spiders.");
        register(ModRegistry.VAULT_KEY, "Can unlock vaults after being locked.");
        register(ModRegistry.FITS_IN_BUNDLE_16, "Fits in a bundle.");
        register(ModRegistry.FITS_IN_BUNDLE_64, "Fits in a bundle.");
        register(null, "Immune to fire damage.", FIRE_IMMUNE_CONDITION);
    }

    private static void register(ItemLike item, String lore) {
        itemLore.add(new Lore<>(new ItemOrTag<>(item), lore));
    }

    private static <T> void register(TagKey<T> tag, String lore) {
        itemLore.add(new Lore<>(new ItemOrTag<>(tag), lore));
    }

    private static void register(ItemLike item, String lore, Function<ItemStack, Boolean> condition) {
        register(item, lore, "", condition);
    }

    private static void register(ItemLike item, String lore, String removalKey, Function<ItemStack, Boolean> condition) {
        itemLore.add(new ConditionalLore<>(new ItemOrTag<>(item), lore, removalKey, condition));
    }

    private static <T> void register(ItemLike item, String lore, String removalKey, Function<ItemStack, T> extraDataGetter, Function<ItemStack, Boolean> condition) {
        itemLore.add(new ConditionalLore<>(new ItemOrTag<>(item), lore, removalKey, extraDataGetter, condition));
    }

    public static boolean updateLore(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        List<Lore<?, ?>> loreList = new ArrayList<>();
        for (Lore<?, ?> lore : itemLore) {
            if (lore.getItem() == null) {
                loreList.add(lore);
            } else {
                if (lore.getItem().matches(stack)) {
                    loreList.add(lore);
                }
            }
        }
        if (loreList.isEmpty()) {
            return false;
        }
        for (Lore<?, ?> lore : loreList) {
            if (lore instanceof ConditionalLore<?, ?> conditionalLore) {
                if (!conditionalLore.getRemovalKey().isEmpty()) {
                    ItemStackUtil.removeLoreFuzzy(stack, conditionalLore.getRemovalKey());
                }
                if (conditionalLore.shouldApply(stack)) {
                    ItemStackUtil.addLore(stack, conditionalLore.getLore(stack));
                } else {
                    ItemStackUtil.removeLore(stack, conditionalLore.getLore(stack));
                }
            } else {
                ItemStackUtil.addLore(stack, lore.getLore());
            }
        }
        return true;
    }

    public static String getLore(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return "";
        }
        for (Lore<?, ?> lore : itemLore) {
            if (lore instanceof ConditionalLore<?, ?> conditionalLore) {
                if (conditionalLore.shouldApply(stack)) {
                    return conditionalLore.getLore();
                }
            } else if (lore.getItem() != null) {
                if (lore.getItem().matches(stack)) {
                    return lore.getLore();
                }
            }
        }
        return "";
    }

    public static class ConditionalLore<I, T> extends Lore<I, T> {
        private final Function<ItemStack, Boolean> condition;
        private final String removalKey;

        public ConditionalLore(ItemOrTag<I> item, String lore, String removalKey, Function<ItemStack, Boolean> condition) {
            super(item, lore);
            this.condition = condition;
            this.removalKey = removalKey;
        }

        public ConditionalLore(ItemOrTag<I> item, String lore, String removalKey, String extraData, Function<ItemStack, Boolean> condition) {
            super(item, lore, extraData);
            this.condition = condition;
            this.removalKey = removalKey;
        }

        public ConditionalLore(ItemOrTag<I> item, String lore, String removalKey, Function<ItemStack, T> extraDataGetter, Function<ItemStack, Boolean> condition) {
            super(item, lore, extraDataGetter);
            this.condition = condition;
            this.removalKey = removalKey;
        }

        public boolean shouldApply(ItemStack stack) {
            return condition.apply(stack);
        }

        public String getRemovalKey() {
            return removalKey;
        }
    }

    public static class Lore<I, T> {
        private final ItemOrTag<I> item;
        private final String lore;
        private final Function<ItemStack, T> extraDataGetter;
        private final String extraData;

        public Lore(ItemOrTag<I> item, String lore) {
            this.item = item;
            this.lore = lore;
            this.extraDataGetter = null;
            this.extraData = "";
        }

        public Lore(ItemOrTag<I> item, String lore, String extraData) {
            this.item = item;
            this.lore = lore;
            this.extraDataGetter = null;
            this.extraData = extraData;
        }

        public Lore(ItemOrTag<I> item, String lore, Function<ItemStack, T> extraDataGetter) {
            this.item = item;
            this.lore = lore;
            this.extraDataGetter = extraDataGetter;
            this.extraData = "";
        }

        public ItemOrTag<I> getItem() {
            return item;
        }

        public String getLore() {
            return getLore(null);
        }

        public String getLore(ItemStack stack) {
            if (!extraData.isEmpty() && extraDataGetter == null) {
                return String.format(lore, extraData);
            } else if (!extraData.isEmpty() && stack != null) {
                return String.format(lore, String.format(extraData, extraDataGetter.apply(stack)));
            } else if (extraDataGetter != null && stack != null) {
                return String.format(lore, extraDataGetter.apply(stack));
            }
            return lore;
        }
    }

    public static class ItemOrTag<T> {
        private final ItemLike item;
        private final TagKey<T> tag;

        public ItemOrTag(ItemLike item) {
            this.item = item;
            this.tag = null;
        }

        public ItemOrTag(TagKey<T> tag) {
            this.item = null;
            this.tag = tag;
        }

        public static ItemOrTag<Block> of(Block block) {
            return new ItemOrTag<>(block);
        }

        public static ItemOrTag<Item> of(Item item) {
            return new ItemOrTag<>(item);
        }

        public static ItemOrTag<Item> of(TagKey<Item> tag) {
            return new ItemOrTag<>(tag);
        }

        public Optional<Item> getItem() {
            if (item != null) {
                return Optional.of(item.asItem());
            }
            return Optional.empty();
        }

        public Optional<TagKey<Item>> getItemTag() {
            if (tag != null) {
                return tag.cast(Registries.ITEM);
            }
            return Optional.empty();
        }

        private Optional<TagKey<Block>> getBlockTag() {
            if (tag != null) {
                return tag.cast(Registries.BLOCK);
            }
            return Optional.empty();
        }

        public boolean matches(ItemStack stack) {
            if (item != null) {
                return stack.is(item.asItem());
            } else if (tag != null) {
                Optional<TagKey<Item>> itemTag = getItemTag();
                Optional<TagKey<Block>> blockTag = getBlockTag();
                if (itemTag.isPresent()) {
                    return stack.is(itemTag.get());
                } else if (blockTag.isPresent()) {
                    if (stack.getItem() instanceof BlockItem blockItem) {
                        return blockItem.getBlock().defaultBlockState().is(blockTag.get());
                    }
                }
            }
            return false;
        }
    }
}
