package com.judahstutorials.glossary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Encode
{
    private static final String endl    = System.lineSeparator();
    private static final String head1   =
        "<!DOCTYPE html>" + endl
        + "<html lang=\"en\">" + endl
        + "<head>" + endl
        + "<meta charset=\"UTF-8\">" + endl;

    private static final String head2   =
       "</head>" + endl
        + "<body>" + endl
        + "<dl>" + endl;

    private static final String tail    =
        "</dl>" + endl
        + "</body>" + endl
        + "</html>" + endl;

    
    public Encode()
    {
        // TODO Auto-generated constructor stub
    }
    
    public static String getHead( String title )
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( head1 );
        bldr.append( "<title>" ).append( title )
            .append( "</title>" ).append( endl );
        getCSS( bldr );
        bldr.append( head2 );
        
        return  bldr.toString();
    }
    
    public static String getTail()
    {
        return  tail;
    }

    public static String getDefinition( Definition_draft def )
    {
        String          slug        = Utils.getSlug( def );
        String          desc        = def.getDescription();
        String          term        = def.getTerm();
        StringBuilder   htmlBldr    = new StringBuilder();
        htmlBldr.append( "<dt id=\"" ).append( slug ).append( "\">" )
                .append( term ).append( "</dt>").append( endl );
        htmlBldr.append( "<dd>" ).append( endl );
        htmlBldr.append( desc ).append( endl );
        htmlBldr.append( "</dd>" ).append( endl );
        return htmlBldr.toString();
    }
    
    private static void getCSS( StringBuilder bldr )
    {
        File    file    = new File( "input/glossary.css" );
        bldr.append( "<style>" ).append( endl );
        getCSS( bldr, file );
        bldr.append( "</style>" ).append( endl );
    }
    
    private static void getCSS( StringBuilder bldr, File file )
    {
        try ( 
            FileReader  fReader = new FileReader( file );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            bReader.lines()
                .map( l -> l + endl )
                .forEach( bldr::append );
        }
        catch ( IOException exc )
        {
            String  msg = "Failed to read " + file.getName();
            throw new FormatException( msg, exc );
        }
    }
}
