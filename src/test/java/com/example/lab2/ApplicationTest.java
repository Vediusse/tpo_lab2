package com.example.lab2;

import com.example.lab2.io.CsvExporter;
import com.example.lab2.io.FunctionSampler;
import com.example.lab2.model.MathFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApplicationTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldInstantiateApplication() {
        assertThat(new Application()).isNotNull();
    }

    @Test
    void shouldRunApplicationWorkflow() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);
        Path outDir = tempDir.resolve("generated");

        MathFunction identity = (x, eps) -> x;

        Application.run(
                outDir,
                printStream,
                new FunctionSampler(),
                new CsvExporter(),
                identity,
                identity,
                identity,
                1E-6
        );

        assertThat(output.toString())
                .contains("Generating CSV...")
                .contains("Generated: " + outDir.resolve("sin.csv"))
                .contains("Generated: " + outDir.resolve("cos.csv"))
                .contains("Generated: " + outDir.resolve("tan.csv"))
                .contains("Generated: " + outDir.resolve("cot.csv"))
                .contains("Generated: " + outDir.resolve("sec.csv"))
                .contains("Generated: " + outDir.resolve("csc.csv"))
                .contains("Generated: " + outDir.resolve("ln.csv"))
                .contains("Generated: " + outDir.resolve("log2.csv"))
                .contains("Generated: " + outDir.resolve("log3.csv"))
                .contains("Generated: " + outDir.resolve("log5.csv"))
                .contains("Generated: " + outDir.resolve("system_left.csv"))
                .contains("Generated: " + outDir.resolve("system_right.csv"))
                .contains("Generated: " + outDir.resolve("system.csv"))
                .contains("Done.");
        assertThat(outDir.resolve("sin.csv")).exists();
        assertThat(outDir.resolve("cos.csv")).exists();
        assertThat(outDir.resolve("tan.csv")).exists();
        assertThat(outDir.resolve("cot.csv")).exists();
        assertThat(outDir.resolve("sec.csv")).exists();
        assertThat(outDir.resolve("csc.csv")).exists();
        assertThat(outDir.resolve("ln.csv")).exists();
        assertThat(outDir.resolve("log2.csv")).exists();
        assertThat(outDir.resolve("log3.csv")).exists();
        assertThat(outDir.resolve("log5.csv")).exists();
        assertThat(outDir.resolve("system_left.csv")).exists();
        assertThat(outDir.resolve("system_right.csv")).exists();
        assertThat(outDir.resolve("system.csv")).exists();
        assertThat(Files.readString(outDir.resolve("system.csv"))).contains("NaN");
    }

    @Test
    void shouldThrowWhenOutputDirectoryCannotBeCreated() throws Exception {
        Path file = Files.createTempFile(tempDir, "not-a-directory", ".tmp");

        assertThatThrownBy(() -> Application.createOutputDirectory(file))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to create output directory");
    }

    @Test
    void shouldParseCustomArguments() {
        Application.Arguments arguments = Application.parseArguments(new String[]{
                "--output-dir", "custom-output",
                "--eps", "1e-3"
        });

        assertThat(arguments.outputDir()).isEqualTo(Path.of("custom-output"));
        assertThat(arguments.eps()).isEqualTo(1E-3);
    }

    @Test
    void shouldRejectUnknownArgument() {
        assertThatThrownBy(() -> Application.parseArguments(new String[]{"--unknown"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown argument");
    }

    @Test
    void shouldRejectMissingArgumentValue() {
        assertThatThrownBy(() -> Application.parseArguments(new String[]{"--eps"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing value for --eps");
    }

    @Test
    void shouldRejectMissingOutputDirectoryValue() {
        assertThatThrownBy(() -> Application.parseArguments(new String[]{"--output-dir"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing value for --output-dir");
    }

    @Test
    void shouldRunMainMethod() throws Exception {
        Path outputDir = Path.of("output");
        boolean existedBefore = Files.exists(outputDir);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream previous = System.out;
        try {
            System.setOut(new PrintStream(output));
            Application.main(new String[0]);
        } finally {
            System.setOut(previous);
        }

        assertThat(output.toString())
                .contains("Generating CSV...")
                .contains("Generated: output/sin.csv")
                .contains("Generated: output/cos.csv")
                .contains("Generated: output/tan.csv")
                .contains("Generated: output/cot.csv")
                .contains("Generated: output/sec.csv")
                .contains("Generated: output/csc.csv")
                .contains("Generated: output/ln.csv")
                .contains("Generated: output/log2.csv")
                .contains("Generated: output/log3.csv")
                .contains("Generated: output/log5.csv")
                .contains("Generated: output/system_left.csv")
                .contains("Generated: output/system_right.csv")
                .contains("Generated: output/system.csv")
                .contains("Done.");
        assertThat(outputDir.resolve("sin.csv")).exists();
        assertThat(outputDir.resolve("cos.csv")).exists();
        assertThat(outputDir.resolve("tan.csv")).exists();
        assertThat(outputDir.resolve("cot.csv")).exists();
        assertThat(outputDir.resolve("sec.csv")).exists();
        assertThat(outputDir.resolve("csc.csv")).exists();
        assertThat(outputDir.resolve("ln.csv")).exists();
        assertThat(outputDir.resolve("log2.csv")).exists();
        assertThat(outputDir.resolve("log3.csv")).exists();
        assertThat(outputDir.resolve("log5.csv")).exists();
        assertThat(outputDir.resolve("system_left.csv")).exists();
        assertThat(outputDir.resolve("system_right.csv")).exists();
        assertThat(outputDir.resolve("system.csv")).exists();

        if (!existedBefore) {
            try (var paths = Files.walk(outputDir)) {
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception ignored) {
                        // Best-effort cleanup for test artifacts.
                    }
                });
            }
        }
    }

    @Test
    void shouldRunMainMethodWithCustomArguments() throws Exception {
        Path customOutput = tempDir.resolve("cli-output");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream previous = System.out;
        try {
            System.setOut(new PrintStream(output));
            Application.main(new String[]{"--output-dir", customOutput.toString(), "--eps", "1e-3"});
        } finally {
            System.setOut(previous);
        }

        assertThat(output.toString())
                .contains("Generating CSV...")
                .contains("Generated: " + customOutput.resolve("sin.csv"))
                .contains("Generated: " + customOutput.resolve("system.csv"))
                .contains("Done.");
        assertThat(customOutput.resolve("sin.csv")).exists();
        assertThat(customOutput.resolve("system.csv")).exists();
    }

    @Test
    void shouldSampleWithNaNOnDiscontinuity() {
        MathFunction unstable = (x, eps) -> {
            if (x < 0.0) {
                throw new ArithmeticException("break");
            }
            return x;
        };

        var points = Application.sampleWithDiscontinuities(unstable, -0.1, 0.1, 0.1, 1E-6);

        assertThat(points).hasSize(3);
        assertThat(points.get(0).result()).isNaN();
        assertThat(points.get(2).result()).isEqualTo(0.1);
    }

    @Test
    void shouldRejectInvalidSamplingStep() {
        assertThatThrownBy(() -> Application.sampleWithDiscontinuities((x, eps) -> x, 0.0, 1.0, 0.0, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("step");
    }

    @Test
    void shouldRejectInvalidSamplingRange() {
        assertThatThrownBy(() -> Application.sampleWithDiscontinuities((x, eps) -> x, 1.0, 0.0, 0.1, 1E-6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("from");
    }
}
