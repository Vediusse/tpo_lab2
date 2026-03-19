package com.example.lab2;

import com.example.lab2.factory.FunctionFactory;
import com.example.lab2.io.CsvExporter;
import com.example.lab2.io.FunctionSampler;
import com.example.lab2.model.MathFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Example application that generates CSV files for the lab report.
 */
public class Application {

    public static void main(String[] args) {
        double eps = 1E-8;
        FunctionSampler sampler = new FunctionSampler();
        CsvExporter exporter = new CsvExporter();

        MathFunction system = FunctionFactory.createFunctionSystem();
        MathFunction sin = FunctionFactory.createSinFunction();
        MathFunction ln = FunctionFactory.createLnFunction();

        Path outputDir = Path.of("output");
        createOutputDirectory(outputDir);

        System.out.println("Generating CSV...");

        Path sinPath = outputDir.resolve("sin.csv");
        exporter.export(sinPath, sampler.sample(sin, -Math.PI, Math.PI, 0.2, eps));
        System.out.println("Generated: output/sin.csv");

        Path lnPath = outputDir.resolve("ln.csv");
        exporter.export(lnPath, sampler.sample(ln, 0.2, 5.0, 0.2, eps));
        System.out.println("Generated: output/ln.csv");

        Path systemPath = outputDir.resolve("system.csv");
        exporter.export(systemPath, sampler.sample(system, 0.2, 5.0, 0.2, eps));
        System.out.println("Generated: output/system.csv");

        System.out.println("Done.");
    }

    private static void createOutputDirectory(Path outputDir) {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create output directory: " + outputDir.toAbsolutePath(), e);
        }
    }
}
