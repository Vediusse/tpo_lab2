package com.example.lab2.unit.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.trig.CscFunction;
import com.example.lab2.function.trig.SinFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CscFunctionTest {

    private final CscFunction function = new CscFunction(new SinFunction(new TaylorSinCalculator()));

    @Test
    void shouldCalculateCsc() {
        assertThat(function.calculate(0.8, 1E-10)).isCloseTo(1.0 / FastMath.sin(0.8), within(1E-8));
    }

    @Test
    void shouldRejectBreakPoint() {
        assertThatThrownBy(() -> function.calculate(0.0, 1E-8))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("csc");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
