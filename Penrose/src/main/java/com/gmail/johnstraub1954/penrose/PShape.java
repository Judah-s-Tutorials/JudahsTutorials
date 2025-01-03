package com.gmail.johnstraub1954.penrose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class PShape implements Serializable
{
    private static final long serialVersionUID = 3627364027645370422L;
    
    public static final double  D18     = 18 * Math.PI / 180;
    public static final double  D36     = 36 * Math.PI / 180;
    public static final double  D72     = 72 * Math.PI / 180;
    public static final double  D108    = 108 * Math.PI / 180;
    public static final double  D01     = Math.PI / 180;
    public static final double  TWO_PI  = 2 * Math.PI;
    
    private static final Color              defaultFill     = Color.WHITE;
    private static final Map<String,Color>  fillColorMap    = 
        new HashMap<>();
    
    private static final Color              defaultEdge     = Color.BLACK;
    private static final Map<String,Color>  drawColorMap    = 
        new HashMap<>();
    
    private static double       dotDiam = 4;
    
    private final Path2D        path;
    private final Rectangle2D   rightBounds;
    private final double        longSide;
    
    private double  xco             = 0;
    private double  yco             = 0;
    private double  rotation        = 0;
    private Color   edgeColor       = null;
    private Color   color       = null;
    private Color   highlightColor  = Color.CYAN;
    private int     highlightWidth  = 1;
    private Shape   workShape;
    
    public abstract Path2D initPath( double longSide );
    public abstract Point2D[] getVertices( double longSide );
    
    public PShape( double longSide )
    {
        this.longSide = longSide;
        path = initPath( longSide );
        rightBounds = path.getBounds2D();
        computePath();
    }
    
    public boolean contains( double xco, double yco ) 
    {
        return workShape.contains( xco, yco );
    }
    
    public boolean intersects( Rectangle2D rect )
    {
        boolean result  = workShape.intersects( rect );
        return result;
    }
    
    public void draw( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        gtx.setColor( getEdgeColor() );
        gtx.draw( workShape );
        gtx.setColor( save );
    }
    
    public void highlight( Graphics2D gtx )
    {
        Color   saveColor   = gtx.getColor();
        Stroke  saveStroke  = gtx.getStroke();
        gtx.setColor( highlightColor );
        gtx.setStroke( new BasicStroke( highlightWidth ) );
        gtx.draw( workShape );
        gtx.setStroke( saveStroke );
        gtx.draw( workShape );
        gtx.setColor( saveColor );
    }
    
    public void fill( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        gtx.setColor( getColor() );
        gtx.fill( workShape );
        gtx.setColor( save );
    }
    
    public void rotate( double radians )
    {
        rotateTo( rotation + radians );
    }
    
    public void rotateTo( double radians )
    {
        rotation = radians;
        computePath();
    }
    
    public void move( double xDelta, double yDelta )
    {
        moveTo( xco + xDelta, yco + yDelta );
    }
    
    public void moveTo( double xco, double yco )
    {
        this.xco = xco;
        this.yco = yco;
        computePath();
    }
    
    public Rectangle2D getBounds()
    {
        Rectangle2D bounds  = workShape.getBounds2D();
        return bounds;
    }
    
    public Rectangle2D getRightBounds()
    {
        double      width   = rightBounds.getWidth();
        double      height  = rightBounds.getHeight();
        Rectangle2D bounds  = 
            new Rectangle2D.Double( xco, yco, width, height );
        return bounds;
    }
    
    public void render( Graphics2D gtx )
    {
        Color   save    = gtx.getColor();
        double  xcoPin  = xco + rightBounds.getWidth() / 2;
        double  ycoPin  = yco + rightBounds.getHeight() / 2;
        
        gtx.setColor( getColor() );
        gtx.fill( workShape );
        gtx.setColor( getEdgeColor() );
        gtx.draw( workShape );
        
        gtx.setColor( Color.RED );
        gtx.drawOval( (int)xcoPin, (int)ycoPin, 3, 3 );
        
        gtx.setColor( save );
    }

    public void setDrawColor(Color drawColor)
    {
        this.edgeColor = drawColor;
    }

    public Color getColor()
    {
        Color   result  = color;
        if ( color == null )
        {
            String  clazz   = getClass().getSimpleName();
            result = fillColorMap.get( clazz );
            if ( result == null )
                result = defaultFill;
        }

        return result;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Get the color for drawing the edge of this shape.
     * First, check to see if this shape 
     * has an explicit edge color.
     * If not, try to get the color from the drawColorMap.
     * If still not found apply default color.
     * 
     * @return  the color for drawing the edge of this shape
     */
    public Color getEdgeColor()
    {
        Color   result  = edgeColor;
        if ( result == null )
        {
            String  clazz   = getClass().getSimpleName();
            result = drawColorMap.get( clazz );
            if ( result == null )
                result = defaultEdge;
        }
        return result;
    }

    public void setEdgeColor(Color color)
    {
        this.edgeColor = color;
    }

    public Color getHighlightColor()
    {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor)
    {
        this.highlightColor = highlightColor;
    }

    public int getHighlightWidth()
    {
        return highlightWidth;
    }

    public void setHighlightWidth(int highlightWidth)
    {
        this.highlightWidth = highlightWidth;
    }

    public static double getDotDiam()
    {
        return dotDiam;
    }
    
    public void snapTo( PShape toShape )
    {
        Point2D[]   fromVertices    = getTransformedVertices();
        Point2D[]   toVertices      = 
            toShape.getTransformedVertices();
        Point2D     delta           = 
            getDistance( toVertices, fromVertices );
        move( delta.getX(), delta.getY() );
    }
    
    public static void setDotDiam( double diam )
    {
        dotDiam = diam;
    }
    
    public static void setDefaultFillColor( String clazz, Color color )
    {
        fillColorMap.put( clazz, color );
    }
    
    public static Color getDefaultFillColor( String clazz )
    {
        Color   color   = fillColorMap.get( clazz );
        return color;
    }
    
    public static void setDefaultDrawColor( String clazz, Color color )
    {
        drawColorMap.put( clazz, color );
    }
    
    public static Color getDefaultDrawColor( String clazz )
    {
        Color   color   = drawColorMap.get( clazz );
        return color;
    }
    
    private static Point2D getDistance( Point2D[] too, Point2D[] from )
    {
        double  minDelta    = Double.MAX_VALUE;
        int     tooInx      = 0;
        int     fromInx     = 0;
        for ( int inx = 0 ; inx < too.length ; ++inx )
        {
            Point2D tooVertex   = too[inx];
            for ( int jnx = 0 ; jnx < from.length ; ++jnx )
            {
                Point2D fromVertex  = from[jnx];
                double  delta       = tooVertex.distance( fromVertex );
                if ( delta < minDelta )
                {
                    minDelta = delta;
                    tooInx = inx;
                    fromInx = jnx;
                }
            }
        }
        double  deltaX  = too[tooInx].getX() - from[fromInx].getX();
        double  deltaY  = too[tooInx].getY() - from[fromInx].getY();
        Point2D delta   = new Point2D.Double( deltaX, deltaY );
        return delta;
    }
    
    private void computePath()
    {
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        workShape = transform.createTransformedShape( path );
    }
    
    private Point2D[] getTransformedVertices()
    {
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        Point2D[]       vertices    = getVertices( longSide );
        int             len         = vertices.length;
        Point2D[]       transformed = new Point2D.Double[len];
        transform.transform( vertices, 0, transformed, 0, len );
        return transformed;
    }
}
