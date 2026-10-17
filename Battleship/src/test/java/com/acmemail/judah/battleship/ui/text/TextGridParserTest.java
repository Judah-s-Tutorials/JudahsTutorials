package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_START;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class TextGridParserTest
{
    private static final String         name            = "TestName";
    private static final String         ignore          = 
        "This line to be ignored";
    private static final List<String>   ignoreLines     = 
        List.of( ignore, ignore, ignore );
    private static final String         boundaryLine    = "************";
    private static final String         emptyRowLine    = "------------";
    
    /** Column headers to use for testing. */
    private static final List<String>   sampleColHeaders  = 
        List.of( 
            "                   1 1 1",
            " 1 2 3 4 5 6 7 8 9 0 1 2"
        );
    /** Labeled rows for testing. */
    private static final List<String>   sampleLabeledRows =
        List.of(
            " A-------",
            " B-------",
            " C-------",
            " D-------",
            " E-------",
            " F-------"
        );
    /** Text grid for testing; see {@link #TextGridParserTest()}. */
    private final List<String>  sampleGrid;
    
    public TextGridParserTest()
    {
        sampleGrid = new ArrayList<>();
        sampleGrid.add( name );
        sampleGrid.addAll( sampleColHeaders );
        sampleGrid.add( boundaryLine );
        sampleLabeledRows.forEach( s -> 
            sampleGrid.addAll( List.of( emptyRowLine, s, emptyRowLine ) )
        );
        sampleGrid.add( boundaryLine );

    }

    @Test
    public void testEmpty()
    {
        TextGridParser  parser = new TextGridParser( Collections.emptyList() );
        assertEmpty( parser );
    }
    
    @Test
    public void baseTestGoRightWithSentinels()
    {
        List<String>    testLines   = new ArrayList<>();
        testLines.addAll( ignoreLines );
        testLines.add( CONFIG_GRID_START );
        testLines.addAll( sampleGrid );
        testLines.add( CONFIG_GRID_END );
        testLines.addAll( ignoreLines );
        
        TextGridParser  parser  = new TextGridParser( testLines );
        assertEquals( name, parser.getName() );
        assertEquals( sampleColHeaders, parser.getColumnHeader() );
        assertEquals( sampleLabeledRows, parser.getLabeledRows() );
        assertEquals( sampleGrid, parser.getGrid() );
    }
    
    @Test
    public void baseTestGoRightWithoutSentinels()
    {
        TextGridParser  parser  = new TextGridParser( sampleGrid );
        assertEquals( name, parser.getName() );
        assertEquals( sampleColHeaders, parser.getColumnHeader() );
        assertEquals( sampleLabeledRows, parser.getLabeledRows() );
        assertEquals( sampleGrid, parser.getGrid() );
    }
    
    @Test
    public void baseTestGoWrongWithoutSentinels()
    {
        List<String>    badLines    = List.of( ignore, ignore, ignore );
        List<String>    lines       = new ArrayList<>();
        lines.addAll( badLines );
        lines.addAll( sampleGrid );
        lines.addAll( badLines );
        // The result of parsing is unpredictable. The important thing
        // is that the scrambled lines don't offend the parser to the
        // point of a hard failure.
        assertDoesNotThrow( () -> new TextGridParser( lines ) );
    }
    
    @Test
    public void baseTestGoWrongGibberish()
    {
        List<String>    lines       = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 30 ; ++inx )
        {
            StringBuilder   bldr    = new StringBuilder();
            IntStream.range( 0,  25 )
                .map( i -> (char)('a' + randy.nextInt( 26 ) ) )
                .forEach( bldr::append );
            lines.add( bldr.toString() );
        }
        // The result of parsing is unpredictable. The important thing
        // is that the scrambled lines don't offend the parser to the
        // point of a hard failure.
        assertDoesNotThrow( () -> new TextGridParser( lines ) );
    }
    
    @Test
    public void baseTestGoWrongMissingColHeader()
    {
        Predicate<String>   notColHeader    =
            s -> !sampleColHeaders.contains( s );
        List<String>        lines           = 
            sampleGrid.stream()
                .filter( notColHeader::test )
                .toList();
        // The result of parsing is unpredictable. The important thing
        // is that the scrambled lines don't offend the parser to the
        // point of a hard failure.
        assertDoesNotThrow( () -> new TextGridParser( lines ) );
    }
    
    @Test
    public void baseTestGoWrongMissingRowLabels()
    {
        Predicate<String>   notLabeledRow   =
            s -> !sampleLabeledRows.contains( s );
        List<String>        lines           = 
            sampleGrid.stream()
                .filter( notLabeledRow::test )
                .toList();
        // The result of parsing is unpredictable. The important thing
        // is that the scrambled lines don't offend the parser to the
        // point of a hard failure.
        assertDoesNotThrow( () -> new TextGridParser( lines ) );
    }
    
    @Test
    public void baseTestGoWrongMissingRows()
    {
        List<String>    lines   = new ArrayList<>();
        lines.add( CONFIG_GRID_START );
        lines.add( name );
        lines.addAll( sampleColHeaders );
        lines.add( boundaryLine );
        lines.add( boundaryLine );
        lines.add( CONFIG_GRID_END );
        // The result of parsing is unpredictable. The important thing
        // is that the scrambled lines don't offend the parser to the
        // point of a hard failure.
        assertDoesNotThrow( () -> new TextGridParser( lines ) );
    }
    
    private static void assertEmpty( TextGridParser parser )
    {
        assertTrue( parser.getColumnHeader().isEmpty() );
        assertTrue( parser.getGrid().isEmpty() );
        assertTrue( parser.getColumnHeader().isEmpty() );
        assertTrue( parser.getLabeledRows().isEmpty() );
        assertTrue( parser.getName().isEmpty() );
    }
}
