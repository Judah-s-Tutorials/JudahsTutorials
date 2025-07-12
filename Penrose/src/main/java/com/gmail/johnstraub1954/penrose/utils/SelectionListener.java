package com.gmail.johnstraub1954.penrose.utils;

/**
 * An object of this class listens for SelectionEvents
 * from the {@linkplain SelectionManager}.
 * 
 * @see SelectionEvent
 */
public interface SelectionListener
{
    /**
     * Method to invoke when a SelectionEvent is dispatched.
     * 
     * @param event object that describes the event
     */
    void select( SelectionEvent event );
}
