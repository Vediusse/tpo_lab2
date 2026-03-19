package com.example.lab2.integration;

import com.example.lab2.factory.FunctionFactory;
import com.example.lab2.function.system.FunctionSystem;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionSystemFullIT {

    private static final double EPS = 1E-8;

    @Test
    void shouldCalculateWholeSystemWithRealModules() {
        FunctionSystem system = FunctionFactory.createFunctionSystem();

        double negativeX = -0.8;
        double positiveX = 2.0;

        assertThat(system.calculate(negativeX, EPS)).isCloseTo(expectedTrig(negativeX), within(1E-6));
        assertThat(system.calculate(positiveX, EPS)).isCloseTo(expectedLog(positiveX), within(1E-6));
    }

    private double expectedTrig(double x) {
        return (((FastMath.pow(FastMath.tan(x), 2) + FastMath.tan(x) + FastMath.cos(x))
                * (1.0 / FastMath.cos(x)))
                / ((1.0 / FastMath.sin(x)) * FastMath.tan(x) - FastMath.tan(x))) - FastMath.cos(x);
    }

    private double expectedLog(double x) {
        double log2 = FastMath.log(x) / FastMath.log(2.0);
        double log3 = FastMath.log(x) / FastMath.log(3.0);
        double log5 = FastMath.log(x) / FastMath.log(5.0);
        double ln = FastMath.log(x);
        return FastMath.pow(FastMath.pow(FastMath.pow(log2, 3), 2) + log2, 2)
                - (log5 + (ln * (FastMath.pow(log3, 2) + FastMath.pow(log2, 3))));
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
