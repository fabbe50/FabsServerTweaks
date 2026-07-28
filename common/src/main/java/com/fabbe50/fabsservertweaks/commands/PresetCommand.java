package com.fabbe50.fabsservertweaks.commands;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.ChangedSettingResult;
import com.fabbe50.fabsservertweaks.data.Presets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;

import java.util.concurrent.CompletableFuture;

public class PresetCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        SuggestionProvider<CommandSourceStack> presetSuggestionProvider = (context, builder) -> {
            Presets.getNames().forEach(builder::suggest);
            return CompletableFuture.supplyAsync(() -> Suggestions.create("preset", builder.build().getList()));
        };

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("preset")
                .requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("preset", StringArgumentType.word())
                .suggests(presetSuggestionProvider)
                .executes(context -> {
                    String presetString = StringArgumentType.getString(context, "preset");
                    Presets presets = Presets.byName(presetString);
                    ServerLevel level = context.getSource().getLevel();
                    ChangedSettingResult result = presets.adjustRules(level);
                    if (result.equals(ChangedSettingResult.NO_RESTART)) {
                        context.getSource().sendSuccess(() -> Component.literal("Successfully adjusted rules using preset: " + presets.getName()), true);
                        return presets.getId();
                    } else if (result.equals(ChangedSettingResult.RESTART_REQUIRED)) {
                        context.getSource().sendSuccess(() -> Component.translatable("Successfully adjusted rules using preset: " + presets.getName() + ", %s", Component.literal("restart required").withStyle(ChatFormatting.RED)), true);
                        return presets.getId();
                    }
                    context.getSource().sendFailure(Component.literal("Failed to adjust rules."));
                    return -1;
                }));

        commandDispatcher.register(command);

        LogUtil.log("Preset command registered");
    }
}
