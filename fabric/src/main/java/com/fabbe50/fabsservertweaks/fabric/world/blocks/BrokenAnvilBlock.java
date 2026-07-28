package com.fabbe50.fabsservertweaks.fabric.world.blocks;

import com.fabbe50.fabsservertweaks.fabric.plugins.lootr.PolymerStateSupplier;
import com.fabbe50.fabsservertweaks.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class BrokenAnvilBlock extends PolymerHorizontalDirectionalTexturedBlock {
    private static final Map<Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or(Block.column(12.0F, 0.0F, 4.0F), Block.column(8.0F, 10.0F, 4.0F, 5.0F), Block.column(4.0F, 8.0F, 5.0F, 10.0F), Block.column(10.0F, 16.0F, 10.0F, 16.0F)));

    public BrokenAnvilBlock(Properties properties, Block polymerBlock, PolymerStateSupplier stateSupplier) {
        super(properties, polymerBlock, stateSupplier);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        if (itemStack.is(Items.IRON_BLOCK)) {
            level.setBlockAndUpdate(pos, Blocks.DAMAGED_ANVIL.defaultBlockState().setValue(FACING, state.getValue(FACING)));
            ItemStackUtil.shrink(itemStack, player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    protected @NonNull VoxelShape getShape(BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING).getAxis());
    }
}
