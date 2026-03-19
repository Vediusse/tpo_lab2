package com.example.lab2.io;

import com.example.lab2.model.SampledPoint;
import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Writes sampled points to CSV files in x,result format.
 */
public class CsvExporter {

    /**
     * Exports points to a CSV file.
     *
     * @param path output file path
     * @param points sampled points
     */
    public void export(Path path, List<SampledPoint> points) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (Writer writer = new OutputStreamWriter(
                    Files.newOutputStream(
                            path,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.WRITE
                    ),
                    StandardCharsets.UTF_8
            );
                 CSVWriter csvWriter = new CSVWriter(
                         writer,
                         CSVWriter.DEFAULT_SEPARATOR,
                         CSVWriter.NO_QUOTE_CHARACTER,
                         CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                         CSVWriter.DEFAULT_LINE_END
                 )) {
                csvWriter.writeNext(new String[]{"x", "result"});
                for (SampledPoint point : points) {
                    csvWriter.writeNext(new String[]{
                            Double.toString(point.x()),
                            Double.toString(point.result())
                    });
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to export CSV to: " + path, e);
        }
    }
}
