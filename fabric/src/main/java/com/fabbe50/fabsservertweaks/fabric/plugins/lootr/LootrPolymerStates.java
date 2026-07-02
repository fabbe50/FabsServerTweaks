package com.fabbe50.fabsservertweaks.fabric.plugins.lootr;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.fabric.plugins.polymer.PolymerStates;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.blocks.api.BlockModelType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.LinkedHashMap;
import java.util.Map;

public class LootrPolymerStates extends PolymerStates {
    private static final LinkedHashMap<LootrBlocks, LinkedHashMap<LootrBlockStateInfo, BlockState>> blockTypeMap = new LinkedHashMap<>();

    public static void init() {
        register(LootrBlocks.CHEST, BlockModelType.LEAVES);
        register(LootrBlocks.TRAPPED_CHEST, BlockModelType.LEAVES);
        register(LootrBlocks.BARREL);
        register(LootrBlocks.OPENED_BARREL);
        register(LootrBlocks.SHULKER);
//        register(LootrBlocks.DECORATED_POT, BlockModelType.CACTUS);
        register(LootrBlocks.SUSPICIOUS_SAND);
        register(LootrBlocks.SUSPICIOUS_GRAVEL);
        register(LootrBlocks.TROPHY, BlockModelType.HEAD);
    }

    private static void register(LootrBlocks type) {
        register(type, BlockModelType.FULL_BLOCK);
    }

    private static void register(LootrBlocks type, BlockModelType blockModelType) {
        LinkedHashMap<LootrBlockStateInfo, BlockState> blockStateMap = new LinkedHashMap<>();
        switch (type) {
            case CHEST, TRAPPED_CHEST, DECORATED_POT, TROPHY -> addRotationalBlockState(blockStateMap, horizontalRotationMap, blockModelType, type.getModId(), type.getName(), type.getSuffix(), type.isSimple());
            case SHULKER, BARREL, OPENED_BARREL -> addRotationalBlockState(blockStateMap, rotationMap, blockModelType, type.getModId(), type.getName(), type.getSuffix(), type.isSimple());
            case SUSPICIOUS_SAND -> blockStateMap.put(new LootrBlockStateInfo(Direction.NORTH, true), textured(blockModelType, "lootr", "block/suspicious_sand_open", 0, 0));
            case SUSPICIOUS_GRAVEL -> blockStateMap.put(new LootrBlockStateInfo(Direction.NORTH, true), textured(blockModelType, "lootr", "block/suspicious_gravel_open", 0, 0));
            default -> addDefaultBlockState(blockStateMap, blockModelType, type.getModId(), type.getName(), type.getSuffix(), type.isSimple());
        }
        blockTypeMap.put(type, blockStateMap);
    }

    private static void addRotationalBlockState(LinkedHashMap<LootrBlockStateInfo, BlockState> blockStateMap, Map<Direction, Pair<Integer, Integer>> rotationMap, BlockModelType blockModelType, String modId, String name, String suffix, boolean simple) {
        for (Direction facing : rotationMap.keySet()) {
            Pair<Integer, Integer> rotation = rotationMap.get(facing);
            blockStateMap.put(new LootrBlockStateInfo(facing, false), textured(blockModelType, modId, "block/" + name, rotation.getFirst(), rotation.getSecond()));
            if (!simple) {
                blockStateMap.put(new LootrBlockStateInfo(facing, true), textured(blockModelType, modId, "block/" + name + suffix, rotation.getFirst(), rotation.getSecond()));
            }
        }
    }

    private static void addDefaultBlockState(LinkedHashMap<LootrBlockStateInfo, BlockState> blockStateMap, BlockModelType blockModelType, String modId, String name, String suffix, boolean simple) {
        blockStateMap.put(new LootrBlockStateInfo(Direction.NORTH, false), textured(blockModelType, modId, "block/" + name, 0, 0));
        if (!simple) {
            blockStateMap.put(new LootrBlockStateInfo(Direction.NORTH, true), textured(blockModelType, modId, "block/" + name + suffix, 0, 0));
        }
    }

    public static BlockState getOpenStateFromState(BlockState state) {
        LootrBlocks lootrBlock = LootrBlocks.fromBlockState(state);
        if (lootrBlock == null) {
            LogUtil.log("Couldn't find lootr block for state: " + state);
            return null;
        }
        return switch (lootrBlock) {
            case CHEST, TRAPPED_CHEST/*, DECORATED_POT*/ -> {
                Direction direction = state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? state.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
                yield LootrPlugin.getOpenedBlock(lootrBlock).defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
            }
            case BARREL, SHULKER -> {
                Direction direction = state.hasProperty(BlockStateProperties.FACING) ? state.getValue(BlockStateProperties.FACING) : Direction.UP;
                yield LootrPlugin.getOpenedBlock(lootrBlock).defaultBlockState().setValue(BlockStateProperties.FACING, direction);
            }
            case SUSPICIOUS_SAND, SUSPICIOUS_GRAVEL -> LootrPlugin.getOpenedBlock(lootrBlock).defaultBlockState();
            default -> null;
        };
    }

    public static BlockState getBlockState(BlockState state, boolean opened) {
        return getBlockState(state, Direction.NORTH, opened);
    }

    public static BlockState getBlockState(BlockState state, Direction facing, boolean opened) {
        LootrBlocks type = LootrBlocks.fromBlockState(state);
        if (type == null) {
            return state;
        }
        LinkedHashMap<LootrBlockStateInfo, BlockState> blockStateMap = blockTypeMap.get(type);
        if (blockStateMap == null) {
            return state;
        }
        for (LootrBlockStateInfo info : blockStateMap.keySet()) {
            if (info.facing().equals(facing) && info.opened() == opened) {
                return blockStateMap.get(info);
            }
        }
        return state;
    }

    public static BlockState getSpecificState(LootrBlocks type, Direction facing, boolean opened) {
        LinkedHashMap<LootrBlockStateInfo, BlockState> blockStateMap = blockTypeMap.get(type);
        if (blockStateMap == null) {
            return null;
        }
        for (LootrBlockStateInfo info : blockStateMap.keySet()) {
            if (info.facing().equals(facing) && info.opened() == opened) {
                return blockStateMap.get(info);
            }
        }
        return null;
    }
}
