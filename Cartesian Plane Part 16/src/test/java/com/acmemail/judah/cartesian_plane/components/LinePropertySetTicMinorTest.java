package com.acmemail.judah.cartesian_plane.components;

import static com.acmemail.judah.cartesian_plane.CPConstants.TIC_MINOR_COLOR_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.TIC_MINOR_DRAW_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.TIC_MINOR_LEN_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.TIC_MINOR_MPU_PN;
import static com.acmemail.judah.cartesian_plane.CPConstants.TIC_MINOR_WEIGHT_PN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import javax.swing.JRadioButton;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.LinePropertySetInitializer;

class LinePropertySetTicMinorTest
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    
    private static final int        ORIG_TIC_MINOR_COLOR = 
        getInt( LinePropertySetInitializer.TIC_MINOR_COLOR );
    private static final float      ORIG_TIC_MINOR_WEIGHT = 
        getFloat( LinePropertySetInitializer.TIC_MINOR_WEIGHT );
    private static final float      ORIG_TIC_MINOR_LEN =
        getFloat( LinePropertySetInitializer.TIC_MINOR_LEN );
    private static final float      ORIG_TIC_MINOR_MPU =
        getFloat( LinePropertySetInitializer.TIC_MINOR_MPU );
    private static final boolean    ORIG_TIC_MINOR_DRAW =
        getBoolean( LinePropertySetInitializer.TIC_MINOR_DRAW );
    
    private static final int        NEW_TIC_MINOR_COLOR = 
        2 * ORIG_TIC_MINOR_COLOR;
    private static final float      NEW_TIC_MINOR_WEIGHT = 
        2 * ORIG_TIC_MINOR_WEIGHT;
    private static final float      NEW_TIC_MINOR_LEN =
        2 * ORIG_TIC_MINOR_LEN;
    private static final float      NEW_TIC_MINOR_MPU =
        2 * ORIG_TIC_MINOR_MPU;
    private static final boolean    NEW_TIC_MINOR_DRAW =
        !ORIG_TIC_MINOR_DRAW;
    
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
        LinePropertySet set = new LinePropertySetTicMinor();
        
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
        assertTrue( set.hasLength() );
        assertTrue( set.hasSpacing() );
    }
    
    private void assertPMgrHasOrigValues()
    {
        int actColor    = 
            pMgr.asColor( TIC_MINOR_COLOR_PN ).getRGB() & 0xFFFFFF;
        assertEquals( ORIG_TIC_MINOR_COLOR, actColor );
        assertEquals(
            pMgr.asFloat( TIC_MINOR_WEIGHT_PN ), 
            ORIG_TIC_MINOR_WEIGHT
        );
        assertEquals(
            pMgr.asFloat( TIC_MINOR_LEN_PN ), 
            ORIG_TIC_MINOR_LEN
        );
        assertEquals(
            pMgr.asFloat( TIC_MINOR_MPU_PN ), 
            ORIG_TIC_MINOR_MPU
        );
        assertEquals(
            pMgr.asBoolean( TIC_MINOR_DRAW_PN ), 
            ORIG_TIC_MINOR_DRAW
        );
    }
    
    private void assertSetHasOrigValues( LinePropertySet set )
    {
        int actColor    = set.getColor().getRGB() & 0xFFFFFF;
        assertEquals( ORIG_TIC_MINOR_COLOR, actColor );
        assertEquals( ORIG_TIC_MINOR_WEIGHT, set.getStroke() );
        assertEquals( ORIG_TIC_MINOR_LEN, set.getLength() );
        assertEquals( ORIG_TIC_MINOR_MPU, set.getSpacing() );
        assertEquals( ORIG_TIC_MINOR_DRAW, set.getDraw() );
    }
    
    private void assertPMgrHasNewValues()
    {
        int actColor    = 
            pMgr.asColor( TIC_MINOR_COLOR_PN ).getRGB() & 0xFFFFFF;
        assertEquals( NEW_TIC_MINOR_COLOR, actColor );
        assertEquals(
            pMgr.asFloat( TIC_MINOR_WEIGHT_PN ), 
            NEW_TIC_MINOR_WEIGHT
        );
        assertEquals(
            pMgr.asFloat( TIC_MINOR_LEN_PN ), 
            NEW_TIC_MINOR_LEN
        );
        assertEquals(
            pMgr.asFloat( TIC_MINOR_MPU_PN ), 
            NEW_TIC_MINOR_MPU
        );
        assertEquals(
            pMgr.asBoolean( TIC_MINOR_DRAW_PN ), 
            NEW_TIC_MINOR_DRAW
        );
    }
    
    private void assertSetHasNewValues( LinePropertySet set )
    {
        int actColor    = set.getColor().getRGB() & 0xFFFFFF;
        assertEquals( NEW_TIC_MINOR_COLOR, actColor );
        assertEquals( NEW_TIC_MINOR_WEIGHT, set.getStroke() );
        assertEquals( NEW_TIC_MINOR_LEN, set.getLength() );
        assertEquals( NEW_TIC_MINOR_MPU, set.getSpacing() );
        assertEquals( NEW_TIC_MINOR_DRAW, set.getDraw() );
    }
    
    private void setNewValues( LinePropertySet set )
    {
        set.setColor( new Color( NEW_TIC_MINOR_COLOR ) );
        set.setStroke( NEW_TIC_MINOR_WEIGHT );
        set.setLength( NEW_TIC_MINOR_LEN );
        set.setSpacing( NEW_TIC_MINOR_MPU );
        set.setDraw( NEW_TIC_MINOR_DRAW );
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
