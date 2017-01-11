package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.*;
import ua.com.juja.yeryery.view.View;

import java.util.*;

public class Dialog {

    private View view;
    private DatabaseManager manager;
    private String cancelInput = "or type 'cancel' to go back";
    private String delimiter = "\\|";

    public Dialog(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public String selectTable(String action) {
        Map<Integer, String> tableList = getTableList();
        String tableName;

        while (true) {
            printTableList(action, tableList);

            try {
                tableName = getTableName(tableList);
                break;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
        return tableName;
    }

    private Map<Integer, String> getTableList() {
        Set<String> names = manager.getTableNames();
        Map<Integer, String> tableList = new HashMap<>();
        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            tableList.put(i, (String) iterator.next());
            i++;
        }
        return tableList;
    }

    private void printTableList(String action, Map<Integer, String> tableList) {
        String message = String.format("Select the table you need for '%s' command", action);
        view.write(message);
        tableList.remove(0);

        for (Map.Entry<Integer, String> entry : tableList.entrySet()) {
            view.write(entry.getKey() + ". " + entry.getValue());
        }

        view.write(0 + ". " + "cancel (to go back)");
    }

    private String getTableName(Map<Integer, String> tableList) {
        String input = view.read();
        checkCancelOrZero(input);

        String tableName;
        if (Parser.isParsable(input)) {
            int tableNumber = Parser.parsedInt;
            checkTableNumber(tableNumber, tableList);
            tableName = tableList.get(tableNumber);
        } else {
            checkTableName(input, tableList);
            tableName = input;
        }
        return tableName;
    }

    private void checkCancelOrZero(String input) {
        if (input.equals("cancel") || input.equals("0")) {
            throw new CancelException();
        }
    }

    private void checkTableNumber(int tableNumber, Map<Integer, String> tableList) {
        if (!tableList.containsKey(tableNumber)) {
            throw new IllegalArgumentException(String.format("There is no table with number %d", tableNumber));
        }
    }

    private void checkTableName(String tableName, Map<Integer, String> tableList) {
        if (!tableList.containsValue(tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' doesn't exist", tableName));
        }
    }

    public void confirmAction(String action, String tableName) {
        String confirm = "";
        String warning = String.format("Are you sure you want to %s table '%s'?", action, tableName);

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(warning + " (y/n)");
            confirm = view.read();
        }

        if (confirm.equals("n")) {
            throw new CancelException();
        }
    }

    public DataEntry findRow(String tableName, String action) {
        String inputSample = "columnName|value";
        String message = String.format("Enter the columnName and defining value of the row you want to %s: " +
                "%s\n%s", action, inputSample, cancelInput);

        while (true) {
            try {
                DataEntry entry = getEntry(message, inputSample);
                checkEntry(tableName, entry);
                return entry;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
    }

    private DataEntry getEntry(String message, String sample) {
        String[] splitInput = splitBySample(message, sample);

        String columnName = splitInput[0];
        Object value = Parser.defineType(splitInput[1]);

        return new DataEntryImpl(columnName, value);
    }

    public String[] splitBySample(String message, String sample) {
        String input = getInput(message);
        checkSizeBySample(input, sample);

        return input.split(delimiter);
    }

    private String getInput(String message) {
        view.write(message);
        String input = view.read();
        checkCancel(input);
        return input;
    }

    private static void checkCancel(String input) {
        if (input.equals("cancel")) {
            throw new CancelException();
        }
    }

    private void checkSizeBySample(String input, String sample) {
        int sampleSize = count(sample);
        int inputSize = count(input);

        if (inputSize != sampleSize) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s parameters, and you have entered %s", sampleSize, inputSize));
        }
    }

    private void checkEntry(String tableName, DataEntry entry) {
        String columnName = entry.getColumn();
        checkColumn(tableName, columnName);
        Object value = entry.getValue();
        checkValue(tableName, columnName, value);
    }

    private void checkColumn(String tableName, String columnName) {
        Set<String> tableColumns = manager.getTableColumns(tableName);
        if (!tableColumns.contains(columnName)) {
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'", tableName, columnName));
        }
    }

    private void checkValue(String tableName, String columnName, Object value) {
        List<DataSet> tableContent = manager.getDataContent(tableName);
        List<Object> columnValues = getColumnValues(tableContent, columnName);
        if (!columnValues.contains(value)) {
            throw new IllegalArgumentException(String.format("Column '%s' doesn't contain value '%s'", columnName, value));
        }
    }

    private List<Object> getColumnValues(List<DataSet> dataSets, String columnName) {
        List<Object> result = new LinkedList<>();

        for (DataSet dataSet : dataSets) {
            result.add(dataSet.get(columnName));
        }

        return result;
    }

    public DataSet getNewEntries(String tableName, String action) {
        String inputSample = "columnName1|newValue1|columnName2|newValue2|...";
        String message = String.format("Enter the columnNames and its values of the row you want to %s:\n" +
                "%s\n%s", action, inputSample, cancelInput);

        while (true) {
            try {
                DataSet newEntries = getEntries(message);
                checkColumns(tableName, newEntries);
                return newEntries;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
    }

    public DataSet getEntries(String message) {
        String[] splitInput = splitByPairs(message);

        DataSet splitDataSet = new DataSetImpl();
        for (int i = 0; i < splitInput.length; i++) {
            String columnName = splitInput[i];
            i++;
            Object value = Parser.defineType(splitInput[i]);
            splitDataSet.put(columnName, value);
        }

        return splitDataSet;
    }

    private String[] splitByPairs(String message) {
        String input = getInput(message);
        checkEvenSize(input);

        return input.split(delimiter);
    }

    private void checkEvenSize(String input) {
        int inputSize = count(input);

        if (inputSize % 2 != 0) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected even number of parameters (2, 4 and so on), and you have entered %s", inputSize));
        }
    }

    private void checkColumns(String tableName, DataSet dataSet) {
        Set<String> checkedColumns = dataSet.getColumnNames();

        for (String columnName : checkedColumns) {
            checkColumn(tableName, columnName);
        }
    }

    private int count(String input) {
        String[] splitData = input.split(delimiter);
        return splitData.length;
    }
}
