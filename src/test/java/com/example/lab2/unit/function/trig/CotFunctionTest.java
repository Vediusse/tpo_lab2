package com.example.lab2.unit.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.CotFunction;
import com.example.lab2.function.trig.SinFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CotFunctionTest {

    private final SinFunction sin = new SinFunction(new TaylorSinCalculator());
    private final CosFunction cos = new CosFunction(sin);
    private final CotFunction function = new CotFunction(sin, cos);

    @Test
    void shouldCalculateCot() {
        assertThat(function.calculate(0.9, 1E-10)).isCloseTo(1.0 / FastMath.tan(0.9), within(1E-8));
    }

    @Test
    void shouldRejectBreakPoint() {
        assertThatThrownBy(() -> function.calculate(Math.PI, 1E-8))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("cot");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
