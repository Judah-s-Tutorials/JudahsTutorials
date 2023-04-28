package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;

public class Investigator
{
    private final BufferedReader    reader;
    private final JEP               parser;
    private       String            input;
    
    public static void main(String[] args)
    {
        try ( 
            InputStreamReader inReader    = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            Investigator    app = new Investigator( bufReader );
            app.exec();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private Investigator( BufferedReader reader )
    {
        this.reader = reader;
        parser = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setImplicitMul( true );
    }
    
    private void exec() throws IOException
    {
        String  prompt  = "Enter a command: ";
        System.out.print( prompt );
        input = reader.readLine().trim();
        while ( !input.equalsIgnoreCase( "exit" ) )
        {
            if ( input.startsWith( "set" ) )
                set();
            else if ( input.startsWith( "exit" ) )
                ;
            else
                parse();
            System.out.print( prompt );
            input = reader.readLine().trim();
        } 
    }
    
    private void parse()
    {
        try
        {
            parser.parse( input );
            if ( parser.hasError() )
                System.out.println( parser.getErrorInfo() );
            else
                System.out.println( parser.getComplexValue() );
        }
        catch ( ParseException exc )
        {
            System.out.println( exc.getErrorInfo() );
        }

    }
    
    private void set()
    {
        String          name    = "";
        String          strVal  = "";
        StringTokenizer tizer   = new StringTokenizer( input );
        // throw away "set"
        tizer.nextToken();
        // validate 
        if ( !tizer.hasMoreElements() )
            System.out.println( "Invalid input: " + input );
        else
        { 
            try
            {
                name = tizer.nextToken();
                strVal = tizer.hasMoreTokens() ? tizer.nextToken( "" ) : "0";
                parser.parse( strVal.trim() );
                if ( parser.hasError() )
                    System.out.println( parser.getErrorInfo() );
                else
                {
                    Complex cpx = parser.getComplexValue();
                    parser.addVariable( name,  parser.getValue() );
                    if ( parser.hasError() )
                        System.out.println( parser.getErrorInfo() );
                    else
                        System.out.println( name + ": " + parser.getValue() );
                }
            }
            catch ( ParseException exc )
            {
                System.out.println( exc.getErrorInfo() );
            }
        }
    }
}
