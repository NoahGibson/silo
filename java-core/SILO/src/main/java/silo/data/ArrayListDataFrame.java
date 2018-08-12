package silo.data;

import java.util.*;

/**
 * ArrayList implementation of a {@link DataFrame}.
 *
 * @param <T>
 *     the type of values stored in the data frame
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180811
 */
public class ArrayListDataFrame<T> implements DataFrame<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * The array list of data rows representing {@code this}.
     */
    private ArrayList<DataRow<T>> rep;

    /**
     * The set of column names of {@code this}.
     */
    private Set<String> columnNames;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String NULL_COLUMN_NAMES_MSG = "columnNames must not be null";

    private static final String NULL_DATAFRAME_MSG = "dataFrame must not be null";

    private static final String INVALID_COLUMNS_MSG = "the given row's columns must match the columns of this";

    private static final String INVALID_COLUMN_MSG = "the specified column does not exist";

    private static final String COLUMN_ALREADY_EXISTS_MSG = "the given column already exists";

    private static final String INVALID_COLUMN_SIZE_MSG = "the size of the column must match the size of this";


    /*
     * PRIVATE METHODS -------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes the representation of {@code this}.
     *
     * @param columnNames
     *      the names of the columns of the data frame
     */
    private void init(Set<String> columnNames) {
        this.rep = new ArrayList<>();
        this.columnNames = columnNames;
    }


    /*
     * OVERRIDDEN METHODS ----------------------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayListDataFrame<?> that = (ArrayListDataFrame<?>) o;
        return Objects.equals(rep, that.rep) &&
                Objects.equals(columnNames, that.columnNames);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(rep, columnNames);
    }

    @Override
    public final String toString() {
        return rep.toString();
    }

    @Override
    public final Iterator<DataRow<T>> iterator() {
        return rep.iterator();
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes a new {@link ArrayListDataFrame} with no data. The columns of the data frame are set to the given
     * set of column names.
     *
     * @param columnNames
     *      the names of the columns of the data frame
     *
     * @throws IllegalArgumentException if {@code columnNames} is {@code null}
     */
    public ArrayListDataFrame(Set<String> columnNames) throws IllegalArgumentException {
        if (columnNames == null) {
            throw new IllegalArgumentException(NULL_COLUMN_NAMES_MSG);
        }
        init(columnNames);
    }

    /**
     * Creates a new {@link ArrayListDataFrame} copied from the given data frame.
     *
     * @param dataFrame
     *      the data frame to copy from
     *
     * @throws IllegalArgumentException if the given data frame is {@code null}
     */
    public ArrayListDataFrame(DataFrame<T> dataFrame) throws IllegalArgumentException {
        if (dataFrame == null) {
            throw new IllegalArgumentException(NULL_DATAFRAME_MSG);
        }
        this.columnNames = new HashSet<>(dataFrame.columns());
        this.rep = new ArrayList<>(dataFrame.rows());
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    @Override
    public final List<DataRow<T>> rows() {
        return this.rep;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException if the given index does not exist in {@code this}
     */
    @Override
    public final DataRow<T> getRow(int index) throws IndexOutOfBoundsException {
        if (index >= this.rep.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return this.rep.get(index);
    }

    @Override
    public final Set<String> columns() {
        return this.columnNames;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given column name does not exist in {@code this}
     */
    @Override
    public final List<T> getColumn(String column) throws IllegalArgumentException {
        if (!this.columnNames.contains(column)) {
            throw new IllegalArgumentException(INVALID_COLUMN_MSG);
        }
        List<T> rv = new ArrayList<>();
        for (DataRow<T> row : this.rep) {
            rv.add(row.getValue(column));
        }
        return rv;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given row does not contain only and all the columns of {@code this}
     * @throws IndexOutOfBoundsException if the given index is out of bounds
     */
    @Override
    public final void addRow(DataRow<T> row, int index) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (index > this.rep.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (row.size() != this.columnNames.size()) {
            throw new IllegalArgumentException(INVALID_COLUMNS_MSG);
        }
        for (String column : this.columnNames) {
            if (!row.columns().contains(column)) {
                throw new IllegalArgumentException(INVALID_COLUMNS_MSG);
            }
        }
        this.rep.add(index, row);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if a column with the given name already exists in {@code this}, or if the size
     *      of {@code column} does not equal the size of {@code this}
     */
    @Override
    public final void addColumn(String columnName, List<T> column) throws IllegalArgumentException {
        if (this.columnNames.contains(columnName)) {
            throw new IllegalArgumentException(COLUMN_ALREADY_EXISTS_MSG);
        }
        if (this.rep.size() != column.size()) {
            throw new IllegalArgumentException(INVALID_COLUMN_SIZE_MSG);
        }
        for (int i = 0; i < this.rep.size(); i++) {
            this.rep.get(i).setValue(columnName, column.get(i));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException if the given index does not exist in {@code this}
     */
    @Override
    public final void removeRow(int index) throws IndexOutOfBoundsException {
        if (index >= this.rep.size()) {
            throw new IndexOutOfBoundsException();
        }
        this.rep.remove(index);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given column name does not exist in {@code this}
     */
    @Override
    public final void removeColumn(String column) throws IllegalArgumentException {
        if (!this.columnNames.contains(column)) {
            throw new IllegalArgumentException(INVALID_COLUMN_MSG);
        }
        for (DataRow<T> row : this.rep) {
            row.removeValue(column);
        }
        this.columnNames.remove(column);
    }

    @Override
    public final int size() {
        return this.rep.size();
    }

}
