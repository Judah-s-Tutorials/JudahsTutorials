package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import javax.swing.JMenuItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.test_utils.CPMenuBarDMTestUtils;

class CPMenuBarDMTest
{
    private static final PropertyManager pmgr           = 
        PropertyManager.INSTANCE;
    private static final Scanner    scanner = new Scanner( System.in );
    
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
