package com.acmemail.judah.anonymous_classes.temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acmemail.judah.cartesian_plane.PropertyManager;

/**
 * This class generates a sample initialization file.
 * The source of the initialization data
 * is the CPConstants.java file in the 
 * Cartesian plane project.
 * Use of this class 
 * is presumably the first step
 * in developing a program
 * that demonstrates how to parse
 * an initialization file.
 * 
 * @author Jack Straub
 *
 */
public class IniFileGenerator
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    
    private final File      inputPath       = 
        new File( "temp/CPConstants.java" );
    private final File      outputPath      = 
        new File( "temp/props.ini" );
    
    private final String    pnRegEx     =   
        " +[A-Z0-9_]+_PN *= *\" *([A-Za-z0-9_]+)+\"";
    private final Pattern   pnPattern   = Pattern.compile( pnRegEx );
    
    /**
     * Parse a comment multiline.
     * Following initial whitespace, a multiline comment starts
     * with "/*" or simply "*".
     * Ignoring trailing whitespace,
     * a multiline comment ends with &ldquo;&#42;/&rdquo;.
     * It is assumed that no significant text
     * follows the end of the comment.
     */
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

    /**
     * Open the input and output files
     * and initiate file processing.
     * Methods called from here may safely throw IOException.
     * Files are closed after processing is complete.
     */
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
    
    /**
     * Processes all lines in the input file.
     * Lines up to the start of the body of the class
     * (the line starting with '{')
     * are skipped.
     * 
     * @throws IOException  if an I/O error occurs
     */
    private void exec() throws IOException
    {
        inFile.lines()
            .filter( Predicate.not( this::discard ) )
            .filter( Predicate.not( this::processSimple ) )
            .filter( Predicate.not( this::processComment ) )
            .forEach( this::processPN );
    }
    
    /**
     * "Process" all lines
     * up to the start of the body of the class
     * in the input file.
     * All such lines,
     * including the start of the body of the class
     * are discarded.
     * 
     * @param line  the line from the input file to process
     * 
     * @return true if the line was processed,
     *         false otherwise.
     */
    private boolean discard( String line )
    {
        boolean result  = false;
        if ( !startFound )
        { 
            result = true;
            if ( line.startsWith( "{" ) )
                startFound = true;
        }
        return result;
    }
    
    /**
     * Identifies and processes "simple" lines 
     * from the input file.
     * A simple line is either a line
     * that is empty, or consists of only whitespace,
     * or a line that contains the double-slash ("//")
     * that indicates a comment.
     * All slashes in a simple line
     * are replaced by pound signs (#)
     * and the revised line
     * is written to the output file.
     * Returns true if the given line
     * was identified as a simple line, 
     * otherwise returns false.
     * 
     * @param line  the given line
     * 
     * @return  true if the given line
     *          was identified as a simple line
     */
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
    
    /**
     * Identify and process a line
     * that is used to declare a property name.
     * Such a line declares a variable
     * with a name that ends in "_PN"
     * and assigns it a String constant,
     * for example<pre>
     *     public static final String GRID_UNIT_PN = "gridUnit";</pre>
     * Processing the line
     * consists of writing to the output file
     * the name of the property
     * with an assignment determined by the PropertyManager,
     * for example:<pre>
     *     gridUnit=65</pre>
     * 
     * @param line  the line to examine for
     *              a property name declaration
     */
    private void processPN( String line )
    {
        Matcher matcher = pnPattern.matcher( line );
        if ( matcher.find() )
        {
            String  constantGroup   = matcher.group( 1 );
            String  defValue        = getDefaultValue( constantGroup );
            StringBuilder   bldr    = 
                new StringBuilder( "    " )
                    .append( constantGroup ).append( "=" )
                    .append( defValue );
            outFile.println( bldr );
        }
    }
    
    /**
     * Given the name of a property
     * get its default value
     * from the PropertyManager.
     * A default value of null
     * is replaced by the empty string.
     * 
     * @param name  name of the given property
     * 
     * @return  the default value of the given property
     *          as determined by the PropertyManager
     */
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
