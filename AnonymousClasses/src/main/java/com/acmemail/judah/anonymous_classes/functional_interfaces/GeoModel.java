package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.awt.Point;

public class GeoModel
{
    private boolean frozen      = false;
    
    public void setFrozen( boolean frozen )
    {
        this.frozen = frozen;
    }
    
    public boolean isFrozen()
    {
        return frozen;
    }
    
    public double force( Point point )
    {
        return 0;
    }
    
    public double stress( Point point )
    {
        return 0;
    }
    
    public double velocity( Point point )
    {
        return 0;
    }
    
    public double acceleration( Point point )
    {
        return 0;
    }
    
    public double force()
    {
        return 0;
    }
    
    public double velocity()
    {
        return 0;
    }
}
