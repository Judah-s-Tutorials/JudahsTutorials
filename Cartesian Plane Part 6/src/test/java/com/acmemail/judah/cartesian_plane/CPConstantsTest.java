package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.*;

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
        String  hexString   = Integer.toHexString( 0xff00ff );
    }

    @Test
    void testAsFontStyle()
    {
        fail("Not yet implemented");
    }

}
