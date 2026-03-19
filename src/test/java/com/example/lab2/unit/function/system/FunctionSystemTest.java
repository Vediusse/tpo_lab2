package com.example.lab2.unit.function.system;

import com.example.lab2.function.system.FunctionSystem;
import com.example.lab2.model.MathFunction;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionSystemTest {

    @Test
    void shouldUseTrigBranchForNonPositiveX() {
        MathFunction cos = (x, eps) -> 2.0;
        MathFunction tan = (x, eps) -> 1.0;
        MathFunction sec = (x, eps) -> 3.0;
        MathFunction csc = (x, eps) -> 5.0;
        MathFunction ln = (x, eps) -> 100.0;
        MathFunction log2 = (x, eps) -> 100.0;
        MathFunction log3 = (x, eps) -> 100.0;
        MathFunction log5 = (x, eps) -> 100.0;

        FunctionSystem system = new FunctionSystem(cos, tan, sec, csc, ln, log2, log3, log5);

        double actual = system.calculate(0.0, 1E-6);

        assertThat(actual).isEqualTo(1.0);
    }

    @Test
    void shouldUseLogBranchForPositiveX() {
        MathFunction cos = (x, eps) -> 100.0;
        MathFunction tan = (x, eps) -> 100.0;
        MathFunction sec = (x, eps) -> 100.0;
        MathFunction csc = (x, eps) -> 100.0;
        MathFunction ln = (x, eps) -> 2.0;
        MathFunction log2 = (x, eps) -> 3.0;
        MathFunction log3 = (x, eps) -> 4.0;
        MathFunction log5 = (x, eps) -> 5.0;

        FunctionSystem system = new FunctionSystem(cos, tan, sec, csc, ln, log2, log3, log5);

        double actual = system.calculate(2.0, 1E-6);

        assertThat(actual).isEqualTo(535733.0);
    }
}
