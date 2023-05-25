package com.acmemail.judah.cartesian_plane.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Implementation of custom functions in JEP.
 * <p>
 * To add a new custom function to this class:
 * <ol>
 * <li>
 *     Add a <em>public static nested class</em>
 *     that extends {@link JEPFunctions#AbstractFunction>.
 * </li>
 * <li>
 *     Add a default constructor
 *     that invokes the super class constructor
 *     passing the function name
 *     and the number of arguments
 *     expected by the custom function.
 * </li>
 * <li>
 *     Override the <em>evaluate<double param></em> method.
 *     Compute and return the desired value.
 * </li>
 * <p>
 * See {@link JEPFunctions2#ToDegrees} for an example.
 * </ol>
 * 
 * @author Jack Straub
 * 
 * @see JEPFunctions2#AbstractFunction
 * @see JEPFunctions2#ToDegrees
 */
public class JEPFunctions2
{
    /** 
     * Class class of the nested <em>AbstractFunction</em> class.
     * Declared here for convenience.
     */
    private static final Class<AbstractFunction>    funkClazz   = 
        AbstractFunction.class;
    /** List of all detected custom functions. */
    private static final List<AbstractFunction>     funkList    = 
        new ArrayList<>();
    
    static
    {
        // Search every nested class for custom function implementations.
        Class<JEPFunctions2>     clazz   = JEPFunctions2.class;
        Class<?>[]              nested  = clazz.getClasses();
        Arrays.stream( nested )
            .forEach( JEPFunctions2::verifyAndAdd );
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
    public static List<AbstractFunction> getFunctions()
    {
        List<AbstractFunction>  list    = 
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
    
    /**
     * Abstract superclass of all classes
     * encapsulating custom functions.
     * 
     * @author Jack Straub
     */
    public static abstract class AbstractFunction 
        extends PostfixMathCommand
    {
        public abstract double evaluate( double param );
        private final String    name;
        
        public AbstractFunction( String name, int numParams )
        {
            this.name = name;
            numberOfParameters = numParams;
        }
        
        public String getName()
        {
            return name;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run( Stack inStack )
            throws ParseException 
        {
            checkStack( inStack );
            Object  param   = inStack.pop();
            if ( !(param instanceof Double) )
            {
                String  type    = param.getClass().getName();
                String  msg     = "Invalid parameter type: " + type;
                throw new ParseException( msg );
            }
            double  result  = evaluate( (double)param );
            inStack.push( result );
        }
    }
    
    public static class ToDegrees extends AbstractFunction
    {
        public ToDegrees()
        {
            super( "toDegrees", 1 );
        }
        
        @Override
        public double evaluate( double param )
        {
            double  degrees = param * 180 / Math.PI;
            return degrees;
        }
    }
    
    public static class ToRadians extends AbstractFunction
    {
        public ToRadians()
        {
            super( "toRadians", 1 );
            
        }
        
        @Override
        public double evaluate( double param )
        {
            double  radians = param * Math.PI / 180.;
            return radians;
        }
    }
    
    public static class Secant extends AbstractFunction
    {
        public Secant()
        {
            super( "sec", 1 );
            
        }
        
        @Override
        public double evaluate( double param )
        {
            double  secant = 1.0 / Math.cos( param );
            return secant;
        }
    }
    
    public static class Cosecant extends AbstractFunction
    {
        public Cosecant()
        {
            super( "csc", 1 );
            
        }
        
        @Override
        public double evaluate( double param )
        {
            double  cosecant = 1.0 / Math.sin( param );
            return cosecant;
        }
    }
    
    public static class Cotangent extends AbstractFunction
    {
        public Cotangent()
        {
            super( "cot", 1 );
            
        }
        
        @Override
        public double evaluate( double param )
        {
            double  cotan = 1.0 / Math.tan( param );
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
                Constructor<AbstractFunction>   ctor    = 
                    (Constructor<AbstractFunction>)clazz.getConstructor();
                AbstractFunction    funk    = ctor.newInstance();
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
