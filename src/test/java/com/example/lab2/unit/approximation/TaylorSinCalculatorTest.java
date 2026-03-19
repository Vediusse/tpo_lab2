package com.example.lab2.unit.approximation;

import com.example.lab2.approximation.TaylorSinCalculator;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaylorSinCalculatorTest {

    private final TaylorSinCalculator calculator = new TaylorSinCalculator();

    @Test
    void shouldApproximateSinWithGoodPrecision() {
        double actual = calculator.calculate(1.2, 1E-10);

        assertThat(actual).isCloseTo(FastMath.sin(1.2), within(1E-9));
    }

    @Test
    void shouldWorkForLargeAnglesAfterNormalization() {
        double actual = calculator.calculate(40.0 * Math.PI + 0.3, 1E-10);

        assertThat(actual).isCloseTo(FastMath.sin(0.3), within(1E-9));
    }

    @Test
    void shouldRejectInvalidPrecision() {
        assertThatThrownBy(() -> calculator.calculate(1.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eps");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
