package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.util.PlantUtil;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.List;

public enum PlantGrowth {
    WHEAT(Blocks.WHEAT, 0, false, false),
    POTATOES(Blocks.POTATOES, 0, false, false),
    CARROTS(Blocks.CARROTS, 0, false, false),
    BEETROOTS(Blocks.BEETROOTS, 0, false, false),
    MELON(Blocks.MELON_STEM, 0, false, false, false) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            if (PlantUtil.growFromStem(level, pos, state, level.getRandom(), Blocks.MELON, Blocks.MELON_STEM)) {
                PlantUtil.handleBoneMealUsed(level, pos, player, stack, true);
                return true;
            }
            return false;
        }
    },
    PUMPKIN(Blocks.PUMPKIN_STEM, 0, false, false, false) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            if (PlantUtil.growFromStem(level, pos, state, level.getRandom(), Blocks.PUMPKIN, Blocks.PUMPKIN_STEM)) {
                PlantUtil.handleBoneMealUsed(level, pos, player, stack, true);
                return true;
            }
            return false;
        }
    },
    NETHER_WARTS(Blocks.NETHER_WART, 0, false, false) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            IntegerProperty property = PlantUtil.getAgeProperty(state);
            if (property != null) {
                if (PlantUtil.growIfPossible(level, pos, state)) {
                    PlantUtil.handleBoneMealUsed(level, pos, player, stack, true);
                    return true;
                }
            }
            return false;
        }
    },
    COCOA_BEANS(Blocks.COCOA, 0, false, false),
    SUGAR_CANE(Blocks.SUGAR_CANE, 0, true, true) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            int maxHeight = ModGameRules.getGameRuleInteger(level, ModGameRules.RULE_SUGAR_CANE_GROW_HEIGHT);
            if (PlantUtil.growInColumn(level, pos, state.getBlock(), maxHeight)) {
                PlantUtil.handleBoneMealUsed(level, pos, player, stack, false);
                return true;
            }
            return false;
        }
    },
    CACTUS(Blocks.CACTUS, 0, true, true) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            int maxHeight = ModGameRules.getGameRuleInteger(level, ModGameRules.RULE_CACTUS_GROW_HEIGHT);
            if (PlantUtil.growInColumn(level, pos, state.getBlock(), maxHeight)) {
                PlantUtil.handleBoneMealUsed(level, pos, player, stack, false);
                return true;
            }
            return false;
        }

        @Override
        public List<Block> getAttachments() {
            return List.of(Blocks.CACTUS_FLOWER);
        }
    },
    BAMBOO(Blocks.BAMBOO, 0, true, true),
    TORCH_FLOWER(Blocks.TORCHFLOWER, 0, false, false, Blocks.TORCHFLOWER_CROP) {
        @Override
        public boolean isAgingPlant() {
            return false;
        }
    },
    PITCHER_PLANT(Blocks.PITCHER_CROP, 0, false, true, Blocks.PITCHER_CROP) {
        @Override
        public boolean isAgingPlant() {
            return false;
        }

        @Override
        public boolean tallPlantDropsForEverySegment() {
            return false;
        }
    },
    BERRY_BUSH(Blocks.SWEET_BERRY_BUSH, 1, false, false),
    LILY_PAD(Blocks.LILY_PAD, 0, false, false) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            List<BlockPos> blockPositions = WorldUtil.getBlockPositions(new AABB(pos).inflate(1));
            for (BlockPos blockPos : blockPositions) {
                if (level.getRandom().nextInt(3) == 0) {
                    BlockState checkState = level.getBlockState(blockPos);
                    if (checkState.isAir() && (level.getBlockState(blockPos.below()).is(Blocks.WATER) && level.getFluidState(blockPos.below()).is(Fluids.WATER))) {
                        level.setBlockAndUpdate(blockPos, state);
                        PlantUtil.handleBoneMealUsed(level, pos, player, stack, true);
                        return true;
                    }
                }
            }
            return false;
        }
    },
    SEA_PICKLE(Blocks.SEA_PICKLE, 0, false, false) {
        @Override
        public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
            int pickles = state.getValue(SeaPickleBlock.PICKLES);
            if (state.getValue(SeaPickleBlock.WATERLOGGED) && pickles < 4) {
                level.setBlockAndUpdate(pos, state.setValue(SeaPickleBlock.PICKLES, pickles + 1));
                PlantUtil.handleBoneMealUsed(level, pos.above(), player, stack, true);
                return true;
            }
            return false;
        }
    },
    ;

    private final Block block;
    private final int resettingAge;
    private final boolean shouldHarvest;
    private final boolean ignoreMaxAge;
    private final boolean tallPlant;
    private final Block differentBlock;
    PlantGrowth(Block block, int resettingAge, boolean ignoreMaxAge, boolean tallPlant) {
        this(block, resettingAge, true, ignoreMaxAge, tallPlant, null);
    }

    PlantGrowth(Block block, int resettingAge, boolean shouldHarvest, boolean ignoreMaxAge, boolean tallPlant) {
        this(block, resettingAge, shouldHarvest, ignoreMaxAge, tallPlant, null);
    }

    PlantGrowth(Block block, int resettingAge, boolean ignoreMaxAge, boolean tallPlant, Block differentBlock) {
        this(block, resettingAge, true, ignoreMaxAge, tallPlant, differentBlock);
    }

    PlantGrowth(Block block, int resettingAge, boolean shouldHarvest, boolean ignoreMaxAge, boolean tallPlant, Block differentBlock) {
        this.block = block;
        this.resettingAge = resettingAge;
        this.shouldHarvest = shouldHarvest;
        this.ignoreMaxAge = ignoreMaxAge;
        this.tallPlant = tallPlant;
        this.differentBlock = differentBlock;
    }

    public static PlantGrowth getPlantAge(BlockState state) {
        for (PlantGrowth plantAge : values()) {
            if (plantAge.getBlock() == state.getBlock()) {
                return plantAge;
            }
        }
        return null;
    }

    public static int getResettingAge(BlockState state) {
        PlantGrowth plantAge = getPlantAge(state);
        if (plantAge == null) {
            return -1;
        }
        return plantAge.getResettingAge();
    }

    public Block getBlock() {
        return block;
    }

    public int getResettingAge() {
        return resettingAge;
    }

    public boolean shouldHarvest() {
        return shouldHarvest;
    }

    public boolean shouldIgnoreMaxAge() {
        return ignoreMaxAge;
    }

    public boolean isTallPlant() {
        return tallPlant;
    }

    public boolean tallPlantDropsForEverySegment() {
        return true;
    }

    public List<Block> getAttachments() {
        return List.of();
    }

    public boolean hasDifferentBlock() {
        return differentBlock != null;
    }

    public boolean isAgingPlant() {
        return true;
    }

    public Block getDifferentBlock() {
        return differentBlock;
    }

    public Block getBlockToSet() {
        return differentBlock != null ? differentBlock : block;
    }

    public boolean growPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
        if (WorldUtil.dropItem(level, pos, new ItemStack(state.getBlock()))) {
            PlantUtil.handleBoneMealUsed(level, pos, player, stack, true);
            return true;
        }
        return false;
    }
}
