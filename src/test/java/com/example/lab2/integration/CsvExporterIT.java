package com.example.lab2.integration;

import com.example.lab2.factory.FunctionFactory;
import com.example.lab2.io.CsvExporter;
import com.example.lab2.io.FunctionSampler;
import com.example.lab2.model.MathFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvExporterIT {

    @TempDir
    Path tempDir;

    @Test
    void shouldExportSampledPointsToCsv() throws Exception {
        MathFunction sin = FunctionFactory.createSinFunction();
        FunctionSampler sampler = new FunctionSampler();
        CsvExporter exporter = new CsvExporter();

        Path output = tempDir.resolve("sin.csv");
        exporter.export(output, sampler.sample(sin, 0.0, 0.4, 0.2, 1E-8));

        List<String> lines = Files.readAllLines(output);
        assertThat(lines).hasSize(4);
        assertThat(lines.get(0)).isEqualTo("x,result");
        assertThat(lines.get(1)).contains("0.0");
    }
}
