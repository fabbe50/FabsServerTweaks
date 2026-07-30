package com.fabbe50.fabsservertweaks.data.stats;

import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerOnline(UUID playerID, Identifier dimension, Vec3 position, LocalDateTime lastSeen) {
}
