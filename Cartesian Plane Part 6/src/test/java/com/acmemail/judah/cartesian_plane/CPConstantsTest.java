package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;
import java.awt.Font;

import org.junit.jupiter.api.Test;

class CPConstantsTest
{
    @Test
    void testAsInt()
    {
        int[]   testVals    = { -5, -1, 0, 1, 5 };
        for ( int val : testVals )
        {
            int actVal  = CPConstants.asInt( "" + val );
            assertEquals( val, actVal );
        }
        
        // Go-wrong path: verify that NumberFormatException is thrown
        // if a non-numeric string is passed.
        Class<NumberFormatException>    clazz   = NumberFormatException.class;
        assertThrows( clazz, () -> CPConstants.asInt( "five" ) );
    }

    @Test
    void testAsFloat()
    {
        float[] testVals    = { -5.1f, -1.1f, 0, 1.1f, 5.1f };
        for ( float val : testVals )
        {
            float   actVal  = CPConstants.asFloat( "" + val );
            assertEquals( val, actVal, .001 );
        }
        
        // Go-wrong path: verify that NumberFormatException is thrown
        // if a non-numeric string is passed.
        Class<NumberFormatException>    clazz   = NumberFormatException.class;
        assertThrows( clazz, () -> CPConstants.asInt( "five" ) );
    }

    @Test
    void testAsBoolean()
    {
        String[]    strVals = 
            { "True", "true", "TRUE", "False", "false", "FALSE" };
        boolean[]   expVals =
            {  true,   true,   true,   false,   false,   false  };
        for ( int inx = 0 ; inx < strVals.length ; ++inx )
        {
            boolean actVal  = CPConstants.asBoolean( strVals[inx] );
            boolean expVal  = expVals[inx];
            assertEquals( actVal, expVal );
        }
    }

    @Test
    void testAsColor()
    {
        int[]       iVals   =
        {
            0xff00ff,
            0x00cc00,
            0x0e0e0e
        };
        for ( int iVal : iVals )
        {
            String  strVal1 = "0x" + Integer.toHexString( iVal );
            String  strVal2 = "#" + Integer.toHexString( iVal );
            String  strVal3 = "" + iVal;
            Color   actVal1 = CPConstants.asColor( strVal1 );
            Color   actVal2 = CPConstants.asColor( strVal2 );
            Color   actVal3 = CPConstants.asColor( strVal3 );
            
            // Compare the original integer value to the value of the
            // Color expressed as an int. The Color value includes
            // the alpha component (bits 28-31, 0xFF000000) which must
            // be turned off before performing the comparison
            testColorAsInt( iVal, actVal1 );
            testColorAsInt( iVal, actVal2 );
            testColorAsInt( iVal, actVal3 );
        }
    }

    @Test
    void testAsFontStyle()
    {
        int pVal    = CPConstants.asFontStyle( "PLAIN" );
        assertEquals( Font.PLAIN, pVal );
        int bVal    = CPConstants.asFontStyle( "BOLD" );
        assertEquals( Font.BOLD, bVal );
        int iVal    = CPConstants.asFontStyle( "ITALIC" );
        assertEquals( Font.ITALIC, iVal );
        
        // Test go-wrong path
        Class<IllegalArgumentException> clazz   =
            IllegalArgumentException.class;
        assertThrows( clazz, () -> CPConstants.asFontStyle( "INVALID" ) );
    }
    
    /**
     * "Test" default constructor just to get test coverage.
     */
    @Test
    public void CPConstantsTest()
    {
        new CPConstants();
    }

    /**
     * Compares an integer value to a Color converted to an integer.
     * The alpha bits in the Color value are suppressed
     * prior to performing the comparison.
     * 
     * @param expVal    given integer value
     * @param color     given Color value
     */
    private static void testColorAsInt( int expVal, Color color )
    {
        int rgb     = color.getRGB();
        int actVal  = rgb & ~0xFF000000;
        assertEquals( actVal, expVal );
    }
}
