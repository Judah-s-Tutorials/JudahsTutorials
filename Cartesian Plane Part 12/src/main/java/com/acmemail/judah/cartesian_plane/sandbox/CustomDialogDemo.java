package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;

/**
 * Program to demonstrate how to use the ItemSelectionDialog.
 * 
 * @author Jack Straub
 */
public class CustomDialogDemo
{
    /** List of items to be presented in the dialog. */
    public static final String[] names =
    {
        "just another list item 1",
        "just another list item 2",
        "just another list item 3",
        "just another list item 4",
        "just another list item 6",
        "just another list item 7",
        "just another list item 8",
        "just another list item 9",
        "just another list item 10",
        "just another list item 11",
        "just another list item 13",
        "just another list item 14",
        "just another list item 15",
        "just another list item 16",
        "just another list item 17",
        "just another list item 18",
        "just another list item 19",
        "just another list item 20",
    };
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        String              title   = "Name Selector";
        ItemSelectionDialog gui     = new ItemSelectionDialog( title, names );
        int                 selected  = -1;
        do
        {
            selected = gui.show();
            System.out.println( "status: " + selected );
            if ( selected >= 0 )
                System.out.println( names[selected] );
        } while ( selected > 0 );
        System.exit( 0 );
    }
}
