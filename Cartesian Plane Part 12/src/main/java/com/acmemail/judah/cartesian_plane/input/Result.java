package com.acmemail.judah.cartesian_plane.input;

import java.util.List;
import java.util.Objects;

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
public final class Result
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
     * Messages are stored in an unmodifiable list.
     * A null messages argument is translated
     * to an empty, unmodifiable list.
     * The messages list may not contain null.
     * 
     * @param success   the given status
     * @param messages  the given list
     * 
     * @throws NullPointerException if messages contains a null element
     */
    public Result( boolean success, List<String> messages )
    {
        this.success = success;
        this.messages = messages == null ? 
            List.of() : List.copyOf( messages );
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
     * Returns an unmodifiable list of messages
     * associated with this result.
     * 
     * @return the list of messages associated with this result
     */
    public List<String> getMessages()
    {
        return messages;
    }
    
    @Override
    public int hashCode()
    {
        int hash    = Objects.hash( success, messages );
        return hash;
    }

    @Override
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( this == other )
            result = true;
        else if ( other instanceof Result that )
        {
            if ( this.success != that.success )
                result = false;
            else
                result = this.messages.equals( that.messages );
        }
        return result;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "success=" ).append( success )
            .append( ", messages=" ).append( messages );
        return bldr.toString();
    }
}
