package com.acmemail.judah.cartesian_plane.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the result of an operation.
 * Contains a list of messages
 * associated with the result;
 * in the case of a successful result,
 * the list will typically be empty.
 * The list will never be null.
 * 
 * @author Jack Straub
 */
public class Result
{
    private final boolean       success;
    private final List<String>  messages;
    
    /**
     * Constructor.
     * Creates a Result with the given status
     * and an empty list of messages.
     * 
     * @param success   the given status
     */
    public Result( boolean success )
    {
        this( success, null );
    }
    
    /**
     * Constructor.
     * Creates a Result with the given status
     * and list of messages.
     * The given list
     * is copied into 
     * an internally allocated buffer.
     * 
     * @param success   the given status
     * @param messages  the given list
     */
    public Result( boolean success, List<String> messages )
    {
        this.success = success;
        this.messages = new ArrayList<>();
        if ( messages != null )
            this.messages.addAll( messages );
    }

    /**
     * Returns the status of this Result.
     * 
     * @return the status of this Result
     */
    public boolean isSuccess()
    {
        return success;
    }

    /**
     * Returns the list of messages
     * associated with this result.
     * 
     * @return the list of messages associated with this result
     */
    public List<String> getMessages()
    {
        return messages;
    }
}
