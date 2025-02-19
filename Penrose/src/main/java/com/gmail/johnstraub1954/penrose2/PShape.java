package com.gmail.johnstraub1954.penrose2;

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
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class PShape implements Serializable
{
    private static final long serialVersionUID = 3627364027645370422L;
    
    public static final double  D18         = 18 * Math.PI / 180;
    public static final double  D18_SUPP    = 18 * Math.PI / 180;
    public static final double  D36         = 36 * Math.PI / 180;
    public static final double  D72         = 72 * Math.PI / 180;
    public static final double  D108        = 108 * Math.PI / 180;
    public static final double  D01         = Math.PI / 180;
    public static final double  TWO_PI      = 2 * Math.PI;
    public static final double  DOT_XIER    = .05;
    public static final double  DOT_OFFSET  = DOT_XIER / 2;
    
    private static final Color              defaultFill     = Color.WHITE;
    private static final Map<String,Color>  fillColorMap    = 
        new HashMap<>();
    
    private static final Color              defaultEdge     = Color.BLACK;
    private static final Map<String,Color>  drawColorMap    = 
        new HashMap<>();
    
    private static double       dotDiam = 4;
    
    private final Path2D        path;
    private final Rectangle2D   rightBounds;
    
    private double  xco             = 0;
    private double  yco             = 0;
    private double  rotation        = 0;
    private Color   edgeColor       = null;
    private Color   color           = null;
    private Color   highlightColor  = Color.CYAN;
    private int     highlightWidth  = 1;
    private Shape   workShape;
    
    public abstract Path2D getPath( double longSide );
    
    public abstract Deque<Vertex> getVertices( double longSide );
    public abstract Deque<Vertex> getVertices();
    
    public PShape( double longSide )
    {
        path = getPath( longSide );
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
        snapTo2( toShape );
        Point2D[]   fromVertices    = getTransformedVertices();
        Point2D[]   toVertices      = 
            toShape.getTransformedVertices();
        Point2D     delta           = 
            getDistance( toVertices, fromVertices );
        move( delta.getX(), delta.getY() );
    }
    
    public void snapTo2( PShape toShape )
    {
        Map<Vertex,Point2D> fromVertices    = 
            getTransformedVertices2();
        Map<Vertex,Point2D> toVertices      =  
            toShape.getTransformedVertices2();
        VertexMatcher       matcher         = 
            new VertexMatcher( toVertices, fromVertices );
        Vertex[][]          pairs           = matcher.match();
        if ( pairs == null )
            System.out.println( "fail" );
        else
        {
            Point2D toA = pairs[0][0].getCoords();
            Point2D toB = pairs[0][1].getCoords();
            Point2D fromA = pairs[1][0].getCoords();
            Point2D fromB = pairs[1][1].getCoords();
            double  distA   = toA.distance( fromA );
            double  distB   = toB.distance( fromB );
            System.out.printf( "%s -> %s, %f%n", fromA, toA, distA );
            System.out.printf( "%s -> %s, %f%n", fromB, toB, distB );
        }
    }
    
    private Vertex[] matchPair(
        Vertex  toVertex,
        Vertex  fromVertex,
        Set<Entry<Vertex,Point2D>> toVertices,
        Set<Entry<Vertex,Point2D>> fromVertices
    )
    {
        Point2D         toCoords    = toVertex.getCoords();
        Point2D         fromCoords  = fromVertex.getCoords();
        double          distA       = toCoords.distance( fromCoords );
        
        for ( Entry<Vertex,Point2D> toEntry : toVertices )
        {
            Vertex  toTest  = toEntry.getKey();
            if ( !toTest.equals( toVertex) )
            {
            }
        }
        return null;
    }
    
    private Vertex matchPair( 
        Vertex toVertex, 
        Set<Entry<Vertex,Point2D>> fromVertices,
        double distance
    )
    {
        final double    epsilon     = .00001;
        Vertex          result      = null;
        for ( Entry<Vertex,Point2D> fromEntry : fromVertices )
        {
            Point2D toCoords    = toVertex.getCoords();
            Vertex  fromVertex  = fromEntry.getKey();
            if ( toVertex.isDotted() == fromVertex.isDotted() )
            {
                Point2D fromCoords  = fromVertex.getCoords();
                double  testDist    = fromCoords.distance( toCoords );
                double  diff        = distance - testDist;
                if ( diff < epsilon )
                {
                    result = fromVertex;
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * Gets, in degrees, 
     * the supplementary angle 
     * of the given angle.
     * 
     * @param angle the given angle
     * 
     * @return  the supplementary angle
     */
    public double outer( double angle )
    {
        double  supp    = angle - 180;
        return supp;
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
        Deque<Vertex>   vertices    = getVertices();
        int             len         = vertices.size();
        Point2D[]       from        = new Point2D.Double[len];
        int             inx         = 0;
        for ( Vertex vertex : vertices )
            from[inx++] = vertex.getCoords();
        Point2D[]       transformed = new Point2D.Double[len];
        transform.transform( from, 0, transformed, 0, len );
        return transformed;
    }
    
    private Map<Vertex, Point2D> getTransformedVertices2()
    {
        Map<Vertex, Point2D>    map     = new HashMap<>();
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        Deque<Vertex>   vertices    = getVertices();
        int             len         = vertices.size();
        Point2D[]       from        = new Point2D.Double[len];
        int             inx         = 0;
        for ( Vertex vertex : vertices )
            from[inx++] = vertex.getCoords();
        Point2D[]       transformed = new Point2D.Double[len];
        transform.transform( from, 0, transformed, 0, len );
        inx = 0;
        for ( Vertex vertex : vertices )
            map.put( vertex, transformed[inx++] );
        return map;
    }
    
    private static class VertexMatcher
    {
        private static final double                 epsilon     = .00001;
        private final Map<Vertex,Point2D>           mapA;
        private final Map<Vertex,Point2D>           mapB;
        private final Set<Entry<Vertex,Point2D>>    entrySetA;
        private final Set<Entry<Vertex,Point2D>>    entrySetB;
        
        private Vertex  testVertexA     = null;
        private Point2D testCoordsA         = null;
        private Vertex  testVertexB     = null;
        private Point2D testCoordsB         = null;
        private double  testDistance    = 0;
        
        public VertexMatcher( 
            Map<Vertex,Point2D> mapA, 
            Map<Vertex,Point2D> mapB
        )
        {
            this.mapA = mapA;
            this.mapB = mapB;
            entrySetA = mapA.entrySet();
            entrySetB = mapB.entrySet();
        }
        
        public Vertex[][] match()
        {
            for ( Entry<Vertex,Point2D> entryA : entrySetA )
            {
                for ( Entry<Vertex,Point2D> entryB : entrySetB )
                {
                    testVertexA = entryA.getKey();
                    testCoordsA = mapA.get( testVertexA );
                    testVertexB = entryB.getKey();
                    testCoordsB = mapB.get( testVertexB );
                    if ( testVertexA.isDotted() == testVertexB.isDotted() )
                    {
                        testDistance = testCoordsA.distance( testCoordsB );
                        Vertex[]    pairB   = matchDistance();
                        if ( pairB != null )
                        {
                            Vertex[][]  result  = 
                                new Vertex[][] { 
                                    {testVertexA, pairB[0]},
                                    {testVertexB,pairB[1]}
                            };
                            return result;
                        }
                    }
                }
            }
            return null;
        }
        
        public Vertex[] matchDistance()
        {
            for ( Entry<Vertex,Point2D> entryA : entrySetA )
            {
                Vertex  vertexA = entryA.getKey();
                if ( vertexA != testVertexA )
                {
                    for ( Entry<Vertex,Point2D> entryB : entrySetB )
                    {
                        Vertex  vertexB = entryB.getKey();
                        if ( vertexB !=testVertexB )
                        {
                            Vertex[]    result  = 
                                testVertices( vertexA, vertexB );
                            if ( result != null )
                                return result;
                        }
                    }
                }
            }
            return null;
        }
        
        private Vertex[] testVertices( Vertex vertexA, Vertex vertexB )
        {
            Vertex[]    result  = null;
            if ( vertexA.isDotted() == vertexB.isDotted() )
            {
                Point2D coordsA     = mapA.get( vertexA );
                Point2D coordsB     = mapB.get( vertexB );
                double  distance    = coordsA.distance( coordsB );
                double  diff        = distance - testDistance;
                if ( Math.abs( diff ) < epsilon )
                    result = new Vertex[] { vertexA, vertexB };
            }
            return result;
        }
    }
}
