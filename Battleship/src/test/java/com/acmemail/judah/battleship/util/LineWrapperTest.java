package com.acmemail.judah.battleship.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class LineWrapperTest
{
    private static final String CHAR_POOL    =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int    BIGGISH_TEST_LINE_LEN   = 30;
    
    @Test
    public void testLineWrapper()
    {
        String  withoutTab  = "With tab";
        String  withTab     = "\tWith tab";
        String  defaultTab  = LineWrapper.getDefaultTab();
        String  customTab   = defaultTab + " ";
        
        LineWrapper     wrapper = new LineWrapper( 50, List.of( withoutTab ) );
        List<String>    lines   = wrapper.getWrappedList();
        assertEquals( 1, lines.size() );
        assertFalse( lines.getFirst().startsWith( " " ) );
        
        wrapper = new LineWrapper( 50, List.of( withTab ) );
        lines = wrapper.getWrappedList();
        assertEquals( 1, lines.size() );
        assertTrue( lines.getFirst().startsWith( defaultTab ) );
        assertFalse( lines.getFirst().startsWith( customTab ) );
        
        wrapper = new LineWrapper( 50, List.of( withTab ), customTab );
        lines = wrapper.getWrappedList();
        assertEquals( 1, lines.size() );
        assertTrue( lines.getFirst().startsWith( customTab ) );
    }
    
    @Test
    public void testLineWrapperGoWrong()
    {
        List<String>    list    = new ArrayList<>();
        assertThrows( NullPointerException.class, 
            () -> new LineWrapper( 10, null )
        );
        assertThrows( IllegalArgumentException.class, 
            () -> new LineWrapper( 0, list )
        );
        assertThrows( NullPointerException.class, 
            () -> new LineWrapper( 10, list, null )
        );
    }

    @Test
    public void testLineWrapperLineLength1()
    {
        String          line1       = "0123 567 901";
        String          line2       = "23 567 901";
        List<String>    expLines    = new ArrayList<>();
        line1.chars()
            .filter( c -> c != ' ' )
            .mapToObj( i -> String.valueOf( (char)i ) )
            .forEach( expLines::add );
        List<String>    actLines    =
            new LineWrapper( 1, List.of( line1 ) ).getWrappedList();
        assertEquals( expLines, actLines );

        line2.chars()
            .filter( c -> c != ' ' )
            .mapToObj( i -> String.valueOf( (char)i ) )
            .forEach( expLines::add );
        actLines =
            new LineWrapper( 1, List.of( line1, line2 ) ).getWrappedList();
        assertEquals( expLines, actLines );
    }
    
    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8, 9, 10, 20, 30})
    public void testLineWrapperSmallNNoLongWords( int lineLen )
    {
        int             maxWordLen  = Math.min( 10, lineLen - 1 );
        List<String>    words   =
            getWords( 10, maxWordLen ).toList();
        String          line1   =
            String.join( " ", words );
        List<String>    lineList    = List.of( line1 );
        
        List<String>    expList     = new ArrayList<>();
        StringBuilder   currLine    = new StringBuilder();
        for ( String word : words )
        {
            boolean needsSpace  = !currLine.isEmpty();
            int     neededLen   =
                currLine.length() + ( needsSpace ? 1 : 0 ) + word.length();
            if ( neededLen > lineLen )
            {
                expList.add( currLine.toString() );
                currLine.setLength( 0 );
                needsSpace = false;
            }
            if ( needsSpace )
                currLine.append( " " );
            currLine.append( word );
        }
        expList.add( currLine.toString() );
        
        LineWrapper     wrapper     = new LineWrapper( lineLen, lineList );
        List<String>    wrappedList = wrapper.getWrappedList();
        assertEquals( expList, wrappedList );

    }
    
    @ParameterizedTest
    @MethodSource("getBiggishWordLengths")
    public void testBiggishWordsA( int wordLen )
    {
        // A line of words, where each word is between
        // line-len / 2 and line-len chars, should each wind up
        // on a wrapped line by itself.
        String          biggishWord = CHAR_POOL.substring( 0, wordLen );
        int             numWords    = 5;
        int             numLines    = 5;
        List<String>    linesIn     = new ArrayList<>();
        List<String>    expResult   = new ArrayList<>();
        for ( int inx = 0 ; inx < numLines ; ++inx )
        {
            StringBuilder   line    = new StringBuilder();
            for ( int jnx = 0 ; jnx < numWords ; ++jnx )
            {
                line.append( biggishWord ).append( " " );
                expResult.add( biggishWord );
            }
            linesIn.add( line.toString() );
        }
        int             lineLen     = BIGGISH_TEST_LINE_LEN;
        LineWrapper     wrapper     = new LineWrapper( lineLen, expResult );
        assertEquals( expResult, wrapper.getWrappedList() );
    }
    
    @Test
    public void testBiggishWordsB()
    {
        // Given: a repeating sequence of 2 short words taking up half a line
        // followed by a long word half the length of the line::
        //     0123456789 123456789
        //     ====================
        //     abcd abcdef abcdefghij abc abcd abcdef abcdefghij...
        // Every two sequences should render as a repeating pattern 
        // of three lines:
        //     0123456789 123456789
        //     ====================
        //     abc abcdef          (1)
        //     abcdefghij abc      (2)
        //     abcdef abcdefghij   (3)
        //     abc abcdef
        //     abcdefghij abc 
        //     abcdef abcdefghij
        int             lineLen     = 20;
        int             wordALen    = lineLen / 4 - 1;
        int             wordBLen    = lineLen / 4 + 1;
        int             wordCLen    = lineLen / 2;
        String          wordA       = CHAR_POOL.substring( 0, wordALen );
        String          wordB       = CHAR_POOL.substring( 0, wordBLen );
        String          wordC       = CHAR_POOL.substring( 0, wordCLen );
        String          sequence    = wordA + " " + wordB + " " + wordC + " ";
        String          subSeq1     = wordA + " " + wordB;
        String          subSeq2     = wordC + " " + wordA;
        String          subSeq3     = wordB + " " + wordC;
        StringBuilder   line        = new StringBuilder();
        List<String>    expResult   = new ArrayList<>();
        
        // Must be an even number
        int             numSeqs     = 6;
        assertEquals( numSeqs % 2, 0 );
        for ( int inx = 0 ; inx < numSeqs ; ++inx )
        {
            line.append( sequence );
            if ( inx % 2 == 1 )
            {
                expResult.add( subSeq1 );
                expResult.add( subSeq2 );
                expResult.add( subSeq3 );
            }
        }
        List<String>    input       = List.of( line.toString() );
        LineWrapper     wrapper     = new LineWrapper( lineLen, input );
        List<String>    actResult   = wrapper.getWrappedList();
        assertEquals( expResult, actResult );
    }

    @Test
    public void testGetLists()
    {
        List<String>    expOrigList     = 
            List.of( "ab def hi klmn", "abc efg ijk" );
        List<String>    expWrappedList  = new ArrayList<>( expOrigList );
        int             lineLen     =
            expOrigList.stream()
                .mapToInt( String::length ).max().getAsInt() + 1;
        LineWrapper     wrapper     = new LineWrapper( lineLen, expOrigList );
        assertEquals( expOrigList, wrapper.getOrigList() );
        assertEquals( expWrappedList, wrapper.getWrappedList() );
    }
    
    @Test
    public void testTabA()
    {
        String  tab             = LineWrapper.getDefaultTab();
        int     tabLen          = tab.length();
        int     maxTabs         = 4;
        int     shortWordLen    = 2;
        int     longWordLen     = 20;
        String  shortWord       = CHAR_POOL.substring( 0, shortWordLen );
        String  longWord        = CHAR_POOL.substring( 0, longWordLen );
        int     lineLen         = longWordLen + shortWordLen + tabLen - 1;
        
        // 1. line length must be long enough for shortWord + " " + longWord
        // 2. line length must too short to fit \t + shortWord + " " + longWord
        // 3. line length must be long enough for maxTAb * \t + shortWord
        assertTrue( lineLen >= shortWordLen + longWordLen + 1 );
        assertTrue( lineLen < tabLen + shortWordLen + longWordLen + 1 );
        assertTrue( lineLen > maxTabs * tabLen + shortWordLen );
        
        String          line        = shortWord + " " + longWord;
        List<String>    lineList    = List.of( line );
        LineWrapper     wrapper     = new LineWrapper( lineLen, lineList );
        List<String>    wrappedList = wrapper.getWrappedList();
        assertEquals( 1, wrappedList.size() );

        String          prefixIn    = "";
        String          prefixOut   = "";
        for ( int inx = 1 ; inx < maxTabs ; ++inx )
        {
            prefixIn += "\t";
            prefixOut += tab;
            lineList = List.of( prefixIn + line );
            wrapper = new LineWrapper( lineLen, lineList );
            wrappedList = wrapper.getWrappedList();
            assertEquals( 2, wrappedList.size() );
            assertEquals( prefixOut + shortWord, wrappedList.get( 0 ) );
            assertEquals( longWord, wrappedList.get( 1 ) );
        }
    }
    
    @Test
    public void testTabB()
    {
        String          tab             = LineWrapper.getDefaultTab();
        int             tabLen          = tab.length();
        int             maxTabs         = 4;
        int             wordALen        = 2;
        int             wordBLen        = 3;
        String          wordA           = CHAR_POOL.substring( 0, wordALen );
        String          wordB           = CHAR_POOL.substring( 0, wordBLen );
        int             lineLen         = 4 * tabLen + wordALen + 1 + wordBLen;
        String          tail            = wordA + " " + wordB;
        StringBuilder   tabChars        = new StringBuilder();
        StringBuilder   tabStr          = new StringBuilder();
        IntStream.range( 0, maxTabs * 2 ).forEach( i -> {
            int             tabStrLen   = tabStr.length();
            List<String>    expLines    = new ArrayList<>();
            if ( tabStrLen + wordALen + wordBLen <= lineLen )
            {
                expLines.add( tabStr + wordA + " " + wordB );
            }
            else if ( tabStrLen + wordALen <= lineLen )
            {
                expLines.add( tabStr + wordA );
                expLines.add( wordB );
            }
            else
            {
                int     actTabStrLen    = (int)Math.min( lineLen, tabStrLen );
                String  tabSubStr       = tabStr.substring( 0, actTabStrLen );
                expLines.add( tabSubStr );
                expLines.add( wordA + " " + wordB );
            }
            
            String          lineIn      = tabChars.toString() + tail;
            List<String>    lineList    = List.of( lineIn );
            LineWrapper     wrapper     = new LineWrapper( lineLen, lineList );
            List<String>    actLines    = wrapper.getWrappedList();
            assertEquals( expLines, actLines, "#tabs=" + tabChars.length() );
            
            tabChars.append( '\t' );
            tabStr.append( tab );
        });
    }
    
    @Test
    public void testMisplacedTab()
    {
        // placing a tab in the middle of a string yields
        // undefined results; however, it should not crash.
        List<String>    list    = List.of( "a\tb" );
        assertDoesNotThrow( () -> new LineWrapper( 50, list ) );
    }
    
    private static IntStream getBiggishWordLengths()
    {
        int min = BIGGISH_TEST_LINE_LEN / 2;
        int max = BIGGISH_TEST_LINE_LEN;
        return IntStream.rangeClosed( min, max );
    }
    
    private Stream<String> getWords( int numWords, int charLimit )
    {
        assertTrue( charLimit < CHAR_POOL.length() );
        Random  randy   = new Random( 0 );
        Stream<String>  stream  =
            IntStream.range( 0, numWords )
                .map( i -> randy.nextInt( charLimit ) + 1 )
                .mapToObj( i -> CHAR_POOL.substring( 0, i ) );
        return stream;
    }
}
