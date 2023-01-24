package com.acmemail.judah.cartesian_plane.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateSwitch
{
    private static final String endl    = System.lineSeparator();
    
    // private (type) (var-name) =
    private static final String     vNameRegEx      =
        "private\\s+([A-Za-z]+)\\s+([a-z]\\w+)\\s*=";
    private static final Pattern    vNamePattern    =
        Pattern.compile( vNameRegEx );
    // pmgr.asInt( CPConstants.MW_HEIGHT_PN )
    private static final String     valRegEX        =
        "pmgr\\.as.+(CPConstants.\\w+)";
    private static final Pattern    valPattern    =
        Pattern.compile( valRegEX );
    
    private final String        inPath  =
        "src/main/java/com/acmemail/judah/cartesian_plane/"
        + "CartesianPlane.java";
    private final String        outPath = "temp/Switch.java";
    private int                 lineNo  = 0;
    
    private BufferedReader  reader;
    private PrintWriter     writer;

    public static void main(String[] args)
    {
        new GenerateSwitch().initiate();
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
        skipHeader();
        writer.println( "    switch ( pName )" );
        writer.println( "    {" );
        
        String      line    = null;
        String[]    vName   = null;
        while ( (line = nextLine()) != null )
        {
            if ( (vName = getVName( line )) != null )
            {
                if ( !line.endsWith( ";" ) )
                    line = nextLine();
                String  pnConstant  = getPNConstant( line );
                formulateCase( pnConstant, vName[0], vName[1] );
            }
        }
        writer.println( "    }" );
    }
    
    private void formulateCase( String pName, String type, String vName )
    {
        String          asType  = null;
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "    case " )
            .append( pName )
            .append( ":" )
            .append( endl )
            .append( "        " )
            .append( vName )
            .append( " = ");
        switch ( type )
        {
        case "int":
            asType = "asInt";
            break;
        case "float":
            asType = "asFloat";
            break;
        case "Color":
            asType = "asColor";
            break;
        case "String":
            asType = "asString";
            break;
        case "boolean":
            asType = "asBoolean";
            break;
        default:
            throw new RuntimeException( "unknown type: " + type );
        }
        bldr.append( asType )
            .append( "( newVal );" )
            .append( endl )
            .append( "        break;" );
        writer.println( bldr );
    }
    
    private String[] getVName( String line ) throws IOException
    {
        String[]    rval    = null;
        if ( line == null )
        {
            String  msg = "Unexpected EOF at line " + lineNo;
            throw new IOException( msg );
        }
        
        Matcher matcher     = vNamePattern.matcher( line );
        if ( matcher.find() )
        {
            String  type    = matcher.group( 1 );
            String  name    = matcher.group( 2 );
            rval = new String[] { type, name };
        }
        return rval;
    }
    
    private String getPNConstant( String line ) throws IOException
    {
        String  rval    = null;
        if ( line == null )
        {
            String  msg = "Unexpected EOF at line " + lineNo;
            throw new IOException( msg );
        }
        
        Matcher matcher     = valPattern.matcher( line );
        if ( !matcher.find() )
        {
            String  msg = "Expected value not found at line " + lineNo;
            System.out.println( msg );
        }
        else
        {
            rval = matcher.group( 1 );
        }
        return rval;
    }
    
    private void skipHeader() throws IOException
    {
        String  line    = null;
        while ( !(line = nextLine()).contains( "public class" ) )
            ;
    }
    
    private String nextLine() throws IOException
    {
        String  next    = reader.readLine();
        if ( next != null )
        {
            ++lineNo;
            next = next.trim();
        }
        return next;
    }

}
