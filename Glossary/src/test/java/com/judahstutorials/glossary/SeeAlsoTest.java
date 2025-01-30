package com.judahstutorials.glossary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
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
    public void setUp() throws Exception
    {
        testDB = new TestDB();
    }
    
    @AfterEach
    public void afterEach()
    {
        ConnectionMgr.closeConnection();
    }

    @Test
    public void testSeeAlsoIntIntString()
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
    public void testSeeAlsoIntString()
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
    public void testSeeAlso()
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
    public void testMarkForDelete()
    {
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isMarkedForDelete() );
        seeAlso.markForDelete( true );
        assertTrue(  seeAlso.isMarkedForDelete() );
        seeAlso.markForDelete( false );
        assertFalse(  seeAlso.isMarkedForDelete() );
    }

    @Test
    public void testIsError()
    {
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isError() );
    }

    @Test
    public void testMarkForUpdate()
    {
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isMarkedForUpdate() );
        seeAlso.markForUpdate( true );
        assertTrue(  seeAlso.isMarkedForUpdate() );
        seeAlso.markForUpdate( false );
        assertFalse(  seeAlso.isMarkedForUpdate() );
    }

    @Test
    public void testGetID()
    {
        Integer testID  = 400;
        SeeAlso seeAlso = new SeeAlso();
        assertNull( seeAlso.getID() );
        assertFalse( seeAlso.isMarkedForUpdate() );
        seeAlso.setID( testID );
        assertEquals( testID, seeAlso.getID() );
        // setID doesn't set 'marked for update'
        assertFalse( seeAlso.isMarkedForUpdate() );
    }

    @Test
    public void testGetTermID()
    {
        Integer testTermID  = 400;
        SeeAlso seeAlso = new SeeAlso();
        assertNull( seeAlso.getTermID() );
        assertFalse( seeAlso.isMarkedForUpdate() );
        seeAlso.updateTermID( testTermID );
        assertEquals( testTermID, seeAlso.getTermID() );
        assertTrue( seeAlso.isMarkedForUpdate() );
    }

    @Test
    public void testGetURL()
    {
        String  testURL = "Temp URL";
        SeeAlso seeAlso = new SeeAlso();
        assertFalse( seeAlso.isMarkedForUpdate() );
        seeAlso.updateURL( testURL );
        assertEquals( testURL, seeAlso.getURL() );
        assertTrue( seeAlso.isMarkedForUpdate() );
    }

    @Test
    public void testInsert()
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
    public void testInsertIntString()
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
    public void testUpdate()
    {
        String          updateStr   = "_modified";
        Definition      def         = testDB.getDefinition( 0 );
        int             termID      = def.getID();
        List<SeeAlso>   allSAsOrig  = SeeAlso.getAllFor( termID );
        assertNotNull( allSAsOrig );
        assertTrue( allSAsOrig.size() > 0 );
        allSAsOrig.forEach( seeAlso -> {
            String  url = seeAlso.getURL() + updateStr;
            seeAlso.updateURL( url );
            assertTrue( seeAlso.isMarkedForUpdate() );
            seeAlso.update();
            assertFalse( seeAlso.isMarkedForUpdate() );
       });
        
        List<SeeAlso>   allSAsUpd   = SeeAlso.getAllFor( termID );
        assertEquals( allSAsOrig.size(), allSAsUpd.size() );
        allSAsOrig.forEach( seeAlso -> {
            String  url = seeAlso.getURL();
            assertTrue( url.endsWith( updateStr ) );
            assertFalse( seeAlso.isMarkedForUpdate() );
        });
    }

    @Test
    public void testDelete()
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
    public void testDeleteAll()
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
    public void testCommit()
    {
        final String    updateSuffix    = "updated";
        
        int     termID      = 1000;
        // Create a list with two objects to later be deleted,
        // two objects to later be update, and two objects to
        // later remain unchanged.
        SeeAlso toKeep1     = new SeeAlso( termID, "to keep 1" );
        SeeAlso toDelete1   = new SeeAlso( termID, "to delete 1" );
        SeeAlso toUpdate1   = new SeeAlso( termID, "to update 1" );

        SeeAlso toKeep2     = new SeeAlso( termID, "to keep 2" );
        SeeAlso toDelete2   = new SeeAlso( termID, "to delete 2" );
        SeeAlso toUpdate2   = new SeeAlso( termID, "to update 2" );
        
        List<SeeAlso>   inList  = new ArrayList<>();
        inList.add( toKeep1 );
        inList.add( toDelete1 );
        inList.add( toUpdate1 );
        
        inList.add( toKeep2 );
        inList.add( toDelete2 );
        inList.add( toUpdate2 );
        
        SeeAlso.commit( inList );
        List<SeeAlso>   outList1    = SeeAlso.getAllFor( termID );
        assertNotNull( outList1 );
        assertEquals( inList.size(), outList1.size() );
        inList.forEach( s -> assertNotNull( getByID( s, outList1 ) ) );
        outList1.forEach( s -> assertNotNull( s.getID() ) );
        
        for ( SeeAlso seeAlso : inList )
        {
            String  url     = seeAlso.getURL();
            assertFalse( seeAlso.isMarkedForUpdate() );
            if ( url.contains( "delete" ) )
                seeAlso.markForDelete( true );
            else if ( url.contains( "update" ) )
            {
                seeAlso.updateURL( url + updateSuffix );
                assertTrue( seeAlso.isMarkedForUpdate() );
            }
            else
                ;
        }
        
        SeeAlso toInsert1   = new SeeAlso( termID, "to insert 1" );
        SeeAlso toInsert2   = new SeeAlso( termID, "to insert 2" );
        SeeAlso toInsert3   = new SeeAlso( termID, "to insert 3" );
        inList.add( toInsert1 );
        inList.add( toInsert2 );
        inList.add( toInsert3 );
        SeeAlso.commit( inList );
        List<SeeAlso>   outList2    = SeeAlso.getAllFor( termID );
        int     expCount    = 0;
        for ( SeeAlso seeAlso : inList )
        {
            // Verify that all objects marked for delete are gone
            String  inURL     = seeAlso.getURL();
            if ( inURL.contains( "delete" ) )
            {
                assertNull( getByID( seeAlso, outList2 ) );
            }
            else
            {
                // Verify that all objects not marked for delete 
                // are present
                ++expCount;
                SeeAlso outSeeAlso = getByID( seeAlso, outList2 );
                assertNotNull( outSeeAlso );
                assertNotNull( outSeeAlso.getID() );
                String  outURL  = seeAlso.getURL();
                
                // Verify that all objects marked for update
                // have been updated.
                if ( inURL.contains( "update" ) )
                {
                    assert( outURL.startsWith( inURL ) );
                    assert( outURL.endsWith( updateSuffix ) );
                }
                // Validate all object meant to be "kept," and
                // all inserted objects.
                else
                    assertEquals( inURL, outURL );
            }
        }
        assertEquals( expCount, outList2.size() );
    }

    @Test
    public void testToString()
    {
        int     termID  = 1000;
        String  url     ="address";
        SeeAlso seeAlso = new SeeAlso( termID, url );
        String  testStr = seeAlso.toString();
        assertFalse( testStr.contains( "HTML" ) );
        assertTrue( testStr.contains( url ) );
        
        seeAlso.markForDelete( true );
        testStr = seeAlso.toString();
        assertTrue( testStr.contains( "HTML" ) );
        assertTrue( testStr.contains( url ) );
    }

    @Test
    public void testGetAllFor()
    {
        Definition      def         = testDB.getDefinition( 0 );
        int             termID      = def.getID();
        List<SeeAlso>   allSAsOrig  = SeeAlso.getAllFor( termID );
        assertNotNull( allSAsOrig );
        assertTrue( allSAsOrig.size() > 2 );
        allSAsOrig.forEach( s -> assertEquals( termID, s.getTermID() ) );
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
