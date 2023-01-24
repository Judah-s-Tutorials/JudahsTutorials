package com.acmemail.judah.cartesian_plane.sandbox;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This application demonstrates how to locate
 * and interrogate all the public fields
 * in a class.
 * 
 * @author Jack Straub
 */
public class ReflectionPublicFieldsDemo
{
    private static final String header1    = "Name       public class final type value";
    private static final String header2    = "========================================";
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        Class<ReflectionDemoObject> clazz   = ReflectionDemoObject.class;
        Field[]                     fields  = clazz.getFields();
        System.out.println( header1 );
        System.out.println( header2 );
        
        // report only static fields
        for ( Field field : fields )
            if ( Modifier.isStatic( field.getModifiers() ) )
                printField( field, null );
        
        // report only non-static fields
        Object  obj = new ReflectionDemoObject( 100, "Petr?", 500f );
        for ( Field field : fields )
            if ( !Modifier.isStatic( field.getModifiers() ) )
                printField( field, obj );
    }
    
    /**
     * Print some of the properties of a field.
     * The second argument is an instance of the class
     * containing the field; 
     * this is required to interrogate the value
     * of an instance variable.
     * If the field is a class variable
     * this argument may be null.
     * 
     * @param field     the field to interrogate
     * @param instance  an instance of the class containing the field;
     *                  may be null for class fields
     */
    private static void printField( Field field, Object instance )
    {
        final String fmt = "%-10s %-6s %-5s %-5s %-6s %s%n";
        int     mods        = field.getModifiers();
        String  name        = field.getName();
        boolean isPublic    = Modifier.isPublic( mods );
        boolean isInstance  = Modifier.isStatic( mods );
        boolean isFinal     = Modifier.isFinal( mods );
        String  type        = field.getType().getName();
        Object  value;
        
        // The FQN of the String class is "java.lang.String".
        // This is too long to comfortably fit on a line in a report
        // so shorten it to "String".
        if ( type.contains( "String" ) )
            type = "String";
        try
        {
            // If we're interrogating a private field this could
            // throw an exception. In this demo we're only looking at
            // public fields, but IllegalAccessException is a checked
            // exception, so we still need to be prepared to catch it.
            value = field.get( instance );
        }
        catch ( IllegalAccessException exc )
        {
            value = "unk";
        }
        
        System.out.printf(
            fmt,
            name,
            isPublic,
            isInstance,
            isFinal,
            type,
            value
        );
    }
}
