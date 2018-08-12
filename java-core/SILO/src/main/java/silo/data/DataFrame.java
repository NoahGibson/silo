package silo.data;

import java.util.List;
import java.util.Set;

/**
 * Interface for a {@code DataFrame}.
 *
 * @param <T>
 *      the type of the values stored in the DataFrame
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180805
 */
public interface DataFrame<T> extends Iterable<DataRow<T>> {

    /**
     * Returns all the rows contained in {@code this}.
     *
     * @return the rows of the data frame
     */
    List<DataRow<T>> rows();

    /**
     * Returns the row at the specified index in {@code this}.
     *
     * @param index
     *      the index of the row to return
     *
     * @return the {@link DataRow} at the specified index of the data frame
     */
    DataRow<T> getRow(int index);

    /**
     * Returns a list of names of the columns contained in {@code this}.
     *
     * @return the names of the columns in the data frame
     */
    Set<String> columns();

    /**
     * Returns the column values of the specified column.
     *
     * @param column
     *      the name of the column to retrieve the values of
     *
     * @return a list of the values in the specified column
     */
    List<T> getColumn(String column);

    /**
     * Adds the given row to {@code this} at the given index. All rows currently in the data frame with an index
     * greater than or equal to the given index will have their index increased by 1.
     * <p>
     * The given row should have all the columns of {@code this} and only those columns.
     *
     * @param row
     *      the new row to add to the data frame
     * @param index
     *      the index at which to put the new row
     */
    void addRow(DataRow<T> row, int index);

    /**
     * Adds the given values to {@code this} as a new column with the given name, associating each value in the list
     * with the row at the same index as the value is within {@code column}.
     * <p>
     * The size of {@code column} must equal the size of {@code this}.
     * <p>
     * The name of the new column must not already exist in the data frame.
     *
     * @param columnName
     *      the name of the new column
     * @param column
     *      the values to insert into the new column
     */
    void addColumn(String columnName, List<T> column);

    /**
     * Removes the row at the given index from {@code this}. All rows with an index greater than the given index will
     * be decreased by 1.
     *
     * @param index
     *      the index of the row to remove
     */
    void removeRow(int index);

    /**
     * Removes the specified column from {@code this}.
     *
     * @param column
     *      the name of the column to remove
     */
    void removeColumn(String column);

    /**
     * Returns the number of rows contained in {@code this}.
     *
     * @return the number of rows in the data frame
     */
    int size();

}
