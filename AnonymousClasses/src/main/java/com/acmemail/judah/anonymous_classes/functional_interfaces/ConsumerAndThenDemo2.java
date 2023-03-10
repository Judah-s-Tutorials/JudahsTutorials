package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is a simple demonstration of how 
 * the Consumer <em>andThen</em> default method
 * can be used to chain several Consumer.accept() invocations
 * as a single lambda.
 * 
 * @author Jack Straub
 */
public class ConsumerAndThenDemo2
{
    private static List<String> online      = new ArrayList<>();
    private static int          logLevel    = 3;
    static
    {
        online.add( "New York" );
        online.add( "Omaha" );
        online.add( "Philadelphia" );
        online.add( "San Francisco" );
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        Consumer<String>    allConsumers    = 
            getSendToRecipients();
        sendTo( "Greetings from Anchorage", allConsumers );
    }
    
    /**
     * Send a given message to the designated consumer.
     * 
     * @param message   the given message
     * @param consumer  the designated consumer
     */
    private static void sendTo( String message, Consumer<String> consumer )
    {
        consumer.accept( message );
    }
    
    /**
     * Stub to simulate sending a given message 
     * to a designated recipient.
     * The message and recipient are simply
     * written to stdout.
     * 
     * @param message   the given message
     * @param recipient the designatedsendTo(message, "Home Office") recipient
     */
    private static void sendTo( String message, String recipient )
    {
        System.out.println( "sending to: " + recipient );
        System.out.println( message );
        System.out.println( "************* end message **************" );
    }
    
    /**
     * Stub to simulate logging a given message 
     * to a designated recipient.
     * The message and recipient are simply
     * written to stdout.
     * 
     * @param message   the given message
     * @param recipient the designatedsendTo(message, "Home Office") recipient
     */
    private static void log( String message, String recipient )
    {
        StringBuilder   bldr    = new StringBuilder( " **** Logging: " )
            .append( "recipient=" ).append( recipient ).append( "; " )
            .append( "message=" ).append( message );
        System.out.println( bldr );
    }
    
    /**
     * Gets a Consumer&lt;String&gt; functional interface
     * encapsulating all current recipients.
     * Each destination in the online list is included.
     * If indicated,
     * each message is sent to 
     * the system log.
     * 
     * @return  a Consumer&gt;String&lt; functional interface
     *          encapsulating all current recipients
     */
    private static Consumer<String> getSendToRecipients()
    {
        Consumer<String>    consumer    = m -> sendTo( m, "history" );
        for ( String next : online )
        {
            consumer = consumer.andThen( m -> sendTo( m, next ) );
            if ( logLevel > 2 )
                consumer = consumer.andThen( m -> log( m, next ) );
        }
        return consumer;
    }
}
