package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Arrays;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.input.Command;

/**
 * Demonstrates how to display a dialog
 * containing a "usage" message.
 * A "usage" message
 * is a string containing a list of all valid commands
 * and a brief description
 * of each command.
 * This demonstration uses HTML
 * to format the message.
 * 
 * @author Jack Straub
 */
public class UsagePopupDemo2
{
    /** Start of HTML string; everything up to start of table body. */
    private static final String header  =
        "<html>"
        +    "<head><title>HTML Usage Demo</title></head>"
        + "<body>"
        +    "<table>"
        +    "<tbody>";
    
    /** End of HTML string; everything from end of table body. */
    private static final String trailer =
        "</tbody>"
        +    "</table>"
        + "</body>"
        + "</html>";
        
        
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        StringBuilder   bldr    = new StringBuilder( header );
        Arrays.stream( Command.values() )
            .filter( e -> e != Command.INVALID )
            .filter( e -> e!= Command.NONE )
            .sorted( (e1,e2) -> e1.name().compareTo( e2.name() ) )
            .forEach( e -> 
                bldr.append( "<tr><td>" )
                    .append( e )
                    .append( "</td><td style='width: 250px;'>" )
                    .append( e.getDescription() )
                    .append( "</td></tr>")
        );
        bldr.append( trailer );
        
        JOptionPane.showMessageDialog( null, bldr );
        System.out.println( bldr );
    }
}
