package com.acmemail.judah.battleship.artwork.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.JPanel;

import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship2D.Cell2DView;
import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Ship2D;

/**
 * This class manages a graphical implementation
 * of a window to display
 * the grid in a game of Battleship.
 * In addition to the display
 * it provides several different facilities.
 * <ol>
 * <li>
 * <strong>Mouse Clicks</strong><br>
 * When a mouse is clicked on the grid
 * the mouse coordinates are converted to cell coordinates
 * and reported to interested listener.
 * Clients implement a {@link CellListener}
 * and register it using {@link #addCellListener(CellListener)}.
 * A clicks are reported to the listener via a {@link CellEvent}.
 * </li>
 * <li>
 * <strong>Cell Highlighting</strong><br>
 * The client can request that a cell
 * or rectangular array of cells
 * be added to a highlighted-cell list.
 * Every time the grid is repainted
 * all cells in the list
 * are painted a distinctive color.
 * </li>
 * <li>
 * <strong>Scaling</strong><br>
 * The drawing in the window
 * can be scaled at the operator's request.
 * The keyboard shortcuts are 
 * ctrl/+ (scale up), ctrl/- (scale down),
 * and ctrl/0 (return to a 1:1 scale).
 * </li>
 * </ol>
 */
public class GridWindow extends JPanel
{
    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The image to draw over a cell when a ship is hit. */
    private final Image  splat;
    
    /** Maximum scale factor. */
    private static final double     maxScale        = 3;
    /** Minimum scale factor. */
    private static final double     minScale        = .5;
    /** Amount to increment scaling when ctrl/+ or ctrl/- are detected. */
    private static final double     scaleIncrement  = .1;
    
    /** The background color of the window. */
    private static final Color      backgroundColor = Color.LIGHT_GRAY;
    /** The fill color for cells belonging to a ship. */
    private static final Color      shipColor       = Color.DARK_GRAY;
    /** The color of gridlines. */
    private static final Color      gridLineColor   = Color.BLACK;
    /** The fill color of a cell that has been attacked. */
    private static final Color      splatColor      = Color.RED;
    /** The color of the row and column labels. */
    private static final Color      labelColor      = Color.BLACK;
    /** The fill color of a selected (highlighted) cell. */
    private static final Color      selectColor     = new Color( 0x3f009bd6, true );
    /** The length of the side of a cell. */
    private static final int        cellSide        = 20;
    /** Space after the last column of the grid. */
    private static final int        rightPadding    = 10;
    /** Space after the last row of the grid. */
    private static final int        bottomPadding   = 10;
    
    /** Rendering hints to control the quality of drawing. */
    private static final Map<?,?>   renderingHints  =
        Map.of(
            RenderingHints.KEY_RENDERING, 
                RenderingHints.VALUE_RENDER_QUALITY,
            RenderingHints.KEY_STROKE_CONTROL, 
                RenderingHints.VALUE_STROKE_NORMALIZE,
            RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON
        );

    /** List of selected cells. */
    private final List<Rectangle>   selectedCells   = new ArrayList<>();
    /** The logical grid that controls the appearance of this window. */
    private final Grid2D            grid;
    /** The number of rows in the grid. */
    private final int               numRows;
    /** The number of columns in the grid. */
    private final int               numCols;
    /** The physical width of this grid including row labels. */
    private final int               baseWidth;
    /** The physical height of this grid including column labels and padding. */
    private final int               baseHeight;
    /** Encapsulation of the physical bounds of this grid. */
    private final Rectangle         gridBounds;
    /** The font used to draw the row/column labels. */
    private final Font              labelFont;
    
    /** The current scale factor. */
    private double                  scaleFactor     = 1;
    /** The maximum width and height of a row column label. */
    private final Dimension         labelBounds;
    
    /** List of CellListeners. */
    private final List<CellListener>    cellListeners   = new ArrayList<>();
    
    /**
     * Constructor.
     * Fully configures this window.
     * 
     * @param grid  the logical grid that controls this window
     */
    public GridWindow( Grid2D grid )
    {
        this.grid = grid;
        
        // Get the splat image. This task is delegated to a helper method
        // in case adjustments have to be made
        splat = getSplat();
        
        labelFont   = new Font( Font.MONOSPACED, Font.BOLD, 12 );
        labelBounds = 
            getMaxLabelBounds( labelFont, getFontMetrics( labelFont ) );
        
        // Calculate the physical extent of the grid, including labels
        // and padding. Note that column labels are drawn rotated by
        // 90 degrees, so the labels' contribution to the height of the
        // grid is equal to the label width.
        numRows = Grid2D.getNumRows();
        numCols = Grid2D.getNumCols();
        int     gridWidth   = numCols * cellSide;
        int     gridHeight  = numRows * cellSide;
        int     labelWidth  = labelBounds.width;
        gridBounds = 
            new Rectangle( labelWidth, labelWidth, gridWidth, gridHeight );
        baseWidth = gridWidth + labelWidth + rightPadding;
        baseHeight = gridHeight + labelWidth + bottomPadding;

        Dimension   dim     = new Dimension( baseWidth, baseHeight );
        setPreferredSize( dim );
        
        // Configure the key and mouse listeners.
        setFocusable( true );
        addKeyListener( new KeyDetector() );
        addMouseListener( new MouseManager() );
    }
    
    /**
     * Adds a given CellListener to the list of CellListeners.
     * 
     * @param listener  the given CellListener
     */
    public void addCellListener( CellListener listener )
    {
        cellListeners.add( listener );
    }
    
    
    /**
     * Removes a given CellListener from the list of CellListeners.
     * If the listener is not found,
     * the operation is silently ignored.
     * 
     * @param listener  the given CellListener
     */
    public void removeCellListener( CellListener listener )
    {
        cellListeners.remove( listener );
    }
    
    /**
     * Dispatch a given event to all CellListeners.
     * 
     * @param event the given event
     */
    public void dispatchEvent( CellEvent event )
    {
        for ( CellListener listener : cellListeners )
            listener.cellClicked( event );
    }
    
    /**
     * Clear the list of selected cells.
     */
    public void select()
    {
        select( () -> selectedCells.clear() );
    }
    
    /**
     * Select a single cell in the grid.
     * 
     * @param coords    the coordinates of the cell to select
     */
    public void select( GridCoords coords )
    {
        Rectangle   rect    = new Rectangle( coords.xco(), coords.yco(), 1, 1 );
        select( () -> selectedCells.add( rect ) );
    }
    
    /**
     * Remove a cell from the list of selected cells.
     * If the cell is not found it is silently ignored.
     * If a cell has been selected as part of a rectangular array
     * it cannot be removed with this method;
     * it can only be removed be clearing the list,
     * or by removing the originally selected rectangle.
     * 
     * @param coords    the coordinates of the cell to remove
     */
    public void deselect( GridCoords coords )
    {
        Rectangle   rect    = new Rectangle( coords.xco(), coords.yco(), 1, 1 );
        select( () -> selectedCells.remove( rect ) );
    }
    
    /**
     * Adds a rectangular array of cells to the selected cell list.
     * If a cell in the rectangle is already selected,
     * or if it is a member of an overlapping rectangle,
     * it will be select twice.
     * 
     * @param rect  the rectangular array of cells to add
     */
    public void select( Rectangle rect )
    {
        select( () -> selectedCells.add( rect ) );
    }
    
    /**
     * Remove a given rectangular array of cells
     * from the list of selected cells.
     * If the array is not found it is silently ignored.
     * 
     * @param rect  the given rectangular array of cells
     */
    public void deselect( Rectangle rect )
    {
        select ( () -> selectedCells.remove( rect ) );
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        // When the drawing is scaled inside the scrolled component, 
        // the window itself doesn't change size, so the scrolled
        // component doesn't know to adjust its scroll bars. So we
        // override the getPreferredSize method to generate a new size
        // that takes the scale factor into account. In order for this
        // logic to work, every time the scale factor changes, we have
        // to invoke the window's revalidate() method.
        int         scaledWidth     = (int)Math.round( baseWidth * scaleFactor );
        int         scaledHeight    = (int)Math.round( baseHeight * scaleFactor );
        Dimension   preferredSize   = new Dimension( scaledWidth, scaledHeight );
        return preferredSize;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        Graphics2D  gtx     = (Graphics2D)graphics;
        gtx.setRenderingHints( renderingHints );
        gtx.setColor( backgroundColor );
        gtx.fillRect( 0, 0, getWidth(), getHeight() );
        gtx.setFont( labelFont );

        gtx.scale( scaleFactor, scaleFactor );

        // Content geometry is computed from the fixed logical
        // (unscaled) size, then projected onto the panel's
        // actual (scaled) size by the transform above, so the
        // drawing keeps the same proportions at every scale.
        
        paintShips( gtx );
        paintSplats( gtx );
        paintSelected( gtx );
        paintGridLines( gtx );
        paintLabels( gtx );
    }

    /**
     * Paint all the ships in the associated logical grid.
     * 
     * @param gtx   the graphics context for painting
     */
    private void paintShips( Graphics2D gtx )
    {
        Color           saveColor       = gtx.getColor();
        AffineTransform saveTransform   = gtx.getTransform();
        int             translate       = labelBounds.width;
        gtx.translate( translate, translate );
        
        gtx.setColor( shipColor );
        Rectangle   shipRect    = new Rectangle();
        for ( Ship2D ship : grid.getAllShips() )
        {
            Rectangle   bounds  = ship.getBounds();
            shipRect.x = cellSide * bounds.x;
            shipRect.y = cellSide * bounds.y;
            shipRect.width = cellSide * bounds.width;
            shipRect.height = cellSide * bounds.height;
            gtx.fill( shipRect );
        }

        gtx.setTransform( saveTransform );
        gtx.setColor( saveColor );
    }

    /**
     * Paint all the gridlines in the grid.
     * 
     * @param gtx   the graphics context for painting
     */
    private void paintGridLines( Graphics2D gtx )
    {
        Color           saveColor       = gtx.getColor();
        AffineTransform saveTransform   = gtx.getTransform();
        int             translate       = labelBounds.width;
        gtx.translate( translate, translate );
        gtx.setColor( gridLineColor );

        int             gridWidth       = gridBounds.width;
        int             gridHeight      = gridBounds.height;
        IntStream.iterate( 0, i -> i <= gridWidth , i -> i += cellSide )
            .mapToObj( i -> new Line2D.Double( i, 0, i, gridHeight ) )
            .forEach( gtx::draw );
        IntStream.iterate( 0, i -> i <= gridHeight , i -> i += cellSide )
            .mapToObj( i -> new Line2D.Double( 0, i, gridWidth, i ) )
            .forEach( gtx::draw );

        gtx.setTransform( saveTransform );
        gtx.setColor( saveColor );
    }
    
    /**
     * Paint all the splats in the associated logical grid.
     * Cells belonging to a ship
     * are painted with the splat image;
     * cells not belonging to a ship
     * are filled with a discriminating color.
     * 
     * @param gtx   the graphics context for painting
     */
    private void paintSplats( Graphics2D gtx )
    {
        Color           saveColor       = gtx.getColor();
        AffineTransform saveTransform   = gtx.getTransform();
        int             translate       = labelBounds.width;
        gtx.translate( translate, translate );
        gtx.setColor( splatColor );

        grid.getCells()
        .filter( Cell2DView::isSplatted )
        .filter( c -> c.getShip() == null )
            .map ( c -> 
                new Rectangle(
                    c.getCoords().xco() * cellSide + 3,
                    c.getCoords().yco() * cellSide + 3,
                    cellSide - 6,
                    cellSide - 6
                )
            )
            .forEach( gtx::fill );

        grid.getCells()
        .filter( Cell2DView::isSplatted )
        .filter( c -> c.getShip() != null )
            .map ( Cell2DView::getCoords )
            .forEach( gc -> {
                Shape   clipSave = gtx.getClip();
                int xco = gc.xco();
                int yco = gc.yco();
                gtx.setClip( xco, yco, cellSide, cellSide );
                gtx.drawImage( splat, gc.xco(), gc.yco(), this );
                gtx.setClip( clipSave );
            });

        gtx.setTransform( saveTransform );
        gtx.setColor( saveColor );
    }
    
    /**
     * Paint all the cells in the list of selected cells.
     * 
     * @param gtx   the graphics context for painting
     */
    private void paintSelected( Graphics2D gtx )
    {
        Color           saveColor       = gtx.getColor();
        AffineTransform saveTransform   = gtx.getTransform();
        int             translate       = labelBounds.width;
        gtx.translate( translate, translate );
        gtx.setColor( selectColor );
        
        selectedCells.stream()
            .forEach( r -> {
                int xco = r.x * cellSide;
                int yco = r.y * cellSide;
                gtx.fillRect( xco, yco, cellSide, cellSide );
            });

        gtx.setTransform( saveTransform );
        gtx.setColor( saveColor );
    }

    /**
     * Paint all the row and column labels.
     * 
     * @param gtx   the graphics context for painting
     */
    private void paintLabels( Graphics2D gtx )
    {
        Color           saveColor       = gtx.getColor();
        AffineTransform saveTransform   = gtx.getTransform();
        int             translate       = labelBounds.width;
        gtx.translate( translate, translate );
        gtx.setColor( labelColor );

        FontRenderContext   context     = gtx.getFontRenderContext();
        Font                font        = gtx.getFont();
        
        for ( int row = 0 ; row < numRows ; ++row )
        {
            Label       label       = new Label( 0, row );
            String      strLabel    = label.getRowStr();
            Rectangle2D strBounds   = 
                font.getStringBounds( strLabel, context );
            int         iWidth      = (int)strBounds.getWidth();
            int         iHeight     = (int)strBounds.getHeight();
            int         xco         = -iWidth - 5;
            int         yco         = 
                cellSide * (row + 1) - cellSide / 2 + iHeight / 2 - 1;
            gtx.drawString( strLabel, xco, yco );
        }
        
        // The labels on the columns require another modification
        // to the transform, so displaying the column labels should
        // be the last thing we do before restoring the original
        // transform (saveTransform).
        gtx.rotate( Math.PI / 2 );
        for ( int col = 0 ; col < numCols ; ++col )
        {
            Label       label       = new Label( col, 0 );
            String      strLabel    = label.getColStr();
            Rectangle2D strBounds   = 
                font.getStringBounds( strLabel, context );
            int         iWidth      = (int)strBounds.getWidth();
            int         iHeight     = (int)strBounds.getHeight();
            
            // drawing the column labels in the       .
            // rotated context is nearly the same     .
            // as drawing the row labels, except      4 _____
            // the first label (1) has to drawn at    3 _____
            // the bottom of the rotated figure, and  2 _____
            // later positions extend toward the top  1 _____
            int         xco         = -iWidth - 5;
            int         yco         = 
                -cellSide * col - cellSide / 2 + iHeight / 4;
            gtx.drawString( strLabel, xco, yco );
        }
        
        gtx.setTransform( saveTransform );
        gtx.setColor( saveColor );
    }
    
    /**
     * Get the splat to use for painting cells
     * of ships that have been attacked.
     * The parameters of the splat
     * are configured to limit the extents of the image
     * to the physical dimensions of a cell.
     * 
     * @return  the allocated splat
     */
    private Image getSplat()
    {
        double          dCellSide   = cellSide;
        Splat           splat       = new Splat();
        Splat.Params    params      = splat.getParams();
        params.crownRadius = dCellSide / 2;
        splat.setParams( params );
        Image           image       = splat.getImage();
        return image;
    }
    
    /**
     * Helper method to add cells to the selected cell list.
     * The caller provides a Runner that  performs the add,
     * after which the helper method attends
     * to bookkeeping tasks,
     * such as scheduling the window for repainting.
     * 
     * @param runner    the Runner that performs cell selection
     */
    private void select( Runnable runner )
    {
        runner.run();
        repaint();
    }

    /**
     * Calculate the maximum bounds of a 
     * row/column label.
     * 
     * @param font          the font that the labels will be drawn with
     * @param fontMetrics   the metrics associated with the font
     * @return  the maximum bounds of a row/column label
     */
    private static Dimension getMaxLabelBounds( 
        Font font, 
        FontMetrics fontMetrics 
    )
    {
        int         height      = 
            fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
        int         width       = 2 * fontMetrics.getMaxAdvance();
        Dimension   dim         = new Dimension( width, height );
        return dim;
    }
    
    /**
     * An instance of this class
     * listens for keystrokes in the grid window.
     * If the keystroke corresponds with a shortcut,
     * such as shortcut to increase the scale-up the window,
     * the shortcut is executed, 
     * otherwise the keystroke is silently ignored.
     */
    private class KeyDetector extends KeyAdapter
    {
        /**
         * Default constructor; not used.
         */
        public KeyDetector()
        {
            // not used
        }
        
        @Override
        public void keyPressed( KeyEvent evt )
        {
            if ( evt.isControlDown() )
            {
                boolean repaint = true;
                int     key     = evt.getKeyCode();
                
                if ( isPlus( key ) )
                {
                    double  testFactor  = scaleFactor + scaleIncrement;
                    scaleFactor = Math.min( testFactor, maxScale );
                }
                else if ( isMinus( key ) )
                {
                    double  testFactor  = scaleFactor - scaleIncrement;
                    scaleFactor = Math.max( testFactor, minScale );
                }
                else if ( isZero( key ) )
                {
                    scaleFactor = 1;
                }
                else
                {
                    repaint = false;
                }
                if ( repaint )
                {
                    revalidate();
                    repaint();
                }
            }
            else
                System.out.println( "not ^" );
        }
        
        /**
         * Determine if a given key corresponds a key
         * displaying a plus sign (+).
         * 
         * @param key   the given key 
         * 
         * @return  true if the key displays a plus sign.
         */
        private static boolean isPlus( int key )
        {
            boolean result  =
                key == KeyEvent.VK_PLUS || 
                key == KeyEvent.VK_EQUALS;
            return result;
        }
        
        /**
         * Determine if a given key corresponds a key
         * displaying a minus sign (-).
         * 
         * @param key   the given key 
         * 
         * @return  true if the key displays a minus sign.
         */
        private static boolean isMinus( int key )
        {
            boolean result  =
                key == KeyEvent.VK_MINUS || 
                key == KeyEvent.VK_UNDERSCORE;
            return result;
        }
        
        /**
         * Determine if a given key corresponds a key
         * displaying the digit zero (0).
         * 
         * @param key   the given key 
         * 
         * @return  true if the key displays a zero.
         */
        private static boolean isZero( int key )
        {
            boolean result  =
                key == KeyEvent.VK_0;
            return result;
        }
    }
    
    /**
     * An instance of this class
     * listens for mouse events in the grid window.
     * If the event corresponds to a button-1 click
     * within the rectangle of grid cells,
     * a CellEvent is dispatched to all CellListeners.
     */
    private class MouseManager extends MouseAdapter
    {
        /**
         * Default constructor; not used.
         */
        public MouseManager()
        {
            // not used
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            boolean     dispatch    = false;
            GridCoords  coords  = null;
            int     mouseXco    = evt.getX();
            int     mouseYco    = evt.getY();
            if ( gridBounds.contains( mouseXco, mouseYco ) )
            {
                coords = deriveCellCoords( mouseXco, mouseYco );
                dispatch = true;
            }
            
            // Dispatch the event upwards into the window hierarchy.
            GridWindow.this.getParent().dispatchEvent( evt );
            if ( dispatch )
            {
                CellEvent   event   = new CellEvent( coords, evt );
                dispatchEvent( event );
                repaint();
            }
        }
        
        /**
         * Translates the pixel coordinates of a mouse click
         * into cell coordinates.
         * 
         * @param mouseXco  the x-coordinate of the mouse click
         * @param mouseYco  the y-coordinate of the mouse click
         * 
         * @return  the translated coordinates
         */
        private GridCoords deriveCellCoords( int mouseXco, int mouseYco )
        {
            int         scaledCellSide      = 
                (int)Math.round( cellSide * scaleFactor );
            int         scaledLabelFactor   = 
                (int)Math.round( labelBounds.width * scaleFactor );
            int         cellXco             = 
                (mouseXco - scaledLabelFactor) / scaledCellSide;
            int         cellYco             = 
                (mouseYco - scaledLabelFactor) / scaledCellSide;
            GridCoords  coords  = new GridCoords( cellXco, cellYco );
            return coords;
        }
    }
}
