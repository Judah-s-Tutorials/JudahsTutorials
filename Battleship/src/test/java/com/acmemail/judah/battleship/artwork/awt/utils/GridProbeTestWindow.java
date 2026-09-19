package com.acmemail.judah.battleship.artwork.awt.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.artwork.awt.GridWindow;
import com.acmemail.judah.battleship.model.Grid2D;

/**
 * This class was designed to facilitate testing of the GridProbe class.
 * It makes the same kind of physical grid that GridWindow does
 * but eliminates the labels,
 * and exposes all its properties,
 * background color, grid color cell side length, etc.,
 * so that the properties derived by the GridProbe class
 * can be validated against actual values.
 * To use it:
 * <ol>
 *      <li>
 *      Configure the number of rows and columns
 *      in the logical grid;
 *      these be small, unique numbers,
 *      such as 10 rows and 15 columns.
 *      </li>
 *      <li>
 *      Instantiate an realize a GridProbeTest window.
 *      The {@link #getRealizedTestWindow(Grid2D)}
 *      can do this for you.
 *      </li>
 *      <li>
 *      Feed the GridProbeTestWindow
 *      into a GridProbe object.
 *      </li>
 * </ol>
 */
@SuppressWarnings({ "serial" })
class GridProbeTestWindow extends GridWindow
{
    /** 
     * The bounds of the physical grid;
     * does not include labels or padding.
     */
    private final Rectangle bounds          = new Rectangle();
    /** The physical grid's background color. */
    private final Color     backgroundColor = Color.LIGHT_GRAY;
    /** The physical grid's gridline color. */
    private final Color     gridlineColor   = Color.DARK_GRAY;
    /** The physical gridline width. */
    private final int       gridlineWidth   = 1;
    /** The padding on all four sides of the physical grid. */
    private final int       padding         = 10;
    /** The length of the side of a cell. */
    private final int       cellSide        = 20;
    /** The logical grid being shadowed by the physical grid. */
    private final Grid2D    grid2D;
    /**
     * The root of the window hierarchy which contains the test window;
     * may be null.
     */
    private final JFrame    frame;
    
    /**
     * Constructor.
     * Instantiates a GridProbeTestWindow encapsulating
     * the given logical grid. 
     * You may specify null for the logical grid
     * in which case the home grid will be used.
     * If non-null, the frame argument
     * represents the root of the window hierarchy
     * in which the test window will be embedded.
     * It's strictly for bookkeeping,
     * so that the frame can be disposed when this window is disposed.
     * 
     * @param grid2D    
     *      the logical grid to encapsulate in the test window; may be null
     * @param frame 
     *      the root of the window hierarchy containing the test window;
     *      may be null
     */
    public GridProbeTestWindow( Grid2D grid2D, JFrame frame )
    {
        super( grid2D == null ? Grid2D.getHomeGrid() : grid2D );
        this.grid2D = super.getGrid();
        this.frame = frame;
        
        int     numCols     = Grid2D.getNumCols();
        int     numRows     = Grid2D.getNumCols();
        int     gridWidth   = numCols * cellSide;
        int     gridHeight  = numRows * cellSide;
        bounds.setRect( padding, padding, gridWidth, gridHeight );
        
        int     prefWidth   = gridWidth + 2 * padding;
        int     prefHeight  = gridHeight + 2 * padding;
        setPreferredSize( new Dimension( prefWidth, prefHeight ) );
    }
    
    /**
     * Gets a fully realized GridProbeTestWindow.
     * The window will be the content pane of a packed JFrame.
     * If the given logical grid is non-null,
     * it will be used to instantiate the test window,
     * otherwise the home grid will be used.
     * When necessary,
     * operations are performed on the EDT.
     * 
     * @param grid2D    logical grid to encapsulate in test window
     * 
     * @return  the realized test window
     */
    public static GridProbeTestWindow getRealizedTestWindow( Grid2D grid2D )
    {
        Grid2D                  actGrid2D   =
            grid2D == null ? Grid2D.getHomeGrid() : grid2D;
        GridProbeTestWindow[]   result      = new GridProbeTestWindow[1];
        try
        {
            SwingUtilities.invokeAndWait( () -> {
                JFrame  frame   = new JFrame();
                result[0] = new GridProbeTestWindow( actGrid2D, frame );
                frame.setContentPane( result[0] );
                frame.pack();
                frame.setVisible( true );
            });
        }
        catch (InvocationTargetException | InterruptedException exc )
        {
            throw new BattleshipException( "Unexpected exception", exc );
        }
        return result[0];
    }
    
    /**
     * Disposes of all internally allocated resources.
     */
    public void dispose()
    {
        if ( frame != null )
            frame.dispose();
    }
    
    /**
     * Gets the bounds of the physical grid area.
     * 
     * @return the bounds of the physical grid area
     */
    public Rectangle getBounds()
    {
        return bounds;
    }

    /**
     * Gets the background color.
     * 
     * @return the background color
     */
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * Gets the gridline color
     * @return the gridline color
     */
    public Color getGridlineColor()
    {
        return gridlineColor;
    }

    /**
     * Gets the gridline width.
     * 
     * @return the gridline width
     */
    public int getGridlineWidth()
    {
        return gridlineWidth;
    }

    /**
     * Gets the length of the side of a cell.
     * 
     * @return the length of the side of a cell
     */
    public int getCellSide()
    {
        return cellSide;
    }

    /**
     * Returns the encapsulated logical grid.
     * 
     * @return the encapsulated logical grid
     */
    public Grid2D getGrid2D()
    {
        return grid2D;
    }

    @Override
    public void paintComponent( Graphics graphics )
    {
        Graphics2D  gtx = (Graphics2D)graphics;
        gtx.setColor( backgroundColor );
        gtx.fillRect( 0,  0, getWidth(), getHeight() );
        
        gtx.setColor( gridlineColor );
        AffineTransform saveTransform   = gtx.getTransform();
        gtx.translate( padding, padding );
        
        int maxXco  = bounds.width;
        int minXco  = 0;
        int maxYco  = bounds.height;
        int minYco  = 0;
        IntStream
            .rangeClosed( minXco, maxXco )
            .filter( xco -> xco % cellSide == 0 )
            .mapToObj( xco -> new Line2D.Double( xco, minYco, xco, maxYco ) )
            .forEach( gtx::draw );
        IntStream
            .rangeClosed( minYco, maxYco )
            .filter( yco -> yco % cellSide == 0 )
            .mapToObj( yco -> new Line2D.Double( 0, yco, maxXco, yco ) )
            .forEach( gtx::draw );
        gtx.setTransform( saveTransform );
    }
}