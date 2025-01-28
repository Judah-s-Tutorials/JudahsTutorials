package com.judahstutorials.glossary.Controls;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.SeeAlso;

import test_utils.GUIUtils;
import test_utils.MainFrameTestGUI;
import test_utils.TestDB;

import static com.judahstutorials.glossary.Controls.MainFrame.*;
import static com.judahstutorials.glossary.Controls.SeeAlsoPanel.*;

class MainFrameTest
{
    private static final String     testTerm    = "Test Term";
    private static final String     testURL     = "Test URL ";
    private static List<SeeAlso>    testURLList = getURLList();

    private static MainFrame        mainFrame   = null;
    private static MainFrameTestGUI testGUI     = null;
    
    private TestDB              testDB      = new TestDB();
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        ConnectionMgr.selectDatabase( ConnectionMgr.TESTING );
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
    
    @Test
    public void testCommitMisc()
    {
        String  testTerm    = "Test Term";
        int     testSeqNum  = 5;
        String  testSlug    = "Test SLUG";
        String  testDescr   = "Test Description";
        List<SeeAlso> list  = getURLList();
        testGUI.click( NEW_BUTTON );
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

        testGUI.setText( TERM_FIELD, testTerm );
        testGUI.setValue( SEQ_NUM_FIELD, testSeqNum );
        testGUI.setText( SLUG_FIELD, testSlug );
        testGUI.setText( DESC_FIELD, testDescr );

        list.forEach( s -> testGUI.setValue( SA_NEW_TEXT, s.getURL() ) );
//        pause();
        testGUI.click( COMMIT_BUTTON );
        ConnectionMgr.closeConnection();
//        pause();
        
        Definition  expDef  = mainFrame.getCurrDef();
        Integer     expID   = expDef.getID();
        assertNotNull( expID );
        Definition  actDef  = testDB.queryDefGivenID( expID );
        assertDefEquals( expDef, actDef );
        
//        assertNotNull( expDef.getID() );
//        Definition  actDef  = testDB.queryDefGivenID( expDef.getID() );
//        assertDefEquals( expDef, actDef );
//        
//        int             newSeqNum   = 100;
//        List<SeeAlso>   list        = expDef.getSeeAlso();
//        List<SeeAlso>   expURLList  = new ArrayList<>();
//        int             lastInx     = list.size();
//        expDef.setSeqNum( newSeqNum );
//        IntStream.iterate( 0, i -> i < lastInx, i -> i + 1 )
//            .forEach( i -> {
//                SeeAlso seeAlso = list.get( i );
//                if ( i % 2 == 0 )
//                {
//                    String  newURL  = seeAlso.getURL();
//                    seeAlso.updateURL( newURL + " updated" );
//                    expURLList.add( seeAlso );
//                }
//                else if ( i % 3 == 0 )
//                    seeAlso.markForDelete( true );
//                else
//                    expURLList.add( seeAlso );
//            });
//        expDef.commit();
//        actDef = testDB.queryDefGivenID( expDef.getID() );
//        List<SeeAlso>   actURLList  = actDef.getSeeAlso();
//        assertEquals( expURLList.size(), actURLList.size() );
//        expURLList.stream()
//            .map( SeeAlso::getURL )
//            .forEach( s -> assertNotNull( getByURL( s, actURLList ) ) );
    }
    
    private void assertListEquals( List<SeeAlso> exp, List<SeeAlso> act )
    {
        assertNotNull( exp );
        assertNotNull( act );
        assertEquals( exp.size(), act.size() );
        exp.stream()
            .map( s -> s.getURL() )
            .map( u -> getByURL( u, act ) )
            .forEach( s -> assertNotNull( s ) );
    }

    private SeeAlso getByURL( String url, List<SeeAlso> list )
    {
        SeeAlso seeAlso =
            list.stream()
                .filter( s -> url.equals( s.getURL() ) )
                .findFirst()
                .orElse( null );
        return seeAlso;
    }

    private void assertDefEquals( Definition exp, Definition act )
    {
        assertEquals( exp.getID(), act.getID() );
        assertEquals( exp.getTerm(), act.getTerm() );
        assertEquals( exp.getSeqNum(), act.getSeqNum() );
        assertEquals( exp.getSlug(), act.getSlug() );
        assertEquals( exp.getDescription(), act.getDescription() );
        assertListEquals( exp.getSeeAlso(), act.getSeeAlso() );
    }


    private Definition getTestTransaction()
    {
        Definition  def = new Definition();
        def.setTerm( testTerm );
        testURLList.stream()
            .map( s -> s.getURL() )
            .map( SeeAlso::new )
            .forEach( def::addSeeAlso );
        return def;
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
    
    private static List<SeeAlso> getURLList()
    {
        List<SeeAlso>   list    =
            IntStream.iterate( 0, i -> i < 12 , i -> i + 1 )
                .mapToObj( i -> testURL + i )
                .map( SeeAlso::new )
                .toList();
        return list;
    }
    
    private static void pause()
    {
        JOptionPane.showMessageDialog( null, "Waiting" );
    }
}
