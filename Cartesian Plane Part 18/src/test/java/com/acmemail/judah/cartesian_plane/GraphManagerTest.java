package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.utils.LineSegment;
import com.acmemail.judah.cartesian_plane.test_utils.GraphManagerTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class GraphManagerTest
{
    private final Scanner scanner = new Scanner( System.in );
    private static final String             AXES        =
        LinePropertySetAxes.class.getSimpleName();
    private static final String             GRID_LINES  =
        LinePropertySetGridLines.class.getSimpleName();
    private static final String             TIC_MAJOR   =
        LinePropertySetTicMajor.class.getSimpleName();
    private static final String             TIC_MINOR   =
        LinePropertySetTicMinor.class.getSimpleName();
    
    private static final int    testRGB1        = 0x0000FF;
    private static final int    testRGB2        = testRGB1 << 4;
    private static final int    testGridRGB1    = testRGB2  << 4;
    private static final int    testGridRGB2    = testGridRGB1 << 4;
    private static final Color  testColor1      = new Color( testRGB1 );
    private static final Color  testColor2      = new Color( testRGB2 );
    private static final Color  testGridColor1  = new Color( testGridRGB1 );
    private static final Color  testGridColor2  = new Color( testGridRGB2 );
    private static final float  testGPU1        = 100;
    private static final float  testGPU2        = testGPU1 + 20;
    private static final float  testLPU1        = 2;
    private static final float  testLPU2        = testLPU1 + 2;
    private static final float  testLength1     = 20;
    private static final float  testLength2     = testLength1 + 10;
    private static final float  testStroke1     = 4;
    private static final float  testStroke2     = testStroke1 + 2;

    private static final Profile    baseProfile = new Profile();
    
    private Profile                 workingProfile  = new Profile();
    private static GraphManagerTestGUI  testGUI;
        
    private float                       workingGPU;
    private Color                       workingGridColor;
    private int                         workingGridRGB;
    private float                       workingLPU;
    private float                       workingStroke;
    private float                       workingLength;
    private Color                       workingColor;
    private int                         workingRGB;
    
    private BufferedImage               workingImage;
    private String                      workingLineSet;
    
    @BeforeAll
    public static void beforeAll()
    {
        GUIUtils.schedEDTAndWait( 
            () -> testGUI = new GraphManagerTestGUI()
        );
    }
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        baseProfile.apply();
        workingProfile.reset();
        initTestParameters( 0 );
    }

    @Test
    public void testGraphManager()
    {
        @SuppressWarnings("unused")
        GraphManager    mgr = 
            new GraphManager( new JPanel(), new Profile() );
    }

    // There's no good test for refresh, other than to follow it
    // with some other action and make sure that action succeeds.
    // Which is what happens when we execute every other test.
//    @Test
//    public void testRefresh()
//    {
//        fail("Not yet implemented");
//    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawBackground( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( AXES );
        workingImage   = testGUI.drawBackground();
        validateFill();
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawGridLines( int paramNum  )
    {
        initTestParameters( paramNum );
        initTestData( GRID_LINES );
        workingImage = testGUI.drawGridLines();
        testVerticalLines();
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawText( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( TIC_MAJOR );
        fail("Not yet implemented");
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawHorizontalLabels( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( TIC_MAJOR );
        testGUI.drawHorizontalLabels();
        Utils.pause( 5000 );
        fail("Not yet implemented");
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawVerticalLabels( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( TIC_MAJOR );
        testGUI.drawVerticalLabels();
        Utils.pause( 5000 );
        fail("Not yet implemented");
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawAxes( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( AXES );
        testDrawAxesInternal( paramNum );
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawMinorTics( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( TIC_MINOR );
        workingImage = testGUI.drawMinorTics();
        testVerticalLines();
    }
    
    private void 
    testVerticalLines()
    {
        LineGenerator   lineGen     = 
            getLineGenerator( LineGenerator.VERTICAL );
        for ( Line2D line : lineGen )
        {
            LineSegment expSeg  = 
                LineSegment.ofVertical( line, workingStroke, workingRGB );
            LineSegment actSeg  = 
                LineSegment.of( line.getP1(), workingImage );
            assertEquals( expSeg, actSeg );
        }
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawMajorTics( int paramNum )
    {
        initTestData( TIC_MAJOR );
        workingImage = testGUI.drawMajorTics();
        testVerticalLines();
    }

    private void initTestParameters( int num )
    {
        if ( num == 0 )
        {
            workingGPU = testGPU1;
            workingGridColor = testGridColor1;
            workingGridRGB = testGridRGB1;
            workingLPU = testLPU1;
            workingStroke = testStroke1;
            workingLength = testLength1;
            workingColor = testColor1;
            workingRGB = testRGB1;
        }
        else
        {
            workingGPU = testGPU2;
            workingGridColor = testGridColor2;
            workingGridRGB = testGridRGB2;
            workingLPU = testLPU2;
            workingStroke = testStroke2;
            workingLength = testLength2;
            workingColor = testColor2;
            workingRGB = testRGB2;
        }
    }
    
    private void initTestData( String propSet )
    {
        workingLineSet = propSet;
        testGUI.setGridUnit( workingGPU );
        testGUI.setGridColor( workingGridColor );
        testGUI.setLineSpacing( propSet, workingLPU );
        testGUI.setLineStroke( propSet, workingStroke );
        testGUI.setLineLength( propSet, workingLength );
        testGUI.setLineColor( propSet, workingColor );
    }
    
    /**
     * Verify that every value in a given image
     * is that of a given color.
     * 
     * @param image the given image
     * @param rgb   the given color
     */
    private void validateFill()
    {
        int             width   = workingImage.getWidth();
        int             height  = workingImage.getHeight();
        for ( int xco = 0 ; xco < width ; ++xco )
            for ( int yco = 0 ; yco < height ; ++yco )
            {
                int imageRGB    = 
                    workingImage.getRGB( xco, yco ) & 0xFFFFFF;
                assertEquals( workingGridRGB, imageRGB );
            }
    }
    
    private void testDrawAxesInternal( int paramNum )
    {
        initTestParameters( paramNum );
        initTestData( AXES );
        workingImage                    = testGUI.drawAxes();
        Rectangle2D rect                = getBoundingRectangle();
        Iterator<Line2D>    iter        = 
            LineGenerator.axesIterator( rect );
        Line2D              line1       = iter.next();
        Line2D              line2       = iter.next();
        Line2D              expXAxis    = null;
        Line2D              expYAxis    = null;
        if ( line1.getY1() == line1.getY2() )
        {
            expXAxis = line1;
            expYAxis = line2;
        }
        else
        {
            expXAxis = line2;
            expYAxis = line1;
        }
        LineSegment         expSegX = 
            LineSegment.ofHorizontal( expXAxis, workingStroke, workingRGB );
        LineSegment         expSegY = 
            LineSegment.ofVertical( expYAxis, workingStroke, workingRGB );
        LineSegment         actSegX = 
            LineSegment.of( expXAxis.getP1(), workingImage );
        LineSegment         actSegY = 
            LineSegment.of( expYAxis.getP1(), workingImage );
        assertEquals( expSegX, actSegX );
        assertEquals( expSegY, actSegY );
    }
  
    private static LineSegment getVerticalLineSegment( 
        BufferedImage   image,
        double          centerYco,
        double          len, 
        double          stroke,
        int             rgb
    )
    {
        double  centerXco   = image.getWidth() / 2;
        double  ulcXco      = centerXco - stroke / 2;
        double  ulcYco      = centerYco - len / 2;
        Rectangle2D rect        =
            new Rectangle2D.Double( ulcXco, ulcYco, stroke, len );
        LineSegment seg     = LineSegment.of( rect, rgb );
        return seg;
    }
    
    private LineGenerator getLineGenerator( int orientation )
    {
        boolean hasLength   = testGUI.getLineHasLength( workingLineSet );
        float   length      = hasLength ? workingLength : -1;
        
        LineGenerator   lineGen = new LineGenerator( 
            getBoundingRectangle(), 
            workingGPU, 
            workingLPU, 
            length, 
            orientation
        );
        return lineGen;
    }

    private Rectangle2D getBoundingRectangle()
    {
        int         width   = workingImage.getWidth();
        int         height  = workingImage.getHeight();
        Rectangle2D rect    = 
            new Rectangle2D.Double( 0, 0, width, height );
        return rect;
    }
}
