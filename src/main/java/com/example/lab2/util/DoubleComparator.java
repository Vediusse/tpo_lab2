package com.example.lab2.util;

/**
 * Utility methods for comparing double values with precision.
 */
public final class DoubleComparator {

    private DoubleComparator() {
    }

    /**
     * Checks that the precision value is positive and finite.
     *
     * @param eps precision
     */
    public static void requirePositiveEps(double eps) {
        if (Double.isNaN(eps) || Double.isInfinite(eps) || eps <= 0) {
            throw new IllegalArgumentException("eps must be positive and finite");
        }
    }

    /**
     * Compares numbers with the given tolerance.
     *
     * @param left first value
     * @param right second value
     * @param eps tolerance
     * @return true when the values are close enough
     */
    public static boolean isClose(double left, double right, double eps) {
        requirePositiveEps(eps);
        return Math.abs(left - right) <= eps;
    }

    /**
     * Checks if the value is effectively zero.
     *
     * @param value checked value
     * @param eps tolerance
     * @return true when the value is near zero
     */
    public static boolean isZero(double value, double eps) {
        return isClose(value, 0.0, eps);
    }
}
