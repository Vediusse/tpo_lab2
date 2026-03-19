package com.example.lab2.unit.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.SinFunction;
import com.example.lab2.function.trig.TanFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TanFunctionTest {

    private final SinFunction sin = new SinFunction(new TaylorSinCalculator());
    private final CosFunction cos = new CosFunction(sin);
    private final TanFunction function = new TanFunction(sin, cos);

    @Test
    void shouldCalculateTan() {
        assertThat(function.calculate(-0.8, 1E-10)).isCloseTo(FastMath.tan(-0.8), within(1E-8));
    }

    @Test
    void shouldRejectBreakPoint() {
        assertThatThrownBy(() -> function.calculate(Math.PI / 2.0, 1E-8))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("tan");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
