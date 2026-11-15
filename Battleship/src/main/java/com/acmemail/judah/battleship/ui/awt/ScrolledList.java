package com.acmemail.judah.battleship.ui.awt;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

/**
 * Encapsulates a JList in a JScrollPane
 * 
 * @param <E>   the type of the JList
 */
public class ScrolledList<E>
{
    /** The encapsulated JList. */
    private final JList<E>      list;
    /** The encapsulated JScrollPane. */
    private final JScrollPane   scrollPane;
    
    public ScrolledList()
    {
        this( Collections.emptyList() );
    }
    
    /**
     * Constructor.
     *  
     * @param items items to add to the encapsulated JList
     */
    public ScrolledList( List<E> items )
    {
        Objects.requireNonNull( items, "items" );
        list = new JList<>( new DefaultListModel<>() );
        items.forEach( this::append );
        scrollPane = new JScrollPane( list );
    }
    
    /**
     * Getter for the encapsulated JList.
     * 
     * @return  the encapsulated JList
     */
    public JList<E> getList()
    {
        return list;
    }
    
    /**
     * Getter for the encapsulated JScrollPane.
     * 
     * @return  the encapsulated JScrollPane
     */
    public JScrollPane getScrolledList()
    {
        return scrollPane;
    }

    /**
     * Convenience method to set the preferred size
     * of the encapsulated JList.
     * 
     * @param size  the preferred size of the JList
     */
    public void setPreferredSize( Dimension size )
    {
        scrollPane.setPreferredSize( size );
    }
    
    /**
     * Convenience method to return a list of items
     * in the encapsulated JList.
     * 
     * @return  a list of items in the encapsulated JList
     */
    public List<E> getItems()
    {
        ListModel<E>   model   = list.getModel();
        int                 count   = model.getSize();
        List<E>        items   =
            IntStream.range( 0, count )
                .mapToObj( model::getElementAt )
                .toList();
        return items;
    }
    
    /**
     * Convenience method to return a list of selected items
     * in the encapsulated JList.
     * 
     * @return  a list of selected items in the encapsulated JList
     */
    public List<E> getSelectedItems()
    {
        List<E>        items   = 
            Collections.unmodifiableList( list.getSelectedValuesList() );
        return items;
    }
    
    /**
     * Convenience method to count the number of selected items
     * in the encapsulated JList.
     * 
     * @return  the number of selected items in the encapsulated JList
     */
    public int getSelectedItemCount()
    {
        int count   = getSelectedItems().size();
        return count;
    }
    
    /**
     * Convenience method to delete the selected items
     * in the encapsulate JList.
     */
    public void deleteSelected()
    {
        ListModel<E>   model   = list.getModel();
        if ( model instanceof DefaultListModel<E> defModel )
        {
            int indices[]   = list.getSelectedIndices();
            int lastIndex   = indices.length - 1;
            IntStream.iterate( lastIndex, i -> i >= 0, i -> i - 1 )
                .forEach( i -> defModel.remove( indices[i] ) );
        }
    }
    
    /**
     * Convenience method to remove an item
     * from the encapsulated JList.
     * 
     * @param   item    the item to remove
     */
    public void removeElement( E item )
    {
        ListModel<E>   model   = list.getModel();
        if ( model instanceof DefaultListModel<E> defModel )
            defModel.removeElement( item );
    }
    
    /**
     * Convenience method to append an item
     * to the encapsulated JList.
     * 
     * @param   item    the item to append
     */
    public void append( E item )
    {
        ListModel<E>   model   = list.getModel();
        if ( model instanceof DefaultListModel<E> defModel )
            defModel.addElement( item );
    }
}
