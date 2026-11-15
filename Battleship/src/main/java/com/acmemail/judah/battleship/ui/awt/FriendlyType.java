package com.acmemail.judah.battleship.ui.awt;

import com.acmemail.judah.battleship.model.ShipType2D;

/**
 * Encapsulate a given ShipType2D
 * for the purpose of overriding toString with a method
 * that produces operator-friendly output.
 * 
 * @param type  the given ShipType2D
 */
public record FriendlyType( ShipType2D type )
{
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
            bldr.append( type.typeName() ).append( " " )
                .append( type.length() ).append( "X" )
                .append( type.breadth() );
        return bldr.toString();
    }
}