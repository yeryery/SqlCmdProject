package ua.com.juja.yeryery.manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    Set<String> getTableNames();

    Set<String> getDatabases();

    Set<String> getTableColumns(String tableName);

    void clear(String tableName);

    void create(String tableName, DataSet columns) throws SQLException;

    void createDB(String dataBase);

    void drop(String tableName);

    void dropDB(String dataBaseName);

    boolean isConnected();

    void insert(String tableName, DataSet input) throws SQLException;

    void update(String tableName, DataSet input, String columnName, Object definingValue) throws SQLException;

    void delete(String tableName, String columnName, Object value);

    List<DataSet> getDataContent(String tableName);
}
