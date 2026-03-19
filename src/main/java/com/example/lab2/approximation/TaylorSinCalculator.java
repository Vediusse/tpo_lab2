package com.example.lab2.approximation;

import com.example.lab2.util.AngleNormalizer;
import com.example.lab2.util.DoubleComparator;

/**
 * Calculates sin(x) using the Taylor series.
 */
public class TaylorSinCalculator {

    /**
     * Calculates sin(x) with the requested precision.
     *
     * @param x angle in radians
     * @param eps target precision
     * @return approximated sin(x)
     */
    public double calculate(double x, double eps) {
        DoubleComparator.requirePositiveEps(eps);

        double normalizedX = AngleNormalizer.normalizeRadians(x);
        double term = normalizedX;
        double sum = normalizedX;
        int n = 1;

        while (Math.abs(term) > eps) {
            term *= -normalizedX * normalizedX / ((2.0 * n) * (2.0 * n + 1.0));
            sum += term;
            n++;
        }

        return sum;
    }
}
