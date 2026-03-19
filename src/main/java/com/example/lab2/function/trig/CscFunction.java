package com.example.lab2.function.trig;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;

/**
 * Cosecant function derived from sine.
 */
public class CscFunction implements MathFunction {

    private final MathFunction sinFunction;

    public CscFunction(MathFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        double sin = sinFunction.calculate(x, eps);
        if (DoubleComparator.isZero(sin, eps)) {
            throw new ArithmeticException("csc(x) is undefined when sin(x) = 0");
        }
        return 1.0 / sin;
    }
}
