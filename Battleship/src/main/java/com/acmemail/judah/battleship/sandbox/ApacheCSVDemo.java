package com.acmemail.judah.battleship.sandbox;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ApacheCSVDemo
{

    public static void main(String[] args) throws IOException
    {
        File            file    = new File( "config.ini" );
        List<CSVRecord> recs;
        CSVFormat       format  = 
            CSVFormat.DEFAULT.builder()
                .setCommentMarker( '#' )
                .setIgnoreSurroundingSpaces(true)
                .setQuote( '"' )
                .get();
        try (
            FileReader  fReader = new FileReader( file );
            CSVParser parser = format.parse( fReader );
        )
        {
            recs = parser.getRecords();
        }
        
        recs.forEach( r -> System.out.println( toString( r ) ) );
    }

    private static String toString( CSVRecord rec )
    {
        StringBuilder   bldr    = new StringBuilder();
        for ( String value : rec.values() )
            bldr.append( value ).append( "," );
        bldr.deleteCharAt( bldr.length() - 1 );
        return bldr.toString();
    }
}
