package com.acmemail.judah.cartesian_plane;

/**
 * Encapsulates an event informing the listener 
 * of a notification.
 * The event is identified by a property name
 * contained in the event object.
 * 
 * @author Jack Straub
 *
 * @see NotificationListener
 * @see #getProperty()
 */
public class NotificationEvent
{
    /** Source of this event. */
    private final Object    source;
    
    /**
     * Name of the property associated with this event.
     */
    private final String    property;
    
    /** Additional data associated with this event; optional. */
    private final Object    data;
    
    /**
     * Constructor. 
     * Initializes all instance fields.
     * 
     * @param source    source of this event
     * @param property  name of property associated with this event
     * @param data      data associated with this event
     * 
     */
    public 
    NotificationEvent( Object source, String property, Object data )
    {
        this.source = source;
        this.property = property;
        this.data = data;
    }
    
    /**
     * Constructor. 
     * Initializes the source and property fields
     * to the give data.
     * The data field is set to null.
     * 
     * @param source    source of this event
     * @param property  name of property associated with this event
     * 
     */
    public NotificationEvent( Object source, String property )
    {
        this( source, property, null );
    }

    /**
     * Returns the optional data associated with this event.
     * 
     * @return the optional data associated with this event
     */
    public Object getData()
    {
        return data;
    }

    /**
     * Returns the source of this event.
     * 
     * @return the source of this event
     */
    public Object getSource()
    {
        return source;
    }

    /**
     * Returns the name of the property associated with this event.
     * 
     * @return the name of the property associated with this event
     */
    public String getProperty()
    {
        return property;
    }
}