package silo.math;

import silo.data.DataRow;

import java.util.List;

/**
 * Class implementing methods to calculate the distance between values.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180817
 */
public class Distance {

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
     * @throws IllegalArgumentException if either array is null, or if the two points are of unequal dimension
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
     * @throws IllegalArgumentException if either array is null, or if the two points are of unequal dimension
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
     * Returns the Minkowski Distance between Lists {@code a} and {@code b} (assuming the types of values contained
     * in the lists are either numeric, or strings that can be parsed as numeric) for the given order p.
     *
     * @param a
     *      the first list
     * @param b
     *      the second list
     * @param p
     *      the order to use (1 -> Manhattan Distance, 2 -> Euclidean Distance, etc.)
     *
     * @return the Minkowski Distance between the two lists
     *
     * @throws IllegalArgumentException if either list is null, if the two lists are of unequal size, or if
     *      there is an error parsing the values in either list as numeric
     */
    public static <T> double minkowskiDistance(List<T> a, List<T> b, int p) throws IllegalArgumentException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("input lists must not be null");
        }
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("lists must be of same size");
        }
        double distance = 0;
        for (int i = 0; i < a.size(); i++) {
            double difference = java.lang.Math.abs(Double.parseDouble("" + a.get(i)) - Double.parseDouble("" + b.get(i)));
            distance += java.lang.Math.pow(difference, p);
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
            double difference = java.lang.Math.abs(Double.parseDouble("" + a.getValue(column)) - Double.parseDouble("" + b.getValue(column)));
            distance += java.lang.Math.pow(difference, p);
        }
        return java.lang.Math.pow(distance, (1.0 / p));
    }

}
