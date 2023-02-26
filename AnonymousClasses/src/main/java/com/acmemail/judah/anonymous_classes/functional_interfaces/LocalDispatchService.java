package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.zip.CRC32;

public class LocalDispatchService
{
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
    
    public static void secureMailer( Message msg )
    {
        System.out.println( "queuing message for secure mail" );
    }
    
    public static void logger( Message msg )
    {
        System.out.println( "queuing message for writing to history DB" );
    }
    
    public static void notifier( Message msg )
    {
        System.out.println( "queuing notification" );
    }
}
