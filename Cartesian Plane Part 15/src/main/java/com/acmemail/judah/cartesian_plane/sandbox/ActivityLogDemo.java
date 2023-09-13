package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.SwingUtilities;

public class ActivityLogDemo
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> build() );
    }
    
    private static void build()
    {
        ActivityLog activityLog = new ActivityLog();
        String  message =
            "<span style='font-style: bold; color: blue;'>"
            +     "this part bold and blue "
            +     "<span style='font-style:italic; color: red;'>"
            +         "this part red and italic "
            +     "</span>"
            +     "<span style='font-size: 150%; color: green;'>"
            +         "this part green, plain and big"
            +     "</span>"
            + "</span>";
        activityLog.append( message );
        message =
            "<span style='font-style: plain; color: blue;'>"
            +     "this part plain and blue "
            +     "<span style='font-style: plain; color: red;'>"
            +         "this part red and plain "
            +     "</span>"
            +     "<span style='font-style: bold; color: red;'>"
            +         "this part red and bold "
            +     "</span>"
            + "</span>";
        activityLog.append( message );
        activityLog.setVisible( true );
    }
}
