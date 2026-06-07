package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Arrays;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This class contains utilities that are helpful
 * to the demo applications in this project.
 */
public final class Utils
{
    /** Line separator for this platform. */
    private static final String lineSep = System.lineSeparator();
    
    /**
     * Default constructor; not used.
     */
    private Utils()
    {
        // not used
    }

   /**
     * Display a dialog containing
     * all commands and their brief descriptions.
     * 
     * @param args  command line arguments; not used
     */
    public static void showUsageDialog()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "<html><table><tbody>" );
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
        bldr.append( "</tbody></table></html>" );
        
        JOptionPane.showMessageDialog( null, bldr );
        System.out.println( bldr );
    }

    
    /**
     * Display the result of executing a command.
     * If the result indicates success,
     * an information dialog will be displayed,
     * otherwise an error dialog will be displayed.
     * Feedback messages associated with the result
     * will be displayed one per line.
     * 
     * @param result    the result to display
     */
    public static void showResultPopup( Result result )
    {
        final String indent  = "        ";
        
        boolean         success = result.success();
        StringBuilder   bldr    = 
            new StringBuilder( success ? "Result" : "Error: " );
        int             type    =
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        result.messages().forEach( m -> 
            bldr.append( lineSep )
                .append( indent )
                .append( m )
        );
        JOptionPane.showMessageDialog(
            null, 
            bldr,
            "Command Result", 
            type
        );
    }
}
