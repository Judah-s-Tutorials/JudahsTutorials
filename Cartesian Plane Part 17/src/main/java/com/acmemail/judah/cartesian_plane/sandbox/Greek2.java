package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Greek2
{
    private static final String prefix  =
        "    private static final Desc[] allDescs = {";
    
    private static final String endl    = System.lineSeparator();
    private static final String input   = "Blog Support\\Greek.csv";
    private static final String output  = "Blog Support\\Greek.java";
    
    private static int                  counter = 0;
    private static final StringBuilder  bldr    = new StringBuilder();
    
    public static void main( String[] args )
    {
        Greek2  app = new Greek2();
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
            pWriter.write( prefix );
            pWriter.write( endl );
            pWriter.write( bldr.toString() );
            pWriter.write( "    };");
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void parse( String line )
    {
        String[]    args    = line.split( "," );
        bldr.append( "    new Desc(")
            .append( "\"").append( args[0] ).append( "\",")
            .append( "0x").append( args[1] ).append( ",")
            .append( "0x").append( args[2] ).append( ")," )
            .append( endl );
    }
}
