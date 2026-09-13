package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.artwork.awt.utils.GridProbe;
import com.acmemail.judah.battleship.artwork.awt.utils.TestUtils;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.Grid2DTestSupport;
import com.acmemail.judah.battleship.model.GridCoords;

class GridWindowTest
{
    private GridWindowProps defGridWindow;
    
    @BeforeAll
    public static void beforeAll()
    {
    }

    @BeforeEach
    public void beforeEach()
    {
        Grid2DTestSupport.reset();
        if ( defGridWindow != null )
            defGridWindow.dispose();
        defGridWindow = GridWindowProps.getInstance();
    }

    @AfterEach
    public void afterEach()
    {
        if ( defGridWindow != null )
        {
            defGridWindow.dispose();
            defGridWindow = null;
        }
    }

    @Test
    void testGridWindow()
    {
        GridProbe   probe   = defGridWindow.getProbe();
        Point       point   = probe.validateHorizontalGridLines();
        assertNull( point );
        point = probe.validateHorizontalGridLines();
        assertNull( point );
        point = probe.validateCellInterior();
        assertNull( point );
    }

    @Test
    void testAddCellListener()
    {
        int         numRows = Grid2D.getNumRows();
        int         numCols = Grid2D.getNumCols();
        GridWindow  grid    = defGridWindow.getGridWindow();
        boolean[][] result  = new boolean[numRows][numCols];
        Arrays.stream( result ).forEach( a -> Arrays.fill( a, false ) );
        grid.addCellListener( e -> {
            GridCoords  coords  = e.coords();
            result[coords.xco()][coords.yco()] = true;
        });
        
        GridProbe   probe   = defGridWindow.getProbe();
        for ( int row = 0 ; row < numRows ; ++row )
            for ( int col = 0 ; col < numCols ; ++col )
            {
                Rectangle   rect    = probe.getCellBounds( col, row );
                int         xco     = (int)rect.getCenterX();
                int         yco     = (int)rect.getCenterY();
                MouseEvent  mEvt    = getMouseClickEvent( xco, yco );
                assertFalse( result[col][row] );
                TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
                assertTrue( result[col][row] );
            }
    }

    @Test
    void testRemoveCellListener()
    {
        GridProbe       probe   = defGridWindow.getProbe();
        Rectangle       rect    = probe.getCellBounds( 0, 0 );
        int             xco     = (int)rect.getCenterX();
        int             yco     = (int)rect.getCenterY();
        MouseEvent      mEvt    = getMouseClickEvent( xco, yco );

        boolean[]       result  = { false, false };
        CellListener    list0   = e -> result[0] = true;
        CellListener    list1   = e -> result[1] = true;
        GridWindow      grid    = defGridWindow.getGridWindow();
        
        grid.addCellListener( list0 );
        TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
        assertTrue( result[0] );
        assertFalse( result[1] );
        
        result[0] = false;
        result[1] = false;
        grid.addCellListener( list1 );
        TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
        assertTrue( result[0] );
        assertTrue( result[1] );
        
        result[0] = false;
        result[1] = false;
        grid.removeCellListener( list1 );
        TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
        assertTrue( result[0] );
        assertFalse( result[1] );
        
        result[0] = false;
        result[1] = false;
        grid.removeCellListener( list0 );
        TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
        assertFalse( result[0] );
        assertFalse( result[1] );
        
        // Cover the branches in removeCellListener
        // where we try to remove a listener that isn't there.
        result[0] = false;
        result[1] = false;
        grid.removeCellListener( list0 );
        TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
        assertFalse( result[0] );
        assertFalse( result[1] );
    }

    @Test
    void testDispatchEventCellEvent()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSelect()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSelectGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testDeselectGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSelectRectangle()
    {
        fail("Not yet implemented");
    }

    @Test
    void testDeselectRectangle()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetScale()
    {
        GridWindow  window      = defGridWindow.getGridWindow();
        double      maxScale    = window.getMaxScale();
        double      minScale    = window.getMinScale();
        assertEquals( 1, window.getScale() );
        assertTrue( maxScale > 1 );
        assertTrue( minScale < 1 );
        
        double      expValSmall = .75;
        double      expValLarge = 1.25;
        // sanity check
        assertTrue( expValSmall > minScale );
        assertTrue( expValLarge < maxScale );
        
        window.setScale( expValSmall );
        assertEquals( expValSmall, window.getScale() );
        window.setScale( expValLarge );
        assertEquals( expValLarge, window.getScale() );
        
        window.setScale( minScale - .1 );
        assertEquals( minScale, window.getScale() );
        window.setScale( maxScale + .1 );
        assertEquals( maxScale, window.getScale() );
    }
    
    private MouseEvent getMouseClickEvent( int xco, int yco )
    {
        MouseEvent  clickEvent  = 
            getMouseClickEvent( defGridWindow.getGridWindow(), xco, yco );
        return clickEvent;
    }
    
    private MouseEvent getMouseClickEvent( Component source, int xco, int yco )
    {
        MouseEvent clickEvent = new MouseEvent(
            source,                         // Source component
            MouseEvent.MOUSE_CLICKED,       // Event type
            System.currentTimeMillis(),     // Timestamp
            0,                              // Modifiers
            xco, yco,                       // X and Y coordinates
            1,                              // Click count
            false,                          // Popup trigger
            MouseEvent.BUTTON1              // Mouse button (Left click)
        );
        return clickEvent;
    }

    /**
     * Instantiates, configures, and provides access to a GridWindow.
     * Where necessary, operations are performed on the EDT.
     */
    private static class GridWindowProps
    {
        /** Application frame in which to embed the encapsulated GridWindow. */
        private final JFrame        dummyFrame;
        
        /** The encapsulated GridWindow. */
        private final GridWindow    gridWindow;
        /** The grid encapsulated in the GridWindow. */
        private final Grid2D        grid;
        /** Probe to characterize encapsulated GridWindow. */
        private GridProbe           probe;
        
        /**
         * Constructor.
         * Must be invoked on the EDT.
         * 
         * @param grid  
         *      the Grid2D to needed to instantiate
         *      the encapsulated GridWindow.
         */
        private GridWindowProps( Grid2D grid )
        {
            this.grid = grid;
            gridWindow = new GridWindow( grid );
            dummyFrame = new JFrame();
            dummyFrame.setContentPane( gridWindow );
            dummyFrame.pack();
        }
        
        /**
         * Instantiates a GridWindowProps incorporating 
         * an internally allocated Grid2D.
         * Instantiation explicitly occurs on the EDT.
         * 
         * @param grid  the given grid
         * 
         * @return  the instantiated GridWindowProps object
         */
        public static GridWindowProps getInstance()
        {
            GridWindowProps props   = getInstance( new Grid2D() );
            return props;
        }
        
        /**
         * Instantiates a GridWindowProps incorporating the given Grid2D.
         * Instantiation explicitly occurs on the EDT.
         * 
         * @param grid  the given grid
         * 
         * @return  the instantiated GridWindowProps object
         */
        public static GridWindowProps getInstance( Grid2D grid )
        {
            GridWindowProps windowProps =
                TestUtils.invokeAndWaitGet( () -> new GridWindowProps( grid ) );
            return windowProps;
        }
        
        /**
         * Gets the GridWindow's preferred size.
         * 
         * @return  the GridWindow's preferred size
         */
        public Dimension getPreferredSize()
        {
            Dimension   size    = 
                TestUtils.invokeAndWaitGet( () -> 
                    gridWindow.getPreferredSize() 
                );
            return size;
        }
        
        /**
         * Disposes all internally held resources.
         */
        public void dispose()
        {
            TestUtils.invokeAndWait( () -> dummyFrame.dispose() );
        }
        
        /**
         * Gets the frame that hosts the GridWindow
         * 
         * @return the dummyFrame
         */
        public JFrame getFrame()
        {
            return dummyFrame;
        }

        /**
         * Gets the GridWindow
         * 
         * @return the gridWindow
         */
        public GridWindow getGridWindow()
        {
            return gridWindow;
        }

        /**
         * Gets the Grid2D encapsulated in the GridWindow.
         * 
         * @return the grid encapsulated in the GridWindow
         */
        public Grid2D getGrid()
        {
            return grid;
        }

        /**
         * Gets a GridProbe describing the encapsulated GridWindow.
         * 
         * @return a GridProbe describing the encapsulated GridWindow
         */
        public GridProbe getProbe()
        {
            if ( probe == null )
                probe = GridProbe.getProbe( gridWindow );
            return probe;
        }
    }
}
