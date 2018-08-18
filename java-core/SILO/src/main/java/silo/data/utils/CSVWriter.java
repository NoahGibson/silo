package silo.data.utils;

import silo.data.DataFrame;
import silo.data.DataRow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Object for creating and writing .csv files.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180817
 */
public class CSVWriter {

    /**
     * Writes the given data to the file specified, formatting the output as csv.
     * <p>
     * If a file with the given name doesn't already exist it will be created. If it does exist, it will be overwritten.
     * <p>
     * The columnOrder parameter is used to specify the order in which the columns should be written to output.
     *
     * @param filename
     *      the name of the file to write to
     * @param data
     *      the data frame to output the data of
     * @param columnOrder
     *      the order in which to output the columns. This should be a list of strings, where each string is the name
     *      of the column to write
     *
     * @throws IllegalArgumentException if either the given data or column order is null, or if the column order list
     *      does not contain all and only the columns of the data
     * @throws IOException if there is an error opening, writing, or closing the given file
     */
    public static void write(String filename, DataFrame<?> data, List<String> columnOrder) throws IllegalArgumentException, IOException {
        if (data == null) {
            throw new IllegalArgumentException("input data must not be null");
        } else if (columnOrder == null) {
            throw new IllegalArgumentException("input columnOrder must not be null");
        }
        if (data.columns().size() != columnOrder.size()) {
            throw new IllegalArgumentException("columnOrder must contain all and only the columns of the given data frame");
        }
        for (String column : columnOrder) {
            if (!data.columns().contains(column)) {
                throw new IllegalArgumentException("columnOrder must contain all and only the columns of the given data frame");
            }
        }

        File file = new File(filename);
        FileWriter writer;
        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            throw new IOException("unable to open or create file " + filename + ": " + e.getMessage());
        }

        // Writing data to file
        try {
            // Writing column names
            String headerRow = "";
            for (int i = 0; i < columnOrder.size(); i++) {
                headerRow += columnOrder.get(i);
                if (i < columnOrder.size() - 1) {
                    headerRow += ",";
                } else {
                    headerRow += "\n";
                }
            }
            writer.write(headerRow);

            // Writing rows
            for (DataRow<?> row : data.rows()) {
                String rowString = "";
                for (int i = 0; i < columnOrder.size(); i++) {
                    rowString += row.getValue(columnOrder.get(i));
                    if (i < columnOrder.size() - 1) {
                        rowString += ",";
                    } else {
                        rowString += "\n";
                    }
                }
                writer.write(rowString);
            }
        } catch (IOException e) {
            writer.close();
            throw new IOException("error writing to file " + filename + ": " + e.getMessage());
        }

        // Closing file
        try {
            writer.close();
        } catch (IOException e) {
            throw new IOException("unable to close file " + filename + ": " + e.getMessage());
        }

    }

    /**
     * Writes the given data to the file specified, formatting the output as csv.
     * <p>
     * If a file with the given name doesn't already exist it will be created. If it does exist, it will be overwritten.
     *
     * @param filename
     *      the name of the file to write to
     * @param data
     *      the data frame to output the data of
     *
     * @throws IllegalArgumentException if either the given data is null
     * @throws IOException if there is an error opening, writing, or closing the given file
     */
    public static void write(String filename, DataFrame<?> data) throws IllegalArgumentException, IOException {
        write(filename, data, new ArrayList<>(data.columns()));
    }

}
