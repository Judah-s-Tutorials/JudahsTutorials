package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.List;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * Demonstrates how to display a dialog
 * containing an error message.
 * 
 * @author Jack Straub
 */
public class ErrorPopupDemo1
{
    private static final String lineSep = System.lineSeparator();
    private static final String indent  = "        ";
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<String>    messages    =
            List.of( "invalid this", "invalid that", "invalid other" );
        Result  result  = new Result( false, messages );
        showResultPopup( result );
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
    private static void showResultPopup( Result result )
    {
        boolean         success = result.isSuccess();
        StringBuilder   bldr    = 
            new StringBuilder( success ? "Result" : "Error: " );
        int             type    =
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        result.getMessages().forEach( m -> 
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
