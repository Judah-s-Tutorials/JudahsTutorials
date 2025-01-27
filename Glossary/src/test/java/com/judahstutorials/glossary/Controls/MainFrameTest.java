package com.judahstutorials.glossary.Controls;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test_utils.GUIUtils;
import test_utils.MainFrameTestGUI;
import test_utils.TestDB;

import static com.judahstutorials.glossary.Controls.MainFrame.*;
import static com.judahstutorials.glossary.Controls.SeeAlsoPanel.*;

class MainFrameTest
{
    private static MainFrame        mainFrame   = null;
    private static MainFrameTestGUI testGUI     = null;
    
    private TestDB              testDB      = new TestDB();
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> mainFrame = new MainFrame() );
        testGUI = MainFrameTestGUI.getMainFrameTestGUI();
    }

    @BeforeEach
    void setUp() throws Exception
    {
        testDB = new TestDB();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        testGUI.click( CANCEL_BUTTON );
    }

    @Test
    public void testMainFrame()
    {
        assertTrue( testGUI.isVisible() );
        validateEnabled( NEW_BUTTON, QUERY_BUTTON, EXIT_BUTTON );
        validateNotEnabled( 
            TERM_ID_FIELD, 
            TERM_FIELD,
            SEQ_NUM_FIELD,
            SLUG_FIELD,
            DESC_FIELD,
            DELETE_BUTTON,
            COMMIT_BUTTON,
            CANCEL_BUTTON,
            SA_DELETE_BUTTON,
            SA_NEW_TEXT
        );
        validateTextEmpty();
    }
    
    @Test
    public void testNew()
    {
        validateEnabled( NEW_BUTTON );
        testGUI.click( NEW_BUTTON );
        validateTextEmpty();
        validateEnabled( 
            TERM_ID_FIELD, 
            TERM_FIELD,
            SEQ_NUM_FIELD,
            SLUG_FIELD,
            DESC_FIELD,
            NEW_BUTTON,
            COMMIT_BUTTON,
            CANCEL_BUTTON,
            SA_NEW_TEXT
        );
        validateNotEnabled( DELETE_BUTTON, SA_DELETE_BUTTON );
        
        testGUI.setText( TERM_FIELD, "test term" );
        testGUI.setText( SLUG_FIELD, "test slug" );
        testGUI.setText( DESC_FIELD, "test description" );
        testGUI.setValue( SEQ_NUM_FIELD, 5 );

        validateEnabled( NEW_BUTTON );
        testGUI.click( NEW_BUTTON );
        validateTextEmpty();
        validateEnabled( 
            TERM_ID_FIELD, 
            TERM_FIELD,
            SEQ_NUM_FIELD,
            SLUG_FIELD,
            DESC_FIELD,
            NEW_BUTTON,
            COMMIT_BUTTON,
            CANCEL_BUTTON,
            SA_NEW_TEXT
        );
        validateNotEnabled( DELETE_BUTTON, SA_DELETE_BUTTON );
}

    private void validateEnabled( String... strings )
    {
        Stream.of( strings ).forEach( s ->
            assertTrue( testGUI.isEnabled( s ), s )
        );
    }

    private void validateNotEnabled( String... strings )
    {
        Stream.of( strings ).forEach( s ->
            assertFalse( testGUI.isEnabled( s ), s )
        );
    }
    
    private void validateTextEmpty()
    {
        validateTextEmpty( 
            TERM_FIELD, 
            SLUG_FIELD, 
            DESC_FIELD, SA_NEW_TEXT
        );
        assertNull( testGUI.getIDFromText( TERM_ID_FIELD ) );
        assertNull( testGUI.getIDFromText( SEQ_NUM_FIELD ) );
    }
    
    private void validateTextEmpty( String... strings )
    {
        Stream.of( strings ).forEach( s -> {
            String  text    = testGUI.getText( s );
            assertEquals( "", text );
        });
    }
}
