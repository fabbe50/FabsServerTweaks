package com.fabbe50.fabsservertweaks.commands;

import com.fabbe50.fabsservertweaks.LogUtil;
import com.fabbe50.fabsservertweaks.data.nickname.NicknameRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.world.entity.player.Player;

public class NicknameCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("nickname")
                        .then(Commands.argument("nickname", StringArgumentType.word()).executes(context -> {
                            String nickname = StringArgumentType.getString(context, "nickname");
                            Player player = context.getSource().getPlayer();
                            if (player != null) {
                                NicknameRegistry.setNickname(player, nickname, ChatFormatting.RESET);
                                return 1;
                            }
                            return 0;
                        }).then(Commands.argument("color", ColorArgument.color()).executes(context -> {
                            String nickname = StringArgumentType.getString(context, "nickname");
                            Player player = context.getSource().getPlayer();
                            if (player != null) {
                                NicknameRegistry.setNickname(player, nickname, ColorArgument.getColor(context, "color"));
                                return 1;
                            }
                            return 0;
                        }))).then(Commands.literal("clear").executes(context -> {
                            Player player = context.getSource().getPlayer();
                            if (player != null) {
                                NicknameRegistry.clearNickname(player);
                                return 1;
                            }
                            return 0;
                        }))
        );

        LogUtil.log("Nickname command registered");
    }
}
