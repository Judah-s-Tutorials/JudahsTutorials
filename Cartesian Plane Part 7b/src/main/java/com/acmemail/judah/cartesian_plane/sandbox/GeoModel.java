package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * This class simulates a model that includes a state
 * and many properties.
 * It's for support of an example used in the lecture,
 * and its main purpose to to validate the syntax
 * of the code used in the example.
 * 
 * @see ModelMonitor
 */
public class GeoModel
{
    /**
     * Maintains various properties at a point.
     */
    private static class Properties
    {
        /**
         * Default constructor; not used.
         */
        private Properties()
        {
        }
        /** The force at a given point. */
        public double   force         = 0;
        /** The stress at a given point. */
        public double   stress        = 0;
        /** The velocity at a given point. */
        public double   velocity      = 0;
        /** The acceleration at a given point. */
        public double   acceleration  = 0;
    }
    
    /** Map of Point to properties. */
    private Map<Point,Properties>   propertyMap = new HashMap<>();
    /** Indicates whether or not this model is frozen. */
    private boolean                 frozen      = false;
    
    /**
     * Default constructor; not used.
     */
    public GeoModel()
    {
    }
    
    /**
     * Sets the frozen state of this model.
     *  
     * @param state the frozen state of this model
     */
    public void setFrozen( boolean state )
    {
        this.frozen = state;
    }
    
    /**
     * Gets the frozen state of this model.
     *  
     * @return the frozen state of this model
     */
    public boolean isFrozen()
    {
        return frozen;
    }
    
    /**
     * Sets the force at the given point to the given value.
     * 
     * @param point the given point
     * @param value the given value
     */
    public void setForce( Point point, double value )
    {
        Properties  props   = getProperties( point );
        props.force = value;
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
        Properties  props   = getProperties( point );
        double      value   = props.force;
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
        Properties  props   = getProperties( point );
        props.force = value;
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
        Properties  props   = getProperties( point );
        double      value   = props.stress;
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
        Properties  props   = getProperties( point );
        props.stress = value;
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
        Properties  props   = getProperties( point );
        double      value   = props.velocity;
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
        Properties  props   = getProperties( point );
        props.velocity = value;
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
        Properties  props   = getProperties( point );
        double      value   = props.acceleration;
        return value;
    }

    /**
     * Gets the set of properties for a given point.
     * If the given Point is not in the database
     * it will be added,
     * with all its properties set to their defaults.
     * 
     * @param point the given point
     * 
     * @return  the set of properties mapped to the given point
     */
    private Properties getProperties( Point point )
    {
        Properties  props   = propertyMap.get( point );
        if ( props == null )
        {
            props = new Properties();
            propertyMap.put( point, props );
        }
        return props;
    }
}
