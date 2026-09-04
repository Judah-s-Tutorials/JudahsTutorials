package com.acmemail.judah.battleship.artwork.awt;

import java.awt.event.MouseEvent;
import java.util.Objects;

import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.model.GridCoords;

/**
 * Encapsulates a mouse-related event
 * occurring in the context of a graphical grid display.
 * The caller provides the unmodified {@code mouseEvent}
 * that triggered this event,
 * and the coordinates of the cell
 * to which the event applies.
 * 
 * @param   mouseEvent
 *          the unmodified {@code mouseEvent} that triggered this event
 * @param   coords
 *          the coordinates of the cell to which the mouse event applies
 */
public record CellEvent( GridCoords coords, MouseEvent mouseEvent )
{
    /**
     * Compact constructor.
     * 
     * @throws NullPointerException if coords is null
     * @throws NullPointerException if mouseEvent is null
     */
    public CellEvent
    {
        Objects.requireNonNull( coords, "coords" );
        Objects.requireNonNull( mouseEvent, "mouseEvent" );
    }
    
    /**
     * A {@code Label} corresponding to the encapsulated
     * grid coordinates.
     * 
     * @return  
     *      {@code Label} corresponding to the encapsulated
     *      grid coordinates
     */
    public Label label()
    {
        return new Label( coords );
    }
}
