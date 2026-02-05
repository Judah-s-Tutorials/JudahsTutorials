package com.acmemail.judah.cartesian_plane;

/**
 * This interface describes
 * an object that can
 * process a NotificationEvent.
 * 
 * @author Jack Straub
 */
@FunctionalInterface
public interface NotificationListener
{
    /**
     * Accepts a NotificationEvent.
     * 
     * @param event the NotificationEvent
     */
    void accept( NotificationEvent event );
}
