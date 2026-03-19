package com.example.lab2.function.log;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;

/**
 * Logarithm with an arbitrary base defined through ln(x).
 */
public class LogNFunction implements MathFunction {

    private final double base;
    private final MathFunction lnFunction;

    public LogNFunction(double base, MathFunction lnFunction) {
        if (base <= 0 || base == 1.0) {
            throw new IllegalArgumentException("log base must be positive and not equal to 1");
        }
        this.base = base;
        this.lnFunction = lnFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        if (x <= 0) {
            throw new IllegalArgumentException("log_a(x) is defined only for x > 0");
        }
        double lnBase = lnFunction.calculate(base, eps);
        if (DoubleComparator.isZero(lnBase, eps)) {
            throw new ArithmeticException("log base produced zero denominator");
        }
        return lnFunction.calculate(x, eps) / lnBase;
    }
}
