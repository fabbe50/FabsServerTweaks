package com.fabbe50.fabsservertweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class GotoCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("goto").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("dimension", DimensionArgument.dimension()).executes(context -> {
                            ServerLevel level = DimensionArgument.getDimension(context, "dimension");
                            Player player = context.getSource().getPlayer();
                            if (player != null) {
                                Vec3 originalPosition = player.position();
                                ServerLevel originalDimension = (ServerLevel) player.level();
                                WorldBorder worldBorder = level.getWorldBorder();
                                double d = DimensionType.getTeleportationScale(originalDimension.dimensionType(), level.dimensionType());
                                double y = originalPosition.y;
                                int logicalHeight = level.dimensionType().logicalHeight();
                                if (y > logicalHeight) {
                                    y = (logicalHeight - 20) - player.getBbHeight();
                                }
                                Vec3 newPosition = worldBorder.clampToBounds(originalPosition.x * d, y, originalPosition.z * d).getCenter();
                                player.teleport(new TeleportTransition(level, newPosition, Vec3.ZERO, player.getYRot(), player.getXRot(), Relative.union(Relative.DELTA, Relative.ROTATION), entity -> {
                                    EntityDimensions entityDimensions = entity.getDimensions(entity.getPose());
                                    Vec3 vec3 = entity.level().findFreePosition(entity, Shapes.create(AABB.ofSize(entity.position(), entityDimensions.width(), entityDimensions.height(), entityDimensions.width())), entity.position(), 21, 21, 21).orElse(entity.position());
                                    entity.setPos(vec3);
                                }));
                                return 1;
                            }
                            return 0;
                        }))
        );
    }
}
