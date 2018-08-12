package silo.data.utils;

import silo.data.ArrayListDataFrame;
import silo.data.DataFrame;
import silo.data.DataRow;
import silo.data.HashMapDataRow;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Object for parsing .csv files.
 *
 * @author Noah Gibson
 * @version 1.0
 * @since 20180811
 */
public class CSVParser {

    /**
     * Parses the given csv file and stores the data in a {@link DataFrame} object, reading each value in as a string.
     *
     * @param filename
     *      the name of the csv value to parse
     * @param headersAsFirstRow
     *      whether or not to use the first row of the csv file as header names. If false, then the names of the columns
     *      in the returned DataFrame will be of the format 'column_#', where # is the index of the column in the
     *      original csv file (with indexing starting at 0)
     *
     * @return a new DataFrame object containing the data in the given csv file
     *
     * @throws IOException if there is an issue opening, reading, or closing the given file
     */
    public static DataFrame<String> parseString(String filename, boolean headersAsFirstRow) throws IOException {
        if (!filename.matches(".+\\.csv$")) {
            throw new IOException("the given file must be .csv");
        }
        Scanner input;
        File csvFile;
        try {
            csvFile = new File(filename);
            input = new Scanner(csvFile);
        } catch (IOException e) {
            throw new IOException("unable to open file " + filename + ": " + e.getMessage());
        }
        if (!input.hasNext()) {
            throw new IOException("the file " + filename + " is empty");
        }

        // Get column names
        List<String> columns;
        Set<String> columnsSet;
        if (headersAsFirstRow) {
            String firstLine = input.nextLine();
            String[] columnNames = firstLine.split(",", -1);
            columns = new ArrayList<>(Arrays.asList(columnNames));
            columnsSet = new HashSet<>(columns);
        } else {
            columns = new ArrayList<>();
            Scanner temp = new Scanner(csvFile);
            String firstLine = temp.nextLine();
            temp.close();
            String[] firstValues = firstLine.split(",", -1);
            for (int i = 0; i < firstValues.length; i++) {
                columns.add("column_" + i);
            }
            columnsSet = new HashSet<>(columns);
        }

        // Read in the data in the file
        DataFrame<String> rv = new ArrayListDataFrame<>(columnsSet);
        while (input.hasNext()) {
            String line = input.nextLine();
            String[] values = line.split(",", -1);
            DataRow<String> row = new HashMapDataRow<>();
            for (int i = 0; i < values.length; i++) {
                row.setValue(columns.get(i), values[i]);
            }
            rv.addRow(row, rv.size());
        }

        input.close();

        return rv;
    }

    /**
     * Parses the given csv file and stores the data in a {@link DataFrame} object, reading each value in as a string
     * and using the first row as column names.
     *
     * @param filename
     *      the name of the csv value to parse
     *
     * @return a new DataFrame object containing the data in the given csv file
     *
     * @throws IOException if there is an issue opening, reading, or closing the given file
     */
    public static DataFrame<String> parseString(String filename) throws IOException {
        return parseString(filename, true);
    }

    /**
     * Parses the given csv file and stores the data in a {@link DataFrame} object, reading each value in as a double.
     *
     * @param filename
     *      the name of the csv value to parse
     * @param headersAsFirstRow
     *      whether or not to use the first row of the csv file as header names. If false, then the names of the columns
     *      in the returned DataFrame will be of the format 'column_#', where # is the index of the column in the
     *      original csv file (with indexing starting at 0)
     *
     * @return a new DataFrame object containing the data in the given csv file
     *
     * @throws IOException if there is an issue opening, reading, or closing the given file
     * @throws NumberFormatException if any non-column name value in the csv file is unable to be parsed as a double
     */
    public static DataFrame<Double> parseDouble(String filename, boolean headersAsFirstRow) throws IOException, NumberFormatException {
        if (!filename.matches(".+\\.csv$")) {
            throw new IOException("the given file must be .csv");
        }
        Scanner input;
        File csvFile;
        try {
            csvFile = new File(filename);
            input = new Scanner(csvFile);
        } catch (IOException e) {
            throw new IOException("unable to open file " + filename + ": " + e.getMessage());
        }
        if (!input.hasNext()) {
            throw new IOException("the file " + filename + " is empty");
        }

        // Get column names
        List<String> columns;
        Set<String> columnsSet;
        if (headersAsFirstRow) {
            String firstLine = input.nextLine();
            String[] columnNames = firstLine.split(",", -1);
            columns = new ArrayList<>(Arrays.asList(columnNames));
            columnsSet = new HashSet<>(columns);
        } else {
            columns = new ArrayList<>();
            Scanner temp = new Scanner(csvFile);
            String firstLine = temp.nextLine();
            temp.close();
            String[] firstValues = firstLine.split(",", -1);
            for (int i = 0; i < firstValues.length; i++) {
                columns.add("column_" + i);
            }
            columnsSet = new HashSet<>(columns);
        }

        // Read in the data in the file
        DataFrame<Double> rv = new ArrayListDataFrame<>(columnsSet);
        while (input.hasNext()) {
            String line = input.nextLine();
            String[] values = line.split(",", -1);
            DataRow<Double> row = new HashMapDataRow<>();
            for (int i = 0; i < values.length; i++) {
                try {
                    row.setValue(columns.get(i), Double.parseDouble(values[i]));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("unable to parse \"" + values[i] + "\" as a double");
                }
            }
            rv.addRow(row, rv.size());
        }

        input.close();

        return rv;
    }

    /**
     * Parses the given csv file and stores the data in a {@link DataFrame} object, reading each value in as a double
     * and using the first row as column names.
     *
     * @param filename
     *      the name of the csv value to parse
     *
     * @return a new DataFrame object containing the data in the given csv file
     *
     * @throws IOException if there is an issue opening, reading, or closing the given file
     * @throws NumberFormatException if any non-column name value in the csv file is unable to be parsed as a double
     */
    public static DataFrame<Double> parseDouble(String filename) throws IOException, NumberFormatException {
        return parseDouble(filename, true);
    }

    /**
     * Parses the given csv file and stores the data in a {@link DataFrame} object, reading each value in as an integer.
     *
     * @param filename
     *      the name of the csv value to parse
     * @param headersAsFirstRow
     *      whether or not to use the first row of the csv file as header names. If false, then the names of the columns
     *      in the returned DataFrame will be of the format 'column_#', where # is the index of the column in the
     *      original csv file (with indexing starting at 0)
     *
     * @return a new DataFrame object containing the data in the given csv file
     *
     * @throws IOException if there is an issue opening, reading, or closing the given file
     * @throws NumberFormatException if any non-column name value in the csv file is unable to be parsed as a double
     */
    public static DataFrame<Integer> parseInt(String filename, boolean headersAsFirstRow) throws IOException, NumberFormatException {
        if (!filename.matches(".+\\.csv$")) {
            throw new IOException("the given file must be .csv");
        }
        Scanner input;
        File csvFile;
        try {
            csvFile = new File(filename);
            input = new Scanner(csvFile);
        } catch (IOException e) {
            throw new IOException("unable to open file " + filename + ": " + e.getMessage());
        }
        if (!input.hasNext()) {
            throw new IOException("the file " + filename + " is empty");
        }

        // Get column names
        List<String> columns;
        Set<String> columnsSet;
        if (headersAsFirstRow) {
            String firstLine = input.nextLine();
            String[] columnNames = firstLine.split(",", -1);
            columns = new ArrayList<>(Arrays.asList(columnNames));
            columnsSet = new HashSet<>(columns);
        } else {
            columns = new ArrayList<>();
            Scanner temp = new Scanner(csvFile);
            String firstLine = temp.nextLine();
            temp.close();
            String[] firstValues = firstLine.split(",", -1);
            for (int i = 0; i < firstValues.length; i++) {
                columns.add("column_" + i);
            }
            columnsSet = new HashSet<>(columns);
        }

        // Read in the data in the file
        DataFrame<Integer> rv = new ArrayListDataFrame<>(columnsSet);
        while (input.hasNext()) {
            String line = input.nextLine();
            String[] values = line.split(",", -1);
            DataRow<Integer> row = new HashMapDataRow<>();
            for (int i = 0; i < values.length; i++) {
                try {
                    row.setValue(columns.get(i), Integer.parseInt(values[i]));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("unable to parse \"" + values[i] + "\" as an integer");
                }
            }
            rv.addRow(row, rv.size());
        }

        input.close();

        return rv;
    }

    /**
     * Parses the given csv file and stores the data in a {@link DataFrame} object, reading each value in as an integer
     * and using the first row as column names.
     *
     * @param filename
     *      the name of the csv value to parse
     *
     * @return a new DataFrame object containing the data in the given csv file
     *
     * @throws IOException if there is an issue opening, reading, or closing the given file
     * @throws NumberFormatException if any non-column name value in the csv file is unable to be parsed as a double
     */
    public static DataFrame<Integer> parseInt(String filename) throws IOException, NumberFormatException {
        return parseInt(filename, true);
    }

}
