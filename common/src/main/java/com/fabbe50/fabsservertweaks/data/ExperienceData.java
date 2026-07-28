package com.fabbe50.fabsservertweaks.data;

import com.fabbe50.fabsservertweaks.ModPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExperienceData {
    private static final List<BlockRecord> BLOCKS = new ArrayList<>();

    static {
        add(Blocks.COAL_ORE, 0, 2);
        add(Blocks.DEEPSLATE_COAL_ORE, 0, 2);
        add(Blocks.COPPER_ORE, 0, 1);
        add(Blocks.DEEPSLATE_COPPER_ORE, 0, 1);
        add(Blocks.IRON_ORE, 0, 1);
        add(Blocks.DEEPSLATE_IRON_ORE, 0, 1);
        add(Blocks.GOLD_ORE, 0, 1);
        add(Blocks.DEEPSLATE_GOLD_ORE, 0, 1);
        add(Blocks.NETHER_GOLD_ORE, 0, 1);
        add(Blocks.LAPIS_ORE, 2, 5);
        add(Blocks.DEEPSLATE_LAPIS_ORE, 2, 5);
        add(Blocks.REDSTONE_ORE, 1, 5);
        add(Blocks.DEEPSLATE_REDSTONE_ORE, 1, 5);
        add(Blocks.DIAMOND_ORE, 3, 7);
        add(Blocks.DEEPSLATE_DIAMOND_ORE, 3, 7);
        add(Blocks.EMERALD_ORE, 3, 7);
        add(Blocks.DEEPSLATE_EMERALD_ORE, 3, 7);
        add(Blocks.NETHER_QUARTZ_ORE, 2, 5);
        add(Blocks.ANCIENT_DEBRIS, 5, 15);
        add(Blocks.SPAWNER, 15, 45);
        add(Blocks.SCULK, 1, 1);
        add(Blocks.SCULK_SENSOR, 5, 5);
        add(Blocks.CALIBRATED_SCULK_SENSOR, 5, 5);
        add(Blocks.SCULK_CATALYST, 5, 5);
        add(Blocks.SCULK_SHRIEKER, 5, 5);

        BLOCKS.addAll(ModPlatform.getExperienceData());
    }

    public static int getExp(Block block) {
        Random random = new Random();
        for (BlockRecord record : BLOCKS) {
            if (record.block() == block) {
                return record.minExp() + random.nextInt(record.maxExp() - record.minExp() + 1);
            }
        }
        return 0;
    }

    private static void add(Block block, int minExp, int maxExp) {
        BLOCKS.add(new BlockRecord(block, minExp, maxExp));
    }

    public record BlockRecord(Block block, int minExp, int maxExp) {

    }
}
