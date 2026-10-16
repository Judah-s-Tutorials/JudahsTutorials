package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_END;
import static com.acmemail.judah.battleship.StatusMessages.CONFIG_GRID_START;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    public void testEmpty()
    {
        TextGridParser  parser = new TextGridParser( Collections.emptyList() );
        assertEmpty( parser );
    }
    
    @Test
    public void baseTest()
    {
        List<String>    colHeaders  = 
            List.of( 
                "                   1 1 1",
                " 1 2 3 4 5 6 7 8 9 0 1 2"
            );
        List<String>    labeledRows =
            List.of(
                " A-------",
                " B-------",
                " C-------",
                " D-------",
                " E-------",
                " F-------"
            );
        List<String>    expGrid     = new ArrayList<>();
        expGrid.add( name );
        expGrid.addAll( colHeaders );
        expGrid.add( boundaryLine );
        labeledRows.forEach( s -> 
            expGrid.addAll( List.of( emptyRowLine, s, emptyRowLine ) )
        );
        expGrid.add( boundaryLine );
        
        List<String>    testLines   = new ArrayList<>();
        testLines.addAll( ignoreLines );
        testLines.add( CONFIG_GRID_START );
        testLines.addAll( expGrid );
        testLines.add( CONFIG_GRID_END );
        testLines.addAll( ignoreLines );
        
        TextGridParser  parser  = new TextGridParser( testLines );
        assertEquals( name, parser.getName() );
        assertEquals( colHeaders, parser.getColumnHeader() );
        assertEquals( labeledRows, parser.getLabeledRows() );
        assertEquals( expGrid, parser.getGrid() );
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
