package ua.com.juja.yeryery.controller.commands.Utility;

import ua.com.juja.yeryery.controller.commands.Exceptions.CancelException;
import ua.com.juja.yeryery.controller.commands.Exceptions.IllegalArgumentException;
import ua.com.juja.yeryery.model.*;
import ua.com.juja.yeryery.view.View;

import java.util.*;

public class Dialog {

    private View view;
    private DatabaseManager manager;
    private static final String CANCEL_INPUT = "or type 'cancel' to go back";
    private static final String DELIMITER = "\\|";

    public Dialog(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public String selectTable(String action) {
        Map<Integer, String> tableList = getTableList();

        return getRequiredTable(action, tableList);
    }

    private Map<Integer, String> getTableList() {
        Set<String> names = manager.getTableNames();
        Map<Integer, String> result = new HashMap<>();
        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            result.put(i, (String) iterator.next());
            i++;
        }
        return result;
    }

    private String getRequiredTable(String action, Map<Integer, String> tableList) {
        while (true) {
            printTableList(action, tableList);
            String input = view.read().toLowerCase();

            try {
                checkCancelOrZero(input);
                return findInputTable(input, tableList);
            } catch (IllegalArgumentException e) {
                view.write(e.getExtendedMessage());
            }
        }
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

    private String findInputTable(String input, Map<Integer, String> tableList) {
        String tableName;

        if (Parser.isParsable(input)) {
            int tableNumber = Parser.parsedInt;

            checkTableNumber(tableNumber, tableList);
            tableName = tableList.get(tableNumber);
        } else {
            checkTableName(input, tableList);
            tableName = input;
        }
        return tableName.toLowerCase();
    }

    private void checkCancelOrZero(String input) {
        if (input.equalsIgnoreCase("cancel") || input.equals("0")) {
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

    public String nameTable() {
        Set<String> names = manager.getTableNames();
        return assignNewTableName(names);
    }

    private String assignNewTableName(Set<String> names) {
        String message = "Enter the name of your table " + CANCEL_INPUT;

        while (true) {
            view.write(message);
            String result = view.read().toLowerCase();

            try {
                checkNewName(names, result);
                return result;
            } catch (IllegalArgumentException e) {
                view.write(e.getExtendedMessage());
            }
        }
    }

    private void checkNewName(Set<String> names, String tableName) {
        checkFirstLetter(tableName);
        checkCancel(tableName);
        checkExistsName(names, tableName);
    }

    private void checkFirstLetter(String name) {
        char firstLetter = name.charAt(0);
        boolean isFirstLetter = (firstLetter >= 'a' && firstLetter <= 'z') || (firstLetter >= 'A' && firstLetter <= 'Z');

        if (!isFirstLetter) {
            throw new IllegalArgumentException(String.format("You have entered '%s' and name must begin with a letter", name));
        }
    }

    private void checkCancel(String input) {
        if (input.equalsIgnoreCase("cancel")) {
            throw new CancelException();
        }
    }

    private void checkExistsName(Set<String> names, String tableName) {
        if (names.contains(tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' already exists\n%s", tableName, names.toString()));
        }
    }

    public void confirmAction(String action, String tableName) {
        String confirm = "";
        String warning = String.format("Are you sure you want to %s table '%s'?", action, tableName);

        while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n")) {
            view.write(warning + " (y/n)");
            confirm = view.read();
        }

        if (confirm.equalsIgnoreCase("n")) {
            throw new CancelException();
        }
    }

    public DataEntry findRow(String tableName, String action) {
        String inputSample = "columnName|value";
        String message = String.format("Select the row you want to %s: " +
                "%s\n%s", action, inputSample, CANCEL_INPUT);

        while (true) {
            try {
                DataEntry result = getEntry(message, inputSample);
                checkEntry(tableName, result);
                return result;
            } catch (IllegalArgumentException e) {
                view.write(e.getExtendedMessage());
            }
        }
    }

    private DataEntry getEntry(String message, String sample) {
        String[] splitInput = splitBySample(message, sample);
        String columnName = splitInput[0];
        Object value = Parser.defineType(splitInput[1]);
        DataEntry result = new DataEntryImpl();

        result.setEntry(columnName, value);
        return result;
    }

    private String[] splitBySample(String message, String sample) {
        String input = getInput(message);

        checkSizeBySample(input, sample);
        return input.split(DELIMITER);
    }

    private String getInput(String message) {
        String result;

        view.write(message);
        result = view.read();
        checkCancel(result);
        return result;
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
        Object value = entry.getValue();

        checkColumn(tableName, columnName);
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

    public String[] splitConnectInput(String input, String sample) {
        checkSizeBySample(input, sample);
        return input.split(DELIMITER);
    }

    public DataSet getNewEntries(String tableName, String action) {
        String message = newEntriesMessage(action);

        while (true) {
            try {
                DataSet result = getEntries(message);
                checkInputColumns(tableName, result);
                return result;
            } catch (IllegalArgumentException e) {
                view.write(e.getExtendedMessage());
            }
        }
    }

    private String newEntriesMessage(String action) {
        String inputSample = "columnName1|newValue1|columnName2|newValue2|...";
        return String.format("Enter the columnNames and its values of the row you want to %s:\n" +
                "%s\n%s", action, inputSample, CANCEL_INPUT);
    }

    private DataSet getEntries(String message) {
        String[] splitInput = splitByPairs(message);
        DataSet result = new DataSetImpl();

        for (int i = 0; i < splitInput.length; i++) {
            String columnName = splitInput[i++];
            Object value = Parser.defineType(splitInput[i]);

            result.put(columnName, value);
        }
        return result;
    }

    private String[] splitByPairs(String message) {
        String input = getInput(message);

        checkEvenSize(input);
        return input.split(DELIMITER);
    }

    private void checkEvenSize(String input) {
        int inputSize = count(input);

        if (inputSize % 2 != 0) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected even number of parameters (2, 4 and so on), and you have entered %s", inputSize));
        }
    }

    private void checkInputColumns(String tableName, DataSet dataSet) {
        Set<String> checkedColumns = dataSet.getColumnNames();

        for (String columnName : checkedColumns) {
            checkColumn(tableName, columnName);
        }
    }

    private int count(String input) {
        String[] splitData = input.split(DELIMITER);
        return splitData.length;
    }

    public DataSet getNewColumns(String action) {
        String message = newColumnsMessage(action);

        while (true) {
            DataSet result = getEntries(message);

            try {
                checkNewColumns(result);
                return result;
            } catch (IllegalArgumentException e) {
                view.write(e.getExtendedMessage());
            }
        }
    }

    private String newColumnsMessage(String action) {
        String inputSample = "columnName1|dataType1|columnName2|dataType2|...";
        return String.format("Enter the columnNames and its dataTypes of the table you want to %s:\n" +
                "%s\n%s", action, inputSample, CANCEL_INPUT);
    }

    private void checkNewColumns(DataSet dataSet) {
        Set<String> inputColumns = dataSet.getColumnNames();

        for (String columnName : inputColumns) {
            checkFirstLetter(columnName);
        }
    }

    public DataEntry getConstraintColumn() {
        String inputSample = "columnName|dataType";
        String message = String.format("Enter the name of PRIMARY KEY column and its dataType: " +
                "%s\n%s", inputSample, CANCEL_INPUT);

        while (true) {
            DataEntry result = getEntry(message, inputSample);

            try {
                checkFirstLetter(result.getColumn());
                return result;
            } catch (IllegalArgumentException e) {
                view.write(e.getExtendedMessage());
            }
        }
    }
}