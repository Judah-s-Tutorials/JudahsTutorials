package com.acmemail.judah.anonymous_classes.streams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This is an application 
 * to read a file of name/value pairs 
 * and produce a Properties object.
 * In addition to name/value pairs,
 * the file contains blank space,
 * blank lines and comments,
 * which must be ignored.
 * Name/value pairs are written in the form:
 * <em>name=value</em>.
 * 
 * @author Jack Straub
 */
public class PropertiesFileParser
{
    private final File      inputPath;

    /**
     * Constructor.
     * Prepares to parse a properties file
     * using a given file path.
     * 
     * @param filePath  the given file path.
     * 
     * @see #getProperties()
     */
    public PropertiesFileParser( String filePath )
    {
        inputPath = new File( filePath );
    }

    /**
     * Open the input and output files
     * and initiate file processing.
     * Methods called from here may safely throw IOException.
     * Files are closed after processing is complete.
     * The generated Properties object is returned.
     * 
     * @return the generated Properties object
     */
    public Properties getProperties()
    {
        Properties  properties  = null;
        try ( 
            FileReader  fReader = new FileReader( inputPath );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            properties = getProperties( bReader );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return properties;
    }
    
    /**
     * Parse the properties file
     * referenced by a given BufferedReader,
     * producing a Properties object
     * as a result.
     * Discards all blank lines and comments,
     * and ignores any other line
     * that is not expressed as <em>name=value</em>.
     * Whitespace between <em>name</em> and <em>value</em>
     * is ignored
     * (i.e., the name/value pair
     * may be expressed as <em>name = value</em>).
     * 
     * @param bReader   the given BufferedReader 
     * 
     * @return  the generated Properties object
     */
    private Properties getProperties( BufferedReader bReader )
    {
        Properties  props   = new Properties();
        props.putAll( 
            bReader.lines()
                .map( String::trim )
                .filter( Predicate.not( String::isBlank ) )
                .filter( Predicate.not( s -> s.startsWith( "#" ) ) )
                .map( s -> s.split( "=" ) )
                .filter( a -> a.length == 2 )
                .map( a -> {a[0]=a[0].trim(); a[1]=a[1].trim(); return a;} )
                .collect( Collectors.toMap( a -> a[0], a -> a[1] ) )
        );
        return props;
    }
}
