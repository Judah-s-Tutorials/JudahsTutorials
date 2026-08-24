package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship2D.GridCoords;

class LabelTest
{
    private static final TestData[] goRightData =
    {
        new TestData( "A10", "A10", "A", 0, "10", 9 ),
        new TestData( "A   10", "A10", "A", 0, "10", 9 ),
        new TestData( "A,10", "A10", "A", 0, "10", 9 ),
        new TestData( "A , 10", "A10", "A", 0, "10", 9 ),
        new TestData( " \tA \t, 10  ", "A10", "A", 0, "10", 9 ),
        new TestData( "Z100", "Z100", "Z", 25, "100", 99 ),
        new TestData( "AA100", "AA100", "AA", 26, "100", 99 ),
        new TestData( "AB100", "AB100", "AB", 27, "100", 99 ),
        new TestData( "AZ100", "AZ100", "AZ", 51, "100", 99 ),
    };
    
    private static final String[]   goWrongData =
    { 
        "10",    "A",      "A z 10", "A 9 10", "A A10", 
        "A10 1", "A A 10", "A 10 1", "A%10",   "%A10",
        "AAAAAA10", "A9999999999", "A0"
    };

    @Test
    public void testLabelGridCoordsGoRight()
    {
        for ( TestData data : goRightData )
            confirmGridCoords( data );
    }

    @Test
    public void testLabelGridCoordsGoWrong()
    {
        GridCoords  coords  = new GridCoords( -1, 0 );
        Label       label   = new Label( coords );
        assertFalse( label.isStatus() );
        assertEquals( StatusMessages.INVALID_COL, label.getMessage() );
        
        coords  = new GridCoords( 0, -1 );
        label   = new Label( coords );
        assertFalse( label.isStatus() );
        assertEquals( StatusMessages.INVALID_ROW, label.getMessage() );
        
        coords  = new GridCoords( -1, -1 );
        label   = new Label( coords );
        assertFalse( label.isStatus() );
    }

    @Test
    public void testLabelGridCoordsNPE()
    {
        assertThrows( 
            NullPointerException.class, 
            () -> new Label( (GridCoords)null )
        );
    }

    @Test
    public void testLabelIntIntGoRight()
    {
        for ( TestData data : goRightData )
            confirmIntInt( data );
    }

    @Test
    public void testLabelIntIntGoWrong()
    {
        Label   label   = new Label( -1, 0 );
        assertFalse( label.isStatus() );
        assertEquals( StatusMessages.INVALID_COL, label.getMessage() );
        
        label   = new Label( 0, -1 );
        assertFalse( label.isStatus() );
        assertEquals( StatusMessages.INVALID_ROW, label.getMessage() );
        
        label   = new Label( -1, -1 );
        assertFalse( label.isStatus() );
    }

    @Test
    public void testLabelStringGoRight()
    {
        for ( TestData data : goRightData )
            confirmString( data );
        
        // The original test data uses capital letters for row IDs;
        // try them again with lower-case letters.
        for ( TestData data : goRightData )
        {
            String      input       = data.input().toLowerCase( Locale.ROOT );
            TestData    lowerData   =
                new TestData(
                    input,
                    data.label(),
                    data.rowStr(),
                    data.yco(),
                    data.colStr(),
                    data.xco()
                );
            confirmString( lowerData);
        }
        
        // Specific tests for row and column string lengths.
        Label   label   = new Label( "AAAAA10" );
        assertTrue( label.isStatus() );
        label   = new Label( "A123456789" );
        assertTrue( label.isStatus() );

    }
    
    @Test
    public void testLabelStringGoWrong()
    {
        for ( String input : goWrongData )
        {
            Label   label   = new Label( input );
            assertFalse( label.isStatus() );
            assertEquals( StatusMessages.PARSE_FAILED, label.getMessage() );
        }
    }

    @Test
    public void testLabelStringTurkishLocale()
    {
        Locale  origLocale  = Locale.getDefault();
        try
        {
            Locale.setDefault( new Locale( "tr", "TR" ) );
            Label   label   = new Label( "i10" );
            assertTrue( label.isStatus() );
            assertEquals( "I", label.getRowStr() );
        }
        finally
        {
            Locale.setDefault( origLocale );
        }
    }

    @Test
    public void testLabelStringNPE()
    {
        assertThrows( 
            NullPointerException.class, 
            () -> new Label( (String)null )
        );
    }
    
    /**
     * Given test data that is assumed sound,
     * create a Label from the encapsulated input
     * and confirm the label was created correctly.
     * 
     * @param data  the given test data
     */
    private static void confirmString( TestData data )
    {
        Label   label       = new Label( data.input() );
        confirm( data, label );
    }
    
    /**
     * Given test data that is assumed sound,
     * create a Label from the encapsulated x- and y-coordinates
     * and confirm the label was created correctly.
     * 
     * @param data  the given test data
     */
    private static void confirmIntInt( TestData data )
    {
        Label   label       = new Label( data.xco(), data.yco() );
        confirm( data, label );
    }
        
    /**
     * Given test data that is assumed sound,
     * create a GridCoords object
     * from the encapsulated x- and y-coordinates;
     * use the GridCoords object to instantiate a label
     * and confirm the label was created correctly.
     * 
     * @param data  the given test data
     */
    private static void confirmGridCoords( TestData data )
    {
        GridCoords  coords  = new GridCoords( data.xco(), data.yco() );
        Label       label   = new Label( coords );
        confirm( data, label );
    }
    
    /**
     * Given test data containing expected values,
     * and a label containing actual values,
     * confirm that the actual values are equal
     * to the expected values.
     * 
     * @param data  the given test data
     * @param label the given label
     */
    private static void confirm( TestData data, Label label )
    {
        assertEquals( data.label(), label.getLabel(), "label" );
        assertEquals( data.rowStr(), label.getRowStr(), "rowStr" );
        assertEquals( data.yco(), label.getYco(), "yco" );
        assertEquals( data.colStr(), label.getColStr(), "colStr" );
        assertEquals( data.xco(), label.getXco(), "xco" );
        GridCoords  coords  = new GridCoords( data.xco(), data.yco() );
        assertEquals( coords, label.getGridCoords() );
        assertTrue( label.isStatus() );
        
        String  str = label.toString();
        String  expCoords   = "(" + data.xco() + "," + data.yco();
        assertTrue( str.contains( data.label() ) );
        assertTrue( str.contains( expCoords ) );
    }

    /**
     * Encapsulate the data expected
     * to be contained in a Label object.
     * 
     * @param   input   the input to the Label(String) constructor
     * @param   label   the expected value of the label field
     * @param   rowStr  
     *      the expected value of the 1-origin, alphabetic row identifier 
     * @param   yco     
     *      the expected value of the 0-origin, numeric row identifier 
     * @param   colStr  
     *      the expected value of the 1-origin, alphanumeric column identifier 
     * @param   xco     
     *      the expected value of the 0-origin, numeric column identifier 
     */
    private record TestData(
        String  input,
        String  label,
        String  rowStr,
        int     yco,
        String  colStr,
        int     xco
    )
    {
        
    }
}
