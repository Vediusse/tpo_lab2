package com.example.lab2.unit.util;

import com.example.lab2.util.DoubleComparator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoubleComparatorTest {

    @Test
    void shouldAcceptPositiveFiniteEps() {
        assertThatCode(() -> DoubleComparator.requirePositiveEps(1E-6)).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectNaNEps() {
        assertThatThrownBy(() -> DoubleComparator.requirePositiveEps(Double.NaN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eps");
    }

    @Test
    void shouldRejectInfiniteEps() {
        assertThatThrownBy(() -> DoubleComparator.requirePositiveEps(Double.POSITIVE_INFINITY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eps");
    }

    @Test
    void shouldRejectNonPositiveEps() {
        assertThatThrownBy(() -> DoubleComparator.requirePositiveEps(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eps");
    }

    @Test
    void shouldCompareValuesWithTolerance() {
        assertThat(DoubleComparator.isClose(1.0, 1.0001, 0.001)).isTrue();
        assertThat(DoubleComparator.isClose(1.0, 1.1, 0.001)).isFalse();
    }

    @Test
    void shouldDetectNearZeroValues() {
        assertThat(DoubleComparator.isZero(1E-7, 1E-6)).isTrue();
        assertThat(DoubleComparator.isZero(1E-2, 1E-6)).isFalse();
    }
}
