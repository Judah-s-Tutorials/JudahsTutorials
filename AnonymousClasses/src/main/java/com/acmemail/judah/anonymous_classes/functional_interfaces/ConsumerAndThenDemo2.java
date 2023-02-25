package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    
    public static void main( String[] args )
    {
        Consumer<String>    allConsumers    = 
            getSendToRecipients();
        sendTo( "Greetings from Anchorage", allConsumers );
    }
    
    private static void sendTo( String message, Consumer<String> consumer )
    {
        consumer.accept( message );
    }
    
    private static void sendTo( String message, String recipient )
    {
        System.out.println( "sending to: " + recipient );
        System.out.println( message );
        System.out.println( "************* end message **************" );
    }
    
    private static void log( String message, String recipient )
    {
        StringBuilder   bldr    = new StringBuilder( " **** Logging: " )
            .append( "recipient=" ).append( recipient ).append( "; " )
            .append( "message=" ).append( message );
        System.out.println( bldr );
    }
    
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
