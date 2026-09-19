package com.acmemail.judah.battleship.artwork.awt.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.swing.JFrame;

import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship.artwork.awt.GridWindow;
import com.acmemail.judah.battleship.model.Grid2D;

/**
 * An instance of this class
 * uses an image of a physical grid
 * to discover the grid's properties.
 * The discovered properties include
 * the grid's background and gridline colors,
 * the bounds of the physical grid,
 * and the coordinates of every cell.
 * Assumptions are made about the image,
 * including, but not limited to, the following:
 * <ol>
 * <li>
 *      The edges of the image are padded 
 *      with pixels painted strictly in the background color.
 * </li>
 * <li>
 *      The gridlines are painted using a single color,
 *      distinct from the background color.
 * </li>
 * <li>
 *      All cells are empty
 *      and their interiors are painted
 *      with the background color.
 * </li>
 * <li>
 *      The gridline width is greater than zero.
 * </li>
 * <li>
 *      If the gridline width is greater than one,
 *      results may be unpredictable.
 * </li>
 * </ol>
 */
public class GridProbe
{
    /** 
     * The root of the component hierarchy
     * that contains the encapsulated GridWindow.
     */
    private final Window        appRoot;
    /** The encapsulated GridWindow. */
    private final GridWindow    gridWindow;
    /** 
     * The logical grid encapsulated in the encapsulated GridWindow,
     * stored as a separate field for convenience.
     */
    private final Grid2D        logicalGrid;
    /** The bounds of the grid contained in the encapsulated GridWindow. */
    private final Rectangle     gridBounds      = new Rectangle();
    /** The image created from the encapsulated GridWindow. */
    private final BufferedImage image;
    /** The discovered background color of the encapsulated GridWindow. */
    private final int           backgroundColor;
    /** The discovered gridline color of the encapsulated GridWindow. */
    private final int           gridlineColor;
    /** 
     * The discovered cell-side length 
     * of the grid in the encapsulated GridWindow.
     */
    private final int           cellSide;
    /** 
     * The discovered width 
     * of a gridline in the encapsulated GridWindow.
     */
    private final int           gridLineWidth;
    
    /**
     * Creates a GridWindow from scratch,
     * and sets it up for probing.
     * 
     * @return  a GridProbe encapsulating the newly created GridWindow
     * 
     * @see #getProbe(GridWindow)
     */
    public static GridProbe getProbe()
    {
        GridWindow  window  = new GridWindow( Grid2D.getHomeGrid() );
        GridProbe   probe   = getProbe( window );
        return probe;
    }
    
    /**
     * Creates a GridProbe encapsulating the given GridWindow.
     * The GridWindow is realized, if necessary.
     * 
     * @param gridWindow    the GridWindow to be probed; may not be null
     *      
     * @return  
     *      a GridProbe encapsulating gridWindow
     */
    public static GridProbe getProbe( GridWindow gridWindow )
    {
        Objects.requireNonNull( gridWindow, "gridWindow" );
        if ( gridWindow.getParent() == null )
            TestUtils.invokeAndWait( () -> {
                JFrame  frame   = new JFrame();
                frame.setContentPane( gridWindow );
                frame.pack();
            });
        return TestUtils.invokeAndWaitGet( () -> new GridProbe( gridWindow ) );
    }
    
    /**
     * Gets the encapsulated GridWindow.
     * 
     * @return the encapsulated gridWindow
     */
    public GridWindow getGridWindow()
    {
        return gridWindow;
    }

    /**
     * Gets the logical grid contained in the encapsulated GridWindow.
     * 
     * @return the logical grid contained in the encapsulated GridWindow
     */
    public Grid2D getGrid()
    {
        return logicalGrid;
    }

    /**
     * Gets the bounds of the physical grid
     * in the encapsulated GridWindow.
     * 
     * @return the the bounds of the physical grid
     */
    public Rectangle getGridBounds()
    {
        return gridBounds;
    }

    /**
     * Gets the image derived from the encapsulated GridWindow.
     * 
     * @return the image derived from the GridWindow
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Gets the background color of the GridWindow.
     * 
     * @return the backgroundColor of the GridWindow
     */
    public int getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * Gets the color of the GridWindow's gridlines.
     * 
     * @return the color of the GridWindow's gridlines
     */
    public int getGridlineColor()
    {
        return gridlineColor;
    }

    /**
     * Gets the length of the side of a cell in the GridWindow.
     * 
     * @return the length of the side of a cell
     */
    public int getCellSide()
    {
        return cellSide;
    }

    /**
     * Gets the width of a gridline in the GridWindow.
     * 
     * @return the width of a gridline
     */
    public int getGridLineWidth()
    {
        return gridLineWidth;
    }

    /**
     * Disposes of any resources allocated to this object.
     */
    public void dispose()
    {
        appRoot.dispose();
    }
    
    /**
     * Verifies that vertical gridlines are drawn 
     * at every cell boundary.
     * If verified, returns null,
     * otherwise the coordinates of the first pixel
     * found not to be part of a gridline is returned.
     * 
     * @return  
     *      null if all vertical gridlines are correctly positioned,
     *      otherwise the coordinates of the first incorrect pixel
     */
    public Point validateVerticalGridLines()
    {
        Point   result  = 
            IntStream.range( 0, gridBounds.height )
                .filter( i -> i % cellSide == 0 )
                .map( i -> i + gridBounds.x )
                .filter( x -> !isVerticalGridLine( x ) )
                .mapToObj( x -> new Point( x, gridBounds.y ) )
                .findFirst().orElse( null );
        return result;
    }
    
    
    /**
     * Verifies that horizontal gridlines are drawn 
     * at every cell boundary.
     * If verified, returns null,
     * otherwise the coordinates of the first pixel
     * found not to be part of a gridline is returned.
     * 
     * @return  
     *      null if all horizontal gridlines are correctly positioned,
     *      otherwise the coordinates of the first incorrect pixel
     */
    public Point validateHorizontalGridLines()
    {
        Point   result  = 
            IntStream.range( 0, gridBounds.height )
                .filter( i -> i % cellSide == 0 )
                .map( i -> i + gridBounds.y )
                .filter( y -> !isHorizontalGridLine( y ) )
                .mapToObj( y -> new Point( gridBounds.x, y ) )
                .findFirst().orElse( null );
        return result;
    }

    /**
     * Verifies that the pixel at the center
     * of every expected cell location
     * is drawn in the background color.
     * If verified, returns null,
     * otherwise the coordinates of the first incorrect pixel
     * are returned.
     * 
     * @return  
     *      null if a all cell interiors are found where expected,
     *      otherwise the coordinates of the first incorrect pixel
     */
    public Point validateCellInterior()
    {
        Point   point   = null;
        int     minXco  = gridBounds.x + cellSide / 2;
        int     maxXco  = (int)gridBounds.getMaxX();
        int     minYco  = gridBounds.y + cellSide / 2;
        int     maxYco  = (int)gridBounds.getMaxY();
        
        int     yco     = minYco;
        for ( ; yco < maxYco && point == null ; yco += cellSide )
        {
            int xco     = minXco;
            for ( ; xco < maxXco && point == null ; xco += cellSide )
            {
                int color   = image.getRGB( xco, yco );
                if ( color != backgroundColor )
                    point = new Point( xco, yco );
            }
        }
        return point;
    }

    /**
     * Encapsulate the given GridWindow
     * in a new GridProbe object;
     * the GridWindow is expected to be
     * fully configured and realized
     * as part of a component hierarchy.

     * @param window    the given GridWindow
     * 
     * @throws NullPointerException if window is null
     * @throws IllegalArgumentException if window is not fully realized
     */
    private GridProbe( GridWindow window )
    {
        Objects.requireNonNull( window, "window" );
        this.appRoot = TestUtils.getAppRoot( window );
        if ( 
            appRoot == null 
            || window.getWidth() <= 0 
            || window.getHeight() <= 0
        )
            throw new IllegalArgumentException( StatusMessages.NOT_REALIZED );
        this.gridWindow = window;
        this.logicalGrid = window.getGrid();
        
        ScratchPad  pad = new ScratchPad();
        getImage( pad, window );
        image = pad.image;
        
        probeRightMargin( pad );
        backgroundColor = pad.backgroundColor;
        gridlineColor = pad.gridlineColor;
        probeTopMargin( pad );
        probeCellSide( pad );
        cellSide = pad.cellSide;
        gridLineWidth = pad.gridLineWidth;
        probeGridBounds( pad );
        gridBounds.setRect( pad.bounds );
    }
    
    /**
     * Get a BufferedImage representing the current state
     * of the given GridWindow.

     * @param pad           scratchpad to be updated
     * @param gridWindow    given GridWindow
     */
    private static void getImage( ScratchPad pad, GridWindow gridWindow )
    {
        Dimension       dim         = gridWindow.getPreferredSize();
        int             type        = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image       = 
            new BufferedImage( dim.width, dim.height, type );
        Graphics        gtx         = image.getGraphics();
        gridWindow.paintComponent( gtx );
        
        pad.image = image;
        pad.imageWidth = dim.width;
        pad.imageHeight = dim.height;
        pad.logicalGridCols = Grid2D.getNumCols();
        pad.logicalGridRows = Grid2D.getNumRows();
        
        // Given that there padding and labels are drawn at the 
        // left and right sides of the physical grid, the actual
        // length of a cell side must be less than
        // physical-grid-width / logical-grid-cols.
        pad.maxCellSide = pad.imageWidth / pad.logicalGridCols;
    }
    
    /**
     * Probe rows of pixels beginning at the top of the image.
     * Probe begins at middle of each row
     * and extends to the x-coordinate
     * of the last vertical gridline.
     * Find the first line that scans
     * as a single row of pixels consisting 
     * or the gridline color.
     * Raises an assertion if the first such row
     * isn't found before reaching
     * the vertical center of the image.
     * 
     * @param   pad 
     *      the scratch pad containing property values so far discovered
     */
    private void probeTopMargin( ScratchPad pad )
    {
        int     glColor     = pad.gridlineColor;
        int     midXco      = pad.imageWidth / 2;
        int     maxXco      = pad.lastXco;
        int     midYco      = pad.imageHeight / 2;
        int     topYco      = -1;
        int     xco         = midXco;
        int     yco         = 0;
        for ( yco = 0 ; yco < midYco && topYco < 0 ; ++ yco )
        {
            int color   = pad.image.getRGB( midXco, yco );
            for ( xco = midXco ; xco < maxXco && color == glColor ; ++xco )
                color = pad.image.getRGB( xco, yco );
            if ( xco == maxXco )
                topYco = yco;
        }
        if ( yco == midYco )
            fail( "top horizontal gridline not found" );
        pad.firstYco = topYco;
    }
    
    /**
     * Probe horizontally along the middle row of the image to locate
     * the rightmost gridline.
     * An assertion is raised if the last vertical gridline
     * can't be found.
     * 
     * <p>
     * Precondition: 
     * the rightmost pixel is painted with the background color.
     * <p>
     * Precondition: 
     * the gridline color is different from the background color.
     * <p>
     * Postcondition: 
     * Scratchpad updated with background color, gridline color, x-coordinate
     * of rightmost gridline.
     * 
     * @param pad   
     *      scratchpad containing previously discovered properties
     *      and updated with newly discovered properties.
     */
    private static void probeRightMargin( ScratchPad pad )
    {
        int     midXco      = pad.imageWidth / 2;
        int     midYco      = pad.imageHeight / 2;
        int     xco         = pad.imageWidth - 1;
        // right-most pixel presumed to be painted with background color
        int     bgColor     = pad.image.getRGB( xco, midYco );
        int     nextColor   = bgColor;
        
        // Choose the middle row of pixels. Probe backwards from the
        // right edge until a color different from the background color
        // is found. The new color is assumed to be the gridline color.
        // Declare and error f we reach the middle of the row without 
        // finding a gridline,
        while ( nextColor == bgColor && xco > midXco )
            nextColor = pad.image.getRGB( --xco, midYco );
        if ( nextColor == bgColor )
            fail( "no vertical gridline found" );
        pad.lastXco = xco;
        pad.backgroundColor = bgColor;
        pad.gridlineColor = nextColor;
    }
    
    /**
     * Scan a rectangular area of the GridWindow image
     * in the vertical center,
     * from the rightmost gridline 
     * to the horizontal center of the window.
     * Each row of pixels will have one of two patterns:<pre>
     *     ...gggggggggggggggggggggg
     *     ... g   g   g   g   g   g
     * </pre>
     * where 'g' is the gridline color,
     * and ' ' is the background color.
     * The length of the side of a cell
     * will be the number of consecutive spaces
     * plus the width of a gridline.
     * An assertion is raised if the expected patterns
     * are not found.
     * <p>
     * Precondition: 
     *     the right-hand edge of the physical grid has been discovered.
     * <p>
     * Precondition: 
     *     the background and gridline colors have been discovered.
     * <p>
     * Precondition: 
     *     the image and image bounds have been calculated.
     * <p>
     * Precondition: 
     *     the maximum length of a cell side has been calculated.
     * <p>
     * Postcondition: 
     *     the length of a cell side, and the width of a gridline
     *     have been set in the scratchpad.
     * 
     * @param pad   
     *      scratchpad containing previously discovered image properties
     *      and updated with newly discovered image properties.
     */
    private static void probeCellSide( ScratchPad pad )
    {
        int     rightXco    = pad.lastXco;
        int     leftXco     = pad.imageWidth / 2;
        int     topYco      = pad.imageHeight / 2 - pad.maxCellSide / 2;
        int     bottomYco   = topYco + pad.maxCellSide;
        List<SegmentList>   segLists    = new ArrayList<SegmentList>();
        for ( int yco = topYco ; yco < bottomYco ; ++yco )
        {
            SegmentList segList = new SegmentList();
            segLists.add( segList );
            int         currColor   = pad.image.getRGB( rightXco, yco );
            int         count       = 0;
            for ( int xco = rightXco ; xco > leftXco ; --xco )
            {
                int     nextColor   = pad.image.getRGB( xco, yco );
                if ( nextColor == currColor )
                    ++count;
                else
                {
                    segList.add( new Segment( currColor, count ) );
                    currColor = nextColor;
                    count = 1;
                }
            }
            segList.add( new Segment( currColor, count ) );
        }
        
        
        // Find a list with more than one segment. [0] must
        // correspond to the rightmost vertical gridline,
        // and [1] to the interior of the rightmost cell.
        SegmentList segList = segLists.stream()
            // only consider lists of more than one segment
            .filter( l -> l.size() > 1 )
            .findAny().orElse( null );
        assertNotNull( segList, "expected pattern not found" );
        Segment     seg0    = segList.get( 0 );
        Segment     seg1    = segList.get( 1 );
        
        // sanity check
        assertEquals( pad.gridlineColor, seg0.color() );
        assertEquals( pad.backgroundColor, seg1.color() );
        pad.gridLineWidth = seg0.count();
        pad.cellSide = seg1.count() + pad.gridLineWidth;
    }
    
    /**
     * Set the bounds of the physical grid in the scratch pad.
     * <p>
     * Precondition: 
     * cell side length, x-coordinate of the last vertical gridline,
     * y-coordinate of the first horizontal gridline,
     * num logical cols, and num logical rows have been discovered.
     * <p>
     * Postcondition: 
     * The bounding rectangle of the physical grid
     * is recorded in the scratch pad.
     * 
     * @param pad   
     *      scratchpad containing previously discovered image properties
     *      and updated with newly discovered image properties.
     */
    private static void probeGridBounds( ScratchPad pad )
    {
        pad.bounds.width = pad.logicalGridCols * pad.cellSide;
        pad.bounds.height = pad.logicalGridRows * pad.cellSide;
        pad.bounds.x = pad.lastXco - pad.bounds.width;
        pad.bounds.y = pad.firstYco;
        
        // sanity check:
        assertTrue( pad.bounds.x > 0 );
        assertTrue( pad.bounds.x + pad.bounds.width < pad.imageWidth );
        assertTrue( pad.bounds.y > 0 );
        assertTrue( pad.bounds.y + pad.bounds.height < pad.imageHeight );
    }
    
    /**
     * Returns true if a vertical gridline
     * is discovered to be drawn
     * at the given x-coordinate.
     * 
     * @param xco   the given x-coordinated
     * 
     * @return  
     *      true if a vertical gridline is drawn
     *      at the given x-coordinate
     */
    private boolean isVerticalGridLine( int xco )
    {
        int     ycoStart    = gridBounds.y;
        int     ycoEnd      = ycoStart + gridBounds.height;
        boolean result      =
            !IntStream.range( ycoStart, ycoEnd )
                .map( yco -> image.getRGB( xco, yco ) )
                .anyMatch( color -> color != gridlineColor );
        return result;
    }

    /**
     * Returns true if a horizontal gridline
     * is discovered to be drawn
     * at the given y-coordinate.
     * 
     * @param yco   the given y-coordinated
     * 
     * @return  
     *      true if a horizontal gridline is drawn
     *      at the given y-coordinate
     */
    private boolean isHorizontalGridLine( int yco )
    {
        int     xcoStart    = gridBounds.x;
        int     xcoEnd      = xcoStart + gridBounds.width;
        boolean result      =
            !IntStream.range( xcoStart, xcoEnd )
                .map( xco -> image.getRGB( xco, yco ) )
                .anyMatch( color -> color != gridlineColor );
        return result;
    }

    /**
     * Temporary to keep track of GridWindow properties
     * as they are discovered.
     * Some of the tracked properties
     * are needed to some other properties,
     * but not all tracked properties are needed
     * after discovery is complete.
     */
    private static class ScratchPad
    {
        /** The bounds of the grid in the GridWindow. */
        public Rectangle        bounds      = new Rectangle();
        /** Image of the GridWindow. */
        public BufferedImage    image;
        /** Rightmost x-coordinate of the grid in the GridWindow. */
        public int              lastXco;
        /** Topmost y-coordinate of the grid in the GridWindow. */
        public int              firstYco;
        /** Number of columns in the logical grid tied to the GridWindow. */
        public int              logicalGridCols;
        /** Number of rows in the logical grid tied to the GridWindow. */
        public int              logicalGridRows;
        /** The width of the image captured from the GridWindow. */
        public int              imageWidth;
        /** The height of the image captured from the GridWindow. */
        public int              imageHeight;
        /** The presumed background color of the GridWindow.. */
        public int              backgroundColor;
        /** The presumed gridline color of the GridWindow.. */
        public int              gridlineColor;
        /** The presumed width of a gridline in the GridWindow.. */
        public int              gridLineWidth;
        /** 
         * An estimated upper bound on length of the side of a cell
         * in the GridWindow's grid.
         */
        public int              maxCellSide;
        /** 
         * The presumed length of the side of a cell
         * in the GridWindow's grid.
         */
        public int              cellSide;
        
        /**
         * Default constructor.
         */
        public ScratchPad()
        {
        }
    }
    
    /**
     * Characterize the color and length
     * of a sequential series of pixels
     * all of the same color.
     * 
     * @param   color   the color of the pixels in the series
     * @param   count   the number of pixels in the series
     */
    private record Segment( int color, int count ) {}
    
    /**
     * A list of Segments.
     * Declared here solely for convenience.
     */
    @SuppressWarnings("serial")
    private static class SegmentList extends ArrayList<Segment>{}
}
