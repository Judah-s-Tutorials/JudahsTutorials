package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.Grid2DTestSupport;

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
        fail("Not yet implemented");
    }

    @Test
    void testAddCellListener()
    {
        fail("Not yet implemented");
    }

    @Test
    void testRemoveCellListener()
    {
        fail("Not yet implemented");
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
    
//    private static void initGridwindow( Grid2D grid )
//    {
//    }
    
    /**
     * Executes the given Runnable on the EDT.
     * 
     * @param runner    the given Runnable
     * 
     * @throws BattleshipException if the operation on the EDT fails
     */
    private static void invokeAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> runner.run() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            throw new BattleshipException( "unexpect exception", exc );
        }
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
        
        /** Used ad hoc in functional interface implementations. */
        private static volatile Object temp;
        
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
        public static synchronized GridWindowProps getInstance( Grid2D grid )
        {
            invokeAndWait( () -> temp = new GridWindowProps( grid ) );
            return (GridWindowProps)temp;
        }
        
        /**
         * Gets the GridWindow's preferred size.
         * 
         * @return  the GridWindow's preferred size
         */
        public synchronized Dimension getPreferredSize()
        {
            invokeAndWait( () -> temp = gridWindow.getPreferredSize() );
            return (Dimension)temp;
        }
        
        /**
         * Disposes all internally held resources.
         */
        public void dispose()
        {
            invokeAndWait( () -> dummyFrame.dispose() );
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

    }
}
