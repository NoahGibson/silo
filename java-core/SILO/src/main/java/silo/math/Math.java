package silo.math;

import silo.data.DataFrame;

import java.util.*;

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
     * Returns the logarithm of the given value using base 2.
     *
     * @param x
     *      the value to compute the logarithm of
     *
     * @return the log base 2 of the given value
     */
    public static double log2(double x) {
        return java.lang.Math.log(x) / java.lang.Math.log(2);
    }

    /**
     * Computes the entropy of the given list of discrete values.
     *
     * @param values
     *      the values to compute the entropy of
     *
     * @return the entropy of the given set of values
     */
    public static <T> double entropy(List<T> values) {
        Map<T, Integer> classCounts = new HashMap<>();
        for (T value : values) {
            if (classCounts.containsKey(value)) {
                classCounts.put(value, classCounts.get(value) + 1);
            } else {
                classCounts.put(value, 1);
            }
        }
        double entropy = 0.0;
        for (Map.Entry<T, Integer> valueCount : classCounts.entrySet()) {
            double prob = (double) valueCount.getValue() / values.size();
            entropy += prob * log2(prob);
        }
        return -entropy;
    }

    /**
     * Calculates the information gain of the given labels if splitting on the given feature.
     *
     * @param feature
     *      the feature of the data set to split on
     * @param labels
     *      the labels of the data set
     *
     *
     * @return the information gain
     *
     * @throws IllegalArgumentException if either feature or labels is {@code null}, or if feature and labels do not
     *      have the same size
     */
    public static <F, L> double informationGain(List<F> feature, List<L> labels) throws IllegalArgumentException {
        if (feature == null) {
            throw new IllegalArgumentException("feature must not be null");
        } else if (labels == null) {
            throw new IllegalArgumentException("labels must not be null");
        }
        if (feature.size() != labels.size()) {
            throw new IllegalArgumentException("feature and labels must be of same size");
        }

        /*
          * Map containing each possible value of the attribute column as the keys, and the list of all labels of that
          * attribute value as the values
          */
        Map<F, List<L>> valueLabels = new HashMap<>();

        // Initializing the valueLabels map
        for (int i = 0; i < feature.size(); i++) {
            F attrVal = feature.get(i);
            if (valueLabels.containsKey(attrVal)) {
                valueLabels.get(attrVal).add(labels.get(i));
            } else {
                List<L> attrLabels = new ArrayList<>();
                attrLabels.add(labels.get(i));
                valueLabels.put(attrVal, attrLabels);
            }
        }

        // Calculating the information gain
        double ig = entropy(labels);
        for (Map.Entry<F, List<L>> valueLabel : valueLabels.entrySet()) {
            ig -= ((double) valueLabel.getValue().size() / feature.size()) * entropy(valueLabel.getValue());
        }
        return ig;
    }

}
