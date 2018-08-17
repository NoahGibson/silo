package silo.classification;

import silo.data.ArrayListDataFrame;
import silo.data.DataFrame;
import silo.data.DataRow;

import java.util.*;
import java.util.function.BiFunction;

/**
 * K Nearest Neighbors Classifier.
 *
 * @param <F>
 *     the type of the features
 * @param <L>
 *     the type of the labels
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180811
 */
public class KNNClassifier<F, L> implements Classifier<F, L> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The data the KNN classifier is fit on.
     */
    private DataFrame<F> data;

    /**
     * The labels of the data the KNN classifier is fit on.
     */
    private List<L> labels;

    /**
     * The number of neighbors to use.
     */
    private int k;

    /**
     * The distance measure for the classifier, which is a function used to calculate the distance between two
     * {@link DataRow}s. By default, it tries to use the Euclidean distance between the rows. However, this assumes
     * that the type {@code F} is numeric, or all values in the rows are strings which can be parsed as a numeric.
     */
    private BiFunction<DataRow<F>, DataRow<F>, Double> distanceMeasure = (a, b) ->
            silo.math.Distance.minkowskiDistance(a, b, 2);


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_FEATURES_MSG = "features must not be null";

    private static final String NULL_LABELS_MSG = "labels must not be null";

    private static final String NULL_DISTANCE_MEASURE_MSG = "distanceMeasure must not be null";

    private static final String INVALID_SIZES_MSG = "features and labels must be of same size";

    private static final String INVALID_COLUMNS_MSG = "the columns of the given features must match the columns of the data this was fit on";

    private static final String MODEL_NOT_FIT_MSG = "this has not yet been fit";


    /*
     * EMBEDDED CLASSES ------------------------------------------------------------------------------------------------
     */

    /**
     * Embedded class to hold a row index and its distance from the currently sought after data point.
     */
    private class RowDistancePair implements Comparable<RowDistancePair> {

        int rowIndex;
        double distance;

        RowDistancePair(int rowIndex, double distance) {
            this.rowIndex = rowIndex;
            this.distance = distance;
        }

        @Override
        public int compareTo(RowDistancePair rdp) {
            return Double.compare(this.distance, rdp.distance);
        }

    }


    /*
     * PRIVATE METHODS -------------------------------------------------------------------------------------------------
     */

    /**
     * Gets the indices of the rows {@code this} is fit on that are the nearest neighbors to the given data point.
     *
     * @param features
     *      the point to get the nearest neighbors of
     *
     * @return the indices of the rows that are the nearest neighbors to the given point
     */
    private int[] getNNIndices(DataRow<F> features) {
        int minOfKAndDataSize = Math.min(this.k, this.data.size());
        PriorityQueue<RowDistancePair> pointDistances = new PriorityQueue<>();
        // Adding k rows initially to the priority queue, or the max number of rows in the fitted data
        for (int i = 0; i < minOfKAndDataSize; i++) {
            // Adding the negative distance so that the max distance will be at the top of the priority queue
            pointDistances.add(new RowDistancePair(i, -this.distanceMeasure.apply(features, this.data.getRow(i))));
        }
        // Iterating through the rest of the data to find the nearest neighbors
        for (int i = minOfKAndDataSize; i < this.data.size(); i++) {
            double pointDistance = -this.distanceMeasure.apply(features, this.data.getRow(i));
            if (pointDistance > pointDistances.peek().distance) {
                pointDistances.poll();
                pointDistances.add(new RowDistancePair(i, pointDistance));
            }
        }
        // Getting the indices of the k nearest rows
        int[] nnIndices = new int[minOfKAndDataSize];
        for (int i = 0; i < pointDistances.size(); i++) {
            nnIndices[i] = pointDistances.poll().rowIndex;
        }
        return nnIndices;
    }

    /**
     * Returns the prediction label based upon the given row indices.
     *
     * @param rowIndices
     *      the indices of the rows to use to predict the label
     *
     * @return the predicted label
     */
    private L getPredictionLabel(int[] rowIndices) {
        Map<L, Integer> labelCounts = new HashMap<>();
        for (int i : rowIndices) {
            L vote = this.labels.get(i);
            if (labelCounts.containsKey(vote)) {
                labelCounts.put(vote, labelCounts.get(vote) + 1);
            } else {
                labelCounts.put(vote, 1);
            }
        }
        L winner = null;
        int winnerVotes = 0;
        for (Map.Entry<L, Integer> candidate : labelCounts.entrySet()) {
            if (candidate.getValue() > winnerVotes) {
                winner = candidate.getKey();
                winnerVotes = candidate.getValue();
            }
        }
        return winner;
    }


    /*
     * OVERRIDDEN METHODS ----------------------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KNNClassifier<?, ?> that = (KNNClassifier<?, ?>) o;
        return k == that.k &&
                Objects.equals(data, that.data) &&
                Objects.equals(labels, that.labels) &&
                Objects.equals(distanceMeasure, that.distanceMeasure);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(data, labels, k, distanceMeasure);
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initialize a new {@link KNNClassifier} object with the given value for K, where K is the number of nearest
     * neighbors to consider when predicting the labels for new data points.
     *
     * @param k
     *      the number of nearest neighbors to consider when predicting new labels
     */
    public KNNClassifier(int k) {
        this.k = k;
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if either the given features or labels are null, or if the size of the features
     *      and labels do not match
     */
    @Override
    public final void fit(DataFrame<F> features, List<L> labels) throws IllegalArgumentException {
        if (features == null) {
            throw new IllegalArgumentException(NULL_FEATURES_MSG);
        } else if (labels == null) {
            throw new IllegalArgumentException(NULL_LABELS_MSG);
        } else if (features.size() != labels.size()) {
            throw new IllegalArgumentException(INVALID_SIZES_MSG);
        }
        this.data = new ArrayListDataFrame<>(features);
        this.labels = new ArrayList<>(labels);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given features are null, or if the data row does not have all and only
     *      the columns as the data {@code this} was fit on
     * @throws IllegalStateException if {@code this} has not already been fit to data
     */
    @Override
    public final L predict(DataRow<F> features) throws IllegalArgumentException, IllegalStateException {
        if (this.data == null || this.labels == null) {
            throw new IllegalStateException(MODEL_NOT_FIT_MSG);
        } else if (features == null) {
            throw new IllegalArgumentException(NULL_FEATURES_MSG);
        } else if (!this.data.columns().equals(features.columns())) {
            throw new IllegalArgumentException(INVALID_COLUMNS_MSG);
        }
        return getPredictionLabel(getNNIndices(features));
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given features are null, or if the data frame does not have all and only
     *      the columns as the data {@code this} was fit on
     * @throws IllegalStateException if {@code this} has not already been fit to data
     */
    @Override
    public final List<L> predict(DataFrame<F> features) throws IllegalArgumentException, IllegalStateException {
        if (this.data == null || this.labels == null) {
            throw new IllegalStateException(MODEL_NOT_FIT_MSG);
        } else if (features == null) {
            throw new IllegalArgumentException(NULL_FEATURES_MSG);
        } else if (!this.data.columns().equals(features.columns())) {
            throw new IllegalArgumentException(INVALID_COLUMNS_MSG);
        }
        List<L> labels = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            labels.add(predict(features.getRow(i)));
        }
        return labels;
    }

    /**
     * Returns the value of K of the KNN classifier.
     *
     * @return the value of K
     */
    public int getK() {
        return this.k;
    }

    /**
     * Set the value of K for the KNN classifier.
     *
     * @param k
     *      the new K value for the classifier
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * Sets the distance measure of the classifier. The distance measure is used to determine the distance between two
     * data points. By default, the distance measure used is the Euclidean distance between the points, assuming that
     * all values in the data are either numeric or strings that can be parsed as numeric.
     *
     * @param distanceMeasure
     *      a function of two {@link DataRow} arguments which computes the distance between the two rows
     *
     * @throws IllegalArgumentException if the given distance measure is {@code null}
     */
    public final void setDistanceMeasure(BiFunction<DataRow<F>, DataRow<F>, Double> distanceMeasure) throws IllegalArgumentException {
        if (distanceMeasure == null) {
            throw new IllegalArgumentException(NULL_DISTANCE_MEASURE_MSG);
        }
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * Returns the distance measure of {@code this}.
     *
     * @return the distance measure of the classifier
     */
    public final BiFunction<DataRow<F>, DataRow<F>, Double> getDistanceMeasure() {
        return this.distanceMeasure;
    }

}
