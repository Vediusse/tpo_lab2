package com.example.lab2.unit.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.SecFunction;
import com.example.lab2.function.trig.SinFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecFunctionTest {

    private final SecFunction function = new SecFunction(new CosFunction(new SinFunction(new TaylorSinCalculator())));

    @Test
    void shouldCalculateSec() {
        assertThat(function.calculate(-0.6, 1E-10)).isCloseTo(1.0 / FastMath.cos(-0.6), within(1E-8));
    }

    @Test
    void shouldRejectBreakPoint() {
        assertThatThrownBy(() -> function.calculate(Math.PI / 2.0, 1E-8))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("sec");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
