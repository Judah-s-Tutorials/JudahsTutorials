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
 * Instantiates, configures, and provides access to
 * a GridWindow and its properties.
 * A GridProbe object encapsulating the GridWindow
 * is created and reflects the GridWindow's discovered properties
 * at the time of its creation.
 * Auxiliary services are provided, such as constructing event objects
 * that encode mouse clicks.
 * Where necessary, operations are performed on the EDT.
 */
public class GridWindowProps
{   
    /** Application frame in which to embed the encapsulated GridWindow. */
    private final JFrame        appFrame;
    
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
     *      the Grid2D needed to instantiate
     *      the encapsulated GridWindow.
     */
    private GridWindowProps( Grid2D grid )
    {
        this.grid = grid;
        gridWindow = new GridWindow( grid );
        appFrame = new JFrame();
        appFrame.setContentPane( gridWindow );
        appFrame.pack();
        probe = GridProbe.getProbe( gridWindow );
    }
    
    /**
     * Instantiates a GridWindowProps incorporating 
     * the grid allocated by {@link Grid2D#getHomeGrid()}.
     * Instantiation explicitly occurs on the EDT.
     * The encapsulated GridWindow is automatically created.
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
        TestUtils.invokeAndWait( () -> appFrame.dispose() );
    }
    
    /**
     * Gets the frame that hosts the GridWindow.
     * 
     * @return the frame that hosts the GridWindow
     */
    public JFrame getFrame()
    {
        return appFrame;
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
     * Gets the Grid2D encapsulated in the GridWindow.
     * 
     * @return the grid encapsulated in the GridWindow
     */
    public Grid2D getGrid()
    {
        return grid;
    }

    /**
     * Construct a mouse-click event at the given coordinates,
     * and using the encapsulated GridWindow as a source.
     * 
     * @param point the given coordinates
     * 
     * @return  the constructed mouse-click event
     */
    public MouseEvent getMouseClickEvent( Point point )
    {
        MouseEvent  clickEvent  = 
            getMouseClickEvent( gridWindow, point.x, point.y );
        return clickEvent;
    }

    /**
     * Construct a mouse-click event at the given coordinates,
     * and using the encapsulated GridWindow as a source.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  the constructed mouse-click event
     */
    public MouseEvent getMouseClickEvent( int xco, int yco )
    {
        MouseEvent  clickEvent  = getMouseClickEvent( gridWindow, xco, yco );
        return clickEvent;
    }
    
    /** 
     * Set the visibility of the application frame
     * containing the encapsulated GridWindow.
     * 
     * @param visible   the desired visibility state
     */
    public void setVisible( boolean visible )
    {
        TestUtils.invokeAndWait( () -> appFrame.setVisible( visible ) );
    }
    
    /**
     * Execute the GridWindow's requestFocusInWindow method.
     * 
     * @return  the result returned by requestFocusInWindow
     */
    public boolean requestFocus()
    {
        boolean result  = 
            TestUtils.invokeAndWaitGet( () -> 
                gridWindow.requestFocusInWindow() 
            );
        return result;
    }
    
    /**
     * Convert the given cell coordinates to pixel coordinates
     * and incorporate the pixel coordinates
     * in a mouse-click event object.
     * The current scale of the encapsulated GridWindow
     * is used in calculating the pixel coordinates.
     * The encapsulated GridWindow is used as the source.
     * 
     * @param coords    the given cell coordinates
     * 
     * @return  the instantiated mouse-click event object
     */
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
    
    /**
     * Constructs a mouse-click event object
     * using the given source and pixel coordinates.
     * 
     * @param source    the given source
     * @param xco       the given x-coordinate
     * @param yco       the given y-coordinate
     * 
     * @return  the constructed event object
     */
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
     * Gets the grid bounds as determined by the GridProbe object
     * that was created when the encapsulated GridWindow
     * was instantiated.
     * Scaling is not used in the bounds calculation.
     * 
     * @return  the bounds of the encapsulated GridWindow
     */
    public Rectangle getGridBounds()
    {
        Rectangle   gridBounds  = probe.getGridBounds();
        return gridBounds;
    }
    
    /**
     * Gets the bounds of the cell at the given coordinates;
     * scaling is accounted for.
     *
     * @param coords    the given cell coordinates
     *
     * @return  the bounds of the cell at the given coordinates
     *
     * @throws IndexOutOfBoundsException
     *      if the given coordinates fall outside the logical grid
     */
    public Rectangle getCellBounds( GridCoords coords )
    {
        double      scale               = getScale();
        Rectangle   scaledGridBounds    = getScaledGridBounds();
        Rectangle   scaledCellBounds    =
            getCellBounds( coords, scale, scaledGridBounds );
        return scaledCellBounds;
    }

    /**
     * Gets the bounds of the cell at the given coordinates,
     * given a previously obtained scale and scaled grid bounds;
     * allows repeated calls (as when computing an entire raster)
     * to avoid repeatedly fetching those values from the EDT.
     *
     * @param coords            the given cell coordinates
     * @param scale             the previously obtained scale
     * @param scaledGridBounds  the previously obtained scaled grid bounds
     *
     * @return  the bounds of the cell at the given coordinates
     *
     * @throws IndexOutOfBoundsException
     *      if the given coordinates fall outside the logical grid
     */
    private Rectangle getCellBounds(
        GridCoords coords, double scale, Rectangle scaledGridBounds
    )
    {
        int         col                 = coords.xco();
        int         row                 = coords.yco();
        if ( col < 0 || col >= Grid2D.getNumCols() )
            throw new IndexOutOfBoundsException( "col: " + col );
        if ( row < 0 || row >= Grid2D.getNumRows() )
            throw new IndexOutOfBoundsException( "row: " + row );
        double      scaledSide          = probe.getCellSide() * scale;
        int         scaledXco           =
            (int)Math.round( scaledGridBounds.getX() + col * scaledSide );
        int         scaledYco           =
            (int)Math.round( scaledGridBounds.getY() + row * scaledSide );
        int         scaledDim           = (int)Math.round( scaledSide );
        Rectangle   scaledCellBounds    =
            new Rectangle( scaledXco, scaledYco, scaledDim, scaledDim );
        return scaledCellBounds;
    }

    /**
     * Gets the bounds of the grid encapsulated in the GridWindow,
     * adjusted to reflect the current scale factor.
     * Executed on the EDT.
     *
     * @return  the scaled bounds of the encapsulated GridWindow's grid
     */
    private Rectangle getScaledGridBounds()
    {
        Rectangle   scaledGridBounds    =
            TestUtils.invokeAndWaitGet(
                () -> gridWindow.getScaledGridBounds()
            );
        return scaledGridBounds;
    }
    
    /**
     * Adds the given CellListener
     * to the encapsulated GridWindow.
     * 
     * @param listener  the given CellListener
     */
    public void addCellListener( CellListener listener )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.addCellListener( listener )
        );
    }
    
    /**
     * Removes the given CellListener
     * from the encapsulated GridWindow.
     * 
     * @param listener  the given CellListener
     */
    public void removeCellListener( CellListener listener )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.removeCellListener( listener )
        );
    }
    
    /**
     * Dispatches the given event
     * via the encapsulated GridWindow's 
     * dispatchEvent(AWTEvent) method.
     * 
     * @param event the given event
     */
    public void dispatchEvent( AWTEvent event )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.dispatchEvent( event )
        );
    }
    
    /**
     * Dispatches the given CellEvent
     * via the encapsulated GridWindow's 
     * dispatchEvent(CellEvent) method.
     * 
     * @param event the given event
     */
    public void dispatchCellEvent( CellEvent event )
    {
        TestUtils.invokeAndWait( 
            () -> gridWindow.dispatchEvent( event )
        );
    }
    
    /**
     * Sets the GridWindow's scale to the given value.
     * 
     * @param scale the given value
     */
    public void setScale( double scale )
    {
        TestUtils.invokeAndWait( () -> gridWindow.setScale( scale ) );
    }
    
    /**
     * Gets the GridWindow's current scale value.
     * 
     * @return  the GridWindow's current scale value
     */
    public double getScale()
    {
        double  scale   =
            TestUtils.invokeAndWaitGet( () -> gridWindow.getScale() );
        return scale;
    }

    /**
     * Gets the GridProbe describing the encapsulated GridWindow.
     * 
     * @return the GridProbe describing the encapsulated GridWindow
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
        Rectangle   rect        = getCellBounds( new GridCoords( col, row ) );
        int         centerXco   = (int)rect.getCenterX();
        int         centerYco   = (int)rect.getCenterY();
        int         color       = raster[centerYco][centerXco];
        return color;
    }
    
    /**
     * Gets a raster representing the color of each cell
     * in the GridWindow's grid.
     * The raster is addressed using the row and column index
     * of each cell.
     * 
     * @return  
     *      a raster representing the color of each cell
     *      in the GridWindow's grid
     */
    public int[][] getCellRaster()
    {
        getProbe();
        int         rows                = Grid2D.getNumRows();
        int         cols                = Grid2D.getNumCols();
        int[][]     raster              = getRaster();
        int[][]     cells               = new int[rows][cols];
        double      scale               = getScale();
        Rectangle   scaledGridBounds    = getScaledGridBounds();
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
            {
                GridCoords  coords      = new GridCoords( col, row );
                Rectangle   rect        =
                    getCellBounds( coords, scale, scaledGridBounds );
                int         centerXco   = (int)rect.getCenterX();
                int         centerYco   = (int)rect.getCenterY();
                int         color       = raster[centerYco][centerXco];
                cells[row][col] = color;
            }
        return cells;
    }
    
    /**
     * Create a fresh image of the encapsulated GridWindow.
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
     * This is the color that the GridProbe discovered 
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
     * This is the color that the GridProbe discovered 
     * when this object was instantiated.
     * 
     * @return  the encapsulated GridWindow's gridline color
     */
    public int getGridlineColor()
    {
        return probe.getBackgroundColor();
    }
    
    /**
     * Invokes {GridWindow{@link #select()}.
     * using the given coordinates.
     */
    public void select()
    {
        TestUtils.invokeAndWait( () -> gridWindow.select() );
    }
    
    /**
     * Invokes {GridWindow{@link #select(GridCoords)}
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
     * using the given rectangle.
     * 
     * @param rect    the given rectangle
     */
    public void select( Rectangle rect )
    {
        TestUtils.invokeAndWait( () -> gridWindow.select( rect ) );
    }
    
    /**
     * Invokes {GridWindow{@link #deselect(GridCoords)}
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
     * @param window    the given GridWindow
     * 
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