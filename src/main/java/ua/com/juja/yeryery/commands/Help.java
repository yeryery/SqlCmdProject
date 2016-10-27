package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.view.View;

public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("help");
    }

    @Override
    public void process(String input) {
        view.write("Content of commands:");

        view.write("\tconnect|database|username|password");
        view.write("\t\tConnect to Database");

        view.write("\tlist");
        view.write("\t\tContent of tables");

        view.write("\tcreate");
        view.write("\t\tCreate new table");

        view.write("\tdisplay");
        view.write("\t\tDisplay require table");

        view.write("\tinsert");
        view.write("\t\tInsert new data in require table");

        view.write("\tclear");
        view.write("\t\tClear require table");

        view.write("\tdrop");
        view.write("\t\tDrop require table");

        view.write("\texit");
        view.write("\t\tProgram exit");

        view.write("\thelp");
        view.write("\t\tAll commands");
    }

}
