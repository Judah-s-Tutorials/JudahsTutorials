package com.acmemail.judah.anonymous_classes.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acmemail.judah.cartesian_plane.PropertyManager;

public class IniFileGenerator
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    
    private final File      inputPath       = 
        new File( "temp/CPConstants.java" );
    private final File      outputPath      = 
        new File( "temp/props.ini" );
    
    private final String    pnRegEx     =   
        " +([A-Z0-9_]+_PN) *= *\" *([A-Za-z0-9_]+)+\"";
    private final Pattern   pnPattern   = Pattern.compile( pnRegEx );
    
    /**
     * Parse a comment line. May look like one of the following:
     * <ul>
     *     <li>"/* comment"</li>
     *     <li>"/** comment"</li>
     *     <li>"* comment"</li>
     * </ul>
     */
     // (0 or more spaces), 
     // followed by 0 or 1 / 
     // followed by 1 or more *
     // (followed by anything that's ^*)
    private final String    commentRegExp   = 
        "\\s*" + "/?" + "\\*+" + "([^*]*)";
    private final Pattern   commentPattern  = Pattern.compile( commentRegExp );
    
    private BufferedReader  inFile;
    private PrintWriter     outFile;
    
    private boolean         startFound  = false;
       
    public static void main(String[] args)
    {
        new IniFileGenerator().start();
    }

    private void start()
    {
        try ( 
            FileReader  fReader = new FileReader( inputPath );
            BufferedReader bReader = new BufferedReader( fReader );
            FileWriter fWriter = new FileWriter( outputPath );
            PrintWriter pWriter = new PrintWriter( fWriter, true );
        )
        {
            inFile = bReader;
            outFile = pWriter;
            exec();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void exec() throws IOException
    {
        // skip header
        inFile.lines().forEach( this::nextLine );
    }
    
    private void nextLine( String line )
    {
        if ( !startFound )
        { 
            if ( line.startsWith( "{" ) )
                startFound = true;
        }
        else if ( processSimple( line ) )
            ;
        else if ( processComment( line ) )
            ;
        else
            processPN( line );
    }
    
    private boolean processSimple( String line )
    {
        boolean processed   = false;
        String  temp        = line.trim();
        if ( temp.isBlank() || temp.startsWith( "//" ) )
        {
            temp = line.replaceAll( "/", "#" );
            outFile.println( temp );
            processed = true;
        }
        return processed;
    }
    
    private boolean processComment( String line )
    {
        boolean processed   = false;
        Matcher matcher = commentPattern.matcher( line );
        if ( matcher.find() )
        {
            String  revised = "    # ";
            if ( matcher.groupCount() > 0 )
                revised += matcher.group( 1 );
            if ( !revised.contains( "default value:" ) )
                outFile.println( revised );
            processed = true;
        }
        
        return processed;
    }
    
    private void processPN( String line )
    {
        Matcher matcher = pnPattern.matcher( line );
        if ( matcher.find() )
        {
            String  pnGroup         = matcher.group( 1 );
            String  constantGroup   = matcher.group( 2 );
            String  defValue        = getDefaultValue( constantGroup );
            StringBuilder   bldr    = 
                new StringBuilder( "    " )
                    .append( constantGroup ).append( "=" )
                    .append( defValue );
            outFile.println( bldr );
        }
    }
    
    private String getDefaultValue( String name )
    {
        String  val = pMgr.asString( name );
        if ( val == null )
        {
            System.err.println( "hmmm... " + name );
            val = "";
        }
        return val;
    }
}
