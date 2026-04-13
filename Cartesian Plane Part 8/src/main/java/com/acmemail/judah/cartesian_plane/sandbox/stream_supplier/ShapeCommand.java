package com.acmemail.judah.cartesian_plane.sandbox.stream_supplier;

public class ShapeCommand implements Command
{
    private final   String  shapeName;
    
    public ShapeCommand( String shapeName )
    {
        this.shapeName = shapeName;
    }
    
    @Override
    public String toString()
    {
        String  str = "Shape command: " + shapeName;
        return str;
    }
}
