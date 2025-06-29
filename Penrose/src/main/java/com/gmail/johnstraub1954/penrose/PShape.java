package com.gmail.johnstraub1954.penrose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.johnstraub1954.penrose.utils.ColorMap;

/**
 * Base class for shapes used to construct aperiodic tiling.
 * The implementation is based on many assumptions; see
 * <a href="index.html">
 *     Overview
 * </a>
 * for details.
 */
public abstract class PShape implements Serializable
{
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 3627364027645370422L;
    
    /**
     * Variable to turn on for debugging and experimentation.
     */
    public static final boolean DEBUG   = 
        "on".equals( System.getenv( "debug" ) );
    
    /**
     * Given the longest side in a Penrose shape,
     * calculate the path that encloses the shape.
     * 
     * @param longSide  the given side length
     * 
     * @return  the path that encloses the shape
     */
    public abstract Path2D getPath( double longSide );
    /**
     * Gets a list of vertices that define the path
     * of the encapsulated Penrose shape.
     * 
     * @return  a list of vertices that define the path
     *          of the encapsulated shape
     */
    public abstract List<Vertex>  getVertices();

    /**
     * Key for fill-color in a shape's color map.
     */
    public static final int     FILL_COLOR      = 0;
    /**
     * Key for edge-color in a shape's color map.
     */
    public static final int     EDGE_COLOR      = 1;
    /**
     * Key into a shape's color map; 
     * designates the edge color when a shape is selected.
     */
    public static final int     SELECTED_COLOR  = 2;
    /**
     * Key into a shape's color map; 
     * designates the color of the current edge when a shape is selected.
     */
    public static final int     CURR_SIDE_COLOR  = 3;
    
    /**
     * 18 degrees converted to radians
     */
    public static final double  D18         = 18 * Math.PI / 180;
    /**
     * 36 degrees converted to radians
     */
    public static final double  D36         = 36 * Math.PI / 180;
    /**
     * 72 degrees converted to radians
     */
    public static final double  D72         = 72 * Math.PI / 180;
    /**
     * 144 degrees converted to radians
     */
    public static final double  D144        = 144 * Math.PI / 180;
    /**
     * 108 degrees converted to radians
     */
    public static final double  D108        = 108 * Math.PI / 180;
    /**
     * 108 degrees converted to radians
     */
    public static final double  D216        = 216 * Math.PI / 180;
    /**
     * 1 degree converted to radians
     */
    public static final double  D01         = 180 / Math.PI;
    /**
     * 2 * PI
     */
    public static final double  TWO_PI      = 2 * Math.PI;
    
    /**
     * Maps PShape subclass to a color map.
     * @see #putColors(Class, ColorMap)
     * @see #highlight(Graphics2D)
     * @see PShape#render(Graphics2D)
     */
    private static final 
    Map<Class<? extends PShape>, ColorMap>classColorMap     = 
        new HashMap<>();
    
    /**
     * Default fill color.
     * See, for example, {@linkplain #render(Graphics2D)}.
     */
    private static final Color      defaultFillColor        = Color.WHITE;
    /**
     * Default edge color.
     * See, for example, {@linkplain #render(Graphics2D)}.
     */
    private static final Color      defaultEdgeColor        = Color.BLACK;
    /**
     * Default curr-side color.
     * See, for example, {@linkplain #highlightEdge(Graphics2D)}.
     */
    private static final Color      defaultCurrSideColor    = Color.RED;
    /**
     * Default fill color.
     * See, for example, {@linkplain #highlight(Graphics2D)}.
     */
    private static final Color      defaultSelectedColor    = Color.CYAN;
    /**
     * Default color map. Used when not overridden by subclass.
     * See, for example, {@linkplain #render(Graphics2D)}.
     */
    private static final ColorMap   defaultColorMap         =
        new ColorMap( 
            FILL_COLOR, defaultFillColor,
            EDGE_COLOR, defaultEdgeColor,
            SELECTED_COLOR, defaultSelectedColor,
            CURR_SIDE_COLOR, defaultCurrSideColor
        );

    /**
     * The diameter of the dot drawn at a dotted-vertex.
     * Used by subclasses to construct a shape's path.
     */
    private static double       dotDiam = 4;
    
    /**
     * The default length of the longest side of a PShape.
     * Setting this value only affects the geometry
     * of newly instantiated shapes;
     * it will not change the geometry of existing shapes.
     * 
     * @see #setLongSide(double)
     * @see #getLongSide()
     */
    private static double       longSide  = 50;
    
    /**
     * A PShape's path. Set by subclasses.
     * This path never changes. 
     * When a PShape is rotated or translated,
     * mutations are applied to {@linkplain #workShape};
     * 
     * @see #getPath(double)
     */
    private final Path2D        path;
    /**
     * The bounds of the base PShape.
     * Mainly useful for getting the width and height of a shape.
     * To get the location of a shape, use workShape.getBounds().
     * 
     * @see #workShape
     */
    private final Rectangle2D   rightBounds;
    
    /**
     * List of vertices that comprise this PShape.
     * Supplied by subclass; 
     * created during construction and never changed.
     * 
     * @see #getVertices()
     * @see #getTransformedVertices()
     */
    private final List<Vertex>  vertexList;
    
    /**
     * The x-coordinate of this PShape.
     */
    private double  xco             = 0;
    /**
     * The y-coordinate of this PShape.
     */
    private double  yco             = 0;
    /**
     * The rotation of this PShape.
     */
    private double  rotation        = 0;
    
    /**
     * The width of an edge when highlighting a selected shape.
     */
    private int     highlightWidth  = 2;
    /**
     * The translated and rotated PShape for use in drawing.
     * @see #moveTo(double, double)
     * @see #rotateTo(double)
     */
    private Shape   workShape;
    /**
     * Index into a list of Vertices to the selected vertex
     * for this shape. The initial list of vertices is supplied
     * by the subclass; see also {@linkplain #getTransformedVertices()}.
     * 
     * @see PShape#getVertices()
     */
    private int     currVertex      = 0;
    
    /**
     * Constructor.
     * Determines the geometry of this PShape.
     * 
     * @param longSide  the length of the longest edge
     *                  in the PShape's geometry
     */
    public PShape( double longSide )
    {
        path = getPath( longSide );
        rightBounds = path.getBounds2D();
        computePath();
        vertexList = getVertices();
    }

    /**
     * Convenience method for subclasses to set colors
     * for this class of PShapes.
     * 
     * @param clazz     the class of the target PShape 
     * @param colorMap  collection of colors to use when drawing
     *                  this class of PShapes
     */
    public static void 
    putColors( Class<? extends PShape> clazz, ColorMap colorMap )
    {
        classColorMap.put( clazz, colorMap );
    }
    
    /**
     * Sets the color drawing the given category of shape component
     * for the given subclass of PShape.
     * Possible categories are 
     * FILL_COLOR, EDGE_COLOR, SELECTED_COLOR, and CURR_SIDE_COLOR.
     * 
     * @param clazz     the given subclass of PShape
     * @param category  the given category
     * @param color     the given color
     */
    public static void 
    putColor( Class<PShape> clazz, int category, Color color )
    {
        ColorMap    colorMap    = classColorMap.get( clazz );
        if ( colorMap == null )
        {
            colorMap = new ColorMap();
            classColorMap.put( clazz, colorMap );
        }
        colorMap.put( category, color );
    }
    
    /**
     * Advances the currently selected vertex for this PShape
     * to the next Vertex in this PShape's list of vertices.
     * The Vertex following the last Vertex in the list
     * is the first Vertex in the list.
     */
    public void nextVertex()
    {
        currVertex = (currVertex + 1) % vertexList.size();
    }
    
    /**
     * Advances the currently selected vertex for this PShape
     * to the next Vertex in this PShape's list of vertices.
     * The Vertex following the last Vertex in the list
     * is the first Vertex in the list.
     */
    public void previousVertex()
    {
        if ( --currVertex  < 0 )
            currVertex = vertexList.size() - 1;
    }
    
    /**
     * Gets the representation of the edge
     * associated with the currently selected Vertex.
     * 
     * @return  line representing the currently selected Vertex
     */
    public Line2D getCurrEdge()
    {
        List<Vertex>    transformedVertices = getTransformedVertices();
        int             toInx       = 
            (currVertex + 1) % transformedVertices.size();
        if ( toInx >= transformedVertices.size() )
            toInx = 0;
        Vertex          fromVertex  = transformedVertices.get( currVertex );
        Vertex          toVertex    = transformedVertices.get( toInx );
        Line2D          edge        = fromVertex.getAdjLine();
        return edge;
    }
    
    /**
     * Gets an array of booleans that describe
     * whether the currently selected Vertex
     * and the next Vertex are dotted.
     * 
     * @return  array of booleans describing the dot-state
     *          of the current and next Vertices
     */
    public boolean[] getCurrDotState()
    {
        int         nextVertex  = (currVertex + 1) % vertexList.size();
        Vertex      fromVertex  = vertexList.get( currVertex );
        Vertex      toVertex    = vertexList.get( nextVertex );
        boolean[]   dotted      = 
            { fromVertex.isDotted(), toVertex.isDotted() };
        return dotted;
    }
    
    /**
     * Gets the length of the edge
     * associated with the currently select Vertex.
     * 
     * @return  the length of the edge 
     *          associated with the currently select Vertex
     */
    public double getCurrLength()
    {
        double  length  = vertexList.get( currVertex ).getLength();
        return length;
    }
    
    /**
     * Gets the slope of the edge
     * associated with the currently select Vertex.
     * 
     * @return  the slope of the edge 
     *          associated with the currently select Vertex
     */
    public double getCurrSlope()
    {
        Line2D  line    = getCurrEdge();
        double  slope   = (line.getY1() - line.getY2()) / (line.getX1() - line.getX2() );
        return slope;
    }
    
    /**
     * Returns true if this shape contains the given coordinates.
     * @param xco   given x-coordinate
     * @param yco   given y-coordinate
     * @return  true if this shape contains the given coordinates
     */
    public boolean contains( double xco, double yco ) 
    {
        return workShape.contains( xco, yco );
    }
    
    /**
     * Returns true if this shape intersects the given shape.
     * @param shape  the given shape
     * @return  
     *      true if this shape intersects the given shape's bounding rectangle
     */
    public boolean intersects( PShape shape )
    {
        boolean result  = workShape.intersects( shape.getBounds() );
        return result;
    }
    
    /**
     * Returns true if this shape intersects the given rectangle.
     * @param rect  the given rectangle
     * @return  true if this shape intersects the given rectangle
     */
    public boolean intersects( Rectangle2D rect )
    {
        boolean result  = workShape.intersects( rect );
        return result;
    }
    
    public void appendDot( Path2D path, Point2D coords )
    {
        double  xco = (int)coords.getX();
        double  yco = (int)coords.getY();
        for ( double diam = dotDiam ; diam > 0 ; --diam, ++xco, ++yco )
        {
            Shape   dot = new Ellipse2D.Double( xco, yco, diam, diam );
            path.append( dot, false );
        }
    }
    
    /**
     * Draws the outline of this shape
     * in the designated highlight color.
     * 
     * @param gtx   graphics context for drawing
     */
    public void highlight( Graphics2D gtx )
    {
        ColorMap    colorMap    = 
            classColorMap.getOrDefault( getClass(), defaultColorMap );
        Color       saveColor   = gtx.getColor();
        Stroke      saveStroke  = gtx.getStroke();
        
        Color       color       = 
            colorMap.getOrDefault( SELECTED_COLOR, defaultSelectedColor );
        gtx.setColor( color );
        gtx.setStroke( new BasicStroke( highlightWidth ) );
        gtx.draw( workShape );
        
        gtx.setColor( saveColor );
        gtx.setStroke( saveStroke );
    }
    
    /**
     * Draws the currently selected edge of this shape
     * in the designated color.
     * 
     * @param gtx   graphics context for drawing
     */
    public void highlightEdge( Graphics2D gtx )
    {
        ColorMap    colorMap    = 
            classColorMap.getOrDefault( getClass(), defaultColorMap );
        Color       saveColor   = gtx.getColor();
        Stroke      saveStroke  = gtx.getStroke();
        
        Color       color       = 
            colorMap.getOrDefault( EDGE_COLOR, defaultCurrSideColor );
        Line2D      edge        = getCurrEdge();
        gtx.setColor( color );
        gtx.setStroke( new BasicStroke( highlightWidth ) );
        
        gtx.draw( edge );
        
        gtx.setColor( saveColor );
        gtx.setStroke( saveStroke );
    }
    
    /**
     * Applies an incremental rotation to this shape.
     * The result will be a shape with the beginning rotation
     * plus the given increment.
     * 
     * @param radians   the increment to apply.
     */
    public void rotate( double radians )
    {
        rotateTo( rotation + radians );
    }
    
    /**
     * Sets the rotation of this shape
     * to the given value.
     * 
     * @param radians   the given value
     */
    public void rotateTo( double radians )
    {
        rotation = radians;
        computePath();
    }
    
    /**
     * Applies an incremental translation to this shape.
     * The result will be a shape with the beginning translation
     * plus the given increment.
     * 
     * @param xDelta    the increment to apply to the x-coordinate
     * @param yDelta    the increment to apply to the y-coordinate
     */
    public void move( double xDelta, double yDelta )
    {
        moveTo( xco + xDelta, yco + yDelta );
    }
    
    /**
     * Translates this shape to the given coordinates.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     */
    public void moveTo( double xco, double yco )
    {
        this.xco = xco;
        this.yco = yco;
        computePath();
    }
    
    /**
     * Returns the bounds of this shape
     * with translation and rotation applied.
     * 
     * @return  the bounds of this shape with transforms applied
     */
    public Rectangle2D getBounds()
    {
        Rectangle2D bounds  = workShape.getBounds2D();
        return bounds;
    }
    
    /**
     * Returns the bounds of this shape
     * with its base coordinates and rotation.
     * 
     * @return  the bounds of this shape with no transform applied
     */
    public Rectangle2D getRightBounds()
    {
        double      width   = rightBounds.getWidth();
        double      height  = rightBounds.getHeight();
        Rectangle2D bounds  = 
            new Rectangle2D.Double( xco, yco, width, height );
        return bounds;
    }
    
    /**
     * Draw this shape.
     * Does not include selection highlighting.
     * 
     * @param gtx   the graphics context for drawing
     * 
     * @see #highlight(Graphics2D)
     * @see #highlightEdge(Graphics2D)
     */
    public void render( Graphics2D gtx )
    {
        ColorMap    colorMap    = 
            classColorMap.getOrDefault( getClass(), defaultColorMap );
        Color   save            = gtx.getColor();
        
        Color   fillColor       = 
            colorMap.getOrDefault( FILL_COLOR, defaultFillColor );
        gtx.setColor( fillColor );
        gtx.fill( workShape );
        
        Color   edgeColor       = 
            colorMap.getOrDefault( EDGE_COLOR, defaultEdgeColor );
        gtx.setColor( edgeColor );
        gtx.draw( workShape );
        
        if ( DEBUG )
        {
            // debugging stuff...
            //     ... draw red circle at rotation pin
            double      pinXco  = xco + rightBounds.getCenterX();
            double      pinYco  = yco + rightBounds.getCenterY();
            Ellipse2D   center  = 
                new Ellipse2D.Double( pinXco - 2, pinYco - 2, 4, 4 );
    
            gtx.setColor( Color.GREEN );
            gtx.fill( center );
            
            Rectangle2D bounds  = getBounds();
            gtx.setColor( Color.YELLOW );
            gtx.draw( bounds );
            // end debugging stuff
        }   
        
        gtx.setColor( save );
    }

    /**
     * Returns the width of the line
     * used for highlighting.
     * 
     * @return  the width of the line used for highlighting
     */
    public int getHighlightWidth()
    {
        return highlightWidth;
    }

    /**
     * Sets the width of the line used for highlighting
     * to the given value.
     * 
     * @param highlightWidth    the given value
     */
    public void setHighlightWidth(int highlightWidth)
    {
        this.highlightWidth = highlightWidth;
    }

    /**
     * Gets the diameter of the dot
     * drawn at a dotted vertex.
     * 
     * @return  the diameter of the dot drawn at a dotted vertex
     */
    public static double getDotDiam()
    {
        return dotDiam;
    }
    
    /**
     * Translate the dotted vertex of the currently selected edge
     * in this PShape
     * to the dotted vertex of the currently selected edge
     * in a given PShape.
     * The result is a pair of shapes
     * connected at their currently selected sides.
     * 
     * @param toShape   the given PShape
     */
    public void snapTo( PShape toShape )
    {
        List<Vertex>    fromVertices    = getTransformedVertices();
        List<Vertex>    toVertices      = 
            toShape.getTransformedVertices();
        Vertex          fromVertex      = getDottedVertex( fromVertices );
        Vertex          toVertex        = toShape.getDottedVertex( toVertices );
        Point2D         fromCoords      = fromVertex.getCoords();
        Point2D         toCoords        = toVertex.getCoords();
        double          deltaX          = toCoords.getX() - fromCoords.getX();
        double          deltaY          = toCoords.getY() - fromCoords.getY();
        move( deltaX, deltaY );
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
    
    public Shape getWorkShape()
    {
        return workShape;
    }
    
    /**
     * Changes the diameter of the circle
     * that denotes a dotted vertex.
     * 
     * @param diam  the desired diameter of the circle
     */
    public static void setDotDiam( double diam )
    {
        dotDiam = diam;
    }
    
    /**
     * Sets the default length of a PShape to the given value.
     * Setting this value only affects the geometry
     * of newly instantiated shapes;
     * it will not change the geometry of existing shapes.
     * 
     * @param longSide  the given value
     */
    public static void setLongSide( double longSide )
    {
        PShape.longSide = longSide;
    }
    
    /**
     * Gets the default length of the longest side of a PShape.
     * 
     * @return  the default length of the longest side of a PShape
     */
    public static double getLongSide()
    {
        return longSide;
    }

    /**
     * Calculates the path to draw
     * for this PShape.
     * Calculated from the base path
     * and this PShape's current coordinates and rotation.
     */
    private void computePath()
    {
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        workShape = transform.createTransformedShape( path );
    }
    
    /**
     * From the list of vertices for this PShape,
     * assemble a new list of vertices
     * after applying
     * with current translation and rotation.
     * 
     * @return  the generated list of transformed vertices
     */
    public List<Vertex> getTransformedVertices()
    {
        AffineTransform transform   = getTransform();
        List<Vertex>    verticesOut = new ArrayList<>();
        List<Vertex>    verticesIn  = getVertices();
        for ( Vertex vertexIn : verticesIn )
//            verticesOut.add( vertexIn.getVertex( xco, yco, rotation ) );
            verticesOut.add( vertexIn.getVertex( transform ) );
        return verticesOut;
    }
    
    public AffineTransform getTransform()
    {
        double          xcoPin      = xco + rightBounds.getWidth() / 2;
        double          ycoPin      = yco + rightBounds.getHeight() / 2;
        AffineTransform transform   = new AffineTransform();
        transform.rotate( rotation, xcoPin, ycoPin );
        transform.translate( xco, yco );
        return transform;
    }
    
    /**
     * Given the edge between the current and next vertices,
     * return the Vertex that is dotted.
     * Assumes that exactly one of the two vertices
     * is dotted.
     * 
     * @param vertices  the list of vertices to examine
     * 
     * @return  the vertex that describes the currently selected edge
     *          that is dotted.
     */
    private Vertex getDottedVertex( List<Vertex> vertices )
    {
        int         nextVertex  = (currVertex + 1) % vertices.size();
        boolean[]   dotState    = getCurrDotState();
        Vertex      vertex      = dotState[0] ? 
            vertices.get( currVertex ) : vertices.get( nextVertex );
        return vertex;
    }
}
