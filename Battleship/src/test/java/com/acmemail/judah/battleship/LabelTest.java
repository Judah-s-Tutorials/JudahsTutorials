package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.Constants.KEY_NUM_COLS;
import static com.acmemail.judah.battleship.Constants.KEY_NUM_ROWS;
import static com.acmemail.judah.battleship.Constants.NAME_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LabelTest
{
    private static final Class<BattleshipException> battleshipExc   =
        BattleshipException.class;
    private static final Class<NullPointerException> nullPointerExc  =  
        NullPointerException.class;
    
    @BeforeAll
    public static void beforeAll()
    {
        // ... Set up the number of rows so that alphabetic labels will be
        // right-justified in a 3-space field.
        // ... Set up the number of columns so that alphabetic labels 
        // will be right-justified in a 2-space field.
        final String    keyNumRows     = NAME_PREFIX + KEY_NUM_ROWS;
        final String    keyNumCols     = NAME_PREFIX + KEY_NUM_COLS;
        System.setProperty( keyNumCols, "110" );
        System.setProperty( keyNumRows, "40" );
    }
    
    @Test
    public void testParseRowColGoRight()
    {
        testParseRowColGoRight( "A,1", "A", "1" );
        testParseRowColGoRight( "A 1", "A", "1" );
        testParseRowColGoRight( "A  ,,  1", "A", "1" );
        testParseRowColGoRight( "A1", "A", "1" );
        testParseRowColGoRight( "ABC123", "ABC", "123" );
    }
    @Test
    public void testParseRowColGoWrong()
    {
        testParseRowColGoWrong( "A" );
        testParseRowColGoWrong( "ABC" );
        testParseRowColGoWrong( "1" );
        testParseRowColGoWrong( "123" );
        testParseRowColGoWrong( "A 1 1" );
        testParseRowColGoWrong( "" );
        
        assertThrows( nullPointerExc, () -> Label.parseRowCol( null ) );
    }

    @Test
    public void testIntToString()
    {
        int     oneDigitIndex       = 5;
        String  expOneDigitLabel    = "  " + (oneDigitIndex + 1);
        int     twoDigitIndex       = 55;
        String  expTwoDigitLabel    = " " + (twoDigitIndex + 1);
        int     threeDigitIndex     = 100;
        String  expThreeDigitLabel  = "" + (threeDigitIndex + 1);

        String  actOneDigitLabel    = Label.intToString( oneDigitIndex );
        assertEquals( expOneDigitLabel, actOneDigitLabel );

        String  actTwoDigitLabel    = Label.intToString( twoDigitIndex );
        assertEquals( expTwoDigitLabel, actTwoDigitLabel );

        String  actThreeDigitLabel    = Label.intToString( threeDigitIndex );
        assertEquals( expThreeDigitLabel, actThreeDigitLabel );
        
        assertThrows( battleshipExc, () -> Label.intToString( -1 ) );
    }

    @Test
    public void testColStrToInt()
    {
        int     actIndex0   = Label.colStrToInt( "1" );
        assertEquals( 0, actIndex0 );
        int     actIndex1   = Label.colStrToInt( "2" );
        assertEquals( 1, actIndex1 );
        int     actIndex25  = Label.colStrToInt( "26" );
        assertEquals( 25, actIndex25 );
        int     actIndex30  = Label.colStrToInt( "31" );
        assertEquals( 30, actIndex30 );
        
        assertThrows( nullPointerExc, () -> Label.colStrToInt( null ) );
        assertThrows( battleshipExc, () -> Label.colStrToInt( "" ) );
        assertThrows( battleshipExc, () -> Label.colStrToInt( "a" ) );
        assertThrows( battleshipExc, () -> Label.colStrToInt( "0" ) );
    }

    @Test
    public void testDecimalToAlpha()
    {
        String  actAlpha0   = Label.decimalToAlpha( 0 );
        assertEquals( " A", actAlpha0 );

        String  actAlpha25   = Label.decimalToAlpha( 25 );
        assertEquals( " Z", actAlpha25 );

        String  actAlpha26   = Label.decimalToAlpha( 26 );
        assertEquals( "AA", actAlpha26 );

        String  actAlpha30   = Label.decimalToAlpha( 30 );
        assertEquals( "AE", actAlpha30 );
        
        assertThrows( battleshipExc, () -> Label.decimalToAlpha( -1 ) );
    }

    @Test
    public void testAlphaToDecimal()
    {
        int index0  = Label.alphaToDecimal( "A" );
        assertEquals( 0, index0 );
        
        int index1  = Label.alphaToDecimal( "B" );
        assertEquals( 1, index1 );
        
        int index25 = Label.alphaToDecimal( "Z" );
        assertEquals( 25, index25 );
        
        int index26 = Label.alphaToDecimal( "AA" );
        assertEquals( 26, index26 );
        
        int index30 = Label.alphaToDecimal( "AE" );
        assertEquals( 30, index30 );
        
        assertThrows( battleshipExc, () -> Label.alphaToDecimal( "a" ) );
        assertThrows( battleshipExc, () -> Label.alphaToDecimal( "" ) );
        assertThrows( nullPointerExc, () -> Label.alphaToDecimal( null ) );
    }

    @Test
    public void testValidateRowStrGoRight()
    {
        testValidateRowStrGoRight( "1" );
        testValidateRowStrGoRight( "111" );
    }

    @Test
    public void testValidateRowStrGoWrong()
    {
        testValidateRowStrGoWrong( "A" );
        testValidateRowStrGoWrong( "" );
        testValidateRowStrGoWrong( null );
    }

    @Test
    public void testValidateColStrGoRight()
    {
        testValidateColStrGoRight( "1" );
        testValidateColStrGoRight( "111" );
    }

    @Test
    public void testValidateColStrGoWrong()
    {
        testValidateColStrGoWrong( "0" );
        testValidateColStrGoWrong( "A" );
        testValidateColStrGoWrong( "" );
        testValidateColStrGoWrong( null );
    }
    
    private static void 
    testParseRowColGoRight( String toParse, String expRow, String expCol )
    {
        String[]    result  = Label.parseRowCol( toParse );
        assertEquals( 2, result.length );
        assertEquals( expRow, result[0] );
        assertEquals( expCol, result[1] );
    }
    
    private static void testParseRowColGoWrong( String toParse )
    {
        String[]    strArr  = Label.parseRowCol( toParse );
        assertEquals( 2, strArr.length );
        assertTrue( strArr[0].isEmpty() || strArr[1].isEmpty() );
    }
    
    private static void testValidateColStrGoRight( String colStr )
    {
        List<String>    list    = Label.validateColStr( colStr );
        assertEquals( 0, list.size() );
    }
    
    private static void testValidateColStrGoWrong( String colStr )
    {
        List<String>    list    = Label.validateColStr( colStr );
        assertTrue( list.size() > 0 );
    }
    
    private static void testValidateRowStrGoRight( String RowStr )
    {
        List<String>    list    = Label.validateRowStr( RowStr );
        assertEquals( 0, list.size() );
    }
    
    private static void testValidateRowStrGoWrong( String RowStr )
    {
        List<String>    list    = Label.validateRowStr( RowStr );
        assertTrue( list.size() > 0 );
    }
}
