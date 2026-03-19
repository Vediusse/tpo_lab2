package com.example.lab2.integration;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.system.FunctionSystem;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.CscFunction;
import com.example.lab2.function.trig.SecFunction;
import com.example.lab2.function.trig.SinFunction;
import com.example.lab2.function.trig.TanFunction;
import com.example.lab2.model.MathFunction;
import com.example.lab2.stub.TableFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionSystemTrigBranchIT {

    private static final double EPS = 1E-8;

    @Test
    void shouldIntegrateTrigModulesWithStubbedLogs() {
        SinFunction sin = new SinFunction(new TaylorSinCalculator());
        CosFunction cos = new CosFunction(sin);
        TanFunction tan = new TanFunction(sin, cos);
        SecFunction sec = new SecFunction(cos);
        CscFunction csc = new CscFunction(sin);
        MathFunction stubLn = TableFunction.fromCsv(Path.of("src/test/resources/tables/log2.csv"));
        MathFunction stubLog2 = TableFunction.fromCsv(Path.of("src/test/resources/tables/log2.csv"));
        MathFunction stubLog3 = TableFunction.fromCsv(Path.of("src/test/resources/tables/log3.csv"));
        MathFunction stubLog5 = TableFunction.fromCsv(Path.of("src/test/resources/tables/log5.csv"));

        FunctionSystem system = new FunctionSystem(cos, tan, sec, csc, stubLn, stubLog2, stubLog3, stubLog5);
        double x = -0.7;

        double actual = system.calculate(x, EPS);
        double expected = (((FastMath.pow(FastMath.tan(x), 2) + FastMath.tan(x) + FastMath.cos(x))
                * (1.0 / FastMath.cos(x)))
                / ((1.0 / FastMath.sin(x)) * FastMath.tan(x) - FastMath.tan(x))) - FastMath.cos(x);

        assertThat(actual).isCloseTo(expected, within(1E-6));
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
