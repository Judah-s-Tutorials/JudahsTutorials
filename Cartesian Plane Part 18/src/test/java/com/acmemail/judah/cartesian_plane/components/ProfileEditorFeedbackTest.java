package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
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

/**
 * @author Jack Straub
 */
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
    
    /** Default grid unit. */
    private static final float      defGridUnit = 100;
    /** Alternate grid unit. */
    private static final float      altGridUnit = 1.5f * defGridUnit;
    /** Default grid color. */
    private static final Color      defBGColor  = new Color( 0xEFEFEF );
    /** Alternate grid color. */
    private static final Color      altBGColor  = Color.WHITE;
    
    /** Default font name. */
    private static final String     defFontName     = Font.DIALOG;
    /** Alternate font name. */
    private static final String     altFontName     = Font.MONOSPACED;
    /** Default font size. */
    private static final float      defFontSize     = 15;
    /** 
     * Alternate font size. The alternate value 
     * must be greater than the default value.
     */
    private static final float      altFontSize     = 1.5f * defFontSize;
    /** Default text color. */
    private static final Color      defFGColor      = Color.BLACK;;
    /** Alternate text color. */
    private static final Color      altFGColor      = Color.DARK_GRAY;
    /** Default font isBold. */
    private static final boolean    defFontBold     = false;
    // The bold properly is handled explicitly in the bold test,
    // hence no extFontBold variable.
    /** Default font isItalic. */
    private static final boolean    defFontItalic   = false;
    // The italic properly is handled explicitly in the italic test,
    // hence no extFontItalic variable.
    /** Default grid width. */
    private static final float      defWidth        = 750;
    // there is no test based on changing size, hence no extWidth variable
    
    /** Default line length. */
    private static final float      defLength       = 20;
    /** Alternate line length. */
    private static final float      altLength       = 2 * defLength;
    /** Default line weight. */
    private static final float      defStroke       = 2;
    /** Alternate line weight. */
    private static final float      altStroke       = 2 * defStroke;
    /** Default line spacing. */
    private static final float      defSpacing      = 5;
    /** Alternate line spacing. */
    private static final float      altSpacing      = 2 * defSpacing;
    /** Default line color. */
    private static final Color      defLineColor    = Color.RED;
    /** Alternate line color. */
    private static final Color      altLineColor    = Color.BLUE;
    
    /** 
     * This profile contains property values as registered in the
     * Property manager before any testing commences. Its purpose
     * is to restore the PropertyManager to its original state
     * at the end of the test; this is important for tests that
     * run as part of test suites. It must be instantiated before
     * {@link #baseProfile} is initialized.
     * 
     * @see #afterAll()
     */
    private static final Profile    pmgrProfile     = new Profile();
    /**
     * Represents the properties of a test Profile's 
     * predetermined values (see {@link #initBaseProfile()}.
     * Used as needed to populate the PropertyManager
     * with predetermined test values
     * (see for example, {@link #beforeEach()}.
     * Never modified after initialization.
     */
    private static final Profile    baseProfile     = new Profile();
    /** 
     * Profile used to initialize the test GUI/ProfileEditor.
     * After initialization the reference must not be changed,
     * but the contents of the object may be changed as needed.
     * The contents are restored to their original values before
     * each test (see {@link #beforeEach()}).
     * 
     * @see #pmgrProfile
     */
    private static final Profile    profile = new Profile();
    /** 
     * The object that displays and manager the ProfileEditor.
     * Guarantees that all interaction with the ProfileEditor
     * components is conducted via the EDT.
     */
    private static ProfileEditorFeedbackTestGUI  testGUI;
    
    /**
     * Initialize profiles and test GUI.
     */
    @BeforeAll
    public static void beforeAll()
    {
        initBaseProfile();
        testGUI = ProfileEditorFeedbackTestGUI.getTestGUI( profile );
    }
    
    /**
     * Resets the PropertyManager to its original state
     * and disposes all GUI windows. This is important
     * when tests are run as part of suites.
     */
    @AfterAll
    public static void afterAll()
    {
        // For the sake of tests run in suites, make sure
        // the original profile is restored at the end of the test.
        // Also make sure all GUI windows are disposed.
        pmgrProfile.apply();
        ComponentFinder.disposeAll();
    }
    
    /**
     * Return working profile and test GUI
     * to their default states.
     */
    @BeforeEach
    public void beforeEach()
    {
        baseProfile.apply();
        profile.reset();
    }

    /**
     * Verify that axes are drawn correctly.
     * Verify that axes are drawn correctly 
     * in their default state,
     * then change to the alternate color
     * and alternate stroke
     * and verify that they are correctly drawn
     * in their new state.
     */
    @Test
    public void testAxes()
    {
        LinePropertySet propSet     = 
            profile.getLinePropertySet( axesSet );
        validateAxes();
        
        propSet.setColor( altLineColor );
        propSet.setStroke( altStroke );
        validateAxes();
    }

    /**
     * Verify that non-axial lines are drawn correctly.
     * For each of the 
     * grid line, tic major, and tic minor property sets,
     * verify that lines are drawn correctly 
     * in their default state,
     * then change to the alternate color,
     * alternate stroke, and alternate length
     * and verify that they are correctly drawn
     * in their new state.
     *
     * @param propSetName   name of LinePropertySet to verify
     */
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
        
        profile.setGridUnit( altGridUnit );
        propSet.setColor( altLineColor );
        propSet.setStroke( altStroke );
        propSet.setSpacing( altSpacing );
        if ( propSet.hasLength() )
            propSet.setLength( altLength );
        LineEvaluator  evalB   = new LineEvaluator( propSetName );
        evalB.validateVertical();
        evalB.validateHorizontal();
    }
    
    /**
     * Verify that the grid color is drawn correctly.
     * Verify that the grid is painted
     * in its default state,
     * then switch to its alternate color
     * and verify that it is correctly drawn
     * in its new state.
     */
    @Test
    public void testGridColor()
    {
        GraphPropertySet    props   = profile.getMainWindow();
        BufferedImage       image   = testGUI.getImage();
        int                 xco     = image.getWidth() / 4;
        int                 yco     = image.getHeight() / 4;
        int                 actRGB  = image.getRGB( xco, yco ) & 0xFFFFFF;
        int                 expRGB  = getRGB( props.getBGColor() );
        assertEquals( actRGB, expRGB );
        
        expRGB = getRGB( altBGColor );
        props.setBGColor( altBGColor );
        image= testGUI.getImage();
        actRGB = image.getRGB( xco, yco ) & 0xFFFFFF;
        assertEquals( expRGB, actRGB );
    }
    
    /**
     * Verify that labels are drawn correctly
     * after the font size changes.
     * The new font size is assumed to be 
     * greater than the default size.
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
        assertTrue( defFontSize < altFontSize );
        
        GraphPropertySet    graph       = profile.getMainWindow();
        LinePropertySet     majorTic    = 
            profile.getLinePropertySet( ticMajorSet );
        majorTic.setSpacing( 1 );
        majorTic.setDraw( true );
        graph.setFontDraw( true );

        // Draw the text at the default font size and get the
        // actual bounds of the text. Verify that it matches the
        // expected bounds.
        BufferedImage   image       = testGUI.getImage();
        ImageRect       imgAAct     = getActTextRect();
        Rectangle2D     rectAExp    = getExpTextRect( image );
        assertTrue( imgAAct.withinBounds( rectAExp ) );
        
        // Draw the text at the alternate font size and get the
        // actual bounds of the text. Verify that it matches the
        // expected bounds.
        graph.setFontSize( altFontSize );
        image = testGUI.getImage();
        ImageRect   rectBAct    = getActTextRect();
        Rectangle2D rectBExp    = getExpTextRect( image );
        assertTrue( rectBAct.withinBounds( rectBExp ) );
        
        // Verify that the bounds of the text in the smaller font
        // is less than the bounds of the text in the larger font.
        assertTrue( imgAAct.getWidth() < rectBAct.getWidth() );
        assertTrue( imgAAct.getHeight() < rectBAct.getHeight() );
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
        LinePropertySet     majorTic    = 
            profile.getLinePropertySet( ticMajorSet );
        majorTic.setSpacing( 1 );
        majorTic.setDraw( true );

        graph.setBold( false );
        graph.setFontDraw( true );
        BufferedImage   image       = testGUI.getImage();
        ImageRect       imageAAct   = getActTextRect();
        Rectangle2D     rectAExp    = getExpTextRect( image );
        assertTrue( imageAAct.withinBounds( rectAExp ) );
        double      countA      = imageAAct.count( rgb );
        
        graph.setBold( true );
        ImageRect   rectB       = getActTextRect();
        image = testGUI.getImage();
        ImageRect   imageBAct   = getActTextRect();
        Rectangle2D rectBExp    = getExpTextRect( image );
        assertTrue( imageBAct.withinBounds( rectBExp ) );
        double      countB      = rectB.count( rgb );        

        // Verify that the plain text bounding box contains fewer
        // pixels of the text color than the bold text bounding box.
        assertTrue( countA < countB );
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
        LinePropertySet     majorTic    = 
            profile.getLinePropertySet( ticMajorSet );
        majorTic.setSpacing( 1 );
        majorTic.setDraw( true );
        graph.setFontDraw( true );

        graph.setItalic( false );
        BufferedImage   image   = testGUI.getImage();

        ImageRect           rectAAct    = getActTextRect();
        Rectangle2D         rectAExp    = getExpTextRect( image );
        assertTrue( rectAAct.withinBounds( rectAExp ) );
        
        graph.setItalic( true );
        image   = testGUI.getImage();
        ImageRect           rectBAct   = getActTextRect();
        Rectangle2D         rectBExp   = getExpTextRect( image );
        assertTrue( rectBAct.withinBounds( rectBExp ) );

        assertNotEquals( rectAAct, rectBAct );
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
        LinePropertySet     majorTic    = 
            profile.getLinePropertySet( ticMajorSet );
        majorTic.setSpacing( 1 );
        majorTic.setDraw( true );
        graph.setFontDraw( true );

        BufferedImage       image       = testGUI.getImage();
        ImageRect           rectAAct    = getActTextRect();
        Rectangle2D         rectAExp    = getExpTextRect( image );
        assertTrue( rectAAct.withinBounds( rectAExp ) );
        
        graph.setFontName( altFontName );
        image   = testGUI.getImage();
        ImageRect           rectBAct    = getActTextRect();
        Rectangle2D         rectBExp    = getExpTextRect( image );
        assertTrue( rectBAct.withinBounds( rectBExp ) );

        assertNotEquals( rectAAct, rectBAct );
    }
    
    /**
     * Verify that label text is redisplayed correctly  
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
        LinePropertySet     majorTic    = 
            profile.getLinePropertySet( ticMajorSet );
        majorTic.setSpacing( 1 );
        majorTic.setDraw( true );
        graph.setFontDraw( true );

        // If the following operation succeeds it means that labels
        // are displayed in the default color.
        ImageRect   rectA   = getActTextRect();
        assertNotNull( rectA );

        graph.setFGColor( altFGColor );
        // If the following operation succeeds it means that labels
        // are displayed in the new color.
        ImageRect   rectB   = getActTextRect();
        assertNotNull( rectB );
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
        
        profile.setGridUnit( altGridUnit );
        LineEvaluator   lineEvalB   = new LineEvaluator( propSet );
        lineEvalB.validateVertical();
    }
    
    @SuppressWarnings("unused")
    private static void waitOp()
    {
        JOptionPane.showMessageDialog( null, "Waiting..." );
    }

    /**
     * Set the default properties
     * for all property sets
     * encapsulated in the base profile
     * ({@link #baseProfile}.
     * <p>
     * See also {@link #initBaseGraphProperties()},
     * {@link #initBaseLineProperties(String)}.
     */
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
    
    /**
     * Set the main window properties
     * in the base profile
     * to their default values.
     */
    private static void initBaseGraphProperties()
    {
        GraphPropertySet    baseGraph   = baseProfile.getMainWindow();
        baseGraph.setBGColor( defBGColor );
        baseGraph.setBold( defFontBold );
        baseGraph.setFGColor( defFGColor );
        baseGraph.setFontName( defFontName );
        baseGraph.setFontSize( defFontSize );
        baseGraph.setItalic( defFontItalic );
        baseGraph.setWidth( defWidth );
        baseGraph.setFontDraw( false );
    }
    
    /**
     * For a given LinePropertySet
     * initialize all properties to the default
     * except for the draw property
     * which is explicitly set to false.
     * Since the draw property 
     * does not affect the axes,
     * repainting the feedback window
     * after performing this initialization
     * will result in a window
     * that displays only axes.
     * 
     * @param propSet   the given LinePropertySet
     */
    private static void initBaseLineProperties( String propSet )
    {
        LinePropertySet baseSet = 
            baseProfile.getLinePropertySet( propSet );
        baseSet.setLength( defLength );
        baseSet.setSpacing( defSpacing );
        baseSet.setStroke( defStroke );
        baseSet.setColor( defLineColor );
        baseSet.setDraw( false );
    }
    
    /**
     * Return the color of the given image
     * at the given point.
     * 
     * @param point the given point
     * @param image the given image
     * 
     * @return  the color of the given image at the given point
     */
    private static int getRGB( Point2D point, BufferedImage image )
    {
        int     xco     = (int)(point.getX());
        int     yco     = (int)(point.getY());
        int     rgb     = image.getRGB( xco, yco ) & 0xffffff;
        return rgb;
    }
    
    /**
     * Get the integer value of a given color
     * with the alpha bits stripped.
     * 
     * @param color the given color
     * 
     * @return  
     *      the integer value of a given color 
     *      with the alpha bits stripped
     */
    private static int getRGB( Color color )
    {
        int     iColor  = color.getRGB() & 0xFFFFFF;
        return iColor;
    }
    
    /**
     * Given the currently configured test data,
     * verify that the x- and y-axes
     * are correctly drawn.
     */
    private void validateAxes()
    {
        LinePropertySet props       = 
            profile.getLinePropertySet( axesSet );
        int             width       = testGUI.getWidth();
        int             height      = testGUI.getHeight();
        float           xAxisYco    = height / 2f;
        float           testYco     = height / 4f;
        float           yAxisXco    = width / 2f;
        float           testXco     = width / 4f;
        float           expStroke   = props.getStroke();
        int             expColor    = getRGB( props.getColor() );
        BufferedImage   image       = testGUI.getImage();
        
        // On y-axis, above x-axis
        Point2D     origin      = new Point2D.Float( yAxisXco, testYco );
        LineSegment yAxis       = LineSegment.of( origin, image );
        Rectangle2D yBounds     = yAxis.getBounds();
        int         yColor      = getRGB( origin, image );

        assertTrue( yBounds.getHeight() >= height );
        assertEquals( expStroke, yBounds.getWidth() );
        assertEquals( expColor, yColor );
        
        // On x-axis, left of y-axis
        origin = new Point2D.Float( testXco, xAxisYco );
        LineSegment xAxis   = LineSegment.of( origin, image );
        Rectangle2D xBounds = xAxis.getBounds();
        int         xColor  = getRGB( origin, image );

        assertEquals( expStroke, xBounds.getHeight() );
        assertTrue( xBounds.getWidth() >= width );
        assertEquals( expColor, xColor );
    }
    
    /**
     * Draw the labels on the feedback panel under test
     * and return the expected bounds
     * of the first label on the x-axis
     * to the right of the y-axis.
     * Before drawing the text
     * the current test data is modified to:
     * <ol>
     * <li>Set the major tic spacing to 1</li>
     * </ol>
     * 
     * @return the calculated rectangle
     * 
     * @see #getExpTextRect()
     */
    private ImageRect getActTextRect()
    {
        LinePropertySet     axes        = 
            profile.getLinePropertySet( axesSet );
        LinePropertySet     ticMajor    = 
            profile.getLinePropertySet( ticMajorSet );
        GraphPropertySet    graph       = profile.getMainWindow();
        Color               textColor   = graph.getFGColor();
        ticMajor.setSpacing( 1 );
        BufferedImage       image       = testGUI.getImage();
        
        // Sanity check. The text color should be different from the
        // axis color and the major tic color in order to eliminate
        // possible confusion.
        assertNotEquals( textColor, axes.getColor() );
        assertNotEquals( textColor, ticMajor.getColor() );
        
        // To locate the center/top of text, start with the x-coordinate
        // of the first major tic, and the y-coordinate of the x-axis.
        double      yAxisXco    = image.getWidth() / 2;
        double      xAxisYco    = image.getHeight() / 2;
        double      gridUnit    = profile.getGridUnit();
        double      halfUnit    = gridUnit / 2;
        
        // Assumption: The target label will not overlap another label
        // or the x-axis. Construct a rectangle one-half unit to either
        // side of the source tic mark and from the x-axis to one-half
        // unit below the x-axis.
        double      topY        = xAxisYco;
        double      leftX       = yAxisXco + halfUnit;
        double      width       = gridUnit;
        double      height      = halfUnit;
        
        int         color       = getRGB( textColor );
        Rectangle2D rect        = 
            new Rectangle2D.Double( leftX, topY, width, height );
        LineSegment lineSeg     = LineSegment.ofRect( rect, image, color );
        Rectangle2D resultRect  = lineSeg.getBounds();
        assertNotNull( resultRect );
        ImageRect   result      = new ImageRect( image, resultRect );
        
        return result;
    }
    
    /**
     * Draw the labels on the feedback panel under test
     * and return the expected bounds
     * of the first label on the x-axis
     * to the right of the y-axis.
     * 
     * @return  
     *      a rectangle describing the bounds of the string "1.0"
     *      as drawn with the current test data settings
     *  
     * @see #getActTextRect()
     */
    private Rectangle2D getExpTextRect( BufferedImage image )
    {
        GraphPropertySet    window  = profile.getMainWindow();
        String              name    = window.getFontName();
        int                 size    = (int)window.getFontSize();
        int                 style   = window.isBold() ? Font.BOLD : 0;
        style |= window.isItalic() ? Font.ITALIC : 0;
        Font                font    = new Font( name, style, size );
        Graphics2D          gtx     = image.createGraphics();
        gtx.setFont( font );
        FontRenderContext   frc     = gtx.getFontRenderContext();
        TextLayout          layout  = new TextLayout( "1.00", font, frc );
        Rectangle2D         rect    = layout.getBounds();
        
        gtx.dispose();
        return rect;
    }
    
    /**
     * An object of this class
     * encapsulates a rectangular region
     * from the interior of an image.
     * @author Jack Straub
     */
    private static class ImageRect
    {
        /** 
         * Data copied from the rectangular region
         * with an image.
         */
        private final int[][]       data;
        /** The bounds of the encapsulated region. */
        private final Rectangle2D   bounds;
        
        /**
         * Encapsulate the given rectangular area
         * from the interior of the given image.
         * 
         * @param image the given image
         * @param rect  the given rectangular area
         */
        public ImageRect( BufferedImage image, Rectangle2D rect )
        {
            int width       = (int)rect.getWidth();
            int height      = (int)rect.getHeight();
            int firstRow    = (int)rect.getY();
            int firstCol    = (int)rect.getX();
            this.bounds     = rect;
            data = new int[height][width];
            
            int row         = firstRow;
            for ( int yco = 0 ; yco < height ; ++yco, row++ )
            {
                int col = firstCol;
                for ( int xco = 0 ; xco < width ; ++xco, ++col )
                    data[yco][xco] = image.getRGB( col, row ) & 0xFFFFFF;
            }
        }
        
        /**
         * Returns a count of the pixels in the encapsulated data
         * that match a given color.
         * 
         * @param rgb   the given color
         * 
         * @return  
         *      count of the pixels in the encapsulated data
         *      that match a given color
         */
        public int count( int rgb )
        {
            long    count   = stream()
                .filter( i -> i == rgb )
                .count();
            return (int)count;
        }
        
        /**
         * Returns the width of the encapsulated
         * rectangular area.
         * 
         * @return  the width of the encapsulated rectangular area
         */
        public int getWidth()
        {
            int width   = (int)bounds.getWidth();
            return width;
        }
        
        /**
         * Returns the height of the encapsulated
         * rectangular area.
         * 
         * @return  the height of the encapsulated rectangular area
         */
        public int getHeight()
        {
            int height  = (int)bounds.getHeight();
            return height;
        }
        
        /**
         * Returns a stream of the encapsulated data.
         * 
         * @return  a stream of the encapsulated data
         */
        public IntStream stream()
        {
            IntStream   stream  =
                Arrays.stream( data )
                    .flatMapToInt( r -> Arrays.stream( r ) );
            return stream;
        }
        
        public boolean withinBounds( Rectangle2D rect )
        {
            int thisWidth   = getWidth();
            int thatWidth   = (int)rect.getWidth();
            int diffWidth   = Math.abs( thisWidth - thatWidth );
            int thisHeight  = getHeight();
            int thatHeight  = (int)rect.getHeight();
            int diffHeight  = Math.abs( thisHeight - thatHeight );
            boolean result  = diffWidth <= 2 && diffHeight <= 2;
            return result;
        }
        
        /**
         * Returns the hashcode of this object.
         */
        @Override
        public int hashCode()
        {
            int hash    = Arrays.deepHashCode( data );
            return hash;
        }
        
        /**
         * Compares this ImageRect to another object
         * and returns true if they are equal.
         * This object is equal to the other object
         * if the object is a non-null ImageRect
         * with a data array equal to
         * this object's data array.
         */
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
     * One vertical and one horizontal line
     * are selected for validation.
     * The spacing between the lines,
     * and their length, stroke, and color
     * are verified.
     * 
     * @author Jack Straub
     * 
     * @see #validateHorizontal()
     * @see #validateVertical()
     */
    private static class LineEvaluator
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
        
        /**
         * Verify that,
         * for the currently configured data,
         * the first vertical line
         * to the right of the y-axis exists, 
         * and is drawn with the expected properties.
         */
        public void validateVertical()
        {
            float   oXco            = yAxisXco + expSpacing;
            float   oYco            = xAxisYco + 1;
            Point2D     origin      = new Point2D.Double( oXco, oYco );
            LineSegment seg         = LineSegment.of( origin, image );
            Rectangle2D bounds      = seg.getBounds();
            double      actLength   = bounds.getHeight();
            double      actStroke   = bounds.getWidth();
            int         actColor    = seg.getColor();
            
            if ( expLength > 0 )
                assertEquals( expLength, actLength, propSetName );
            else
                assertTrue( actLength >= height, propSetName );
            assertEquals( expStroke, actStroke, propSetName );
            assertEquals( expColor, actColor, propSetName );
        }
        
        /**
         * Verify that,
         * for the currently configured data,
         * the first horizontal line
         * below the x-axis exists, 
         * and is drawn with the expected properties.
         */
        public void validateHorizontal()
        {
            float   oYco            = xAxisYco + expSpacing;
            float   oXco            = expLength > 0 ?
                yAxisXco + expLength / 4 : expSpacing / 2;
            Point2D     origin      = new Point2D.Double( oXco, oYco );
            LineSegment seg         = LineSegment.of( origin, image );
            Rectangle2D bounds      = seg.getBounds();
            
            double      actLength   = bounds.getWidth();
            double      actStroke   = bounds.getHeight();
            int         actColor    = seg.getColor();

            assertEquals( actStroke, bounds.getHeight() );
            assertEquals( actLength, bounds.getWidth() );
            assertEquals( actColor, seg.getColor() );
            
            if ( expLength > 0 )
                assertEquals( expLength, actLength, propSetName );
            else
                assertTrue( actLength >= width );
            assertEquals( expStroke, actStroke, propSetName );
            assertEquals( expColor, actColor, propSetName );
        }
    }
}
