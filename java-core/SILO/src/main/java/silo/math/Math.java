package silo.math;

import silo.data.DataRow;

/**
 * Class implementing basic mathematical functions.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180815
 */
public class Math {

    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Returns the Minkowski Distance between points {@code a} and {@code b}, for the given order p.
     *
     * @param a
     *      the first point
     * @param b
     *      the second point
     * @param p
     *      the order to use (1 -> Manhattan Distance, 2 -> Euclidean Distance, etc.)
     *
     * @return the Minkowski Distance between the two points
     *
     * @throws IllegalArgumentException if either array is null, if the two points are of unequal dimension
     */
    public static double minkowskiDistance(double[] a, double[] b, int p) throws IllegalArgumentException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("input arrays must not be null");
        }
        if (a.length != b.length) {
            throw new IllegalArgumentException("arrays must be of same size");
        }
        double distance = 0;
        for (int i = 0; i < a.length; i++) {
            distance += java.lang.Math.pow(java.lang.Math.abs(a[i] - b[i]), p);
        }
        return java.lang.Math.pow(distance, (1.0 / p));
    }

    /**
     * Returns the Minkowski Distance between points {@code a} and {@code b}, for the given order p.
     *
     * @param a
     *      the first point
     * @param b
     *      the second point
     * @param p
     *      the order to use (1 -> Manhattan Distance, 2 -> Euclidean Distance, etc.)
     *
     * @return the Minkowski Distance between the two points
     *
     * @throws IllegalArgumentException if either array is null, if the two points are of unequal dimension
     */
    public static double minkowskiDistance(int[] a, int[] b, int p) throws IllegalArgumentException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("input arrays must not be null");
        }
        if (a.length != b.length) {
            throw new IllegalArgumentException("arrays must be of same size");
        }
        double distance = 0;
        for (int i = 0; i < a.length; i++) {
            distance += java.lang.Math.pow(java.lang.Math.abs(a[i] - b[i]), p);
        }
        return java.lang.Math.pow(distance, (1.0 / p));
    }

    /**
     * Returns the Minkowski Distance between DataRows {@code a} and {@code b} (assuming the types of values contained
     * in the rows are either numeric, or strings that can be parsed as numeric) for the given order p.
     *
     * @param a
     *      the first DataRow
     * @param b
     *      the second DataRow
     * @param p
     *      the order to use (1 -> Manhattan Distance, 2 -> Euclidean Distance, etc.)
     *
     * @return the Minkowski Distance between the two DataRows
     *
     * @throws IllegalArgumentException if either DataRow is null, if the two data rows are of unequal size, or if
     *      there is an error parsing the values in either row as numeric
     */
    public static <T> double minkowskiDistance(DataRow<T> a, DataRow<T> b, int p) throws IllegalArgumentException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("input data rows must not be null");
        }
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("data rows must be of same size");
        }
        double distance = 0;
        for (String column : a.columns()) {
            distance += java.lang.Math.pow(java.lang.Math.abs(Double.parseDouble("" + a.getValue(column)) - Double.parseDouble("" + b.getValue(column))), p);
        }
        return java.lang.Math.pow(distance, (1.0 / p));
    }

}
