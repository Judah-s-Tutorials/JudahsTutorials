package com.judahstutorials.glossary;

import static com.judahstutorials.glossary.GConstants.DEFINITION;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Formatter
{
    private final Element   rootElement;
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        Formatter   formatter   = new Formatter( "input/try1.xml" );
        formatter.format();
    }
    
    public Formatter( String input ) throws FormatException
    {
        File    xmlFile     = new File( input );
        if ( !xmlFile.exists() )
        {
            String  msg     = "\"" + input + "\": doesn't exist";
            throw new FormatException( msg );
        }
        DocumentBuilderFactory  factory     =
            DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder docBuilder  = factory.newDocumentBuilder();
            Document        xmldoc      = docBuilder.parse( input );
            rootElement = xmldoc.getDocumentElement();  
        }
        catch ( ParserConfigurationException 
                | IOException 
                | SAXException
                    exc
        )
        {
            String  msg     = "Core exception: " + exc.getMessage();
            throw new FormatException( msg, exc );
        }
        catch ( Exception exc )
        {
            String  msg     = "Unexpected exception: " + exc.getMessage();
            throw new FormatException( msg, exc );
        }
    }
    
    public void format()
    {
        NodeList    defs        = 
            rootElement.getElementsByTagName( DEFINITION );
        int         numNodes    = defs.getLength();
        StringBuilder   bldr    = new StringBuilder();
        String          title   = Utils.getTitle( rootElement );
        bldr.append( Encode.getHead( title ) );
        for ( int inx = 0 ; inx < numNodes ; ++inx )
        {
            Element     ele     = (Element)defs.item( inx );
            Definition  def     = new Definition( ele );
            String      html    = Encode.getDefinition( def );
            bldr.append( html );
        }
        bldr.append( Encode.getTail() );
        writeTemp( bldr );
        System.out.println( bldr );
    }
    
    private void writeTemp( StringBuilder bldr )
    {
        File    file    = new File( "output/temp.html" );
        try ( 
            PrintWriter writer = new PrintWriter( file );
        )
        {
            writer.print( bldr.toString() );
        }
        catch ( IOException exc )
        {
            String  msg = "Failed to write " + file.getName();
            throw new FormatException( msg, exc );
        }
    }
}
