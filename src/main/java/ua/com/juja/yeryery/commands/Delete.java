package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Delete implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "delete";

    public Delete(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Dialog dialog = new Dialog(view, manager);
        String findRowMessage = "Enter the columnName and defining value of deleted row: %s\nor type 'cancel' to go back";
        String commandSample = "columnName|value";

        String currentTableName;
        DataEntry definingEntry;

        currentTableName = dialog.selectTable(ACTION);
        definingEntry = dialog.defineRow(currentTableName, findRowMessage, commandSample);

        TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);

        manager.delete(currentTableName, definingEntry);
        view.write(String.format("You have successfully deleted data from '%s'", currentTableName));

        tablePrinter.print();
    }

}
