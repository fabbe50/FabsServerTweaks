package com.fabbe50.fabsservertweaks.commands;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.ModConfig;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.Difficulty;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue.DifficultyArgumentType;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleArgumentType;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue.TrampleMode;
import com.fabbe50.fabsservertweaks.util.BuiltinDatapack;
import com.fabbe50.fabsservertweaks.util.BuiltinDatapackUtil;
import com.fabbe50.fabsservertweaks.util.EnchantmentUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent.CopyToClipboard;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerTweaksCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        List<String> BOOLEAN_SETTINGS = List.of(
                "shareSeed",
                "overrideNormalLead",
                "canLeashAnimals",
                "canLeashMonsters",
                "canLeashBosses",
                "canLeashVillagerTypes",
                "canLeashGolems",
                "canLeashPets"
        );
        List<String> INTEGER_SETTINGS = List.of(
        );
        List<String> ALL_SETTINGS = new ArrayList<>(List.of(
                "cropTrampleMode",
                "eggTrampleMode",
                "difficultyType"
        ));
        ALL_SETTINGS.addAll(BOOLEAN_SETTINGS);
        ALL_SETTINGS.addAll(INTEGER_SETTINGS);
        SuggestionProvider<CommandSourceStack> optionProvider = (context, builder) -> {
            ALL_SETTINGS.forEach(builder::suggest);
            return CompletableFuture.supplyAsync(() -> Suggestions.create("option", builder.build().getList()));
        };

        RequiredArgumentBuilder<CommandSourceStack, String> valueArgument = Commands.argument("value", StringArgumentType.word())
                .suggests((context, builder) -> {
                    String option = StringArgumentType.getString(context, "option");
                    if (option.equals("difficultyType")) {
                        return DifficultyArgumentType.value().listSuggestions(context, builder);
                    } else if (option.equals("cropTrampleMode") || option.equals("eggTrampleMode")) {
                        return TrampleArgumentType.value().listSuggestions(context, builder);
                    } else if (BOOLEAN_SETTINGS.contains(option)) {
                        return BoolArgumentType.bool().listSuggestions(context, builder);
                    }
                    return builder.buildFuture();
                })
                .executes(context -> {
                    String option = StringArgumentType.getString(context, "option");
                    String value = StringArgumentType.getString(context, "value");
                    if (option.equals("difficultyType")) {
                        Difficulty difficulty = Difficulty.byName(value, null);
                        if (difficulty == null) {
                            context.getSource().sendFailure(Component.literal("Invalid difficulty type."));
                            return 0;
                        }
                        Fabsservertweaks.CONFIG.difficulty = difficulty;
                        AutoConfig.getConfigHolder(ModConfig.class).save();
                        context.getSource().sendSuccess(() -> Component.literal("Trample mode for crops set to " + difficulty.getSerializedName()), true);
                        return 1;
                    } else if (option.equals("cropTrampleMode") || option.equals("eggTrampleMode")) {
                        TrampleMode trampleMode = TrampleMode.byName(value, null);
                        if (trampleMode == null) {
                            context.getSource().sendFailure(Component.literal("Invalid trample mode."));
                            return 0;
                        }
                        if (option.equals("cropTrampleMode")) {
                            Fabsservertweaks.CONFIG.cropTrampleMode = trampleMode;
                            AutoConfig.getConfigHolder(ModConfig.class).save();
                            context.getSource().sendSuccess(() -> Component.literal("Trample mode for crops set to " + trampleMode.getSerializedName()), true);
                            return 1;
                        } else if (option.equals("eggTrampleMode")) {
                            Fabsservertweaks.CONFIG.eggTrampleMode = trampleMode;
                            AutoConfig.getConfigHolder(ModConfig.class).save();
                            context.getSource().sendSuccess(() -> Component.literal("Trample mode for crops set to " + trampleMode.getSerializedName()), true);
                            return 1;
                        }
                    } else if (BOOLEAN_SETTINGS.contains(option)) {
                        boolean booleanValue = Boolean.parseBoolean(value);
                        switch (option) {
                            case "shareSeed" -> Fabsservertweaks.CONFIG.shareSeed = booleanValue;
                            case "overrideNormalLead" -> Fabsservertweaks.CONFIG.overrideNormalLead = booleanValue;
                            case "canLeashAnimals" -> Fabsservertweaks.CONFIG.canLeashAnimals = booleanValue;
                            case "canLeashMonsters" -> Fabsservertweaks.CONFIG.canLeashMonsters = booleanValue;
                            case "canLeashBosses" -> Fabsservertweaks.CONFIG.canLeashBosses = booleanValue;
                            case "canLeashVillagerTypes" -> Fabsservertweaks.CONFIG.canLeashVillagerTypes = booleanValue;
                            case "canLeashGolems" -> Fabsservertweaks.CONFIG.canLeashGolems = booleanValue;
                            case "canLeashPets" -> Fabsservertweaks.CONFIG.canLeashPets = booleanValue;
                            default -> {
                                context.getSource().sendFailure(Component.literal("Invalid value: " + booleanValue));
                                return 0;
                            }
                        }
                        AutoConfig.getConfigHolder(ModConfig.class).save();
                        context.getSource().sendSuccess(() -> Component.literal("Setting " + option + " is now " + (booleanValue ? "enabled" : "disabled")), true);
                        return 1;
                    } else if (INTEGER_SETTINGS.contains(option)) {
                        /*int integerValue = Integer.parseInt(value);
                        switch (option) {
                            default -> {
                                context.getSource().sendFailure(Component.literal("Invalid value: " + integerValue));
                                return 0;
                            }
                        }
                        AutoConfig.getConfigHolder(ModConfig.class).save();
                        context.getSource().sendSuccess(() -> Component.literal("Setting " + option + " is now " + integerValue), true);
                        return 1;*/
                    }
                    context.getSource().sendFailure(Component.literal("Invalid option: " + option));
                    return 0;
                });
        RequiredArgumentBuilder<CommandSourceStack, String> optionArgument = Commands.argument("option", StringArgumentType.word())
                .suggests(optionProvider)
                .then(valueArgument)
                .executes(context -> {
                    String option = StringArgumentType.getString(context, "option");
                    if (ALL_SETTINGS.contains(option)) {
                        String value = switch (option) {
                            case "difficultyType" -> Fabsservertweaks.CONFIG.difficulty.getSerializedName();
                            case "cropTrampleMode" -> Fabsservertweaks.CONFIG.cropTrampleMode.getSerializedName();
                            case "eggTrampleMode" -> Fabsservertweaks.CONFIG.eggTrampleMode.getSerializedName();
                            case "shareSeed" -> Boolean.toString(Fabsservertweaks.CONFIG.shareSeed);
                            case "overrideNormalLead" -> Boolean.toString(Fabsservertweaks.CONFIG.overrideNormalLead);
                            case "canLeashAnimals" -> Boolean.toString(Fabsservertweaks.CONFIG.canLeashAnimals);
                            case "canLeashMonsters" -> Boolean.toString(Fabsservertweaks.CONFIG.canLeashMonsters);
                            case "canLeashBosses" -> Boolean.toString(Fabsservertweaks.CONFIG.canLeashBosses);
                            case "canLeashVillagerTypes" -> Boolean.toString(Fabsservertweaks.CONFIG.canLeashVillagerTypes);
                            case "canLeashGolems" -> Boolean.toString(Fabsservertweaks.CONFIG.canLeashGolems);
                            case "canLeashPets" -> Boolean.toString(Fabsservertweaks.CONFIG.canLeashPets);
                            default -> "";
                        };
                        if (value.isBlank()) {
                            context.getSource().sendFailure(Component.literal("Couldn't get value from option: " + option));
                            return 0;
                        }
                        context.getSource().sendSuccess(() -> Component.literal("Value of option '" + option + "' is: " + value), true);
                        return 1;
                    }
                    context.getSource().sendFailure(Component.literal("Invalid option: " + option));
                    return 0;
                });
        LiteralArgumentBuilder<CommandSourceStack> configArgument = Commands.literal("config").requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(optionArgument);
        LiteralArgumentBuilder<CommandSourceStack> seedArgument = Commands.literal("seed")
                .requires(commandSourceStack -> Fabsservertweaks.CONFIG.shareSeed || commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .executes(context -> {
                    long seed = context.getSource().getLevel().getSeed();
                    context.getSource().sendSuccess(() -> Component.translatable("Server Seed: [%s]", Component.literal(String.valueOf(seed)).withStyle(ChatFormatting.GREEN)).withStyle(style -> style.withClickEvent(new CopyToClipboard(String.valueOf(seed)))), true);
                    return 1;
                });
        RequiredArgumentBuilder<CommandSourceStack, Integer> levelArgument = Commands.argument("level", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    Reference<Enchantment> enchantment = ResourceArgument.getEnchantment(context, "enchantment");
                    ServerPlayer player = context.getSource().getPlayer();
                    return enchant(context, player, enchantment, IntegerArgumentType.getInteger(context, "level"));
                });
        LiteralArgumentBuilder<CommandSourceStack> removeArgument = Commands.literal("remove").executes(context -> {
            Reference<Enchantment> enchantment = ResourceArgument.getEnchantment(context, "enchantment");
            ServerPlayer player = context.getSource().getPlayer();
            if (player != null) {
                ItemStack itemInHand = player.getMainHandItem();
                if (itemInHand.isEmpty()) {
                    context.getSource().sendFailure(Component.literal("You must be holding an item in your hand to use this command."));
                    return 0;
                }
                if (!EnchantmentUtil.hasEnchantment(player, itemInHand, enchantment.key())) {
                    context.getSource().sendFailure(Component.literal("You don't have this enchantment on your item."));
                    return 0;
                }
                EnchantmentHelper.updateEnchantments(itemInHand, mutable -> mutable.set(enchantment, 0));
                context.getSource().sendSuccess(() -> Component.literal("Successfully removed " + enchantment.value().description().getString() + " from " + itemInHand.getItemName().getString()), true);
                return 1;
            }
            context.getSource().sendFailure(Component.literal("Must be executed by a player."));
            return 0;
        });
        RequiredArgumentBuilder<CommandSourceStack, Reference<Enchantment>> enchantmentArgument = Commands.argument("enchantment", ResourceArgument.resource(commandBuildContext, Registries.ENCHANTMENT))
                .then(levelArgument)
                .then(removeArgument)
                .executes(context -> {
                    Reference<Enchantment> enchantment = ResourceArgument.getEnchantment(context, "enchantment");
                    ServerPlayer player = context.getSource().getPlayer();
                    return enchant(context, player, enchantment, 1);
                });
        LiteralArgumentBuilder<CommandSourceStack> enchantArgument = Commands.literal("enchant")
                .requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(enchantmentArgument);
        LiteralArgumentBuilder<CommandSourceStack> featureArgument = Commands.literal("feature")
                .requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("feature", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (BuiltinDatapack builtinDatapack : BuiltinDatapack.values()) {
                                builder.suggest(builtinDatapack.toString());
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                BuiltinDatapack builtinDatapack = BuiltinDatapack.valueOf(StringArgumentType.getString(context, "feature"));
                                boolean enabled = BuiltinDatapackUtil.isEnabled(context.getSource().getServer(), builtinDatapack);
                                CompletableFuture<Void> reloadFuture = enabled
                                        ? BuiltinDatapackUtil.disable(context.getSource().getServer(), builtinDatapack)
                                        : BuiltinDatapackUtil.enable(context.getSource().getServer(), builtinDatapack);

                                reloadFuture.whenComplete((result, throwable) -> context.getSource().getServer().execute(() -> {
                                    if (throwable != null) {
                                        context.getSource().sendFailure(Component.literal("Failed to reload datapacks: " + throwable.getMessage()));
                                        return;
                                    }

                                    if (enabled) {
                                        context.getSource().sendSuccess(() -> Component.translatable("Disabled feature: %s", builtinDatapack.toString()).withStyle(ChatFormatting.GREEN), true);
                                    } else {
                                        context.getSource().sendSuccess(() -> Component.translatable("Enabled feature: %s", builtinDatapack.toString()).withStyle(ChatFormatting.GREEN), true);
                                    }
                                    if (builtinDatapack.requiresRestart()) {
                                        context.getSource().sendSuccess(() -> Component.literal("Server restart required to apply changes").withStyle(ChatFormatting.RED), true);
                                    }
                                }));
                                return 1;
                            } catch (IllegalArgumentException ignored) {}
                            context.getSource().sendFailure(Component.translatable("Invalid feature: %s", StringArgumentType.getString(context, "feature")));
                            return 0;
                        })
                );

        commandDispatcher.register(
                Commands.literal("fabs")
                        .then(configArgument)
                        .then(seedArgument)
                        .then(enchantArgument)
                        .then(featureArgument)
        );

        LogUtil.log("Server tweaks command registered");
    }

    private static int enchant(CommandContext<CommandSourceStack> context, ServerPlayer player, Reference<Enchantment> enchantment, int level) {
        if (player != null) {
            ItemStack itemInHand = player.getMainHandItem();
            if (itemInHand.isEmpty()) {
                context.getSource().sendFailure(Component.literal("You must be holding an item in your hand to use this command."));
                return 0;
            }
            /*if (EnchantmentUtil.getEnchantmentLevel(player, itemInHand, enchantment.key()) >= level) {
                context.getSource().sendFailure(Component.literal("You can't apply the same or lower level of existing enchantment to this item."));
                return 0;
            }*/
            EnchantmentHelper.updateEnchantments(itemInHand, mutable -> mutable.set(enchantment, level));
            context.getSource().sendSuccess(() -> Component.literal("Successfully enchanted " + itemInHand.getItemName().getString() + " with " + enchantment.value().description().getString() + " " + level), true);
            return 1;
        }
        context.getSource().sendFailure(Component.literal("Must be executed by a player."));
        return 0;
    }
}
