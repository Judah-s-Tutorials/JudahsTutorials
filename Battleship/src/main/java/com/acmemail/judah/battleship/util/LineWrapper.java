package com.acmemail.judah.battleship.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This is a lightweight utility
 * to help simplify line-wrapping tasks.
 * An input list of strings is treated as a block of text.
 * It is rendered into an output list of strings
 * containing the same text,
 * but rendered into lines of a given maximum length.
 * For example, given a maximum line length of 40,
 * the list:
 * <pre style="margin-left: 2em;">
String          graph1  =
    "When in the Course of human events, it becomes "
    + "necessary for one people to dissolve the political bands which"
    + " have connected them with another, and to assume among the "
    + "powers of the earth, the separate and equal station to which the "
    + "Laws of Nature and of Nature's God entitle them, a decent "
    + "respect to the opinions of mankind requires that they should"
    + " declare the causes which impel them to the separation.";
String          graph2  =
    "We hold these truths to be self-evident, that all men are created "
    + "equal, that they are endowed by their Creator with certain "
    + "unalienable Rights, that among these are Life, Liberty and the "
    + "pursuit of Happiness.";

List&lt;String>    lines   = List.of( graph1, "", graph2 );</pre>
 * <p>
 * Is rendered into the list:
 * <pre style="margin-left: 2em;">
"When in the Course of human events, it",
"becomes necessary for one people to",
"dissolve the political bands which have",
"connected them with another, and to",
"assume among the powers of the earth,",
"the separate and equal station to which",
"the Laws of Nature and of Nature's God",
"entitle them, a decent respect to the",
"opinions of mankind requires that they",
"should declare the causes which impel",
"them to the separation.",
"",
"We hold these truths to be self-evident,",
"that all men are created equal, that",
"they are endowed by their Creator with",
"certain unalienable Rights, that among",
"these are Life, Liberty and the pursuit",
"of Happiness.",
 * </pre>
 * <p>
 * Where possible,
 * line breaks occur at whitespace characters,
 * however, if a single token
 * is greater than the maximum line length,
 * the token itself will be divided
 * into to two or more separate lines.
 * <p>
 * Tab characters <strong>at the beginning of a string</strong>
 * are rendered into spaces;
 * tab characters in the middle of line are ignored.
 * The default tab length is four spaces,
 * and can be changed for an individual instance of LineWrapper.
 * A tab character in the middle of a string
 * will produce unpredictable results.
 * <p>
 * Non-displayable characters inserted into a string are ignored.
 * The result of doing so depends on the architecture of the medium
 * it is displayed on.
 * <p>
 * Non-ASCII unicode characters are ignored, 
 * the result of doing so depends on the architecture of the medium
 * it is displayed on.
 * <p>
 * After expanding the tabs at the beginning of a line:
 * <ol type="a">
 * <li>
 * If the number of expanded spaces
 * exceeds the line length,
 * it will be truncated,
 * and rendered as a single blank line.
 * The first non-space token 
 * will be rendered on the next line.
 * </li>
 * <li>
 * If the number of expanded spaces
 * plus the length of the first non-space token
 * exceeds the line length,
 * two lines will be rendered,
 * the first a blank line
 * and the second beginning with
 * the first non-space word.
 * </li>
 * <p>
 * A line length of less than one
 * will throw an IllegalArgumentException.
 */
public class LineWrapper
{
    private static final String TAB         = "    ";
    private final String        tab;
    private final int           lineLen;
    private final List<String>  origList;
    private final List<String>  wrappedList = new ArrayList<>();
    
    public LineWrapper( int lineLen, List<String> strings )
    {
        this( lineLen, strings, TAB );
    }
    
    public LineWrapper( int lineLen, List<String> strings, String tab )
    {
        Objects.requireNonNull( strings, "strings" );
        Objects.requireNonNull( tab, "tab" );
        if ( lineLen < 1 )
        {
            String  msg     = "Invalid line length: " + lineLen;
            throw new IllegalArgumentException( msg );
        }
        this.tab = tab;
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

    /**
     * Get the string used to replace
     * tabs at the start of a string
     * rendered using this LineWrapper.
     * 
     * @return the string used to replace tabs at the start of a string
     */
    public String getTab()
    {
        return tab;
    }

    /**
     * Get the default string used to replace
     * tabs at the start of a string.
     * 
     * @return the string used to replace tabs at the start of a string
     */
    public static String getDefaultTab()
    {
        return TAB;
    }

    private void processString( String str )
    {
        StringBuilder   workStr = new StringBuilder( str );
        StringBuilder   tabStr  = new StringBuilder();
        // preserve tabs at the beginning of the string
        int             numTabs = 0;
        while ( numTabs < str.length() && str.charAt( numTabs ) == '\t' )
            ++numTabs;
        IntStream.range( 0, numTabs ).forEach( i -> tabStr.append( tab ) );
        
        // whitespace at the beginning and end of the string is eliminated
        workStr = new StringBuilder( str.trim() );
        // empty strings translate to a blank line 
        if ( workStr.isEmpty() )
            wrappedList.add( tabStr.toString() );
        else
            processString( workStr.toString(), tabStr.toString() );
    }
    
    /**
     * 
     * Precondition: 
     * tab characters ('\t') at the beginning of a line
     * are expanded into a string of spaces.
     * 
     * @param str
     * @param tabStr
     */
    private void processString( String str, String tabStr )
    {
        String[]        tokens      = str.split( "\\s+" );
        StringBuilder   currLine    = tabStr.length() < lineLen ?
            new StringBuilder( tabStr ) :
            new StringBuilder( tabStr.substring( 0, lineLen ) );
        for ( String word : tokens )
        {
            String  workWord    = word;
            if ( workWord.length() > lineLen )
            {
                if ( !currLine.isEmpty() )
                {
                    wrappedList.add( currLine.toString() );
                    currLine.setLength( 0 );
                    currLine.append( tabStr );
                }
                workWord = splitLongWord( workWord );
            }
            // a separating space is only needed (and only counts against
            // the line length) when currLine already has a word on it
            boolean needsSpace  = !currLine.toString().isBlank();
            int     neededLen   =
                currLine.length() + ( needsSpace ? 1 : 0 ) + workWord.length();
            if ( neededLen > lineLen )
            {
                if ( !currLine.toString().isEmpty() )
                {
                    wrappedList.add( currLine.toString() );
                    currLine.setLength( 0 );
                    currLine.append( tabStr );
                }
                currLine.append( workWord );
            }
            else
            {
                if ( needsSpace )
                    currLine.append( ' ' );
                currLine.append( workWord );
            }
        }
        if ( !currLine.isEmpty() )
            wrappedList.add( currLine.toString() );
    }
    
    /**
     * Splits a word that is longer than the line length
     * into two or more lines
     * consisting of line-length characters.
     * The substring left over
     * after splitting the word
     * is returned to the caller.
     * Specifically:
     * if ll is the maximum length of a line
     * and lw is the length of the word,
     * the word will be split into lw / ll lines
     * consisting of ll character each,
     * and the remainder,
     * the last lw % ll characters,
     * is returned to the caller.
     * 
     * @param longWord  the word to split
     * 
     * @return  the characters remaining after splitting the word
     */
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
