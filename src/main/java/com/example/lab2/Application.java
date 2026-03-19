package com.example.lab2;

import com.example.lab2.function.log.LogNFunction;
import com.example.lab2.function.trig.CosFunction;
import com.example.lab2.function.trig.CotFunction;
import com.example.lab2.function.trig.CscFunction;
import com.example.lab2.function.trig.SecFunction;
import com.example.lab2.function.trig.TanFunction;
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
        Arguments arguments = parseArguments(args);
        run(
                arguments.outputDir(),
                System.out,
                new FunctionSampler(),
                new CsvExporter(),
                FunctionFactory.createFunctionSystem(),
                FunctionFactory.createSinFunction(),
                FunctionFactory.createLnFunction(),
                arguments.eps()
        );
    }

    static Arguments parseArguments(String[] args) {
        Path outputDir = Path.of("output");
        double eps = 1E-10;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--output-dir".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --output-dir");
                }
                outputDir = Path.of(args[++i]);
                continue;
            }
            if ("--eps".equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --eps");
                }
                eps = Double.parseDouble(args[++i]);
                continue;
            }
            throw new IllegalArgumentException("Unknown argument: " + arg);
        }

        return new Arguments(outputDir, eps);
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

        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction sec = new SecFunction(cos);
        MathFunction csc = new CscFunction(sin);
        MathFunction log2 = new LogNFunction(2.0, ln);
        MathFunction log3 = new LogNFunction(3.0, ln);
        MathFunction log5 = new LogNFunction(5.0, ln);

        out.println("Generating CSV...");

        exportContinuous(outputDir.resolve("sin.csv"), out, sampler, exporter, sin, -Math.PI, Math.PI, 0.001, eps);
        exportContinuous(outputDir.resolve("cos.csv"), out, sampler, exporter, cos, -Math.PI, Math.PI, 0.001, eps);
        exportDiscontinuous(outputDir.resolve("tan.csv"), out, exporter, tan, -2.0, 2.0, 0.001, eps);
        exportDiscontinuous(outputDir.resolve("cot.csv"), out, exporter, cot, -2.0, 2.0, 0.001, eps);
        exportDiscontinuous(outputDir.resolve("sec.csv"), out, exporter, sec, -2.0, 2.0, 0.001, eps);
        exportDiscontinuous(outputDir.resolve("csc.csv"), out, exporter, csc, -2.0, 2.0, 0.001, eps);

        exportContinuous(outputDir.resolve("ln.csv"), out, sampler, exporter, ln, 0.05, 5.0, 0.001, eps);
        exportContinuous(outputDir.resolve("log2.csv"), out, sampler, exporter, log2, 0.05, 5.0, 0.001, eps);
        exportContinuous(outputDir.resolve("log3.csv"), out, sampler, exporter, log3, 0.05, 5.0, 0.001, eps);
        exportContinuous(outputDir.resolve("log5.csv"), out, sampler, exporter, log5, 0.05, 5.0, 0.001, eps);

        Path systemLeftPath = outputDir.resolve("system_left.csv");
        List<SampledPoint> leftBranch = sampleWithDiscontinuities(system, -4.0, -0.001, 0.001, eps);
        exporter.export(systemLeftPath, leftBranch);
        out.println("Generated: " + systemLeftPath);

        Path systemRightPath = outputDir.resolve("system_right.csv");
        List<SampledPoint> rightBranch = sampleWithDiscontinuities(system, 0.001, 2.2, 0.001, eps);
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

    private static void exportContinuous(
            Path path,
            PrintStream out,
            FunctionSampler sampler,
            CsvExporter exporter,
            MathFunction function,
            double from,
            double to,
            double step,
            double eps
    ) {
        exporter.export(path, sampler.sample(function, from, to, step, eps));
        out.println("Generated: " + path);
    }

    private static void exportDiscontinuous(
            Path path,
            PrintStream out,
            CsvExporter exporter,
            MathFunction function,
            double from,
            double to,
            double step,
            double eps
    ) {
        exporter.export(path, sampleWithDiscontinuities(function, from, to, step, eps));
        out.println("Generated: " + path);
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

    record Arguments(Path outputDir, double eps) {
    }
}
