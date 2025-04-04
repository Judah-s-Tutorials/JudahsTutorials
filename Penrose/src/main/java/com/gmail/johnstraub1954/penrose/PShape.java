package com.gmail.johnstraub1954.penrose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.gmail.johnstraub1954.penrose.matcher.MatchDialog;
import com.gmail.johnstraub1954.penrose.matcher.Matcher;
import com.gmail.johnstraub1954.penrose.matcher.VertexPair;

public abstract class PShape implements Serializable
{
    private static final long serialVersionUID = 3627364027645370422L;
    
    public static final double  D18         = 18 * Math.PI / 180;
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
    
    /**
     * Given sets A and B, distinct coordinates a1, a2 elements
     * of A and distinct coordinates b1, b2 elements of B.
     * Find two pairs of coordinates (a1, b1) and (a2, b2)
     * such that the distance a1 -> b1 is the same as a2 -> b2.
     * <p>
     * For each element a of A:
     * <ul>
     * <li>
     *      Create a list of the distances from a to each element
     *      of B.
     * </li>    
     * <li>
     *      For each element
     * </li>  to
     * </ul>
     */
    public void snapTo( PShape toShape )
    {
        Collection<Vertex>  fromVertices    = getTransformedVertices();
        Collection<Vertex>  toVertices      = 
            toShape.getTransformedVertices();
        Matcher             matcher         = 
            new Matcher( fromVertices, toVertices );
        List<VertexPair[]>  allPairs        = matcher.match();        
        allPairs.sort( (v1,v2) -> 
            (int)(v1[0].getDistance() - v2[0].getDistance()) );
        VertexPair[]        pairs        = select( allPairs );
        if ( pairs != null )
        {
            Point2D toCoords    = pairs[0].getToVertex().getCoords();
            Point2D fromCoords  = pairs[0].getFromVertex().getCoords();
            double  deltaX      = toCoords.getX() - fromCoords.getX();
            double  deltaY      = toCoords.getY() - fromCoords.getY();
            move( deltaX, deltaY );
        }
    }
    
    private VertexPair[] select( List<VertexPair[]> list )
    {
        VertexPair[]    selectedPair    = null;
        int             size            = list.size();
        if ( size == 0 )
            ;
        else if ( size == 1 )
            selectedPair = list.get( 0 );
        else
        {
            int[]   choice  = new int[]{ 0 };
            MatchDialog matchDialog = new MatchDialog( null, list );
            matchDialog.setOptionMonitor( i -> {
                System.out.println( i );
                choice[0] = i;
                selectEdges( list.get( i ) );
            });
            int     option  = matchDialog.showDialog();
            if ( option == JOptionPane.OK_OPTION )
                selectedPair = list.get( choice[0] );
            PCanvas.getDefaultCanvas().clearEdges();
        }
        return selectedPair;
    }
    
    /**
     * Given a pair of vertices in one shape,
     * and a pair of vertices in another shape,
     * describe the line connecting the vertices in one shape
     * and the line connecting the vertices in the other.
     * Note that pair[0] is a mapping from a vertex in the first shape
     * to a vertex in the second shape.
     * Likewise, pair[1] is a mapping of a different vertices
     * in the first and second shapes.
     * 
     * @param pairs      the two pairs of vertices
     */
    private static void selectEdges( VertexPair[] pairs )
    {
        // Given PShape shape1, shape2
        //     pair[0] associates the from-vertex in shape1 with
        //     the from-vertex in shape2
        //     pair[1] associates the to-vertex in shape1 with
        //     the to-vertex in shape2
        //
        //     To highlight the edge in shape1, use the from-vertex
        //     in shape1 (pair[0]) and the from-vertex in shape2
        //     (pair[1]).
        //
        //     To highlight the edge in shape2, use the to-vertex
        //     in shape1 (pair[0]) and the to-vertex in shape2
        //     (pair[1]).
        Vertex  fromV1  = pairs[0].getFromVertex();
        Vertex  fromV2  = pairs[1].getFromVertex();
        Vertex  toV1    = pairs[0].getToVertex();
        Vertex  toV2    = pairs[1].getToVertex();
        Line2D  edge1   = fromV1.getEdge( fromV2 );
        Line2D  edge2   = toV1.getEdge( toV2 );
        PCanvas canvas  = PCanvas.getDefaultCanvas();
        canvas.clearEdges();
        canvas.addEdge( edge1 );
        canvas.addEdge( edge2 );
        canvas.repaint();
    }
    
    /**
     * Gets, in degrees, 
     * the supplement of the given angle.
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
    
    private void computePath()
    {
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        workShape = transform.createTransformedShape( path );
    }
    
    public Collection<Vertex> getTransformedVertices()
    {
        List<Vertex>    verticesOut = new ArrayList<>();
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        Deque<Vertex>   verticesIn  = getVertices();
        int             len         = verticesIn.size();
        Point2D[]       from        = new Point2D.Double[len];
        int             inx         = 0;
        for ( Vertex vertex : verticesIn )
            from[inx++] = vertex.getCoords();
        Point2D[]       transformed = new Point2D.Double[len];
        transform.transform( from, 0, transformed, 0, len );
        inx = 0;
        for ( Vertex vertex : verticesIn )
            verticesOut.add( new Vertex( vertex, transformed[inx++] ) );
        return verticesOut;
    }
}
