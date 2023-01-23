package com.acmemail.judah.cartesian_plane.sandbox;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionPublicFieldsDemo
{
    private static final String header1    = "Name       public class final type value";
    private static final String header2    = "========================================";

    
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
        
        if ( type.contains( "String" ) )
            type = "String";
        try
        {
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
