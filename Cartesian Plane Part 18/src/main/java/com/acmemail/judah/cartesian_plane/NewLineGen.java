package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NewLineGen implements Iterable<Line2D>
{
    public static final int HORIZONTAL  = 1;
    /** Indicates that the iterable generates vertical lines. */
    public static final int VERTICAL    = 2;
    /** 
     * Indicates that the iterable generates horizontal 
     * and vertical lines. 
     */
    public static final int BOTH        = HORIZONTAL | VERTICAL;

    private final Rectangle2D   rect;
    private final float         gpu;
    private final float         lpu;
    private final float         horLength;
    private final float         vertLength;
    private final int           orientation;
    
    private final List<Line2D>  axes        = new LinkedList<>();
    private final List<Line2D>  horLines    = new LinkedList<>();
    private final List<Line2D>  vertLines   = new LinkedList<>();
    
    private final float         originXco;
    private final float         originYco;
    /**
     * Constructor.
     * Instantiates a LineGenerator
     * with the given bounding rectangle,
     * grid unit and lines-per-unit.
     * The line length defaults to -1
     * (grid lines will span the width or height
     * of the bounding rectangle)
     * and the orientation will default to BOTH
     * (the iterator will generate
     * horizontal and vertical lines).
     * 
     * @param rect      the given bounding rectangle
     * @param gridUnit  the given grid unit
     * @param lpu       the given lines-per-unit
     */
    public NewLineGen( Rectangle2D rect, float gridUnit, float lpu )
    {
        this( rect, gridUnit, lpu, -1, BOTH );
    }

    public NewLineGen( 
        Rectangle2D rect, 
        float       gridUnit,
        float       lpu,
        float       length 
    )
    {
        this( rect, gridUnit, lpu, length, BOTH );    
    }
    
    public NewLineGen( 
        Rectangle2D rect, 
        float       gridUnit,
        float       lpu,
        float       length, 
        int         orientation
    )
    {
        this.rect = rect;
        this.gpu = gridUnit;
        this.lpu = lpu;
        this.horLength = length != -1 ? length : (float)rect.getWidth();
        this.vertLength = length != -1 ? length : (float)rect.getHeight();
        this.orientation = orientation;
        
        originXco = (float)rect.getCenterX();
        originYco = (float)rect.getCenterY();
        computeHorizontals();
        computeVerticals();
        computeAxes();
    }
    
    @Override
    public Iterator<Line2D> iterator()
    {
        List<Line2D>    allLines    = new ArrayList<>();
        if ( (orientation & HORIZONTAL) != 0 )
            allLines.addAll( horLines );
        if ( (orientation & VERTICAL) != 0 )
            allLines.addAll( vertLines );
            
        Iterator<Line2D>    iter    = allLines.iterator();
        return iter;
    }
    
    public Iterator<Line2D> axesIterator()
    {
        return axes.iterator();
    }
    
    public float getHorLineCount()
    {
        return horLines.size();
    }

    public float getVertLineCount()
    {
        return vertLines.size();
    }

    private void computeHorizontals()
    {
        float   spacing = gpu / lpu;
        float   xco1    = originXco - horLength / 2;
        float   xco2    = originXco + horLength / 2;

        for ( 
            float yco = originYco - spacing ; 
            yco > 0 ; 
            yco -= spacing
        )
        {
            Point2D left    = new Point2D.Double( xco1, yco );
            Point2D right   = new Point2D.Double( xco2, yco );
            Line2D  line    = new Line2D.Double( left, right );
            horLines.add( line );
        }

        for ( 
            float yco = originYco + spacing ; 
            yco < rect.getHeight() ; 
            yco += spacing
        )
        {
            Point2D left    = new Point2D.Double( xco1, yco );
            Point2D right   = new Point2D.Double( xco2, yco );
            Line2D  line    = new Line2D.Double( left, right );
            horLines.add( line );
        }
    }
    
    private void computeVerticals()
    {
        float   spacing = gpu / lpu;
        float   yco1    = originYco - vertLength / 2;
        float   yco2    = originYco + vertLength / 2;

        for ( 
            float xco = originXco - spacing ;
            xco > 0 ; 
            xco -= spacing
        )
        {
            Point2D top     = new Point2D.Double( xco, yco1 );
            Point2D bottom  = new Point2D.Double( xco, yco2 );
            Line2D  line    = new Line2D.Double( top, bottom );
            vertLines.add( line );
        }

        for ( 
            float xco = originXco + spacing ; 
            xco < rect.getWidth() ; 
            xco += spacing
        )
        {
            Point2D top     = new Point2D.Double( xco, yco1 );
            Point2D bottom  = new Point2D.Double( xco, yco2 );
            Line2D  line    = new Line2D.Double( top, bottom );
            vertLines.add( line );
        }
    }
    
    private void computeAxes()
    {
        double  width   = rect.getWidth();
        double  height  = rect.getHeight();
        Point2D hPoint1 = new Point2D.Double( 0, originYco );
        Point2D hPoint2 = new Point2D.Double( width, originYco );
        Point2D vPoint1 = new Point2D.Double( originXco, 0 );
        Point2D vPoint2 = new Point2D.Double( originXco, height );
        axes.add( new Line2D.Double( hPoint1, hPoint2 ) );
        axes.add( new Line2D.Double( vPoint1, vPoint2 ) );
    }
}
