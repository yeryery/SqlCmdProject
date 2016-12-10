package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

public class Insert implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "insert";

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> tableNames = manager.getTableNames();
        Dialog dialog = new SelectTable();
        String currentTableName = dialog.askUser(tableNames, view, ACTION);

        if (!currentTableName.equals("cancel")) {
            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            int tableSize = -1;

            while (tableSize < 0) {
                view.write("Enter new values you require");

                tableSize = tableColumns.size();
                String[] columnNames = tableColumns.toArray(new String[tableSize]);
                String[] values = new String[tableSize];
                DataSet newRow = new DataSetImpl();

                for (int i = 0; i < tableSize; i++) {
                    view.write(columnNames[i]);
                    values[i] = view.read();
                    newRow.put(columnNames[i], values[i]);
                }

                try {
                    manager.insert(currentTableName, newRow);
                    view.write("You have successfully entered new data into the table '" + currentTableName + "'");
                } catch (SQLException e) {
                    String errorMessage = editErrorMessage(e);
                    view.write(errorMessage);
                    tableSize = -1;
                }
            }
        } else {
            view.write("Table inserting canceled");
        }
    }

    private String editErrorMessage(SQLException e) {
        String result = "SQL " + e.getMessage();
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '\n') {
                result = result.substring(0, i);
                break;
            }
        }
        return result + "!\nTry again.";
    }
}
