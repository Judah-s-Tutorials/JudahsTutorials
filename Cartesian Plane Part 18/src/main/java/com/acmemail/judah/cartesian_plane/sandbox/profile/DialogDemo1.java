package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorDialog;

public class DialogDemo1
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ProfileEditorDialog dialog  = 
            new ProfileEditorDialog( null, new Profile() );
        dialog.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent evt )
            {
                System.exit( 0 );
            }
        });
        dialog.showDialog();
    }
    
}
