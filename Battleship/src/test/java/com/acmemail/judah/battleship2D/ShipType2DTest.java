package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

class ShipType2DTest
{
    @Test
    public void testCompactConstructor()
    {
        final String    testTypeName    = "test type name";
        final int       testLength      = 10;
        final int       testBreadth     = 5;
        final int       iType           = BufferedImage.TYPE_INT_RGB;
        final Image     testImage       = new BufferedImage( 10, 10, iType );
        
        ShipType2D  type    = 
            new ShipType2D( testTypeName, testLength, testBreadth, testImage );
        assertEquals( testTypeName, type.typeName() );
        assertEquals( testLength, type.length() );
        assertEquals( testBreadth, type.breadth() );
        assertEquals( testImage, type.image() );
        
        type = new ShipType2D( testTypeName, testLength, testBreadth, null );
        assertEquals( testTypeName, type.typeName() );
        assertEquals( testLength, type.length() );
        assertEquals( testBreadth, type.breadth() );
        assertNull( type.image() );
        
        assertThrows( NullPointerException.class, () -> 
            new ShipType2D( null, testLength, testBreadth, null )
        );
    }
}
