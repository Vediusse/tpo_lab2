package com.example.lab2;

import com.example.lab2.factory.FunctionFactory;
import com.example.lab2.io.CsvExporter;
import com.example.lab2.io.FunctionSampler;
import com.example.lab2.model.MathFunction;
import com.example.lab2.model.SampledPoint;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
                1E-10
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
        exporter.export(sinPath, sampler.sample(sin, -Math.PI, Math.PI, 0.00005, eps));
        out.println("Generated: " + sinPath);

        Path lnPath = outputDir.resolve("ln.csv");
        exporter.export(lnPath, sampler.sample(ln, 0.05, 5.0, 0.00005, eps));
        out.println("Generated: " + lnPath);

        Path systemLeftPath = outputDir.resolve("system_left.csv");
        List<SampledPoint> leftBranch = sampleWithDiscontinuities(system, -2.0, -0.001, 0.00002, eps);
        exporter.export(systemLeftPath, leftBranch);
        out.println("Generated: " + systemLeftPath);

        Path systemRightPath = outputDir.resolve("system_right.csv");
        List<SampledPoint> rightBranch = sampleWithDiscontinuities(system, 0.001, 2.2, 0.00002, eps);
        exporter.export(systemRightPath, rightBranch);
        out.println("Generated: " + systemRightPath);

        Path systemPath = outputDir.resolve("system.csv");
        List<SampledPoint> combined = new ArrayList<>(leftBranch.size() + rightBranch.size() + 1);
        combined.addAll(leftBranch);
        combined.add(new SampledPoint(0.0, Double.NaN));
        combined.addAll(rightBranch);
        exporter.export(systemPath, combined);
        out.println("Generated: " + systemPath);

        out.println("Done.");
    }

    static List<SampledPoint> sampleWithDiscontinuities(
            MathFunction function,
            double from,
            double to,
            double step,
            double eps
    ) {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive");
        }
        if (from > to) {
            throw new IllegalArgumentException("from must be less than or equal to to");
        }

        List<SampledPoint> points = new ArrayList<>();
        double x = from;
        while (x <= to + eps) {
            double value;
            try {
                value = function.calculate(x, eps);
            } catch (RuntimeException exception) {
                value = Double.NaN;
            }
            points.add(new SampledPoint(x, value));
            x += step;
        }
        return points;
    }

    static void createOutputDirectory(Path outputDir) {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create output directory: " + outputDir.toAbsolutePath(), e);
        }
    }
}
