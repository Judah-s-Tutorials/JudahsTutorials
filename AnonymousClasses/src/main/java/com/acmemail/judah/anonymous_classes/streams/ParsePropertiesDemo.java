package com.acmemail.judah.anonymous_classes.streams;

import java.util.Properties;

/**
 * This application does nothing more
 * than demonstrating that
 * {@linkplain PropertiesFileParser}
 * works.
 * 
 * @author Jack Straub
 */
public class ParsePropertiesDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        PropertiesFileParser    parser  = 
            new PropertiesFileParser( "temp/props.ini" );
        Properties  props   = parser.getProperties();
        props.entrySet().forEach( System.out::println );
    }
}
