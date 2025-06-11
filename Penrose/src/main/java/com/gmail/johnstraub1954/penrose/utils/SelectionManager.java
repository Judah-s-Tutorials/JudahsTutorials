package com.gmail.johnstraub1954.penrose.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gmail.johnstraub1954.penrose.PShape;

/**
 * This is a simple class
 * to manage lists of selected/unselected PShapes.
 * The main reason for its creation
 * is to isolate property change logic.
 */
public class SelectionManager implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -5494592525711839694L;

    private static final double     epsilon     = .001;
    
    private final List<SelectionListener>   selectionListeners  = new ArrayList<>();
    private final List<PShape>              shapes              = new ArrayList<>();
    private final List<PShape>              selected            = new ArrayList<>();
    private int                             mapping             = SelectionEvent.NO_MAPPING;
    
    public void addSelectionListener( SelectionListener listener )
    {
        if ( !selectionListeners.contains( listener ) )
            selectionListeners.add( listener );
    }
    
    public void removeSelectionListener( SelectionListener listener )
    {
        selectionListeners.remove( listener );
    }
    
    public void add( PShape shape )
    {
        if ( !shapes.contains( shape ) )
            shapes.add( shape );
    }
    
    public void remove( PShape shape )
    {
        shapes.remove( shape );
        selected.remove( shape );
    }
    
    public void deselectAll()
    {
        clearSelected();
    }
    
    public void deselect()
    {
        selected.clear();
    }
    
    /**
     * If the given shape is not in the selected list it is added;
     * if already in the selected list
     * the next or previous side is selected.
     * If the shape is already selected,
     * the direction parameter determines 
     * whether the next side (direction >= 0)
     * or the previous side (direction < 0)
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
    
    public List<PShape> getShapes()
    {
        return shapes;
    }
    
    public void setShapes( List<PShape> shapes )
    {
        this.shapes.clear();
        this.shapes.addAll( shapes );
        selected.clear();
    }
    
    public List<PShape> getSelected()
    {
        return selected;
    }
    
    public int getMapping()
    {
        computeMapping();
        return mapping;
    }
    
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
     * Recompute the mapping state.
     * Propagate SelectionEvent if it has changed.
     * 
     * @return the recomputed mapping state
     */
    public int testMapping()
    {
        int currState   = mapping;
        computeMapping();
        if ( mapping != currState )
            propagateEvent( null, true );
        return mapping;
    }
    
    private void propagateEvent( PShape shape, boolean selected )
    {
        computeMapping();
        SelectionEvent  event   = new SelectionEvent( shape, selected, mapping );
        for ( SelectionListener listener : selectionListeners )
            listener.select( event );
    }
    
    private void computeMapping()
    {
        if ( selected.size() < 2 )
            mapping = SelectionEvent.NO_MAPPING;
        else
        {
            PShape      fromShape   = selected.get( 0 );
            PShape      toShape     = selected.get( 1 );
            double      fromSlope   = fromShape.getCurrSlope();
            double      toSlope     = toShape.getCurrSlope();
            double      fromLength  = fromShape.getCurrLength();
            double      toLength    = toShape.getCurrLength();
            boolean[]   fromDotted  = fromShape.getCurrDotState();
            boolean[]   toDotted    = toShape.getCurrDotState();
            if ( !testDoubles( fromSlope, toSlope ) )
                mapping = SelectionEvent.CAN_MAP;
            else if ( !testDoubles( fromLength, toLength ) )
                mapping = SelectionEvent.CAN_MAP;
            else if ( fromDotted[0] != toDotted[1] )
                mapping = SelectionEvent.CAN_MAP;
            else if ( fromDotted[1] != toDotted[0] )
                mapping = SelectionEvent.CAN_MAP;
            else
                mapping = SelectionEvent.IS_MAPPED;
        }
    }
    
    private static boolean testDoubles( double dVal1, double dVal2 )
    {
        double  diff    = dVal1 - dVal2;
        boolean match   = Math.abs( diff ) < epsilon;
        return match;
    }
}
