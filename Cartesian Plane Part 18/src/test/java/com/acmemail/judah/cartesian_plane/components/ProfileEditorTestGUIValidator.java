package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ProfileEditorTestGUIValidator
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
    private static ProfileEditorTestGUI    testGUI;
    private static Profile          profile         = new Profile();
    
    @BeforeAll
    public static void beforeAll()
    {
        testGUI = ProfileEditorTestGUI.getTestGUI( baseProfile );
    }
    
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

        boolean fontDrawOrig   = testGUI.isFontDraw();
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

        boolean fontBoldOrig   = testGUI.isFontBold();
        boolean fontBoldExp    = mwDistinct.isBold();
        boolean fontBoldAct    = false;

        boolean fontItalicOrig = testGUI.isFontItalic();
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
        Utils.pause( 5000 );
        testGUI.selectFDOK();
        Utils.join( thread );
        
        gridUnitAct = testGUI.getGridUnit();
        fontDrawAct = testGUI.isFontDraw();
        gridColorAct = testGUI.getBGColor();
        gridSColorAct = toHexString( gridColorAct );
        fontColorAct = testGUI.getFGColor();
        fontSColorAct = toHexString( fontColorAct );
        fontNameAct = testGUI.getFontName();
        fontSizeAct = testGUI.getFontSize();
        fontBoldAct = testGUI.isFontBold();
        fontItalicAct = testGUI.isFontItalic();
        
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
        
        JOptionPane.showMessageDialog( null, "Done" );
        assertEquals( gridUnitExp, gridUnitAct );
        assertEquals( fontDrawExp, fontDrawAct );
        assertEquals( gridColorExp, gridColorAct );
        assertEquals( fontColorExp, fontColorAct );
        assertEquals( fontNameExp, fontNameAct );
        assertEquals( fontSizeExp, fontSizeAct );
        assertEquals( fontBoldExp, fontBoldAct );
        assertEquals( fontItalicExp, fontItalicAct );
    }
    
    private void tempLinePropertyTest( String setName )
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
        String  sColorAct       = "";
        
        boolean drawOrig        = false;
        boolean drawExp         = false;
        boolean drawAct         = false;
        
        System.out.println( setName + ":" );
        
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
            drawExp = distinct.hasDraw();
            testGUI.setDraw( setName, drawExp );
        }
        
        if ( distinct.hasColor() )
        {
            colorOrig = testGUI.getColor( setName );
            colorExp = distinct.getColor().getRGB() &0xffffff;
            testGUI.setColor( setName, colorExp );
        }

        JOptionPane.showMessageDialog( null, "Done" );
    }

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
    
    private static String toHexString( int value )
    {
        String  hex = String.format( "0x%x", value );
        return hex;
    }
}
