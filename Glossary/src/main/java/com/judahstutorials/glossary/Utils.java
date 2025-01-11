package com.judahstutorials.glossary;

import static com.judahstutorials.glossary.GConstants.TITLE;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Utils
{
    private static final String validSlugChars  =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "abcdefghijklmnopqrstuvwxyz"
        + "0123456789"
        + "-_:.";
    
    /**
     * Prevent instantiation of utility class.
     */
    private Utils()
    {
    }

    public static void verifyHasSingleton()
    {
        
    }
    
    public static String getTitle( Element root )
    {
        String      title   = "Title Not Found";
        NodeList    list    = root.getElementsByTagName( TITLE );
        if ( list.getLength() > 0 )
            title = list.item( 0 ).getTextContent();
        return title;
    }
    
    public static String getSlug( Definition def )
    {
        String  rawSlug = def.getSlug();
        if ( rawSlug == null )
        {
            rawSlug = def.getTerm();
            Integer seqNum  = def.getSeqNum();
            if ( seqNum != null )
                rawSlug += "-" + seqNum;
        }
        String  slug    = encodeSlug( rawSlug );
        return slug;
    }
    
    public static String getTerm( Definition def )
    {
        String  term    = def.getSlug();
            Integer seqNum  = def.getSeqNum();
        if ( seqNum != null )
            term += "(" + seqNum + ")";
        return term;
    }
    
    public static String encodeSlug( String rawSlug )
    {
        StringBuilder   slugBldr    = new StringBuilder();
        String          workingSlug = rawSlug.trim();
        if ( workingSlug.isEmpty() )
            slugBldr.append( 'A' );
        else if ( !Character.isAlphabetic( workingSlug.charAt( 0 ) ) )
            slugBldr.append( 'A' );
        else
            ;
        workingSlug.chars()
            .map( c -> encodeSlugChar( (char)c ) )
            .forEach( c -> slugBldr.append( (char)c ) );
        return slugBldr.toString();
    }
    
    public static char encodeSlugChar( char ccc )
    {
        char    slugChar    = ccc;
        if ( !validSlugChars.contains( "" + ccc ) )
            slugChar = '-';
        return slugChar;
    }
}
