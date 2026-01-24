package com.fabbe50.fabsservertweaks.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.util.WorldUtil.RelativePosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RailUtil {
    public static boolean sidesAreParallel(Level level, BlockPos pos, RailShape railShape) {
        RailShape[] railShapes = getShapesFromSides(level, pos, railShape);
        if (railShapes.length != 2) {
            return false;
        }
        return areParallel(railShape, railShapes[0]) || areParallel(railShape, railShapes[1]);
    }

    public static BlockState getBetterPlacement(BlockState originalState, BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction placeDirection = context.getHorizontalDirection();
        boolean east_west = placeDirection == Direction.EAST || placeDirection == Direction.WEST;
        RailShape straightRailShape = east_west ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
        RailShape[] railShapes = getShapesFromSides(level, pos, straightRailShape);
        originalState = setShape(originalState, straightRailShape);
        if (railShapes.length != 2) {
            return originalState;
        }
        RailShape slopedShape = getSlope(level, pos, placeDirection);
        if (slopedShape != null) {
            return setShape(originalState, slopedShape);
        }
        updateNeighborSlopes(level, pos, placeDirection);

        if (isStraight(originalState)) {
            return originalState;
        }
        if (railShapes[0] != null && !areParallel(straightRailShape, railShapes[0])) {
            RailShape newShape = switch (placeDirection) {
                case NORTH ->
                        (isStraight(railShapes[0]) || isRailOpenTo(railShapes[0], Direction.WEST)) ? RailShape.NORTH_EAST : straightRailShape;
                case EAST ->
                        (isStraight(railShapes[0]) || isRailOpenTo(railShapes[0], Direction.SOUTH)) ? RailShape.NORTH_EAST : straightRailShape;
                case SOUTH ->
                        (isStraight(railShapes[0]) || isRailOpenTo(railShapes[0], Direction.WEST)) ? RailShape.SOUTH_EAST : straightRailShape;
                case WEST ->
                        (isStraight(railShapes[0]) || isRailOpenTo(railShapes[0], Direction.SOUTH)) ? RailShape.NORTH_WEST : straightRailShape;
                default -> straightRailShape;
            };
            return setShape(originalState, newShape);
        } else if (railShapes[1] != null && !areParallel(straightRailShape, railShapes[1])) {
            RailShape newShape = switch (placeDirection) {
                case NORTH ->
                        (isStraight(railShapes[1]) || isRailOpenTo(railShapes[1], Direction.EAST)) ? RailShape.NORTH_WEST : straightRailShape;
                case EAST ->
                        (isStraight(railShapes[1]) || isRailOpenTo(railShapes[1], Direction.NORTH)) ? RailShape.SOUTH_EAST : straightRailShape;
                case SOUTH ->
                        (isStraight(railShapes[1]) || isRailOpenTo(railShapes[1], Direction.EAST)) ? RailShape.SOUTH_WEST : straightRailShape;
                case WEST ->
                        (isStraight(railShapes[1]) || isRailOpenTo(railShapes[1], Direction.NORTH)) ? RailShape.SOUTH_WEST : straightRailShape;
                default -> straightRailShape;
            };
            return setShape(originalState, newShape);
        }
        return originalState;
    }

    public static void updateNeighborSlopes(Level level, BlockPos pos, Direction placeDirection) {
        switch (placeDirection) {
            case NORTH, SOUTH -> {
                updateSlope(level, pos, RelativePosition.NORTH_DOWN, RailShape.NORTH_SOUTH, RailShape.ASCENDING_SOUTH);
                updateSlope(level, pos, RelativePosition.SOUTH_DOWN, RailShape.NORTH_SOUTH, RailShape.ASCENDING_NORTH);
            }
            case EAST, WEST -> {
                updateSlope(level, pos, RelativePosition.EAST_DOWN, RailShape.EAST_WEST, RailShape.ASCENDING_WEST);
                updateSlope(level, pos, RelativePosition.WEST_DOWN, RailShape.EAST_WEST, RailShape.ASCENDING_EAST);
            }
        }
    }

    public static void updateSlope(Level level, BlockPos pos, RelativePosition relativePosition, RailShape match, RailShape result) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos relativePos = relativePosition.getRelativePosition(pos);
            BlockState state = level.getBlockState(relativePos);
            RailShape railShape = getShapeFromBlockState(state);
            if (railShape != null && railShape.equals(match)) {
                serverLevel.setBlockAndUpdate(relativePos, setShape(state, result));
            }
        }
    }

    public static RailShape getSlope(Level level, BlockPos pos, Direction placeDirection) {
        return switch (placeDirection) {
            case NORTH, SOUTH -> {
                if (doesPositionHaveRail(level, pos, RelativePosition.NORTH_UP)) {
                    yield RailShape.ASCENDING_NORTH;
                } else if (doesPositionHaveRail(level, pos, RelativePosition.SOUTH_UP)) {
                    yield RailShape.ASCENDING_SOUTH;
                }
                yield null;
            }
            case EAST, WEST -> {
                if (doesPositionHaveRail(level, pos, RelativePosition.EAST_UP)) {
                    yield RailShape.ASCENDING_EAST;
                } else if (doesPositionHaveRail(level, pos, RelativePosition.WEST_UP)) {
                    yield RailShape.ASCENDING_WEST;
                }
                yield null;
            }
            default -> null;
        };
    }

    public static boolean doesPositionHaveRail(Level level, BlockPos pos, RelativePosition relativePosition) {
        BlockState state = level.getBlockState(relativePosition.getRelativePosition(pos));
        return state.is(BlockTags.RAILS);
    }

    public static RailShape[] getShapesFromSides(Level level, BlockPos pos, RailShape railShape) {
        return switch (railShape) {
            case NORTH_SOUTH -> {
                RailShape shape1 = getShapeFromSide(level, pos, RelativePosition.EAST);
                RailShape shape2 = getShapeFromSide(level, pos, RelativePosition.WEST);
                yield new RailShape[] {shape1, shape2};
            }
            case EAST_WEST -> {
                RailShape shape1 = getShapeFromSide(level, pos, RelativePosition.NORTH);
                RailShape shape2 = getShapeFromSide(level, pos, RelativePosition.SOUTH);
                yield new RailShape[] {shape1, shape2};
            }
            default -> new RailShape[] {};
        };
    }

    public static RailShape getShapeFromSide(Level level, BlockPos pos, RelativePosition relativePosition) {
        BlockPos side = relativePosition.getRelativePosition(pos);
        BlockState state = level.getBlockState(side);
        return getShapeFromBlockState(state);
    }

    public static BlockState setShape(BlockState state, RailShape shape) {
        if (state.getBlock() instanceof BaseRailBlock railBlock) {
            return state.setValue(railBlock.getShapeProperty(), shape);
        }
        return state;
    }

    public static RailShape getShapeFromBlockState(BlockState state) {
        if (state.getBlock() instanceof BaseRailBlock railBlock) {
            return state.getValue(railBlock.getShapeProperty());
        }
        return null;
    }

    public static boolean isRailOpenTo(RailShape shape, Direction relativeDirection) {
        return switch (relativeDirection) {
            case NORTH -> RailDirections.NORTH_PLAIN_ENDED.compare(shape);
            case SOUTH -> RailDirections.SOUTH_PLAIN_ENDED.compare(shape);
            case WEST -> RailDirections.WEST_PLAIN_ENDED.compare(shape);
            case EAST -> RailDirections.EAST_PLAIN_ENDED.compare(shape);
            default -> false;
        };
    }

    public static boolean areParallel(RailShape shape1, RailShape shape2) {
        if (shape2 == null) {
            return false;
        }
        return shape1.equals(shape2);
    }

    public static boolean isStraight(RailShape shape) {
        return shape.equals(RailShape.EAST_WEST) || shape.equals(RailShape.NORTH_SOUTH);
    }

    public static boolean isStraight(BlockState state) {
        if (state.getBlock() instanceof BaseRailBlock railBlock) {
            return railBlock.isStraight();
        }
        return false;
    }

    public enum RailDirections {
        NORTH_PLAIN_ENDED(RailShape.NORTH_SOUTH, RailShape.NORTH_EAST, RailShape.NORTH_WEST, RailShape.ASCENDING_SOUTH),
        SOUTH_PLAIN_ENDED(RailShape.NORTH_SOUTH, RailShape.SOUTH_EAST, RailShape.SOUTH_WEST, RailShape.ASCENDING_NORTH),
        EAST_PLAIN_ENDED(RailShape.EAST_WEST, RailShape.SOUTH_EAST, RailShape.NORTH_EAST, RailShape.ASCENDING_WEST),
        WEST_PLAIN_ENDED(RailShape.EAST_WEST, RailShape.SOUTH_WEST, RailShape.NORTH_WEST, RailShape.ASCENDING_EAST);

        private final RailShape[] shapes;
        RailDirections(RailShape... shapes) {
            this.shapes = shapes;
        }

        public boolean compare(RailShape railShape) {
            for (RailShape shape : this.shapes) {
                if (shape.equals(railShape)) {
                    return true;
                }
            }
            return false;
        }
    }
}
