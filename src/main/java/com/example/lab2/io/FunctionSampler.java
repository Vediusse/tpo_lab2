package com.example.lab2.io;

import com.example.lab2.model.MathFunction;
import com.example.lab2.model.SampledPoint;
import com.example.lab2.util.DoubleComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Samples a function on a numeric range.
 */
public class FunctionSampler {

    /**
     * Samples the function on [from, to] with the given step.
     *
     * @param function target function
     * @param from range start
     * @param to range end
     * @param step positive step
     * @param eps precision
     * @return sampled points
     */
    public List<SampledPoint> sample(MathFunction function, double from, double to, double step, double eps) {
        DoubleComparator.requirePositiveEps(eps);
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive");
        }
        if (from > to) {
            throw new IllegalArgumentException("from must be less than or equal to to");
        }

        List<SampledPoint> result = new ArrayList<>();
        double x = from;
        while (x <= to + eps) {
            result.add(new SampledPoint(x, function.calculate(x, eps)));
            x += step;
        }
        return result;
    }
}
