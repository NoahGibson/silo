package silo.math;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing basic statistical functions.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180815
 */
public class Statistics {

    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Returns the mean of the given list of values (assuming the types of values contained in the given list are
     * either numeric, or strings that can be parsed as numeric).
     *
     * @param values
     *      the list of values to find the mean of
     *
     * @return the mean of the list of values
     *
     * @throws IllegalArgumentException if the given list is null, or if there is an error parsing the values in the
     *      list as numeric
     */
    public static <T> double mean(List<T> values) throws IllegalArgumentException {
        if (values == null) {
            throw new IllegalArgumentException("input list must not be null");
        }
        double sum = 0;
        for (T value : values) {
            sum += Double.parseDouble("" + value);
        }
        return sum / values.size();
    }

    /**
     * Returns the mode of the given list of values.
     *
     * @param values
     *      the list of values to find the mode of
     *
     * @return the mode of the list of values
     *
     * @throws IllegalArgumentException if the given list is null
     */
    public static <T> T mode(List<T> values) throws IllegalArgumentException {
        if (values == null) {
            throw new IllegalArgumentException("input list must not be null");
        }
        Map<T, Integer> occurrences = new HashMap<>();
        for (T value : values) {
            if (occurrences.containsKey(value)) {
                occurrences.put(value, occurrences.get(value) + 1);
            } else {
                occurrences.put(value, 1);
            }
        }
        T winner = null;
        int maxCount = 0;
        for (Map.Entry<T, Integer> candidate : occurrences.entrySet()) {
            if (candidate.getValue() > maxCount) {
                winner = candidate.getKey();
                maxCount = candidate.getValue();
            }
        }
        return winner;
    }

    /**
     * Returns the percent similarity of the two lists. That is, returns the percentage of values in the two lists that
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
    public static <T> double similarity(List<T> a, List<T> b) throws IllegalArgumentException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("input lists must not be null");
        } else if (a.size() != b.size()) {
            throw new IllegalArgumentException("input lists must be of same size");
        }
        double numEqual = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).equals(b.get(i))) {
                numEqual++;
            }
        }
        return numEqual / a.size();
    }

}
