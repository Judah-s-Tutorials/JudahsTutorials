package com.acmemail.judah.battleship.artwork;

import com.acmemail.judah.battleship2D.Grid2D;

public interface Artwork
{
    void update( Grid2D grid );
    default void update()
    {
        update( null );
    }
}
