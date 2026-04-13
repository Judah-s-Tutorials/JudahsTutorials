package com.acmemail.judah.cartesian_plane.sandbox.stream_supplier;

import java.awt.Color;

public class ColorCommand implements Command
{
    private final   Color   color;
    
    public ColorCommand( Color color )
    {
        this.color = color;
    }
    
    @Override
    public String toString()
    {
        String  str = "Color command: " + color;
        return str;
    }
}
