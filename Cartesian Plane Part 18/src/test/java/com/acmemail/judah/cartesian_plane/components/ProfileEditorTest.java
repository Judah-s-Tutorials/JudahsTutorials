package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ProfileEditorTest
{
    private static final String axesSetName         =
        LinePropertySetAxes.class.getSimpleName();
    private static final String gridLinesSetName    =
        LinePropertySetGridLines.class.getSimpleName();
    private static final String ticMajorSetName     =
        LinePropertySetTicMajor.class.getSimpleName();
    private static final String ticMinorSetName     =
        LinePropertySetTicMinor.class.getSimpleName();
    private static final String[]   allSetNames     =
    { axesSetName, gridLinesSetName, ticMajorSetName, ticMinorSetName };

    private static Profile          baseProfile     = new Profile();
    private static Profile          distinctProfile = 
        ProfileUtils.getDistinctProfile( baseProfile );
    private static Profile              profile = new Profile();
    private static ProfileEditorTestGUI testGUI;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        testGUI = ProfileEditorTestGUI.getTestGUI( profile );
    }

    @BeforeEach
    public void beforeEach() throws Exception
    {
        baseProfile.apply();
        profile = new Profile();
        testGUI.reset();
        assertEquals( baseProfile, profile );
        validateCurrState( baseProfile );
    }
    
    /**
     * Verify that all ProfileEditor components
     * are correctly initialized
     * immediately after instantiation.
     */
    @Test
    public void testProfileEditor()
    {
        validateCurrState( baseProfile );
    }

    @Test
    public void testGetFeedBack()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testApply()
    {
        // Sanity check: verify that the test data is in
        // the expected state.
        validateCurrState( baseProfile );
        
        applyDistinctGraphProperties();
        Stream.of( allSetNames )
            .forEach( this::applyDistinctLineProperties );
        // Verify all components have been changed to distinct values
        validateCurrState( distinctProfile );
        
        // Apply the modified values and verify that the
        // PropertyManager has been updated.
        testGUI.apply();
        Profile profile = new Profile();
        validateCurrState( profile );
    }

    @Test
    public void testReset()
    {
        validateCurrState( baseProfile );
        applyDistinctProperties();
        validateCurrState( distinctProfile );
        testGUI.reset();
        validateCurrState( baseProfile );
    }

    private void waitOp()
    {
        JOptionPane.showMessageDialog( null, "Waiting" );
    }

    /**
     * Set the values of those components of the ProfileEditor
     * that are responsible for editing a profile's properties
     * to unique values.
     */
    private void applyDistinctProperties()
    {
        applyDistinctGraphProperties();
        Stream.of( allSetNames )
            .forEach( this::applyDistinctLineProperties );
    }
    
    /**
     * Set the values of those components of the ProfileEditor
     * that are responsible for editing a profile's GraphPropertySet
     * to unique values.
     */
    private void applyDistinctGraphProperties()
    {
        GraphPropertySet    graphSet    = distinctProfile.getMainWindow();
        int                 iFGColor    = getRGB( graphSet.getFGColor() );
        int                 iBGColor    = getRGB( graphSet.getBGColor() );
        
        testGUI.setFontDraw( graphSet.isFontDraw() );
        testGUI.setBGColor( iBGColor );
        testGUI.setName( distinctProfile.getName() );
        testGUI.setGridUnit( distinctProfile.getGridUnit() );
        testGUI.setGridWidth( graphSet.getWidth() );
        
        Thread  thread  = testGUI.editFont();
        testGUI.setFGColor( iFGColor );
        testGUI.setFontBold( graphSet.isBold() );
        testGUI.setFontItalic( graphSet.isItalic() );
        testGUI.setFontName( graphSet.getFontName() );
        testGUI.setFontSize( graphSet.getFontSize() );
        testGUI.selectFDOK();
        Utils.join( thread );
    }
    
    /**
     * Set the values of those components of the ProfileEditor
     * that are responsible for editing a given LinePropertySet
     * to unique values.
     * 
     * @param propSet   the given LinePropertySet
     */
    private void applyDistinctLineProperties( String propSet )
    {
        LinePropertySet lineSet = 
            distinctProfile.getLinePropertySet( propSet );
        if ( lineSet.hasColor() )
        {
            Color   color   = lineSet.getColor();
            int     rgb     = getRGB( color );
            testGUI.setColor( propSet, rgb );
        }
        if ( lineSet.hasDraw() )
            testGUI.setDraw( propSet, lineSet.getDraw() );
        if ( lineSet.hasLength() )
            testGUI.setLength( propSet, lineSet.getLength() );
        if ( lineSet.hasSpacing() )
            testGUI.setSpacing( propSet, lineSet.getSpacing() );
        if ( lineSet.hasStroke() )
            testGUI.setStroke( propSet, lineSet.getStroke() );
    }

    /**
     * Verify that the values of all components
     * in the ProfileEditor
     * match the given profile.
     * 
     * @param currState the given profile
     */
    private void validateCurrState( Profile currState )
    {
        Profile profile = new Profile();
        
        collectGraphProperties( profile );
        assertEquals( currState.getMainWindow(), profile.getMainWindow() );
        Stream.of( allSetNames )
            .peek( s -> collectLineProperties( s, profile ) )
            .forEach( s -> {
                LinePropertySet currSet = 
                    currState.getLinePropertySet( s );
                LinePropertySet testSet = profile.getLinePropertySet( s );
                assertEquals( currSet, testSet );
            });
        assertEquals( currState, profile );
    }
    
    /**
     * Use the values of the components of the ProfileEditor.
     * to initialize the GraphPropertySetMW of a given profile.
     * 
     * @param profile   the given profile
     */
    private void collectGraphProperties( Profile profile )
    {
        // Make sure the font dialog is initialized
        Thread  thread  = testGUI.editFont();
        testGUI.selectFDCancel();
        Utils.join( thread );
        
        GraphPropertySet    graphSet    = profile.getMainWindow();
        Color               bgColor     =
            new Color( testGUI.getBGColor() );
        Color               fgColor     =
            new Color( testGUI.getFGColor() );
        
        profile.setName( testGUI.getName() );
        profile.setGridUnit( testGUI.getGridUnit() );
        graphSet.setWidth( testGUI.getGridWidth() );
        graphSet.setBGColor( bgColor );
        graphSet.setBold( testGUI.getFontBold() );
        graphSet.setFGColor( fgColor );
        graphSet.setFontDraw( testGUI.getFontDraw() );
        graphSet.setFontName( testGUI.getFontName() );
        graphSet.setFontSize( testGUI.getFontSize() );
        graphSet.setItalic( testGUI.getFontItalic() );
    }
    /**
     * Use the values of the components of the ProfileEditor.
     * to initialize the given LinePropertySet of a given profile.
     * 
     * @param profile   the given profile
     */
    private void collectLineProperties( String propSet, Profile profile )
    {
        LinePropertySet lineProperties  = 
            profile.getLinePropertySet( propSet );
        if ( lineProperties.hasColor() )
        {
            Color   color   = new Color( testGUI.getColor( propSet ) );
            lineProperties.setColor( color );
        }
        if ( lineProperties.hasDraw() )
            lineProperties.setDraw( testGUI.getDraw( propSet ) );
        if ( lineProperties.hasLength() )
            lineProperties.setLength( testGUI.getLength( propSet ) );
        if ( lineProperties.hasSpacing() )
            lineProperties.setSpacing( testGUI.getSpacing( propSet ) );
        if ( lineProperties.hasStroke() )
            lineProperties.setStroke( testGUI.getStroke( propSet ) );
    }
    
    /**
     * Gets the RGB value of a given color
     * with the alpha bits suppressed.
     * 
     * @param color the given color
     * 
     * @return  the RGB value of the given color
     */
    private int getRGB( Color color )
    {
        int rgb = color.getRGB() & 0xffffff;
        return rgb;
    }
}
