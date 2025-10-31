package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {
    public static List<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        List<BlockPos> blocks = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        int rSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= rSq) {
                    blocks.add(new BlockPos(cx + x, cy, cz + z));
                }
            }
        }

        return blocks;
    }

    public static List<BlockPos> getBlocksInSphericalRadius(BlockPos center, int radius) {
        List<BlockPos> blocks = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        int rSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= rSq) {
                        blocks.add(new BlockPos(cx + x, cy + y, cz + z));
                    }
                }
            }
        }

        return blocks;
    }

    public enum RelativePosition {
        NORTH(Direction.NORTH) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH);
            }
        },
        NORTH_EAST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.EAST);
            }
        },
        NORTH_WEST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.WEST);
            }
        },
        NORTH_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.UP);
            }
        },
        NORTH_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.NORTH).relative(Direction.DOWN);
            }
        },
        SOUTH(Direction.SOUTH) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH);
            }
        },
        SOUTH_EAST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.EAST);
            }
        },
        SOUTH_WEST {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.WEST);
            }
        },
        SOUTH_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.UP);
            }
        },
        SOUTH_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.SOUTH).relative(Direction.DOWN);
            }
        },
        WEST(Direction.WEST) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.WEST);
            }
        },
        WEST_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.WEST).relative(Direction.UP);
            }
        },
        WEST_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.WEST).relative(Direction.DOWN);
            }
        },
        EAST(Direction.EAST) {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.EAST);
            }
        },
        EAST_UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.EAST).relative(Direction.UP);
            }
        },
        EAST_DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.EAST).relative(Direction.DOWN);
            }
        },
        UP {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.UP);
            }
        },
        DOWN {
            @Override
            public BlockPos getRelativePosition(BlockPos pos) {
                return pos.relative(Direction.DOWN);
            }
        };

        private final Direction[] directions;
        RelativePosition(Direction... directions) {
            this.directions = directions;
        }

        public Direction[] getDirections() {
            return directions;
        }

        public BlockPos getRelativePosition(BlockPos pos) {
            throw new RuntimeException("Incorrect method call. This method should not be called without overriding the function.");
        }

        public static List<RelativePosition> getImmediateHorizontals() {
            return List.of(NORTH, SOUTH, EAST, WEST);
        }
    }
}
