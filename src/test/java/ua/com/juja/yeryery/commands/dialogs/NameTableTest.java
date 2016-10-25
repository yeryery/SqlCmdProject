package ua.com.juja.yeryery.commands.dialogs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.view.View;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameTableTest {

    private View view;
    private Dialog dialog;

    @Before
    public void setup() {
        view = mock(View.class);
        dialog = new NameTable();
    }

    @Test
    public void testNameTableWithOriginalName() {
        //given
        String[] tableNames = new String[]{"test", "table"};
        when(view.read()).thenReturn("myTable");

        //when
        String actual = dialog.askUser(tableNames, view);

        //then
        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back]");
        //myTable
        assertEquals("myTable", actual);
    }

    @Test
    public void testNameTableWithExistingName() {
        //given
        String[] tableNames = new String[]{"test", "table"};
        when(view.read()).thenReturn("test").thenReturn("myTable");

        //when
        String actual = dialog.askUser(tableNames, view);

        //then
        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                    //test
                    "Table with name 'test' already exists., " +
                    "[test, table], " +
                    "Try again.]");
                    //myTable
        assertEquals("myTable", actual);
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
