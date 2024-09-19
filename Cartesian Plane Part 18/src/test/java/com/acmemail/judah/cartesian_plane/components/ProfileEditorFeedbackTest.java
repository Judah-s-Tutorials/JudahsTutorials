package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.LineSegment;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorFeedbackTestGUI;

public class ProfileEditorFeedbackTest
{
    /** The simple name of the LinePropertySetAxes class. */
    private static final String axesSet         =
        LinePropertySetAxes.class.getSimpleName();
    /** The simple name of the LinePropertySetGridLines class. */
    private static final String gridLinesSet    =
        LinePropertySetGridLines.class.getSimpleName();
    /** The simple name of the LinePropertySetTicMajor class. */
    private static final String ticMajorSet     =
        LinePropertySetTicMajor.class.getSimpleName();
    /** The simple name of the LinePropertySetTicMinor class. */
    private static final String ticMinorSet     =
        LinePropertySetTicMinor.class.getSimpleName();
    
    private static final Color      defColor    = Color.RED;
    private static final Color      extColor    = Color.BLUE;
    private static final float      defGridUnit = 175;
    private static final float      extGridUnit = 2 * defGridUnit;
    private static final Color      defBGColor  = new Color( 0xEFEFEF );
    private static final Color      extBGColor  = Color.WHITE;
    
    private static final String     defFontName     = Font.DIALOG;
    private static final String     extFontName     = Font.MONOSPACED;
    private static final float      defFontSize     = 20;
    private static final float      extFontSize     = 2 * defFontSize;
    private static final boolean    defFontBold     = false;
    // The bold properly is handled explicitly in the bold test,
    // hence no extFontBold variable.
    private static final boolean    defFontItalic   = false;
    // The bold properly is handled explicitly in the italic test,
    // hence no extFontItalic variable.
    private static final Color      defFontColor    = Color.BLUE;
    private static final Color      extFontColor    = Color.GREEN;
    private static final float      defWidth        = 750;
    // there is no test based on changing size, hence no extWidth variable
    
    private static final float      defLength       = 20;
    private static final float      extLength       = 2 * defLength;
    private static final float      defStroke       = 2;
    private static final float      extStroke       = 2 * defStroke;
    private static final float      defSpacing      = 5;
    private static final float      extSpacing      = 2 * defSpacing;
    
    /**
     * Represents the properties of a Profile as determined by the
     * PropertyManager. Used as needed to return the PropertyManager
     * to its original state (see for example, {@link #beforeEach()}.
     * Never modified after initialization.
     */
    private static Profile          baseProfile     = new Profile();
    /** 
     * Profile used to initialize the test GUI/ProfileEditor.
     * After initialization the reference must not be changed,
     * but the contents of the object may be changed as needed.
     * The contents are restored to their original values before
     * each test (see {@link #beforeEach()}).
     */
    private static Profile              profile = new Profile();
    /** 
     * The object that displays and manager the ProfileEditor.
     * Guarantees that all interaction with the ProfileEditor
     * components is conducted via the EDT.
     */
    private static ProfileEditorFeedbackTestGUI  testGUI;
    
    @BeforeAll
    public static void beforeAll()
    {
        initBaseProfile();
        profile = new Profile();
        testGUI = ProfileEditorFeedbackTestGUI.getTestGUI( profile );
        testGUI.repaint();
    }
    
    @AfterAll
    public static void afterAll()
    {
        // For the sake of tests run in suites, make sure
        // the original profile is restored at the end of the test.
        // Also make sure all GUI windows are disposed.
        baseProfile.apply();
        ComponentFinder.disposeAll();
    }
    
    @BeforeEach
    public void beforeEach()
    {
        baseProfile.apply();
        profile.reset();
        testGUI.repaint();
    }

    @Test
    public void testAxes()
    {
        LinePropertySet propSet     = 
            profile.getLinePropertySet( axesSet );
        validateAxes();
        
        propSet.setColor( extColor );
        propSet.setStroke( extStroke );
        validateAxes();
    }

    @ParameterizedTest
    @ValueSource( strings= {
        "LinePropertySetGridLines",
        "LinePropertySetTicMajor",
        "LinePropertySetTicMinor"
    })
    public void testLines( String propSetName )
    {
        LinePropertySet propSet     = 
            profile.getLinePropertySet( propSetName );
        LineEvaluator  evalA   = new LineEvaluator( propSetName );
        evalA.validateVertical();
        evalA.validateHorizontal();
        
        profile.setGridUnit( extGridUnit );
        propSet.setColor( extColor );
        propSet.setStroke( extStroke );
        propSet.setSpacing( extSpacing );
        if ( propSet.hasLength() )
            propSet.setLength( extLength );
        LineEvaluator  evalB   = new LineEvaluator( propSetName );
        evalB.validateVertical();
        evalB.validateHorizontal();
    }
    
    @Test
    public void testGridColor()
    {
        GraphPropertySet    props   = profile.getMainWindow();
        BufferedImage       image   = testGUI.getImage();
        int                 xco     = image.getWidth() / 4;
        int                 yco     = image.getHeight() / 4;
        int                 actRGB  = image.getRGB( xco, yco ) & 0xFFFFFF;
        int                 expRGB  = getRGB( props.getBGColor() );
//        waitOp();
        assertEquals( actRGB, expRGB );
        
        expRGB = getRGB( extBGColor );
        props.setBGColor( extBGColor );
        testGUI.repaint();
        image= testGUI.getImage();
        actRGB = image.getRGB( xco, yco ) & 0xFFFFFF;
//        //waitOp();
        assertEquals( actRGB, expRGB );
    }
    
    /**
     * Measure the bounds of a text label
     * with the default font size,
     * then change to the extended font size
     * and measure the bounds of the label
     * with the larger size.
     * Verify that the bounds of the label
     * with the larger font
     * is greater than  the bounds of the label
     * with the smaller font.
     */
    @Test
    public void testFontSize()
    {
        // Sanity check: the default label font size is expected to be
        // smaller than the extended font size.
        assertTrue( defFontSize < extFontSize );
        
        GraphPropertySet    graph       = profile.getMainWindow();

        // Get a rectangle that encloses the text at the smaller font size
        ImageRect   rectA   = getTextRect();
        
        // Get a rectangle that encloses the text at the larger font size
        graph.setFontSize( extFontSize );
        ImageRect   rectB   = getTextRect();
        
        // Verify that the bounds of the text in the smaller font
        // is less than the bounds of the text in the larger font.
        assertTrue( rectA.getWidth() < rectB.getWidth() );
        assertTrue( rectA.getHeight() < rectB.getHeight() );
    }
    
    /**
     * Verify that label text is displayed in bold
     * after the fontBold property is changed to true.
     * <p>
     * Precondition: the grid background color is <em>colorA</em>,
     * the font color is <em>colorB</em>, 
     * and <em>colorA</em> is not equal to <em>colorB</em>.
     * <p>
     * Obtain the rectangle enclosing a label
     * display in non-bold text.
     * Change the fontBold property to true
     * and obtain its new bounding rectangle.
     * Verify that the ratio <em>colorA</em>/<em>colorB</em>
     * is greater in the second rectangle.
     */
    @Test
    public void testFontBold()
    {
        GraphPropertySet    graph   = profile.getMainWindow();
        int                 rgb     = getRGB( graph.getFGColor() );

        graph.setBold( false );
        ImageRect   rectA   = getTextRect();
        double      countA  = rectA.count( rgb );
//        waitOp();
        
        graph.setBold( true );
        ImageRect   rectB   = getTextRect();
        double      countB  = rectB.count( rgb );
//        //waitOp();
        
        
        // Verify that the plain text bounding box contains fewer
        // pixels of the text color than the bold text bounding box.
        assertTrue( countA < countB );
    }
    
    /**
     * Verify that label text is redisplayed in 
     * after the fontColor property is changed.
     * <p>
     * Verify that a rectangle enclosing a label
     * in the default color 
     * can be obtained.
     * Change the font color.
     * Verify that a rectangle enclosing a label
     * in the new color 
     * can be obtained.
     */
    @Test
    public void testFontColor()
    {
        GraphPropertySet    graph   = profile.getMainWindow();

        // If the following operation succeeds it means that labels
        // are displayed in the default color.
        ImageRect   rectA   = getTextRect();
        assertNotNull( rectA );

        graph.setFGColor( extFontColor );
        // If the following operation succeeds it means that labels
        // are displayed in the new color.
        ImageRect   rectB   = getTextRect();
        assertNotNull( rectB );
//        waitOp();
    }
    
    /**
     * Verify that label text is displayed in italics
     * after the fontItalic property is changed to true.
     * <p>
     * Precondition: the grid background color is <em>colorA</em>,
     * the font color is <em>colorB</em>, 
     * and <em>colorA</em> is not equal to <em>colorB</em>.
     * <p>
     * Obtain the rectangle enclosing a label
     * display in non-italic text.
     * Change the fontItalic property to true
     * and obtain its new bounding rectangle.
     * Verify that the content 
     * of the first rectangle
     * is different from the content
     * of the second rectangle.
     */
    @Test
    public void testFontItalic()
    {
        GraphPropertySet    graph   = profile.getMainWindow();

        graph.setItalic( false );
        ImageRect   rectA   = getTextRect();
//        //waitOp();
        
        graph.setItalic( true );
        ImageRect   rectB   = getTextRect();
//        //waitOp();
        assertNotEquals( rectA, rectB );
    }
    
    /**
     * Verify that the displayed label text
     * changes when the font name changes.
     * <p>
     * Precondition: the grid background color is <em>colorA</em>,
     * the font color is <em>colorB</em>, 
     * and <em>colorA</em> is not equal to <em>colorB</em>.
     * <p>
     * Obtain the rectangle enclosing a label
     * display in non-italic text.
     * Change the fontItalic property to true
     * and obtain its new bounding rectangle.
     * Verify that the content 
     * of the first rectangle
     * is different from the content
     * of the second rectangle.
     */
    @Test
    public void testFontName()
    {
        GraphPropertySet    graph   = profile.getMainWindow();

        ImageRect   rectA   = getTextRect();
//        //waitOp();
        
        graph.setFontName( extFontName );
        ImageRect   rectB   = getTextRect();
//        //waitOp();
        assertNotEquals( rectA, rectB );
    }
    
    /**
     * Verify that the test display changes correctly
     * when the grid unit is changed.
     * <ol>
     * <li>
     * Turn on display of grid lines and major tics.
     * </li>
     * <li>
     * Set spacing for grid lines to  1 and major tics to 2.
     * </li>
     * <li>
     * Obtain an image of the display. 
     * Verify that there is a grid line at unit distance of 1.
     * Verify that there is a major tic at unit distance .5.
     * </li>
     * <li>
     * Change the grid unit.
     * </li>
     * <li>
     * Obtain an image of the display. 
     * Verify that there is a grid line at (new) unit distance of 1.
     * Verify that there is a major tic at (new) unit distance .5.
     * </li>
     * </ol>
     */
    @ParameterizedTest
    @ValueSource( 
        strings= {"LinePropertySetGridLines", "LinePropertySetTicMajor"}
    )
    public void testGridUnit( String propSet ) 
    {
        beforeEach();
        LineEvaluator   lineEvalA   = new LineEvaluator( propSet );
        lineEvalA.validateVertical();
        
        
        LineEvaluator   lineEvalB   = new LineEvaluator( propSet );
        profile.setGridUnit( extGridUnit );
        lineEvalB.validateVertical();
    }
    
    /**
     * Get an image of the test GUI with labels turned on,
     * and calculate the minimum rectangle that encloses one label.
     * 
     * @return the calculated rectangle
     */
    private ImageRect getTextRect()
    {
        LinePropertySet     axes        = 
            profile.getLinePropertySet( axesSet );
        LinePropertySet     ticMajor    = 
            profile.getLinePropertySet( ticMajorSet );
        GraphPropertySet    graph       = profile.getMainWindow();
        Color               textColor   = graph.getFGColor();
        
        // Sanity check. The text color should be different from the
        // axis color and the major tic color in order to eliminate
        // possible confusion.
        assertNotEquals( textColor, axes.getColor() );
        assertNotEquals( textColor, ticMajor.getColor() );
        
        // Turn on display of major tics in case the tester requires
        // visual confirmation from the GUI; displayed correctly, the 
        // text will be centered below the first major tic on the x-axis.
        ticMajor.setDraw( true );
        
        // Labels are displayed on each major tic. With spacing
        // set to one a major tic will be drawn at one unit distance
        // to the right of the y axis.
        ticMajor.setSpacing( 1 );
        
        // Turn on display of labels; get an image of the GUI, but
        // also redisplay the GUI in case visual inspection is desired.
        graph.setFontDraw( true );
        testGUI.repaint();
        BufferedImage       image       = testGUI.getImage();
        //waitOp();
        
        // To locate the center/top of text, start with x= 1 unit 
        // to the right of the y-axis (the same as the first major tic)
        // and y= one-half the length of a major tic below the x-axis.
        double      yAxisXco    = image.getWidth() / 2;
        double      xAxisYco    = image.getHeight() / 2;
        double      spacing     = profile.getGridUnit();
        double      ticLen      = ticMajor.getLength() / 2;
        double      midX        = yAxisXco + spacing;
        double      topY        = xAxisYco + ticLen / 2;
        
        // Given that the text to be located is four characters
        // ("1.00") estimate the left edge of the text to be no more 
        // midX - 2.5 * font-size and the right edge to be no more than
        // midX + 2.5 * font-size. Assume there are no more than 10 
        // pixels of padding between the tic mark and the text, and the
        // maximum height of the text to be no more than 1.5 * the 
        // font-size. For accurate results the width of the search area 
        // should be no more than 1 unit.
        double      fontSize    = graph.getFontSize();
        double      leftX       = midX - 2 * fontSize;
        double      width       = 4 * fontSize;
        double      maxPadding  = 10;
        double      height      = 1.5 * fontSize + maxPadding;
        assertTrue( width < spacing, width + "," + spacing );
        
        int         color       = getRGB( graph.getFGColor() );
        Rectangle2D rect        = 
            new Rectangle2D.Double( leftX, topY, width, height );
        LineSegment lineSeg     = LineSegment.ofRect( rect, image, color );
        Rectangle2D resultRect  = lineSeg.getBounds();
        assertNotNull( resultRect );
        ImageRect   result      = new ImageRect( image, resultRect );
        return result;
    }
    
    @SuppressWarnings("unused")
    private static void waitOp()
    {
        JOptionPane.showMessageDialog( null, "Waiting..." );
    }
    
    private void validateAxes()
    {
        testGUI.repaint();
        
        LinePropertySet props       = 
            profile.getLinePropertySet( axesSet );
        int             width       = testGUI.getWidth();
        int             height      = testGUI.getHeight();
        float           yTestCo     = height / 4f;
        float           yCenter     = height / 2f;
        float           xTestCo     = width / 4f;
        float           xCenter     = width / 2f;
        Point2D         origin      = null;
        float           stroke      = props.getStroke();
        int             expColor    = getRGB( props.getColor() );
        BufferedImage   image       = testGUI.getImage();
        
        // On y-axis, above x-axis
        origin = new Point2D.Float( xCenter, yTestCo );
        LineSegment yAxis       = LineSegment.of( origin, image );
        Rectangle2D yBounds     = yAxis.getBounds();
        int         yColor      = getRGB( origin, image );
        
        // On x-axis, left of y-axis
        origin = new Point2D.Float( xTestCo, yCenter );
        LineSegment xAxis   = LineSegment.of( origin, image );
        Rectangle2D xBounds = xAxis.getBounds();
        int         xColor  = getRGB( origin, image );

        assertTrue( yBounds.getHeight() >= height );
        assertEquals( yBounds.getWidth(), stroke );
        assertEquals( expColor, yColor );

        assertEquals( xBounds.getHeight(), stroke );
        assertTrue( xBounds.getWidth() >= width );
        assertEquals( expColor, xColor );
    }

    private static void initBaseProfile()
    {
        baseProfile.setGridUnit( defGridUnit );
        initBaseGraphProperties();
        Stream.of(
            axesSet,
            gridLinesSet,
            ticMajorSet,
            ticMinorSet
        ).forEach( s -> initBaseLineProperties( s ) );
        
        
        baseProfile.apply();
    }
    
    private static void initBaseGraphProperties()
    {
        GraphPropertySet    baseGraph   = baseProfile.getMainWindow();
        baseGraph.setBGColor( defBGColor );
        baseGraph.setBold( defFontBold );
        baseGraph.setFGColor( defFontColor );
        baseGraph.setFontName( defFontName );
        baseGraph.setFontSize( defFontSize );
        baseGraph.setItalic( defFontItalic );
        baseGraph.setWidth( defWidth );
        baseGraph.setFontDraw( false );
    }
    
    private static void initBaseLineProperties( String propSet )
    {
        LinePropertySet baseSet = 
            baseProfile.getLinePropertySet( propSet );
        baseSet.setLength( defLength );
        baseSet.setSpacing( defSpacing );
        baseSet.setStroke( defStroke );
        baseSet.setColor( defColor );
        baseSet.setDraw( false );
    }
    
    private static int getRGB( Point2D point, BufferedImage image )
    {
        int     xco     = (int)(point.getX());
        int     yco     = (int)(point.getY());
        int     rgb     = image.getRGB( xco, yco ) & 0xffffff;
        return rgb;
    }
    
    private static int getRGB( Color color )
    {
        int     iColor  = color.getRGB() & 0xFFFFFF;
        return iColor;
    }
    
    private class ImageRect
    {
        private final int[][]   data;
        
        public ImageRect( BufferedImage image, Rectangle2D rect )
        {
            int width       = (int)rect.getWidth();
            int height      = (int)rect.getHeight();
            int firstRow    = (int)rect.getY();
            int firstCol    = (int)rect.getX();
            data = new int[height][width];
            
            int row         = firstRow;
            for ( int yco = 0 ; yco < height ; ++yco, row++ )
            {
                int col = firstCol;
                for ( int xco = 0 ; xco < width ; ++xco, ++col )
                    data[yco][xco] = image.getRGB( col, row ) & 0xFFFFFF;
            }
        }
        
        private int count( int rgb )
        {
            long    count   = stream()
                .filter( i -> i == rgb )
                .count();
            return (int)count;
        }
        
        public int getWidth()
        {
            int width   = 0;
            if ( data.length > 0 )
                width = data[0].length;
            return width;
        }
        
        public int getHeight()
        {
            int height  = data.length;
            return height;
        }
        
        public IntStream stream()
        {
            IntStream   stream  =
                Arrays.stream( data )
                    .flatMapToInt( r -> Arrays.stream( r ) );
            return stream;
        }
        
        @Override
        public int hashCode()
        {
            int hash    = Arrays.deepHashCode( data );
            return hash;
        }
        
        @Override
        public boolean equals( Object obj )
        {
            boolean result  = false;
            if ( obj == null )
                result = false;
            else if ( obj == this )
                result = true;
            else if ( !(obj instanceof ImageRect) )
                result = false;
            else
            {
                ImageRect   that    = (ImageRect)obj;
                result = Arrays.deepEquals(this.data, that.data );
            }
            return result;
        }
    }
    
    /**
     * For the current Profile
     * and a given LinePropertySet
     * collect the data needed to validate
     * the properties of the encapsulated lines,
     * and perform the validation.
     * Two consecutive vertical lines
     * and two consecutive horizontal lines
     * are selected.
     * The spacing between the lines,
     * and their length, stroke, and color
     * are verified.
     * 
     * @author Jack Straub
     * 
     * @see #validateHorizontal()
     * @see #validateVertical()
     */
    private class LineEvaluator
    {
        /** 
         * The simple name of the LinePropertySet subclass
         * being evaluated.
         */
        private final String        propSetName;
        /** The width of the containing window. */
        private final int           width;
        /** The height of the containing window. */
        private final int           height;
        /** The y-coordinate of the x-axis. */
        private final int           xAxisYco;
        /** The x-coordinate of the y-axis. */
        private final int           yAxisXco;
        /** The expected length of each line. */
        private final float         expLength;
        /** The expected stroke of each line. */
        private final float         expStroke;
        /** The expected spacing between lines. */
        private final float         expSpacing;
        /** The expected line color. */
        private final int           expColor;
        /** An image of the window containing the lines to be validated. */
        private final BufferedImage image;

        /**
         * Constructor.
         * Collects the data needed for line validation,
         * and creates the image containing the lines
         * to be validated.
         * The color of the axes
         * is set to the color of the lines
         * to be evaluated,
         * which facilitates calculating 
         * the length of a line
         * that crosses an axis.
         * The stroke of the axes is set to 1,
         * which prevents the width of the axes
         * from overlapping the lines
         * to be validated.
         * 
         * @param propSetName   
         *      the name of the LinePropertySet to be validated
         */
        public LineEvaluator( String propSetName )
        {
            LinePropertySet props   = 
                profile.getLinePropertySet( propSetName );
            props.setDraw( true );
            
            LinePropertySet axesProps   = 
                profile.getLinePropertySet( axesSet );
            Color           lineColor   = props.getColor();
            axesProps.setColor( lineColor );
            axesProps.setStroke( 1 );
            
            testGUI.repaint();
            this.propSetName = propSetName;
            width  = testGUI.getWidth();
            height = testGUI.getHeight();
            xAxisYco = height / 2;
            yAxisXco = width / 2;
            expLength = props.getLength();
            expStroke = props.getStroke();
            expColor = getRGB( lineColor );
            expSpacing = profile.getGridUnit() / props.getSpacing();
            image = testGUI.getImage();            
        }
        
        public void validateVertical()
        {
            float   oXco        = yAxisXco + expSpacing;
            float   oYco        = expLength > 0 ?
                xAxisYco + expLength / 4 : expSpacing / 2;
            Point2D     origin  = new Point2D.Double( oXco, oYco );
            LineSegment seg1    = LineSegment.of( origin, image );
            Rectangle2D bounds1 = seg1.getBounds();
            
            origin.setLocation( oXco + expSpacing, oYco );
            LineSegment seg2    = LineSegment.of( origin, image );
            Rectangle2D bounds2 = seg2.getBounds();
            
            double      actSpacing  = bounds2.getX() - bounds1.getX();
            double      actLength   = bounds1.getHeight();
            double      actStroke   = bounds1.getWidth();
            int         actColor    = seg1.getColor();
//            //waitOp();

            assertEquals( actStroke, bounds2.getWidth() );
            assertEquals( actLength, bounds2.getHeight() );
            assertEquals( actColor, seg2.getColor() );
            
            if ( expLength > 0 )
                assertEquals( expLength, actLength, propSetName );
            else
                assertTrue( actLength >= height );
            assertEquals( expSpacing, actSpacing, propSetName );
            assertEquals( expStroke, actStroke, propSetName );
            assertEquals( expColor, actColor, propSetName );
        }
        
        public void validateHorizontal()
        {
            float   oYco        = xAxisYco + expSpacing;
            float   oXco        = expLength > 0 ?
                yAxisXco + expLength / 4 : expSpacing / 2;
            Point2D     origin  = new Point2D.Double( oXco, oYco );
            LineSegment seg1    = LineSegment.of( origin, image );
            Rectangle2D bounds1 = seg1.getBounds();
            
            origin.setLocation( oXco, oYco + expSpacing );
            LineSegment seg2    = LineSegment.of( origin, image );
            Rectangle2D bounds2 = seg2.getBounds();
            
            double      actSpacing  = bounds2.getY() - bounds1.getY();
            double      actLength   = bounds1.getWidth();
            double      actStroke   = bounds1.getHeight();
            int         actColor    = seg1.getColor();
//            //waitOp();

            assertEquals( actStroke, bounds2.getHeight() );
            assertEquals( actLength, bounds2.getWidth() );
            assertEquals( actColor, seg2.getColor() );
            
            if ( expLength > 0 )
                assertEquals( expLength, actLength, propSetName );
            else
                assertTrue( actLength >= width );
            assertEquals( expSpacing, actSpacing, propSetName );
            assertEquals( expStroke, actStroke, propSetName );
            assertEquals( expColor, actColor, propSetName );
        }
    }
}
