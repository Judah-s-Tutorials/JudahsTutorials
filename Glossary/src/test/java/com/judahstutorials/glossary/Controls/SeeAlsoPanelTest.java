package com.judahstutorials.glossary.Controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

import test_utils.SeeAlsoTestGUI;
import test_utils.TestDB;

class SeeAlsoPanelTest
{
    private static final SeeAlsoTestGUI testGUI = 
        SeeAlsoTestGUI.getTestGUI();
    private TestDB  testDB;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        testDB = new TestDB();
        testGUI.reset();
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        ConnectionMgr.closeConnection();
    }

    @Test
    public void testSeeAlsoPanel()
    {
        validateReset();
    }

    @Test
    public void testSetDefinition0SeeAlso()
    {
        assertFalse( testGUI.getPanelEnabled() );
        Definition  def     = new Definition();
        testGUI.setDefinition( def );
        assertTrue( testGUI.getPanelEnabled() );
        assertEquals( 0, testGUI.getElements().size() );
    }

    @Test
    public void testSetDefinitionNon0SeeAlso()
    {
        Definition  def     = new Definition();
        SeeAlso     see1    = new SeeAlso();
        SeeAlso     see2    = new SeeAlso();
        SeeAlso     see3    = new SeeAlso();
        see1.updateURL( "url 1" );
        see2.updateURL( "url 2" );
        see3.updateURL( "url 3" );
        def.addSeeAlso( see1 );
        def.addSeeAlso( see2 );
        def.addSeeAlso( see3 );
        
        assertFalse( testGUI.getPanelEnabled() );
        testGUI.setDefinition( def );
        assertTrue( testGUI.getPanelEnabled() );
        List<SeeAlso>   panelList   = testGUI.getElements();
        List<SeeAlso>   defList     = def.getSeeAlso();
        assertEquals( defList, panelList );
    }
    
    @Test
    public void testPostNewItem()
    {
        String[]    urls    = { "URL 1", "URL 2", "URL 3" };
        Definition  def     = new Definition();
        testGUI.setDefinition( def );
        Stream.of( urls )
            .peek( testGUI::setNewLinkText )
            .forEach( s -> 
                assertTrue( testGUI.getNewLinkText().isEmpty() )
            );
        
        List<SeeAlso>   list    = testGUI.getElements();
        Stream.of( urls )
            .map( s -> matchURL( s, list ) )
            .forEach( s -> assertNotNull( s ) );
    }

    @Test
    public void testReset()
    {
        validateReset();
        testGUI.setDefinition( new Definition() );
        assertTrue( testGUI.getPanelEnabled() );
        testGUI.reset();
        validateReset();
    }
    
    @Test
    public void testDelete()
    {
        int         delNum  = 1;
        Definition  def     = new Definition();
        IntStream.of( 1, 2, 3 )
            .mapToObj( i -> "URL " + i )
            .map( s -> new SeeAlso( null, s ) )
            .forEach( def::addSeeAlso );
        testGUI.setDefinition( def );
        testGUI.selectElement( delNum );
        testGUI.delete();
        List<SeeAlso>   list    = def.getSeeAlso();
        SeeAlso         seeAlso = list.get( delNum );
        assertTrue( seeAlso.isMarkedForDelete() );
    }
    
    private void validateReset()
    {
        assertFalse( testGUI.getPanelEnabled() );
        assertTrue( testGUI.getNewLinkText().isEmpty() );
        assertEquals( 0, testGUI.getElements().size() );
    }
    
    private SeeAlso matchURL( String url, List<SeeAlso> list )
    {
        SeeAlso seeAlso =
            list.stream()
                .filter( s -> url.equals( s.getURL() ) )
                .findFirst().orElse( null );
        return seeAlso;
    }

    private void pause()
    {
        JOptionPane.showMessageDialog( null, "Waiting..." );;
    }
}
