package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;

/**
 * Simple program to demonstrate
 * how to use the ItemSelectionDialog.
 * 
 * @author Jack Straub
 *
 */
public class ItemSelectionDialogDemo1
{
    /**
	 * Application entry point.
	 * 
     * @param args command line arguments; not used
     */
    public static void main(String[] args)
    {
        String[]            names   =
            { "Sally", "Manny", "Jane", "Moe", "Anapurna", 
              "Jack", "Alice", "Tweedledee", "Elizabeth", "Tweedledum",
            };
        ItemSelectionDialog dialog  = 
            new ItemSelectionDialog( "Choose a Name", names );
        int                 choice  = dialog.show();
        System.out.println( "Option: " + choice );
        if ( choice >= 0 )
            System.out.println( names[choice] );
        System.exit( 0 );
    }
}
