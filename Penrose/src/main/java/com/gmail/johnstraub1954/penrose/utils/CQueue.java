package com.gmail.johnstraub1954.penrose.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements an unmodifiable, circular queue
 * of type T.
 * The queue always has a current item;
 * there is no concept of a first item or a last item.
 * The client can retrieve the current, next, or previous item.
 * The client can iterate over the list,
 * starting with the current item
 * and moving forward circularly.
 * 
 * @param <T>   the type of item contained in the queue
 */
public class CQueue<T> implements Iterable<T>
{
    /**
     * The list of items in the queue.
     */
    private final   List<T> queue   = new ArrayList<>();
    /**
     * The number of items in the queue.
     */
    private final   int     size;
    /**
     * Index to the current item in the queue.
     */
    private int             currInx = 0;
    
    /**
     * Default constructor; not used.
     */
    public CQueue()
    {
        // not used
        size = 0;
    }

    /**
     * Initializes this instance from a non-empty list.
     * @param list  the list to encapsulate; must be non-empty
     */
    public CQueue( List<T> list )
    {
        if ( list.isEmpty() )
            throw new Malfunction( "Queue may not be empty." );
        queue.addAll( list );
        size = list.size();
    }
    
    /**
     * Gets the current item.
     *  
     * @return  the current item
     */
    public T getCurr()
    {
        T   item    = queue.get( currInx );
        return item;
    }
    
    /**
     * Increments the current item index
     * and returns the new current item.
     *  
     * @return  the current item after incrementing the current item index
     */
    public T getNext()
    {
        currInx = (currInx + 1) % size;
        T   item    = queue.get( currInx );
        return item;
    }
    
    /**
     * Decrements the current item index
     * and returns the new current item.
     *  
     * @return  the current item
     */
    public T getPrevious()
    {
        if ( --currInx < 0 )
            currInx = size - 1;
        T   item    = queue.get( currInx );
        return item;
    }
    
    /**
     * Returns the next item in the queue without removing it.
     * @return  the next item in the queue without removing it
     */
    public T peekNext()
    {
        int nextInx = (currInx + 1) % size;
        T   item    = queue.get( nextInx );
        return item;
    }
    
    /**
     * Returns the previous item in the queue without removing it.
     * @return  the previous item in the queue without removing it
     */
    public T peekPrevious()
    {
        int prevInx = currInx - 1;
        if ( prevInx < 0 )
            prevInx = size - 1;
        T   item    = queue.get( prevInx );
        return item;
    }
    
    /**
     * Gets the next item after the given item.
     * Returns null if the given item isn't in the queue.
     * 
     * @param item  the given item
     * @return  
     *      the item in the queue after the given item,
     *      or null if the given item isn't in the queue
     */
    public T getNext( T item )
    {
        T   next    = null;
        int inx     = queue.indexOf( item );
        if ( inx >= 0 )
        {
            int nextInx = (inx + 1) % size;
            next = queue.get( nextInx );
        }
        return next;
    }
    
    /**
     * Gets the item before the given item.
     * Returns null if the given item isn't in the queue.
     * 
     * @param item  the given item
     * @return
     *      the item in the queue after the given item,
     *      or null if the given item isn't in the queue
     */
    public T getPrevious( T item )
    {
        T   next    = null;
        int inx     = queue.indexOf( item );
        if ( inx >= 0 )
        {
            int nextInx = inx - 1;
            if ( nextInx < 0 )
                nextInx = size - 1;
            next = queue.get( nextInx );
        }
        return next;
    }

    @Override
    public Iterator<T> iterator()
    {
        Iterator<T> iterator    = new ForwardIterator<T>( queue, currInx );
        return iterator;
    }
    
    /**
     * Iterates over a CQueue, 
     * beginning at its current index.
     * @param <T>   the type of the CQueue
     */
    private static class ForwardIterator<T> implements Iterator<T>
    {
        /**
         * The list of items in the queue.
         */
        private final List<T>   list;
        /**
         * The number of items in the queue.
         */
        private final int       size;
        /**
         * The index of the first item in the queue.
         */
        private final int       start;
        /**
         * The index of the next item in the queue.
         */
        private int             currInx;
        /**
         * True, if the queue has be fully traversed.
         */
        private boolean         hasNext;
        
        /**
         * Initializes this iterator.
         * 
         * @param list      the CQueue over which to iterate
         * @param currInx   the index to iterate from
         */
        public ForwardIterator( List<T> list, int currInx )
        {
            this.list = list;
            size = list.size();
            start = currInx;
            this.currInx = currInx;
            hasNext = size > 0;
        }
        
        @Override
        public boolean hasNext()
        {
            return hasNext;
        }
        @Override
        public T next()
        {
            T   item    = list.get( currInx );
            currInx = (currInx + 1) % size;
            hasNext = currInx != start;
            return item;
        }
    }
}
