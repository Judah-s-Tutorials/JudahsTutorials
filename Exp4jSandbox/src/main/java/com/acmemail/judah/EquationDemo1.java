package com.acmemail.judah;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class EquationDemo1
{
    private enum Command
    {
        EQUATION,
        PARAMETRIC_EQUATION,
        EXPRESSION,
        VARIABLES,
        START,
        END,
        PARAM,
        EVALUATE,
        INVALID,
        NONE;
        
        private static final int    shortForm   = 3;
        private final String abbr;
        
        private Command()
        {
            String  name    = this.name();
            int     nameLen = name.length();
            int     end     = nameLen < shortForm ? nameLen : shortForm;
            this.abbr = name.substring( 0, end );
        }
        
        public static Command toCommand( String from )
        {
            String  upperFrom   = from.toUpperCase();
            Command cmd         =
                Arrays.stream( values() )
                    .filter( e -> upperFrom.startsWith( e.abbr ) )
                    .findFirst()
                    .orElse( INVALID );
            return cmd;
        }
    }
    
    private final String            prompt      = "Enter a command: ";
    private final BufferedReader    reader;
    private Equation                equation    = new Equation();
    private String                  input       = "";
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }

    public EquationDemo1( BufferedReader reader )
    {
        this.reader = reader;
    }
    
    public void execute() throws IOException
    {
        
    }
    
    private Command getCommand() throws IOException
    {
        Command cmd = Command.NONE;
        System.out.print( prompt );
        System.out.flush();
        String  line    = reader.readLine().strip();
        int     len     = line.length();
        int     split   = line.indexOf( ' ' );
        if ( len == 0 )
            ;
        else
        {
            String  cmdPart = line;
            input = "";
            if ( split > 0 )
            {
                cmdPart = line.substring( 0, split );
                input = line.substring( split ).strip();
            }
            cmd = Command.toCommand( cmdPart );
        }
        
        return cmd;
    }
}
