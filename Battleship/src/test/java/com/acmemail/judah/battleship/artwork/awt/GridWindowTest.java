package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.artwork.awt.utils.GridProbe;
import com.acmemail.judah.battleship.artwork.awt.utils.TestUtils;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.Grid2DTestSupport;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

class GridWindowTest
{
    /** Number of rows in Grid2D; should be different from TEST_COLS. */
    private static final int    TEST_ROWS   = 10;
    /** Number of columns in Grid2D; should be different from TEST_COLS. */
    private static final int    TEST_COLS   = 15;
    
    private static int          selectColor;
    private static int          shipColor;
    private static int          splatColor;
    
    private GridWindowProps defGridWindow;
    
    @BeforeAll
    public static void beforeAll()
    {
        Grid2DTestSupport.setGridBounds( TEST_ROWS, TEST_COLS );
        getMiscColors();
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
    public void testGridWindow()
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
    public void testAddCellListener()
    {
        int         numRows = Grid2D.getNumRows();
        int         numCols = Grid2D.getNumCols();
        GridWindow  grid    = defGridWindow.getGridWindow();
        boolean[][] result  = new boolean[numRows][numCols];
        Arrays.stream( result ).forEach( a -> Arrays.fill( a, false ) );
        grid.addCellListener( e -> {
            GridCoords  coords  = e.coords();
            result[coords.yco()][coords.xco()] = true;
        });
        
        GridProbe   probe   = defGridWindow.getProbe();
        for ( int row = 0 ; row < numRows ; ++row )
            for ( int col = 0 ; col < numCols ; ++col )
            {
                Rectangle   rect    = probe.getCellBounds( col, row );
                int         xco     = (int)rect.getCenterX();
                int         yco     = (int)rect.getCenterY();
                MouseEvent  mEvt    = getMouseClickEvent( xco, yco );
                assertFalse( result[row][col] );
                TestUtils.invokeAndWait( () -> grid.dispatchEvent( mEvt ) );
                assertTrue( result[row][col] );
            }
    }

    @Test
    public void testRemoveCellListener()
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
    public void testDispatchEventCellEvent()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSelect()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectGridCoords()
    {
        int         bgColor     = defGridWindow.getProbe().getBackgroundColor();
        GridWindow  window      = defGridWindow.getGridWindow();
        int[][]     expRaster   = defGridWindow.getCellRaster();
        // Sanity check
        for ( int[] row : expRaster )
            for ( int cellColor : row )
                assertEquals( bgColor, cellColor );
        for ( int row = 0 ; row < expRaster.length ; ++row )
            for ( int col = 0 ; col < expRaster[row].length ; ++col)
            {
                expRaster[row][col]     = selectColor;
                GridCoords  coords      = new GridCoords( col, row );
                window.select( coords );
                int[][]     actRaster   = defGridWindow.getCellRaster();
                assertArrEquals( expRaster, actRaster );
            }
    }
    
    private static void assertArrEquals( int[][] arr1, int[][] arr2 )
    {
        assertEquals( arr1.length, arr2.length, "rows" );
        for ( int row = 0 ; row < arr1.length ; ++row )
        {
            assertEquals( arr1[row].length, arr2[row].length );
            for ( int col = 0 ; col < arr1[row].length ; ++col )
            {
                String  comment =
                    String.format( "[%d][%d]", row, col );
                assertEquals( arr1[row][col], arr2[row][col], comment );
            }
        }
    }
    
    void printArr( int[][] arr )
    {
        for ( int[] row : arr )
        {
            for ( int col : row )
                System.out.print( col + " " );
            System.out.println();
        }
        System.out.println();
    }

    @Test
    public void testDeselectGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectRectangle()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testDeselectRectangle()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSetScale()
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
    
    private static MouseEvent getMouseClickEvent( 
        Component source, int xco, int yco 
    )
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
     * Configures the GridWindow select, ship, and splat colors.
     * This entails manipulating the logical grid;
     * upon completion of the task,
     * the Grid2D framework will be reset.
     * <p>
     * Postcondition:
     * selectColor, shipColor, and splatColor fields initialized,
     * Grid2D reset.
     */
    private static void getMiscColors()
    {
        // must be in config mode to add/remove ships
        Configurator.reset();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );
        
        GridWindowProps props       = GridWindowProps.getInstance();
        GridProbe       probe       = props.getProbe();
        GridWindow      gridWindow  = props.getGridWindow();
        Grid2D          logicalGrid = props.getGrid();
        ShipType2D      shipType    = Submarine.getType();
        GridCoords      coords      = new GridCoords( 0, 0 );
        Ship2D          ship        = 
            new Ship2D( shipType, coords, Orientation.HORIZONTAL );
        
        List<Integer>   allColors   = 
            new ArrayList<>( 
                List.of( probe.getBackgroundColor(), probe.getGridlineColor() )
            );
        gridWindow.select( coords );
        int             color       = props.getCellColor( coords );
        assertFalse( allColors.contains( color ) );
        selectColor = color;
        allColors.add( color );
        
        gridWindow.select();
        logicalGrid.put( ship );
        color = props.getCellColor( coords );
        assertFalse( allColors.contains( color ) );
        shipColor = color;
        allColors.add( shipColor );
        
        logicalGrid.remove( ship );
        color = props.getCellColor( coords );
        assertEquals( color, probe.getBackgroundColor() );
        
        // Must be in game state to attack a cell
        Configurator.nextState();
        assertTrue( Configurator.isConfigComplete() );
        logicalGrid.attack( coords );
        color = props.getCellColor( coords );
        assertFalse( allColors.contains( color ) );
        splatColor = color;
        allColors.add( splatColor );
        
        Grid2DTestSupport.reset();
        props.dispose();
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
         * the grid allocated by {@link Grid2D#getHomeGrid()}.
         * Instantiation explicitly occurs on the EDT.
         * 
         * @return  the instantiated GridWindowProps object
         */
        public static GridWindowProps getInstance()
        {
            GridWindowProps props   = getInstance( Grid2D.getHomeGrid() );
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
        
        /**
         * Gets a fresh snapshot of the encapsulated grid,
         * and returns the color of the cell
         * at the given coordinates.
         * 
         * @param coords   the given coordinates
         *  
         * @return  the current color of the cell at the given coordinates
         */
        public int getCellColor( GridCoords coords )
        {
            int color   = getCellColor( coords.xco(), coords.yco() );
            return color;
        }
        
        /**
         * Gets a fresh snapshot of the encapsulated grid,
         * and returns the color of the cell
         * at the given coordinates.
         *
         * @param col   the given column-coordinate
         * @param row   the given row-coordinate
         *  
         * @return  the current color of the cell at the given coordinates
         */
        public int getCellColor( int col, int row )
        {
            getProbe();
            int[][]     raster      = getRaster();
            Rectangle   rect        = probe.getCellBounds( col, row );
            int         centerXco   = (int)rect.getCenterX();
            int         centerYco   = (int)rect.getCenterY();
            int         color       = raster[centerXco][centerYco];
            return color;
        }
        
        public int[][] getCellRaster()
        {
            getProbe();
            int     rows    = Grid2D.getNumRows();
            int     cols    = Grid2D.getNumCols();
            int[][] raster  = getRaster();
            int[][] cells   = new int[rows][cols];
            for ( int row = 0 ; row < rows ; ++row )
                for ( int col = 0 ; col < cols ; ++col )
                {
                    Rectangle   rect        = probe.getCellBounds( col, row );
                    int         centerXco   = (int)rect.getCenterX();
                    int         centerYco   = (int)rect.getCenterY();
                    int         color       = raster[centerYco][centerXco];
                    cells[row][col] = color;
                }
            return cells;
        }
        
        /**
         * Create a fresh image using the encapsulated GridWindow.
         * Return a raster of the image
         * represented as a 2-dimensional array of integers.
         * 
         * @return a raster of a freshly drawn GridWindow image
         */
        public int[][] getRaster()
        {
            BufferedImage   image   = getImage( gridWindow );
            
            // There are more efficient ways to do this but I opted
            // for straightforward.
            int     height  = image.getHeight();
            int     width   = image.getWidth();
            int[][] raster  = new int[height][width];
            for ( int row = 0 ; row < raster.length ; ++row )
                for ( int col = 0 ; col < raster[row].length ; ++col )
                    raster[row][col] = image.getRGB( col, row );
            return raster;
        }

        /**
         * Create a snapshot image of the given GridWindow.
         * 
         * @param window
         * @return  a snapshot image of the given GridWindow
         */
        private static BufferedImage getImage( GridWindow window )
        {
            Dimension       dim         = window.getPreferredSize();
            int             type        = BufferedImage.TYPE_INT_ARGB;
            BufferedImage   image       = 
                new BufferedImage( dim.width, dim.height, type );
            Graphics        gtx         = image.getGraphics();
            window.paintComponent( gtx );
            return image;
        }
    }
    
    private static class ImageManager
    {
        private final GridProbe     probe;
        private final GridWindow    gridWindow;
        
        public ImageManager( GridWindowProps props )
        {
            probe = props.getProbe();
            gridWindow = props.getGridWindow();
        }
        
        public int[][] getSnapshot()
        {
            BufferedImage   image   = getImage( gridWindow );
            
            // There are more efficient ways to do this but I opted
            // for straightforward.
            int[][] raster  = new int[image.getWidth()][image.getHeight()];
            for ( int yco = 0 ; yco < raster.length ; ++yco )
                for ( int xco = 0 ; xco < raster[yco].length ; ++xco )
                    raster[xco][yco] = image.getRGB( xco, yco );
            return raster;
        }

        private static BufferedImage getImage( GridWindow window )
        {
            Dimension       dim         = window.getPreferredSize();
            int             type        = BufferedImage.TYPE_INT_ARGB;
            BufferedImage   image       = 
                new BufferedImage( dim.width, dim.height, type );
            Graphics        gtx         = image.getGraphics();
            window.paintComponent( gtx );
            return image;
        }
    }
}
