package com.example.lab2.unit.util;

import com.example.lab2.util.AngleNormalizer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AngleNormalizerTest {

    @Test
    void shouldKeepAngleInsideRangeUnchanged() {
        assertThat(AngleNormalizer.normalizeRadians(1.0)).isEqualTo(1.0);
    }

    @Test
    void shouldNormalizeAngleGreaterThanPi() {
        assertThat(AngleNormalizer.normalizeRadians(1.5 * Math.PI))
                .isCloseTo(-Math.PI / 2.0, within(1E-12));
    }

    @Test
    void shouldNormalizeAngleLessThanMinusPi() {
        assertThat(AngleNormalizer.normalizeRadians(-1.5 * Math.PI))
                .isCloseTo(Math.PI / 2.0, within(1E-12));
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
