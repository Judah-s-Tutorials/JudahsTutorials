package com.acmemail.judah.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the status of an operation (true or false)
 * and messages associated with the operations.
 */
public class Result
{
    private boolean         status  = false;
    private List<String>    messages    = new ArrayList<>();
    
    /**
     * Set the status of this result.
     * 
     * @param status    the status of this result
     */
    public void setStatus( boolean status )
    {
        this.status = status;
    }
    
    /**
     * Get the status of this result.
     * 
     * @return  the status of this result
     */
    public boolean getStatus()
    {
        return status;
    }
    
    /**
     * Gets an unmodifiable view
     * of the messages encapsulated in this result.
     * 
     * @return  
     *      an unmodifiable view
     *      of the messages encapsulated in this result
     */
    public List<String> getMessages()
    {
        List<String>    list    = Collections.unmodifiableList( messages );
        return list;
    }
    
    /**
     * Adds a message to the encapsulated list of messages.
     * 
     * @param message   the message to add
     */
    public void addMessage( String message )
    {
        messages.add( message );
    }
    
    /**
     * Adds a message to the encapsulated list of messages
     * and sets the status of the result.
     * 
     * @param message   the message to add
     */
    public void addMessage( String message, boolean status )
    {
        this.status = status;
        messages.add( message );
    }
}
