package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

import com.acmemail.judah.cartesian_plane.input.Command;

/**
 * This is a small program that demonstrates
 * some of the components of CommandReaderTest
 * to simulate input from a file or other source,
 * especially:
 * <ul>
 * <li>
 *     {@linkplain IOConsumer},
 *     a functional interface based on java.util.function.Consumer
 *     with an <em>accept(BufferedReader)</em>
 *     which declares that it throws IOException.
 * </li>
 * <li>
 *     {@linkplain #getByteBuffer(List)},
 *     which converts a list of strings
 *     to a buffer that can be used
 *     to create a BufferedReader.
 * </li>
 * <li>
 *     {@linkplain #ioTest(byte[], IOConsumer)},
 *     which converts a byte buffer to a BufferedReader
 *     and passes to a consumer for input processing.
 * </li>
 * <li>
 *     {@linkplain #ioTest(List, IOConsumer)},
 *     which invokes {@linkplain #getByteBuffer(List)}
 *     to obtain a byte buffer
 *     and passes it to {@linkplain #ioTest(byte[], IOConsumer)}
 *     for formatting and input processing.
 * </li>
 * </ul>
 */
public class IOTestDemo
{
    /**
     * Default constructor, not used.
     */
    private IOTestDemo()
    {
        // not used
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used.
     */
    public static void main(String[] args)
    {
        executeCommandsMain();
        filterInvalidCommandsMain();
    }
    
    /**
     * Initiates an execution of a method 
     * that simulates processing a list of commands.
     * 
     * @see #executeCommands(BufferedReader)
     * @see #ioTest(List, IOConsumer)
     */
    private static void executeCommandsMain()
    {
        List<String>    allCommands =
            Arrays.stream( Command.values() )
                .map( Command::toString )
                .toList();
        ioTest( allCommands, IOTestDemo::executeCommands );
    }
    
    /**
     * Simulates reading and executing a list of commands
     * from a BufferedReader.
     * 
     * @param reader    the BufferedReader to read from
     */
    private static void executeCommands( BufferedReader reader )
    {
        reader.lines()
            .forEach( c -> System.out.println( "executing " + c ) );
    }
    
    /**
     * Initiates an execution of a method 
     * that simulates filtering a list 
     * for valid commands
     * and then executing them.
     * 
     * @see #filterInvalidCommands(BufferedReader)
     * @see #ioTest(List, IOConsumer)
     */
    private static void filterInvalidCommandsMain()
    {
        List<String>    commands    =
            List.of( "a", "yplot", "b", "exit", "c", "xequals" );
        ioTest( commands, IOTestDemo::filterInvalidCommands );
    }
    
    /**
     * Simulates reading, filtering, and executing a list of commands
     * from a BufferedReader.
     * 
     * @param reader    the BufferedReader to read from
     */
    private static void filterInvalidCommands( BufferedReader reader )
    {
        reader.lines()
            .map( Command::toCommand)
            .filter( s -> s != Command.INVALID )
            .forEach( c -> System.out.println( "executing " + c ) );
    }
    
    /**
     * Writes each string in a list into a byte buffer.
     * Each string is terminated with a line separator.
     * The effect is as if individual strings in a list
     * were written to the console with println,
     * except the output is redirected to a buffer.
     * The buffer, 
     * suitable for use in instantiating 
     * a ByteArrayInputStream,
     * is returned to the caller.
     *  
     * @param list  the list to write
     * 
     * @return  the byte array containing the output from the operation
     * 
     * @see #ioTest(byte[], IOConsumer)
     * @see #ioTest(List, IOConsumer)
     */
    private static byte[] getByteBuffer( List<String> list )
    {
        byte[]  bytes   = null;
        try (
            ByteArrayOutputStream   baoStream   = new ByteArrayOutputStream();
            PrintWriter             writer      = new PrintWriter( baoStream );
        )
        {
            list.forEach( writer::println );
            writer.flush();
            bytes = baoStream.toByteArray();
        }
        catch ( IOException exc )
        {
            throw new UncheckedIOException( exc );
        }
        return bytes; 
    }
    
    /**
     * Generates sample input from a list
     * in the form of a BufferedReader.
     * Uses the BufferedReader
     * to execute a given consumer.
     * 
     * @param list      the list to convert to input
     * @param tester    the given consumer
     * 
     * @see #getByteBuffer(List)
     * @see #ioTest(byte[], IOConsumer)
     */
    private static void ioTest( List<String> list, IOConsumer tester )
    {
        byte[]  bytes   = getByteBuffer( list );
        ioTest( bytes, tester );
    }
    
    /**
     * Transforms a byte buffer into an input stream
     * in the form of a BufferedReader.
     * The byte buffer is assumed to contain
     * only valid Unicode characters,
     * divided into lines using
     * the appropriate line separator.
     * Uses the BufferedReader
     * to execute a given consumer.
     * 
     * @param buff      the source byte buffer
     * @param tester    the given consumer
     * 
     * @see #getByteBuffer(List)
     * @see #ioTest(List, IOConsumer)
     */
    private static void ioTest( byte[] buff, IOConsumer tester )
    {
        try (
            ByteArrayInputStream baiStream = new ByteArrayInputStream( buff );
            InputStreamReader strReader = new InputStreamReader( baiStream );
            BufferedReader bufReader = new BufferedReader( strReader );
        )
        {
            tester.accept( bufReader );
        }
        catch ( IOException exc )
        {
            throw new UncheckedIOException( exc );
        }
    }

    /**
     * Defines a functional interface
     * that accepts an argument of type BufferedReader
     * and returns nothing.
     * This is essentially a Consumer&lt;BufferedReader&gt;
     * except that the abstract method, accept
     * may throw an IOException.
     * 
     * @author Jack Straub
     */
    @FunctionalInterface
    private interface IOConsumer
    {
        /**
         * Executes a test
         * using a BufferedReader for input.
         * 
         * @param reader    the BufferedReader representing the input
         * 
         * @throws IOException  if an I/O error occurs
         */
        public abstract void accept( BufferedReader reader ) 
            throws IOException;
    }
}
