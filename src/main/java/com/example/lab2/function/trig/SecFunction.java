package com.example.lab2.function.trig;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;

/**
 * Secant function derived from cosine.
 */
public class SecFunction implements MathFunction {

    private final MathFunction cosFunction;

    public SecFunction(MathFunction cosFunction) {
        this.cosFunction = cosFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        double cos = cosFunction.calculate(x, eps);
        if (DoubleComparator.isZero(cos, eps)) {
            throw new ArithmeticException("sec(x) is undefined when cos(x) = 0");
        }
        return 1.0 / cos;
    }
}
