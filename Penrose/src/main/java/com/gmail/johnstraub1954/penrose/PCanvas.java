package com.gmail.johnstraub1954.penrose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.JPanel;

import com.gmail.johnstraub1954.penrose.utils.FileManager;
import com.gmail.johnstraub1954.penrose.utils.SelectionListener;
import com.gmail.johnstraub1954.penrose.utils.SelectionManager;

public class PCanvas extends JPanel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static PCanvas          defaultCanvas   = null;
    private final SelectionManager  selectionMgr    = new SelectionManager();

    /** Start point of a drag operation. */
    private Point2D     dragFrom    = null;
    /** Start point of a drag operation. */
    private Point2D     dragTo      = null;
    /** Rectangle describing a rubber band operation. */
    private Rectangle2D rubberBand  = new Rectangle2D.Double();
    
    private transient boolean       showGrid    = false;
    private transient int           gridSpacing = 100;;
    private transient Graphics2D    gtx;
    private transient int           width;
    private transient int           height;
    
    private transient BiConsumer<Graphics2D,SelectionManager>   tweaker = null;
    
    public static PCanvas getDefaultCanvas()
    {
        return getDefaultCanvas( 500, 500 );
    }
    
    public static PCanvas getDefaultCanvas( int width, int height )
    {
        if ( defaultCanvas == null )
            defaultCanvas = new PCanvas( width, height );
        return defaultCanvas;
    }
    
    private PCanvas( int width, int height )
    {
        setPreferredSize( new Dimension( width, height ) );
        setFocusable( true );
        MListener   mListener   = new MListener();
        KListener   kListener   = new KListener();
        addMouseListener( mListener );
        addMouseMotionListener( mListener );
        addKeyListener( kListener );
    }
    
    public void setTweaker( BiConsumer<Graphics2D,SelectionManager> tweaker )
    {
        this.tweaker = tweaker;
    }
    
    public void addShape( PShape shape )
    {
        addShape( shape, false, false );
    }
    
    public void addShape( PShape shape, boolean center, boolean select )
    {
        selectionMgr.add( shape );
        if ( center )
        {
            int         width   = getWidth();
            int         height  = getHeight();
            Rectangle2D rect    = shape.getBounds();    
            double      xco     = width / 2 - rect.getWidth() / 2;
            double      yco     = height / 2 - rect.getHeight() / 2;
            shape.moveTo( xco, yco );
        }
        if ( select )
        {
            deselect();
            select( shape,0 );
        }
        repaint();
    }
    
    public void removeShape( PShape shape )
    {
        selectionMgr.remove( shape );
    }
    
    public void deselect()
    {
        selectionMgr.deselect();
    }
    
    public void deselect( PShape shape )
    {
        selectionMgr.deselect( shape );
    }
    
    public void select( PShape shape, int direction )
    {
        selectionMgr.select( shape, direction );
    }
    
    public void select( double xco, double yco )
    {
        List<PShape>    shapes      = selectionMgr.getShapes();
        List<PShape>    selected    = selectionMgr.getSelected();
        shapes.stream()
            .filter( s -> s.contains( xco, yco ) )
            .limit( 1 )
            .forEach( s -> {
                if ( selected.contains( s ) )
                    deselect( s );
                else
                    select( s, 0 );
            });
        repaint();
    }
    
    public void delete( PShape shape )
    {
        selectionMgr.remove( shape );
    }
    
    public void deleteSelected()
    {
        List<PShape>    selected    = getSelected();
        while ( !selected.isEmpty() )
            delete( selected.get( 0 ) );
        repaint();
    }
    
    /**
     * Convenience method for calling SelectionManager.clearSelected().
     * 
     * @see SelectionManager#toggleSelectAll()
     */
    public void clearSelected()
    {
        selectionMgr.clearSelected();
        repaint();
    }
    
    /**
     * Convenience method for calling SelectionManager.toggleSelectAll.
     * 
     * @see SelectionManager#toggleSelectAll()
     */
    public void toggleSelectAll()
    {
        selectionMgr.toggleSelectAll();
        repaint();
    }
    
    public SelectionManager getSelectionManager()
    {
        return selectionMgr;
    }
    
    public void addSelectionListener( SelectionListener listener )
    {
        selectionMgr.addSelectionListener( listener );
    }
    
    public void removeSelectionListener( SelectionListener listener )
    {
        selectionMgr.removeSelectionListener( listener );
    }
    
    public void showGrid( boolean show )
    {
        showGrid = show;
        repaint();
    }
    
    public boolean isShowGrid()
    {
        return showGrid;
    }
    
    public int getGridSpacing()
    {
        return gridSpacing;
    }
    
    public void setGridSpacing( int spacing )
    {
        gridSpacing = spacing;
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        List<PShape>    shapes      = selectionMgr.getShapes();
        List<PShape>    selected    = selectionMgr.getSelected();

        gtx = (Graphics2D)graphics.create();
        width = getWidth();
        height = getHeight();
        
        gtx.setRenderingHint( 
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        gtx.setColor( new Color( 200, 200, 200 ) );
        gtx.fillRect( 0, 0, width, height );
        shapes.forEach( s -> s.render( gtx ) );
        selected.forEach( s -> s.highlight( gtx ) );
        selected.stream().limit( 2 ).forEach( s ->  s.highlightEdge( gtx ) );
        
        if ( dragFrom != null && dragTo != null )
        {
            gtx.setColor( Color.BLACK );
            gtx.draw( rubberBand );
        }
        
        if (showGrid )
            drawGrid();
        
        if ( tweaker != null )
            tweaker.accept( gtx, selectionMgr );

        this.grabFocus();
        gtx.dispose();
    }
    
    public List<PShape> getSelected()
    {
        return selectionMgr.getSelected();
    }
    
    public List<PShape> getShapes()
    {
        return selectionMgr.getShapes();
    }
    
    public boolean rotate( double radians )
    {
        boolean         eventConsumed  = false;
        List<PShape>    selected        = selectionMgr.getSelected();
        if ( selected.size() > 0 )
        {
            PShape  shape   = selected.get( 0 );
            shape.rotate( radians);
            selectionMgr.testMapping();
            eventConsumed = true;
            repaint();
        }
        return eventConsumed;
    }
    
    public boolean consumeSnap()
    {
        boolean eventConsumed   = selectionMgr.snapTo();
        if ( eventConsumed )
            repaint();
        return eventConsumed;
    }
    
    public void move( double deltaX, double deltaY )
    {
        getSelected().forEach( s -> s.move( deltaX, deltaY ) );
        repaint();
    }
    
    public boolean selectSource( int direction )
    {
        boolean         eventConsumed   = false;
        List<PShape>    selected        = getSelected();
        int             size            = selected.size();
        if ( size > 0 )
        {
            select( selected.get( 0 ), direction );
            eventConsumed = true;
        }
        if ( eventConsumed )
            repaint();
        return eventConsumed;
    }
    
    public boolean selectDestination( int direction )
    {
        boolean         eventConsumed   = false;
        List<PShape>    selected        = getSelected();
        int             size            = selected.size();
        if ( size > 1 )
        {
            select( selected.get( 1 ), direction );
            eventConsumed = true;
        }
        if ( eventConsumed )
            repaint();
        return eventConsumed;
    }
    
    private class KListener extends KeyAdapter
    {
        @Override
        public void keyPressed( KeyEvent evt )
        {
            int keyCode = evt.getKeyCode();
            if ( consumeSnapEvent( evt ) )
                ;
            else if ( evt.isControlDown() )
            {
                double  angle   = PShape.D18;
                switch ( keyCode )
                {
                case KeyEvent.VK_LEFT:
                    rotate( -angle );
                    repaint();
                    break;
                case KeyEvent.VK_RIGHT:
                    rotate( angle );
                    repaint();
                    break;
                case KeyEvent.VK_K:
                    addShape( new PKite(), true, true );
                    repaint();
                    break;
                case KeyEvent.VK_D:
                    addShape( new PDart(), true, true );
                    repaint();
                    break;
                case KeyEvent.VK_A:
                    toggleSelectAll();
                    repaint();
                    break;
                case KeyEvent.VK_S:
                    if ( evt.isShiftDown() )
                        FileManager.save();
                    else
                        FileManager.saveAs();
                    repaint();
                    break;
                case KeyEvent.VK_O:
                    FileManager.open();
                    repaint();
                    break;
                }
            }
            else
            {
                switch ( keyCode )
                {
                case KeyEvent.VK_LEFT:
                    move( -1d, 0d );
                    break;
                case KeyEvent.VK_RIGHT:
                    move( 1d, 0d );
                    break;
                case KeyEvent.VK_UP:
                    move( 0d, -1d );
                    break;
                case KeyEvent.VK_DOWN:
                    move( 0d, 1d );
                    break;
                case KeyEvent.VK_DELETE:
                    deleteSelected();
                    break;
                }
            }
        }
        
        /**
         * If possible,
         * process a key event that may be associated with snapping.
         * All such events require the control and alt keys to be pressed.
         * The specific event may or may not
         * correspond to a valid snap operation.
         * 
         * @param evt   the key event to process
         * 
         * @return  true if the event is consumed
         */
        public boolean consumeSnapEvent( KeyEvent evt )
        {
            boolean eventConsumed   = false;
            if ( evt.isControlDown() && evt.isAltDown() )
            {
                int             keyCode     = evt.getKeyCode();
                switch ( keyCode )
                {
                case KeyEvent.VK_RIGHT:
                    if ( evt.isShiftDown() )
                        eventConsumed = selectDestination( 1 );
                    else
                        eventConsumed = selectSource( 1 );
                    break;
                case KeyEvent.VK_LEFT:
                    if ( evt.isShiftDown() )
                        eventConsumed = selectDestination( -1 );
                    else
                        eventConsumed = selectSource( -1 );
                    break;
                case KeyEvent.VK_S:
                    if ( evt.isShiftDown() )
                        eventConsumed = testIntersection();
                    else
                        eventConsumed = consumeSnap();
                    break;
                }
            }
            return eventConsumed;
        }
        
        private boolean testIntersection()
        {
            List<PShape>    selectedList    = selectionMgr.getSelected();
            if ( !selectedList.isEmpty() )
            {
                PShape  selectedShape   = selectedList.get( 0 );
                for ( PShape shape : selectionMgr.getShapes() )
                {
                    if ( shape != selectedShape )
                    {
                        if ( selectedShape.intersects( shape.getBounds() ) )
                            selectionMgr.select( shape );
                    }
                }
            }
            repaint();
            return true;
        }
    }
    
    private void readObject(ObjectInputStream in) 
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        MListener   mListener   = new MListener();
        KListener   kListener   = new KListener();
        addMouseListener( mListener );
        addMouseMotionListener( mListener );
        addKeyListener( kListener );
    }
    
    private void drawGrid()
    {
        gtx.setStroke( new BasicStroke( 1 ) );
        for ( int inx = gridSpacing ; inx < width ; inx += gridSpacing )
        {
            Color   color   = inx % 100 == 0 ? Color.RED : Color.BLACK;
            gtx.setColor( color );
            gtx.drawLine( inx, 0, inx, height );
        }
        for ( int inx = gridSpacing ; inx < height ; inx += gridSpacing )
        {
            Color   color   = inx % 100 == 0 ? Color.RED : Color.BLACK;
            gtx.setColor( color );
            gtx.drawLine( 0, inx, width, inx );
        }
    }

    private class MListener extends MouseAdapter implements Serializable
    {
        private static final long serialVersionUID = -8780588853585090448L;

        @Override
        public void mousePressed( MouseEvent evt )
        {
            requestFocusInWindow();
            if ( evt.getButton() == 1 )
            {
                int xco = evt.getX();
                int yco = evt.getY();
                dragFrom = new Point2D.Double( xco, yco );
            }
        }
        
        @Override
        public void mouseReleased( MouseEvent evt )
        {
            List<PShape>    selected    = selectionMgr.getSelected();
            List<PShape>    shapes      = selectionMgr.getShapes();
            if ( evt.getButton() == 1 && dragFrom != null )
            {
                // Determine if this is the completion of a drag operation.
                // If drag is in progress and the selected list is
                // is non-empty, the drag was performing a move operation,
                // which is handled entirely by the mouseDragged method.
                // If the selected list is empty, the drag was a 
                // rubber-banding operation which must no be completed by
                // selecting all shapes within the rubber band.
                if ( selected.isEmpty() )
                {
                    double      startX  = dragFrom.getX();
                    double      startY  = dragFrom.getY();
                    double      endX    = evt.getX();
                    double      endY    = evt.getY();
                    double      width   = endX - dragFrom.getX();
                    double      height  = endY - dragFrom.getY();
                    if ( width < 0 )
                    {
                        double  temp    = startX;
                        startX = endX;
                        endX = temp;
                        width = -width;
                    }
                    if ( height < 0 )
                    {
                        double  temp    = startY;
                        startY = endY;
                        endY = temp;
                        height = - height;
                    }
                    Rectangle2D rect    = 
                        new Rectangle2D.Double( 
                            startX,
                            startY,
                            width,
                            height
                        );
                    shapes.stream()
                        .filter( s -> s.intersects( rect ) )
                        .forEach( selected::add );
                }
                dragFrom = null;
                dragTo = null;
                repaint();
            }
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            if ( evt.getButton() == 1 )
            {
                if ( evt.isControlDown() )
                    snap( evt );
                else 
                {
                    if ( !evt.isShiftDown() )
                        clearSelected();
                    int xco = evt.getX();
                    int yco = evt.getY();
                    select( xco, yco );
                }
            }
        }
        
        @Override
        public void mouseDragged( MouseEvent evt )
        {
            if ( dragFrom != null )
            {
                List<PShape>    selected    = selectionMgr.getSelected();
                double  newXco  = evt.getX();
                double  newYco  = evt.getY();
                double  deltaX  = newXco - dragFrom.getX();
                double  deltaY  = newYco - dragFrom.getY();
                if ( !selected.isEmpty() )
                {
                    selected.forEach( s -> s.move( deltaX, deltaY ) );
                    dragFrom.setLocation( newXco, newYco );
                }
                else
                {
                    double      startX  = dragFrom.getX();
                    double      startY  = dragFrom.getY();
                    double      endX    = evt.getX();
                    double      endY    = evt.getY();
                    double      width   = endX - dragFrom.getX();
                    double      height  = endY - dragFrom.getY();
                    if ( width < 0 )
                    {
                        double  temp    = startX;
                        startX = endX;
                        endX = temp;
                        width = -width;
                    }
                    if ( height < 0 )
                    {
                        double  temp    = startY;
                        startY = endY;
                        endY = temp;
                        height = - height;
                    }
                    dragTo = new Point2D.Double( endX, endY );
                    rubberBand.setRect( startX, startY, width, height ); 
                }
                repaint();
            }
        }
        
        private void snap( MouseEvent evt )
        {
            List<PShape>    selected    = selectionMgr.getSelected();
            if ( selected.size() == 2 )
            {
                PShape  toShape     = selected.get( 1 );
                PShape  fromShape   = selected.get( 0 );
                fromShape.snapTo( toShape );
                repaint();
            }
        }
    }
}
