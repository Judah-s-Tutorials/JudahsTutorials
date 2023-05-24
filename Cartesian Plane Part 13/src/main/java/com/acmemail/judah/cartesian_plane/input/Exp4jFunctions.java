package com.acmemail.judah.cartesian_plane.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.objecthunter.exp4j.function.Function;

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
    
    public static List<Function> getFunctions()
    {
        return funkList;
    }
    
    public static class ToDegrees extends Function
    {
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

    public static class ToRadians extends Function
    {
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

    public static class Secant extends Function
    {
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

    public class Cotangent extends Function
    {
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

    public class Cosecant extends Function
    {
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
