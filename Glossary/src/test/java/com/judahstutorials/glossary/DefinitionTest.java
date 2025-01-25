package com.judahstutorials.glossary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test_utils.TestDB;

class DefinitionTest
{
    private TestDB  testDB;
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        ConnectionMgr.selectDatabase( ConnectionMgr.TESTING );
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        testDB = new TestDB();
    }

    @AfterEach
    public void afterEach() throws Exception
    {
        ConnectionMgr.closeConnection();
    }

    @AfterEach
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testDefinition()
    {
        Definition  def     = new Definition();
        assertNull( def.getID() );
        assertEquals( "", def.getTerm() );
        assertEquals( 0, def.getSeqNum() );
        assertEquals( "", def.getSlug() );
        assertEquals( "", def.getDescription() );
        List<SeeAlso>   list    = def.getSeeAlso();
        assertNotNull( list );
        assertEquals( 0, list.size() );
    }

    @Test
    public void testDefinitionStringIntegerStringString()
    {
        String  term    = "termTest";
        Integer seqNum  = 2;
        String  slug    = "testSlug";
        String  desc    = "testDesc";
        
        Definition  def     = 
            new Definition(
                term,
                seqNum,
                slug,
                desc
            );
        assertNull( def.getID() );
        assertEquals( term, def.getTerm() );
        assertEquals( seqNum, def.getSeqNum() );
        assertEquals( slug, def.getSlug() );
        assertEquals( desc, def.getDescription() );
        List<SeeAlso>   list    = def.getSeeAlso();
        assertNotNull( list );
        assertEquals( 0, list.size() );
    }

    @Test
    public void testDefinitionResultSet()
    {
        Definition          expDef  = testDB.getDefinition( 0 );
        String              sqlStr  =
            "SELECT * FROM definition WHERE id = ?";
        PreparedStatement   sql     = 
            ConnectionMgr.getPreparedStatement( sqlStr );
        ResultSet           result  = null;
        try
        {
            sql.setInt( 1, expDef.getID() );
            result = sql.executeQuery();
            assertNotNull( result );
            assertTrue( result.next() );
            Definition  actDef = new Definition( result );
            assertDefEquals( expDef, actDef );
            result.close();
        }
        catch( SQLException exc )
        {
            fail( exc );
        }
    }

    @Test
    public void testMarkForDelete()
    {
        Definition  def     = new Definition();
        assertFalse( def.isMarkedForDelete() );
        def.markForDelete( true );
        assertTrue( def.isMarkedForDelete() );
        def.markForDelete( false );
        assertFalse( def.isMarkedForDelete() );
    }

    @Test
    public void testCommitInsertEmptySAList()
    {
        String  term    = "termTest";
        Integer seqNum  = 2;
        String  slug    = "testSlug";
        String  desc    = "testDesc";
        
        Definition  expDef  = 
            new Definition(
                term,
                seqNum,
                slug,
                desc
            );
        testCommit( expDef );
    }

    @Test
    public void testCommitInsertNonEmptySAList()
    {
        String  term    = "termTest";
        Integer seqNum  = 2;
        String  slug    = "testSlug";
        String  desc    = "testDesc";
        
        Definition  expDef  = 
            new Definition(
                term,
                seqNum,
                slug,
                desc
            );
        expDef.addSeeAlso( new SeeAlso( null, "url A" ) );
        expDef.addSeeAlso( new SeeAlso( null, "url B" ) );
        expDef.addSeeAlso( new SeeAlso( null, "url C" ) );
        testCommit( expDef );
    }
    
    @Test
    public void commitUpdate()
    {
        Definition  def     = testDB.getDefinition( 0 );
        def.addSeeAlso( new SeeAlso( "NEW SEE ALSO" ) );
        def.setTerm( def.getTerm() + "_updated" );
        def.setSeqNum( def.getSeqNum() + 1 );
        def.setSlug( def.getSlug() + "_updated" );
        def.setDescription( def.getDescription() + "_updated" );
        testCommit( def );
    }
    
    @Test
    public void commitDelete()
    {
        Definition  def     = testDB.getDefinition( 0 );
        Integer     ident   = def.getID();
        assertNotNull( testDB.queryDefGivenID( ident ) );
        List<SeeAlso>   list    = SeeAlso.getAllFor( ident );
        assertNotNull( list );
        assertTrue( list.size() > 0 );
        
        // Sanity check
        
        def.markForDelete( true );
        def.commit();
        Definition  testDef = testDB.queryDefGivenID( ident );
        assertNull( testDef );
        
        list = SeeAlso.getAllFor( ident );
        assertNotNull( list );
        assertEquals( 0, list.size() );
    }

    @Test
    public void testAddSeeAlso()
    {
        Definition      def     = new Definition();
        String[]        urls    = new String[]{ "URLA", "URLB", "URLC" };
        List<SeeAlso>   expList = new ArrayList<>();
        Stream.of( urls )
            .map( SeeAlso::new )
            .peek( expList::add )
            .peek( def::addSeeAlso )
            .map( u -> def.getSeeAlso() )
            .forEach( l -> assertListEquals( expList, l ) );
    }

    @Test
    public void testSetTerm()
    {
        String      testTerm    = "TestTerm";
        Definition  def     = new Definition();
        assertEquals( "", def.getTerm() );
        def.setTerm( testTerm );
        assertEquals( testTerm, def.getTerm() );
    }

    @Test
    public void testGetSeeAlso()
    {
        Definition      def     = new Definition();
        SeeAlso         seeAlso = new SeeAlso( "URL Z" );
        List<SeeAlso>   actList = def.getSeeAlso();
        assertNotNull( actList );
        assertEquals( 0, actList.size() );
        
        def.addSeeAlso( seeAlso );
        actList = def.getSeeAlso();
        assertNotNull( actList );
        assertEquals( 1, actList.size() );
        assertEquals( seeAlso.getURL(), actList.get( 0 ).getURL() );
    }

    @Test
    public void testSelectGoRight()
    {
        Definition  expDef  = testDB.getDefinition( 0 );
        Definition  actDef  = Definition.select( expDef.getTerm() );
        assertNotNull( actDef );
        assertDefEquals( expDef, actDef );
    }

    @Test
    public void testSelectGoWrong()
    {
        Definition  actDef  = Definition.select( "NO SUCH TERM" );
        assertNull( actDef );
    }

    @Test
    public void testInsert()
    {
        String  term    = "termTest";
        Integer seqNum  = 2;
        String  slug    = "testSlug";
        String  desc    = "testDesc";
        
        Definition  expDef  = 
            new Definition(
                term,
                seqNum,
                slug,
                desc
            );
        expDef.addSeeAlso( new SeeAlso( null, "url A" ) );
        expDef.addSeeAlso( new SeeAlso( null, "url B" ) );
        expDef.addSeeAlso( new SeeAlso( null, "url C" ) );
        expDef.insert();
        
        assertNotNull( expDef.getID() );
        Definition  actDef  = testDB.queryDefGivenID( expDef.getID() );
        assertNotNull( actDef );
        assertDefEquals( expDef, actDef );
    }

    @Test
    public void testGetTerm()
    {
        String      testTerm    = "TestTerm";
        Definition  def         = new Definition();
        assertEquals( "", def.getTerm() );
        def.setTerm( testTerm );
        assertEquals( testTerm, def.getTerm() );
    }

    @Test
    public void testGetTermDisplay()
    {
        int         testSeqNum  = 1;
        String      testTerm    = "TestTerm";
        Definition  def         = new Definition();
        assertEquals( "", def.getTermDisplay() );
        def.setTerm( testTerm );
        assertEquals( testTerm, def.getTermDisplay() );
        
        def.setSeqNum( testSeqNum );
        String      expTermDisplay  =
            testTerm + "(" + testSeqNum + ")";
        assertEquals( expTermDisplay, def.getTermDisplay() );
    }

    @Test
    public void testSetSeqNum()
    {
        Integer     testSeqNum  = 1;
        Definition  def         = new Definition();
        assertEquals( 0, def.getSeqNum() );
        def.setSeqNum( testSeqNum );
        assertEquals( testSeqNum, def.getSeqNum() );
    }

    @Test
    public void testSetSlug()
    {
        String      testSlug    = "Test Slug";
        Definition  def         = new Definition();
        assertEquals( "", def.getSlug() );
        def.setSlug( testSlug );
        assertEquals( testSlug, def.getSlug() );
    }

    @Test
    public void testSetDescription()
    {
        String      testDesc    = "Test Definition";
        Definition  def         = new Definition();
        assertEquals( "", def.getDescription() );
        def.setDescription( testDesc );
        assertEquals( testDesc, def.getDescription() );
    }

    @Test
    public void testToString()
    {
        // make sure toString doesn't crash on a trivial Definition
        new Definition().toString();
        
        String      testTerm    = "Test Term";
        Integer     testSeqNum  = 1;
        String      testSlug    = "Test Slug";
        String      testDesc    = "Test";
        Definition  def         = new Definition(
            testTerm,
            testSeqNum,
            testSlug,
            testDesc
        );
        String      testStr     = def.toString();
        assertTrue( testStr.contains( testTerm ) );
        assertTrue( testStr.contains( String.valueOf( testSeqNum ) ) );
        assertTrue( testStr.contains( testDesc ) );
    }

    @Test
    public void testGetID()
    {
        Integer     testID  = 1000;
        Definition  def     = new Definition();
        assertNull( def.getID() );
        def.setID( testID );
        assertEquals( testID, def.getID() );
    }
    
    @Test
    public void testMisc()
    {
        Definition  def     = new Definition();
        def.setSeqNum( null );
        assertEquals( 0, def.getSeqNum() );
        
        def.setDescription( null );
        assertEquals( "", def.getDescription() );
        
        def.setTerm( null );
        assertEquals( "", def.getTerm() );
        
        def.setDescription( "XXXXXXXXXX YYYYYYYYYY" );
        String  testStr = def.toString();
        assertTrue( testStr.contains( "XXX" ) );
        assertTrue( testStr.contains( "YYY" ) );
        
        def.markForDelete( true );
        def.commit();
        
        def.setID( 1000 );
        def.insert();
        
        def.setTerm( "test" );
        def.setSeqNum( null );
        def.setSlug( null );
        assertNotNull( def.getSlug() );
        assertEquals( "test", def.getSlug() );
        def.setSlug( "" );
        assertNotNull( def.getSlug() );
        assertEquals( "test", def.getSlug() );
        def.setSlug( null );
        def.setSeqNum( 1 );
        assertEquals( "test-1", def.getSlug() );
    }
    
    private void testCommit( Definition expDef )
    {
        expDef.commit();
        Integer ident   = expDef.getID();
        assertNotNull( ident );
        Definition  actDef  = testDB.queryDefGivenID( ident );
        assertDefEquals( expDef, actDef );
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
}
