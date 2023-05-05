package com.acmemail.judah.cartesian_plane.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class JEPFunctions
{
    private static final Class<AbstractFunction>    funkClazz   = 
        AbstractFunction.class;
    private static List<AbstractFunction>           funkList    = 
        new ArrayList<>();
    
    static
    {
        Class<JEPFunctions>     clazz   = JEPFunctions.class;
        Class<?>[]              nested  = clazz.getClasses();
        Arrays.stream( nested ).forEach( JEPFunctions::verifyAndAdd );
    }
    
    public static List<AbstractFunction> getFunctions()
    {
        return funkList;
    }
    
    public static void addFunctions( JEP parser )
    {   
        funkList.forEach( f -> 
            parser.addFunction( f.getName(), f ) 
        );
    }
    
    private static abstract class AbstractFunction extends PostfixMathCommand
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
        boolean isFunction  = clazz.getSuperclass() == funkClazz;
        
        if ( isPublic && isStatic && isFunction )
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
                System.out.println( "*** " + exc.getMessage() );
            }
        }
    }
}
