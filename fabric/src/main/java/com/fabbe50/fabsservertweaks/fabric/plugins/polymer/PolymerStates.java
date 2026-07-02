package com.fabbe50.fabsservertweaks.fabric.plugins.polymer;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class PolymerStates {
    protected static final Map<Direction, Pair<Integer, Integer>> rotationMap = Map.of(
            Direction.UP, Pair.of(0, 0),
            Direction.DOWN, Pair.of(0, 180),
            Direction.NORTH, Pair.of(90, 0),
            Direction.EAST, Pair.of(90, 90),
            Direction.SOUTH, Pair.of(90, 180),
            Direction.WEST, Pair.of(90, 270)
    );
    protected static final Map<Direction, Pair<Integer, Integer>> horizontalRotationMap = Map.of(
            Direction.NORTH, Pair.of(0, 0),
            Direction.EAST, Pair.of(0, 90),
            Direction.SOUTH, Pair.of(0, 180),
            Direction.WEST, Pair.of(0, 270)
    );
    protected static final Map<Axis, Pair<Integer, Integer>> axisRotationMap = Map.of(
            Axis.X, Pair.of(90, 90),
            Axis.Y, Pair.of(0, 0),
            Axis.Z, Pair.of(90, 0)
    );

    protected PolymerStates() {}

    public static BlockState textured(BlockModelType blockModelType, String modId, String modelPath, int xRotation, int yRotation) {
        LogUtil.log("Requesting block model: " + modId + ":" + modelPath + " with rotation: X=" + xRotation + ", Y=" + yRotation);
        if (PolymerBlockResourceUtils.getBlocksLeft(blockModelType) < 1) {
            LogUtil.error("Not enough models for " + modId + ":" + modelPath);
        }
        return PolymerBlockResourceUtils.requestBlock(
                blockModelType,
                PolymerBlockModel.of(Fabsservertweaks.location(modId, modelPath), xRotation, yRotation)
        );
    }
}
