package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Point;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This class simulates a test utility for a model with many properties.
 * It's for support of an example used in the lecture,
 * and its main purpose to to validate the syntax
 * of the code used in the example.
 * 
 * @see GeoModel
 */
public class ModelMonitor
{
    /** GeoModel instance under test. */
    private GeoModel    model   = new GeoModel();
    
    /**
     * Default constructor; not used.
     */
    private ModelMonitor()
    {
    }
    
    /**
     * Sets the force at the given point to the given value.
     * 
     * @param point the given point
     * @param value the given value
     */
    public void setForce( Point point, double value )
    {
        setDouble( point, value, model::setForce );
    }
    
    /**
     * Gets the value of the force at the given point.
     * If the given point is not in the database,
     * 0 is returned.
     * 
     * @param point the given point
     * 
     * @return 
     *      the value of the force at the given point;
     *      0 if the given point is not present in the database
     */
    public double getForce( Point point )
    {
        double      value   = getDouble( point, model::getForce );
        return value;
    }
    
    /**
     * Sets the stress at the given point to the given value.
     * 
     * @param point the given point
     * @param value the given value
     */
    public void setStress( Point point, double value )
    {
        setDouble( point, value, model::setStress );
    }
    
    /**
     * Gets the value of the stress at the given point.
     * If the given point is not in the database,
     * 0 is returned.
     * 
     * @param point the given point
     * 
     * @return 
     *      the value of the stress at the given point;
     *      0 if the given point is not present in the database
     */
    public double getStress( Point point )
    {
        double      value   = getDouble( point, model::getStress );
        return value;
    }
    
    /**
     * Sets the velocity at the given point to the given value.
     * 
     * @param point the given point
     * @param value the given value
     */
    public void setVelocity( Point point, double value )
    {
        setDouble( point, value, model::setVelocity );
    }
    
    /**
     * Gets the value of the velocity at the given point.
     * If the given point is not in the database,
     * 0 is returned.
     * 
     * @param point the given point
     * 
     * @return 
     *      the value of the velocity at the given point;
     *      0 if the given point is not present in the database
     */
    public double getVelocity( Point point )
    {
        double      value   = getDouble( point, model::getVelocity );
        return value;
    }
    
    /**
     * Sets the acceleration at the given point to the given value.
     * 
     * @param point the given point
     * @param value the given value
     */
    public void setAcceleration( Point point, double value )
    {
        setDouble( point, value, model::setAcceleration );
    }
    
    /**
     * Gets the value of the acceleration at the given point.
     * If the given point is not in the database,
     * 0 is returned.
     * 
     * @param point the given point
     * 
     * @return 
     *      the value of the acceleration at the given point;
     *      0 if the given point is not present in the database
     */
    public double getAcceleration( Point point )
    {
        double      value   = getDouble( point, model::getAcceleration );
        return value;
    }

    /**
     * Gets the value obtained by the given getter
     * at the given point,
     * ensuring that all rules regarding
     * freezing the model under test are followed.
     * 
     * @param point the given point
     * @param getter the given getter
     * 
     * @return  the value obtained by the given getter
     */
    private double 
    getDouble( Point point, Function<Point,Double> getter )
    {
        double  rval        = 0;
        boolean isFrozen    = model.isFrozen();
        if ( !isFrozen )
        {
            model.setFrozen( true );
        }
        rval = getter.apply( point );
        if ( !isFrozen )
            model.setFrozen( false );
        return rval;
    }

    /**
     * Sets the property at the given point,
     * to the given value,
     * via the given setter.
     * Ensures that all rules regarding
     * freezing the model under test are followed.
     * 
     * @param point the given point
     * @param value the given value
     * @param setter the given setter
     */
    private void 
    setDouble( Point point, double value, BiConsumer<Point,Double> setter )
    {
        boolean isFrozen    = model.isFrozen();
        if ( !isFrozen )
        {
            model.setFrozen( true );
        }
        setter.accept( point, value );
        if ( !isFrozen )
            model.setFrozen( false );
    }
}
