package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.Consumer;

public class ConsumerAndThenDemo1
{
    public static void main( String[] args )
    {
        Consumer<String>    toHomeOffice    = m -> sendTo( m, "Home Office" );
        Consumer<String>    toNewYork       = m -> sendTo( m, "New York" );
        Consumer<String>    toOmaha         = m -> sendTo( m, "Omaha" );
        Consumer<String>    sendAll         =
            toHomeOffice.andThen( toNewYork ).andThen( toOmaha );
        sendTo( "Hello from New Guinea", sendAll );
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
}
