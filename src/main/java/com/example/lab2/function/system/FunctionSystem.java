package com.example.lab2.function.system;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;

/**
 * Piecewise system of trigonometric and logarithmic functions.
 */
public class FunctionSystem implements MathFunction {

    private final MathFunction cosFunction;
    private final MathFunction tanFunction;
    private final MathFunction secFunction;
    private final MathFunction cscFunction;
    private final MathFunction lnFunction;
    private final MathFunction log2Function;
    private final MathFunction log3Function;
    private final MathFunction log5Function;

    public FunctionSystem(
            MathFunction cosFunction,
            MathFunction tanFunction,
            MathFunction secFunction,
            MathFunction cscFunction,
            MathFunction lnFunction,
            MathFunction log2Function,
            MathFunction log3Function,
            MathFunction log5Function
    ) {
        this.cosFunction = cosFunction;
        this.tanFunction = tanFunction;
        this.secFunction = secFunction;
        this.cscFunction = cscFunction;
        this.lnFunction = lnFunction;
        this.log2Function = log2Function;
        this.log3Function = log3Function;
        this.log5Function = log5Function;
    }

    @Override
    public double calculate(double x, double eps) {
        DoubleComparator.requirePositiveEps(eps);
        if (x <= 0) {
            return calculateTrigBranch(x, eps);
        }
        return calculateLogBranch(x, eps);
    }

    private double calculateTrigBranch(double x, double eps) {
        double tan = tanFunction.calculate(x, eps);
        double cos = cosFunction.calculate(x, eps);
        double sec = secFunction.calculate(x, eps);
        double csc = cscFunction.calculate(x, eps);

        double denominator = csc * tan - tan;
        if (DoubleComparator.isZero(denominator, eps)) {
            throw new ArithmeticException("Function system trig branch denominator is zero");
        }

        return (((tan * tan + tan + cos) * sec) / denominator) - cos;
    }

    private double calculateLogBranch(double x, double eps) {
        double log2 = log2Function.calculate(x, eps);
        double log3 = log3Function.calculate(x, eps);
        double log5 = log5Function.calculate(x, eps);
        double ln = lnFunction.calculate(x, eps);

        double log2Cube = log2 * log2 * log2;
        double left = Math.pow(Math.pow(log2Cube, 2.0) + log2, 2.0);
        double right = log5 + (ln * (Math.pow(log3, 2.0) + log2Cube));
        return left - right;
    }
}
