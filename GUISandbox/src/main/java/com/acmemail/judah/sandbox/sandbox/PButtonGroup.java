package com.acmemail.judah.sandbox.sandbox;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * Subclass of javax.swing.ButtonGroup
 * intended just for objects
 * of type PRadioButton<T> 
 * (a subclass of <em>JRadioButton</em>).
 * It adds the ability
 * to identify the selected button
 * within the group (if any),
 * to return
 * the property encapsulated
 * by the selected button,
 * and to select the button
 * that encapsulates
 * a given property value.
 * 
 * @author Jack Straub
 * 
 * @param <T>   the type of property encapsulated
 *              by members of this group
 *              
 * @See PRadioButton
 */
@SuppressWarnings("serial")
public class PButtonGroup<T> extends ButtonGroup
{
    private final List<PRadioButton<T>> buttonList  = new ArrayList<>();
    
    /**
     * Adds the given button to this group.
     * 
     * @param button    the given button
     */
    public void add( PRadioButton<T> button )
    {
        buttonList.add( button );
        super.add( button );
    }
    
    /**
     * Gets the currently selected button
     * in this group. 
     * Returns null if none.
     * 
     * @return
     *      the currently selected button in this group
     *      or null if none
     */
    public PRadioButton<T> getSelectedButton()
    {
        PRadioButton<T> button  = null;
        buttonList.stream()
            .filter( b -> b.isSelected() )
            .findFirst()
            .orElse( null );
        return button;
    }
    
    /**
     * Gets the value
     * of the property
     * encapsulated by
     * the currently selected button
     * in this group.
     * Returns null
     * if no button is selected.
     * 
     * @return
     *      the value encapsulated by 
     *      the currently selected button in this group;
     *      null if no button is selected
     */
    public T getSelectedProperty()
    {
        PRadioButton<T> selected    = getSelectedButton();
        T               property    = null;
        if ( selected != null )
            property = selected.get();
        return property;
    }
    
    /**
     * Selects the button
     * in this group
     * with the given property value.
     * 
     * @param value the given property value
     * 
     * @return  
     *      true if a button with the given value
     *      was found and selected
     */
    public boolean selectButton( T value )
    {
        final String    mayNotBeNull    = "value may not be null";
        if ( value == null )
            throw new IllegalArgumentException( mayNotBeNull );
        JRadioButton    toSelect    = buttonList.stream()
            .filter( p -> value.equals( p ) )
            .findFirst()
            .orElse( null );
        
        boolean rval    = false;
        if ( toSelect != null )
        {
            toSelect.setSelected( true );
            rval = true;
        }
        
        return rval;
    }
}
