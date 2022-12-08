package com.acmemail.judah.sanbox;

import java.awt.Color;

public class SmartRect
{
    private static final Color  DEFAULT_COLOR   = Color.BLUE;
    private static final float  DEFAULT_WIDTH   = 127.3f;
    private static final float  DEFAULT_HEIGHT  = 99.6f;
    
    private final Color color;
    private final float width;
    private final float height;
    
    public SmartRect()
    {
        this( DEFAULT_COLOR, DEFAULT_WIDTH, DEFAULT_HEIGHT );
    }
    
    public SmartRect( Color color )
    {
        this( color, DEFAULT_WIDTH, DEFAULT_HEIGHT );
    }
    
    public SmartRect( float width, float height )
    {
        this( DEFAULT_COLOR, width, height );
    }
    
    public SmartRect( Color color, float width, float height )
    {
        this.color = color;
        this.width = width;
        this.height = height;
    }
    // ...
    public float getArea()
    {
        float   area    = width * height;
        return area;
    }
    
    public float getPerimeter()
    {
        float   perimeter   = 2 * width + 2 * height;
        return perimeter;
    }
}
