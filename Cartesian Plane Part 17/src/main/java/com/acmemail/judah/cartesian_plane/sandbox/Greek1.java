package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Greek1
{
    private static final String endl    = System.lineSeparator();
    private static final String input   = "Blog Support\\Greek.txt";
    private static final String output  = "Blog Support\\Greek.csv";
    
    private static int                  counter = 0;
    private static final StringBuilder  bldr    = new StringBuilder();
    
    public static void main( String[] args )
    {
        Greek1  app = new Greek1();
        app.work();
        System.out.println( bldr );
    }
    
    private void work()
    {
        try ( 
            FileReader fReader = new FileReader( input );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            bReader.lines()
                .peek( this::parse )
                .forEach( l -> counter++ );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        try ( 
            FileWriter fWriter = new FileWriter( output );
            PrintWriter pWriter = new PrintWriter( fWriter );
        )
        {
            pWriter.write( bldr.toString() );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void parse( String line )
    {
        if ( counter != 0 && counter % 3 == 0 )
            bldr.append( endl );
        else if ( counter != 0 )
            bldr.append( "," );
        else
            ;
        bldr.append( line.trim() );
    }
}
