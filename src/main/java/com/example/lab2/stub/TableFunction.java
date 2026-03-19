package com.example.lab2.stub;

import com.example.lab2.model.MathFunction;
import com.example.lab2.util.DoubleComparator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Tabular function implementation with exact lookup and linear interpolation.
 */
public class TableFunction implements MathFunction {

    private final NavigableMap<Double, Double> points;

    public TableFunction(NavigableMap<Double, Double> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("table points must not be null or empty");
        }
        this.points = new TreeMap<>(points);
    }

    public static TableFunction fromCsv(Path path) {
        try (Reader reader = Files.newBufferedReader(path);
             CSVReader csvReader = new CSVReader(reader)) {
            return fromCsv(csvReader);
        } catch (IOException | CsvValidationException e) {
            throw new IllegalArgumentException("Failed to read table CSV: " + path, e);
        }
    }

    public static TableFunction fromCsv(Reader reader) {
        try (CSVReader csvReader = new CSVReader(reader)) {
            return fromCsv(csvReader);
        } catch (IOException | CsvValidationException e) {
            throw new IllegalArgumentException("Failed to read table CSV", e);
        }
    }

    private static TableFunction fromCsv(CSVReader csvReader) throws IOException, CsvValidationException {
        NavigableMap<Double, Double> map = new TreeMap<>();
        String[] row;
        while ((row = csvReader.readNext()) != null) {
            if (row.length < 2 || "x".equalsIgnoreCase(row[0].trim())) {
                continue;
            }
            map.put(Double.parseDouble(row[0].trim()), Double.parseDouble(row[1].trim()));
        }
        return new TableFunction(map);
    }

    @Override
    public double calculate(double x, double eps) {
        DoubleComparator.requirePositiveEps(eps);

        Double exactKey = findExactKey(x, eps);
        if (exactKey != null) {
            return points.get(exactKey);
        }

        Double floor = points.floorKey(x);
        Double ceil = points.ceilingKey(x);
        if (floor == null || ceil == null) {
            throw new IllegalArgumentException("x is outside the table range: " + x);
        }

        double y1 = points.get(floor);
        double y2 = points.get(ceil);
        if (DoubleComparator.isClose(floor, ceil, eps)) {
            return y1;
        }
        return y1 + (x - floor) * (y2 - y1) / (ceil - floor);
    }

    private Double findExactKey(double x, double eps) {
        for (Double key : points.keySet()) {
            if (DoubleComparator.isClose(key, x, eps)) {
                return key;
            }
        }
        return null;
    }
}
