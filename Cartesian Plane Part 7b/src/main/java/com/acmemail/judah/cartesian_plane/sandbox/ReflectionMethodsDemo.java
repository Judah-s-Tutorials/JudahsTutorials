package com.acmemail.judah.cartesian_plane.sandbox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This application demonstrates
 * how to find and use
 * methods from a class.
 * It goes a bit beyond the requirements
 * for this lesson, 
 * but it may be of interest to the student.
 * 
 * @author Jack Straub
 */
public class ReflectionMethodsDemo
{
    private static final Class<ReflectionDemoObject>    clazz   = 
        ReflectionDemoObject.class;
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        ReflectionDemoObject        obj     = 
            new ReflectionDemoObject( 100, "Peter", 500f );
        for ( Field field : clazz.getDeclaredFields() ) 
        {
            String  fName   = field.getName();
            Method  method  = getMethod( fName );
            if ( method != null )
            {
                try
                {
                    Object  val = method.invoke( obj );
                    System.out.println( fName + ": " + val );
                }
                catch ( 
                    IllegalAccessException    |
                    IllegalArgumentException  |
                    InvocationTargetException exc
                )
                {
                    String  msg = "Failed to invoke getter for " + fName;
                    System.err.println( msg );
                    
                }
            }
        }
    }
    
    /**
     * Given a field name
     * find the associated getter.
     * Assumptions:
     * <ol>
     * <li>The field name consists of at least two character.</li>
     * <li>
     *     The tail of the associated method name
     *     consists of the field name
     *     with the first letter capitalized.
     * </li>
     * <li>The head of the method name is "get".</li>
     * <li>The method is public.</li>
     * <li>The method has no parameters.
     * </ol>
     * <p>
     * If the method can't be found
     * null is returned.
     * </p>
     * 
     * @param fName the given field name
     * 
     * @return  the getter for fName
     */
    private static Method getMethod( String fName )
    {
        Method  method      = null;
        char    firstChar   = Character.toUpperCase( fName.charAt( 0 ) );
        String  remainder   = fName.substring( 1 );
        String  methodName  = "get" + firstChar + remainder;
        try
        {
            // The cast (Class<?>[]) is present to eliminate a compiler 
            // warning: "Type null of the last argument to method 
            // getMethod(String, Class<?>...) doesn't exactly match the 
            // vararg parameter type. Cast to Class<?>[] to confirm the
            // non-varargs invocation..."
            method = clazz.getMethod( methodName, (Class<?>[])null );
        }
        catch ( NoSuchMethodException exc )
        {
            String  msg = "Mehod name \"" + methodName + "\" not found";
            System.err.println( msg );
        }
        
        return method;
    }
}
