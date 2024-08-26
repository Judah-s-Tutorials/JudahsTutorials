package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorTestGUI_old;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

/**
 * This application should be run as a JUnit test.
 * It verifies that a ProfileEditorTestGUI_old object
 * has read and write access 
 * to all the components of a ProfileEditor
 * that are necessary to edit
 * the properties of a Profile.
 * 
 * @author Jack Straub
 */
class ProfileEditorTestGUIValidator
{
    /**
     * Base profile with property values initialized from the
     * PropertyManager. Never modified.
     */
    private static Profile              profile         = new Profile();
    /**
     * Profile initialized with property values guaranteed to be
     * different from the values of the base profile ({@link #profile}).
     */
    private static Profile              distinctProfile = 
        ProfileUtils.getDistinctProfile( profile );
    /** Test GUI to be validated. */
    private static ProfileEditorTestGUI_old testGUI         =     
        ProfileEditorTestGUI_old.getTestGUI( profile );

    /**
     * Verify that the ProfileEditorTestGUI_old has read/write access
     * to all those components of the ProfileEditor
     * that are necessary to edit the GraphPropertySetMW properties
     * of a Profile
     * The validation logic:
     * <ol>
     * <li>
     *      Obtains the value of a component
     *      (the component's "original" value)
     * </li>
     * <li>
     *      Sets the value of the component
     *      to a unique value
     *      (the component's "expected" value after modification)
     * </li>    
     * <li>
     *      <em>After modification of all components:</em>
     *      <ol>
     *      <li>
     *          Obtains the value of the component
     *          (the component's "actual" value after modification)
     *      </li>
     *      <li>
     *          Prints a summary of the values of each component,
     *          including the component's original, expected, and
     *          actual values.
     *      </li>
     *      <li>
     *          Compares each component's 
     *          expected and actual values.
     *      </li>
     *      </ol>
     * </li>
     * </ol>
     */
    @Test
    void testGraphicsProperties()
    {
        float   gridUnitOrig    = testGUI.getGridUnit();
        float   gridUnitExp     = distinctProfile.getGridUnit();
        float   gridUnitAct     = 0;
        
        GraphPropertySet    mwDistinct  = 
            distinctProfile.getMainWindow(); 

        String  gridColorOrig   = 
            toHexString( testGUI.getBGColor() );
        int     gridColorExp    = 
            mwDistinct.getBGColor().getRGB() &0xffffff;
        int     gridColorAct   = 0;
        String  gridSColorAct   = "";

        boolean fontDrawOrig   = testGUI.getFontDraw();
        boolean fontDrawExp    = mwDistinct.isFontDraw();
        boolean fontDrawAct    = false;

        String  fontColorOrig   = 
            toHexString( testGUI.getFGColor() );
        int     fontColorExp    = 
            mwDistinct.getFGColor().getRGB() & 0xffffff;
        int     fontColorAct    = 0;
        String  fontSColorAct   = "";

        String  fontNameOrig   = testGUI.getFontName();
        String  fontNameExp    = mwDistinct.getFontName();
        String  fontNameAct    = "";

        float   fontSizeOrig   = testGUI.getFontSize();
        float   fontSizeExp    = mwDistinct.getFontSize();
        float   fontSizeAct    = 0;

        boolean fontBoldOrig   = testGUI.getFontBold();
        boolean fontBoldExp    = mwDistinct.isBold();
        boolean fontBoldAct    = false;

        boolean fontItalicOrig = testGUI.getFontItalic();
        boolean fontItalicExp  = mwDistinct.isItalic();
        boolean fontItalicAct  = false;
        
        testGUI.setGridUnit( gridUnitExp );
        testGUI.setFontDraw(fontDrawExp );
        testGUI.setBGColor( gridColorExp );
        
        Thread  thread  = testGUI.editFont();
        testGUI.setFGColor( fontColorExp );
        testGUI.setFontName( fontNameExp );
        testGUI.setFontSize( fontSizeExp );
        testGUI.setFontBold( fontBoldExp );
        testGUI.setFontItalic( fontItalicExp );
// Uncomment the following line to pause the application
// after displaying and modifying the FontEditorDialog
//        JOptionPane.showMessageDialog( null, "Done" );
        testGUI.selectFDOK();
        Utils.join( thread );
        
        gridUnitAct = testGUI.getGridUnit();
        fontDrawAct = testGUI.getFontDraw();
        gridColorAct = testGUI.getBGColor();
        gridSColorAct = toHexString( gridColorAct );
        fontColorAct = testGUI.getFGColor();
        fontSColorAct = toHexString( fontColorAct );
        fontNameAct = testGUI.getFontName();
        fontSizeAct = testGUI.getFontSize();
        fontBoldAct = testGUI.getFontBold();
        fontItalicAct = testGUI.getFontItalic();
        
        printThreeValues( 
            "Grid unit", 
            gridUnitOrig, 
            gridUnitExp, 
            gridUnitAct
        );
        printThreeValues( 
            "Font draw", 
            fontDrawOrig, 
            fontDrawExp, 
            fontDrawAct
        );
        printThreeValues( 
            "Grid color", 
            gridColorOrig, 
            toHexString( gridColorExp ), 
            gridSColorAct
        );
        printThreeValues( 
            "Font color", 
            fontColorOrig, 
            fontSColorAct, 
            fontColorAct
        );
        printThreeValues( 
            "Font name", 
            fontNameOrig, 
            fontNameExp, 
            fontNameAct
        );
        printThreeValues( 
            "Font size", 
            fontSizeOrig, 
            fontSizeExp, 
            fontSizeAct
        );
        printThreeValues( 
            "Font bold", 
            fontBoldOrig, 
            fontBoldExp, 
            fontBoldAct
        );
        printThreeValues( 
            "Font italic", 
            fontItalicOrig, 
            fontItalicExp, 
            fontItalicAct
        );
        
// Uncomment the following line to pause the application with
// the ProfileEditor displayed after modifying all components
// associated with the GraphPropertySetMW properties.
//        JOptionPane.showMessageDialog( null, "Done" );
        assertEquals( gridUnitExp, gridUnitAct );
        assertEquals( fontDrawExp, fontDrawAct );
        assertEquals( gridColorExp, gridColorAct );
        assertEquals( fontColorExp, fontColorAct );
        assertEquals( fontNameExp, fontNameAct );
        assertEquals( fontSizeExp, fontSizeAct );
        assertEquals( fontBoldExp, fontBoldAct );
        assertEquals( fontItalicExp, fontItalicAct );
    }
    
    /**
     * For each LinePropertySet object contained in a Profile
     * verify that the ProfileEditorTestGUI_old has read/write access
     * to all those components of the ProfileEditor
     * that are necessary to edit the LinePropertySet.
     * The validation logic:
     * <ol>
     * <li>
     *      Obtains the value of a component
     *      (the component's "original" value)
     * </li>
     * <li>
     *      Sets the value of the component
     *      to a unique value
     *      (the component's "expected" value after modification)
     * </li>    
     * <li>
     *      <em>After modification of all components:</em>
     *      <ol>
     *      <li>
     *          Obtains the value of the component
     *          (the component's "actual" value after modification)
     *      </li>
     *      <li>
     *          Prints a summary of the values of each component,
     *          including the component's original, expected, and
     *          actual values.
     *      </li>
     *      <li>
     *          Compares each component's 
     *          expected and actual values.
     *      </li>
     *      </ol>
     * </li>
     * </ol>
     * 
     * @param setName   
     *      the simple name of the LinePropertySet subclass to verify
     */
    @ParameterizedTest
    @ValueSource( strings= 
        {
            "LinePropertySetAxes", 
            "LinePropertySetGridLines", 
            "LinePropertySetTicMajor",
            "LinePropertySetTicMinor"
        }
    )
    private void testLineProperties( String setName )
    {
        LinePropertySet distinct    = 
            distinctProfile.getLinePropertySet( setName );
        
        float   lengthOrig      = -1;
        float   lengthExp       = -1;
        float   lengthAct       = -1;
        
        float   strokeOrig      = -1;
        float   strokeExp       = -1;
        float   strokeAct       = -1;
        
        float   spacingOrig     = -1;
        float   spacingExp      = -1;
        float   spacingAct      = -1;
        
        int     colorOrig       = 0; 
        int     colorExp        = -1;
        int     colorAct        = 0;
        
        boolean drawOrig        = false;
        boolean drawExp         = false;
        boolean drawAct         = false;
        
        if ( distinct.hasLength() )
        {
            lengthOrig = testGUI.getLength( setName );
            lengthExp = distinct.getLength();
            testGUI.setLength( setName, lengthExp );
        }
        
        if ( distinct.hasSpacing() )
        {
            spacingOrig = testGUI.getSpacing( setName );
            spacingExp = distinct.getSpacing();
            testGUI.setSpacing( setName, spacingExp );
        }
        
        if ( distinct.hasStroke() )
        {
            strokeOrig = testGUI.getStroke( setName );
            strokeExp = distinct.getStroke();
            testGUI.setStroke( setName, strokeExp );
        }
        
        if ( distinct.hasDraw() )
        {
            drawOrig = testGUI.getDraw( setName );
            drawExp = distinct.getDraw();
            testGUI.setDraw( setName, drawExp );
        }
        
        if ( distinct.hasColor() )
        {
            colorOrig = testGUI.getColor( setName );
            colorExp = distinct.getColor().getRGB() & 0xffffff;
            testGUI.setColor( setName, colorExp );
        }
        
        if ( distinct.hasLength() )
            lengthAct = testGUI.getLength( setName );
        if ( distinct.hasSpacing() )
            spacingAct = testGUI.getSpacing( setName );
        if ( distinct.hasStroke() )
            strokeAct = testGUI.getStroke( setName );
        if ( distinct.hasDraw() )
            drawAct = testGUI.getDraw( setName );
        if ( distinct.hasColor() )
            colorAct = testGUI.getColor( setName );

        System.out.println( setName + ":" );
        printThreeValues( 
            "    Length", 
            lengthOrig, 
            lengthExp, 
            lengthAct
        );
        printThreeValues( 
            "    Spacing", 
            spacingOrig, 
            spacingExp, 
            spacingAct
        );
        printThreeValues( 
            "    Stroke", 
            strokeOrig, 
            strokeExp, 
            strokeAct
        );
        printThreeValues( 
            "    Draw", 
            drawOrig, 
            drawExp, 
            drawAct
        );
        printThreeValues( 
            "    Color", 
            colorOrig, 
            toHexString( colorExp ), 
            toHexString( colorAct )
        );

// Uncomment the following line to pause the application with
// the ProfileEditor displayed after modifying all components
// associated with the give LinePropertySet properties.
//        JOptionPane.showMessageDialog( null, "Done" );
        assertEquals( lengthExp, lengthAct, "length" );
        assertEquals( spacingExp, spacingAct, "spacing" );
        assertEquals( strokeExp, strokeAct, "stroke" );
        assertEquals( drawExp, drawAct, "draw" );
        assertEquals( colorExp, colorAct, "color" );
    }

    /**
     * Format and print a line of text
     * detailing the values of a component
     * under three distinct conditions.
     * The conditions are original value
     * (the value of the component
     * immediately after the ProfileEditor),
     * expected value
     * (the expected value of the component
     * immediately after modification
     * by the validation logic),
     * and the actual value
     * (the actual value of the component
     * immediately after modification
     * by the validation logic).
     * 
     * @param label 
     *      label describing the property being edited
     *      by the component
     * @param orig
     *      the value of the component before modification
     *      by the validation logic
     * @param exp
     *      the expected value of the component after modification
     *      by the validation logic
     * @param act
     *      the actual value of the component after modification
     *      by the validation logic
     */
    private static void 
    printThreeValues( String label, Object orig, Object exp, Object act )
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( label ).append( ": " )
            .append( "Orig: " ).append( orig ).append( ", " )
            .append( "Exp: " ).append( exp ).append( ", " )
            .append( "Act: " ).append( act );
        System.out.println( bldr );
    }
    
    /**
     * Format a given integer value
     * as a hexadecimal string.
     * 
     * @param value the given value
     * 
     * @return the formatted string
     */
    private static String toHexString( int value )
    {
        String  hex = String.format( "0x%x", value );
        return hex;
    }
}
