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
    
    public void deselect()
    {
        selected.clear();
    }
    
    /**
     * The given shape is added to the selected list.
     * 
     * @param shape the given shape
     */
    public void select( PShape shape )
    {
        if ( !selected.contains( shape ) )
            selected.add( shape );
        else
            shape.nextVertex();
        propagateEvent( shape, true );
    }
    
    /**
     * Removes the given shape from the selected list.
     * 
     * @param shape the given shape
     */
    public void deselect( PShape shape )
    {
        if ( !selected.contains( shape ) )
            selected.add( shape );
        else
            shape.nextVertex();
        propagateEvent( shape, false );
    }
    
    public List<PShape> getShapes()
    {
        return shapes;
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
