package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an auxiliary test class
 * for validating the lines generated
 * by a LineGenerator instance against
 * the expected values
 * as determined by the algorithm
 * described by that class.
 * See <em>Line Generation Algorithm</em>"
 * in the LineGenerator class description.
 * 
 * @author Jack Straub
 * 
 * @see LineGenerator
 *
 */
public class LineMetrics
{
    private final List<Line2D>  hLines      = new ArrayList<>();
    private final List<Line2D>  vLines      = new ArrayList<>();
    private final Rectangle2D   rect;
    private final float         ppu;
    private final float         lpu;
    
    private final float         centerXco;
    private final float         centerYco;
    private final float         minXco;
    private final float         minYco;
    private final float         floorMinXco;
    private final float         floorMinYco;
    private final float         maxXco;
    private final float         maxYco;
    private final float         ceilMaxXco;
    private final float         ceilMaxYco;
    
    public LineMetrics(
        Rectangle2D rect,
        float       pixelsPerUnit, 
        float       linesPerUnit
    )
    {
        this.rect = rect;
        this.ppu = pixelsPerUnit;
        this.lpu = linesPerUnit;
        
        // pixels between lines
        float   gridSpacing = pixelsPerUnit / linesPerUnit;
        
        // determines location of y-axis
        centerXco = (float)rect.getCenterX();
        // determines location of x-axis
        centerYco = (float)rect.getCenterY();
        minXco = (float)rect.getX();
        maxXco = (float)rect.getWidth() + minXco;
        minYco = (float)rect.getY();
        maxYco = (float)rect.getHeight() + minYco;
        
        floorMinXco = floor( minXco );
        floorMinYco = floor( minYco );
        ceilMaxXco = ceil( maxXco );
        ceilMaxYco = ceil( maxYco );
        
        // horizontal lines above or below x-axis
        float   halfHoriz   = 
            (float)Math.floor( rect.getHeight() / 2 / gridSpacing);
        float   halfVert    = 
            (float)Math.floor( rect.getWidth() / 2 / gridSpacing);

        // generate vertical lines 
        for ( float nextVert = -halfVert ; nextVert <= halfVert ; ++nextVert )
        {
            float   xco         = centerXco + nextVert * gridSpacing;
            Line2D  line        = 
                new Line2D.Float( xco, minYco, xco, maxYco );
            validateEndpoints( line );
            vLines.add( line );
        }
        
        // generate horizontal lines 
        for ( float nextHoriz = -halfHoriz ; nextHoriz <= halfHoriz ; ++nextHoriz )
        {
            float   yco = centerYco + nextHoriz * gridSpacing;
            Line2D  line    = 
                new Line2D.Float( minXco, yco, maxXco, yco );
            hLines.add( line );
        }
    }
    
    public Line2D getLineSegment( Line2D lineIn, float len )
    {
        Line2D  lineOut;
        
        float   lineInXco1  = (float)lineIn.getX1();
        if ( doubleEquals( lineInXco1, lineIn.getX2() ) )
        {
            float   xco     = lineInXco1;
            float   yco1    = centerYco - len / 2;
            float   yco2    = yco1 + len;
            lineOut = new Line2D.Float( xco, yco1, xco, yco2 );
        }
        else
        {
            float   yco     = (float)lineIn.getY1();
            float   xco1    = centerXco - len / 2;
            float   xco2    = xco1 + len;
            lineOut = new Line2D.Float( xco1, yco, xco2, yco );
        }
        return lineOut;
    }
    
    /**
     * Gets the expected list of horizontal lines.
     * 
     * @return the expected list of horizontal lines.
     */
    public List<Line2D> getHorizLines()
    {
        return hLines;
    }
    
    /**
     * Gets the expected number of horizontal lines.
     * 
     * @return the expected number of horizontal lines.
     */
    public int getNumHorizLines()
    {
        return hLines.size();
    }

    /**
     * Gets the expected list of vertical lines.
     * 
     * @return the expected list of vertical lines.
     */
    public List<Line2D> getVertLines()
    {
        return vLines;
    }
    
    /**
     * Gets the expected number of vertical lines.
     * 
     * @return the expected number of vertical lines.
     */
    public int getNumVertLines()
    {
        return vLines.size();
    }

    /**
     * Gets the rectangle used to create this object.
     * 
     * @return the rectangle used to create this object
     */
    public Rectangle2D getRect()
    {
        return rect;
    }

    /**
     * Gets the pixels-per-unit value used to create this object.
     * 
     * @return the pixels-per-unit value used to create this object
     */
    public float getPpu()
    {
        return ppu;
    }

    /**
     * Gets the lines-per-unit value used to create this object.
     * 
     * @return the lines-per-unit value used to create this object
     */
    public float getLPU()
    {
        return lpu;
    }
    
    private void validateEndpoints( Line2D line )
    {
        assertGE( ceil( line.getX1() ), floorMinXco );
        assertGE( ceil( line.getY1() ), floorMinYco );
        assertLT( floor( line.getX2() ), ceilMaxXco );
        assertLT( floor( line.getY2() ), ceilMaxYco );
    }
    
    private static float floor( double valIn )
    {
        float   valOut  = (float)Math.floor( valIn );
        return valOut;
    }
    
    private static float ceil( double valIn )
    {
        float   valOut  = (float)Math.ceil( valIn );
        return valOut;
    }
    
    private static boolean doubleEquals( double fVal1, double fVal2 )
    {
        final double    epsilon = .001f;
        double          diff    = Math.abs( fVal1 - fVal2 );
        boolean         result  = diff < epsilon;
        return result;
    }
    
    private static void assertGE( float leftVal, float rightVal )
    {
        String  msg     = "Testing " + leftVal + " >= " + rightVal;
        assertTrue( leftVal >= rightVal, msg );
    }
    
    private static void assertLT( float leftVal, float rightVal )
    {
        String  msg     = "Testing " + leftVal + " >= " + rightVal;
        assertTrue( leftVal < rightVal, msg );
    }
}
