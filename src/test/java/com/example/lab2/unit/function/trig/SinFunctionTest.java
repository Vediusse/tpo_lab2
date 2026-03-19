package com.example.lab2.unit.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.trig.SinFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SinFunctionTest {

    private final SinFunction function = new SinFunction(new TaylorSinCalculator());

    @Test
    void shouldCalculateSin() {
        assertThat(function.calculate(-0.7, 1E-10)).isCloseTo(FastMath.sin(-0.7), within(1E-9));
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
