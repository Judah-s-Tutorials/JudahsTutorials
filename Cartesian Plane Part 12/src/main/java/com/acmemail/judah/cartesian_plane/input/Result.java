package com.acmemail.judah.cartesian_plane.input;

import java.util.List;

/**
 * Describes the result of an operation.
 * Contains a list of messages
 * associated with the result;
 * in the case of a successful result,
 * the list will typically be empty.
 * The list will never be null,
 * and is always unmodifiable.
 * @param success   the status of this Result
 * @param messages  the messages associated with this Result;
 *                  a null value is treated as an empty list,
 *                  and the list may not contain null elements
 *
 * @author Jack Straub
 */
public final record Result( boolean success, List<String> messages )
{
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
     * Compact constructor.
     * Creates a Result with the given status
     * and list of messages.
     * Normalizes a null messages list to an empty list,
     * and otherwise stores a defensive copy
     * 
     * @throws NullPointerException if messages contains a null element
     */
    public Result
    {
        messages = messages == null ? List.of() : List.copyOf( messages );
    }
}
