package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;

/**
 * This is a simple program 
 * to demonstrate what an ItemSelectionDialog
 * with an empty list
 * should look. 
 * Specifically, 
 * the OK button 
 * should be disabled.
 * @author Jack Straub
 */
public class EmptySelectionDialogDemo
{
    public static void main(String[] args)
    {
        String[]    emptyList       = new String[0];
        String[]    nonEmptyList    = { "Item 1", "Item 2", "Item 3" };
        String      title           = "Choose 1";
        
        ItemSelectionDialog nonEmpty    = 
            new ItemSelectionDialog( title, nonEmptyList );
        ItemSelectionDialog empty    = 
            new ItemSelectionDialog( title, emptyList );

        nonEmpty.show();
        empty.show();
        System.exit( 0 );
    }

}
