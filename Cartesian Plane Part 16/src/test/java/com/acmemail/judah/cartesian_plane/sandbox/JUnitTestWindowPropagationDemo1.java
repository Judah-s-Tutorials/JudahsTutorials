package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Window;
import java.util.Arrays;

import javax.swing.JDialog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.components.ItemSelectionDialog;

class JUnitTestWindowPropagationDemo1
{
    @BeforeEach
    public void beforeEach() throws Exception
    {
        System.out.println( "===== BeforeEach Start =====" );
        Arrays.stream( Window.getWindows() ).forEach( w -> {
            String  title   = "none";
            if ( w instanceof JDialog )
                title = ((JDialog)w).getTitle();
            System.out.println( w.getClass().getName() );
            System.out.println( title);
            System.out.println( "visible: " + w.isVisible() );
            System.out.println( "displayable: " + w.isDisplayable() );
            System.out.println( "---------------------" );
        });
        System.out.println( "===== BeforeEach End =====" );
    }

    @ParameterizedTest
    @ValueSource ( ints= {1, 2, 3, 4 } )
    void test()
    {
        String[]            items   = { "Item 1", "Item 2", "Item 3" };
        ItemSelectionDialog dialog  = 
            new ItemSelectionDialog( "JUnit Propagation Test", items );
        dialog.show();
    }
}