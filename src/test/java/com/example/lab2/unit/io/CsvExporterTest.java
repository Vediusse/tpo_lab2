package com.example.lab2.unit.io;

import com.example.lab2.io.CsvExporter;
import com.example.lab2.model.SampledPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvExporterTest {

    @TempDir
    Path tempDir;

    private final CsvExporter exporter = new CsvExporter();

    @Test
    void shouldCreateDirectoriesAndWriteCsvInUtf8() throws Exception {
        Path file = tempDir.resolve("nested/output/data.csv");
        exporter.export(file, List.of(new SampledPoint(1.0, 2.0)));

        assertThat(file).exists();
        assertThat(Files.readString(file, StandardCharsets.UTF_8)).startsWith("x,result");
    }

    @Test
    void shouldSupportPathWithoutParent() throws Exception {
        Path file = Path.of("csv-export-test.csv");
        try {
            exporter.export(file, List.of(new SampledPoint(0.0, 0.0)));

            assertThat(file).exists();
            assertThat(Files.readAllLines(file)).containsExactly("x,result", "0.0,0.0");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void shouldExportHeaderForEmptyPoints() throws Exception {
        Path file = tempDir.resolve("empty.csv");
        exporter.export(file, List.of());

        assertThat(Files.readAllLines(file)).containsExactly("x,result");
    }

    @Test
    void shouldThrowWhenTargetIsDirectory() {
        assertThatThrownBy(() -> exporter.export(tempDir, List.of(new SampledPoint(1.0, 2.0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to export CSV");
    }

    @Test
    void shouldThrowWhenParentCannotBeCreated() throws Exception {
        Path fileParent = Files.createTempFile(tempDir, "parent", ".tmp");
        Path invalidTarget = fileParent.resolve("child.csv");

        assertThatThrownBy(() -> exporter.export(invalidTarget, List.of(new SampledPoint(1.0, 2.0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to export CSV");
    }
}
