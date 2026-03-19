package com.example.lab2.factory;

import com.example.lab2.approximation.LnSeriesCalculator;
import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.log.LnFunction;
import com.example.lab2.function.log.LogNFunction;
import com.example.lab2.function.system.FunctionSystem;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.CotFunction;
import com.example.lab2.function.trig.CscFunction;
import com.example.lab2.function.trig.SecFunction;
import com.example.lab2.function.trig.SinFunction;
import com.example.lab2.function.trig.TanFunction;
import com.example.lab2.model.MathFunction;

/**
 * Factory for assembling production implementations of the function system.
 */
public final class FunctionFactory {

    private FunctionFactory() {
    }

    public static MathFunction createSinFunction() {
        return new SinFunction(new TaylorSinCalculator());
    }

    public static MathFunction createLnFunction() {
        return new LnFunction(new LnSeriesCalculator());
    }

    public static FunctionSystem createFunctionSystem() {
        MathFunction sin = createSinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction sec = new SecFunction(cos);
        MathFunction csc = new CscFunction(sin);
        MathFunction ln = createLnFunction();
        MathFunction log2 = new LogNFunction(2.0, ln);
        MathFunction log3 = new LogNFunction(3.0, ln);
        MathFunction log5 = new LogNFunction(5.0, ln);

        return new FunctionSystem(cos, tan, sec, csc, ln, log2, log3, log5);
    }
}
