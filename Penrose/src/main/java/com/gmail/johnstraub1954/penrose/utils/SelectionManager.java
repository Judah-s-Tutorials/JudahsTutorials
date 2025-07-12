package com.gmail.johnstraub1954.penrose.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.johnstraub1954.penrose.PShape;

/**
 * This is a simple class
 * to manage lists of selected/unselected PShapes;
 * also manages SelectionEvent propagation
 * (property change logic).
 * The main reason for its creation
 * is to isolate property change logic.
 */
public class SelectionManager implements Serializable
{
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -5494592525711839694L;

    /**
     * List of SelectionListeners.
     */
    private final List<SelectionListener>   
    selectionListeners  = new ArrayList<>();
    /**
     * List of all PShapes.
     */
    private final List<PShape>              
    shapes = new ArrayList<>();
    /**
     * List of currently selected PShapes.
     */
    private final List<PShape>              
    selected = new ArrayList<>();
    /**
     * The current state of the snapping state;
     * NO_MAPPING: source and destination PShapes not selected;
     * CAN_MAP: source and destination PShapes selected,
     * but not correctly configured for snapping;
     * IS_MAPPED: selected PShapes can be snapped.
     */
    private int mapping = SelectionEvent.NO_MAPPING;
    
    /**
     * Default constructor, 
     * contains no special logic.
     */
    public SelectionManager()
    {
    }
    
    /**
     * If not already present,
     * adds the given listener to this list of SelectionListeners.
     * 
     * @param listener  the given listener
     */
    public void addSelectionListener( SelectionListener listener )
    {
        if ( !selectionListeners.contains( listener ) )
            selectionListeners.add( listener );
    }
    
    /**
     * Remove the given listener from the list
     * of SelectionListeners;
     * ignored if not present in the list.
     * 
     * @param listener  the given istener
     */
    public void removeSelectionListener( SelectionListener listener )
    {
        selectionListeners.remove( listener );
    }
    
    /**
     * Adds the given PShape to the list of PShapes.
     * If the PShape is already in the list
     * the operation is ignored.
     * 
     * @param shape the given shape
     */
    public void add( PShape shape )
    {
        if ( !shapes.contains( shape ) )
            shapes.add( shape );
    }
    
    /**
     * Removes the given PShape from the list of PShapes.
     * If selected, the given PShape is deselected.
     * 
     * @param shape the given shape.
     * 
     * @see #deselect(PShape)
     */
    public void remove( PShape shape )
    {
        shapes.remove( shape );
        deselect( shape );
    }
    
    /**
     * Deselects all selected PShapes,
     * propagating a SelectionEvent
     * for each deselected shape.
     * Equivalent to 
     */
    public void deselect()
    {
        clearSelected();
    }
    
    /**
     * If the given shape is not in the selected list it is added;
     * if already in the selected list
     * the next or previous side is selected.
     * If the shape is already selected,
     * the direction parameter determines 
     * whether the next side (direction >= 0)
     * or the previous side (direction &lt; 0)
     * is selected.
     * 
     * A select event is propagated for the given shape.
     * 
     * @param shape     the given shape
     * @param direction determines whether the shape's next side
     *                  or previous side is selected
     */
    public void select( PShape shape, int direction )
    {
        if ( !selected.contains( shape ) )
            selected.add( shape );
        else if ( direction >= 0 )
            shape.nextVertex();
        else
            shape.previousVertex();
        propagateEvent( shape, true );
    }
    
    /**
     * Every shape is removed from the selected list.
     * A deselect event is propagated for each shape.
     */
    public void clearSelected()
    {
        int size    = selected.size();
        while ( size > 0 )
        {
            PShape  shape   = selected.remove( --size );
            propagateEvent( shape, false );
        }
    }
    
    /**
     * If the given shape is in the selected list remove it.
     * A deselect event is generated for the shape, 
     * even if it was not in the selected list.
     * 
     * @param shape the given shape
     */
    public void deselect( PShape shape )
    {
        selected.remove( shape );
        propagateEvent( shape, false );
    }
    
    /**
     * If the given shape is not in the selected list add it.
     * A select event is generated for the shape,
     * even if it was already in the selected list.
     * 
     * @param shape the given shape
     */
    public void select( PShape shape )
    {
        if ( !selected.contains( shape ) )
            selected.add( shape );
        propagateEvent( shape, true );
    }
    
    /**
     * If all shapes are selected, deselects all shapes,
     * otherwise selects all shapes.
     * If one or more shapes (but not all shapes)
     * are already selected, 
     * first the selected shapes are deselected,
     * then all shapes are selected.
     * Deselect and select events are propagated 
     * for all deselected/selected shapes.
     */
    public void toggleSelectAll()
    {
        int     shapesSize      = shapes.size();
        int     selectedSize    = selected.size();
        clearSelected();
        if ( selectedSize < shapesSize )
        {
            shapes.stream()
                .peek( selected::add )
                .forEach( s -> propagateEvent( s, true ) );
        }
    }
    
    /**
     * Returns an unmodifiable list of all PShapes
     * under management.
     * 
     * @return  an unmodifiable list of all PShapes under management
     */
    public List<PShape> getShapes()
    {
        List<PShape>    unmodifiable    = 
            Collections.unmodifiableList( shapes );
        return unmodifiable;
    }
    
    /**
     * Creates a new list of PShapesa
     * and copies to it the contents
     * of a given list.
     * 
     * @param shapes    the given list
     */
    public void setShapes( List<PShape> shapes )
    {
        this.shapes.clear();
        this.shapes.addAll( shapes );
        selected.clear();
    }
    
    /**
     * Returns an unmodifiable list of all 
     * currently selected PShapes.
     * 
     * @return  an unmodifiable list of all PShapes under management
     */
    public List<PShape> getSelected()
    {
        List<PShape>    unmodifiable    = 
            Collections.unmodifiableList( selected );
        return unmodifiable;
    }
    
    /**
     * Checks the current mapping state and,
     * if valid,
     * snaps the source shape 
     * (the first selected shape)
     * to the destination shape
     * (the second selected shape).
     * Returns true if the snap operation
     * was performed
     * 
     * @return true if the snap operation was performed
     */
    public boolean snapTo()
    {
        boolean performed   = false;
        computeMapping();
        if ( mapping == SelectionEvent.IS_MAPPED )
        {
            PShape  fromShape   = selected.get( 0 );
            PShape  toShape     = selected.get( 1 );
            fromShape.snapTo( toShape );
            performed = true;
        }
        return performed;
    }
    
    /**
     * Recompute the mapping state
     * after the properties of a PShape
     * have been updated.
     * Propagate SelectionEvent if it has changed.
     *
     * @param shape the PShape that was modified
     *
     * @return the recomputed mapping state
     */
    public int testMapping( PShape shape )
    {
        int currState   = mapping;
        computeMapping();
        if ( mapping != currState )
            propagateEvent( shape, true );
        return mapping;
    }
    
    /**
     * Propagates a SelectionEvent
     * designating the given PShape as the source
     * of the event.
     * 
     * @param shape     the given PShape
     * @param selected  true, if the given PShape is selected
     */
    private void propagateEvent( PShape shape, boolean selected )
    {
        computeMapping();
        SelectionEvent  event   = 
            new SelectionEvent( shape, selected, mapping );
        for ( SelectionListener listener : selectionListeners )
            listener.select( event );
    }
    
    /**
     * Computes the mapping state,
     * which determines whether a snap operation can be executed.
     * The state is computed as follows:
     * <ul>
     *      <li>
     *          If fewer than two PShapes are currently selected,
     *          the mapping state is set to NO_MAPPING.
     *      </li>
     *      <li>
     *          If at least two PShapes are selected,
     *          but their selection states are not compatible;
     *          for example selected sides
     *          do not have the same slope,
     *          or the dot-state of selected vertices doesn't match,
     *          the mapping state is set to CAN_MAP.
     *      </li>
     *      <li>
     *          If two PShapes are selected
     *          and are property configured for snapping,
     *          the state is set to IS_MAPPED.
     *      </li>
     * </ul>
     * 
     * @see SnapValidator#validate()
     */
    private void computeMapping()
    {
        if ( selected.size() < 2 )
            mapping = SelectionEvent.NO_MAPPING;
        else
        {
            SnapValidator   validator   = new SnapValidator( this );
            if ( validator.validate() )
                mapping = SelectionEvent.IS_MAPPED;
            else
                mapping = SelectionEvent.CAN_MAP;
        }
    }
}
