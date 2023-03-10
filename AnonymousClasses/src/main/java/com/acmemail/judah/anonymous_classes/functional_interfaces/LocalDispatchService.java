package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.zip.CRC32;

/**
 * The class is used
 * as part of a demonstration
 * of how to use a <em>Consumer</em> functional interface.
 * 
 * @author Jack Straub
 */
public class LocalDispatchService
{
    /**
     * Dispatches a message to a given consumer.
     * The message is formatted
     * according to the required
     * protocol and corporate standards,
     * including a date/time stamp in UTC,
     * and CRC generation.
     * 
     * @param message   the message to dispatch
     * @param consumer  the given consumer
     */
    public static void dispatch( Message message, Consumer<Message> consumer )
    {
        ZonedDateTime   zonedDateTime   = ZonedDateTime.now();
        String          strDateTime     = 
            zonedDateTime.format( DateTimeFormatter.ISO_ZONED_DATE_TIME );
        message.setDateTimeUTC( strDateTime );
        
        message.setEncoding( "utf-16" );
        message.setAssembledBy( ConsumerDemo.class.getName() );
        
        byte[]  contentBytes    = message.getContent().getBytes();
        CRC32   crc             = new CRC32();
        crc.update( contentBytes );
        message.setChecksumAlgo( "crc32" );
        message.setChecksum( Long.toString( crc.getValue() ) );
        
        consumer.accept( message );
    }
    
    /**
     * This method masquerades as a "secure mail service,"
     * simulating the abstract method
     * required by the <em>Consumer&gt;Message&lt;</em>
     * functional interface.
     * The given message
     * is printed to stdout.
     * 
     * @param msg   the Message to consume
     */
    public static void secureMailer( Message msg )
    {
        System.out.println( "queuing message for secure mail" );
    }
    
    /**
     * This method masquerades as a "log service,"
     * simulating the abstract method
     * required by the <em>Consumer&gt;Message&lt;</em>
     * functional interface.
     * The given message
     * is printed to stdout.
     * 
     * @param msg   the Message to consume
     */
    public static void logger( Message msg )
    {
        System.out.println( "queuing message for writing to history DB" );
    }
    
    /**
     * This method masquerades as a "notification service,"
     * simulating the abstract method
     * required by the <em>Consumer&gt;Message&lt;</em>
     * functional interface.
     * The given message
     * is printed to stdout.
     * 
     * @param msg   the Message to consume
     */
    public static void notifier( Message msg )
    {
        System.out.println( "queuing notification" );
    }
}
