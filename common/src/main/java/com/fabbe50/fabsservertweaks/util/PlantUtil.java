package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.PlantGrowth;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PlantUtil {
    private static final IntegerProperty[] AGE_PROPERTIES = new IntegerProperty[]{
            BlockStateProperties.AGE_1,
            BlockStateProperties.AGE_2,
            BlockStateProperties.AGE_3,
            BlockStateProperties.AGE_4,
            BlockStateProperties.AGE_5,
            BlockStateProperties.AGE_7,
            BlockStateProperties.AGE_15,
            BlockStateProperties.AGE_25
    };

    public static IntegerProperty getAgeProperty(BlockState state) {
        for (IntegerProperty ageProperty : AGE_PROPERTIES) {
            if (state.hasProperty(ageProperty)) {
                return ageProperty;
            }
        }
        return null;
    }

    public static boolean isMaxAge(BlockState state, IntegerProperty ageProperty) {
        int maxAge = 0;
        if (ageProperty != null) {
            for (Integer possibleMaxAge : ageProperty.getPossibleValues()) {
                if (possibleMaxAge > maxAge) {
                    maxAge = possibleMaxAge;
                }
            }
            return state.getValue(ageProperty) == maxAge;
        }
        return false;
    }

    public static void setPlantWithAge(ServerLevel level, BlockPos pos, BlockState state, PlantGrowth plantGrowth, IntegerProperty ageProperty, int age) {
        if (ageProperty != null) {
            if (plantGrowth.hasDifferentBlock()) {
                level.setBlockAndUpdate(pos, plantGrowth.getBlockToSet().defaultBlockState().setValue(ageProperty, age));
            } else {
                level.setBlockAndUpdate(pos, state.setValue(ageProperty, age));
            }
        } else {
            if (plantGrowth.hasDifferentBlock()) {
                level.setBlockAndUpdate(pos, plantGrowth.getBlockToSet().defaultBlockState());
            } else {
                level.setBlockAndUpdate(pos, state);
            }
        }
    }

    public static boolean boneMealPlant(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
        PlantGrowth plantGrowth = PlantGrowth.getPlantAge(state);
        if (plantGrowth != null) {
            return plantGrowth.growPlant(level, pos, state, player, stack);
        }
        return false;
    }

    public static boolean growIfPossible(ServerLevel level, BlockPos pos, BlockState state) {
        IntegerProperty ageProperty = getAgeProperty(state);
        if (ageProperty != null) {
            if (isMaxAge(state, ageProperty)) {
                return false;
            }
            level.setBlockAndUpdate(pos, state.setValue(ageProperty, state.getValue(ageProperty) + 1));
            return true;
        }
        return false;
    }

    public static boolean growFromStem(ServerLevel serverLevel, BlockPos pos, BlockState state, RandomSource random, Block fruit, Block stem) {
        if (state.getValue(StemBlock.AGE) == StemBlock.MAX_AGE) {
            if (random.nextInt(4) == 0) {
                for (int i = 0; i < 6; i++) {
                    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                    BlockPos blockPos2 = pos.relative(direction);
                    BlockState blockState2 = serverLevel.getBlockState(blockPos2.below());
                    if (serverLevel.getBlockState(blockPos2).isAir() && (blockState2.is(Blocks.FARMLAND) || blockState2.is(BlockTags.DIRT))) {
                        serverLevel.setBlockAndUpdate(blockPos2, fruit.defaultBlockState());
                        serverLevel.setBlockAndUpdate(pos, stem.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction));
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean growInColumn(ServerLevel serverLevel, BlockPos pos, Block block, int maxHeight) {
        BlockPos footPos = PillarGrowUtil.getPillarFoot(block, serverLevel, pos);
        int height = PillarGrowUtil.getPillarHeight(block.defaultBlockState(), serverLevel, footPos);
        LogUtil.debug("Pillar height: " + height);
        RandomSource random = serverLevel.getRandom();
        boolean cactus = block == Blocks.CACTUS;
        if (height < maxHeight) {
            BlockPos above = footPos.above();
            for (int i = 1; i < maxHeight; i++) {
                BlockState newState = serverLevel.getBlockState(above);
                if (newState.is(Blocks.CACTUS_FLOWER)) {
                    return false;
                }
                if (newState.isAir()) {
                    if (cactus && random.nextInt(10) == 0) {
                        block = Blocks.CACTUS_FLOWER;
                    }
                    if (random.nextBoolean()) {
                        serverLevel.setBlockAndUpdate(above, block.defaultBlockState());
                    }
                    boneMealParticle(serverLevel, above);
                    return true;
                }
                above = above.above();
            }
        } else if (cactus && height == maxHeight) {
            return tryToGrowCactusFlower(serverLevel, footPos, random, maxHeight);
        }
        return false;
    }

    public static boolean tryToGrowCactusFlower(ServerLevel serverLevel, BlockPos pos, RandomSource random, int maxHeight) {
        BlockPos newPos = pos.above();
        BlockState newState = serverLevel.getBlockState(newPos);
        int tries = maxHeight + 1;
        while (!newState.isAir()) {
            if (newState.is(Blocks.CACTUS_FLOWER)) {
                return false;
            }
            newPos = newPos.above();
            newState = serverLevel.getBlockState(newPos);
            if (tries == 0) {
                break;
            }
            tries--;
        }
        if (random.nextInt(4) == 0) {
            serverLevel.setBlockAndUpdate(newPos, Blocks.CACTUS_FLOWER.defaultBlockState());
        }
        boneMealParticle(serverLevel, newPos);
        return true;
    }

    public static void handleBoneMealUsed(ServerLevel serverLevel, BlockPos pos, Player player, ItemStack stack, boolean doParticle) {
        if (doParticle) {
            boneMealParticle(serverLevel, pos);
        }
        ItemStackUtil.shrink(stack, player);
        player.getCooldowns().addCooldown(stack, 4);
    }

    public static void boneMealParticle(ServerLevel serverLevel, BlockPos pos) {
        RandomSource random = serverLevel.getRandom();
        double offset = 0.5;
        double posX = pos.getX() + offset;
        double posY = pos.getY() + offset;
        double posZ = pos.getZ() + offset;
        double distX = random.nextDouble() / 2.5;
        double distY = random.nextDouble() / 2.5;
        double distZ = random.nextDouble() / 2.5;
        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, posX, posY, posZ, 10, distX, distY, distZ, 1);
    }
}
