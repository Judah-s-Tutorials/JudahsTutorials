package com.gmail.johnstraub1954.penrose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.gmail.johnstraub1954.penrose.utils.SelectionListener;
import com.gmail.johnstraub1954.penrose.utils.SelectionManager;

import sandbox.Temp;

public class PCanvasX extends JPanel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static PCanvasX  defaultCanvas   = null;

    private final SelectionManager  selectionMgr        = new SelectionManager();
    private final   List<Line2D>    highlightedEdges    = 
        new ArrayList<>();
    
    /** Color to use when highlighting edges. */
    private Color       highlightedEdgeColor    = Color.RED;
    /** Width to use when drawing a highlighted edge. */
    private float       highlightedEdgeWidth    = 3;
    
    /** Start point of a drag operation. */
    private Point2D     dragFrom    = null;
    /** Start point of a drag operation. */
    private Point2D     dragTo      = null;
    /** Rectangle describing a rubber band operation. */
    private Rectangle2D rubberBand  = new Rectangle2D.Double();
    
    private transient boolean       showGrid    = false;
    private transient Graphics2D    gtx;
    private transient int           width;
    private transient int           height;
    
    public static PCanvasX getDefaultCanvas()
    {
        return getDefaultCanvas( 500, 500 );
    }
    
    public static PCanvasX getDefaultCanvas( int width, int height )
    {
        if ( defaultCanvas == null )
            defaultCanvas = new PCanvasX( width, height );
        return defaultCanvas;
    }
    
    private PCanvasX( int width, int height )
    {
        setPreferredSize( new Dimension( width, height ) );
        setFocusable( true );
        MListener   mListener   = new MListener();
        KListener   kListener   = new KListener();
        addMouseListener( mListener );
        addMouseMotionListener( mListener );
        addKeyListener( kListener );
    }
    
    public void addShape( PShape shape )
    {
        selectionMgr.add( shape );
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
    
    public void select( PShape shape )
    {
        selectionMgr.select( shape );
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
                    select( s );
            });
        repaint();
    }
    
    public void delete( PShape shape )
    {
        selectionMgr.remove( shape );
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
    
    public void addEdge( Line2D edge )
    {
        highlightedEdges.add( edge );
    }
    
    public void removeEdge( Line2D edge )
    {
        highlightedEdges.remove( edge );
    }
    
    public void clearEdges()
    {
        highlightedEdges.clear();
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
        drawHighlightedEdges();
        
        if ( dragFrom != null && dragTo != null )
        {
            gtx.setColor( Color.BLACK );
            gtx.draw( rubberBand );
        }
        
        if (showGrid )
            drawGrid();

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
    
    public void rotate( double radians )
    {
        List<PShape>    selected    = selectionMgr.getSelected();
        if ( selected.size() > 1 )
        selected.get( 0 ).rotate( radians);
    }
    
    public void move( double deltaX, double deltaY )
    {
        getSelected().forEach( s -> s.move( deltaX, deltaY ) );
    }
    
    private class KListener extends KeyAdapter
    {
        private void render()
        {
            List<PShape>    selected    = selectionMgr.getSelected();
            if ( selected.size() > 1 )
            {
                PShape  shape1  = selected.get( 0 );
                PShape  shape2  = selected.get( 1 );
                double  slope1  = shape1.getCurrSlope();
                double  slope2  = shape2.getCurrSlope();
                String  feedback    = 
                    shape1.getCurrDotState() + " - "
                    + shape2.getCurrDotState() + System.lineSeparator()
                    + slope1 + " - " + slope2;
                System.out.println( feedback );
            }
            repaint();
        }
        
        @Override
        public void keyPressed( KeyEvent evt )
        {
            List<PShape>    selected    = selectionMgr.getSelected();
            int keyCode = evt.getKeyCode();
            if ( evt.isControlDown() && evt.isAltDown() && keyCode == KeyEvent.VK_N )
            {
                if ( selected.size() > 0 )
                    select( selected.get( 0 ) );
                render();
            }
            else if ( evt.isControlDown() && evt.isAltDown() && keyCode == KeyEvent.VK_O )
            {
                if ( selected.size() > 1 )
                    select( selected.get( 1 ) );
                render();
            }

            else if ( evt.isControlDown() )
            {
                double  angle   = 
                    evt.isShiftDown() ? PShape.D01 : PShape.D18;
                if ( evt.isShiftDown() )
                    angle = PShape.D01;
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
                }
            }
            else
            {
                switch ( keyCode )
                {
                case KeyEvent.VK_LEFT:
                    move( -1d, 0d );
                    repaint();
                    break;
                case KeyEvent.VK_RIGHT:
                    move( 1d, 0d );
                    repaint();
                    break;
                case KeyEvent.VK_UP:
                    move( 0d, -1d );
                    repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    move( 0d, 1d );
                    repaint();
                    break;
                }
            }
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
    
    private void drawHighlightedEdges()
    {
        float   width   = highlightedEdgeWidth;
        int     cap     = BasicStroke.CAP_BUTT;
        int     join    = BasicStroke.JOIN_ROUND;
        Stroke  stroke  = new BasicStroke( width, cap, join );
        gtx.setStroke( stroke );
        gtx.setColor( highlightedEdgeColor );
        highlightedEdges.forEach( gtx::draw );
    }
    
    private void drawGrid()
    {
        gtx.setColor( Color.BLACK );
        gtx.setStroke( new BasicStroke( 1 ) );
        for ( int inx = 100 ; inx < width ; inx += 100 )
            gtx.drawLine( inx, 0, inx, height );
        for ( int inx = 100 ; inx < height ; inx += 100 )
            gtx.drawLine( 0, inx, width, inx );
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
            List<PShape>    selected    = selectionMgr.getSelected();
            if ( evt.getButton() == 1 )
            {
                System.out.println( evt );
                if ( evt.isControlDown() )
                    snap( evt );
                else 
                {
                    if ( !evt.isShiftDown() )
                        selected.clear();
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
            int xco = evt.getX();
            int yco = evt.getY();
            select( xco, yco );
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
