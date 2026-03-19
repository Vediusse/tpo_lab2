package com.example.lab2.function.trig;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;

/**
 * Tangent function derived from sine and cosine.
 */
public class TanFunction implements MathFunction {

    private final MathFunction sinFunction;
    private final MathFunction cosFunction;

    public TanFunction(MathFunction sinFunction, MathFunction cosFunction) {
        this.sinFunction = sinFunction;
        this.cosFunction = cosFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        double sin = sinFunction.calculate(x, eps);
        double cos = cosFunction.calculate(x, eps);
        if (DoubleComparator.isZero(cos, eps)) {
            throw new ArithmeticException("tan(x) is undefined when cos(x) = 0");
        }
        return sin / cos;
    }
}
