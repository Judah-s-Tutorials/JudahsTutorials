package com.judahstutorials.glossary;

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
