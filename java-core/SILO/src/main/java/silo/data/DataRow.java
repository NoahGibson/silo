package silo.data;

import java.util.Map;
import java.util.Set;

/**
 * Interface for a {@code DataRow}. A DataRow represents a single row within a {@link DataFrame}.
 *
 * @param <T>
 *     the type of values stored in the row
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180811
 */
public interface DataRow<T> {

    /**
     * Returns the mapping from the columns of {@code this} to the values of {@code this}.
     *
     * @return the mapping from the columns to column values
     */
    Map<String, T> values();

    /**
     * Returns the value of the specified column of {@code this}.
     *
     * @param column
     *      the name of the column to return the value of
     *
     * @return the value in the column named {@code column}
     */
    T getValue(String column);

    /**
     * Sets the value of the specified column of {@code this}. If the column does not already exist, it will be added.
     * If the column does exist, the value will be overwritten.
     *
     * @param column
     *      the name of the column to set the value of
     * @param value
     *      the value of the column to set
     */
    void setValue(String column, T value);

    /**
     * Removes the value of the specified column of {@code this}, and returns the removed value. The column of that
     * value is also removed from {@code this}.
     *
     * @param column
     *      the name of the column to remove the value of
     */
    void removeValue(String column);

    /**
     * Returns a list of names of the columns contained in {@code this}.
     *
     * @return the names of the columns in the data row
     */
    Set<String> columns();

    /**
     * Returns the number of values contained in {@code this}.
     *
     * @return the number of values in tShe row
     */
    int size();

}
