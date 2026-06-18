package com.acmemail.judah.cartesian_plane.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Provides methods to save and restore
 * configuration data for equations.
 * 
 * @see com.acmemail.judah.cartesian_plane.input
 * @see EquationReader
 * @see EquationWriter
 * 
 * @author Jack Straub
 */
public final class FileManager
{
    /**
     * Default constructor; not used.
     */
    private FileManager()
    {
        // not used
    }
    
    /**
     * Saves an equation to the given file.
     * The output is clear text,
     * and the format is described on the
     * {@link com.acmemail.judah.cartesian_plane.input package summary page}.
     * 
     * @param file      the given file; must not be null
     * @param equation  the equation to save; must not be null
     * 
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if file or equation is null
     *
     * @see EquationWriter#write(Equation, PrintWriter)
     * @see com.acmemail.judah.cartesian_plane.input
     */
    public static void save( File file, Equation equation )
        throws IOException
    {
        Objects.requireNonNull( file, "file" );
        Objects.requireNonNull( equation, "equation" );
        try ( PrintWriter writer = 
            new PrintWriter( file, StandardCharsets.UTF_8 )
        ) 
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
     * 
     * @see EquationReader#load(Equation, BufferedReader)
     */
    public static Result load( File file, Equation equation )
        throws IOException
    {
        Objects.requireNonNull( file, "file" );
        Objects.requireNonNull( equation, "equation" );
        try ( 
            FileReader fReader = new FileReader( file, StandardCharsets.UTF_8 );
            BufferedReader bReader = new BufferedReader( fReader ); 
        )
        {
            Result  result  = EquationReader.load( equation, bReader );
            return result;
        }
    }
}
