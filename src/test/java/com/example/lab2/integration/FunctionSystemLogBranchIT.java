package com.example.lab2.integration;

import com.example.lab2.approximation.LnSeriesCalculator;
import com.example.lab2.function.log.LnFunction;
import com.example.lab2.function.log.LogNFunction;
import com.example.lab2.function.system.FunctionSystem;
import com.example.lab2.model.MathFunction;
import com.example.lab2.stub.TableFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionSystemLogBranchIT {

    private static final double EPS = 1E-8;

    @Test
    void shouldIntegrateLogModulesWithStubbedTrigFunctions() {
        MathFunction stubCos = TableFunction.fromCsv(Path.of("src/test/resources/tables/trig-cos.csv"));
        MathFunction stubTan = TableFunction.fromCsv(Path.of("src/test/resources/tables/trig-tan.csv"));
        MathFunction stubSec = (x, eps) -> 0.0;
        MathFunction stubCsc = (x, eps) -> 0.0;
        LnFunction ln = new LnFunction(new LnSeriesCalculator());
        LogNFunction log2 = new LogNFunction(2.0, ln);
        LogNFunction log3 = new LogNFunction(3.0, ln);
        LogNFunction log5 = new LogNFunction(5.0, ln);

        FunctionSystem system = new FunctionSystem(stubCos, stubTan, stubSec, stubCsc, ln, log2, log3, log5);
        double x = 2.5;

        double actual = system.calculate(x, EPS);
        double log2Value = FastMath.log(x) / FastMath.log(2.0);
        double expected = FastMath.pow(FastMath.pow(FastMath.pow(log2Value, 3), 2) + log2Value, 2)
                - ((FastMath.log(x) / FastMath.log(5.0))
                + FastMath.log(x) * (FastMath.pow(FastMath.log(x) / FastMath.log(3.0), 2) + FastMath.pow(log2Value, 3)));

        assertThat(actual).isCloseTo(expected, within(1E-5));
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
