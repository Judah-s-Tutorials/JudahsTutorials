package com.acmemail.judah.cartesian_plane.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateCPConstants
{
    private static final String     pNameRegEx      =
        "(\\s*public static final String\\s+)(\\S+)(\\s+=)";
    private static final Pattern    pNamePattern    =
        Pattern.compile( pNameRegEx );
    
    private final String        inPath   =
        "src/main/java/com/acmemail/judah/cartesian_plane/"
        + "CPConstantsSource.java";
    private final String        outPath  = "temp/CPConstants.java";
    private final List<String>  comment = new ArrayList<>();
    
    private BufferedReader  reader;
    private PrintWriter     writer;
    
    private String          previousLine    = null;
    private int             lineNo          = 0;
    
    public static void main(String[] args)
    {
        new GenerateCPConstants().initiate();
    }
    
    private void initiate()
    {
        try ( 
            FileReader fReader = new FileReader( inPath );
            FileWriter fWriter = new FileWriter( outPath );
        )
        {
            reader = new BufferedReader( fReader );
            writer = new PrintWriter( fWriter );
            execute();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void execute() throws IOException
    {
        writeHead();
        
        String  line    = null;
        while ( (line = nextLine()) != null )
        {
            if ( line.contains( "/**" ) )
                compileComment( line );
            else if ( line.contains( "_DV" ) )
                deriveProperty( line );
            else
                writer.println( line );
        }
    }
    
    private void writeHead() throws IOException 
    {
        String  line    = "";
        while ( !line.startsWith( "public class" ) )
        {
            line = nextLine();
            if  ( line == null )
            {
                String  msg = "Unexpected EOF at line " + lineNo;
                throw new IOException( msg );
            }
            writer.println( line );
        }
    }
    
    private void deriveProperty( String dvLine ) throws IOException
    {
        if ( comment.isEmpty() )
        {
            String  msg = "missing comment at line " + lineNo;
            throw new IOException( msg );
        }
        
        // Generate a comment for the property name
        int dvCounter   = 0;
        for ( String line : comment )
        {
            int dvIndex = line.indexOf( "default value" );
            if ( dvIndex > 0 )
            {
                StringBuilder   bldr    = new StringBuilder();
                bldr.append( line.subSequence( 0, dvIndex ) )
                    .append( "property name " );
                if ( line.contains( "*/" ) )
                    bldr.append( "*/" );
                writer.println( bldr.toString() );
                dvCounter++;
            }
            else
                writer.println( line );
        }
        if ( dvCounter != 1 )
        {
            String  msg =
                "invalid comment found at line " + lineNo
                + " (dvCount = " + dvCounter + ")";
            throw new IOException( msg );
        }
        
        // Derive property name variable declaration
        String  pnLine   = derivePropertyNameConstantVariable( dvLine );
        writer.println( pnLine );
        
        // print out the original comment
        for ( String line : comment )
            writer.println( line );
        // print out the default value declaration
        writer.println( dvLine );
    }
    
    private String derivePropertyNameConstantVariable( String line ) 
        throws IOException
    {
        Matcher matcher     = pNamePattern.matcher( line );
        int     groupCount  = matcher.groupCount();
        if ( !matcher.find() )
        {
            String  msg = "expected match not found at line " + lineNo;
            throw new IOException( msg );
        }
        
        if ( groupCount != 3 )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( "group count: " ).append( groupCount );
            for ( int inx = 1 ; inx <= groupCount ; ++inx )
                bldr.append( " {")
                    .append( matcher.group( inx ) )
                    .append( "}" );
            System.err.println( bldr.toString() );
            
            String  msg = "Invalid group count (" + groupCount + ")";
            throw new IOException( msg );
        }
        
        String          dvPart  = matcher.group( 2 );
        int             dvIndex = dvPart.indexOf( "_DV" );
        String          pnPart  = dvPart.substring( 0, dvIndex ) + "_PN";
        String          pName   = derivePropertyName( dvPart );
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( matcher.group( 1 ) )
            .append( pnPart )
            .append( matcher.group( 3 ) )
            .append( " \"" )
            .append( pName )
            .append( "\";" );
        
        return bldr.toString();
    }
    
    private String derivePropertyName( String from )
    {
        int             strLen  = from.length();
        StringBuilder   bldr    = new StringBuilder();
        
        // strip the _DV
        strLen -= 3;
        for ( int inx = 0 ; inx < strLen ; ++inx )
        {
            char    next    = from.charAt( inx  );
            // If the next character is NOT an underscore
            // convert it to lower case and add the converted
            // character to the name. Otherwise get the next
            // character from the string (it should be
            // uppercase) and it to the name.
            if ( next != '_' )
                bldr.append( Character.toLowerCase( next ) );
            else
                bldr.append( from.charAt( ++inx ) );
        }
        return bldr.toString();
    }
    
    private void compileComment( String start ) throws IOException
    {
        comment.clear();
        comment.add( start );
        String  line    = start;
        while ( !line.contains( "*/" ) )
        {
            line = nextLine();
            if ( line == null )
            {
                String  msg = "unexpected EOF at line " + lineNo;
                throw new IOException( msg );
            }
            comment.add( line );
        }
    }
    
    private String nextLine() throws IOException
    {
        String  next    = reader.readLine();
        if ( next != null )
            ++lineNo;
        return next;
    }
}
