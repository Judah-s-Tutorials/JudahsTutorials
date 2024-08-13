package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Scanner;

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

    private static Profile          profile         = new Profile();
    private static Profile          distinctProfile = 
        ProfileUtils.getDistinctProfile( profile );
    private static ProfileEditorTestGUI    testGUI;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        testGUI = ProfileEditorTestGUI.getTestGUI( profile );
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    private void waitOp()
    {
        JOptionPane.showMessageDialog( null, "Waiting" );
    }
    
    @Test
    void testProfileEditor()
    {
        float   val     = testGUI.getGridUnit();
        System.out.println( "Grid unit: " + val );
        testGUI.setGridUnit( distinctProfile.getGridUnit() );
        
        String  name    = testGUI.getName();
        System.out.println( "Name: " + name );
        testGUI.setName( distinctProfile.getName() );
        
        GraphPropertySet    distinctVals    = distinctProfile.getMainWindow();
        String  fontName    = testGUI.getFontName();
        System.out.println( "font name: " + fontName );
        testGUI.setFontName( distinctVals.getFontName() );
        
        boolean isDraw = testGUI.isFontDraw();
        System.out.println( "isDraw: " + isDraw );
        testGUI.setFontDraw( distinctVals.isFontDraw() );

        Thread  thread  = testGUI.editFont();
        
        float fontSize      = testGUI.getFontSize();
        System.out.println( "font size: " + fontSize );
        testGUI.setFontSize( distinctVals.getFontSize() );
        
        boolean isBold = testGUI.isFontBold();
        System.out.println( "isBold: " + isBold );
        testGUI.setFontBold( distinctVals.isBold() );
        
        boolean isItalic = testGUI.isFontItalic();
        System.out.println( "isItalic: " + isItalic );
        testGUI.setFontItalic( distinctVals.isItalic() );
        
        int     color   = testGUI.getFGColor();
        System.out.println( "fontColor: " + color );
        testGUI.setFGColor( distinctVals.getFGColor().getRGB() );
        
        JOptionPane.showMessageDialog( null, "Done" );
        testGUI.selectFDCancel();
        
        Utils.join( thread );
    }
    
    @Test
    public void tempLinePropertyTest()
    {
        Arrays.stream( allSetNames )
            .forEach( this::tempLinePropertyTest );
    }
    
    private void tempLinePropertyTest( String setName )
    {
        LinePropertySet distinct    = 
            distinctProfile.getLinePropertySet( setName );
        println( setName + ":" );
        
        if ( distinct.hasLength() )
        {
            println( "    Length: " + testGUI.getLength( setName ) );
            testGUI.setLength( setName, distinct.getLength() );
        }
        
        if ( distinct.hasSpacing() )
        {
            println( "    Spacing: " + testGUI.getSpacing( setName ) );
            testGUI.setSpacing( setName, distinct.getSpacing() );
        }
        
        if ( distinct.hasStroke() )
        {
            println( "    Stroke: " + testGUI.getStroke( setName ) );
            testGUI.setStroke( setName, distinct.getStroke() );
        }
        
        if ( distinct.hasDraw() )
        {
            println( "    Draw: " + testGUI.getDraw( setName ) );
            testGUI.setDraw( setName, distinct.getDraw() );
        }
        
        if ( distinct.hasColor() )
        {
            println( "    Color: " + testGUI.getColor( setName ) );
            testGUI.setColor( setName, distinct.getColor().getRGB() );
        }

        JOptionPane.showMessageDialog( null, "Done" );
    }
    
    private void println( String text )
    {
        System.out.println( text );
    }

    @Test
    void testGetFeedBack()
    {
        fail("Not yet implemented");
    }

    @Test
    void testApply()
    {
        fail("Not yet implemented");
    }

    @Test
    void testReset()
    {
        fail("Not yet implemented");
    }

}
