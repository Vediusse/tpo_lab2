package com.example.lab2.unit.function.log;

import com.example.lab2.approximation.LnSeriesCalculator;
import com.example.lab2.function.log.LnFunction;
import com.example.lab2.function.log.LogNFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LogNFunctionTest {

    private final LnFunction ln = new LnFunction(new LnSeriesCalculator());

    @Test
    void shouldCalculateLogBase2() {
        LogNFunction function = new LogNFunction(2.0, ln);

        assertThat(function.calculate(8.0, 1E-10)).isCloseTo(3.0, within(1E-8));
    }

    @Test
    void shouldApproximateAnyBase() {
        LogNFunction function = new LogNFunction(3.0, ln);

        assertThat(function.calculate(7.0, 1E-10))
                .isCloseTo(FastMath.log(7.0) / FastMath.log(3.0), within(1E-8));
    }

    @Test
    void shouldRejectNonPositiveX() {
        LogNFunction function = new LogNFunction(2.0, ln);

        assertThatThrownBy(() -> function.calculate(0.0, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("x > 0");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
