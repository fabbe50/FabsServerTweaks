package com.fabbe50.fabsservertweaks.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(CompassItem.class)
public abstract class CompassItemMixin extends Item {
    @Unique
    private final List<Direction> PLACEABLE_DIRECTIONS = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    public CompassItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (level instanceof ServerLevel serverLevel) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ItemStack compassItem = player.getItemInHand(interactionHand);
            LodestoneTracker tracker = compassItem.get(DataComponents.LODESTONE_TRACKER);
            if (tracker != null) {
                Optional<GlobalPos> optionalBlockPos = tracker.target();
                if (optionalBlockPos.isEmpty()) {
                    return super.use(level, player, interactionHand);
                }
                GlobalPos pos = optionalBlockPos.get();

                ResourceKey<Level> dimension = pos.dimension();
                ServerLevel targetDimension = serverLevel.getServer().getLevel(dimension);
                if (targetDimension == null) {
                    return super.use(level, player, interactionHand);
                }
                BlockPos teleportPosition = null;
                for (Direction direction : PLACEABLE_DIRECTIONS) {
                    BlockPos relativePosition = pos.pos().relative(direction);
                    if (serverLevel.getBlockState(relativePosition.below()).isAir() && serverLevel.getBlockState(relativePosition).isAir()) {
                        teleportPosition = relativePosition.below();
                        break;
                    } else if (serverLevel.getBlockState(relativePosition).isAir() && serverLevel.getBlockState(relativePosition.above()).isAir()) {
                        teleportPosition = relativePosition;
                        break;
                    }
                }
                if (teleportPosition == null) {
                    BlockPos relativePosition = pos.pos().relative(Direction.UP);
                    if (serverLevel.getBlockState(relativePosition).isAir() && serverLevel.getBlockState(relativePosition.above()).isAir()) {
                        teleportPosition = relativePosition;
                    }
                }
                if (teleportPosition != null) {
                    if (!player.isCreative()) {
                        for (ItemStack stack : player.getInventory()) {
                            if (stack.is(Items.ENDER_PEARL)) {
                                stack.shrink(1);
                                player.teleport(new TeleportTransition(targetDimension, teleportPosition.getBottomCenter(), Vec3.ZERO, player.getYRot(), player.getXRot(), Relative.union(Relative.DELTA, Relative.ROTATION), entity -> {}));
                                player.getCooldowns().addCooldown(compassItem, 600);
                                serverPlayer.sendSystemMessage(Component.literal("Teleported!"), true);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    } else {
                        player.teleport(new TeleportTransition(targetDimension, teleportPosition.getBottomCenter(), Vec3.ZERO, player.getYRot(), player.getXRot(), Relative.union(Relative.DELTA, Relative.ROTATION), entity -> {}));
                        player.getCooldowns().addCooldown(compassItem, 600);
                        serverPlayer.sendSystemMessage(Component.literal("Teleported!"), true);
                        return InteractionResult.SUCCESS;
                    }
                    serverPlayer.sendSystemMessage(Component.literal("Missing ender pearl.").withStyle(ChatFormatting.RED), true);
                    return InteractionResult.FAIL;
                } else {
                    serverPlayer.sendSystemMessage(Component.literal("No valid teleport location.").withStyle(ChatFormatting.RED), true);
                    return InteractionResult.FAIL;
                }
            }
        }
        return super.use(level, player, interactionHand);
    }
}
