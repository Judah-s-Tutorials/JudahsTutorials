package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineGenerator implements Iterable<Line2D>
{
    public static final int HORIZONTAL  = 1;
    public static final int VERTICAL    = 2;
    public static final int BOTH        = 3;;
    
    private final float gridWidth;      // width of the rectangle
    private final float gridHeight;     // height of the rectangle
    private final float centerXco;      // center x-coordinate
    private final float maxXco;         // right-most x-coordinate
    private final float centerYco;      // center y-coordinate
    private final float maxYco;         // bottom-most y-coordinate
    
    private final float gridUnit;       // pixels per unit
    
    private final float lpu;            // lines per unit
    private final float length;         // the length of a line
    private final int   orientation;    // HORIZONTAL, VERTICAL or BOTH
    
    private final float gridSpacing;    // pixels between lines
    private final float totalHorLines;  // total number of horizontal lines
    private final float totalVerLines;  // total number of vertical lines
    
    public LineGenerator( Rectangle2D rect, float gridUnit, float lpu )
    {
        this( rect, gridUnit, lpu, -1, BOTH );
    }
    
    public LineGenerator( 
        Rectangle2D rect, 
        float       gridUnit,
        float       lpu,
        float       length, 
        int         orientation
    )
    {
        gridWidth = (float)rect.getWidth();
        gridHeight = (float)rect.getHeight();
        centerXco = (float)rect.getCenterX();
        maxXco = (float)rect.getMaxX();
        centerYco = (float)rect.getCenterY();
        maxYco = (float)rect.getMaxY();
        this.gridUnit = gridUnit;
        
        this.lpu = lpu;
        this.length = length;
        this.orientation = orientation;

        gridSpacing = gridUnit / lpu;
        totalVerLines = (float)Math.floor( gridWidth / gridSpacing );
        totalHorLines = (float)Math.floor( gridHeight / gridSpacing );
    }

    @Override
    public Iterator<Line2D> iterator()
    {
        Iterator<Line2D>    iter    = null;
        switch ( orientation )
        {
        case HORIZONTAL:
            iter = new Line2DHorizontalIterator();
            break;
        case VERTICAL:
            iter = new Line2DVerticalIterator();
            break;
        default:
            iter = new Line2DIterator();
        }
        return iter;
    }
    
    public float getTotalHorizontalLines()
    {
        return totalHorLines;
    }
    
    public float getTotalVerticalLines()
    {
        return totalVerLines;
    }

    // produces a sequence of vertical lines from left to right.
    private class Line2DVerticalIterator implements Iterator<Line2D>
    {
        private final float yco1;
        private final float yco2;
        private float       xco;
        
        public Line2DVerticalIterator()
        {
            // Calculate the number of lines drawn left of the origin.
            float   numLeft     = (float)Math.floor( totalVerLines / 2 );
            
            // The actual length is the length passed by the user, or,
            // if the user passed a negative value, the height of the grid.
            float   actLength   = length >= 0 ? length : gridHeight;
            
            // Calculate the top (yco1) and bottom (yco2) of the line
            yco1 = centerYco - actLength / 2;
            yco2 = yco1 + actLength;
            
            // Calculate the x-coordinate of the leftmost line. The
            // x-coordinate is the same for both endpoints of a line.
            // After generating a line, the x-coordinate is incremented
            // to the next line.
            xco = centerXco - numLeft * gridSpacing;
        }
        
        @Override
        // This method required by "implements Iterator<Line2D>"
        public boolean hasNext()
        {
            // The iterator is exhausted when the x-coordinate
            // exceeds the bounds of the grid.
            boolean hasNext = xco < maxXco;
            return hasNext;
        }

        @Override
        // This method required by "implements Iterator<Line2D>"
        public Line2D next()
        {
            // Throw an exception if there is no next line.
            if ( xco > maxXco )
            {
                String  msg = "Grid bounds exceeded at x = " + xco;
                throw new NoSuchElementException( msg );
            }
            Line2D  line    = 
                new Line2D.Float( xco, yco1, xco, yco2 );
            xco += gridSpacing;
            return line;
        }
    }
    
    // Produces a sequence of horizontal lines from top to bottom
    private class Line2DHorizontalIterator implements Iterator<Line2D>
    {
        private final float gridSpacing;
        private final float xco1;
        private final float xco2;
        private float       yco;
        
        public Line2DHorizontalIterator()
        {
            float   actLength = length >= 0 ? length : gridWidth;
            float   numTop  = (float)Math.floor( totalHorLines / 2 );
            gridSpacing = gridUnit / lpu;
            xco1 = centerXco - actLength / 2;
            xco2 = xco1 + actLength;
            yco = centerYco - numTop * gridSpacing;
        }
        
        @Override
        public boolean hasNext()
        {
            boolean hasNext = yco < maxYco;
            return hasNext;
        }

        @Override
        public Line2D next()
        {
            if ( yco > maxYco )
            {
                String  msg = "Grid bounds exceeded at y = " + yco;
                throw new NoSuchElementException( msg ); 
            }
            Line2D  line    = 
                new Line2D.Float( xco1, yco, xco2, yco );
            yco += gridSpacing;
            return line;
        }
    }
    
    private class Line2DIterator implements Iterator<Line2D>
    {
        private final Iterator<Line2D>  horizontalIter  = 
            new Line2DHorizontalIterator();
        private final Iterator<Line2D>  verticalIter    = 
            new Line2DVerticalIterator();
        
        @Override
        public boolean hasNext()
        {
            boolean hasNext = horizontalIter.hasNext() | verticalIter.hasNext();
            return hasNext;
        }
        
        @Override
        public Line2D next()
        {
            Line2D  next    =
                horizontalIter.hasNext() ?
                horizontalIter.next() :
                verticalIter.next();
            return next;
        }
    }
}
