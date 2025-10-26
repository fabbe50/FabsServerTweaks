package com.fabbe50.fabsservertweaks.util;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {
    public static List<BlockPos> getBlocksInRadius(BlockPos center, int radius) {
        List<BlockPos> blocks = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        int rSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= rSq) {
                    blocks.add(new BlockPos(cx + x, cy, cz + z));
                }
            }
        }

        return blocks;
    }

    public static List<BlockPos> getBlocksInSphericalRadius(BlockPos center, int radius) {
        List<BlockPos> blocks = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        int rSq = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= rSq) {
                        blocks.add(new BlockPos(cx + x, cy + y, cz + z));
                    }
                }
            }
        }

        return blocks;
    }
}
