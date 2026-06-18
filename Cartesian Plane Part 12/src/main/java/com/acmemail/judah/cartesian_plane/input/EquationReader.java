package com.acmemail.judah.cartesian_plane.input;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for reading a stream containing
 * Commands formatted as text,
 * and using the Commands to configure an Equation.
 * See the {@linkplain com.acmemail.judah.cartesian_plane.input the package documentation}
 * for details about the input stream.
 * 
 * @author Jack Straub
 */
public final class EquationReader
{
    /**
     * Default constructor; not used.
     */
    private EquationReader()
    {
        // not used
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
     * @param equation  the given equation; must be non-null
     * @param reader    reader that provides access to the source;
     *                  must be non-null
     * 
     * @return  a Result object indicating the status of the operation
     * 
     * @throws NullPointerException if reader or equation is null
     * 
     * @see com.acmemail.judah.cartesian_plane.input
     * @see CommandProcessor
     * @see CommandReader
     */
    public static Result load( Equation equation, BufferedReader reader )
    {
        Objects.requireNonNull( reader, "reader" );
        Objects.requireNonNull( equation, "equation" );
        
        List<String>        errors      = new ArrayList<>();
        CommandProcessor    proc        = new CommandProcessor( equation );
        CommandReader       commands    = new CommandReader( reader );
        commands.stream().forEach( pc -> {
            Result result = proc.processCommand( pc );
            if ( !result.success() )
                errors.addAll( result.messages() );
        });
        Result              result      = errors.isEmpty() ?
            new Result( true ) : new Result( false, errors );
        return result;
    }
}
