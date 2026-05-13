package com.acmemail.judah.cartesian_plane.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Provides methods to save and restore
 * configuration data for equations.
 * Input data is expected to conform to the description on the 
 * {@link com.acmemail.judah.cartesian_plane.input package summary page}.
 * 
 * @author Jack Straub
 */
public class FileManagerNew
{
    /**
     * Default constructor; not used.
     */
    private FileManagerNew()
    {
        // not used
    }
    
    /**
     * Saves an equation to the given file.
     * The output is clear text,
     * and the format is described on the
     * {@link com.acmemail.judah.cartesian_plane.input package summary page}.
     * 
     * @param equation  the equation to save; must not be null
     * @param file      the given file; must not be null
     * 
     * @throws IOException if an error occurs
     * 
     * @see EquationWriter#write(Equation, PrintWriter)
     * @see com.acmemail.judah.cartesian_plane.input
     * 
     * @throws NullPointerException if file or equation is null
     */
    public static void save( File file, Equation equation ) 
        throws IOException
    {
        Objects.requireNonNull( file, "file" );
        Objects.requireNonNull( equation, "equation" );
        try ( PrintWriter writer = new PrintWriter( file ) ) 
        {
            EquationWriter.write( equation, writer );
            if ( writer.checkError() )
                throw new IOException( "write failure on " + file );
        }
    }

    /**
     * Given a formatted text source,
     * read data from the source, parse it,
     * and store the data in the given equation.
     * The source format is documented on the
     * {@link com.acmemail.judah.cartesian_plane.input package summary page}.
     * If an I/O error occurs an IOException is thrown.
     * If an error is found in the source data, 
     * a failed {@linkplain Result} is returned
     * containing the relevant error message(s).
     * 
     * @param file      the formatted text source; must be non-null
     * @param equation  the given equation; must be non-null
     * 
     * @return  a Result object indicating the status of the operation
     * 
     * @throws NullPointerException if file or equation is null
     * @throws IOException  if an I/O error occurs
     */
    public static Result load( File file, Equation equation ) 
        throws IOException
    {
        Objects.requireNonNull( file, "file" );
        Objects.requireNonNull( equation, "equation" );
        try ( 
            FileReader fReader = new FileReader( file );
            BufferedReader bReader = new BufferedReader( fReader ); 
        )
        {
            Result  result  = load( bReader, equation );
            return result;
        }
    }
    
    /**
     * Given a formatted text source wrapped in a BufferedReader,
     * read data from the source, parse it,
     * and store the data in the given equation.
     * The source format is documented on the
     * {@link com.acmemail.judah.cartesian_plane.input package summary page}.
     * If an error occurs, a failed {@linkplain Result} is returned
     * containing the relevant error message(s).
     * 
     * @param reader    reader that provides access to the source; 
     *                  must be non-null
     * @param equation  the given equation; must be non-null
     * 
     * @return  a Result object indicating the status of the operation
     * 
     * @throws NullPointerException if reader or equation is null
     * @throws IOException  if an I/O error occurs
     * 
     * @see com.acmemail.judah.cartesian_plane.input
     * @see CommandProcessor
     * @see CommandReader
     */
    public static Result load( BufferedReader reader, Equation equation )
    {
        Objects.requireNonNull( reader, "file" );
        Objects.requireNonNull( equation, "equation" );
        
        List<String>        errors      = new ArrayList<>();
        CommandProcessor    proc        = new CommandProcessor( equation );
        CommandReader       commands    = new CommandReader( reader );
        commands.stream().forEach( pc -> {
            Result result = proc.processCommand( pc );
            if ( !result.isSuccess() )
                errors.addAll( result.getMessages() );
        });
        Result              result      = errors.isEmpty() ?
            new Result( true ) : new Result( false, errors );
        return result;
    }
}
