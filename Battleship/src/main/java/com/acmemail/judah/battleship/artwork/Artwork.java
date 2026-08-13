package com.acmemail.judah.battleship.artwork;

import com.acmemail.judah.battleship2D.Grid;

public interface Artwork
{
    void update( Grid grid );
    default void update()
    {
        update( null );
    }
}
