package com.gmail.johnstraub1954.penrose2;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class PCanvas extends JPanel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final   List<PShape>    shapes      = new ArrayList<>();
    private final   List<PShape>    selected    = new ArrayList<>();
    
    /** Start point of a drag operation. */
    private Point2D     dragFrom    = null;
    /** Start point of a drag operation. */
    private Point2D     dragTo      = null;
    /** Rectangle describing a rubber band operation. */
    private Rectangle2D rubberBand  = new Rectangle2D.Double();
    
    private transient Graphics2D  gtx;
    private transient int         width;
    private transient int         height;
    
    public PCanvas()
    {
        this( 500, 500 );
    }
    
    public PCanvas( int width, int height )
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
        shapes.add( shape );
    }
    
    public void removeShape( PShape shape )
    {
        shapes.remove( shape );
        selected.remove( shape );
    }
    
    public void deselect()
    {
        selected.clear();
    }
    
    public void deselect( PShape shape )
    {
        selected.remove( shape );
    }
    
    public void select( PShape shape )
    {
        if ( !selected.contains( shape ) )
            selected.add( shape );
    }
    
    public void select( double xco, double yco )
    {
        shapes.forEach( s -> {
            if ( s.contains( xco, yco ))
            {
                if ( selected.contains( s ) )
                    selected.remove( s );
                else
                    selected.add( s );
            }
        });
        repaint();
    }
    
    public void delete( PShape shape )
    {
        selected.remove( shape );
        shapes.remove( shape );
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
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
        
        if ( dragFrom != null && dragTo != null )
        {
            gtx.setColor( Color.BLACK );
            gtx.draw( rubberBand );
        }

        this.grabFocus();
        gtx.dispose();
    }
    
    public List<PShape> getSelected()
    {
        return selected;
    }
    
    public List<PShape> getShapes()
    {
        return shapes;
    }
    
    public void rotate( double radians )
    {
        selected.forEach( s -> s.rotate( radians) );
    }
    
    public void move( double deltaX, double deltaY )
    {
        selected.forEach( s -> s.move( deltaX, deltaY ) );
    }
    
    private class KListener extends KeyAdapter
    {
        @Override
        public void keyPressed( KeyEvent evt )
        {
            int keyCode = evt.getKeyCode();
            if ( evt.isControlDown() )
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
                else if ( !evt.isShiftDown() )
                    selected.clear();
                int xco = evt.getX();
                int yco = evt.getY();
                select( xco, yco );
            }
        }
        
        @Override
        public void mouseDragged( MouseEvent evt )
        {
            if ( dragFrom != null )
            {
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
