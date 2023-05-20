package com.acmemail.judah.cartesian_plane.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

/**
 * Utility class that
 * maintains a map of equation names
 * to equations.
 * To create the map
 * invoke the <em>parseEquationFiles( File dir )</em> method
 * passing the name of a directory
 * that contains text files
 * with equation configuration data;
 * subdirectories will be searched recursively.
 * The directory may contain other files
 * which will be ignored.
 * You can also add a single equation 
 * to the map by invoking 
 * <em>parseEquationFile( File file )</em>
 * and passing the name of a text file
 * that contains equation configuration data.
 * 
 * @author Jack Straub
 */
public class EquationMap
{
    /** File-chooser to allow operator to select files. */
    private static final JFileChooser   chooser;
    
    /** Map of names to Equation objects. */
    private static final Map<String,Equation>   equationMap = new HashMap<>();
    
    static
    {
        String  userDir = System.getProperty( "user.dir" );
        File    baseDir = new File( userDir );
        chooser = new JFileChooser( baseDir );
    }
    
    /**
     * Default constructor.
     * Private to prevent instantiation
     * of this utility class.
     */
    private EquationMap()
    {
        // intentionally empty
    }
    
    /**
     * Prompts the operator to select a file
     * to be parsed as an equation file.
     * The operator may choose a directory
     * in which case it will be recursively
     * searched for equation files.
     * 
     * @see #parseEquationFiles(File)
     * @see #parseEquationFile(File)
     */
    public void parseEquationFiles()
    {
        int choice  = chooser.showOpenDialog( null );
        if ( choice == JFileChooser.APPROVE_OPTION )
        {
            File    file    = chooser.getSelectedFile();
            if ( file.isDirectory() )
                parseEquationFiles( file );
            else
                parseEquationFile( file );
        }
    }
    
    /**
     * Examines a given directory file
     * and determines which, if any,
     * of its child files contains
     * a valid equation specification.
     * If a child of the given file
     * is itself a directory
     * it is recursively examined.
     * If the given file
     * is not a directory file
     * it is silently ignored.
     * If a file cannot be examined
     * an error message is printed,
     * otherwise it is silently ignored.
     * 
     * @param dir   the given file
     * 
     * @see #parseEquationFiles()
     */
    public static void parseEquationFiles( File dir )
    {
        File[]  fileList    = dir.listFiles();
        if ( fileList != null )
        {
            for ( File file : fileList )
            {
                if ( file.isDirectory() )
                    parseEquationFiles( file );
                else if ( isEquationFile( file ) )
                    parseEquationFile( file );
                else
                    ;
            }
        }
    }
    
    /**
     * Extracts from a file
     * a named equation,
     * and stores the equation
     * in the equation map.
     * If the file cannot be read,
     * or if it does not contain a named equation,
     * it is silently ignored.
     * 
     * @param file  the given file
     */
    public static void parseEquationFile( File file )
    {
        Equation    equation    = FileManager.open( file );
        if ( equation != null )
            equationMap.put( equation.getName(), equation );
    }
    
    /**
     * Indicates whether a given file
     * contains an equation specification.
     * The file passes the test if:
     * <ul>
     * <li>It exists;</li>
     * <li>It is a regular file;</li>
     * <li>It is readable;</li>
     * <li>It can be accessed as a text file; and</li>
     * <li>
     *      Its first non-blank, non-comment line
     *      contains an EQUATION command
     *      with a non-empty name.
     * </li>
     * </ul>
     * @param file  the given file
     * 
     * @return  true, if the given file is an equation file
     */
    public static boolean isEquationFile( File file )
    {
        boolean isEquationFile  = false;
        if ( file.exists() && file.canRead() && !file.isDirectory() )
        {
            try (
                FileReader fileReader = new FileReader( file );
                BufferedReader bufReader = new BufferedReader( fileReader );
            )
            {
                CommandReader   reader  = new CommandReader( bufReader );
                ParsedCommand   command = reader.nextCommand( null );
                if ( command.getCommand() == Command.EQUATION
                     && !command.getArgString().isEmpty()
                   )
                    isEquationFile = true;
            }
            catch ( IOException exc )
            {
                String  fmt = "Error reading file \"%s\": %s%n";
                System.err.printf( fmt, file.getName(), exc.getMessage() );
            }
        }
        
        return isEquationFile;
    }
    
    /**
     * Given the name of an equation
     * returns the associated equation.
     * If there is no such equation
     * null is returned.
     * 
     * @param name  the name of the target equation
     * 
     * @return  the equation associated with the given name
     * 
     * @throws  NullPointerException 
     *          if the given name is null
     */
    public static Equation getEquation( String name )
    {
        Equation    equation    = equationMap.get( name );
        return equation;
    }
    
    public static Equation getEquation()
    {
        //TODO implement
        return null;
    }
    
    /**
     * Returns an <em>unmodifiable</em> map
     * of equation names to equations.
     * 
     * @return  an unmodifiable map
     *          of equation names to equations
     */
    public static Map<String,Equation> getEquationMap()
    {
        Map<String,Equation>    map = 
            Collections.unmodifiableMap( equationMap );
        return map;
    }
}
