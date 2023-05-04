package com.acmemail.judah.cartesian_plane.sandbox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.acmemail.judah.cartesian_plane.input.Exp4jFunctions;

import net.objecthunter.exp4j.function.Function;

public class GetNestedClassDemo
{
    private static final Class<Function>    functionClazz   = Function.class;
    
    public static void main(String[] args)
    {
        Class<Exp4jFunctions>   clazz   = Exp4jFunctions.class;
        Class<?>[]              nested  = clazz.getClasses();
        Arrays.stream( nested ).forEach( GetNestedClassDemo::demo );
    }
    
    private static void demo( Class<?> clazz )
    {
        String  name        = clazz.getName();
        int     mods        = clazz.getModifiers();
        boolean isPublic    = Modifier.isPublic( mods );
        boolean isStatic    = Modifier.isStatic( mods );
        boolean isFunction  = clazz.getSuperclass() == functionClazz;
        System.out.println( name );
        System.out.println( "    public:   " + isPublic );
        System.out.println( "    static:   " + isStatic );
        System.out.println( "    Function: " + isFunction );
        
        try
        {
            double  theta   = Math.PI;
            if ( isPublic && isStatic && isFunction )
            {
                @SuppressWarnings("unchecked")
                Constructor<Function>   ctor    = 
                    (Constructor<Function>)clazz.getConstructor();
                Function    funk        = ctor.newInstance();
                String      funkName    = funk.getName();
                double      value       = funk.apply( theta );
                System.out.printf( "   %s( pi ) = %f%n", funkName, value );
            }
        }
        catch ( 
            NoSuchMethodException
            | InstantiationException
            | IllegalAccessException
            | IllegalArgumentException
            | InvocationTargetException exc
        )
        {
            System.out.println( "*** " + exc.getMessage() );
        }
    }
}
