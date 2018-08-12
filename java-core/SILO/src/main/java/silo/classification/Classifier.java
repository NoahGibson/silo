package silo.classification;

import silo.data.DataFrame;
import silo.data.DataRow;

import java.util.List;

/**
 * Interface for a {@code Classifier}, which, fit on a set of labeled data, can make a prediction for the label of a
 * new point of data.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180807
 */
public interface Classifier<F, L> {

    /**
     * Fits {@code this} to the given features and labels.
     *
     * @param features
     *      the features of the data points
     * @param labels
     *      the labels of the data points
     */
    void fit(DataFrame<F> features, List<L> labels);

    /**
     * Predicts a label for the given features, using the features and labels {@code this} was fit to.
     *
     * @param features
     *      the features to predict the label for
     *
     * @return the predicted label for the given features
     */
    L predict(DataRow<F> features);

    /**
     * Predicts labels for each point in the given data frame, using the features and labels {@code this} was fit to.
     *
     * @param features
     *      the data frame of features to predict the labels of
     *
     * @return the predicted labels for the given features
     */
    List<L> predict(DataFrame<F> features);

}
