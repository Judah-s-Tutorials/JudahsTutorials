package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    /** Number of columns in Grid2D; should be different from TEST_ROWS. */
    private static final int    TEST_COLS   = 15;
    
    /** The color the GridWindow uses to paint selected cells. */
    private static int          selectColor;
    /** The color the GridWindow uses to paint ships. */
    private static int          shipColor;
    /** The color the GridWindow uses to paint splatted cells. */
    private static int          splatColor;
    /** Establishes/restores this class's grid dimensions. */
    private static final Grid2DTestSupport.GridBoundsFixture  gridBounds  =
        new Grid2DTestSupport.GridBoundsFixture();

    /**
     * The default GridWindow and its properties for ad hoc use by test
     * methods; create fresh for each test in the before-each method,
     * disposed in the after-each method.
     */
    private GridWindowProps defGridWindow;

    @BeforeAll
    public static void beforeAll()
    {
        gridBounds.establish( TEST_ROWS, TEST_COLS );
        getMiscColors();
    }

    @AfterAll
    public static void afterAll()
    {
        gridBounds.restore();
        Configurator.reset();
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
    
    @ParameterizedTest
    @ValueSource( doubles = { 2.0, .5 } )
    public void testScaledSelection2( double scale )
    {
        // beforeEach runs before each parameterized test, so
        // there is no need to save and restore scale
        defGridWindow.setScale( scale );
        
        int         numRows     = Grid2D.getNumRows();
        int         numCols     = Grid2D.getNumCols();
        int         centerRow   = numRows / 2;
        int         centerCol   = numCols / 2;
        // sanity check
        assertTrue( centerRow > 1 );
        assertTrue( centerRow < numRows - 1 );
        assertTrue( centerCol > 1 );
        assertTrue( centerCol < numCols - 1 );

        List<GridCoords>    expResults  = 
            IntStream.rangeClosed( centerRow - 1, centerRow + 1 )
                .boxed()
                .flatMap( r -> IntStream.rangeClosed( centerCol - 1, centerCol + 1)
                    .mapToObj( c -> new GridCoords( c, r ) )
                )
                .toList();
        List<GridCoords>    actResults   = new ArrayList<>();
        
        CellListener    cellListener    = ce -> actResults.add( ce.coords() );
        defGridWindow.addCellListener( cellListener );
        expResults.stream()
            .map( defGridWindow::getMouseClickEvent )
            .forEach( defGridWindow::dispatchEvent );
        assertEquals( expResults, actResults );
    }

    @Test
    public void testGridWindow()
    {
        GridProbe   probe   = defGridWindow.getProbe();
        Point       point   = probe.validateHorizontalGridLines();
        assertNull( point );
        point = probe.validateVerticalGridLines();
        assertNull( point );
        point = probe.validateCellInterior();
        assertNull( point );
    }

    @Test
    public void testAddCellListener()
    {
        int         numRows = Grid2D.getNumRows();
        int         numCols = Grid2D.getNumCols();
        boolean[][] result  = new boolean[numRows][numCols];
        Arrays.stream( result ).forEach( a -> Arrays.fill( a, false ) );
        defGridWindow.addCellListener( e -> {
            GridCoords  coords  = e.coords();
            result[coords.yco()][coords.xco()] = true;
        });
        
        for ( int row = 0 ; row < numRows ; ++row )
            for ( int col = 0 ; col < numCols ; ++col )
            {
                GridCoords  coords  = new GridCoords( col, row );
                MouseEvent  mEvt    = defGridWindow.getMouseClickEvent( coords );
                assertFalse( result[row][col] );
                defGridWindow.dispatchEvent( mEvt );
                assertTrue( result[row][col] );
            }
    }

    @Test
    public void testRemoveCellListener()
    {
        GridCoords      coords  = new GridCoords( 0, 0 );
        MouseEvent      mEvt    = defGridWindow.getMouseClickEvent( coords );

        boolean[]       result  = { false, false };
        CellListener    list0   = e -> result[0] = true;
        CellListener    list1   = e -> result[1] = true;
        
        defGridWindow.addCellListener( list0 );
        defGridWindow.dispatchEvent( mEvt );
        assertTrue( result[0] );
        assertFalse( result[1] );
        
        result[0] = false;
        result[1] = false;
        defGridWindow.addCellListener( list1 );
        defGridWindow.dispatchEvent( mEvt );
        assertTrue( result[0] );
        assertTrue( result[1] );
        
        result[0] = false;
        result[1] = false;
        defGridWindow.removeCellListener( list1 );
        defGridWindow.dispatchEvent( mEvt );
        assertTrue( result[0] );
        assertFalse( result[1] );
        
        result[0] = false;
        result[1] = false;
        defGridWindow.removeCellListener( list0 );
        defGridWindow.dispatchEvent( mEvt );
        assertFalse( result[0] );
        assertFalse( result[1] );
        
        // Cover the branches in removeCellListener
        // where we try to remove a listener that isn't there.
        result[0] = false;
        result[1] = false;
        defGridWindow.removeCellListener( list0 );
        defGridWindow.dispatchEvent( mEvt );
        assertFalse( result[0] );
        assertFalse( result[1] );
    }

    @Test
    public void testDispatchEventCellEvent()
    {
        boolean[]       actResult   = { false, false, false };
        CellListener    list0       = e -> actResult[0] = true;
        CellListener    list1       = e -> actResult[1] = true;
        CellListener    list2       = e -> actResult[2] = true;
        
        boolean[]       expResult   = new boolean[] { true, false, false };
        dispatchCellEvent( expResult, actResult, list0, null );
        
        expResult = new boolean[] { true, true, false };
        dispatchCellEvent( expResult, actResult, list1, null );
        
        expResult = new boolean[] { true, true, true };
        dispatchCellEvent( expResult, actResult, list2, null );
        
        expResult   = new boolean[] { false, true, true };
        dispatchCellEvent( expResult, actResult, null, list0 );
        
        expResult   = new boolean[] { false, false, true };
        dispatchCellEvent( expResult, actResult, null, list1 );
        
        expResult   = new boolean[] { false, false, false };
        dispatchCellEvent( expResult, actResult, null, list2 );
    }

    @Test
    public void testSelect()
    {
        int         bgColor     = defGridWindow.getBackgroundColor();
        int[][]     initRaster  = defGridWindow.getCellRaster();
        for ( int[] row : initRaster )
            for ( int cellColor : row )
                assertEquals( bgColor, cellColor );
        
        for ( int row = 0 ; row < initRaster.length ; ++row )
            for ( int col = 0 ; col < initRaster[row].length ; ++col)
            {
                GridCoords  coords      = new GridCoords( col, row );
                select( defGridWindow, coords );
            }
        
        defGridWindow.getGridWindow().select();
        int[][]     finalRaster = defGridWindow.getCellRaster();
        assertArrEquals( initRaster, finalRaster );
    }

    @Test
    public void testSelectGridCoords()
    {
        // Sanity check
        int         bgColor     = defGridWindow.getBackgroundColor();
        int[][]     expRaster   = defGridWindow.getCellRaster();
        for ( int[] row : expRaster )
            for ( int cellColor : row )
                assertEquals( bgColor, cellColor );
        
        for ( int row = 0 ; row < expRaster.length ; ++row )
            for ( int col = 0 ; col < expRaster[row].length ; ++col)
            {
                GridCoords  coords      = new GridCoords( col, row );
                select( defGridWindow, coords );
            }
    }

    @Test
    public void testDeselectGridCoords()
    {
        int         bgColor     = defGridWindow.getBackgroundColor();
        int[][]     expRaster   = defGridWindow.getCellRaster();
        // Sanity check
        for ( int[] row : expRaster )
            for ( int cellColor : row )
                assertEquals( bgColor, cellColor );
        
        int             min         = 
            Math.min( expRaster.length, expRaster[0].length );
        GridCoords[]    allCoords   = 
            IntStream.range( 0, min )
                .mapToObj( i -> new GridCoords( i, i ) )
                .toArray( GridCoords[]::new );
        Arrays.stream( allCoords )
            .forEach( gc -> select( defGridWindow, gc ) );
        
        Arrays.stream( allCoords )
            .forEach( gc -> deselect( defGridWindow, gc ) );
    }

    @Test
    public void testSelectRectangle()
    {
        int         rows    = Grid2D.getNumRows();
        int         cols    = Grid2D.getNumCols();
        int         yco0    = 0;
        int         xco0    = 0;
        Rectangle   rect0   = new Rectangle( xco0, yco0, 1, 1 );
        int         yco1    = yco0 + rect0.height;
        int         xco1    = xco0 + 1;
        Rectangle   rect1   = new Rectangle( xco1, yco1, 2, 2 );
        int         yco2    = yco1 + rect1.height;
        int         xco2    = xco1 + 1;
        Rectangle   rect2   = new Rectangle( xco2, yco2, 3, 3 );
        // sanity check
        assertTrue( rect2.x + rect2.width < cols );
        assertTrue( rect2.y + rect2.height < rows );
        
        select( defGridWindow, rect0 );
        select( defGridWindow, rect1 );
        select( defGridWindow, rect2 );
        
        deselect( defGridWindow, rect0 );
        deselect( defGridWindow, rect1 );
        deselect( defGridWindow, rect2 );
    }

    @Test
    public void testSetScale()
    {
        double      maxScale    = GridWindow.getMaxScale();
        double      minScale    = GridWindow.getMinScale();
        assertEquals( 1, defGridWindow.getScale() );
        assertTrue( maxScale > 1 );
        assertTrue( minScale < 1 );
        
        double      expValSmall = .75;
        double      expValLarge = 1.25;
        // sanity check
        assertTrue( expValSmall > minScale );
        assertTrue( expValLarge < maxScale );
        
        defGridWindow.setScale( expValSmall );
        assertEquals( expValSmall, defGridWindow.getScale() );
        defGridWindow.setScale( expValLarge );
        assertEquals( expValLarge, defGridWindow.getScale() );
        
        defGridWindow.setScale( minScale - .1 );
        assertEquals( minScale, defGridWindow.getScale() );
        defGridWindow.setScale( maxScale + .1 );
        assertEquals( maxScale, defGridWindow.getScale() );
    }
    
    @Test
    public void testSplats()
    {
        Configurator.reset();
        Configurator.nextState();
        assertTrue( Configurator.isConfig() );
        
        ShipType2D  shipType    = new ShipType2D( "testType", 4, 2, null );
        GridCoords  coords      = new GridCoords( 3, 2 );
        Ship2D      ship        = 
            new Ship2D( shipType, coords, Orientation.HORIZONTAL );
        Grid2D      grid2D      = defGridWindow.getGrid();
        grid2D.put( ship );
        
        // get the raster with the ship already displayed
        int[][]     expRaster   = defGridWindow.getCellRaster();
        Rectangle   shipBounds  = ship.getBounds();
        
        // attack cells starting in last row of ship, and ending
        // first row after ship
        int         startRow    = shipBounds.y + shipBounds.height - 1;
        int         endRow      = startRow + 1;
        // attack cells starting in the second column of the ship,
        // and ending in the first column after the ship
        int         startCol    = shipBounds.x + 1;
        int         endCol      = startCol + shipBounds.width;
        // sanity check
        GridCoords  upperLeft   = new GridCoords( startCol, startRow );
        GridCoords  lowerRight  = new GridCoords( endCol, endRow );
        assertTrue( grid2D.contains( upperLeft ) );
        assertTrue( grid2D.contains( lowerRight ) );
        
        Configurator.nextState();
        assertTrue( Configurator.isConfigComplete() );
        IntStream.rangeClosed( startRow, endRow )
            .boxed()
            .forEach( row -> IntStream.rangeClosed( startCol, endCol )
            .boxed()
                .map( col -> new GridCoords( col, row ) )
                .forEach( gc -> {
                    grid2D.attack( gc );
                    expRaster[gc.yco()][gc.xco()] = splatColor;
                })
            );
        
        int[][]     actRaster   = defGridWindow.getCellRaster();
        assertArrEquals( expRaster, actRaster );
    }
    
    @Test
    public void clickOutOfGrid()
    {
        Rectangle   gridBounds  = defGridWindow.getGridBounds();
        int         firstXco    = gridBounds.x;
        int         lastXco     = (int)gridBounds.getMaxX();
        int         firstYco    = gridBounds.y;
        int         lastYco     = (int)gridBounds.getMaxY();
        
        List<Point> cornerPoints    =
            List.of( 
                new Point( firstXco, firstYco ),
                new Point( lastXco - 1, firstYco ),
                new Point( firstXco, lastYco - 1 ),
                new Point( lastXco - 1, lastYco - 1 )
            );
        List<Point> externalPoints  =
            List.of( 
                new Point( firstXco - 1, firstYco ),
                new Point( lastXco + 1, firstYco ),
                new Point( firstXco, lastYco + 1 ),
                new Point( lastXco, lastYco + 1 )
            );
        
        boolean[]       result      = { false };
        CellListener    listener    = c -> result[0] = true;
        defGridWindow.addCellListener( listener );
        
        // sanity check... all mouse events hit
        cornerPoints.stream()
            .map( defGridWindow::getMouseClickEvent )
            .forEach( me -> {
                result[0] = false;
                defGridWindow.dispatchEvent( me );
                assertTrue( result[0], me.getPoint().toString() );
            });
        
        // all mouse events miss
        result[0] = false;
        externalPoints.stream()
            .map( defGridWindow::getMouseClickEvent )
            .forEach( me -> {
                defGridWindow.dispatchEvent( me );
                assertFalse( result[0], me.getPoint().toString()  );
            });
    }
    
    @Test
    public void testKeyEvents()
    {
        int         plusCode        = KeyEvent.VK_EQUALS;
        char        plusChar        = '=';
        int         minusCode       = KeyEvent.VK_MINUS;
        char        minusChar       = '-';
        int         zeroCode        = KeyEvent.VK_0;
        char        zeroChar        = '0';
        int         aCode           = KeyEvent.VK_A;
        char        aChar           = 'a';
        
        KeyEvent    plusEvent       = 
            getKeyPressedEvent( defGridWindow, false, plusCode, plusChar );
        KeyEvent    minusEvent      = 
            getKeyPressedEvent( defGridWindow, false, minusCode, minusChar );
        KeyEvent    zeroEvent       = 
            getKeyPressedEvent( defGridWindow, false, zeroCode, zeroChar );
        KeyEvent    aEvent          = 
            getKeyPressedEvent( defGridWindow, false, aCode, aChar );
        
        KeyEvent    ctrlPlusEvent   = 
            getKeyPressedEvent( defGridWindow, true, plusCode, plusChar );
        KeyEvent    ctrlMinusEvent  = 
            getKeyPressedEvent( defGridWindow, true, minusCode, minusChar );
        KeyEvent    ctrlZeroEvent   = 
            getKeyPressedEvent( defGridWindow, true, zeroCode, zeroChar );
        KeyEvent    ctrlAEvent      = 
            getKeyPressedEvent( defGridWindow, true, aCode, aChar );
        
        // to get KeyEvents a component must be visible and have the focus
        defGridWindow.setVisible( true );
        assertTrue( defGridWindow.requestFocus() );

        // these events don't change the scale
        assertEquals( 1.0, defGridWindow.getScale() );
        defGridWindow.dispatchEvent( plusEvent );
        assertEquals( 1.0, defGridWindow.getScale() );
        defGridWindow.dispatchEvent( minusEvent );
        assertEquals( 1.0, defGridWindow.getScale() );
        
        defGridWindow.setScale( .5 );
        defGridWindow.dispatchEvent( zeroEvent );
        assertEquals( .5, defGridWindow.getScale() );

        defGridWindow.dispatchEvent( aEvent );
        assertEquals( .5, defGridWindow.getScale() );
        defGridWindow.dispatchEvent( ctrlAEvent );
        assertEquals( .5, defGridWindow.getScale() );
        
        // these events change the scale
        defGridWindow.dispatchEvent( ctrlZeroEvent );
        assertEquals( 1.0, defGridWindow.getScale() );
        
        defGridWindow.dispatchEvent( ctrlPlusEvent );
        assertTrue( defGridWindow.getScale() > 1.0 );
        
        defGridWindow.dispatchEvent( ctrlMinusEvent );
        assertEquals( 1.0, defGridWindow.getScale() );
        
        defGridWindow.dispatchEvent( ctrlMinusEvent );
        assertTrue( defGridWindow.getScale() < 1.0 );
    }
    
    /**
     * Select the given coordinates in the GridWindow
     * of the given {@link GridWindowProps}.
     * Verify that the selection took place as expected.
     * Operations are executed on the EDT as necessary.
     * 
     * @param props     the given GridWindowProps
     * @param coords    the given coordinates
     */
    private static void select( GridWindowProps props, GridCoords coords )
    {
        int[][]     expRaster   = props.getCellRaster();
        GridWindow  gridWindow  = props.getGridWindow();
        int         col         = coords.xco();
        int         row         = coords.yco();
        TestUtils.invokeAndWait( () -> {
            gridWindow.select( coords );
            expRaster[row][col] = selectColor;
        });
        int[][]     actRaster   = props.getCellRaster();
        assertArrEquals( expRaster, actRaster );
    }
    
    /**
     * Deselect the given coordinates in the GridWindow
     * of the given {@link GridWindowProps}.
     * Verify that deselection takes place as expected.
     * Operations are executed on the EDT as necessary.
     * 
     * @param props     the given GridWindowProps
     * @param coords    the given coordinates
     */
    private static void deselect( GridWindowProps props, GridCoords coords )
    {
        int         bgColor     = props.getBackgroundColor();
        int[][]     expRaster   = props.getCellRaster();
        int         col         = coords.xco();
        int         row         = coords.yco();
        props.deselect( coords );
        expRaster[row][col] = bgColor;
        int[][]     actRaster   = props.getCellRaster();
        assertArrEquals( expRaster, actRaster );
    }
    
    /**
     * Select the given rectangle in the GridWindow
     * of the given {@link GridWindowProps}.
     * Verify that the selection took place as expected.
     * Operations are executed on the EDT as necessary.
     * 
     * @param props   the given GridWindowProps
     * @param rect    the given rectangle
     */
    private static void select( GridWindowProps props, Rectangle rect )
    {
        int[][]     expRaster   = props.getCellRaster();
        int         col         = rect.x;
        int         row         = rect.y;
        IntStream.range( row, row + rect.height )
            .forEach( r -> 
                IntStream.range( col, col + rect.width )
                    .forEach(c -> expRaster[r][c] = selectColor )
            );
 
        props.select( rect );
        int[][]     actRaster   = props.getCellRaster();
        assertArrEquals( expRaster, actRaster );
    }
    
    /**
     * Deselect the given rectangle in the GridWindow
     * of the given {@link GridWindowProps}.
     * Verify that the deselection took place as expected.
     * Operations are executed on the EDT as necessary.
     * 
     * @param props   the given GridWindowProps
     * @param rect    the given rectangle
     */
    private static void deselect( GridWindowProps props, Rectangle rect )
    {
        int         bgColor     = props.getBackgroundColor();
        int[][]     expRaster   = props.getCellRaster();
        int         col         = rect.x;
        int         row         = rect.y;
        IntStream.range( row, row + rect.height )
            .forEach( r -> 
                IntStream.range( col, col + rect.width )
                    .forEach(c -> expRaster[r][c] = bgColor )
            );
 
        props.deselect( rect );
        int[][]     actRaster   = props.getCellRaster();
        assertArrEquals( expRaster, actRaster );
    }
    
    /**
     * Dispatch a CellEvent and validate the result.
     * This is a helper method for {@link #testDispatchEventCellEvent()}.
     * The caller passes an expected result array,
     * and an array to hold an actual result.
     * Optionally,
     * the caller passes a CellListener to add,
     * and/or a CellListener to remove,
     * which are configured prior to dispatching the CellEvent;
     * the expectation is that a listener added to the GridWindow
     * will set a value in the actual result array.
     * The actual result array is reset,
     * the CellEvent is dispatched,
     * and the expected result is compared to the actual result.
     * 
     * @param expResult the expected result array
     * @param actResult the actual result array
     * @param toAdd     a listener to add; may be null
     * @param toRemove  a listener to remove; may be null
     */
    private void dispatchCellEvent( 
        boolean[]       expResult, 
        boolean[]       actResult,
        CellListener    toAdd,
        CellListener    toRemove
    )
    {
        GridCoords  coords  = new GridCoords( 0, 0 );
        MouseEvent  mEvent  = defGridWindow.getMouseClickEvent( coords );
        CellEvent   event   = new CellEvent( coords, mEvent );
        Arrays.fill( actResult, false );
        if ( toAdd != null )
            defGridWindow.addCellListener( toAdd );
        if ( toRemove != null )
            defGridWindow.removeCellListener( toRemove );
        defGridWindow.dispatchCellEvent( event );
        assertTrue( Arrays.equals( expResult, actResult ) );
    }
    
    /**
     * Constructs a KeyEvent encoding key-pressed event.
     * 
     * @param props     the properties object containing the event source
     * @param isCtrl    true if the control-down mask is to be set
     * @param keyCode   the key-code to encapsulate
     * @param keyChar   the key-char to encapsulated
     * 
     * @return  the constructed key event
     */
    private static KeyEvent getKeyPressedEvent( 
        GridWindowProps props,
        boolean isCtrl,
        int keyCode,
        char keyChar
    )
    {
        int mask    =  isCtrl ? InputEvent.CTRL_DOWN_MASK : 0;
        KeyEvent    event   =
            new KeyEvent(
                props.getGridWindow(),
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                mask,
                keyCode,
                keyChar
            );
        return event;
    }

    /**
     * Assert that two given 2D arrays are equal.
     *  
     * @param arr1  the first given array
     * @param arr2  the second given array
     */
    private static void assertArrEquals( int[][] arr1, int[][] arr2 )
    {
        assertEquals( arr1.length, arr2.length, "rows" );
        for ( int row = 0 ; row < arr1.length ; ++row )
        {
            assertEquals( arr1[row].length, arr2[row].length, "cols" );
            for ( int col = 0 ; col < arr1[row].length ; ++col )
            {
                String  comment =
                    String.format( "[%d][%d]", row, col );
                assertEquals( arr1[row][col], arr2[row][col], comment );
            }
        }
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
        Grid2D          logicalGrid = props.getGrid();
        ShipType2D      shipType    = Submarine.getType();
        GridCoords      coords      = new GridCoords( 0, 0 );
        Ship2D          ship        = 
            new Ship2D( shipType, coords, Orientation.HORIZONTAL );
        
        List<Integer>   allColors   = 
            new ArrayList<>( 
                List.of( props.getBackgroundColor(), props.getGridlineColor() )
            );
        props.select( coords );
        int             color       = props.getCellColor( coords );
        assertFalse( allColors.contains( color ) );
        selectColor = color;
        allColors.add( color );
        
        props.select();
        logicalGrid.put( ship );
        color = props.getCellColor( coords );
        assertFalse( allColors.contains( color ) );
        shipColor = color;
        allColors.add( shipColor );
        
        logicalGrid.remove( ship );
        color = props.getCellColor( coords );
        assertEquals( color, props.getBackgroundColor() );
        
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
}
