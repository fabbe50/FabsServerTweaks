package com.fabbe50.fabsservertweaks.fabric;

import com.fabbe50.fabsservertweaks.data.ExperienceData.BlockRecord;
import com.fabbe50.fabsservertweaks.fabric.plugins.Plugins;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ingredient), output);
        builder.registerPotionRecipe(Potions.WATER, Ingredient.of(ingredient), Potions.MUNDANE);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        builder.registerPotionRecipe(input, Ingredient.of(ingredient), output);
    }

    public static void registerCompostable(float chance, Item item, boolean canVillagerCompost) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }

    public static List<BlockRecord> getExperienceData() {
        return List.of();
    }

    public static boolean isDataGen() {
        return false;
    }

    public static boolean breakBlockEvent(Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        return Plugins.breakBlockEvent(level, pos, state, player);
    }

    public static boolean placeBlockEvent(Level level, BlockPos pos, BlockState state, Entity placer) {
        return Plugins.placeBlockEvent(level, pos, state, placer);
    }

    public static boolean rightClickBlockEvent(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        return Plugins.rightClickBlockEvent(player, hand, pos, face);
    }
}
