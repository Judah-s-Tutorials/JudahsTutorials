package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import javax.swing.JMenuItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.FileManager;
import com.acmemail.judah.cartesian_plane.test_utils.CPMenuBarDMTestUtils;

class CPMenuBarDMTest
{
    private static final PropertyManager    pmgr            = 
        PropertyManager.INSTANCE;
    private static final File               testFileDir     =
        new File( "equationsTestData/CPMenuBar" );
    private static final File               saveAsTestFile  =
        new File( testFileDir, "saveAsTestFile.txt" );
    private static final File               saveTestFile    =
        new File( testFileDir, "saveAsTestFile.txt" );
    private static final File               openTestFile    =
        new File( testFileDir, "openTestFile.txt" );
    private static final File               deleteTestFile  =
        new File( testFileDir, "deleteTestFile.txt" );
    
    private static CPFrame                  plane;
    private static CPMenuBarDMTestUtils     tester;
    private static JMenuItem                newMenuItem;
    private static JMenuItem                openMenuItem;
    private static JMenuItem                saveMenuItem;
    private static JMenuItem                saveAsMenuItem;
    private static JMenuItem                deleteMenuItem;
    
    @BeforeAll
    public static void beforeAll()
    {
//        newMenuItem = tester.getMenuItem( "File", "New" );
//        openMenuItem = tester.getMenuItem( "File", "Open" );
//        saveMenuItem = tester.getMenuItem( "File", "Save" );
//        saveAsMenuItem = tester.getMenuItem( "File", "Save As" );
//        deleteMenuItem = tester.getMenuItem( "File", "Delete" );
    }
    
    @BeforeEach
    public void beforeEach()
    {
        tester = CPMenuBarDMTestUtils.getUtils();
        if ( saveAsTestFile.exists() )
            saveAsTestFile.delete();
        setProperty( CPConstants.DM_MODIFIED_PN, false );
        setProperty( CPConstants.DM_OPEN_FILE_PN, false );
    }

    @AfterEach
    void tearDown() throws Exception
    {
        tester.dispose();
        tester = null;
    }

    @Test
    public void newTest()
    {
        // No equation is loaded, so nothing to save or delete
        tester.testEnablement( false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        
        // Initiate a new equation. There's still no file loaded
        // nothing has been modified, so there's nothing to save
        // or delete.
        tester.testEnablement( false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
    }

    @Test
    public void saveTest()
    {
        // No equation is loaded, so nothing to save or delete
        tester.testEnablement( false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        
        // Initiate a new equation. There's still no file loaded
        // nothing has been modified, so there's nothing to save
        // or delete.
        tester.testEnablement( false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        
        // Mark data as modified. Now there is data that can be
        // "Saved As", but no file to save or delete.
        setProperty( CPConstants.DM_MODIFIED_PN, true );
        tester.testEnablement( false, true, false );
        
        // Mark file as open. Now files can be saved and deleted.
        // saved
        setProperty( CPConstants.DM_OPEN_FILE_PN, true );
        tester.testEnablement( true, true, true );
    }
    
    @Test
    public void saveAsTest()
    {
        assertFalse( saveAsTestFile.exists() );
        
        String  eqName  = "Save As Test";
        // Start a new equation and change something
        tester.newEquation();
        tester.setEquationName( eqName );
        // Mark the data model changed
        setProperty( CPConstants.DM_MODIFIED_PN, true );
      
        // there's no open file so it can't be Saved or Deleted,
        // but it can be Save As'd
        tester.testEnablement( false, true, false );
        tester.saveAs( saveAsTestFile.getPath(), true );
        
        // Does the file now exist, and does it describe an
        // equation with the correct equation name?
        assertTrue( saveAsTestFile.exists() );
        Equation    equation    = FileManager.open( saveAsTestFile );
        assertNotNull( equation );
        assertEquals( eqName, equation.getName() );
    }
    
    /**
     * Test the value of a given property
     * against a given expected value.
     * 
     * @param propName  the given property
     * @param expValue  the given expected value
     */
    private void testProperty( String propName, boolean expValue )
    {
        boolean actValue    = pmgr.asBoolean( propName );
        assertEquals( expValue, actValue );
    }
    
    /**
     * Set the value of a given property
     * to a given value.
     * 
     * @param propName  the given property
     * @param value     the given value
     */
    private void setProperty( String propName, boolean value )
    {
        pmgr.setProperty( propName, value );

    }
    
    private void openFileItem()
    {
        
    }
}
