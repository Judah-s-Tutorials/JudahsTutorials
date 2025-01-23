package com.judahstutorials.glossary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test_utils.TestDB;

class SeeAlsoTest
{
    private TestDB  testDB;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        ConnectionMgr.selectDatabase( ConnectionMgr.TESTING );
    }

    @BeforeEach
    void setUp() throws Exception
    {
        testDB = new TestDB();
    }

    @Test
    void testSeeAlsoIntIntString()
    {
        int     ident   = 25;
        int     termID  = 35;
        String  url     = "URL 45";
        SeeAlso test    = new SeeAlso( ident, termID, url );
        assertEquals( ident, test.getID() );
        assertEquals( termID, test.getTermID() );
        assertEquals( url, test.getURL() );
    }

    @Test
    void testSeeAlsoIntString()
    {
        Integer ident   = null;
        int     termID  = 35;
        String  url     = "URL 45";
        SeeAlso test    = new SeeAlso( termID, url );
        assertEquals( ident, test.getID() );
        assertEquals( termID, test.getTermID() );
        assertEquals( url, test.getURL() );
    }

    @Test
    void testSeeAlso()
    {
        Integer ident   = null;
        Integer termID  = null;
        String  url     = "";
        SeeAlso test    = new SeeAlso( termID, url );
        assertEquals( ident, test.getID() );
        assertEquals( termID, test.getTermID() );
        assertEquals( url, test.getURL() );
    }

    @Test
    void testMarkForDelete()
    {
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isMarkedForDelete() );
        seeAlso.markForDelete( true );
        assertTrue(  seeAlso.isMarkedForDelete() );
        seeAlso.markForDelete( false );
        assertFalse(  seeAlso.isMarkedForDelete() );
    }

    @Test
    void testIsError()
    {
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isError() );
    }

    @Test
    void testMarkForUpdate()
    {
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isMarkedForUpdate() );
        seeAlso.markForUpdate( true );
        assertTrue(  seeAlso.isMarkedForUpdate() );
        seeAlso.markForUpdate( false );
        assertFalse(  seeAlso.isMarkedForUpdate() );
    }

    @Test
    void testGetID()
    {
        Integer testID  = 400;
        SeeAlso seeAlso = new SeeAlso();
        assertNull( seeAlso.getID() );
        seeAlso.setID( testID );
        assertEquals( testID, seeAlso.getID() );
    }

    @Test
    void testGetURL()
    {
        String  testURL = "Test URL";
        SeeAlso seeAlso = new SeeAlso();
        seeAlso.setURL( testURL );
        assertEquals( testURL, seeAlso.getURL() );
    }

    @Test
    void testInsert()
    {
        int             termID      = 1000;
        List<SeeAlso>   expected    = new ArrayList<>();
        for ( int inx = 100 ; inx < 103 ; ++inx )
        {
            SeeAlso seeAlso = new SeeAlso( termID, "URL " + inx );
            expected.add( seeAlso );
            seeAlso.insert();
        }
        List<SeeAlso>  actual      = SeeAlso.getAllFor( termID );
        assertNotNull( actual );
        assertEquals( expected.size(), actual.size() );
        for ( SeeAlso expSee : expected )
        {
            SeeAlso actSee   = getByID( expSee, actual );
            assertNotNull( actSee );
            assertTrue( equals( expSee, actSee ) );
        }
    }

    @Test
    void testInsertIntString()
    {
        int             termID      = 1000;
        List<SeeAlso>   expected    = new ArrayList<>();
        for ( int inx = 100 ; inx < 103 ; ++inx )
        {
            String  url     = "URL " + inx;
            SeeAlso seeAlso = SeeAlso.insert( termID, url );
            assertNotNull( seeAlso.getID() );
            assertEquals( termID, seeAlso.getTermID() );
            assertEquals( url, seeAlso.getURL() );
            expected.add( seeAlso );
        }
        List<SeeAlso>  actual      = SeeAlso.getAllFor( termID );
        assertNotNull( actual );
        assertEquals( expected.size(), actual.size() );
        for ( SeeAlso expSee : expected )
        {
            SeeAlso actSee   = getByID( expSee, actual );
            assertNotNull( actSee );
            assertTrue( equals( expSee, actSee ) );
        }
    }

    @Test
    void testUpdate()
    {
        String          updateStr   = "_modified";
        Definition      def         = testDB.getDefinition( 0 );
        int             termID      = def.getID();
        List<SeeAlso>   allSAsOrig  = SeeAlso.getAllFor( termID );
        assertNotNull( allSAsOrig );
        assertTrue( allSAsOrig.size() > 0 );
        allSAsOrig.forEach( seeAlso -> {
            String  url = seeAlso.getURL() + updateStr;
            seeAlso.setURL( url );
            seeAlso.markForUpdate( true );
            seeAlso.update();
       });
        
        List<SeeAlso>   allSAsUpd   = SeeAlso.getAllFor( termID );
        assertEquals( allSAsOrig.size(), allSAsUpd.size() );
        allSAsOrig.forEach( seeAlso -> {
            String  url = seeAlso.getURL();
            assertTrue( url.endsWith( updateStr ) );
        });
    }

    @Test
    void testDelete()
    {
        int             testInx     = 2;
        Definition      def         = testDB.getDefinition( 0 );
        int             termID      = def.getID();
        List<SeeAlso>   allSAsOrig  = SeeAlso.getAllFor( termID );
        assertNotNull( allSAsOrig );
        assertTrue( allSAsOrig.size() > testInx );
        
        SeeAlso seeAlso = allSAsOrig.remove( testInx );
        seeAlso.delete();
        List<SeeAlso>   allSAsUpd   = SeeAlso.getAllFor( termID );
        assertEquals( allSAsOrig.size(), allSAsUpd.size() );
        assertNull( getByID( seeAlso, allSAsUpd ) );
        allSAsOrig.forEach( s -> 
            assertNotNull( getByID( s, allSAsUpd ) )
        );
    }

    @Test
    void testDeleteAll()
    {
        Definition      def         = testDB.getDefinition( 0 );
        int             termID      = def.getID();
        List<SeeAlso>   allSAsOrig  = SeeAlso.getAllFor( termID );
        assertNotNull( allSAsOrig );
        assertTrue( allSAsOrig.size() > 2 );
        
        SeeAlso.deleteAll( termID );
        List<SeeAlso>   allSAsUpd   = SeeAlso.getAllFor( termID );
        assertNotNull( allSAsUpd );
        assertEquals( 0, allSAsUpd.size() );
    }

    @Test
    void testCommit()
    {
        fail("Not yet implemented");
    }

    @Test
    void testToString()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetAllFor()
    {
        fail("Not yet implemented");
    }
    
    private static boolean equals( SeeAlso see1, SeeAlso see2 )
    {
        boolean rVal    = false;
        if ( !Objects.equals( see1.getID(), see2.getID() ) )
            ;
        else if ( !Objects.equals( see1.getTermID(), see2.getTermID() ) )
            ;
        else if ( !Objects.equals( see1.getURL(), see2.getURL() ) )
            ;
        else
            rVal = true;
        return rVal;

    }
    
    private static SeeAlso getByID( SeeAlso seeAlso, List<SeeAlso> list )
    {
        SeeAlso result  = 
            list.stream()
                .filter( s -> seeAlso.getID() == s.getID() )
                .findFirst().orElse( null );
        return result;
    }
}
