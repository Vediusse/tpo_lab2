package com.example.lab2.unit.io;

import com.example.lab2.io.FunctionSampler;
import com.example.lab2.model.MathFunction;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FunctionSamplerTest {

    private final FunctionSampler sampler = new FunctionSampler();
    private final MathFunction identity = (x, eps) -> x;

    @Test
    void shouldSampleRangeIncludingUpperBound() {
        var points = sampler.sample(identity, 0.0, 0.4, 0.2, 1E-6);

        assertThat(points).hasSize(3);
        assertThat(points.get(0).x()).isEqualTo(0.0);
        assertThat(points.get(2).x()).isCloseTo(0.4, within(1E-12));
    }

    @Test
    void shouldSampleSinglePointRange() {
        var points = sampler.sample(identity, 2.0, 2.0, 0.5, 1E-6);

        assertThat(points).hasSize(1);
        assertThat(points.get(0).result()).isEqualTo(2.0);
    }

    @Test
    void shouldRejectNonPositiveStep() {
        assertThatThrownBy(() -> sampler.sample(identity, 0.0, 1.0, 0.0, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("step");
    }

    @Test
    void shouldRejectInvalidRange() {
        assertThatThrownBy(() -> sampler.sample(identity, 2.0, 1.0, 0.1, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("from");
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
