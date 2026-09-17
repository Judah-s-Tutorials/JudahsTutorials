package com.acmemail.judah.battleship.artwork.awt;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.acmemail.judah.battleship.artwork.awt.utils.GridProbe;
import com.acmemail.judah.battleship.artwork.awt.utils.TestUtils;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;

/**
 * Instantiates, configures, and provides access to a GridWindow.
 * Where necessary, operations are performed on the EDT.
 */
public class GridWindowProps
{   
    /** Application frame in which to embed the encapsulated GridWindow. */
    private final JFrame        dummyFrame;
    
    /** The encapsulated GridWindow. */
    private final GridWindow    gridWindow;
    /** The grid encapsulated in the GridWindow. */
    private final Grid2D        grid;
    /** Probe to characterize encapsulated GridWindow. */
    private final GridProbe     probe;
    
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
        probe = GridProbe.getProbe( gridWindow );
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

    public MouseEvent getMouseClickEvent( Point point )
    {
        MouseEvent  clickEvent  = 
            getMouseClickEvent( gridWindow, point.x, point.y );
        return clickEvent;
    }

    public MouseEvent getMouseClickEvent( int xco, int yco )
    {
        MouseEvent  clickEvent  = getMouseClickEvent( gridWindow, xco, yco );
        return clickEvent;
    }
    
    public void setVisible( boolean visible )
    {
        TestUtils.invokeAndWait( () -> dummyFrame.setVisible( visible ) );
    }
    
    public boolean requestFocus()
    {
        boolean result  = gridWindow.requestFocusInWindow();
        return result;
    }
    
    public MouseEvent getMouseClickEvent( GridCoords coords )
    {
        Rectangle   scaledBounds    = getCellBounds( coords );
        int         xco             = 
            (int)Math.round( scaledBounds.getCenterX() );
        int         yco             =
            (int)Math.round( scaledBounds.getCenterY() );
        MouseEvent  clickEvent      = 
            getMouseClickEvent( gridWindow, xco, yco );
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

    public Rectangle getGridBounds()
    {
        Rectangle   gridBounds  = probe.getGridBounds();
        return gridBounds;
    }
    
    public Rectangle getCellBounds( GridCoords coords )
    {
        int         col                 = coords.xco();
        int         row                 = coords.yco();
        double      scale               = getScale();
        double      scaledSide          = probe.getCellSide() * scale;
        Rectangle   scaledGridBounds    = gridWindow.getScaledGridBounds();
        int         scaledXco           = 
            (int)Math.round( scaledGridBounds.getX() + col * scaledSide );
        int         scaledYco           = 
            (int)Math.round( scaledGridBounds.getY() + row * scaledSide );
        int         scaledDim           = (int)Math.round( scaledSide );
        Rectangle   scaledCellBounds    =
            new Rectangle( scaledXco, scaledYco, scaledDim, scaledDim );
        return scaledCellBounds;
    }
    
    public void addCellListener( CellListener listener )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.addCellListener( listener )
        );
    }
    
    public void removeCellListener( CellListener listener )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.removeCellListener( listener )
        );
    }
    
    public void dispatchEvent( AWTEvent event )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.dispatchEvent( event )
        );
    }
    
    public void dispatchCellEvent( CellEvent event )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.dispatchEvent( event )
        );
    }
    
    public void setScale( double scale )
    {
        TestUtils.invokeAndWait( () -> gridWindow.setScale( scale ) );
    }
    
    public double getScale()
    {
        double  scale   =
            TestUtils.invokeAndWaitGet( () -> gridWindow.getScale() );
        return scale;
    }

    /**
     * Gets a GridProbe describing the encapsulated GridWindow.
     * 
     * @return a GridProbe describing the encapsulated GridWindow
     */
    public GridProbe getProbe()
    {
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
     * Get the encapsulated GridWindow's background color.
     * This is the color that GridProbe discovered 
     * when this object was instantiated.
     * 
     * @return  the encapsulated GridWindow's background color
     */
    public int getBackgroundColor()
    {
        return probe.getBackgroundColor();
    }
    
    /**
     * Get the encapsulated GridWindow's gridline color.
     * This is the color that GridProbe discovered 
     * when this object was instantiated.
     * 
     * @return  the encapsulated GridWindow's gridline color
     */
    public int getGridlineColor()
    {
        return probe.getBackgroundColor();
    }
    
    /**
     * Invokes {GridWindow{@link #select()}
     * on the EDT
     * using the given coordinates.
     * 
     * @param coords    the given coordinates
     */
    public void select()
    {
        TestUtils.invokeAndWait( () -> gridWindow.select() );
    }
    
    /**
     * Invokes {GridWindow{@link #select(GridCoords)}
     * on the EDT
     * using the given coordinates.
     * 
     * @param coords    the given coordinates
     */
    public void select( GridCoords coords )
    {
        TestUtils.invokeAndWait( () -> gridWindow.select( coords ) );
    }
    
    /**
     * Invokes {@link GridWindow #select(Rectangle)}
     * on the EDT
     * using the given rectangle.
     * 
     * @param coords    the given rectangle
     */
    public void select( Rectangle rect )
    {
        TestUtils.invokeAndWait( () -> gridWindow.select( rect ) );
    }
    
    /**
     * Invokes {GridWindow{@link #deselect(GridCoords)}
     * on the EDT
     * using the given coordinates.
     * 
     * @param coords    the given coordinates
     */
    public void deselect( GridCoords coords )
    {
        TestUtils.invokeAndWait( () -> gridWindow.deselect( coords ) );
    }
    
    /**
     * Invokes {GridWindow{@link #deselect(Rectangle)}
     * on the EDT
     * using the given rectangle.
     * 
     * @param rect    the given rectangle
     */
    public void deselect( Rectangle rect )
    {
        TestUtils.invokeAndWait( () -> gridWindow.deselect( rect ) );
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