package com.fabbe50.fabsservertweaks.fabsservertweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static List<BlockPos> getBlockPositions(AABB box) {
        List<BlockPos> positions = new ArrayList<>();

        int minX = (int) Math.floor(box.minX);
        int minY = (int) Math.floor(box.minY);
        int minZ = (int) Math.floor(box.minZ);
        int maxX = (int) Math.floor(box.maxX);
        int maxY = (int) Math.floor(box.maxY);
        int maxZ = (int) Math.floor(box.maxZ);

        for (BlockPos pos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
            positions.add(pos.immutable()); // avoid mutable references
        }

        return positions;
    }

    public static void breakBlocks(Level level, BlockPos dropPos, Set<BlockPos> positions, ServerPlayer player, ItemStack stack) {
        List<ItemStack> drops = new ArrayList<>();
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block block = state.getBlock();
            if (checkGameMasterCondition(block, player)) {
                level.sendBlockUpdated(pos, state, state, 3);
                continue;
            }
            if (checkGameTypeCondition(player, level, pos)) {
                continue;
            }
            if (!isPlayerInstaBuild(player)) {
                drops.addAll(state.getDrops(new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, pos.getCenter())));
                ToolUtil.hurtItem(1, (ServerLevel) level, stack, pos);
            }
            if (blockEntity != null) {
                level.removeBlockEntity(pos);
            }
//            level.removeBlock(pos, false);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        Vec3 newDropPos = dropPos.getCenter();
        for (ItemStack drop : drops) {
            ItemEntity itemEntity = new ItemEntity(level, newDropPos.x(), newDropPos.y(), newDropPos.z(), drop);
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
        }
    }

    public static boolean checkGameMasterCondition(Block block, ServerPlayer player) {
        if (block instanceof GameMasterBlock) {
            if (player == null) {
                return false;
            }
            return !player.canUseGameMasterBlocks();
        }
        return false;
    }

    public static boolean checkGameTypeCondition(ServerPlayer player, Level level, BlockPos pos) {
        if (player == null) {
            return false;
        }
        GameType type = isPlayerInstaBuild(player) ? GameType.CREATIVE : GameType.SURVIVAL;
        return player.blockActionRestricted(level, pos, type);
    }

    public static boolean isPlayerInstaBuild(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        return player.getAbilities().instabuild;
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
