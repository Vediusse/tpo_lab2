package com.example.lab2.function.trig;

import com.example.lab2.model.MathFunction;

/**
 * Cosine function derived from sine.
 */
public class CosFunction implements MathFunction {

    private final MathFunction sinFunction;

    public CosFunction(MathFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        return sinFunction.calculate(x + Math.PI / 2.0, eps);
    }
}
