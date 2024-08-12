package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.fail;

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
        
        float fontSize      = testGUI.getFontSize();
        System.out.println( "font size: " + fontSize );
        testGUI.setFontSize( distinctVals.getFontSize() );
        
        boolean isBold = testGUI.isFontBold();
        System.out.println( "isBold: " + isBold );
        testGUI.setFontBold( distinctVals.isBold() );
        
        boolean isItalic = testGUI.isFontItalic();
        System.out.println( "isItalic: " + isItalic );
        testGUI.setFontItalic( distinctVals.isItalic() );
        
        boolean isDraw = testGUI.isFontDraw();
        System.out.println( "isDraw: " + isDraw );
        testGUI.setFontDraw( distinctVals.isFontDraw() );
        
        int     color   = testGUI.get
        
        Thread  thread  = testGUI.editFont();
        Utils.join( thread );
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
