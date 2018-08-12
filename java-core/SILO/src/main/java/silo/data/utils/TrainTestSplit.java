package silo.data.utils;

import silo.data.DataFrame;

import java.util.HashMap;
import java.util.Map;

/**
 * Object to store the results of a train-test split.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180812
 */
public class TrainTestSplit<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * Map to store the results of the split
     */
    private Map<String, DataFrame<T>> splitResults;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String TRAIN_SET = "TRAIN_SET";

    private static final String TEST_SET = "TEST_SET";


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Creates a new {@link TrainTestSplit} object with the given train and test sets.
     *
     * @param trainSet
     *      the train set of the split
     * @param testSet
     *      the test set of the split
     */
    public TrainTestSplit(DataFrame<T> trainSet, DataFrame<T> testSet) {
        this.splitResults = new HashMap<>();
        this.splitResults.put(TRAIN_SET, trainSet);
        this.splitResults.put(TEST_SET, testSet);
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * Returns the train set of {@code this}.
     *
     * @return the train set of the split
     */
    public final DataFrame<T> getTrainSet() {
        return this.splitResults.get(TRAIN_SET);
    }

    /**
     * Returns the test set of {@code this}.
     *
     * @return the test set of the split
     */
    public final DataFrame<T> getTestSet() {
        return this.splitResults.get(TEST_SET);
    }

}
