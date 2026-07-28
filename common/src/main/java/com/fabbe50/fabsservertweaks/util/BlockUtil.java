package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtil {
    public static Pair<Item, Integer> getSpecialDropsAmount(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack toolStack) {
        if (state.is(Blocks.ANCIENT_DEBRIS) && ModGameRules.getGameRuleBoolean(level, ModGameRules.RULE_FORTUNE_ANCIENT_DEBRIS)) {
            if (EnchantmentUtil.hasSilkTouch(player, toolStack)) {
                return null;
            }
            int amount = WorldUtil.getAncientDebrisDrops(level, pos, player, toolStack);
            return Pair.of(Items.NETHERITE_SCRAP, amount);
        }

        return null;
    }
}
