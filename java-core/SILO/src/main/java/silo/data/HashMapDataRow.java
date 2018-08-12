package silo.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * HashMap implementation of a {@link DataRow}.
 *
 * @param <T>
 *      the type of values stored in the row
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180811
 */
public class HashMapDataRow<T> implements DataRow<T> {

    /*
     * PRIVATE MEMBERS -------------------------------------------------------------------------------------------------
     */

    /**
     * HashMap representation of {@code this}.
     */
    private HashMap<String, T> rep;


    /*
     * CONSTANTS -------------------------------------------------------------------------------------------------------
     */

    private static final String INVALID_COLUMN_MSG = "the specified column does not exist";

    private static final String NULL_DATAROW_MSG = "dataRow must not be null";


    /*
     * OVERRIDDEN METHODS ----------------------------------------------------------------------------------------------
     */

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashMapDataRow<?> that = (HashMapDataRow<?>) o;
        return Objects.equals(rep, that.rep);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(rep);
    }

    @Override
    public String toString() {
        return rep.toString();
    }


    /*
     * CONSTRUCTORS ----------------------------------------------------------------------------------------------------
     */

    /**
     * Initializes an empty {@link HashMapDataRow}.
     */
    public HashMapDataRow() {
        this.rep = new HashMap<>();
    }

    /**
     * Creates a new {@link HashMapDataRow} copied from the given data row.
     *
     * @param dataRow
     *      the data row to copy from
     *
     * @throws IllegalArgumentException if the given data row is {@code null}
     */
    public HashMapDataRow(DataRow<T> dataRow) throws IllegalArgumentException {
        if (dataRow == null) {
            throw new IllegalArgumentException(NULL_DATAROW_MSG);
        }
        this.rep = new HashMap<>(dataRow.values());
    }


    /*
     * PUBLIC METHODS --------------------------------------------------------------------------------------------------
     */

    @Override
    public final Map<String, T> values() {
        return this.rep;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given column does not exist in {@code this}
     */
    @Override
    public final T getValue(String column) throws IllegalArgumentException {
        if (!this.rep.containsKey(column)) {
            throw new IllegalArgumentException(INVALID_COLUMN_MSG);
        }
        return this.rep.get(column);
    }

    @Override
    public final void setValue(String column, T value) {
        this.rep.put(column, value);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the given column does not exist in {@code this}
     */
    @Override
    public final void removeValue(String column) throws IllegalArgumentException {
        if (!this.rep.containsKey(column)) {
            throw new IllegalArgumentException(INVALID_COLUMN_MSG);
        }
        this.rep.remove(column);
    }

    @Override
    public final Set<String> columns() {
        return this.rep.keySet();
    }

    @Override
    public final int size() {
        return this.rep.size();
    }

}
