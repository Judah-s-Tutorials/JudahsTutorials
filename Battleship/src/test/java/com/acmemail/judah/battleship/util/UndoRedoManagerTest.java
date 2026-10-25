package com.acmemail.judah.battleship.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UndoRedoManagerTest
{
    private final Map<Integer,String>   testMap = new HashMap<>();
    private final UndoRedoManager       undoMgr = new UndoRedoManager();
    
    @BeforeEach
    public void beforeEach()
    {
        testMap.clear();
        undoMgr.init();
    }
    
    @Test
    public void basicUndoTest()
    {
        Map<Integer,String> expMap      = new HashMap<>();
        Deque<Integer>      intStack    = new ArrayDeque<>();
        int                 min         = -20;
        int                 max         = 20;
        int                 step        = 2;
        getIterator( min, max, step ).forEach( item -> {
            assertEquals( expMap, testMap );
            put( item, expMap );
            assertEquals( expMap, testMap );
            undoMgr.undo();
            expMap.remove( item );
            assertEquals( expMap, testMap );
            put( item, expMap );
            assertEquals( expMap, testMap );
            intStack.push( item );
        });
        
        while ( !intStack.isEmpty() )
        {
            Integer bottom  = intStack.pop();
            expMap.remove( bottom );
            undoMgr.undo();
            assertEquals( expMap, testMap );
        }
    }
    
    @Test
    public void basicRedoTest()
    {
        Map<Integer,String> expMap      = new HashMap<>();
        Deque<Integer>      intStack    = new ArrayDeque<>();
        int                 min         = -20;
        int                 max         = 20;
        int                 step        = 2;
        getIterator( min, max, step ).forEach( item -> {
            assertEquals( expMap, testMap );
            put( item, expMap );
            assertEquals( expMap, testMap );
            undoMgr.undo();
            String  was = expMap.remove( item );
            assertEquals( expMap, testMap );
            undoMgr.redo();
            expMap.put( item, was );
            assertEquals( expMap, testMap, "" + item );
            intStack.push( item );
        });
        
        while ( !intStack.isEmpty() )
        {
            Integer bottom  = intStack.pop();
            expMap.remove( bottom );
            undoMgr.undo();
            assertEquals( expMap, testMap );
        }
        
        getIterator( min, max, step ).forEach( item -> {
            assertEquals( expMap, testMap );
            undoMgr.redo();
            expMap.put( item, String.valueOf( item ) );
            assertEquals( expMap, testMap );
        });
    }
    
    @Test
    public void testEmptyStack()
    {
        Map<Integer,String> expMap      = new HashMap<>();
        Integer             key         = 10;
        String              val         = String.valueOf( key );
        expMap.put( key, val );
        testMap.put( key, val );
        assertEquals( expMap, testMap );
        undoMgr.undo();
        assertEquals( expMap, testMap );
        undoMgr.redo();
        assertEquals( expMap, testMap );
    }
    
    @Test
    public void testExecuteClearsRedoStack()
    {
        Map<Integer,String> expMap      = new HashMap<>();
        Deque<Integer>      tracker     = new ArrayDeque<>();
        int                 min         = -20;
        int                 max         = 20;
        int                 mid         = (min + max) / 2;
        int                 step        = 2;
    
        getIterator( min, max, step ).forEach( item -> {
            put( item, expMap );
            tracker.push( item );
            assertEquals( expMap, testMap );
        });

        getIterator( min, mid, step ).forEach( i -> {
            Integer val     = tracker.pop();
            undoMgr.undo();
            expMap.remove( val );
            assertEquals( expMap, testMap );
        });
        
        Integer newKey  = max + 1;
        put( newKey, expMap );
        assertEquals( expMap, testMap );
        undoMgr.redo();
        assertEquals( expMap, testMap );
    }
    
    /**
     * Generate a stream of Integers,
     * from min to max, stepping by step.
     * 
     * @param min   the minimum value of the range
     * @param max   the maximum value of the range, exclusive
     * @param step  the increment
     * 
     * @return  a stream of Integers, from min to max, stepping by step
     */
    private static Stream<Integer> getIterator( int min, int max, int step )
    {
        Stream<Integer> stream  = 
            IntStream.iterate( min, i -> i < max, i -> i + step )
            .boxed();
        return stream;
    }
    
    /**
     * Generate a PutCommand from the given key,
     * and execute it via the undo manager.
     * The value mapped to the key
     * is the stringified value of the key.
     * If shadowMap is non-null,
     * the key is also put to the it, as well.
     * 
     * @param key       the key to put
     * @param shadowMap the shadow map; may be null
     * @return
     */
    private String put( Integer key, Map<Integer,String> shadowMap )
    {
        String      val     = String.valueOf( key );
        PutCommand  cmd     = new PutCommand( key, val );
        String      origVal = testMap.get( key );
        undoMgr.executeCommand( cmd );
        if ( shadowMap != null )
            shadowMap.put( key, val );
        return origVal;
    }
    
    /**
     * Implements undo/redo logic
     * for Map.put( k, v ).
     */
    private class PutCommand implements Command
    {
        /** Key to put. */
        private final Integer   key;
        /** Value to put. */
        private final String    val;
        /** Original value mapped by key. */
        private String          orig;
        
        /**
         * Constructor.
         * 
         * @param key   the key to put
         * @param val   the value to put
         */
        public PutCommand( Integer key, String val )
        {
            this.val = val;
            this.key = key;
        }
        
        @Override
        public void execute()
        {
            orig = testMap.put( key, val );
        }

        @Override
        public void undo()
        {
            if ( orig == null )
                testMap.remove( key );
            else
                testMap.put( key, orig );
        }
    }
}
