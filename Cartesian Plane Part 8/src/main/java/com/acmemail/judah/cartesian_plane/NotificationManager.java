package com.acmemail.judah.cartesian_plane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class maintains lists
 * of NotificationListeners.
 * It is implemented as a singleton.
 * A client may register a listener
 * for all notifications,
 * or for notifications for a specific property.
 * @author Jack Straub
 */
public enum NotificationManager
{
    /** The single instance associated with this class. */
    INSTANCE;
    
    /** List of NotificationListeners. */
    private final List<NotificationListener>  notificationListeners = 
        new ArrayList<>();
    /** map of property to list of notification listeners. */
    private final Map<String, List<NotificationListener>> 
        notificationPropertyMap = new HashMap<>();
    
    /**
     * Add a given NotificationListener to the list of NotificationListeners.
     * Listeners will be invoked for every notification event.
     * 
     * @param listener  the given NotificationListener
     * 
     * @see #addNotificationListener(String, NotificationListener)
     */
    public void addNotificationListener( NotificationListener listener )
    {
        notificationListeners.add( listener );
    }
    /**
     * Add a given NotificationListener/property pair to the list 
     * of NotificationListeners.
     * Listeners will be invoked for only for property notifications
     * encapsulating the given property.
     * 
     * @param listener  the given NotificationListener
     * @param property  the given property
     * 
     * @see #addNotificationListener(NotificationListener)
     */
    public void 
    addNotificationListener( String property, NotificationListener listener )
    {
        List<NotificationListener>  listenerList    =
            notificationPropertyMap.get( property );
        if ( listenerList == null )
        {
            listenerList = new ArrayList<>();
            notificationPropertyMap.put( property, listenerList );
        }
        listenerList.add( listener );
    }
    
    /**
     * Remove a given NotificationListener from the 
     * list of NotificationListeners.
     * If the listener is not in the list
     * no action is taken.
     * If the listener 
     * is in the list multiple times
     * only the first instance
     * will be removed.
     * 
     * @param listener  the given NotificationListener
     */
    public void removeNotificationListener( NotificationListener listener )
    {
        notificationListeners.remove( listener );
    }
    
    /**
     * Remove a given NotificationListener from the 
     * list of NotificationListeners for a given property.
     * If the NotificationListener is not in the list
     * no action is taken.
     * If the listener 
     * is in the list multiple times
     * only the first instance
     * will be removed.
     * 
     * @param listener  the given NotificationListener
     * @param property  the given property
     */
    public void removeNotificationListener(
        String property, 
        NotificationListener listener 
    )
    {
        notificationListeners.remove( listener );
        List<NotificationListener>  list    = 
            notificationPropertyMap.get( property );
        if ( list != null )
            list.remove( listener );
    }
    
    /**
     * Instantiates a NotificationEvent
     * using the given property,
     * and propagates it to all NotificationListeners.
     * The NotificationEvent source
     * defaults to the NotificationManager singleton.
     * The NotificationEvent data defaults to null.
     * 
     * @param property  the given property
     */
    public void 
    propagateNotification( String property )
    {
        propagateNotification( INSTANCE, property, null );
    }
    
    /**
     * Instantiates a NotificationEvent
     * using the given source and property,
     * and propagates it to all NotificationListeners.
     * The NotificationEvent data defaults to null.
     * 
     * @param source    the given source
     * @param property  the given property
     */
    public void 
    propagateNotification( Object source, String property )
    {
        propagateNotification( source, property, null );
    }
    
    /**
     * Instantiates a NotificationEvent
     * using the give source, property and data,
     * and propagates it to all NotificationListeners.
     * 
     * @param source    the given source
     * @param property  the given property
     * @param data      the given data
     */
    public void 
    propagateNotification( Object source, String property, Object data )
    {
        NotificationEvent   event   = 
            new NotificationEvent( source, property, data );
        propagateNotification( event );
    }
    
    /**
     * Propagates a given NotificationEvent
     * to all NotificationListeners.
     * 
     * @param event the given NotificationEvent
     */
    public void propagateNotification( NotificationEvent event )
    {
        List<NotificationListener>  perPropertyList =
            notificationPropertyMap.getOrDefault( 
                event.getProperty(), 
                new ArrayList<>()
        );
        Stream.concat( 
            perPropertyList.stream(), 
            notificationListeners.stream()
        ).forEach( l -> l.accept( event ) );
    }
}
