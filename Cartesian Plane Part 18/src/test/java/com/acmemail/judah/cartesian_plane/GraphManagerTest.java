package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.GraphManagerTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.LineSegment;
import com.acmemail.judah.cartesian_plane.test_utils.Tess4JConfig;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * JUnit test for {@link GraphManager}.
 * 
 */
public class GraphManagerTest
{
    /**
     * Default image width. This is the width used by most of the
     * tests. For exceptions see {@link #testGraphManagerProfile()},
     * {@link #testGraphManagerRectProfile()}, and 
     * {@link #prepareRectTest()}.
     */
    private static final int    defImgWidth      = 300;
    /**
     * Default image width. This is the width used by most of the
     * tests. For exceptions see {@link #testGraphManagerProfile()},
     * {@link #testGraphManagerRectProfile()}, and 
     * {@link #prepareRectTest()}.
     */
    private static final int    defImgHeight     = 400;
    private static final int    defImgType       = 
        BufferedImage.TYPE_INT_RGB;
    
    /** Simple class name of LinePropertySetAxes. */
    private static final String             AXES        =
        LinePropertySetAxes.class.getSimpleName();
    /** Simple class name of LinePropertySetGridLines. */
    private static final String             GRID_LINES  =
        LinePropertySetGridLines.class.getSimpleName();
    /** Simple class name of LinePropertySetTicMajor. */
    private static final String             TIC_MAJOR   =
        LinePropertySetTicMajor.class.getSimpleName();
    /** Simple class name of LinePropertySetTicMinor. */
    private static final String             TIC_MINOR   =
        LinePropertySetTicMinor.class.getSimpleName();
    
    /** 
     * The factor by which to scale text 
     * when performing OCR via tess4J.
     */
    private static final float  tessScaleFactor =
        Tess4JConfig.getScaleFactor();
    /**  The font size to use when performing OCR via tess4J. */
    private static final float  tessFontSize    =
        Tess4JConfig.getFontSize();
    /** The name of the font to use when performing OCR via tess4J. */
    private static final String tessFontName    =
        Tess4JConfig.getFontName();
    
    /** Grid unit to use when testing with tess4j. */
    private static final float  tessGPU         = Tess4JConfig.getGPU();
    /** Line spacing to use when testing with tess4j. */
    private static final float  tessLPU         = 2;
    /** Text color to use when testing with tess4j. */
    private static final int    tessRGB         = 0x000000;

    /** The controlling object when performing tess4j operations. */
     final Tesseract     tesseract       = 
        Tess4JConfig.getTesseract();
    /** 
     * Profile containing the original values of the properties
     * stored in the PropertyManager. Never modified after initialization.
     * Used to restore PropertyManager to original state in the
     * {@link #beforeEach()} method.
     */
    private static final Profile        baseProfile     = new Profile();
    /** 
     * Profile to be used as needed by individual tests. Restored
     * to initial values in the {@link #beforeEach()} method.
     */
    private static final Profile        workingProfile  = new Profile();
    /** 
     * The manager of all GUI operations needed by this JUnit test.
     */
    private static GraphManagerTestGUI  testGUI;

    /** The BufferedImage managed by the test GUI. */
    private BufferedImage       workingImage;
    /** 
     * The area within the workingImage that contains the graphic
     * components managed by the test GUI. 
     */
    private Rectangle           workingRect;
    
    /** 
     * Data for use during testing. Initialized to state 0 in
     * the {@link #beforeEach()} method. Reconfigured as needed
     * by individual tests.
     * @see #initTestData
     */
    private TestData            testData        = new TestData();
    
    /**
     * Executed once, before any tests are executed. Initializes
     * the test GUI used by individual tests.
     */
    @BeforeAll
    public static void beforeAll()
    {
        GUIUtils.schedEDTAndWait( 
            () -> testGUI = new GraphManagerTestGUI( workingProfile )
        );
    }
    
    /**
     * Executed immediately before every test method. Restore the
     * PropertyManager, {@link #baseProfile} and {@link #workingProfile} 
     * to their initial states.
     * 
     * @throws Exception
     */
    @BeforeEach
    public void beforeEach()
    {
        baseProfile.apply();
        workingProfile.reset();
        testData.initTestData( 0 );
    }

    @Test
    public void testGraphManagerJComponentProfile()
    {
        @SuppressWarnings("unused")
        GraphManager    mgr = 
            new GraphManager( new JPanel(), new Profile() );
    }
    
    @Test
    public void testGraphManagerProfile()
    {
        testData.initProfile( AXES );
        prepareRectTest();
        GraphManager    test    = new GraphManager( workingProfile );
        Graphics2D      gtx     = workingImage.createGraphics();
        test.refresh( gtx, workingRect );
        validateFill( workingRect );
    }
    
    @Test
    public void testGraphManagerRectProfile()
    {
        testData.initProfile( AXES );
        prepareRectTest();
        GraphManager    test            = 
            new GraphManager( workingRect, workingProfile );
        Graphics2D      gtx             = workingImage.createGraphics();
        test.refresh( gtx );
        validateFill( workingRect );
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawBackground( int paramNum )
    {
        testData.initTestData( paramNum );
        testData.initProfile( AXES );
        workingImage   = testGUI.drawBackground();
        validateFill();
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawAxes( int paramNum )
    {
        testData.initTestData( paramNum );
        testData.initProfile( AXES );
        testDrawAxesInternal();
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawGridLines( int paramNum  )
    {
        testData.initTestData( paramNum );
        testData.initProfile( GRID_LINES );
        workingImage = testGUI.drawGridLines();
        testVerticalLines();
        testHorizontalLines();
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawMajorTics( int paramNum )
    {
        testData.initProfile( TIC_MAJOR );
        workingImage = testGUI.drawMajorTics();
        testVerticalLines();
        testHorizontalLines();
    }

    @ParameterizedTest
    @ValueSource( ints= {0,1} )
    public void testDrawMinorTics( int paramNum )
    {
        testData.initTestData( paramNum );
        testData.initProfile( TIC_MINOR );
        workingImage = testGUI.drawMinorTics();
        testVerticalLines();
        testHorizontalLines();
    }

    @Test
    public void testDrawText()
    {
        initTessTestData();
        workingImage = testGUI.drawText();
        assertTrue( hasHorizontalLabel() );
        assertTrue( hasVerticalLabel() );
    }

    @Test
    public void testDrawHorizontalLabels()
    {
        initTessTestData();
        workingImage = testGUI.drawHorizontalLabels();
        Utils.pause( 250 );
        
        List<Float>     expValues   = getExpectedHorizontalLabels();
        List<Float>     actValues   = getLabels();
        assertEquals( expValues, actValues );
    }

    @Test
    public void testDrawVerticalLabels()
    {
        initTessTestData();
        workingImage = testGUI.drawVerticalLabels();
        
        List<Float>     expValues   = getExpectedVerticalLabels();
        List<Float>     actValues   = getLabels();
        assertEquals( expValues, actValues );
    }
    
    /**
     * Validate the GraphManager.drawAll method.
     * See line property requirements at {@link #drawAllHasLine(LinePropertySet)}.
     * See text property initialization at {@link #initTessTestData()}.
     */
    @Test
    public void testDrawAll()
    {
        drawAllConfigProfile();        
        workingImage = testGUI.drawAll();
        Stream.of( AXES, GRID_LINES, TIC_MAJOR, TIC_MINOR )
            .map( s -> workingProfile.getLinePropertySet( s ) )
            .forEach( s -> assertTrue( drawAllHasLine( s ) ) );
        
        initTessTestData();
        assertTrue( hasVerticalLabel() );
        assertTrue( hasHorizontalLabel() );
    }
    
    @Test
    public void testNoGridLines()
    {
        testNoLines( GRID_LINES, testGUI::drawGridLines );
    }
    
    @Test
    public void testNoMajorTics()
    {
        testNoLines( TIC_MAJOR, testGUI::drawMajorTics );
    }
    
    @Test
    public void testNoMinorTics()
    {
        testNoLines( TIC_MINOR, testGUI::drawMinorTics );
    }
    
    @Test
    public void testNoLabels()
    {
        initTessTestData();
        testGUI.setGridDrawLabels( false );
        workingImage = testGUI.drawVerticalLabels();
        List<Float>     actValues   = getLabels();
        assertTrue( actValues.isEmpty() );
        
        workingImage = testGUI.drawHorizontalLabels();
        actValues = getLabels();
        assertTrue( actValues.isEmpty() );
    }
    
    @Test
    public void testResetProfile()
    {
        String              propSetName = TIC_MAJOR;
        LinePropertySet     propSet     = 
            workingProfile.getLinePropertySet( propSetName );
        float               origStroke  = propSet.getStroke();
        float               newStroke   = origStroke + 5;
        testGUI.setLineStroke( propSetName, newStroke );
        float               actStroke   = propSet.getStroke();
        assertEquals( newStroke, actStroke );
            
        testGUI.invokeResetProfile();
        actStroke = propSet.getStroke();
        assertEquals( origStroke, actStroke );
    }
    
    /**
     * Create a BufferedImage and a rectangle
     * with bounds that fall inside the image
     * but leave an empty margin on all sides.
     * No two margins are the same size.
     * The image is initialized to the foreground
     * color in the test data.
     * <p>
     * Postcondition:
     * {@link #workingImage} points to the created image.
     * <p>
     * Postcondition:
     * {@link #workingRect} points to the rectangle.
     */
    private void prepareRectTest()
    {
        workingImage = 
            new BufferedImage( defImgWidth, defImgHeight, defImgType );
        int             rectXco         = 10;
        int             rectYco         = rectXco + 5;
        int             right           = rectYco + 5;
        int             bottom          = right + 5;
        int             rectWidth       = defImgWidth - rectXco - right;
        int             rectHeight      = defImgHeight - rectYco - bottom;
        workingRect = 
            new Rectangle( rectXco, rectYco, rectWidth, rectHeight );
        int             intFG           = testData.fontRGB;
        for ( int row = 0 ; row < defImgHeight ; ++row )
            for ( int col = 0 ; col < defImgWidth ; ++col )
                workingImage.setRGB( col, row, intFG );
    }
    
    /**
     * Verifies that text has been drawn in the working image
     * at least partly inside the given bounding rectangle.
     * Verification passes if the rectangle
     * within the working image
     * contains at least one pixel
     * with the value {@link #tessRGB}.
     * 
     * @param rect  the given rectangle
     * 
     * @return  true 
     *      if the given image has text 
     *      within the bounds of the given rectangle
     */
    private boolean hasLabel( Rectangle rect )
    {
        boolean result  = false;
        int     maxXco  = rect.x + rect.width;
        int     maxYco  = rect.y + rect.height;
        for ( int xco = rect.x ; xco < maxXco ; ++xco )
            for ( int yco = rect.y ; yco < maxYco ; ++yco )
            {
                int rgb = workingImage.getRGB( xco, yco ) & 0xFFFFFF;
                if ( rgb == tessRGB )
                    result = true;
            }
        return result;
    }
    
    /**
     * Configure the working profile
     * in anticipation of 
     * testing the drawAll method
     * against the encapsulated LinePropertySets.
     * See profile property requirements
     * at {@link #drawAllHasLine(LinePropertySet)}.
     * <p>
     * This method does not configure the GraphPopertySet
     * for testing against text.
     * See {@link #initTessTestData()}.
     */
    private void drawAllConfigProfile()
    {
        final int   strokeAll   = 3;
        final int   ticMinorLen = 10;
        final int   ticMajorLen = 2 * ticMinorLen;
        final int   ticMinorLPU = 4;
        final int   ticMajorLPU = 2;
        final int   gridLineLPU = 1;
        final int   gpu         = 100;
        
        workingImage = testGUI.getImage();
        int     gridWidth   = workingImage.getWidth();
        int     gridHeight  = workingImage.getHeight();
        
        assertTrue( gridWidth > 2 * gpu );
        assertTrue( gridHeight > 2 * gpu );
        assertTrue( ticMajorLen < gridWidth / 4 );
        assertTrue( ticMajorLen < gridHeight / 4 );
        assertTrue( ticMajorLen >= 2 * ticMinorLen );
        assertTrue( (gpu / ticMinorLPU) > strokeAll );
        
        LinePropertySet axesSet         = 
            workingProfile.getLinePropertySet( AXES );
        LinePropertySet gridLinesSet    = 
            workingProfile.getLinePropertySet( GRID_LINES );
        LinePropertySet ticMajorSet     = 
            workingProfile.getLinePropertySet( TIC_MAJOR );
        LinePropertySet ticMinorSet     = 
            workingProfile.getLinePropertySet( TIC_MINOR );
        
        Stream.of( axesSet, gridLinesSet, ticMajorSet, ticMinorSet )
            .forEach( s -> s.setStroke( strokeAll ) );
        
        ticMinorSet.setSpacing( ticMinorLPU );
        ticMajorSet.setSpacing( ticMajorLPU );
        gridLinesSet.setSpacing( gridLineLPU );
        ticMajorSet.setLength( ticMajorLen );
        ticMinorSet.setLength( ticMinorLen );
        workingProfile.setGridUnit( gpu );
    }

    /**
     * Return true if,
     * given the prevailing test parameters,
     * the working image contains 
     * horizontal and vertical lines.
     * <p>
     * Preconditions:
     * </p>
     * <ol>
     * <li>gridUnit &gt;= 100</li>
     * <li>grid line spacing = 1</li>
     * <li>tic major spacing = 2</li>
     * <li>tic minor spacing = 4</li>
     * <li>stroke for all lines &lt; 5</li>
     * <li>stroke for all lines &gt; 1</li>
     * <li>tic major length &lt; grid width / 2</li>
     * <li>tic major length &lt; grid height / 2</li>
     * <li>tic major length gt;= minor tic length * 2</li>
     * <li>grid width &gt; 2 * grid unit</li>
     * <li>grid height &gt; 2 * grid unit</li>
     * </ol>
     * 
     * @param propSet
     * @return
     */
     private boolean drawAllHasLine( LinePropertySet propSet )
     {
        float   gridUnit    = workingProfile.getGridUnit();
        int     width       = workingImage.getWidth();
        int     height      = workingImage.getHeight();
        float   xAxisYco    = height / 2;
        float   yAxisXco    = width / 2;
        float   lineOffset  = propSet.hasSpacing() ?
           gridUnit / propSet.getSpacing() : 0;
        float   testOffset  = propSet.hasLength() ? 
            propSet.getLength() / 2 - 1 : gridUnit * .6667f;
        Color   color       = propSet.getColor();
        int     expColor    = color.getRGB() & 0xffffff;
        int     vXco        = (int)(yAxisXco + lineOffset);
        int     vYco        = (int)(xAxisYco - testOffset);
        int     vPoint      = workingImage.getRGB( vXco, vYco ) & 0xFFFFFF;
        int     hXco        = (int)(yAxisXco + testOffset);
        int     hYco        = (int)(xAxisYco - lineOffset);
        int     hPoint      = workingImage.getRGB( hXco, hYco ) & 0xFFFFFF;
        
        boolean result      = expColor == vPoint && expColor == hPoint;
        return result;
    }
    
    /**
     * Verify that the working image
     * contains no lines
     * configured according to
     * the given property set
     * if the given property set's draw property
     * is set to false.
     * The caller provides a supplier
     * that will prompt the test GUI
     * to attempt to draw the given category of lines
     * in the working image.
     * 
     * @param propSet   the given property set
     * 
     * @param getter
     *      Supplier to initiate line drawing 
     *      in the test GUI
     *      of the line category under test
     *      and obtain a working image
     */
    private void 
    testNoLines( String propSet, Supplier<BufferedImage> getter )
    {
        testData.initProfile( propSet );
        testGUI.setLineDraw( propSet, false );
        workingImage = getter.get();
        
        LineGenerator       lineGen = 
            getLineGenerator( LineGenerator.BOTH );
        Iterator<Line2D>    iter    = lineGen.iterator();
        assertTrue( iter.hasNext() );
        for ( Line2D line : lineGen )
        {
            int xco     = (int)line.getX1();
            int yco     = (int)line.getY1();
            int actRGB  = 
                workingImage.getRGB( xco, yco ) & 0xffffff;
            assertEquals( testData.gridRGB, actRGB );
        }
    }
    
    /**
     * Verify that,
     * given the currently configured test data,
     * vertical lines are correctly
     * drawn in the working image.
     */
    private void testVerticalLines()
    {
        LineGenerator   lineGen     = 
            getLineGenerator( LineGenerator.VERTICAL );
        float   testStroke  = testData.stroke;
        int     testRGB     = testData.lineRGB;
        for ( Line2D line : lineGen )
        {
            LineSegment expSeg  = 
                LineSegment.ofVertical( line, testStroke, testRGB );
            LineSegment actSeg  = 
                LineSegment.of( line.getP1(), workingImage );
            assertEquals( expSeg, actSeg );
        }
    }
    
    /**
     * Verify that,
     * given the currently configured test data,
     * horizontal lines are correctly
     * drawn in the working image.
     */
    private void testHorizontalLines()
    {
        LineGenerator   lineGen     = 
            getLineGenerator( LineGenerator.HORIZONTAL );
        for ( Line2D line : lineGen )
        {
            float   stroke  = testData.stroke;
            int     rgb     = testData.lineRGB;
            LineSegment expSeg  = 
                LineSegment.ofHorizontal( line, stroke, rgb );
            LineSegment actSeg  = 
                LineSegment.of( line.getP1(), workingImage );
            assertEquals( expSeg, actSeg );
        }
    }
    
    /**
     * Return true if at least one label
     * can be detected on the horizontal tic marks
     * in the working image.
     * 
     * @return  true if at least one horizontal label can be detected
     */
    private boolean hasHorizontalLabel()
    {
        LineGenerator       lineGen = 
            getLineGenerator( LineGenerator.HORIZONTAL );
        Iterator<Line2D>    iter    = lineGen.iterator();
        assertTrue( iter.hasNext() );
        
        Line2D      line    = iter.next();
        int         testWidth   = 20;
        int         testHeight  = 20;
        int         xco     = (int)line.getX2();
        int         yco     = (int)(line.getY1() - testHeight / 2);
        Rectangle   rect    = 
           new Rectangle( xco, yco, testWidth, testHeight );
        boolean             result  = hasLabel( rect );
        return result;
    }
    
    private boolean hasVerticalLabel()
    {
        LineGenerator       lineGen = 
            getLineGenerator( LineGenerator.VERTICAL );
        Iterator<Line2D>    iter    = lineGen.iterator();
        assertTrue( iter.hasNext() );
        
        int         testWidth   = (int)(testData.gpu / 2);
        int         testHeight  = testWidth;
        Line2D      line    = iter.next();
        int         xco     = (int)line.getX1() - testWidth / 2;
        int         yco     = (int)line.getY2();
        Rectangle   rect    = 
           new Rectangle( xco, yco, testWidth, testHeight );
        boolean             result  = hasLabel( rect );
        return result;
    }
    
    /**
     * Configure the test data
     * for processing with Tesseract.
     */
    private void initTessTestData()
    {
        testData.initProfile( TIC_MAJOR );
        testGUI.setGridFontSize( tessFontSize );
        testGUI.setGridFontName( tessFontName );
        testGUI.setGridUnit( tessGPU );
        testGUI.setGridFontRGB( tessRGB );
        testGUI.setLineSpacing( TIC_MAJOR, tessLPU );
    }
    
    /**
     * Verify that the background image
     * is set to the current background color
     * only in the given range.
     * 
     * @param rect  the given range
     */
    private void validateFill( Rectangle rect )
    {
        int             rows            = workingImage.getHeight();
        int             cols            = workingImage.getWidth();
        for ( int row = 0 ; row < rows ; ++row )
            for ( int col = 0 ; col < cols ; ++col )
            {
                int rgb = workingImage.getRGB( col, row ) & 0xFFFFFF;
                String  msg = "col=" + col + ",row=" + row;
                if ( rect.contains( col, row  ) )
                    assertEquals( testData.gridRGB, rgb, msg );
                else
                    assertNotEquals( testData.gridRGB, rgb, msg );
            }
    }
    
    /**
     * Verify that every value in the working image
     * is equal to the expected background color.
     */
    private void validateFill()
    {
        int             width   = workingImage.getWidth();
        int             height  = workingImage.getHeight();
        Rectangle       rect    = 
            new Rectangle( 0, 0, width, height );
        validateFill( rect );
    }
    
    private void testDrawAxesInternal()
    {
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
        
        float   stroke  = testData.stroke;
        int     rgb     = testData.lineRGB;
        LineSegment         expSegX = 
            LineSegment.ofHorizontal( expXAxis, stroke, rgb );
        LineSegment         expSegY = 
            LineSegment.ofVertical( expYAxis, stroke, rgb );
        LineSegment         actSegX = 
            LineSegment.of( expXAxis.getP1(), workingImage );
        LineSegment         actSegY = 
            LineSegment.of( expYAxis.getP1(), workingImage );
        assertEquals( expSegX, actSegX );
        assertEquals( expSegY, actSegY );
    }
    
    /**
     * Given the currently configured test data,
     * compile a list of values
     * we expect to find displayed
     * on the horizontal tic marks
     * drawn in the working image.
     * Vaues are ordered from lowest to highest
     * (top to bottom on the y-axis).
     * 
     * @return
     *      a list of values we expect to find displayed
     *      on the horizontal tic marks
     */
    private List<Float> getExpectedHorizontalLabels()
    {
        List<Float>     list    = new LinkedList<>();
        float           spacing = testData.gpu / testData.lpu;
        float           xAxis   = workingImage.getHeight() / 2;

        int             xier    = 1;
        for ( float mark = xAxis - spacing ; mark > 0 ; mark -= spacing )
        {
            float   val = xier++ / testData.lpu; 
            list.add( 0, val );
            list.add( -val );
        }
        return list;
    }
    
    /**
     * Given the currently configured test data,
     * compile a list of values
     * we expect to find displayed
     * on the horizontal tic marks
     * drawn in the working image.
     * Vaues are ordered from lowest to highest
     * (top to bottom on the y-axis).
     * 
     * @return
     *      a list of values we expect to find displayed
     *      on the horizontal tic marks
     */
    private List<Float> getExpectedVerticalLabels()
    {
        List<Float>     list    = new ArrayList<>();
        float           spacing = testData.gpu / testData.lpu;
        float           yAxis   = workingImage.getWidth() / 2;

        int             xier    = -1;
        for ( float mark = yAxis - spacing ; mark > 0 ; mark -= spacing )
        {
            float   val = xier-- / testData.lpu; 
            list.add( 0, val );
            list.add( -val );
        }
        return list;
    }
    
    /**
     * From the working image,
     * extract the text on the major tic marks,
     * and convert it to a list
     * of decimal values.
     * If conversion fails
     * a partial list may be returned.
     * 
     * @return  
     *      a list of the values displayed on the major tic marks
     *      in the working image
     */
    private List<Float> getLabels()
    {
        List<Float>     list            = null;
        
        // Allocate new buffered image
        int             scaledWidth     = 
            (int)(workingImage.getWidth() * tessScaleFactor + .5);
        int             scaledHeight    = 
            (int)(workingImage.getHeight() * tessScaleFactor + .5);
        int             scaledType      = workingImage.getType();
        BufferedImage   scaledImage     = 
            new BufferedImage( scaledWidth, scaledHeight, scaledType );

        // Scale the image
        Image   image   = 
            workingImage.getScaledInstance( 
                scaledWidth, 
                scaledHeight, 
                Image.SCALE_REPLICATE 
            );
        Graphics2D  gtx = scaledImage.createGraphics();
        gtx.drawImage( image, 0, 0, null );

        // Perform OCR
        try
        {
            String      tessStr = tesseract.doOCR( scaledImage );
            list = parseFloats( tessStr );
        }
        catch ( TesseractException exc )
        {
            String  message = "Fatal Tesseract error";
            fail( message, exc );
        }
        return list;
    }
    
    /**
     * Convert a string containing
     * formatted decimal numbers separated by whitespace
     * into a list of decimal values.
     * 
     * @param string    the string to convert
     * 
     * @return  the compiles list
     */
    private List<Float> parseFloats( String string )
    {
        List<Float>     list    = new ArrayList<>();
        String[]        tokens  = string.split( "\\s" );
        for ( String token : tokens )
        {
            try
            {
                float   val = Float.parseFloat( token );
                list.add( val );
            }
            catch ( NumberFormatException exc )
            {
                String  msg = "OCR parse failure; token = " + token;
                System.err.println( msg );
            }
        }
        return list;
    }
   
    /**
     * Instantiates a LineGenerator.
     * The orientation of the LineGenerator
     * is provided by the caller.
     * All other parameters
     * are taken from the
     * currently configured test data.
     * 
     * @param orientation   
     *      the orientation of the instantiated LineGenerator
     * 
     * @return  the instantiated LineGenerator
     */
    private LineGenerator getLineGenerator( int orientation )
    {
        boolean hasLength   = testGUI.getLineHasLength( testData.lineSet );
        float   length      = hasLength ? testData.length : -1;
        
        LineGenerator   lineGen = new LineGenerator( 
            getBoundingRectangle(), 
            testData.gpu, 
            testData.lpu, 
            length, 
            orientation
        );
        return lineGen;
    }

    /**
     * Create a rectangle that describes the bounds
     * of the working image.
     *
     * @return 
     *      a rectangle that describes the bounds
     *      of the working image
     */
    /**
     * @return
     */
    private Rectangle2D getBoundingRectangle()
    {
        int         width   = workingImage.getWidth();
        int         height  = workingImage.getHeight();
        Rectangle2D rect    = 
            new Rectangle2D.Double( 0, 0, width, height );
        return rect;
    }
    
    /**
     * Class to encapsulate data for testing a GraphManager feature.
     * The data include the grid unit (GPU) from the Profile class,
     * the grid color and font color from a Profile object's
     * encapsulated GridPropertySetMW, 
     * and the LPU, length, stroke, and color properties
     * from one of the Profile's encapsulated LinePropertySets.
     * The user can choose from two distinct sets of data,
     * the primary set and the secondary set; 
     * see {@link #initTestData(int)}.
     * 
     * @author Jack Straub
     */
    private static class TestData
    {
        /** Primary GPU value. */
        private static final float  GPU         = 100;
        /** Primary font RGB value. */
        private static final int    FONT_RGB    = 0x000008;
        /** Primary grid RGB value. */
        private static final int    GRID_RGB    = 0xEEEEEE;
        /** Primary LPU value. */
        private static final float  LPU         = 2;
        /** Primary length value. */
        private static final float  LENGTH      = 20;
        /** Primary stroke value. */
        private static final float  STROKE      = 4;
        /** Primary line RGB value. */
        private static final int    LINE_RGB    = 0x00000F;
        
        /** 
         * The name of the encapsulated LinePropertySet,
         * determined by {@link #initProfile(String)}.
         */
        public  String    lineSet;
        /** The GPU, determined by {@link #initTestData(int)}. */
        public  float     gpu;
        /** The font RGB value, determined by {@link #initTestData(int)}. */
        public  int       fontRGB;
        /** The grid RGB value, determined by {@link #initTestData(int)}. */
        public  int       gridRGB;
        /** 
         * The grid color, 
         * determined by {@link #initTestData(int)}, 
         * derived from {@link #gridRGB}. 
         */
        public  Color     gridColor;
        /** The LPU, determined by {@link #initTestData(int)}. */
        public  float     lpu;
        /** The line length, determined by {@link #initTestData(int)}. */
        public  float     length;
        /** The line weight, determined by {@link #initTestData(int)}. */
        public  float     stroke;
        /** The the line RGB, determined by {@link #initTestData(int)}. */
        public  int       lineRGB;
        /** 
         * The line color, 
         * determined by {@link #initTestData(int)}, 
         * derived from {@link #lineRGB}. 
         */
        public  Color     lineColor;
        
        /**
         * Initializes the fields of this object
         * to values from the primary or secondary data set.
         * To select the secondary data set
         * pass an argument of 1.
         * Any other choice of argument
         * will select the primary data set.
         * 
         * @param dataSet
         *      value to choose between the primary and
         *      secondary data sets
         */
        public void initTestData( int dataSet )
        {
            fontRGB = FONT_RGB;
            gridRGB = GRID_RGB;
            gridColor = new Color( gridRGB );
            gpu = GPU;
            lpu = LPU;
            length = LENGTH;
            stroke = STROKE;
            lineRGB = LINE_RGB;
            lineColor = new Color( lineRGB );
            if ( dataSet == 1 )
            {
                fontRGB = (fontRGB << 2) & 0xFFFFFF;
                gridRGB = (gridRGB << 2) & 0xFFFFFF;
                gridColor = new Color( gridRGB );
                gpu += 20;
                lpu += 2;
                length += 10;
                stroke += 2;
                lineRGB = (lineRGB << 2) & 0xFFFFFF;
                lineColor = new Color( lineRGB );
            }
        }
        
        /**
         * Transfers data from this object to the test Profile,
         * the Profile's encapsulated GraphPropertySetMW,
         * and the Profile's given LinePopertySet.
         * 
         * @param propSet   the given LinePropertySet
         */ 
        public void initProfile( String propSet )
        {
            lineSet = propSet;
            testGUI.setGridUnit( gpu );
            testGUI.setGridColor( gridColor );
            testGUI.setGridFontRGB( fontRGB );
            testGUI.setLineSpacing( propSet, lpu );
            testGUI.setLineStroke( propSet, stroke );
            testGUI.setLineLength( propSet, length );
            testGUI.setLineColor( propSet, lineColor );
            testGUI.setLineDraw( propSet, true );
        }
    }
}
