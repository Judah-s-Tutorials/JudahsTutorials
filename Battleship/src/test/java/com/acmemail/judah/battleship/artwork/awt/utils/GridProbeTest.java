package com.acmemail.judah.battleship.artwork.awt.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.artwork.awt.GridWindow;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.Grid2DTestSupport;

class GridProbeTest
{
    private GridProbeTestWindow defTestWindow;
    private GridProbe           defGridProbe;
    
    @BeforeEach
    public void beforeEach()
    {
        Grid2DTestSupport.reset();
        Grid2DTestSupport.reinitDimensions( "10", "15" );
        defTestWindow = GridProbeTestWindow.getRealizedTestWindow( null );
        defGridProbe = GridProbe.getProbe( defTestWindow );
    }
    
    @AfterEach
    public void afterEach()
    {
        if ( defTestWindow != null )
            defTestWindow.dispose();
        if ( defGridProbe != null )
            defGridProbe.dispose();
    }
    
    @AfterAll
    static void afterAll() throws Exception
    {
        Grid2DTestSupport.reset();
    }

    @Test
    public void testGetProbe()
    {
        GridProbe   probe           = GridProbe.getProbe();
        GridWindow  window          = probe.getGridWindow();
        assertNotNull( window );
        Grid2D      logicalGridA    = probe.getGrid();
        Grid2D      logicalGridB    = window.getGrid();
        assertEquals( Constants.HOME_GRID, logicalGridA.getName() );
        assertEquals( Constants.HOME_GRID, logicalGridB.getName() );
        
        probe.dispose();
        Window      appWndow        = getAppRoot( window );
        assertFalse( appWndow.isDisplayable() );
    }

    @Test
    public void testGetProbeGridWindowGrid2DNonNull()
    {
        String      gridName    = "Test Grid Name"; 
        Grid2D      expGrid     = new Grid2D( gridName );
        GridWindow  expWindow   = 
            GridProbeTestWindow.getRealizedTestWindow( expGrid );
        GridProbe   probe       = GridProbe.getProbe( expWindow );
        GridWindow  actWindow   = probe.getGridWindow();
        Grid2D      actGrid     = actWindow.getGrid();
        assertSame( expWindow, actWindow );
        assertSame( expGrid, actGrid );
        
        Window      window      = getAppRoot( expWindow );
        // sanity check
        assertTrue( window.isDisplayable() );
        probe.dispose();
        assertFalse( window.isDisplayable() );
    }

    @Test
    public void testGetGridWindow()
    {
        GridWindow  window  = defGridProbe.getGridWindow();
        assertTrue( window instanceof GridProbeTestWindow );
        assertSame( defTestWindow, window );
    }

    @Test
    public void testGetGrid()
    {
        Grid2D  actGrid = defGridProbe.getGrid();
        assertEquals( actGrid.getName(), Constants.HOME_GRID );
    }

    @Test
    public void testGetGridBounds()
    {
        assertEquals( defTestWindow.getBounds(), defGridProbe.getGridBounds() );
    }

    @Test
    public void testGetImage()
    {
        Dimension       dim     = defTestWindow.getPreferredSize();
        BufferedImage   image   = defGridProbe.getImage();
        assertNotNull( image );
        assertEquals( dim.width, image.getWidth() );
        assertEquals( dim.height, image.getHeight() );
    }

    @Test
    public void testGetBackgroundColor()
    {
        assertEquals( 
            defTestWindow.getBackgroundColor().getRGB(), 
            defGridProbe.getBackgroundColor(),
            "background color"
        );
    }

    @Test
    public void testGetGridlineColor()
    {
        assertEquals( 
            defTestWindow.getGridlineColor().getRGB(), 
            defGridProbe.getGridlineColor(),
            "gridline color"
        );
    }

    @Test
    public void testGetCellSide()
    {
        assertEquals( defTestWindow.getCellSide(), defGridProbe.getCellSide() );
    }

    @Test
    public void testGetGridLineWidth()
    {
        assertEquals( 
            defTestWindow.getGridlineWidth(), 
            defGridProbe.getGridLineWidth(),
            "gridlineWidth"
        );
    }

    @Test
    public void testDispose()
    {
        GridWindow  testWindow  = new GridProbeTestWindow( null, null );
        testDispose( testWindow );
        testWindow = GridProbeTestWindow.getRealizedTestWindow( null );
        testDispose( testWindow );
    }
    
    @Test
    public void testGridValidation()
    {
        assertNull( defGridProbe.validateVerticalGridLines() );
        assertNull( defGridProbe.validateHorizontalGridLines() );
        assertNull( defGridProbe.validateCellInterior() );
    }
    
    /**
     * Gets a GridProbe encapsulating the given test window,
     * call GridProbe.dispose,
     * and verify the the test window's application root
     * has been disposed.
     * 
     * @param testWindow    the given test window
     */
    private static void testDispose( GridWindow testWindow )
    {
        GridProbe   probe       = GridProbe.getProbe( testWindow );
        Window      appRoot     = TestUtils.getAppRoot( testWindow );
        assertTrue( appRoot.isDisplayable() );
        probe.dispose();
        assertFalse( appRoot.isDisplayable() );
    }

    /**
     * Gets the application root window for the given component.
     * Raises an assertion if the root is not found.
     * 
     * @param component the given component
     * 
     *  @return the root of component's window hierarchy
     */
    private static Window getAppRoot( Component component )
    {
        Window  window  = TestUtils.getAppRoot( component );
        assertNotNull( window );
        return window;
    }
}
