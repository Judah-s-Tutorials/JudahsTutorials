package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.sandbox.utils.LineSegment;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorFeedbackTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;

class ProfileEditorFeedbackTest
{
    private static int  nextGray    = 1;
    
    /** The simple name of the GraphPropertySetMW class. */
    private static final String graphSet        =
        GraphPropertySetMW.class.getSimpleName();
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
    private static final float      defGridUnit = 100;
    private static final float      extGridUnit = 2 * defGridUnit;
    private static final Color      defBGColor  = new Color( 0xEFEFEF );
    private static final Color      extBGColor  = Color.WHITE;
    
    private static final String     defFontName     = Font.DIALOG;
    private static final String     extFontName     = Font.MONOSPACED;
    private static final float      defFontSize     = 10;
    private static final float      extFontSize     = 2 * defFontSize;
    private static final boolean    defFontBold     = false;
    private static final boolean    extFontBold     = !defFontBold;
    private static final boolean    defFontItalic   = false;
    private static final boolean    extFontItalic   = !defFontItalic;
    private static final Color      defFontColor    = Color.BLUE;
    private static final Color      extFontColor    = Color.GREEN;
    private static final float      defWidth        = 1000;
    private static final float      extWidth        = 1.5f * defWidth;
    
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
     * Contains property values guaranteed to be different from those
     * stored in the BaseProfile. Never modified after initialization.
     */
    private static Profile          distinctProfile = 
        ProfileUtils.getDistinctProfile( baseProfile );
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
    
    @BeforeEach
    public void beforeEAch() throws Exception
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
        int                 yco     = image.getWidth() / 4;
        int                 actRGB  = image.getRGB( xco, yco ) & 0xFFFFFF;
        int                 expRGB  = getRGB( props.getBGColor() );
//        waitOp();
        assertEquals( actRGB, expRGB );
        
        expRGB = getRGB( extBGColor );
        props.setBGColor( extBGColor );
        testGUI.repaint();
        image= testGUI.getImage();
        actRGB = image.getRGB( xco, yco ) & 0xFFFFFF;
//        waitOp();
        assertEquals( actRGB, expRGB );
    }
    
    @Test
    public void testFontSize()
    {
        LinePropertySet     ticMajor    = 
            profile.getLinePropertySet( ticMajorSet );
        GraphPropertySet    graph       = profile.getMainWindow();
        ticMajor.setDraw( true );
        ticMajor.setSpacing( 1 );
        graph.setFontDraw( true );
        
        int                 heightA     = getTextHeight();
        System.out.println( "text height: " +  heightA );
        
        graph.setFGColor( extFontColor );
        int                 heightB     = getTextHeight();
        System.out.println( "text height: " +  heightB );
        assertEquals( heightA, heightB );
        
        graph.setFontSize( extFontSize );
        System.out.println( "text height: " + getTextHeight() );
        int                 heightC     = getTextHeight();
        System.out.println( "text height: " +  heightB );
        assertTrue( heightA < heightC );
    }
    
    private int getTextHeight()
    {
        GraphPropertySet    graph       = profile.getMainWindow();
        LinePropertySet     ticMajor    = 
            profile.getLinePropertySet( ticMajorSet );
        testGUI.repaint();
        BufferedImage       image       = testGUI.getImage();
        waitOp();
        
        int         width   = image.getWidth();
        int         height  = image.getHeight();
        double      spacing = profile.getGridUnit() / ticMajor.getSpacing();
        double      xco     = width / 2 + spacing;
        double      yco     = height / 2 + ticMajor.getLength() / 2 + 1;
        Point       origin  = new Point( (int)xco, (int)yco );
        int         rgb     = getRGB( graph.getFGColor() );
        
        TextRect    rect        = 
            new TextRect( origin, rgb, image );
        int         textHeight  = rect.bottom - rect.top;
        return textHeight;
    }
    
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
    
    private class TextRect
    {
        private static final int    range   = 20;
        private final int           oXco;
        private final int           oYco;
        private final int           rgb;
        private final BufferedImage image;
        private final int           width;
        private final int           height;
        
        private int top     = Integer.MAX_VALUE;
        private int bottom  = -1;
        private int left    = -1;
        private int right   = Integer.MAX_VALUE;
        public TextRect( Point origin, int color, BufferedImage image )
        {
            oXco = origin.x;
            oYco = origin.y;
            rgb = color;
            this.image = image;
            width = image.getWidth();
            height = image.getHeight();
            getTop();
        }
        
        private void getTop()
        {
            int first       = Integer.MAX_VALUE;
            int topLimit    = oYco;
            int bottomLimit = oYco + range;
            int leftLimit   = oXco - range;
            int rightLimit  = oXco + range;
            
            for ( int xco = leftLimit ; xco < rightLimit ; ++ xco )
            {
                int emptyCount  = 0;
                for ( int yco = oYco ; oYco < height ; ++yco )
                {
                    int nextColor   = image.getRGB( xco, yco ) & 0xffffff;
                    if ( nextColor == rgb )
                    {
                        if ( yco < top )
                            top = yco;
                        if ( yco > bottom )
                            bottom = yco;
                    }
                    else
                    {
                        if ( ++emptyCount > 10 )
                            break;
                    }
                }
            }
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
            
//            System.out.println( propSetName + "V: " + seg1 );
//            System.out.println( propSetName + "V: " + seg2 );
//            waitOp();

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
            
//            System.out.println( propSetName + "H: " + seg1 );
//            System.out.println( propSetName + "H: " + seg2 );
//            waitOp();

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
