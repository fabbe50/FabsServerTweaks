package com.fabbe50.fabsservertweaks.core.dispenser;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class BlockDispenseItemBehaviour implements DispenseItemBehavior {
    @Override
    public @NonNull ItemStack dispense(@NonNull BlockSource source, @NonNull ItemStack dispensed) {
        ServerLevel level = source.level();
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Position position = DispenserBlock.getDispensePosition(source);
        if (ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_DISPENSERS_CAN_PLACE_BLOCKS)) {
            BlockPos posInFront = source.pos().relative(direction);
            if (level.getBlockState(posInFront).isAir()) {
                if (dispensed.getItem() instanceof BlockItem blockItem && !dispensed.is(ModRegistry.BLOCK_DISPENSE_BLACKLIST)) {
                    Block block = blockItem.getBlock();
                    BlockState state = block.defaultBlockState();
                    VoxelShape shape;
                    try {
                        shape = state.getShape(level, posInFront);
                    } catch (Exception e) {
                        LogUtil.debug("Shape not found: " + state);
                        return spawnItem(level, source, direction, position, dispensed);
                    }
                    if (state.hasBlockEntity()) {
                        return spawnItem(level, source, direction, position, dispensed);
                    }
                    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) || state.hasProperty(BlockStateProperties.FACING) || state.hasProperty(BlockStateProperties.AXIS)) {
                        return spawnItem(level, source, direction, position, dispensed);
                    }
                    if (Block.isShapeFullBlock(shape)) {
                        if (dispensed.has(DataComponents.BLOCK_STATE)) {
                            BlockItemStateProperties stateProperties = dispensed.get(DataComponents.BLOCK_STATE);
                            if (stateProperties != null) {
                                state = stateProperties.apply(state);
                            }
                        }
                        level.setBlockAndUpdate(posInFront, state);
                        dispensed.shrink(1);
                        playSound(source);
                        playAnimation(source, direction);
                        return dispensed;
                    }
                    return spawnItem(level, source, direction, position, dispensed);
                }
            } else {
                playSound(source);
                playAnimation(source, direction);
                return dispensed;
            }
        }
        return spawnItem(level, source, direction, position, dispensed);
    }

    private static ItemStack spawnItem(final Level level, final BlockSource source, final Direction direction, final Position position, final ItemStack dispensed) {
        ItemStack stack = dispensed.split(1);
        spawnItem(level, stack, 6, direction, position);
        playSound(source);
        playAnimation(source, direction);
        return dispensed;
    }

    public static void spawnItem(final Level level, final ItemStack itemStack, final int accuracy, final Direction direction, final Position position) {
        double spawnX = position.x();
        double spawnY = position.y();
        double spawnZ = position.z();
        if (direction.getAxis() == Axis.Y) {
            spawnY -= 0.125F;
        } else {
            spawnY -= 0.15625F;
        }

        ItemEntity itemEntity = new ItemEntity(level, spawnX, spawnY, spawnZ, itemStack);
        RandomSource random = level.getRandom();
        double pow = random.nextDouble() * 0.1 + 0.2;
        itemEntity.setDeltaMovement(random.triangle((double)direction.getStepX() * pow, 0.0172275 * (double)accuracy), random.triangle(0.2, 0.0172275 * (double)accuracy), random.triangle((double)direction.getStepZ() * pow, 0.0172275 * (double)accuracy));
        level.addFreshEntity(itemEntity);
    }

    private static void playSound(final BlockSource source) {
        source.level().levelEvent(1000, source.pos(), 0);
    }

    private static void playAnimation(final BlockSource source, final Direction direction) {
        source.level().levelEvent(2000, source.pos(), direction.get3DDataValue());
    }
}
