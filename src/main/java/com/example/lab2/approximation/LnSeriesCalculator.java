package com.example.lab2.approximation;

import com.example.lab2.util.DoubleComparator;

/**
 * Calculates ln(x) with a converging numerical series.
 */
public class LnSeriesCalculator {

    /**
     * Calculates the natural logarithm.
     *
     * @param x positive argument
     * @param eps target precision
     * @return approximated ln(x)
     */
    public double calculate(double x, double eps) {
        DoubleComparator.requirePositiveEps(eps);
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) is defined only for x > 0");
        }

        if (DoubleComparator.isClose(x, 1.0, eps)) {
            return 0.0;
        }

        double y = (x - 1.0) / (x + 1.0);
        double ySquared = y * y;
        double term = y;
        double sum = 0.0;
        int denominator = 1;

        while (Math.abs(term / denominator) > eps) {
            sum += term / denominator;
            term *= ySquared;
            denominator += 2;
        }

        return 2.0 * sum;
    }
}
