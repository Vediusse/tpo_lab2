package com.example.lab2.unit.function.log;

import com.example.lab2.approximation.LnSeriesCalculator;
import com.example.lab2.function.log.LnFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LnFunctionTest {

    private final LnFunction function = new LnFunction(new LnSeriesCalculator());

    @Test
    void shouldCalculateLn() {
        assertThat(function.calculate(4.2, 1E-10)).isCloseTo(FastMath.log(4.2), within(1E-8));
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
