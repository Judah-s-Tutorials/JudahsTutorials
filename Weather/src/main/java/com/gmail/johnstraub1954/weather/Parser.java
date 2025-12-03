package com.gmail.johnstraub1954.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Parser
{
    private static final String indent  = "    ";
    
    private static final String apiKey  =   "cb4566e090b84696a7d161318252911";
    private static final String urlHost =   "api.weatherapi.com";
    private static final String urlPath =   "/v1/forecast.json";
    private static final String params  = 
        "key=" + apiKey + "&q=41.4,2.2&days=3";
    private static final String testURL =   
        "https://" + urlHost + urlPath + "?" + params;
    
    private boolean quoted      = false;
    private int     indentLevel = 0;
    
    public static void main(String[] args)
    {
        Parser  app     = new Parser();
        app.exec();
    }

    public void exec()
    {
        List<String> list   = null;
        try
        {
            URI uri                     = new URI( testURL );
            URL url = uri.toURL();
            InputStreamReader inReader  = 
                new InputStreamReader( url.openStream()  );
            BufferedReader bufStream    = new BufferedReader( inReader );
            list = bufStream.lines().toList();
        }
        catch ( URISyntaxException | IOException exc  )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        System.out.println( list.size() );
        String line = list.get( 0 );
        System.out.println( line.length() );
        System.out.println( line );
        parse( line );
    }
    
    private void parse( String line )
    {
        String          endl    = System.lineSeparator();
        StringBuilder   bldr    = new StringBuilder();
        char[]          cArr    = line.toCharArray();
        for ( char ccc : cArr )
        {
            switch ( ccc )
            {
            case '"':
                quoted = !quoted;
                break;
            case '{':
            case '[':
                if ( !quoted )
                {
                    bldr.append( endl );
                    indent( bldr );
                    bldr.append( ccc );
                    bldr.append( endl );
                    ++indentLevel;
                    indent( bldr );
                }
                break;
            case '}':
            case ']':
                if ( !quoted )
                {
                    --indentLevel;
                    bldr.append( endl );
                    indent( bldr );
                    bldr.append( ccc );
                    bldr.append( endl );
                }
                break;
            case ',':
                bldr.append( endl );
                indent( bldr );
                break;
            default:
                bldr.append( ccc );    
            }
        }
        System.out.println( bldr );
        write( bldr );
    }
    
    private void indent( StringBuilder bldr )
    {
        for ( int inx = 0 ; inx < indentLevel ; ++inx )
            bldr.append( indent );
    }
    
    private void write( StringBuilder bldr )
    {
        File    file    = new File( "temp.txt" );
        try ( FileWriter fWriter = new FileWriter( file ) )
        {
            PrintWriter pWriter = new PrintWriter( fWriter );
            pWriter.println( bldr );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }
}
