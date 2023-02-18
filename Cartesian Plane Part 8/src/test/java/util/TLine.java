package util;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * An instance of this class
 * encapsulates a horizontal or vertical line
 * with a given starting point and width.
 * It solves a problem with testing the way
 * a line is drawn;
 * specifically, the endpoints of the line
 * may or may not be drawn,
 * depending on the width of the line.
 * 
 * @see #getExteriorTestPoints()
 * @see #getInteriorTestPoints()
 * 
 * @author Jack Straub
 */
public abstract class TLine
{
    private final Line2D    line    = new Line2D.Float();
    private final Stroke    stroke;
    private final int       width;
    
    /**
     * Formulates a list of points that can 
     * reliably be considered interior
     * to the encapsulated line.
     *
     * @return  a list of points that can be
     *          reliably considered interior to the line.
     */
    public abstract List<Point> getInteriorTestPoints();
    /**
     * Formulates a list of points that can 
     * reliably be considered exterior
     * to the encapsulated line.
     *
     * @return  a list of points that can be
     *          reliably considered interior to the line.
     */
    public abstract List<Point> getExteriorTestPoints();
    
    /**
     * Determine the line
     * with the given endpoints and width.
     * Note that,
     * for a horizontal line,
     * <em>width</em> refers to 
     * the number of rows of pixels
     * that constitute a line;
     * for a vertical line,
     * it refers to 
     * the number of columns of pixels. 
     * 
     * @param xco1      given left-hand x-coordinate of the line
     * @param yco1      given upper y-coordinate of the line
     * @param xco2      given right-hand x-coordinate of the line
     * @param yco2      given upper y-coordinate of the line
     * @param width     given width of the line
     */
    public TLine( int xco1, int yco1, int xco2, int yco2, int width )
    {
        line.setLine( xco1, yco1, xco2, yco2 );
        this.width = width;
        stroke = new BasicStroke( 
            width,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER
        );
    }

    /**
     * Draws the encapsulated line
     * using a given graphics context.
     * 
     * @param gtx   the given graphics context
     */
    public void draw( Graphics2D gtx )
    {
        gtx.setStroke( stroke );
        gtx.draw( line );
    }
    
    /**
     * Returns the width of the encapsulated line.
     * 
     * @return  the width of the encapsulated line
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Gets the encapsulated line 
     * in the form of a Line2D.
     * 
     * @return  the encapsulated line 
     *          in the form of a Line2D
     */
    public Line2D getLine()
    {
        return line;
    }
}
