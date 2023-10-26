package com.acmemail.judah.cartesian_plane.components;

import static com.acmemail.judah.cartesian_plane.CPConstants.GRID_LINE_COLOR_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.GRID_LINE_DRAW_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.GRID_LINE_LPU_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.GRID_LINE_WEIGHT_PN;
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

class LinePropertySetGridLinesTest
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    
    private static final int        ORIG_GRID_LINE_COLOR = 
        getInt( LinePropertySetInitializer.GRID_LINE_COLOR );
    private static final float      ORIG_GRID_LINE_WEIGHT = 
        getFloat( LinePropertySetInitializer.GRID_LINE_WEIGHT );
    // Note: grid lines don't have a length property
    private static final float      ORIG_GRID_LINE_LPU =
        getFloat( LinePropertySetInitializer.GRID_LINE_LPU );
    private static final boolean    ORIG_GRID_LINE_DRAW =
        getBoolean( LinePropertySetInitializer.GRID_LINE_DRAW );
    
    private static final int        NEW_GRID_LINE_COLOR = 
        2 * ORIG_GRID_LINE_COLOR;
    private static final float      NEW_GRID_LINE_WEIGHT = 
        2 * ORIG_GRID_LINE_WEIGHT;
    // Note: grid lines don't have a length property
    private static final float      NEW_GRID_LINE_LPU =
        2 * ORIG_GRID_LINE_LPU;
    private static final boolean    NEW_GRID_LINE_DRAW =
        !ORIG_GRID_LINE_DRAW;
    
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
        LinePropertySet set = new LinePropertySetGridLines();
        
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
        assertTrue( set.hasDraw() );
        assertTrue( set.hasColor() );
        assertTrue( set.hasStroke() );
        assertFalse( set.hasLength() );
        assertTrue( set.hasSpacing() );
    }
    
    private void assertPMgrHasOrigValues()
    {
        int actColor    = 
            pMgr.asColor( GRID_LINE_COLOR_PN ).getRGB() & 0xFFFFFF;
        assertEquals( ORIG_GRID_LINE_COLOR, actColor );
        assertEquals(
            pMgr.asFloat( GRID_LINE_WEIGHT_PN ), 
            ORIG_GRID_LINE_WEIGHT
        );
        // Note: grid lines don't have a length property
        assertEquals(
            pMgr.asFloat( GRID_LINE_LPU_PN ), 
            ORIG_GRID_LINE_LPU
        );
        assertEquals(
            pMgr.asBoolean( GRID_LINE_DRAW_PN ), 
            ORIG_GRID_LINE_DRAW
        );
    }
    
    private void assertSetHasOrigValues( LinePropertySet set )
    {
        int actColor    = set.getColor().getRGB() & 0xFFFFFF;
        assertEquals( ORIG_GRID_LINE_COLOR, actColor );
        assertEquals( ORIG_GRID_LINE_WEIGHT, set.getStroke() );
        // Note: grid lines don't have a length property
        assertEquals( -1, set.getLength() );
        assertEquals( ORIG_GRID_LINE_LPU, set.getSpacing() );
        assertEquals( ORIG_GRID_LINE_DRAW, set.getDraw() );
    }
    
    private void assertPMgrHasNewValues()
    {
        int actColor    = 
            pMgr.asColor( GRID_LINE_COLOR_PN ).getRGB() & 0xFFFFFF;
        assertEquals( NEW_GRID_LINE_COLOR, actColor );
        assertEquals(
            pMgr.asFloat( GRID_LINE_WEIGHT_PN ), 
            NEW_GRID_LINE_WEIGHT
        );
        // Note: grid lines don't have a length property
        assertEquals(
            pMgr.asFloat( GRID_LINE_LPU_PN ), 
            NEW_GRID_LINE_LPU
        );
        assertEquals(
            pMgr.asBoolean( GRID_LINE_DRAW_PN ), 
            NEW_GRID_LINE_DRAW
        );
    }
    
    private void assertSetHasNewValues( LinePropertySet set )
    {
        int actColor    = set.getColor().getRGB() & 0xFFFFFF;
        assertEquals( NEW_GRID_LINE_COLOR, actColor );
        assertEquals( NEW_GRID_LINE_WEIGHT, set.getStroke() );
        // Note: grid lines don't have a length property
        assertEquals( -1, set.getLength() );
        assertEquals( NEW_GRID_LINE_LPU, set.getSpacing() );
        assertEquals( NEW_GRID_LINE_DRAW, set.getDraw() );
    }
    
    private void setNewValues( LinePropertySet set )
    {
        set.setColor( new Color( NEW_GRID_LINE_COLOR ) );
        set.setStroke( NEW_GRID_LINE_WEIGHT );
        // Note: grid lines don't have a length property
        set.setLength( 5 );
        set.setSpacing( NEW_GRID_LINE_LPU );
        set.setDraw( NEW_GRID_LINE_DRAW );
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

    private static boolean getBoolean( String strVal )
    {
        boolean booleanVal  = Boolean.parseBoolean( strVal );
        return booleanVal;
    }

    private static float getFloat( String strVal )
    {
        float   floatVal  = Float.parseFloat( strVal );
        return floatVal;
    }
}
