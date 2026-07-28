package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.data.ExperienceData.BlockRecord;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ModPlatform {
    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        throw new AssertionError();
    }

    public static void registerCompostable(float chance, Item item) {
        registerCompostable(chance, item, false);
    }

    @ExpectPlatform
    public static void registerCompostable(float chance, Item item, boolean canVillagerCompost) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static List<BlockRecord> getExperienceData() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isDataGen() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean breakBlockEvent(Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean placeBlockEvent(Level level, BlockPos pos, BlockState state, Entity placer) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean rightClickBlockEvent(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        throw new AssertionError();
    }
}
