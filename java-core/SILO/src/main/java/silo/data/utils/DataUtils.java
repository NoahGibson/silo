package silo.data.utils;

import silo.data.ArrayListDataFrame;
import silo.data.DataFrame;

import java.util.*;

/**
 * Class containing utility methods for manipulating data sets.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180811
 */
public class DataUtils {

    /**
     * Randomly splits the given data frame into two new data frames, a test set and a train set. Each row in the given
     * data frame will have a probability of {@code testRatio} percent of being in the test set. If it is not chosen
     * to be in the test set, then it will be placed in the train set.
     *
     * @param data
     *      the data to split into a train and test set
     * @param testRatio
     *      the ratio of data rows which should be put in the test set
     *
     * @return a {@link TrainTestSplit} containing the new train and test sets
     *
     * @throws IllegalArgumentException if the given data is {@code null}
     */
    public static <T> TrainTestSplit<T> trainTestSplit(DataFrame<T> data, double testRatio) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        DataFrame<T> testSet = new ArrayListDataFrame<>(new HashSet<>(data.columns()));
        DataFrame<T> trainSet = new ArrayListDataFrame<>(new HashSet<>(data.columns()));
        Random rand = new Random();
        for (int i = 0; i < data.size(); i++ ) {
            if (rand.nextDouble() < testRatio) {
                testSet.addRow(data.getRow(i), testSet.size());
            } else {
                trainSet.addRow(data.getRow(i), trainSet.size());
            }
        }
        Collections.shuffle(testSet.rows());
        Collections.shuffle(trainSet.rows());
        return new TrainTestSplit<>(trainSet, testSet);
    }

}
