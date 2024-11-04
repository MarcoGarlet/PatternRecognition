package com.example.demo.utils;

/**
 * Utility class for mathematical helper methods.
 */
public class MathUtils {

    private static final double DEFAULT_TOLERANCE = 1e-6;;

    /**
     * Compares two double values for equality within a default tolerance.
     *
     * @param d1 the first double value
     * @param d2 the second double value
     * @return true if the values are within the default tolerance, false otherwise
     */
    public static boolean areEqual(double d1, double d2) {
        return areEqual(d1, d2, DEFAULT_TOLERANCE);
    }

    /**
     * Compares two double values for equality within a specified tolerance.
     *
     * @param d1 the first double value
     * @param d2 the second double value
     * @param tolerance the maximum allowable difference between the values
     * @return true if the values are within the specified tolerance, false otherwise
     */
    public static boolean areEqual(double d1, double d2, double tolerance) {
        return Math.abs(d1 - d2) <= tolerance;
    }
}
