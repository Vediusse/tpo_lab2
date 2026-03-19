package com.example.lab2.unit.function.trig;

import com.example.lab2.approximation.TaylorSinCalculator;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.CotFunction;
import com.example.lab2.function.trig.CscFunction;
import com.example.lab2.function.trig.SecFunction;
import com.example.lab2.function.trig.SinFunction;
import com.example.lab2.function.trig.TanFunction;
import com.example.lab2.model.MathFunction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TrigFunctionCsvReferenceTest {

    private static final double EPS = 1E-10;

    @ParameterizedTest(name = "{0}")
    @MethodSource("csvReferences")
    void shouldMatchReferenceCsvPoints(String stem, MathFunction function) throws IOException {
        Path path = Path.of("src/test/resources/tables", stem + ".csv");
        var lines = Files.readAllLines(path);
        for (String line : lines.subList(1, lines.size())) {
            String[] parts = line.split(",");
            double x = Double.parseDouble(parts[0]);
            double expected = Double.parseDouble(parts[1]);
            assertThat(function.calculate(x, EPS)).as(stem + " at x=" + x).isCloseTo(expected, within(1E-8));
        }
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> csvReferences() {
        SinFunction sin = new SinFunction(new TaylorSinCalculator());
        CosFunction cos = new CosFunction(sin);
        TanFunction tan = new TanFunction(sin, cos);
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("sin", (MathFunction) sin),
                org.junit.jupiter.params.provider.Arguments.of("cos", (MathFunction) cos),
                org.junit.jupiter.params.provider.Arguments.of("tan", (MathFunction) tan),
                org.junit.jupiter.params.provider.Arguments.of("cot", (MathFunction) new CotFunction(sin, cos)),
                org.junit.jupiter.params.provider.Arguments.of("sec", (MathFunction) new SecFunction(cos)),
                org.junit.jupiter.params.provider.Arguments.of("csc", (MathFunction) new CscFunction(sin))
        );
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}
