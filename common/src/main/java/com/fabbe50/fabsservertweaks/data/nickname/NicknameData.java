package com.fabbe50.fabsservertweaks.data.nickname;

import net.minecraft.ChatFormatting;

import java.util.UUID;

public record NicknameData(UUID playerID, String nickname, ChatFormatting color) {
}
