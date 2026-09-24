package com.acmemail.judah.battleship.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineWrapper
{
    private static final String TAB         = "    ";
    private final int           lineLen;
    private final List<String>  origList;
    private final List<String>  wrappedList = new ArrayList<>();
    
    public LineWrapper( int lineLen, List<String> strings )
    {
        Objects.requireNonNull( strings, "strings" );
        this.lineLen = lineLen;
        origList = strings;
        strings.forEach( this::processString );
    }
    
    /**
     * Get the original list of strings.
     * 
     * @return the original list of strings
     */
    public List<String> getOrigList()
    {
        return origList;
    }

    /**
     * Get the list of wrapped strings.
     * 
     * @return the list of wrapped strings
     */
    public List<String> getWrappedList()
    {
        return wrappedList;
    }

    private void processString( String str )
    {
        StringBuilder   workStr = new StringBuilder( str );
        StringBuilder   tabStr  = new StringBuilder();
        // preserve tabs at the beginning of the string
        while ( !workStr.isEmpty() && workStr.charAt( 0 ) == '\t' )
        {
            tabStr.append( TAB );
            workStr.deleteCharAt( 0 );
        }
        
        // whitespace at the beginning and end of the string is eliminated
        workStr = new StringBuilder( str.trim() );
        // empty strings translate to a blank line 
        if ( workStr.isEmpty() )
            wrappedList.add( tabStr.toString() );
        else
            processString( workStr.toString(), tabStr.toString() );
    }
    
    private void processString( String str, String tabStr )
    {
        int             charLimit   = lineLen - 1;
        String[]        words       = str.split( "\\s+" );
        StringBuilder   currLine    = new StringBuilder( tabStr );
        for ( String word : words )
        {
            String  workWord    = word;
            if ( workWord.length() > lineLen )
                workWord = splitLongWord( workWord );
            if ( currLine.length() + workWord.length() > charLimit )
            {
                wrappedList.add( currLine.toString() );
                currLine = new StringBuilder( workWord );
            }
            else
            {
                if ( !currLine.isEmpty() )
                    currLine.append( ' ' );
                currLine.append( workWord );
                
            }
        }
        if ( !currLine.isEmpty() )
            wrappedList.add( currLine.toString() );
    }
    
    private String splitLongWord( String longWord )
    {
        String  workWord    = longWord;
        while ( workWord.length() > lineLen )
        {
            wrappedList.add( workWord.substring( 0, lineLen ) );
            workWord = workWord.substring( lineLen );
        }
        return workWord;
    }
}
