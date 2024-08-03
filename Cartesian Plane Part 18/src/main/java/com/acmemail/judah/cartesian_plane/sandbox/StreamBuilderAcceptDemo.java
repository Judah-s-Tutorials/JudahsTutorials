package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.stream.Stream;

import javax.swing.JOptionPane;

/**
 * Simple application to build a Steam dynamically
 * with Stream.builder.
 * The operator is repeatedly prompted to enter a name
 * via JOptionPane.showInputDialog.
 * The names entered by the operator
 * are added sequentially to the stream.
 * To terminate the input
 * the operator cancels the input dialog,
 * after which the elements of the stream
 * are printed to stdout.
 * 
 * @author Jack Straub
 * 
 * @see StreamBuilderAddDemo
 */
public class StreamBuilderAcceptDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main( String[] args )
    {
        Stream.Builder<String>  bldr    = Stream.<String>builder();
        String                  name    = null;
        while ( (name = getNextName()) != null )
            bldr.accept( name );
        Stream<String>          stream  = bldr.build();
        stream.forEach( System.out::println );
    }

    /**
     * Ask the operator to enter the next name
     * in a sequence.
     * The sequence is terminated when the operator
     * cancels the input dialog.
     * 
     * @return  
     *      the next name entered by the operator,
     *      or null if the dialog is canceled
     */
    private static String getNextName()
    {
        final String    prompt  = "Enter the next name; cancel when done";
        String          name    = JOptionPane.showInputDialog( prompt );
        return name;
    }
}
