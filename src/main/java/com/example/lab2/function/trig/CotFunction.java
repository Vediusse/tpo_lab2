package com.example.lab2.function.trig;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;

/**
 * Cotangent function derived from sine and cosine.
 */
public class CotFunction implements MathFunction {

    private final MathFunction sinFunction;
    private final MathFunction cosFunction;

    public CotFunction(MathFunction sinFunction, MathFunction cosFunction) {
        this.sinFunction = sinFunction;
        this.cosFunction = cosFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        double sin = sinFunction.calculate(x, eps);
        double cos = cosFunction.calculate(x, eps);
        if (DoubleComparator.isZero(sin, eps)) {
            throw new ArithmeticException("cot(x) is undefined when sin(x) = 0");
        }
        return cos / sin;
    }
}
