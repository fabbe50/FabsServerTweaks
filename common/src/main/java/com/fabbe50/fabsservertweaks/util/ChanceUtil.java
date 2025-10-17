package com.fabbe50.fabsservertweaks.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

public class ChanceUtil {
    public static double chanceAtY(LevelAccessor level, int y, double minChance, double maxChance, double exponent) {
        final int minY = level.getMinY();
        final int maxY = level.getMaxY() - 1;

        if (maxY <= minY) return minChance; // safety

        // Normalize Y into [0,1] from bottom to top
        double t = (y - minY) / (double) (maxY - minY);
        t = Mth.clamp(t, 0.0, 1.0);

        // Flip so bottom=1, top=0, then apply curve
        double bottomBias = 1.0 - t;
        double curved = Math.pow(bottomBias, Math.max(0.0001, exponent));

        // Lerp from minChance (top) → maxChance (bottom)
        return Mth.lerp(curved, minChance, maxChance);
    }

    /** Convenience roll: returns true with the computed probability. */
    public static boolean rollAtY(LevelAccessor level, int y, double minChance, double maxChance, double exponent, RandomSource rng) {
        return rng.nextDouble() < chanceAtY(level, y, minChance, maxChance, exponent);
    }
}
