package com.fabbe50.fabsservertweaks.commands;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.stats.StatsRegistry;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.fabbe50.fabsservertweaks.util.StringUtil;
import com.google.common.collect.Iterators;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.boat.Boat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StatsCommand {
    private static final SimpleCommandExceptionType NO_TARGETS = new SimpleCommandExceptionType(Component.literal("No targets found."));
    private static final DynamicCommandExceptionType INVALID_ENTITY_TYPE = new DynamicCommandExceptionType(entityType -> Component.translatable("Invalid Entity Type: ", entityType));

    private static final List<String> MOB_TYPES = List.of(
            "monsters",
            "animals",
            "aquatic_creatures",
            "bosses",
            "static",
            "neutral",
            "hostile",
            "passive",
            "living",
            "items",
            "boats",
            "tamed"
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        SuggestionProvider<CommandSourceStack> mobTypeProvider = (context, builder) -> {
            MOB_TYPES.forEach(builder::suggest);
            return CompletableFuture.supplyAsync(() -> Suggestions.create("type", builder.build().getList()));
        };

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("stats")
                .requires(commandSourceStack -> commandSourceStack.isPlayer() && commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.literal("lastseen")
                        .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                                .executes(StatsCommand::getPlayerLastOnline)
                        )
                )
                .then(Commands.literal("entities")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .suggests(mobTypeProvider)
                                .executes(context -> getEntityCount(context, StringArgumentType.getString(context, "type")))
                        )
                        .then(Commands.argument("entity_type", ResourceKeyArgument.key(Registries.ENTITY_TYPE))
                                .executes(StatsCommand::getEntityCount)
                        )
                );

        dispatcher.register(command);

        LogUtil.log("Stats command registered");
    }

    private static int getPlayerLastOnline(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<NameAndId> targets = GameProfileArgument.getGameProfiles(context, "targets");
        int success = 0;
        PlayerList playerList = context.getSource().getServer().getPlayerList();

        for (NameAndId target : targets) {
            if (playerList.getPlayer(target.id()) != null) {
                context.getSource().sendSuccess(() -> Component.literal("Player " + target.name() + " is online"), false);
                success++;
                continue;
            }
            LocalDateTime lastOnline = StatsRegistry.getLastOnline(target.id());
            if (lastOnline == null) {
                continue;
            }
            context.getSource().sendSuccess(() -> Component.literal("Player " + target.name() + " was last online: " + StringUtil.formatLocalDateTime(lastOnline)), false);
            success++;
        }

        if (success > 0) {
            return success;
        }
        throw NO_TARGETS.create();
    }

    private static int getEntityCount(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ResourceKey<EntityType<?>> entityTypeKey = ResourceKeyArgument.getRegistryKey(context, "entity_type", Registries.ENTITY_TYPE, INVALID_ENTITY_TYPE);
        ServerLevel level = context.getSource().getLevel();
        RegistryAccess registryAccess = level.registryAccess();
        Optional<Registry<EntityType<?>>> optionalRegistry = registryAccess.lookup(Registries.ENTITY_TYPE);
        if (optionalRegistry.isEmpty()) {
            return -1;
        }
        Registry<EntityType<?>> registry = optionalRegistry.get();
        if (!registry.containsKey(entityTypeKey)) {
            return -1;
        }

        int count = 0;

        List<Entity> entities = new ArrayList<>();
        Iterators.addAll(entities, level.getAllEntities().iterator());
        for (Entity entity : entities) {
            if (entity.is(entityTypeKey)) {
                count++;
            }
        }

        int finalCount = count;
        context.getSource().sendSuccess(() -> Component.literal("Total " + entityTypeKey.identifier().getPath().replace("_", " ") + "s on the server: " + finalCount), false);
        return finalCount;
    }

    private static int getEntityCount(CommandContext<CommandSourceStack> context, String argument) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();
        int count = 0;

        if (!MOB_TYPES.contains(argument)) {
            context.getSource().sendFailure(Component.literal("Invalid mob type"));
            return 0;
        }

        List<Entity> entities = new ArrayList<>();
        Iterators.addAll(entities, level.getAllEntities().iterator());
        for (Entity entity : entities) {
            if (isMobType(entity, argument)) {
                count++;
            }
        }

        int finalCount = count;
        context.getSource().sendSuccess(() -> Component.literal("Total " + argument + " on the server: " + finalCount), false);
        return finalCount;
    }

    private static boolean isMobType(Entity entity, String type) {
        return switch (type) {
            case "monsters" -> entity instanceof Monster;
            case "animals" -> entity instanceof Animal;
            case "aquatic_creatures" -> entity.is(EntityTypeTags.AQUATIC);
            case "bosses" -> entity.is(ModRegistry.BOSSES);
            case "static" -> !(entity instanceof LivingEntity);
            case "neutral" -> entity instanceof NeutralMob;
            case "hostile" -> entity instanceof Enemy && !(entity instanceof NeutralMob);
            case "passive" -> entity instanceof LivingEntity && !(entity instanceof Enemy);
            case "living" -> entity instanceof LivingEntity;
            case "items" -> entity instanceof ItemEntity;
            case "boats" -> entity instanceof Boat;
            case "tamed" -> entity instanceof TamableAnimal tamable && tamable.isTame();
            default -> false;
        };
    }
}
