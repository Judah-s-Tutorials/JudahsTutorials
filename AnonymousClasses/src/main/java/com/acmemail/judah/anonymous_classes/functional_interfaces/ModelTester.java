package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.awt.Point;
import java.util.function.Function;

public class ModelTester
{
    private GeoModel    model;
    
    public double getStressOrig( Point point )
    {
        boolean isFrozen    = model.isFrozen();
        if ( !isFrozen )
        {
            model.setFrozen( true );
        }
        double  stress  = model.stress( point );
        if ( !isFrozen )
            model.setFrozen( false );
        return stress;
    }
    
    public double getStress( Point point )
    {
        double  stress  = 
            getDouble( point, p -> model.stress( p ) );
        return stress;
    }
    
    public double getForce( Point point )
    {
        double  force   = getDouble( point, p -> model.stress( p ) );
        return force;
    }
    
    private double 
    getDouble( Point point, Function<Point,Double> getter )
    {
        double  rval    = 0;
        boolean isFrozen    = model.isFrozen();
        if ( !isFrozen )
        {
            model.setFrozen( true );
        }
        rval = getter.apply( point );
        model.setFrozen( isFrozen );
        return rval;
    }
}
