package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;

import javax.swing.JRadioButton;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This is no a thorough test
 * of LinePropertySet.
 * It just picks up
 * a few lines of coverage
 * that are missed
 * by subclass tests
 * such as LinePropertySetGridLinesTest.
 * 
 * @author Jack Straub
 */
public class LinePropertySetTest
{

    @Test
    void test()
    {
        LinePropertySet set = new LinePropertySetMisc();
        
        // No properties should be present in the test set
        assertPresent( set );
        // Every property should have a default value
        assertHasDefaultValues( set );
        // Attempt to set all properties, then verify that
        // properties still have default values.
        setNewValues( set );
        assertHasDefaultValues( set );
        
        // This changes nothing, but does pick up a couple 
        // of lines of coverage.
        set.apply();
    }
    
    @Test
    public void negativeTesting()
    {
        // Verify that LinePropertySet.getPropertySet
        // fails when it's supposed to.
        Class<ComponentException>   clazz   = ComponentException.class;
        JRadioButton    button  = new JRadioButton();
        assertThrows( clazz, () -> LinePropertySet.getPropertySet( button ) );
        
        button.putClientProperty( LinePropertySet.TYPE_KEY, new Object() );
        assertThrows( clazz, () -> LinePropertySet.getPropertySet( button ) );
    }
    
    private void assertPresent( LinePropertySet set )
    {
        assertFalse( set.hasDraw() );
        assertFalse( set.hasColor() );
        assertFalse( set.hasStroke() );
        assertFalse( set.hasLength() );
        assertFalse( set.hasSpacing() );
    }
    
    private void assertHasDefaultValues( LinePropertySet set )
    {
        assertNull( set.getColor() );
        assertEquals( -1, set.getStroke() );
        assertEquals( -1, set.getLength() );
        assertEquals( -1, set.getSpacing() );
        assertFalse( set.getDraw() );
    }
    
    private void setNewValues( LinePropertySet set )
    {
        set.setColor( Color.BLUE );
        set.setStroke( 5 );
        set.setLength( 5 );
        set.setSpacing( 5 );
        set.setDraw( true );
    }

    private static class LinePropertySetMisc extends LinePropertySet
    {
        LinePropertySetMisc()
        {
            super( "", "", "", "", "" );
        }
    }
}
