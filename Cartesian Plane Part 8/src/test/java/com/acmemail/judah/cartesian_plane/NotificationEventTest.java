package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NotificationEventTest
{
    @Test
    void testNotificationEventObjectStringObject()
    {
        Object  source      = this;
        String  property    = "prop";
        Integer data        = Integer.valueOf( 42 );
        
        NotificationEvent   event   =
            new NotificationEvent( source, property, data );
        assertEquals( source, event.getSource() );
        assertEquals( property, event.getProperty() );
        assertEquals( data, event.getData() );
    }

    @Test
    void testNotificationEventObjectString()
    {
        Object  source      = this;
        String  property    = "prop";
        
        NotificationEvent   event   =
            new NotificationEvent( source, property );
        assertEquals( source, event.getSource() );
        assertEquals( property, event.getProperty() );
        assertNull( event.getData() );
    }

    @Test
    void testGetData()
    {
        Object  source      = this;
        String  property    = "prop";
        Integer data        = Integer.valueOf( 42 );
        
        NotificationEvent   event   =
            new NotificationEvent( source, property, data );
        assertEquals( data, event.getData() );
    }

    @Test
    void testGetSource()
    {
        Object  source      = this;
        String  property    = "prop";
        Integer data        = Integer.valueOf( 42 );
        
        NotificationEvent   event   =
            new NotificationEvent( source, property, data );
        assertEquals( source, event.getSource() );
    }

    @Test
    void testGetProperty()
    {
        Object  source      = this;
        String  property    = "prop";
        Integer data        = Integer.valueOf( 42 );
        
        NotificationEvent   event   =
            new NotificationEvent( source, property, data );
        assertEquals( property, event.getProperty() );
    }
}
