package com.example.lab2.model;

/**
 * Common contract for every mathematical function in the project.
 */
public interface MathFunction {

    /**
     * Calculates a function value with the given precision.
     *
     * @param x input value
     * @param eps target precision
     * @return calculated result
     */
    double calculate(double x, double eps);
}
