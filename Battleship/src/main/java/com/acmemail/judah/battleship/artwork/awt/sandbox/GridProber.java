package com.acmemail.judah.battleship.artwork.awt.sandbox;

import static com.acmemail.judah.battleship.Constants.KEY_NUM_COLS;
import static com.acmemail.judah.battleship.Constants.KEY_NUM_ROWS;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Constants;
import com.acmemail.judah.battleship.artwork.awt.GridWindow;
import com.acmemail.judah.battleship.model.Grid2D;

/**
 * Determine the geometry of a physical grid.
 * In the following figure, 
 * '==' represents a horizontal gridline
 * and '||' represents a vertical gridline.
 * <p>
 * Assumptions:
 * Given a logical grid with a "reasonable" number of rows and columns:
 * <ol>
 * <li>
 * If we compute {@code max-cell-side = window-width / num-cols},
 * than {max-cell-side > actual-cell-side}.
 * </li>
 * <li>
 * {@code min-yco = window-height / 2 - max-cell-side}
 * will be a pixel location below the first row, and
 * {@code max-yco = window-height / 2 + max-cell-side} will be 
 * above the last row.
 * </li>
 * <li>
 * The range {@code [min-yco,max-yco]}
 * will span at least one row,
 * including the bounding horizontal gridlines,
 * and will not include margins
 * above the first row or below the last row.
 * </li>
 * <li>
 * Every horizontal line of pixels
 * will end in the same pattern: 
 * <em>n</em> pixels of background color
 * preceded by a pixel of gridline color.
 * </li>
 * <li>
 * Given the horizontal range {@code min-xco = grid-width / 2, 
 * max-xco = grid-width}
 * every line of pixels will end in one of two patterns:<br>
 * <pre>
 *    ... |    |    |    ]
 *    ...============    ]
 *    ... |    |    |    ]
 *    ...============    ]
 *    ... |    |    |    ]</pre>
 * </li>
 * <li>
 * The length of the side of a cell is 
 * the number of pixels between two vertical gridlines
 * plus the width of a gridline.
 * </li>
 * </ol>
 */
public class GridProber
{
    private static final int    EXP_NUM_COLS    = 10;
    private static final int    EXP_NUM_ROWS    = 15;
    
    private static JFrame       dummyFrame;
    private static GridWindow   window;
    private static Dimension    gridWindowSize;
    
    public static void main( String[] args )
    {
        initGrid2D();
        Grid2D          grid        = new Grid2D();
        initGridwindow( grid );
        
        int             type        = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image       = new 
            BufferedImage( gridWindowSize.width, gridWindowSize.height, type );
        window.paintComponent( image.getGraphics() );
        GridProperties  props   = new GridProperties( image, grid );
        System.out.println( props.gridBounds );
        verifyHorizontalGridlines( props );
        verifyVerticalGridlines( props );
        verifyCellInterior( props );
        dummyFrame.dispose();
    }
    
    private static void initGridwindow( Grid2D grid )
    {
        invokeAndWait( () -> {
            window = new GridWindow( grid );
            dummyFrame = new JFrame();
            dummyFrame.setContentPane( window );
            dummyFrame.pack();
            gridWindowSize = window.getPreferredSize();
        });
    }
    
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
    
    private static void initGrid2D()
    {
        String  rowsProp    = Constants.NAME_PREFIX + KEY_NUM_ROWS;
        String  colsProp    = Constants.NAME_PREFIX + KEY_NUM_COLS;
        System.setProperty( rowsProp, "" + EXP_NUM_ROWS );
        System.setProperty( colsProp, "" + EXP_NUM_COLS );
        int     actNumRows  = Grid2D.getNumRows();
        int     actNumCols  = Grid2D.getNumCols();
        if ( EXP_NUM_ROWS != actNumRows )
        {
            String  msg = 
                "Invalid num rows; expected: " + EXP_NUM_ROWS
                + " actual: " + actNumRows;
            throw new BattleshipException( msg );
        }
        if ( EXP_NUM_COLS != actNumCols )
        {
            String  msg = 
                "Invalid num cols; expected: " + EXP_NUM_COLS
                + " actual: " + actNumCols;
            throw new BattleshipException( msg );
        }
    }
    
    private static void verifyHorizontalGridlines( GridProperties props )
    {
        // Calculate an x-coordinate halfway into the first cell.
        int     startXco    = props.gridBounds.x + props.cellSide / 2;
        // Calculate an x-coordinate halfway into the last cell.
        int     endXco      = 
            startXco + (props.numCols - 1) * props.cellSide;
        // Y-coordinate of the first horizontal gridline
        int     startYco    = props.gridBounds.y;
        // Y-coordinate of the last horizontal gridline
        int     endYco      = startYco + props.numRows * props.cellSide;
        int     iBGColor    = props.backgroundColor;
        int     iGLColor    = props.gridlineColor;
        for ( int yco = startYco ; yco <= endYco ; yco += props.cellSide )
            for ( int xco = startXco ; xco <= endXco ; xco += props.cellSide )
            {
                int rgbLessOne  = props.gridImage.getRGB( xco, yco - 1 );
                int rgbYco      = props.gridImage.getRGB( xco, yco );
                int rgbPlusOne  = props.gridImage.getRGB( xco, yco + 1 );
                if ( rgbLessOne != iBGColor )
                {
                    String  msg =
                        "Pixel above horizontal gridline, expected "
                        + "background color, was: " + rgbLessOne;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
                if ( rgbYco != iGLColor )
                {
                    String  msg =
                        "Pixel on horizontal gridline, expected "
                        + "gridline color, was: " + rgbYco;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
                if ( rgbPlusOne != iBGColor )
                {
                    String  msg =
                        "Pixel after horizontal gridline, expected "
                        + "background color, was: " + rgbPlusOne;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
            }
    }
    
    private static void verifyVerticalGridlines( GridProperties props )
    {
        // Calculate a y-coordinate halfway into the first cell.
        int     startYco    = props.gridBounds.y + props.cellSide / 2;
        // Calculate a y-coordinate halfway into the last cell.
        int     endYco      = 
            startYco + (props.numRows - 1) * props.cellSide;
        // X-coordinate of the first vertical gridline
        int     startXco    = props.gridBounds.x;
        // X-coordinate of the last horizontal gridline
        int     endXco      = startXco + props.numCols * props.cellSide;
        int     iBGColor    = props.backgroundColor;
        int     iGLColor    = props.gridlineColor;
        for ( int yco = startYco ; yco <= endYco ; yco += props.cellSide )
            for ( int xco = startXco ; xco <= endXco ; xco += props.cellSide )
            {
                int rgbLessOne  = props.gridImage.getRGB( xco - 1, yco );
                int rgbXco      = props.gridImage.getRGB( xco, yco );
                int rgbPlusOne  = props.gridImage.getRGB( xco + 1, yco );
                if ( rgbLessOne != iBGColor )
                {
                    String  msg =
                        "Pixel left of vertical gridline, expected "
                        + "background color, was: " + rgbLessOne;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
                if ( rgbXco != iGLColor )
                {
                    String  msg =
                        "Pixel on vertical gridline, expected "
                        + "gridline color, was: " + rgbXco;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
                if ( rgbPlusOne != iBGColor )
                {
                    String  msg =
                        "Pixel right of vertical gridline, expected "
                        + "background color, was: " + rgbPlusOne;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
            }
    }
    
    private static void verifyCellInterior( GridProperties props )
    {
        // Calculate an x-coordinate halfway into the first cell.
        int     startXco    = props.gridBounds.x + props.cellSide / 2;
        // Calculate an x-coordinate halfway into the last cell.
        int     endXco      = startXco + (props.numCols - 1) * props.cellSide;
        // Calculate a y-coordinate halfway into the first cell.
        int     startYco    = props.gridBounds.y + props.cellSide / 2;
        // Calculate a y-coordinate halfway into the last cell.
        int     endYco      = startYco + (props.numRows - 1) * props.cellSide;
        int     iBGColor    = props.backgroundColor;
        for ( int yco = startYco ; yco <= endYco ; yco += props.cellSide )
            for ( int xco = startXco ; xco <= endXco ; xco += props.cellSide )
            {
                int rgb = props.gridImage.getRGB( xco, yco );
                if ( rgb != iBGColor )
                {
                    String  msg =
                        "Pixel in cell interior, expected "
                        + "background color, was: " + rgb;
                    msg = getCooMsg( xco, yco, msg );
                    throw new BattleshipException( msg );
                }
            }
    }
    
    private static String getCooMsg( int xco, int yco, String msg )
    {
        String  cooMsg  = String.format( "(%d,%d) %s", xco, yco, msg );
        return cooMsg;
    }
    
    private static class GridProperties
    {
        public BufferedImage    gridImage;
        public Rectangle        gridBounds;
        public int              numCols;
        public int              numRows;
        public int              imageWidth;
        public int              imageHeight;
        public int              backgroundColor;
        public int              gridlineColor;
        public int              gridlineWidth;
        public int              cellSide;
        public int              maxCellSide;
        public int              rightMargin;
        
        public GridProperties( BufferedImage gridImage, Grid2D logicalGrid )
        {
            this.gridImage = gridImage;
            gridBounds = new Rectangle();
            imageWidth = gridImage.getWidth();
            imageHeight = gridImage.getHeight();
            numRows = Grid2D.getNumRows();
            numCols = Grid2D.getNumCols();
            maxCellSide = imageWidth / numCols;
            getGridProps();
        }
        
        private void getGridProps()
        {
            int     maxXco      = imageWidth - 1;
            int     minXco      = imageWidth / 2;
            int     centerYco   = imageHeight / 2;
            int     minYco      = centerYco - maxCellSide;
            int     maxYco      = centerYco + maxCellSide;
            int     numLines    = 2 * maxCellSide;
            int     currLine    = 0;
            @SuppressWarnings("unchecked")
            List<Segment>[] segLists = (List<Segment>[]) new List[numLines];
            for ( int yco = minYco ; yco < maxYco ; ++yco )
            {
                segLists[currLine] = new ArrayList<>();
                for ( int xco = maxXco ; xco > minXco ;  )
                {
                    int count       = 0;
                    int saveColor   = gridImage.getRGB( xco--, yco );
                    int nextColor   = saveColor;
                    do 
                    {
                        ++count;
                        nextColor = gridImage.getRGB( xco--, yco );
                    } while ( xco > minXco && saveColor == nextColor );
                    xco++;
                    segLists[currLine].add( new Segment( saveColor, count) );
                }
                currLine++;
            }
            rightMargin = segLists[0].get( 0 ).extent;
            getBackgroundColor( segLists );
            getGridlineProps( segLists );
            getCellSide( segLists );
            getGridYco();
            finishBounds();
        }
        
        private void getBackgroundColor( List<Segment>[] segs )
        {
            // 1. Verify all lines end with the same pattern,
            //    which should be padding using the background color.
            // 2. Set the background color from the first segment of any line.
            Segment baseLine    = segs[0].get( 0 );
            boolean same0       = Arrays.stream( segs )
                .map( s -> s.get( 0 ) )
                .filter( s -> !s.equals( baseLine ) )
                .findAny().isEmpty();
            if ( !same0 ) 
            {
                String  msg = "segment[0] not consistent ";
                throw new BattleshipException( msg );
            }
            backgroundColor = baseLine.color;
        }
        
        private void getGridlineProps( List<Segment>[] segs )
        {
            // Segment[1] of every line will be either a short length
            // of edge color (vertical gridline) or a long length of
            // edge color (horizontal gridline).
            List<Segment>   workingList = new ArrayList<>();
                Arrays.stream( segs )
                    .map( s -> s.get( 1 ) )
                    .filter( s -> !workingList.contains( s ) )
                    .forEach( workingList::add );
            int             size        = workingList.size();
            if ( size != 2 )
            {
                String  msg =
                    "Failed segment[1] filter; expect size = 2 was "
                    + "size = " + size;
                throw new BattleshipException( msg );
            }
            workingList.sort( (s1,s2) -> s1.extent() - s2.extent() );
            Segment seg0    = workingList.getFirst();
            gridlineColor = seg0.color();
            gridlineWidth = seg0.extent();
            if ( gridlineWidth != 1 )
            {
                String  msg =
                    "Expected gridlineWidth = 1, actual: " + gridlineWidth;
                throw new BattleshipException( msg );
            }
        }
        
        private void getCellSide( List<Segment>[] segs )
        {
            // Any line with more than two segments ends with the pattern:
            // empty space | empty space
            //      ^      ^      ^
            //     [2]cell     |  [0]right
            //     interior   [1]  margin
            //             grid line
            // The length of the side of a cell is the width of the
            // cell interior, plus the width of a gridline.
            List<Segment> segList= 
                Arrays.stream( segs )
                    .filter( s -> s.size() > 2 )
                    .findFirst().orElse( null );
            if ( segList == null )
            {
                String  msg = "Failed to find a segment from which "
                    + "to extrapolate cell side length";
                throw new BattleshipException( msg );
            }
            cellSide = segList.get( 2 ).extent + gridlineWidth;
        }
        
        private void getGridYco()
        {
            // look for a line whose middle has many sequential pixels of 
            // gridLine color.
            int     centerXco   = imageWidth / 2;
            int     extent      = cellSide + gridlineWidth + 1;
            int     width       = 2 * extent;
            int     xco         = centerXco - extent;
            int[]   testLine    = new int[2 * extent];
            Arrays.fill( testLine, gridlineColor );
            
            int     first       = IntStream.range( 0,  imageHeight / 2 )
                .map( y -> {
                    int[] arr = 
                        gridImage.getRGB( xco, y, width, 1, null, 0, width );
                    int   yco = Arrays.equals( testLine, arr ) ? y : -1;
                    return yco;
                })
                .filter( y -> y > 0 )
                .findFirst().orElse( -1 );
            if ( first < 0 )
            {
                String  msg     = "First horizontal gridline not found";
                throw new BattleshipException( msg );
            }
            gridBounds.y = first;
        }
        
        private void finishBounds()
        {
            int     lastXco     = imageWidth - rightMargin - 1;
            gridBounds.width = numCols * cellSide;
            gridBounds.x = lastXco - gridBounds.width;
            gridBounds.height = numRows * cellSide;
        }
    }
    
    private record Segment( int color, int extent )
    {
    }
}
