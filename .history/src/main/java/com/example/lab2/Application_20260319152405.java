package com.example.lab2;

import com.example.lab2.factory.FunctionFactory;
import com.example.lab2.io.CsvExporter;
import com.example.lab2.io.FunctionSampler;
import com.example.lab2.model.MathFunction;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Example application that generates CSV files for the lab report.
 */
public class Application {

    public static void main(String[] args) {
        run(
                Path.of("output"),
                System.out,
                new FunctionSampler(),
                new CsvExporter(),
                FunctionFactory.createFunctionSystem(),
                FunctionFactory.createSinFunction(),
                FunctionFactory.createLnFunction(),
                1E-8
        );
    }

    static void run(
            Path outputDir,
            PrintStream out,
            FunctionSampler sampler,
            CsvExporter exporter,
            MathFunction system,
            MathFunction sin,
            MathFunction ln,
            double eps
    ) {
        createOutputDirectory(outputDir);

        out.println("Generating CSV...");

        Path sinPath = outputDir.resolve("sin.csv");
        exporter.export(sinPath, sampler.sample(sin, -Math.PI, Math.PI, 0.2, eps));
        out.println("Generated: " + sinPath);

        Path lnPath = outputDir.resolve("ln.csv");
        exporter.export(lnPath, sampler.sample(ln, 0.2, 5.0, 0.2, eps));
        out.println("Generated: " + lnPath);

        // левая часть (x <= 0)
        Path systemLeftPath = outputDir.resolve("system_left.csv");
        exporter.export(systemLeftPath, sampler.sample(system, -1.0, -0.001, 0.001, eps));
        out.println("Generated: " + systemLeftPath);

        // правая часть (x > 0)
        Path systemRightPath = outputDir.resolve("system_right.csv");
        exporter.export(systemRightPath, sampler.sample(system, 0.001, 2.0, 0.001, eps));
        out.println("Generated: " + systemRightPath);

        out.println("Done.");
    }

    static void createOutputDirectory(Path outputDir) {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create output directory: " + outputDir.toAbsolutePath(), e);
        }
    }
}
