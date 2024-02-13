package com.acmemail.judah.cartesian_plane.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.objecthunter.exp4j.function.Function;

/**
 * Implementation of common custom function
 * for the Exp4j implementation.
 * 
 * @author Jack Straub
 */
public class Exp4jFunctions
{
    private static final Class<Function>    funkClazz   = Function.class;
    private static final List<Function>     funkList    = new ArrayList<>();
    
    static
    {
        Class<Exp4jFunctions>   clazz   = Exp4jFunctions.class;
        Class<?>[]              nested  = clazz.getClasses();
        Arrays.stream( nested ).forEach( Exp4jFunctions::verifyAndAdd );
    }
    
    /**
     * Gets a list of all custom functions.
     * 
     * @return  a list of all custom functions
     */
    public static List<Function> getFunctions()
    {
        List<Function>  list    = Collections.unmodifiableList( funkList );
        return list;
    }

    /**
     * Exp4j custom function to convert radians to degrees.
     * 
     * @author Jack Straub
     */
    public static class ToDegrees extends Function
    {
        /**
         * Constructor.
         * Establishes the name of the custom function,
         * and the number of required arguments.
         */
        public ToDegrees()
        {
            super( "toDegrees", 1 );
        }
        
        @Override
        public double apply( double... args )
        {
            double  degrees = args[0] * 180 / Math.PI;
            return degrees;
        }        
    }

    /**
     * Exp4j custom function to convert degrees to radians.
     * 
     * @author Jack Straub
     */
    public static class ToRadians extends Function
    {
        /**
         * Constructor.
         * Establishes the name of the custom function,
         * and the number of required arguments.
         */
        public ToRadians()
        {
            super( "toRadians", 1 );
        }
        
        @Override
        public double apply( double... args )
        {
            double  radians = args[0] * Math.PI / 180.;
            return radians;
        }        
    }

    /**
     * Exp4j custom function to calculate the
     * secant of a triangle.
     * 
     * @author Jack Straub
     */
    public static class Secant extends Function
    {
        /**
         * Constructor.
         * Establishes the name of the custom function,
         * and the number of required arguments.
         */
        public Secant()
        {
            super( "sec", 1 );
        }
        
        @Override
        public double apply( double... args )
        {
            double  secant = 1.0 / Math.cos( args[0] );
            return secant;
        }        
    }

    /**
     * Exp4j custom function to calculate the
     * cotangent of a triangle.
     * 
     * @author Jack Straub
     */
    public static class Cotangent extends Function
    {
        /**
         * Constructor.
         * Establishes the name of the custom function,
         * and the number of required arguments.
         */
        public Cotangent()
        {
            super( "cot", 1 );
        }
        
        @Override
        public double apply( double... args )
        {
            double  cotan = 1.0 / Math.tan( args[0] );
            return cotan;
        }        
    }

    /**
     * Exp4j custom function to calculate the
     * cosecant of a triangle.
     * 
     * @author Jack Straub
     */
    public static class Cosecant extends Function
    {
        /**
         * Constructor.
         * Establishes the name of the custom function,
         * and the number of required arguments.
         */
        public Cosecant()
        {
            super( "csc", 1 );
        }
        
        @Override
        public double apply( double... args )
        {
            double  cosecant = 1.0 / Math.sin( args[0] );
            return cosecant;
        }        
    }
    
    /**
     * Verifies that a given class
     * encapsulates an Exp4j custom function
     * and, if verified,
     * instantiates the class
     * and adds the instantiated object
     * to the list of functions
     * encapsulated  by this module.
     * 
     * @param clazz the given class
     */
    private static void verifyAndAdd( Class<?> clazz )
    {
        int     mods        = clazz.getModifiers();
        boolean isPublic    = Modifier.isPublic( mods );
        boolean isStatic    = Modifier.isStatic( mods );
        boolean isAbstract  = Modifier.isAbstract( mods );
        boolean isFunction  = funkClazz.isAssignableFrom( clazz );
        
        if ( isPublic && isStatic && isFunction && !isAbstract )
        {
            try
            {
                @SuppressWarnings("unchecked")
                Constructor<Function>   ctor    = 
                    (Constructor<Function>)clazz.getConstructor();
                Function    funk        = ctor.newInstance();
                funkList.add( funk );
            }
            catch ( 
                NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException exc
            )
            {
                String  className   = exc.getClass().getName();
                String  message     = 
                    "Unexpected error: " 
                        + className + ", " 
                        + exc.getMessage();
                System.out.println( message );
            }
        }
    }
}
