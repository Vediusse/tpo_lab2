package com.example.lab2.unit.stub;

import com.example.lab2.stub.TableFunction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.NavigableMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableFunctionTest {

    @Test
    void shouldRejectNullTable() {
        assertThatThrownBy(() -> new TableFunction(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("table points");
    }

    @Test
    void shouldRejectEmptyTable() {
        assertThatThrownBy(() -> new TableFunction(new TreeMap<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("table points");
    }

    @Test
    void shouldReadFunctionFromReader() {
        TableFunction function = TableFunction.fromCsv(new StringReader("""
                x,result
                0.0,0.0
                ignored
                2.0,4.0
                """));

        assertThat(function.calculate(2.0, 1E-6)).isEqualTo(4.0);
    }

    @Test
    void shouldReadFunctionFromPath() {
        TableFunction function = TableFunction.fromCsv(Path.of("src/test/resources/tables/log2.csv"));

        assertThat(function.calculate(2.0, 1E-6)).isEqualTo(1.0);
    }

    @Test
    void shouldThrowWhenPathDoesNotExist() {
        assertThatThrownBy(() -> TableFunction.fromCsv(Path.of("src/test/resources/tables/missing.csv")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to read table CSV");
    }

    @Test
    void shouldThrowWhenReaderFails() {
        Reader delegate = new StringReader("x,result\n1.0,2.0\n");
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return delegate.read(cbuf, off, len);
            }

            @Override
            public void close() throws IOException {
                delegate.close();
                throw new IOException("boom");
            }
        };

        assertThatThrownBy(() -> TableFunction.fromCsv(reader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to read table CSV");
    }

    @Test
    void shouldThrowWhenCsvContainsNoData() {
        assertThatThrownBy(() -> TableFunction.fromCsv(new StringReader("x,result\n")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("table points");
    }

    @Test
    void shouldThrowWhenCsvContainsInvalidNumbers() {
        assertThatThrownBy(() -> TableFunction.fromCsv(new StringReader("x,result\na,b\n")))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void shouldReturnExactValue() {
        TableFunction function = new TableFunction(points(0.0, 0.0, 2.0, 4.0));

        assertThat(function.calculate(2.0, 1E-6)).isEqualTo(4.0);
    }

    @Test
    void shouldInterpolateBetweenNeighbourPoints() {
        TableFunction function = new TableFunction(points(0.0, 0.0, 2.0, 4.0));

        assertThat(function.calculate(1.0, 1E-6)).isEqualTo(2.0);
    }

    @Test
    void shouldThrowOutsideRange() {
        TableFunction function = new TableFunction(points(0.0, 0.0, 2.0, 4.0));

        assertThatThrownBy(() -> function.calculate(3.0, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("outside the table range");
    }

    @Test
    void shouldThrowBelowRange() {
        TableFunction function = new TableFunction(points(0.0, 0.0, 2.0, 4.0));

        assertThatThrownBy(() -> function.calculate(-1.0, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("outside the table range");
    }

    @Test
    void shouldRejectInvalidPrecision() {
        TableFunction function = new TableFunction(points(0.0, 0.0, 2.0, 4.0));

        assertThatThrownBy(() -> function.calculate(1.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("eps");
    }

    private static NavigableMap<Double, Double> points(double x1, double y1, double x2, double y2) {
        NavigableMap<Double, Double> map = new TreeMap<>();
        map.put(x1, y1);
        map.put(x2, y2);
        return map;
    }
}
