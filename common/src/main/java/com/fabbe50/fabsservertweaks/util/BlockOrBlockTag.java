package com.fabbe50.fabsservertweaks.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockOrBlockTag {
    private final Block block;
    private final TagKey<Block> blockTag;

    public BlockOrBlockTag(Block block) {
        this.block = block;
        this.blockTag = null;
    }

    public BlockOrBlockTag(TagKey<Block> blockTag) {
        this.blockTag = blockTag;
        this.block = null;
    }

    public Block getBlock() {
        return block;
    }

    public TagKey<Block> getBlockTag() {
        return blockTag;
    }
}
