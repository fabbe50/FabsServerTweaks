package com.fabbe50.fabsservertweaks.fabric.world.blocks;

import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.PolymerStateSupplier;
import com.fabbe50.fabsservertweaks.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class ChunkLoaderBlock extends PolymerSimpleTexturedBlock {
    private final VoxelShape shape = Block.column(4, 0, 10);

    public ChunkLoaderBlock(Properties properties, Block polymerBlock, PolymerStateSupplier stateSupplier) {
        super(properties, polymerBlock, stateSupplier);
    }

    @Override
    protected void onPlace(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState oldState, boolean movedByPiston) {
        level.getChunkSource().updateChunkForced(level.getChunk(pos).getPos(), true);
    }

    @Override
    public @NonNull BlockState playerWillDestroy(@NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState state, @NonNull Player player) {
        level.getChunkSource().updateChunkForced(level.getChunk(pos).getPos(), false);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void destroy(LevelAccessor level, @NonNull BlockPos pos, @NonNull BlockState state) {
        level.getChunkSource().updateChunkForced(level.getChunk(pos).getPos(), false);
    }

    @Override
    protected @NonNull BlockState updateShape(@NonNull BlockState state, @NonNull LevelReader levelReader, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        BlockState newState = directionToNeighbour == Direction.DOWN && !canSurvive(state, levelReader, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, levelReader, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        if (!newState.equals(state)) {
            if (levelReader instanceof ServerLevel level) {
                level.getChunkSource().updateChunkForced(level.getChunk(pos).getPos(), false);
            }
        }
        return newState;
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return shape;
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, @NonNull LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        LevelChunk chunk = level.getChunkAt(pos);
        if (WorldUtil.isChunkLoaded(level, pos)) {
            player.sendOverlayMessage(Component.literal("Chunk loaded at: " + chunk.getPos()));
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }
}
