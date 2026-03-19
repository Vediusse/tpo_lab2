package com.example.lab2.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.model.MathFunction;

/**
 * Sine function based on Taylor approximation.
 */
public class SinFunction implements MathFunction {

    private final TaylorSinCalculator calculator;

    public SinFunction(TaylorSinCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public double calculate(double x, double eps) {
        return calculator.calculate(x, eps);
    }
}
