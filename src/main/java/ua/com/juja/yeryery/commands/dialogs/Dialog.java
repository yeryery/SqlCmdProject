package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DataSet;

public interface Dialog {
    String selectTable(String message);

    String nameTable(String message);

    void confirmAction(String warning);

    DataEntry findRow(String tableName, String action, String sample);

    DataSet setValues(String tableName, String message, DataEntry entry);

    DataSet getInputByTwo(String message);
}
