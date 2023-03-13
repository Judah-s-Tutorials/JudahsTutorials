package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NotificationManagerTest
{
    private static final NotificationManager    nMgr    =
        NotificationManager.INSTANCE;
    
    @Test
    void testAddNotificationListenerNotificationListener()
    {
        TestListener    test1       = new TestListener();
        TestListener    test2       = new TestListener();
        String          property    = "prop";
        nMgr.addNotificationListener( test1 );
        nMgr.addNotificationListener( test2 );
        nMgr.propagateNotification( property );
        assertTrue( test1.invoked );
        assertTrue( test2.invoked );
        assertEquals( property, test1.property );
        assertEquals( property, test2.property );
    }

    @Test
    void testAddNotificationListenerStringNotificationListener()
    {
        TestListener    testAll     = new TestListener();
        TestListener    testProp1_1 = new TestListener();
        TestListener    testProp1_2 = new TestListener();
        TestListener    testProp2   = new TestListener();
        
        String          prop1       = "prop1";
        String          prop2       = prop1 + "___";
        
        nMgr.addNotificationListener( testAll );
        nMgr.addNotificationListener( prop1, testProp1_1 );
        nMgr.addNotificationListener( prop1, testProp1_2 );
        nMgr.addNotificationListener( prop2, testProp2 );
        nMgr.propagateNotification( prop1 );
        
        assertTrue( testAll.invoked );
        assertEquals( prop1, testAll.property );
        
        assertTrue( testProp1_1.invoked );
        assertEquals( prop1, testProp1_1.property );
        
        assertTrue( testProp1_2.invoked );
        assertEquals( prop1, testProp1_2.property );
        
        assertFalse( testProp2.invoked );
    }

    @Test
    void testRemoveNotificationListenerNotificationListener()
    {
        TestListener    test1       = new TestListener();
        TestListener    test2       = new TestListener();
        String          property    = "prop";
        nMgr.addNotificationListener( test1 );
        nMgr.addNotificationListener( test2 );
        nMgr.propagateNotification( property );
        assertTrue( test1.invoked );
        assertTrue( test2.invoked );
        
        test1.invoked = false;
        test2.invoked = false;
        nMgr.removeNotificationListener( test2 );
        nMgr.propagateNotification( property );
        assertTrue( test1.invoked );
        assertFalse( test2.invoked );
        
        test1.invoked = false;
        test2.invoked = false;
        nMgr.removeNotificationListener( test1 );
        nMgr.propagateNotification( property );
        assertFalse( test1.invoked );
        assertFalse( test2.invoked );
    }

    @Test
    void testRemoveNotificationListenerNotificationListenerString()
    {
        TestListener    testProp1_1 = new TestListener();
        TestListener    testProp1_2 = new TestListener();
        String          prop1       = "prop1";
        
        nMgr.addNotificationListener( prop1, testProp1_1 );
        nMgr.addNotificationListener( prop1, testProp1_2 );
        
        nMgr.propagateNotification( prop1 );
        assertTrue( testProp1_1.invoked );
        assertTrue( testProp1_2.invoked );
        
        testProp1_1.invoked = false;
        testProp1_2.invoked = false;
        nMgr.removeNotificationListener( prop1, testProp1_2 );
        nMgr.propagateNotification( prop1 );
        assertTrue( testProp1_1.invoked );
        assertFalse( testProp1_2.invoked );
        
        testProp1_1.invoked = false;
        testProp1_2.invoked = false;
        nMgr.removeNotificationListener( prop1, testProp1_1 );
        nMgr.propagateNotification( prop1 );
        assertFalse( testProp1_1.invoked );
        assertFalse( testProp1_2.invoked );
    }

    @Test
    void testPropagateNotificationString()
    {
        String              property    = "prop";
        
        TestListener        listener    = new TestListener();
        nMgr.addNotificationListener( listener );
        nMgr.propagateNotification( property );
        assertTrue( listener.invoked );
        assertEquals( nMgr, listener.source );
        assertEquals( property, listener.property );
        assertNull( listener.data );
    }

    @Test
    void testPropagateNotificationObjectString()
    {
        Object              source      = new Object();
        String              property    = "prop";
        
        TestListener        listener    = new TestListener();
        nMgr.addNotificationListener( listener );
        nMgr.propagateNotification( source, property );
        assertTrue( listener.invoked );
        assertEquals( source, listener.source );
        assertEquals( property, listener.property );
        assertNull( listener.data );
    }

    @Test
    void testPropagateNotificationObjectStringObject()
    {
        Object              source      = new Object();
        String              property    = "prop";
        Integer             data        = Integer.valueOf( 42 );
        
        TestListener        listener    = new TestListener();
        nMgr.addNotificationListener( listener );
        nMgr.propagateNotification( source, property, data );
        assertTrue( listener.invoked );
        assertEquals( source, listener.source );
        assertEquals( property, listener.property );
        assertEquals( data, listener.data );
    }

    @Test
    void testPropagateNotificationNotificationEvent()
    {
        Object              source      = new Object();
        String              property    = "prop";
        Integer             data        = Integer.valueOf( 42 );
        NotificationEvent   event       = 
            new NotificationEvent( source, property, data );
        
        TestListener        listener    = new TestListener();
        nMgr.addNotificationListener( listener );
        nMgr.propagateNotification( event );
        assertTrue( listener.invoked );
        assertEquals( source, listener.source );
        assertEquals( property, listener.property );
        assertEquals( data, listener.data );
    }
    
    private static class TestListener implements NotificationListener
    {
        private boolean invoked     = false;
        private Object  source      = null;
        private String  property    = null;
        private Object  data        = null;
        
        public void accept( NotificationEvent event )
        {
            invoked = true;
            source = event.getSource();
            property = event.getProperty();
            data = event.getData();
        }
    }
}
