package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.Consumer;

/**
 * This is a simple demonstration of how 
 * the Consumer <em>adThen</em> default method
 * can be used to chain several Consumer.accept() invocations
 * as a single lambda.
 * 
 * @author Jack Straub
 */
public class ConsumerAndThenDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        Consumer<String>    toHomeOffice    = m -> sendTo( m, "Home Office" );
        Consumer<String>    toNewYork       = m -> sendTo( m, "New York" );
        Consumer<String>    toOmaha         = m -> sendTo( m, "Omaha" );
        Consumer<String>    sendAll         =
            toHomeOffice.andThen( toNewYork ).andThen( toOmaha );
        
        // The following method invocation will send a message to the home office,
        // and then to the New York office and then to the Omaha office.
        sendTo( "Hello from New Guinea", sendAll );
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
}
