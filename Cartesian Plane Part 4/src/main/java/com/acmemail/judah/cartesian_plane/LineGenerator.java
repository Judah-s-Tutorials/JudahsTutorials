package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

public class LineGenerator implements Iterable<Line2D>
{
    public static final int HORIZONTAL  = 1;
    public static final int VERTICAL    = 2;
    public static final int BOTH        = 3;;
    
    private final float gridWidth;      // width of the rectangle
    private final float gridHeight;     // height of the rectangle
    private final float centerXco;      // center x-coordinate
    private final float minXco;         // left-most x-coordinate
    private final float maxXco;         // right-most x-coordinate
    private final float centerYco;      // center y-coordinate
    private final float minYco;         // top-most y-coordinate
    private final float maxYco;         // bottom-most y-coordinate
    
    private final float gridUnit;       // pixels per unit
    
    private float       lpu;            // lines per unit
    private float       length;
    private int         orientation;
    
    private final float gridSpacing;
    private final float totalHorLines;
    private final float totalVerLines;
    
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
        minXco = (float)rect.getMinX();
        maxXco = (float)rect.getMaxX();
        centerYco = (float)rect.getCenterY();
        minYco = (float)rect.getMinY();
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
    
    /**
     * @return the lpu
     */
    public float getLpu()
    {
        return lpu;
    }

    /**
     * @param lpu the lpu to set
     */
    public void setLpu(float lpu)
    {
        this.lpu = lpu;
    }

    /**
     * @return the length
     */
    public float getLength()
    {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(float length)
    {
        this.length = length;
    }

    /**
     * @return the orientation
     */
    public int getOrientation()
    {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(int orientation)
    {
        this.orientation = orientation;
    }

    public void setLengthAndLPU( float length, float lpu )
    {
        this.length = length;
        this.lpu = lpu;
    }
    
    public float getTotalHorizontalLines()
    {
        return totalHorLines;
    }
    
    public float getTotalVerticalLines()
    {
        return totalVerLines;
    }
    
    public float getCenterXco()
    {
        return centerXco;
    }
    
    public float getCenterYco()
    {
        return centerYco;
    }

    private class Line2DVerticalIterator implements Iterator<Line2D>
    {
        private final float yco1;
        private final float yco2;
        private final float actLength;
        private float       xco;
        
        public Line2DVerticalIterator()
        {
            actLength = length >= 0 ? length : gridHeight;
            float   numLeft = (float)Math.floor( totalVerLines );
            xco = centerXco - numLeft * gridSpacing;
            yco1 = centerYco - actLength / 2;
            yco2 = yco1 + actLength;
        }
        
        @Override
        public boolean hasNext()
        {
            boolean hasNext = xco < maxXco;
            return hasNext;
        }

        @Override
        public Line2D next()
        {
            Line2D  line    = 
                new Line2D.Float( xco, yco1, xco, yco2 );
            xco += gridSpacing;
            return line;
        }

    }
    
    private class Line2DHorizontalIterator implements Iterator<Line2D>
    {
        private final float gridSpacing;
        private final float xco1;
        private final float xco2;
        private final float actLength;
        private float       yco;
        
        public Line2DHorizontalIterator()
        {
            actLength = length >= 0 ? length : gridWidth;
            gridSpacing = gridUnit / lpu;
            float   numTop  = (float)Math.floor( totalHorLines / 2 );
            yco = centerYco - numTop * gridSpacing;
            xco1 = centerXco - actLength / 2;
            xco2 = xco1 + actLength;
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
            Line2D  line    = 
                new Line2D.Float( xco1, yco, xco2, yco );
            yco += gridSpacing;
            return line;
        }
    }
    
    private class Line2DIterator implements Iterator<Line2D>
    {
        Iterator<Line2D>    horizontalIter  = new Line2DHorizontalIterator();
        Iterator<Line2D>    verticalIter    = new Line2DVerticalIterator();
        
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
