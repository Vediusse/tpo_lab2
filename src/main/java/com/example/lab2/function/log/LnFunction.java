package com.example.lab2.function.log;

import com.example.lab2.approximation.LnSeriesCalculator;
import com.example.lab2.model.MathFunction;

/**
 * Natural logarithm function based on a series calculator.
 */
public class LnFunction implements MathFunction {

    private final LnSeriesCalculator calculator;

    public LnFunction(LnSeriesCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public double calculate(double x, double eps) {
        return calculator.calculate(x, eps);
    }
}
