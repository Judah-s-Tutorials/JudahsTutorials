package com.acmemail.judah.cartesian_plane.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.nfunk.jep.JEP;

/**
 * Implementation of custom functions in JEP.
 * <p>
 * To add a new custom function to this class:
 * <ol>
 * <li>
 *     Add a <em>public static nested class</em>
 *     that extends {@link JEPAbstractFunction}.
 * </li>
 * <li>
 *     Add a default constructor
 *     that invokes the super class constructor
 *     passing the function name
 *     and the number of arguments
 *     expected by the custom function.
 * </li>
 * <li>
 *     Override the <em>evaluate( double params )</em> method.
 *     Compute and return the desired value.
 * </li>
 * </ol>
 * <p>
 * See {@link JEPFunctions.ToDegrees} for an example.
 * 
 * @author Jack Straub
 * 
 * @see JEPAbstractFunction
 * @see JEPFunctions.ToDegrees
 */
public class JEPFunctions
{
    /** 
     * Class class of the nested <em>AbstractFunction</em> class.
     * Declared here for convenience.
     */
    private static final Class<JEPAbstractFunction>    funkClazz   = 
        JEPAbstractFunction.class;
    /** List of all detected custom functions. */
    private static final List<JEPAbstractFunction>     funkList    = 
        new ArrayList<>();
    
    static
    {
        // Search every nested class for custom function implementations.
        Class<JEPFunctions>     clazz   = JEPFunctions.class;
        Class<?>[]              nested  = clazz.getClasses();
        Arrays.stream( nested )
            .forEach( JEPFunctions::verifyAndAdd );
    }
    
    /**
     * Gets an unmodifiable list
     * of all the custom functions
     * encapsulated in this class.
     * 
     * @return  
     *      an unmodifiable list of all the custom functions
     *      encapsulated in this class
     */
    public static List<JEPAbstractFunction> getFunctions()
    {
        List<JEPAbstractFunction>  list    = 
            Collections.unmodifiableList( funkList );
        return list;
    }
    
    /**
     * Add all encapsulated custom functions
     * to a given JEP object.
     * 
     * @param parser    the given JEP object
     */
    public static void addFunctions( JEP parser )
    {   
        funkList.forEach( f -> 
            parser.addFunction( f.getName(), f ) 
        );
    }
    
    public static class ToDegrees extends JEPAbstractFunction
    {
        public ToDegrees()
        {
            super( "toDegrees", 1 );
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  degrees = params[0] * 180 / Math.PI;
            return degrees;
        }
    }
    
    public static class ToRadians extends JEPAbstractFunction
    {
        public ToRadians()
        {
            super( "toRadians", 1 );
            
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  radians = params[0] * Math.PI / 180.;
            return radians;
        }
    }
    
    public static class Secant extends JEPAbstractFunction
    {
        public Secant()
        {
            super( "sec", 1 );
            
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  secant = 1.0 / Math.cos( params[0] );
            return secant;
        }
    }
    
    public static class Cosecant extends JEPAbstractFunction
    {
        public Cosecant()
        {
            super( "csc", 1 );
            
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  cosecant = 1.0 / Math.sin( params[0] );
            return cosecant;
        }
    }
    
    public static class Cotangent extends JEPAbstractFunction
    {
        public Cotangent()
        {
            super( "cot", 1 );
            
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  cotan = 1.0 / Math.tan( params[0] );
            return cotan;
        }
    }
    
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
                Constructor<JEPAbstractFunction>   ctor    = 
                    (Constructor<JEPAbstractFunction>)clazz.getConstructor();
                JEPAbstractFunction    funk    = ctor.newInstance();
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
                System.err.println( message );
                exc.printStackTrace();
            }
        }
    }
}
