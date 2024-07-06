package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.util.Scanner;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Simple application to create a Profile
 * and display the values
 * of its properties.
 * to terminate this program,
 * type Enter in the application console.
 * 
 * @author Jack Straub
 */
public class ProfileDemo1
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
        Profile profile = new Profile();
        profile.getProperties()
            .forEach( log::append );
        System.out.println( "Waiting..." );
        Scanner scanner = new Scanner( System.in );
        scanner.nextLine();
        scanner.close();
        System.exit( 0 );
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
