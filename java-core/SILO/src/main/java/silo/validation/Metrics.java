package silo.validation;

import java.util.List;

/**
 * Class containing methods in order to perform validation checks on model results.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180817
 */
public class Metrics {

    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Returns the percent accuracy of the two lists. That is, returns the percentage of values in the two lists that
     * are equal.
     *
     * @param a
     *      the first list
     * @param b
     *      the second list
     *
     * @return the percentage of the two lists that are equal
     *
     * @throws IllegalArgumentException if either of the lists are null, or if the size of the two lists do not match
     */
    public static <T> double accuracy(List<T> a, List<T> b) throws IllegalArgumentException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("input lists must not be null");
        } else if (a.size() != b.size()) {
            throw new IllegalArgumentException("input lists must be of same size");
        }
        int numEqual = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).equals(b.get(i))) {
                numEqual++;
            }
        }
        return (double) numEqual / a.size();
    }

}
