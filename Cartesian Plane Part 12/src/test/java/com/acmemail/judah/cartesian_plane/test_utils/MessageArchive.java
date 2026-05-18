package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Component;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.acmemail.judah.cartesian_plane.input.MessageConsumer;

/**
 * Encapsulates a list of messages propagated
 * by a MessageConsumer facility.
 * An instance of this class can serve as a
 * {@link MessageConsumer}.
 * 
 * @see MessageConsumer
 * @see MessageRec
 */
public class MessageArchive implements MessageConsumer, Iterable<MessageRec>
{
    /** Encapsulated list of messages; must be thread-safe. */
    private final List<MessageRec>  messageList = 
        new CopyOnWriteArrayList<>();
    /** Unmodifiable view of message list. */
    private final List<MessageRec> unmodifiableView =
        Collections.unmodifiableList(messageList);
    
    /**
     * Default constructor.
     * Fully initializes a new instance state.
     */
    public MessageArchive()
    {
    }
    
    @Override
    public void postMessage(
        Component parent,
        String message, 
        String title, 
        int messageType
    )
    {
        MessageRec messageRec = 
            new MessageRec( parent, message, title, messageType );
        messageList.add( messageRec );
    }
    
    /**
     * Gets an unmodifiable view of the encapsulated list.
     * 
     * @return  an unmodifiable view of the encapsulated list
     */
    public List<MessageRec> getMessageList()
    {
        return unmodifiableView;
    }

    /**
     * Returns an iterator on a <em>stable snapshot</em>
     * of the encapsulated MessageRec list.
     * Concurrent updates after the iterator is acquired
     * will not be observed in the snapshot.
     */
    @Override
    public Iterator<MessageRec> iterator()
    {
        return messageList.iterator();
    }
    
    /**
     * Gets the last MessageRec in the encapsulated list.
     * Returns null if the list is empty.
     * 
     * @return  
     *      the last MessageRec in the encapsulated list,
     *      or null if the list is empty
     */
    public MessageRec getLastMessageRec()
    {
        MessageRec  rec     = null;
        int         size    = messageList.size();
        // Concurrent operations could cause the list to be cleared
        // after interrogating the message list size. Silently catch
        // out-of-bounds exceptions.
        try
        {
            if ( size > 0 )
                rec = messageList.get( size - 1 );
        }
        catch ( IndexOutOfBoundsException exc )
        {
            // treat as if messageList.size() originally returned 0
        }
        return rec;
    }
    
    /**
     * Gets the message from the last MessageRec 
     * in the encapsulated list.
     * Returns null if the list is empty.
     * 
     * @return
     *      the message from last MessageRec in the encapsulated list,
     *      or null if the list is empty
     */
    public String getLastMessage()
    {
        String      message = null;
        MessageRec  rec     = getLastMessageRec();
        if ( rec != null )
            message = rec.message();
        return message;
    }
    
    /**
     * Returns true if the encapsulated list is empty.
     * 
     * @return  true if the encapsulated list is empty
     */
    public boolean isEmpty()
    {
        return messageList.isEmpty();
    }
    
    /**
     * Gets the size of the encapsulated list.
     * 
     * @return  the size of the encapsulated list
     */
    public int size()
    {
        return messageList.size();
    }
    
    /**
     * Clears the encapsulated list.
     */
    public void clear()
    {
        messageList.clear();
    }
}
