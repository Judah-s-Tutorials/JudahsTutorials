package com.acmemail.judah.cartesian_plane.components;

import static com.acmemail.judah.cartesian_plane.CPConstants.AXIS_COLOR_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.AXIS_WEIGHT_PN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import javax.swing.JRadioButton;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;

class LinePropertySetAxesTest
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    
    private static final int        ORIG_AXIS_COLOR = 
        getInt( LinePropertySetInitializer.AXIS_COLOR );
    private static final float      ORIG_AXIS_WEIGHT = 
        getFloat( LinePropertySetInitializer.AXIS_WEIGHT );
    // Note: axes don't have a length property
    // Note: axes don't have a spacing property
    // Note: axes don't have a draw property    
    private static final int        NEW_AXIS_COLOR = 
        2 * ORIG_AXIS_COLOR;
    private static final float      NEW_AXIS_WEIGHT = 
        2 * ORIG_AXIS_WEIGHT;
    // Note: axes don't have a length property
    // Note: axes don't have a spacing property
    // Note: axes don't have a draw property
    
    @BeforeAll
    public static void beforeAll()
    {
        // Initialize the property manager
        LinePropertySetInitializer.initProperties();
    }

    @Test
    void test()
    {
        // instantiate line property set
        LinePropertySet set = new LinePropertySetAxes();
        
        // verify that all properties are present in the set
        assertPresent( set );
        
        // verify that the property manager has the expected values
        assertPMgrHasOrigValues();
        
        // verify that a new line-property set has the expected values
        assertSetHasOrigValues( set );
        
        // change all the values in the set
        setNewValues( set );
        
        // verify that all the values in the set were correctly changed
        assertSetHasNewValues( set );
        
        // reset all fields in the set to their original values,
        // and verify that the fields are correctly set
        set.reset();
        assertSetHasOrigValues( set );
        
        // give all fields in the set new values...
        // verify that the new values are correctly set...
        // apply the new values...
        // verify that the property manager records the new values
        setNewValues( set );
        assertSetHasNewValues( set );
        set.apply();
        assertPMgrHasNewValues();
        
        // test the component registry logic
        testComponentRegistry( set );
    }
    
    private void assertPresent( LinePropertySet set )
    {
        assertFalse( set.hasDraw() );
        assertTrue( set.hasColor() );
        assertTrue( set.hasStroke() );
        assertFalse( set.hasLength() );
        assertFalse( set.hasSpacing() );
    }
    
    private void assertPMgrHasOrigValues()
    {
        int actColor    = 
            pMgr.asColor( AXIS_COLOR_PN ).getRGB() & 0xFFFFFF;
        assertEquals( ORIG_AXIS_COLOR, actColor );
        assertEquals(
            pMgr.asFloat( AXIS_WEIGHT_PN ), 
            ORIG_AXIS_WEIGHT
        );
        // Note: axes don't have a length property
        // Note: axes don't have a spacing property
        // Note: axes don't have a draw property
    }
    
    private void assertSetHasOrigValues( LinePropertySet set )
    {
        int actColor    = set.getColor().getRGB() & 0xFFFFFF;
        assertEquals( ORIG_AXIS_COLOR, actColor );
        assertEquals( ORIG_AXIS_WEIGHT, set.getStroke() );
        // Note: axes don't have a length property
        assertEquals( -1, set.getLength() );
        // Note: axes don't have a spacing property
        assertEquals( -1, set.getSpacing() );
        // Note: axes don't have a draw property
        assertFalse( set.getDraw() );
    }
    
    private void assertPMgrHasNewValues()
    {
        int actColor    = 
            pMgr.asColor( AXIS_COLOR_PN ).getRGB() & 0xFFFFFF;
        assertEquals( NEW_AXIS_COLOR, actColor );
        assertEquals(
            pMgr.asFloat( AXIS_WEIGHT_PN ), 
            NEW_AXIS_WEIGHT
        );
        // Note: axes don't have a length property
        // Note: axes don't have a spacing property
        // Note: axes don't have a draw property
    }
    
    private void assertSetHasNewValues( LinePropertySet set )
    {
        int actColor    = set.getColor().getRGB() & 0xFFFFFF;
        assertEquals( NEW_AXIS_COLOR, actColor );
        assertEquals( NEW_AXIS_WEIGHT, set.getStroke() );
        // Note: axes don't have a length property
        assertEquals( -1, set.getLength() );
        // Note: axes don't have a spacing property
        assertEquals( -1, set.getSpacing() );
        // Note: axes don't have a draw property
        assertFalse( set.getDraw() );
    }
    
    private void setNewValues( LinePropertySet set )
    {
        set.setColor( new Color( NEW_AXIS_COLOR ) );
        set.setStroke( NEW_AXIS_WEIGHT );
        // Note: axes don't have a length property
        set.setLength( 5 );
        // Note: axes don't have a spacing property
        set.setSpacing( 5 );
        // Note: axes don't have a draw property
        set.setDraw( false );
    }
    
    private void testComponentRegistry( LinePropertySet set )
    {
        // I really don't think this needs to be executed on the
        // event processing thread, but let's be thorough
        GUIUtils.schedEDTAndWait( () -> {
            JRadioButton    comp    = new JRadioButton();
            LinePropertySet.putPropertySet( comp, set );
            LinePropertySet actSet  = LinePropertySet.getPropertySet( comp );
            assertEquals( set, actSet );
        });
    }

    private static int getInt( String strVal )
    {
        int intVal  = Integer.decode( strVal );
        return intVal;
    }

    private static float getFloat( String strVal )
    {
        float   floatVal  = Float.parseFloat( strVal );
        return floatVal;
    }
}
