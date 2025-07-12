package com.gmail.johnstraub1954.penrose.utils;

import com.gmail.johnstraub1954.penrose.PShape;

/**
 * An object of this class
 * describes the selection or deselection
 * of a PShape by the {@linkplain SelectionManager}.
 */
public class SelectionEvent
{
    /**
     * Source and destination PSHapes selected for snapping,
     * but require configuration before snapping can occur.
     */
    public static final int     CAN_MAP     = 0;
    /**
     * Source and destination PShapes ready to be snapped.
     */
    public static final int     IS_MAPPED   = 1;
    /**
     * Source and destination PShapes not selected for snapping.
     */
    public static final int     NO_MAPPING  = 2;
    
    /** Shape that was selected or deselected. */
    private final PShape    shape;
    /** True when shape is selected, false when deselected. */
    private final boolean   selected;
    /** Mapping state: can't be mapped, can be mapped, is mapped */
    private final int       mapState;
    
    /**
     * An object of this type
     * describes the state of PShape selection.
     * The state includes:
     * <ul>
     * <li>
     * The PShape associated with the event.
     * An even is generated when a PShape is selected,
     * deselected, or when its selected edge changes.
     *
     * </li>
     * <li>
     * The selected state, true (selected) or false (deselected)
     * </li>
     * <li>
     * The state of possible mapping.
     * A mapping occurs if two shapes are selected
     * and the first can be mapped to the second.
     * Possibilities are:
     * <ol>
     * <li>
     *      CAN_MAP: 
     *      two shapes are selected
     *      but their selected edges can't be mapped.
     * </li>
     * <li>
     *      IS_MAPPED: 
     *      two shapes are selected
     *      and their selected edges can be mapped.
     * </li>
     * <li>
     *      NO_MAPPING: 
     *      fewer than two shapes are selected
     *      or the selected shapes have no candidate edges.
     * </li>
     * </ol>
     * </li>
     * </ul>
     * 
     * @param shape     shape that was selected or deselected
     * @param selected  true if the shape is selected
     * @param mapState  describes how the first two selected shapes
     *                  can or cannot be mapped
     */
    public SelectionEvent(PShape shape, boolean selected, int mapState)
    {
        super();
        this.shape = shape;
        this.selected = selected;
        this.mapState = mapState;
    }

    /**
     * Gets the shape associated with the event.
     * 
     * @return the shape associated with the event
     */
    public PShape getShape()
    {
        return shape;
    }

    /**
     * True if the source shape is selected.
     * 
     * @return true if the source shape is selected
     */
    public boolean isSelected()
    {
        return selected;
    }

    /**
     * Gets the mapping state of the first two selected shapes:
     * NO_MAPPING: no mapping is possible;
     * CAN_MAP:    the first two selected shapes can be mapped,
     *             but are not mapped
     *             because compatible edges are not selected;
     * IS_MAPPED:  the selected edges of the first two selected shapes 
     *             can be mapped.
     * @return the mapping state of the first two selected shapes
     */
    public int getMapState()
    {
        return mapState;
    }
}
