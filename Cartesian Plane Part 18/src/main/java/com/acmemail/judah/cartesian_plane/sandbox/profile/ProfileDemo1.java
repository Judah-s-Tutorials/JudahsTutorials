package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.util.Scanner;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class ProfileDemo1
{
    private static ActivityLog      log;
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
    
    private static void build()
    {
        log = new ActivityLog();
        log.setLocation( 200, 200 );
        log.setVisible( true );
    }
}
