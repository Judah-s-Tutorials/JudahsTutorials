package com.acmemail.judah.cartesian_plane.components;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.FileManager;
import com.acmemail.judah.cartesian_plane.test_utils.CPMenuBarDMTestUtils;

public class CPMenuBarDMTest
{
    private static final PropertyManager    pmgr        = 
        PropertyManager.INSTANCE;
    private static final File   testFileDir             =
        new File( "equationsTestData/CPMenuBar" );
    private static final File   saveAsApproveTestFile   =
        new File( testFileDir, "saveAsApproveTestFile.txt" );
    private static final File   saveAsCancelTestFile    =
        new File( testFileDir, "saveAsCancelTestFile.txt" );
    private static final File   saveTestFile    =
        new File( testFileDir, "saveTestFile.txt" );
    private static final File   openApproveTestFile     =
        new File( testFileDir, "openApproveTestFile.txt" );
    private static final File   openCancelTestFile      =
        new File( testFileDir, "openCancelTestFile.txt" );
    private static final File   deleteTestFile  =
        new File( testFileDir, "deleteTestFile.txt" );
    private static final File   miscTestFile            =
        new File( testFileDir, "miscTestFile.txt" );
    
    private CPMenuBarDMTestUtils    tester;
    
    @BeforeAll
    public static void beforeAll()
    {
        if ( !testFileDir.exists() )
            testFileDir.mkdirs();
        assertTrue( testFileDir.exists() );
            
        createTestData( saveTestFile, "Save Test");
        createTestData( openApproveTestFile, "Open Approve Test");
        createTestData( openCancelTestFile, "Open Cancel Test");
        createTestData( deleteTestFile, "Delete Test");
        createTestData( miscTestFile, "Misc Test");
    }
    
    @BeforeEach
    public void beforeEach()
    {
        tester = CPMenuBarDMTestUtils.getUtils();
        tester.setRelativePath( testFileDir );
        deleteTestData( saveAsApproveTestFile );
        deleteTestData( saveAsCancelTestFile );
        setProperty( CPConstants.DM_MODIFIED_PN, false );
        setProperty( CPConstants.DM_OPEN_FILE_PN, false );
        setProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
    }

    @AfterEach
    public void afterEach() throws Exception
    {
        tester.dispose();
        tester = null;
    }

    @Test
    public void newTest()
    {
        // No equation is loaded, so nothing to save, close or delete
        tester.testEnablement( false, false, false,false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Initiate a new equation. There's still no file loaded and
        // nothing has been modified, so there's nothing to save
        // or delete, but we should be able to save-as or close
        tester.newEquation();
        tester.testEnablement( false, true, true, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
    }

    @Test
    public void saveTest()
    {
        String  testEquationName    = "Save Test Equation Name";
        assertTrue( saveTestFile.exists() );
        
        // No equation is loaded, so nothing to save, close, or delete
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );

        
        // Open an existing equation. There's is now a file loaded
        // but nothing has been modified.
        tester.open( saveTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        assertNotNull( tester.getEquation() );
        
        // Modify the equation, so there is now something to save.
        // Save, save-as and delete features should all be available.
        tester.setEquationName( testEquationName );
        tester.testEnablement( true, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );

        
        // Save the equation. Now data should be marked "unmodified
        // since last save," and a file should be open; it should be
        // possible to "Save As" to a different file, or delete the
        // current file.
        tester.save();
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        tester.testEnablement( false, true, true, true );
        
        // Open the equation just saved, and verify that it contains
        // the correct equation name.
        Equation    equation    = FileManager.open( saveTestFile );
        assertEquals( testEquationName, equation.getName() );
    }
    
    @Test
    public void saveAsTestApprove()
    {
        String  eqName1 = "Save As Test 1";
        String  eqName2 = "Save As Test 2";
        
        // The file for this test should not yet exist, data should
        // be unmodified and no file should be open. There should be
        // nothing to save, save-as, close, or delete.
        assertFalse( saveAsApproveTestFile.exists() );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Start a new equation. Data should be unmodified, and no
        // file should be open. There is nothing to save or delete,
        // but we should be able to save-as.
        tester.newEquation();
        tester.testEnablement( false, true, true, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );

        // Modify the equation. Data is now modified but there
        // is no file open; the save-as feature should be available,
        // but not save or delete.
        tester.setEquationName( eqName1 );
        tester.testEnablement( false, true, true, false );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );

        // Save the equation under a new name. Data should now be
        // "unchanged since last save," so the save feature should
        // be unavailable. Save-as and delete features should be
        // available.
        tester.saveAs( saveAsApproveTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );

        // Does the file now exist, and does it describe an
        // equation with the correct equation name?
        assertTrue( saveAsApproveTestFile.exists() );
        Equation    equation    = FileManager.open( saveAsApproveTestFile );
        assertNotNull( equation );
        assertEquals( eqName1, equation.getName() );
        
        /////////////////////////////////////////////////////////////
        // Ensure that the file created above is the default file
        // for saving.
        /////////////////////////////////////////////////////////////
        
        // Modify the equation a second time. Data is now modified and
        // there is an open file; the save-as, save, close, and delete
        // features should all be available,
        tester.setEquationName( eqName2 );
        tester.testEnablement( true, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );

        // Save the file; the file should still be open, but
        // the data should be marked "unchanged since last save.
        // Available features: Save NO, save-as YES, delete YES.
        tester.save();
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );

        assertTrue( saveAsApproveTestFile.exists() );
        equation  = FileManager.open( saveAsApproveTestFile );
        assertNotNull( equation );
        assertEquals( eqName2, equation.getName() );
    }
    
    @Test
    public void saveAsTestCancel()
    {
        String  eqName  = "Save As Cancel Test";
        
        // The output file for this test should not exist, but
        // the input file should. Data should be unmodified and no 
        // file or equation should be open. There should be nothing to 
        // save, save-as, or delete.
        assertFalse( saveAsCancelTestFile.exists() );
        assertTrue( miscTestFile.exists() );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Open the input file. There is now a file and an equation,
        // but no modified data, so save should not be available. 
        // save-as, close, and delete should be available.
        tester.open( miscTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        // Modify the data.
        tester.setEquationName( eqName );
        tester.testEnablement( true, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        Equation    originalEquation    = tester.getEquation();
        // Start a save-as operation, but cancel it before
        // it completes. Verify that property state hasn't
        // changed, and that specified file is not created.
        tester.saveAs( saveAsCancelTestFile, false );
        tester.testEnablement( true, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        assertEquals( originalEquation, tester.getEquation() );
        assertEquals( eqName, tester.getEquationName() );
        assertFalse( saveAsCancelTestFile.exists() );
        
        // Verify that the original input file is still the
        // default file for save operations.
        tester.save();
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        assertEquals( originalEquation, tester.getEquation() );
        
        Equation    testEquation    = FileManager.open( miscTestFile );
        assertEquals( eqName, testEquation.getName() );
    }
    
    @Test
    public void closeTest()
    {
        // There is no file for this test. No file should be open,
        // no equation should be open. 
        // be unmodified and no file should be open. There should be
        // nothing to save, save-as, or delete.
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        assertNull( tester.getEquation() );
        
        // Start a new equation. There is an open equation, but no
        // file. Data has not been modified. Save-as, close, and
        // delete features should be available, but not save.
        tester.newEquation();
        tester.testEnablement( false, true, true, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        assertNotNull( tester.getEquation() );
        
        // Close the equation. Afterwards there should be no open equation,
        // no open file, and nothing modified. Should not be able to save,
        // save-as, close or delete.
        tester.close();
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        assertNull( tester.getEquation() );
        
        // Open a new file. Afterwards there should be an open equation,
        // and an open file, but nothing modified. Should not be able to
        // save, but save-as, close and delete features should all be
        // available.
        tester.open( miscTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        assertNotNull( tester.getEquation() );
    }
    
    @Test
    public void deleteTest()
    {
        // The file for this test should exist, data should
        // be unmodified and no file should be open. There should be
        // nothing to save, save-as, or delete.
        assertTrue( deleteTestFile.exists() );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Open delete test file. There is an open equation, an
        // open file, but data has not been modified. Save-as and
        // delete features should be available, but not save.
        tester.open( deleteTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        // Delete the file. Afterwards there should be no open equation,
        // no open file, and nothing modified. Should not be able to save,
        // save-as or delete.
        tester.delete();
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        assertFalse( deleteTestFile.exists() );
        assertNull( tester.getEquation() );
    }
    
    @Test
    public void openTestApprove()
    {
        String  eqName  = "Open Test 1";
        
        // The file for this test should exist, data should
        // be unmodified and no file should be open. There should be
        // nothing to save, save-as, or delete.
        assertTrue( openApproveTestFile.exists() );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Open a file. MODIFIED should test false, OPEN_FILE
        // and OPEN_EQATION should be true. Save feature should
        // be unavailable, save-as and delete should be available.
        tester.open( openApproveTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        // Make a change to the DM. MODIFIED should switch to true,
        // save should be available, everything else is unchanged.
        tester.setEquationName( eqName );
        tester.testEnablement( true, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        // Save the data, and verify that is is saved to the
        // correct file.
        tester.save();
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        Equation    equation    = FileManager.open( openApproveTestFile );
        assertEquals( eqName, equation.getName() );
    }
        
    /**
     * With no equation or file open, 
     * start an open operation and cancel it.
     */
    @Test
    public void openFromNothingTestCancel()
    {
        // The file for this test should exist, data should
        // be unmodified and no file should be open. There should be
        // nothing to save, save-as, or delete.
        assertTrue( openCancelTestFile.exists() );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Start to open a file, but cancel the operation. 
        // Nothing in the state should change.
        tester.open( openCancelTestFile, false );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
    }
    
    /**
     * With equation and file open, 
     * start an open operation then cancel it.
     */
    @Test
    public void openFromExistingTestCancel()
    {
        String  eqName  = "Open from Existing but Cancel test";
        
        // The files for this test should exist, data should
        // be unmodified and no file should be open. There should be
        // nothing to save, save-as, or delete.
        assertTrue( miscTestFile.exists() );
        assertTrue( openCancelTestFile.exists() );
        tester.testEnablement( false, false, false, false );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, false );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, false );
        
        // Open the input file.
        tester.open( miscTestFile, true );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        // Start to open a file, but cancel the operation. 
        // Nothing in the state should change.
        tester.open( openCancelTestFile, false );
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        // Verify that the original file is still the default
        // for save operations. Save a change to the original file
        // and verify that the change is correctly saved.
        tester.setEquationName( eqName );
        tester.testEnablement( true, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, true );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        tester.save();
        tester.testEnablement( false, true, true, true );
        testProperty( CPConstants.DM_MODIFIED_PN, false );
        testProperty( CPConstants.DM_OPEN_FILE_PN, true );
        testProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        
        Equation    testEquation    = FileManager.open( miscTestFile );
        assertNotNull( testEquation );
        assertEquals( eqName, testEquation.getName() );
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
    
    /**
     * Creates a test data file with the given path,
     * encapsulating an Equation with the given name.
     * If the file already exists it will be overwritten.
     * Except for the name,
     * all equation properties will default.
     * 
     * @param path  the given path
     * @param name  the given name
     */
    private static void createTestData( File path, String name )
    {
        if ( path.exists() )
            path.delete();
        Equation    equation    = new Exp4jEquation();
        equation.setName( name );
        FileManager.save( path, equation );
        
        // Sanity check
        assertTrue( path.exists() );
        Equation    test    = FileManager.open( path );
        assertNotNull( test );
        assertEquals( name, test.getName() );
    }
    
    /**
     * Deletes the file in the given path
     * if it exists.
     * 
     * @param path  the given path
     */
    private void deleteTestData( File path )
    {
        if ( path.exists() )
            path.delete();
    }
}
