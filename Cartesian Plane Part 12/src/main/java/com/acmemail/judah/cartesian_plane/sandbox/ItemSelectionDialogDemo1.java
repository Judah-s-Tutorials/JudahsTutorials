package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;

public class ItemSelectionDialogDemo1
{
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
