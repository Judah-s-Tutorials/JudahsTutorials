package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;

class LinePropertySetInitializerTest
{
    private final PropertyManager   pMgr    = PropertyManager.INSTANCE;
    
    @BeforeAll
    public static void beforeAll()
    {
        LinePropertySetInitializer.initProperties();
    }
    
    @Test
    void testTicMajorProperties()
    {
        assertPropertyEqualsBoolean(
            CPConstants.TIC_MAJOR_DRAW_PN,
            LinePropertySetInitializer.TIC_MAJOR_DRAW
        );
        assertPropertyEqualsFloat(
            CPConstants.TIC_MAJOR_WEIGHT_PN,
            LinePropertySetInitializer.TIC_MAJOR_WEIGHT
        );
        assertPropertyEqualsFloat(
            CPConstants.TIC_MAJOR_LEN_PN,
            LinePropertySetInitializer.TIC_MAJOR_LEN
        );
        assertPropertyEqualsFloat(
            CPConstants.TIC_MAJOR_MPU_PN,
            LinePropertySetInitializer.TIC_MAJOR_MPU
        );
        assertPropertyEqualsColor(
            CPConstants.TIC_MAJOR_COLOR_PN,
            LinePropertySetInitializer.TIC_MAJOR_COLOR
        );
    }
    
    @Test
    void testTicMinorProperties()
    {
        assertPropertyEqualsBoolean(
            CPConstants.TIC_MINOR_DRAW_PN,
            LinePropertySetInitializer.TIC_MINOR_DRAW
        );
        assertPropertyEqualsFloat(
            CPConstants.TIC_MINOR_WEIGHT_PN,
            LinePropertySetInitializer.TIC_MINOR_WEIGHT
        );
        assertPropertyEqualsFloat(
            CPConstants.TIC_MINOR_LEN_PN,
            LinePropertySetInitializer.TIC_MINOR_LEN
        );
        assertPropertyEqualsFloat(
            CPConstants.TIC_MINOR_MPU_PN,
            LinePropertySetInitializer.TIC_MINOR_MPU
        );
        assertPropertyEqualsColor(
            CPConstants.TIC_MINOR_COLOR_PN,
            LinePropertySetInitializer.TIC_MINOR_COLOR
        );
    }
    
    @Test
    void testGridlineProperties()
    {
        assertPropertyEqualsBoolean(
            CPConstants.GRID_LINE_DRAW_PN,
            LinePropertySetInitializer.GRID_LINE_DRAW
        );
        assertPropertyEqualsFloat(
            CPConstants.GRID_LINE_WEIGHT_PN,
            LinePropertySetInitializer.GRID_LINE_WEIGHT
        );
        // Note: grid lines don't have a length property
        assertPropertyEqualsFloat(
            CPConstants.GRID_LINE_LPU_PN,
            LinePropertySetInitializer.GRID_LINE_LPU
        );
        assertPropertyEqualsColor(
            CPConstants.GRID_LINE_COLOR_PN,
            LinePropertySetInitializer.GRID_LINE_COLOR
        );
    }
    
    @Test
    void testAxisProperties()
    {
        // Note: axes don't have a draw property
        assertPropertyEqualsFloat(
            CPConstants.AXIS_WEIGHT_PN,
            LinePropertySetInitializer.AXIS_WEIGHT
        );
        // Note: axes don't have a length property
        // Note: axes don't have a spacing property
        assertPropertyEqualsColor(
            CPConstants.AXIS_COLOR_PN,
            LinePropertySetInitializer.AXIS_COLOR
        );
    }
    
    private void 
    assertPropertyEqualsFloat( String propName, String expValStr )
    {
        float   actVal  = pMgr.asFloat( propName );
        float   expVal  = Float.parseFloat( expValStr );
        System.out.println(
            propName + ", " + expValStr + ", " + expVal + ", " + actVal );
        assertEquals( actVal, expVal );
    }
    
    private void 
    assertPropertyEqualsBoolean( String propName, String expValStr )
    {
        boolean actVal  = pMgr.asBoolean( propName );
        boolean expVal  = Boolean.parseBoolean( expValStr );
        System.out.println(
            propName + ", " + expValStr + ", " + expVal + ", " + actVal );
        assertEquals( actVal, expVal );
    }
    
    private void 
    assertPropertyEqualsColor( String propName, String expValStr )
    {
        Color   actVal      = pMgr.asColor( propName );
        int     actIntVal   = actVal.getRGB() & 0xFFFFFF;
        int     expIntVal   = Integer.decode( expValStr );
        System.out.println(
            propName + ", " + expValStr + ", " + expIntVal + ", " + actIntVal );
        assertEquals( actIntVal, expIntVal );
    }
}
