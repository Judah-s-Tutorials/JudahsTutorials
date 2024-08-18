package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
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
    private static final Color      defBGColor  = new Color( 0xEEEEEE );
    private static final Color      extBGColor  = Color.WHITE;
    
    private static final String     defFontName     = Font.DIALOG;
    private static final String     extFontName     = Font.MONOSPACED;
    private static final float      defFontSize     = 10;
    private static final float      extFontSize     = 2 * defFontSize;
    private static final boolean    defFontBold     = false;
    private static final boolean    extFontBold     = !defFontBold;
    private static final boolean    defFontItalic   = false;
    private static final boolean    extFontItalic   = !defFontItalic;
    private static final Color      defFGColor      = Color.BLACK;
    private static final Color      extFGColor      = Color.DARK_GRAY;
    private static final float      defWidth        = 1000;
    private static final float      extWidth        = 1.5f * defWidth;
    
    private static final float      defLength   = 20;
    private static final float      extLength   = 2 * defLength;
    private static final float      defStroke   = 5;
    private static final float      extStroke   = 2 * defStroke;
    private static final float      defSpacing  = 5;
    private static final float      extSpacing  = 2 * defSpacing;
    private static final Color      defColor    = Color.RED;
    private static final Color      extColor    = Color.BLUE;
    
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
    public void test()
    {
        validateAxes();
        waitOp();
    }
    
    private static void waitOp()
    {
        JOptionPane.showMessageDialog( null, "Waiting..." );
    }
    
    private void validateAxes()
    {
        int             width   = testGUI.getWidth();
        int             height  = testGUI.getHeight();
        float           yTestCo = height / 4f;
        float           yCenter = height / 2f;
        float           xTestCo = width / 4f;
        float           xCenter = width / 2f;
        Point2D         origin  = null;
        BufferedImage   image   = testGUI.getImage();
        LinePropertySet axesProps   = 
            profile.getLinePropertySet( axesSet );
        
        // On y-axis, above x-axis
        origin = new Point2D.Float( xCenter, yTestCo );
        LineSegment yAxis   = LineSegment.of( origin, image );
        
        // On x-axis, left of y-axis
        origin = new Point2D.Float( xTestCo, yCenter );
        LineSegment xAxis   = LineSegment.of( origin, image );
        
        System.out.println( yAxis );
        System.out.println( xAxis );
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
        baseSet.setColor( defColor );
        baseSet.setLength( defLength );
        baseSet.setSpacing( defSpacing );
        baseSet.setStroke( defStroke );
        baseSet.setDraw( false );
    }
}
