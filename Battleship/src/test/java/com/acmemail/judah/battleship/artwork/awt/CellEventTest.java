package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.model.GridCoords;

class CellEventTest
{
    @Test
    public void testCellEvent()
    {
        GridCoords  nonNullCoords   = new GridCoords( 10, 10 );
        Component   source          = new JPanel();
        MouseEvent  nonNullEvent    = 
            new MouseEvent( source, 0, 0, 0, 0, 0, 0, false );
        
        CellEvent   event           = 
            new CellEvent( nonNullCoords, nonNullEvent );
        assertEquals( nonNullCoords, event.coords() );
        assertEquals( nonNullEvent, event.mouseEvent() );
        
        assertThrows( NullPointerException.class, () -> 
            new CellEvent( null, nonNullEvent )
        );
        assertThrows( NullPointerException.class, () -> 
            new CellEvent( nonNullCoords, null )
        );
    }

    @Test
    public void testLabel()
    {
        Component          source  = new JPanel();
        MouseEvent  mouseEvent  = 
            new MouseEvent( source, 0, 0, 0, 0, 0, 0, false );
        int         cellXco     = 5;
        int         cellYco     = 10;
        String      expCol      = String.valueOf( cellXco + 1 );
        String      expRow      = String.valueOf( (char)(cellYco + 'A') );
        GridCoords  coords      = new GridCoords( cellXco, cellYco );
        CellEvent   cellEvent   = new CellEvent( coords, mouseEvent );
        Label       label       = cellEvent.label();
        String      actCol      = label.getColStr();
        String      actRow      = label.getRowStr();
        assertEquals( expCol, actCol );        
        assertEquals( expRow, actRow );        
    }
}
