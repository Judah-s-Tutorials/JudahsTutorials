package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private static final Color      defFGColor      = nextColor();
    private static final Color      extFGColor      = nextColor();
    private static final float      defWidth        = 1000;
    private static final float      extWidth        = 1.5f * defWidth;
    
    private static final float      defLength   = 20;
    private static final float      extLength   = 2 * defLength;
    private static final float      defStroke   = 5;
    private static final float      extStroke   = 2 * defStroke;
    private static final float      defSpacing  = 5;
    private static final float      extSpacing  = 2 * defSpacing;
    
    private static final int    defAxesColor        = 0xFF0000;
    private static final int    extAxesColor        = defAxesColor + 10;
    private static final int    defGridLinesColor   = 0x00FF00;
    private static final int    extGridLinesColor   = 
        defGridLinesColor + 10;
    private static final int    defTicMajorColor    = 0x0000FF;
    private static final int    extTicMajorColor    = 
        defTicMajorColor + 10;
    private static final int    defTicMinorColor    = 0x0000FF;
    private static final int    extTicMinorColor    = 
        defTicMinorColor + 10;
    
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
        Color           defColor    = new Color( defAxesColor );
        Color           extColor    = new Color( extAxesColor );
        propSet.setColor( defColor );
        validateAxes();
        waitOp();
        
        propSet.setColor( extColor );
        propSet.setStroke( extStroke );
        validateAxes();
        waitOp();
    }

    @Test
    public void testGridLines()
    {
        LinePropertySet propSet     = 
            profile.getLinePropertySet( gridLinesSet );
        propSet.setDraw( true );
        Color           defColor    = new Color( defGridLinesColor );
        Color           extColor    = new Color( extGridLinesColor );
        propSet.setColor( defColor );
//        validateGridLines();
        validateVerticalLines( propSet );
        waitOp();
        
        propSet.setColor( extColor );
        propSet.setStroke( extStroke );
//        validateGridLines();
        validateVerticalLines( propSet );
        waitOp();
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
        
        System.out.println( "ax: " + yAxis );
        System.out.println( "ax: " + xAxis );

        assertTrue( yBounds.getHeight() >= height );
        assertEquals( yBounds.getWidth(), stroke );
        assertEquals( expColor, yColor );

        assertEquals( xBounds.getHeight(), stroke );
        assertTrue( xBounds.getWidth() >= width );
        assertEquals( expColor, xColor );
    }

    private void validateGridLines()
    {        
        LinePropertySet props       = 
            profile.getLinePropertySet( gridLinesSet );
        testGUI.repaint();

        int             width       = testGUI.getWidth();
        int             height      = testGUI.getHeight();
        float           expStroke   = props.getStroke();
        float           xCenter     = width / 2f;
        float           yCenter     = height / 2;
        float           yTestCo     = yCenter + expStroke / 2 + 1;
        int             expColor    = getRGB( props.getColor() );
        BufferedImage   image       = testGUI.getImage();
        Point2D         origin      = null;
        
        // On y-axis, above x-axis
        origin = new Point2D.Float( xCenter, yTestCo );
        LineSegment     seg1        = 
            LineSegment.getNextVerticalLine( origin, image, expColor );
        assertNotNull( seg1 );
        Rectangle2D     bounds1     = seg1.getBounds();
        
        LineSegment     seg2        = seg1.getNextVerticalLine( image );
        assertNotNull( seg2 );
        Rectangle2D     bounds2     = seg2.getBounds();
        System.out.println( "gl: " + seg1 );
        System.out.println( "gl: " + seg2 );
        
        double          expSpacing  = 
            profile.getGridUnit() / props.getSpacing();
        double          actSpacing  = bounds2.getX() - bounds1.getX();
        double          actStroke   = bounds1.getWidth();
        
        assertEquals( actStroke, bounds2.getWidth() );
        assertEquals( expStroke, actStroke );
        assertEquals( expSpacing, actSpacing );
    }

    private void validateVerticalLines( LinePropertySet props )
    {        
        props.setDraw( true );
        testGUI.repaint();
        
        float           expLength   = props.getLength();
        float           expStroke   = props.getStroke();
        int             expColor    = getRGB( props.getColor() );

        int             width       = testGUI.getWidth();
        int             height      = testGUI.getHeight();
        float           xCenter     = width / 2f;
        float           yCenter     = height / 2;
        float           yTest       = yCenter - expStroke / 2 - 1;
        BufferedImage   image       = testGUI.getImage();
        Point2D         origin      = null;
        
        // On y-axis, above x-axis
        origin = new Point2D.Float( xCenter, yTest );
        LineSegment     seg1        = 
            LineSegment.getNextVerticalLine( origin, image, expColor );
        assertNotNull( seg1 );
        Rectangle2D     bounds1     = seg1.getBounds();
        
        LineSegment     seg2        = seg1.getNextVerticalLine( image );
        assertNotNull( seg2 );
        Rectangle2D     bounds2     = seg2.getBounds();
        System.out.println( "gl: " + seg1 );
        System.out.println( "gl: " + seg2 );
        
        double          expSpacing  = 
            profile.getGridUnit() / props.getSpacing();
        double          actSpacing  = bounds2.getX() - bounds1.getX();
        double          actLength   = bounds1.getHeight();
        double          actStroke   = bounds1.getWidth();
        
        if ( expLength > 0 )
            assertEquals( expLength, actLength );
        else
            assertTrue( actLength >= height );        
        assertEquals( actLength, bounds2.getHeight() );
        assertEquals( actStroke, bounds2.getWidth() );
        assertEquals( expStroke, actStroke );
        assertEquals( expSpacing, actSpacing );
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
        baseGraph.setFGColor( defFGColor );
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
        baseSet.setDraw( false );
    }
    
    private static Color nextColor()
    {
        int     iGray       = nextGray << 16 | nextGray << 8 | nextGray;
        Color   gray        = new Color( iGray );
        nextGray += 5;
        return gray;
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
}
