package com.fabbe50.fabsservertweaks.neoforge.datagen;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.LoreRegistry.ItemOrTag;
import com.fabbe50.fabsservertweaks.util.BlockOrBlockTag;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TagProviders {
    public static class BlockTags extends BlockTagsProvider {
        private final String packName;
        private final Map<TagKey<Block>, List<BlockOrBlockTag>> tagBlockMap;

        public BlockTags(PackOutput output, String packName, Map<TagKey<Block>, List<BlockOrBlockTag>> tagBlockMap, CompletableFuture<Provider> lookupProvider) {
            super(output, lookupProvider, Fabsservertweaks.MOD_ID);
            this.packName = packName;
            this.tagBlockMap = tagBlockMap;
        }

        @Override
        protected void addTags(@NonNull Provider provider) {
            for (TagKey<Block> tagKey : this.tagBlockMap.keySet()) {
                TagAppender<Block, Block> tagAppender = tag(tagKey).replace(false);
                for (BlockOrBlockTag blockOrBlockTag : this.tagBlockMap.get(tagKey)) {
                    Block block = blockOrBlockTag.getBlock();
                    TagKey<Block> blockTag = blockOrBlockTag.getBlockTag();
                    if (block != null) {
                        tagAppender.addOptional(block);
                    } else if (blockTag != null) {
                        tagAppender.addOptionalTag(blockTag);
                    }
                }
            }
        }

        @Override
        public @NonNull String getName() {
            return Fabsservertweaks.MOD_ID + "_block_tags_" + packName;
        }
    }

    public static class ItemTags extends ItemTagsProvider {
        private final String packName;
        private final Map<TagKey<Item>, List<ItemOrTag<Item>>> tagItemMap;

        public ItemTags(PackOutput output, String packName, Map<TagKey<Item>, List<ItemOrTag<Item>>> tagItemMap, CompletableFuture<Provider> lookupProvider) {
            super(output, lookupProvider, Fabsservertweaks.MOD_ID);
            this.packName = packName;
            this.tagItemMap = tagItemMap;
        }

        @Override
        protected void addTags(@NonNull Provider provider) {
            for (TagKey<Item> tagKey : this.tagItemMap.keySet()) {
                TagAppender<Item, Item> tagAppender = tag(tagKey).replace(false);
                for (ItemOrTag<Item> itemOrTag : this.tagItemMap.get(tagKey)) {
                    Optional<Item> item = itemOrTag.getItem();
                    if (item.isEmpty()) {
                        Optional<TagKey<Item>> itemTag = itemOrTag.getItemTag();
                        itemTag.ifPresent(tagAppender::addOptionalTag);
                        continue;
                    }
                    tagAppender.addOptional(item.get());
                }
            }
        }

        @Override
        public @NonNull String getName() {
            return Fabsservertweaks.MOD_ID + "_item_tags_" + packName;
        }
    }

    public static class EntityTags extends EntityTypeTagsProvider {
        private final String packName;
        private final Map<TagKey<EntityType<?>>, List<EntityType<?>>> tagEntityTypeMap;

        public EntityTags(PackOutput output, String packName, Map<TagKey<EntityType<?>>, List<EntityType<?>>> tagEntityTypeMap, CompletableFuture<Provider> lookupProvider) {
            super(output, lookupProvider, Fabsservertweaks.MOD_ID);
            this.packName = packName;
            this.tagEntityTypeMap = tagEntityTypeMap;
        }

        @Override
        protected void addTags(@NonNull Provider provider) {
            for (TagKey<EntityType<?>> tagKey : this.tagEntityTypeMap.keySet()) {
                TagAppender<EntityType<?>, EntityType<?>> tagAppender = tag(tagKey).replace(false);
                for (EntityType<?> entityType : this.tagEntityTypeMap.get(tagKey)) {
                    tagAppender.addOptional(entityType);
                }
            }
        }

        @Override
        public @NonNull String getName() {
            return Fabsservertweaks.MOD_ID + "_entity_tags_" + packName;
        }
    }

    public static class EnchantmentTags extends EnchantmentTagsProvider {
        private final String packName;
        private final Map<TagKey<Enchantment>, List<ResourceKey<Enchantment>>> tagEnchantmentMap;

        public EnchantmentTags(PackOutput output, String packName, Map<TagKey<Enchantment>, List<ResourceKey<Enchantment>>> tagEnchantmentMap, CompletableFuture<Provider> lookupProvider) {
            super(output, lookupProvider, Fabsservertweaks.MOD_ID);
            this.packName = packName;
            this.tagEnchantmentMap = tagEnchantmentMap;
        }

        @Override
        protected void addTags(@NonNull Provider provider) {
            for (TagKey<Enchantment> tagKey : this.tagEnchantmentMap.keySet()) {
                TagAppender<ResourceKey<Enchantment>, Enchantment> tagAppender = tag(tagKey).replace(false);
                for (ResourceKey<Enchantment> enchantment : this.tagEnchantmentMap.get(tagKey)) {
                    tagAppender.addOptional(enchantment);
                }
            }
        }

        @Override
        public @NonNull String getName() {
            return Fabsservertweaks.MOD_ID + "_enchantment_tags_" + packName;
        }
    }
}
