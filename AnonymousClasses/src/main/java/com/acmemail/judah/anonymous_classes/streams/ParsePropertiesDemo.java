package com.acmemail.judah.anonymous_classes.streams;

import java.util.Properties;

public class ParsePropertiesDemo
{
    public static void main( String[] args )
    {
        PropertiesFileParser    parser  = 
            new PropertiesFileParser( "temp/props.ini" );
        Properties  props   = parser.getProperties();
        props.entrySet().forEach( System.out::println );
    }
}
