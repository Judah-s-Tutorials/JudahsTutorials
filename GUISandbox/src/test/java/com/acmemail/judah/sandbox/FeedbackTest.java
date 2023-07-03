package com.acmemail.judah.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class FeedbackTest
{

    @Test
    void testGetValue()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetColor()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetTextColor()
    {
        testGetTextColor( Color.RED, "0xff0000" );
        testGetTextColor( Color.GREEN, "0x00ff00" );
        testGetTextColor( Color.BLUE, "0x0000ff" );
        testGetTextColor( Color.WHITE, "0xffffff" );
        testGetTextColor( Color.BLACK, "0x000000" );
    }
    
    private void testGetTextColor( Color color, String expText )
    {
        String  expTextConverted    = expText.toUpperCase();
        String  actText             = Feedback.getText( color );
        String  actTextConverted    = actText.toUpperCase();
        System.out.println( actTextConverted );
        assertEquals( expTextConverted, actTextConverted );
    }

    @Test
    void testShowError()
    {
        fail("Not yet implemented");
    }

}
