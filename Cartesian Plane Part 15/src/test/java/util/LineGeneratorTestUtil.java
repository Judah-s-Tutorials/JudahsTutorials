package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.LineGenerator;

public class LineGeneratorTestUtil
{
    private static final int[]          gpuOptions  = { 32, 64 };
    private static final int[]          lpuOptions  = { 2, 4 };
    private static final int            gridWidth   = 20 * 64;
    private static final int            gridHeight  = gridWidth / 2;
    private static final int            xcoBCheck   = 16;
    private static final int            ycoBCheck   = 16;
    private static final int            xcoNoBCheck = 24;
    private static final int            ycoNoBCheck = 24;
    private static final Rectangle2D    gridRect    = 
        new Rectangle2D.Double();
    
    private final List<Line2D>          allHor     = new LinkedList<>();
    private final List<Line2D>          allVert    = new LinkedList<>();
    
    private float                       originXco;
    private float                       originYco;
    private float                       gridXco;
    private float                       gridYco;
    private float                       gpu;
    private float                       lpu;
    private float                       spacing;
    private float                       length;
    private int                         orientation;
    private LineGenerator               lineGenerator;
    
    public LineGenerator 
    getLineGenerator( int gpuOpt, int lpuOpt, boolean bCheck )
    {
        gpu = gpuOptions[gpuOpt];
        lpu = lpuOptions[lpuOpt];
        length = -1;
        orientation = LineGenerator.BOTH;
        lineGenerator = new LineGenerator( gridRect, gpu, lpu );
        configure();
        return lineGenerator;
    }
    
    public LineGenerator 
    getLineGenerator( int gpuOpt, int lpuOpt, boolean bCheck, float len )
    {
        gpu = gpuOptions[gpuOpt];
        lpu = lpuOptions[lpuOpt];
        length = len;
        orientation = LineGenerator.BOTH;
        calculateRect( bCheck );
        lineGenerator = new LineGenerator( gridRect, gpu, lpu, length );
        configure();
        return lineGenerator;
    }
    
    public LineGenerator 
    getLineGenerator( 
        int     gpuOpt, 
        int     lpuOpt, 
        boolean bCheck, 
        float   len,
        int     orient
    )
    {
        gpu = gpuOptions[gpuOpt];
        lpu = lpuOptions[lpuOpt];
        length = len;
        orientation = orient;
        calculateRect( bCheck );
        lineGenerator = new LineGenerator( gridRect, gpu, lpu, len, orient );
        configure();
        return lineGenerator;
    }
    
    public void validateInstanceAxesIterator()
    {
        Iterator<Line2D>    iter    = lineGenerator.axesIterator();
        validateAxesIterator( iter );
    }
    
    public void validateClassAxesIterator()
    {
        Iterator<Line2D>    iter    = 
            LineGenerator.axesIterator( gridRect );
        validateAxesIterator( iter );
    }
    
    public void validateCount()
    {
        int     expHCount   = allHor.size();
        int     actHCount   = lineGenerator.getHorLineCount();
        assertEquals( expHCount, actHCount );
        
        int     expVCount   = allVert.size();
        int     actVCount   = lineGenerator.getVertLineCount();
        assertEquals( expVCount, actVCount );
        
        int     expTotal;
        if ( orientation == LineGenerator.HORIZONTAL )
            expTotal = expHCount;
        else if ( orientation == LineGenerator.VERTICAL )
            expTotal = expVCount;
        else
            expTotal = expHCount + expVCount;
        
        int     actTotal    = 0;
        Iterator<Line2D>    iter    = lineGenerator.iterator();
        while ( iter.hasNext() )
        {
            actTotal++;
            iter.next();
        }
        assertEquals( expTotal, actTotal );
    }
    
    public void validateLength()
    {
        List<Float> allLengths  = new ArrayList<>();
        if ( length > 0 )
            allLengths.add( length );
        else if ( orientation == LineGenerator.BOTH )
        {
            allLengths.add( (float)gridWidth );
            allLengths.add( (float)gridHeight );
        }
        else if ( orientation == LineGenerator.HORIZONTAL )
            allLengths.add( (float)gridWidth );
        else 
            allLengths.add( (float)gridHeight );
        
        Iterator<Line2D>    iter    = lineGenerator.iterator();
        while ( iter.hasNext() )
        {
            Line2D  line    = iter.next();
            float   length  = getLength( line );
            assertTrue( allLengths.contains( length ) );
        }
    }
    
    public void validateLines()
    {
        if ( (orientation & LineGenerator.HORIZONTAL) != 0 )
            validateHorizontalLines();
        if ( (orientation & LineGenerator.VERTICAL) != 0 )
            validateVerticalLines();
    }
    
    public void validateGetHorizontalCount()
    {
        int expCount    = allHor.size();
        int actCount    = lineGenerator.getHorLineCount();
        assertEquals( expCount, actCount );
    }
    
    public void validateGetVerticalCount()
    {
        int expCount    = allVert.size();
        int actCount    = lineGenerator.getVertLineCount();
        assertEquals( expCount, actCount );
    }
    
    private void validateAxesIterator( Iterator<Line2D> iter )
    {
        List<Line2D>        axes    = new ArrayList<>();
        while ( iter.hasNext() )
            axes.add( iter.next() );
        assertFalse( iter.hasNext() );
    }
    
    private void validateHorizontalLines()
    {
        Iterator<Line2D>    actIterator = lineGenerator.iterator();
        Iterator<Line2D>    expIterator = allHor.iterator();
        while ( actIterator.hasNext() )
        {
            Line2D  actLine = actIterator.next();
            if ( isHorizontal( actLine ) )
            {
                assertTrue( expIterator.hasNext() );
                Line2D  expLine = expIterator.next();
                assertTrue( equals( expLine, actLine ) );
            }
        }
        assertFalse( expIterator.hasNext() );
    }
    
    private void validateVerticalLines()
    {
        Iterator<Line2D>    actIterator = lineGenerator.iterator();
        Iterator<Line2D>    expIterator = allVert.iterator();
        while ( actIterator.hasNext() )
        {
            Line2D  actLine = actIterator.next();
            if ( isVertical( actLine ) )
            {
                assertTrue( expIterator.hasNext() );
                Line2D  expLine = expIterator.next();
                assertTrue( equals( expLine, actLine ) );
            }
        }
        assertFalse( expIterator.hasNext() );
    }
    
    private float getLength( Line2D line )
    {
        Point2D point1  = line.getP1();
        Point2D point2  = line.getP2();
        float   length  = (float)point1.distance( point2 );
        return length;
    }
    
    private void calculateRect( boolean bCheck )
    {
        if ( bCheck )
        {
            gridXco = xcoBCheck;
            gridYco = ycoBCheck;
        }
        else
        {
            gridXco = xcoNoBCheck;
            gridYco = ycoNoBCheck;
        }
        gridRect.setFrame( gridXco, gridYco, gridWidth, gridHeight );
    }
    
    private void configure()
    {
        spacing = gpu / lpu;
        int intSpacing  = (int)spacing;
        originXco = (float)gridRect.getCenterX();
        originYco = (float)gridRect.getCenterY();
        assertEquals( intSpacing, spacing );
        assertEquals( gridWidth % intSpacing, 0 );
        assertEquals( gridHeight % intSpacing, 0 );
        
        calculateAllHor();
        calculateAllVert();
    }
    
    private void calculateAllHor()
    {
        allHor.clear();
        float   leftEnd;
        float   rightEnd;
        if ( length == -1 )
        {
            leftEnd = gridXco;
            rightEnd = (float)gridRect.getMaxX();
        }
        else
        {
            leftEnd = originXco - length / 2;
            rightEnd = originXco + length / 2;
        }

        float   firstUp     = originYco - spacing;
        float   firstDown   = originYco + spacing;
        float   lastDown    = (float)gridRect.getMaxY();
        for ( float yco = firstUp ; yco > gridYco ; yco -= spacing )
        {
            Line2D  line    = 
                new Line2D.Double( leftEnd, yco, rightEnd, yco ); 
            allHor.add( 0, line );
        }
        for ( float yco = firstDown ; yco < lastDown ; yco += spacing )
        {
            Line2D  line    = 
                new Line2D.Double( leftEnd, yco, rightEnd, yco ); 
            allHor.add( line );
        }
    }
    
    private void calculateAllVert()
    {
        allVert.clear();
        float   upEnd;
        float   downEnd;
        if ( length == -1 )
        {
            upEnd = gridYco;
            downEnd = (float)gridRect.getMaxY();
        }
        else
        {
            upEnd = originYco - length / 2;
            downEnd = originYco + length / 2;
        }
        float   firstLeft     = originXco - spacing;
        float   firstRight   = originXco + spacing;
        float   lastRight    = (float)gridRect.getMaxX();
        for ( float xco = firstLeft ; xco > gridXco ; xco -= spacing )
        {
            Line2D  line    = 
                new Line2D.Double( xco, upEnd, xco, downEnd ); 
            allVert.add( 0, line );
        }
        for ( float xco = firstRight ; xco < lastRight ; xco += spacing )
        {
            Line2D  line    = 
                new Line2D.Double( xco, upEnd, xco, downEnd ); 
            allVert.add( line );
        }
    }
    
    private boolean equals( Line2D lineA, Line2D lineB )
    {
        Point2D lineAP1 = lineA.getP1();
        Point2D lineAP2 = lineA.getP2();
        Point2D lineBP1 = lineB.getP1();
        Point2D lineBP2 = lineB.getP2();
        boolean rcode   = false;
        if ( equals( lineAP1, lineBP1 ) )
        {
            rcode = equals( lineAP2, lineBP2 );
        }
        else if ( equals( lineAP1, lineBP2 ) )
        {
            rcode = equals( lineAP2, lineBP1 );
        }
        else
            rcode = false;
        return rcode;
    }
    
    private boolean equals( Point2D pointA, Point2D pointB )
    {
        boolean rcode   = 
            pointA.getX() == pointB.getX() &&
            pointA.getY() == pointB.getY();
        return rcode;
    }
    
    private boolean isHorizontal( Line2D line )
    {
        double  yco1    = line.getY1();
        double  yco2    = line.getY2();
        boolean rcode   = yco1 == yco2;
        return rcode;
    }
    
    private boolean isVertical( Line2D line )
    {
        double  xco1    = line.getX1();
        double  xco2    = line.getX2();
        boolean rcode   = xco1 == xco2;
        return rcode;
    }
}
