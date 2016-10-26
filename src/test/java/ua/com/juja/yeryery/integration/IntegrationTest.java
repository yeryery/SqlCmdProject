package ua.com.juja.yeryery.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yeryery.Main;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private LogOutputStream out;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void TestExit() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestHelp() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //help
                "List of commands:\r\n" +
                "\tconnect|database|username|password\r\n" +
                "\t\tConnect to Database\r\n" +
                "\tlist\r\n" +
                "\t\tList of tables\r\n" +
                "\tcreate\r\n" +
                "\t\tCreate new table\r\n" +
                "\tdisplay\r\n" +
                "\t\tDisplay require table\r\n" +
                "\tinsert\r\n" +
                "\t\tInsert new data in require table\r\n" +
                "\tclear\r\n" +
                "\t\tClear require table\r\n" +
                "\tdrop\r\n" +
                "\t\tDrop require table\r\n" +
                "\texit\r\n" +
                "\t\tProgram exit\r\n" +
                "\thelp\r\n" +
                "\t\tAll commands\r\n" +
                "Type command or 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestListWithoutConnect() {
        //given
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //list
                "You can`t use 'list' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestClearWithoutConnect() {
        //given
        in.add("clear");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //clear
                "You can`t use 'clear' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestCreateWithoutConnect() {
        //given
        in.add("create");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //create
                "You can`t use 'create' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestDisplayWithoutConnect() {
        //given
        in.add("display");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //display
                "You can`t use 'display' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestDropWithoutConnect() {
        //given
        in.add("drop");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //drop
                "You can`t use 'drop' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestInsertWithoutConnect() {
        //given
        in.add("insert");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //insert
                "You can`t use 'insert' unless you are not connected.\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestUnknownCommandBeforeConnect() {
        //given
        in.add("someCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //someCommand
                "Unknown command: someCommand!\r\n" +
                "Try again.\r\n" +
                "Type command or 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestConnectWithWrongNumberOfParameters() {
        //given
        in.add("connect|yeryery");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //connect|yeryery
                "Error! Wrong number of parameters. Expected 4, and you have entered 2\r\n" +
                "Try again.\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestConnectWithWrongParameter() {
        //given
        in.add("connect|yeryery|postgres|wrongpass");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                "Error! org.postgresql.util.PSQLException: FATAL: password authentication failed for user \"postgres\"\r\n" +
                "Try again.\r\n" +
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestUnknownCommandAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("someCommand");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\r\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\r\n" +
                //connect
                "Success!\r\n" +
                "Type command or 'help'\r\n" +
                //someCommand
                "Unknown command: someCommand!\r\n" +
                "Try again.\r\n" +
                "Type command or 'help'\r\n" +
                //exit
                "See you!\r\n", out.getData());
    }

    @Test
    public void TestListAfterConnect() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //list
                "[test, ttable]\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDisplayAndSelectNumberOfTable() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("1");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //select table number 1
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDisplayAndSelectTablename() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("test");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //select test
                "| id | login | password | \n" +
                "-------------------------\n" +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDisplayChooseWrongNumber() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("display");
        in.add("-1");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //-1
                "There is no table with this number! Try again.\n" +
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //cancel
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateWithExistingName() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("test");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //test
                "Table with name 'test' already exists.\n" +
                "[test, ttable]\n" +
                "Try again.\n" +
                //cancel
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateAndCancelAfterNaming() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or '0' to go back\n" +
                //-1
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateWithNegativeTableSize() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("-1");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or '0' to go back\n" +
                //-1
                "Number must be positive! Try again (or type '0' to go back).\n" +
                "Please enter the number of columns of your table or '0' to go back\n" +
                //0
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateWithSqlErrorWhenNameisNumeric() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("111");
        in.add("1");
        in.add("name");
        in.add("text");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //111
                "Please enter the number of columns of your table or '0' to go back\n" +
                //1
                "name of column 1\n" +
                //name
                "datatype of column 1\n" +
                //text
                "ERROR: syntax error at or near \"111\"\n" +
                "  Position: 14\n" +
                //0
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateWithSqlErrorWhenDatatypeDoesntExist() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("1");
        in.add("name");
        in.add("wrongtype");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or '0' to go back\n" +
                //1
                "name of column 1\n" +
                //name
                "datatype of column 1\n" +
                //wrongtype
                "ERROR: type \"wrongtype\" does not exist\n" +
                "  Position: 57\n" +
                //0
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestCreateAfterConnectAndDrop() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("create");
        in.add("somename");
        in.add("2");
        in.add("name");
        in.add("text");
        in.add("age");
        in.add("int");
        in.add("display");
        in.add("somename");
        in.add("drop");
        in.add("somename");
        in.add("y");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //create
                "Please enter the name of table you want to create or 'cancel' to go back\n" +
                //somename
                "Please enter the number of columns of your table or '0' to go back\n" +
                //2
                "name of column 1\n" +
                //name
                "datatype of column 1\n" +
                //text
                "name of column 2\n" +
                //age
                "datatype of column 2\n" +
                //int
                "Your table 'somename' have successfully created!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you need\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. ttable\n" +
                "0. cancel (to go back)\n" +
                //somename
                "| id | name | age | \n" +
                "-------------------------\n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //drop
                "Please enter the name or select number of table you need\n" +
                "1. somename\n" +
                "2. test\n" +
                "3. ttable\n" +
                "0. cancel (to go back)\n" +
                //somename
                "Are you sure you want to drop table 'somename'? (y/n)\n" +
                //y
                "Table 'somename' successfully dropped!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestInsertAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //insert
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Table clearing canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestInsertWrongSql() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("ttable");
        in.add("notnumber");
        in.add("somename");
        in.add("25");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //insert
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the values you require\n" +
                //notnumber
                "id\n" +
                //somename
                "name\n" +
                //25
                "age\n" +
                "ERROR: invalid input syntax for integer: \"notnumber\"\n" +
                "  Position: 41\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestInsertAndClear() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("insert");
        in.add("ttable");
        in.add("10");
        in.add("somename");
        in.add("25");
        in.add("display");
        in.add("ttable");
        in.add("clear");
        in.add("ttable");
        in.add("y");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //insert
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Enter the values you require\n" +
                //10
                "id\n" +
                //somename
                "name\n" +
                //25
                "age\n" +
                "You have successfully entered new data!\n" +
                "Type command or 'help'\n" +
                //display
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "| id | name | age | \n" +
                "-------------------------\n" +
                "| 10 | somename | 25 | \n" +
                "------------------------\n" +
                "Type command or 'help'\n" +
                //clear
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //ttable
                "Are you sure you want to clear table 'ttable'? (y/n)\n" +
                //y
                "Table 'ttable' successfully cleared!\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDropAndCancel() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("drop");
        in.add("cancel");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //drop
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //cancel
                "Table dropping canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }

    @Test
    public void TestDropAndSelectNull() {
        //given
        in.add("connect|yeryery|postgres|postgrespass");
        in.add("drop");
        in.add("0");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Hello, user!\n" +
                "Please, enter: 'connect|database|username|password' or use command 'help'\n" +
                //connect
                "Success!\n" +
                "Type command or 'help'\n" +
                //drop
                "Please enter the name or select number of table you need\n" +
                "1. test\n" +
                "2. ttable\n" +
                "0. cancel (to go back)\n" +
                //0
                "Table dropping canceled\n" +
                "Type command or 'help'\n" +
                //exit
                "See you!", out.getData().trim().replace("\r",""));
    }
}
