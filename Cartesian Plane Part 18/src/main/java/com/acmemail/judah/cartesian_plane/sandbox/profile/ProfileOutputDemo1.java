package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.acmemail.judah.cartesian_plane.ProfileParser;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Simple application to create a Profile
 * and display the values
 * of its properties.
 * to terminate this program,
 * close the activity log.
 * 
 * @author Jack Straub
 */
public class ProfileOutputDemo1
{
    /** Log for displaying Profile properties. */
    private static ActivityLog      log;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        GUIUtils.schedEDTAndWait( () -> build() );
        ProfileParser parser = new ProfileParser();
        parser.getProperties()
            .forEach( log::append );
        
        WindowListener  winListener = new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent evt )
            {
                System.exit( 0 );
            }
        };
        log.addWindowListener( winListener );
    }
    
    /**
     * Creates and shows the dialog
     * used to display the Profile properties.
     */
    private static void build()
    {
        log = new ActivityLog();
        log.setLocation( 200, 200 );
        log.setVisible( true );
    }
}
