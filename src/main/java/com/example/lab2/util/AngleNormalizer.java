package com.example.lab2.util;

/**
 * Normalizes angles to improve convergence of Taylor series.
 */
public final class AngleNormalizer {

    private static final double TWO_PI = 2.0 * Math.PI;

    private AngleNormalizer() {
    }

    /**
     * Normalizes an angle to the interval [-PI, PI].
     *
     * @param x angle in radians
     * @return normalized angle
     */
    public static double normalizeRadians(double x) {
        double normalized = x % TWO_PI;
        if (normalized > Math.PI) {
            normalized -= TWO_PI;
        } else if (normalized < -Math.PI) {
            normalized += TWO_PI;
        }
        return normalized;
    }
}
