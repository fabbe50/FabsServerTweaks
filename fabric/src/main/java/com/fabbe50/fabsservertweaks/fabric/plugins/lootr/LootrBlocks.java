package com.fabbe50.fabsservertweaks.fabric.plugins.lootr;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.fabric.plugins.util.Directional;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.fabric.init.ModBlocks;

public enum LootrBlocks {
    CHEST("chest", ModBlocks.CHEST, Blocks.CHEST, Directional.HORIZONTAL, true, false),
    TRAPPED_CHEST("trapped_chest", ModBlocks.TRAPPED_CHEST, Blocks.TRAPPED_CHEST, Directional.HORIZONTAL, true, false),
    BARREL("barrel_unopened", ModBlocks.BARREL, Blocks.BARREL, Directional.ALL, false, false),
    OPENED_BARREL("opened_barrel", ModBlocks.BARREL, Blocks.BARREL, Directional.ALL, false, false),
    SHULKER("shulker", ModBlocks.SHULKER_BOX, Blocks.SHULKER_BOX, Directional.ALL, true, false),
    DECORATED_POT("decorated_pot", ModBlocks.DECORATED_POT, Blocks.DECORATED_POT, Directional.HORIZONTAL, false, false),
    SUSPICIOUS_SAND("suspicious_sand", ModBlocks.SUSPICIOUS_SAND, Blocks.SUSPICIOUS_SAND, Directional.NO, false, false),
    SUSPICIOUS_GRAVEL("suspicious_gravel", ModBlocks.SUSPICIOUS_GRAVEL, Blocks.SUSPICIOUS_GRAVEL, Directional.NO, false, false),
    TROPHY("trophy", ModBlocks.TROPHY, Blocks.PLAYER_HEAD, Directional.HORIZONTAL, false, true),
    ;

    private final String name;
    private final Block originalBlock;
    private final Block polymerBlock;
    private final Directional directional;
    private final boolean special;
    private final boolean simple;
    LootrBlocks(String name, Block originalBlock, Block polymerBlock, Directional directional, boolean special, boolean simple) {
        this.name = name;
        this.originalBlock = originalBlock;
        this.polymerBlock = polymerBlock;
        this.directional = directional;
        this.special = special;
        this.simple = simple;
    }

    public String getModId() {
        return (special ? "lootr_plugin" : "lootr");
    }

    public String getName() {
        return (special ? "lootr_" + this.name : this.name);
    }

    public String getSuffix() {
        return (special ? "_opened" : "_open");
    }

    public Identifier getIdentifier(boolean opened) {
        if (opened) {
            return Fabsservertweaks.location(getName() + getSuffix());
        }
        return Fabsservertweaks.location(getName());
    }

    public Identifier getOpenedIdentifier() {
        return getIdentifier(true);
    }

    public Identifier getClosedIdentifier() {
        return getIdentifier(false);
    }

    public static LootrBlocks fromBlockState(BlockState state) {
        for (LootrBlocks type : values()) {
            if (type.originalBlock.equals(state.getBlock()) || type.polymerBlock.equals(state.getBlock())) {
                return type;
            }
            Block block = LootrPlugin.getOpenedBlock(type);
            if (block != null && block.equals(state.getBlock())) {
                return type;
            }
        }
        return null;
    }

    public Block getOriginalBlock() {
        return originalBlock;
    }

    public Block getPolymerBlock() {
        return polymerBlock;
    }

    public Directional getDirectional() {
        return directional;
    }

    public boolean isSpecial() {
        return special;
    }

    public boolean isSimple() {
        return simple;
    }
}
