package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PRadioButtonTest
{
    @Test
    void testPRadiobuttonTestT()
    {
        String                  prop    = "Property";
        PRadioButton<String>    button  = new PRadioButton<>( prop );
        assertEquals( prop, button.get() );
    }

    @Test
    void testPRadiobuttonTestTString()
    {
        String                  prop    = "Property";
        String                  text    = "Label";
        PRadioButton<String>    button  = new PRadioButton<>( prop, text );
        assertEquals( prop, button.get() );
        assertEquals( text, button.getText() );
    }
}
