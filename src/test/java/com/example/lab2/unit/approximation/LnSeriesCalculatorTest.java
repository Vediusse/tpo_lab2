package com.example.lab2.unit.approximation;

import com.example.lab2.approximation.LnSeriesCalculator;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LnSeriesCalculatorTest {

    private final LnSeriesCalculator calculator = new LnSeriesCalculator();

    @Test
    void shouldApproximateNaturalLogarithm() {
        double actual = calculator.calculate(2.5, 1E-10);

        assertThat(actual).isCloseTo(FastMath.log(2.5), within(1E-9));
    }

    @Test
    void shouldReturnZeroForOne() {
        assertThat(calculator.calculate(1.0, 1E-10)).isZero();
    }

    @Test
    void shouldRejectNonPositiveArguments() {
        assertThatThrownBy(() -> calculator.calculate(0.0, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("x > 0");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
