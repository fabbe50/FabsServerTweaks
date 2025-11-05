package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.gamerules.DifficultyValue;
import com.fabbe50.fabsservertweaks.registries.gamerules.TrampleValue;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;

public enum Presets implements StringRepresentable {
    DEFAULT(0, "default") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_ENDERMAN).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_CREEPER).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_ZOMBIE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROP_EQUIPABLE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_CROP_TRAMPLE_MODE).setFrom(TrampleValue.create(TrampleValue.TrampleMode.FEATHER_FALLING).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_TURTLE_EGG_TRAMPLE_MODE).setFrom(TrampleValue.create(TrampleValue.TrampleMode.FEATHER_FALLING).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKERS_RANDOM_COLOR).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKERS_CAN_TELEPORT).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT).set(2, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SPAWN_WITH_EFFECT_MODE).setFrom(DifficultyValue.create(DifficultyValue.Difficulty.SCALE_BY_DIFFICULTY).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_FRIENDLY_PHANTOMS).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_COBBLE_GEN).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHARE_SEED).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_BETTER_HOES).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_RAIN_FILLS_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_FILLS_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_BETTER_RAIL_PLACEMENT).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_SOLICITING_SIGN).set(true, minecraftServer);
            return true;
        }
    },
    VANILLA(1, "vanilla") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_ENDERMAN).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_CREEPER).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_ZOMBIE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROP_EQUIPABLE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_CROP_TRAMPLE_MODE).setFrom(TrampleValue.create(TrampleValue.TrampleMode.NORMAL).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_TURTLE_EGG_TRAMPLE_MODE).setFrom(TrampleValue.create(TrampleValue.TrampleMode.NORMAL).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKERS_RANDOM_COLOR).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKERS_CAN_TELEPORT).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT).set(0, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SPAWN_WITH_EFFECT_MODE).setFrom(DifficultyValue.create(DifficultyValue.Difficulty.SCALE_BY_DIFFICULTY).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_FRIENDLY_PHANTOMS).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_COBBLE_GEN).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHARE_SEED).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_BETTER_HOES).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_RAIN_FILLS_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_FILLS_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_BETTER_RAIL_PLACEMENT).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_SOLICITING_SIGN).set(false, minecraftServer);
            return true;
        }
    },
    FABS(2, "fabs_choice") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_ENDERMAN).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_CREEPER).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_GRIEF_ZOMBIE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROP_EQUIPABLE).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROP_FULL_DURABILITY).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOB_DROPS_REQUIRE_PLAYER_KILL).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_CROP_TRAMPLE_MODE).setFrom(TrampleValue.create(TrampleValue.TrampleMode.FEATHER_FALLING).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_TURTLE_EGG_TRAMPLE_MODE).setFrom(TrampleValue.create(TrampleValue.TrampleMode.FEATHER_FALLING).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKERS_RANDOM_COLOR).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKERS_CAN_TELEPORT).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHULKER_SHELL_DROP_AMOUNT).set(2, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LOYALTY_TRIDENT_RETURNS_FROM_VOID).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_ALWAYS_CONVERT_VILLAGERS).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_MOBS_SPAWN_WITH_EFFECTS).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SPAWN_WITH_EFFECT_MODE).setFrom(DifficultyValue.create(DifficultyValue.Difficulty.SCALE_BY_DIFFICULTY).createRule(), minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_FRIENDLY_PHANTOMS).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_COBBLE_GEN).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SHARE_SEED).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_BETTER_HOES).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_RAIN_FILLS_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_FILLS_CAULDRON).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_BETTER_RAIL_PLACEMENT).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_SURVIVE_ON_ICE).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_SOLICITING_SIGN).set(true, minecraftServer);
            return true;
        }
    },
    NO_RENEWABLE(3, "no_renewable") {
        @Override
        public boolean adjustRules(ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            serverLevel.getGameRules().getRule(ModGameRules.RULE_NO_COBBLE_GEN).set(true, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_WATER_DRIPSTONE_FILL_CAULDRON).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_LAVA_DRIPSTONE_FILL_CAULDRON).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_RAIN_FILLS_CAULDRON).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_FILLS_CAULDRON).set(false, minecraftServer);
            serverLevel.getGameRules().getRule(ModGameRules.RULE_SNOW_GOLEMS_GENERATE_SNOW).set(false, minecraftServer);
            return true;
        }
    };

    public static final EnumCodec<Presets> CODEC = StringRepresentable.fromEnum(Presets::values);
    private static final IntFunction<Presets> BY_ID = ByIdMap.continuous(Presets::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Presets> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Presets::getId);

    private final int id;
    private final String name;

    Presets(int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public boolean adjustRules(ServerLevel serverLevel) {
        throw new RuntimeException("Preset without adjustments. This shouldn't happen.");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Collection<String> getNames() {
        return Arrays.stream(values()).map(Presets::getName).toList();
    }

    public static Presets byName(String name) {
        return byName(name, DEFAULT);
    }

    public static Presets byName(String name, Presets presets) {
        Presets newPreset = CODEC.byName(name);
        return newPreset != null ? newPreset : presets;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.getName();
    }
}
