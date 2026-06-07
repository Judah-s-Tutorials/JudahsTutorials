package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class EqualsDemo2Test
{
    @Test
    public void testEquals()
    {
        String      alpha1  = "alpha 1";
        String      alpha2  = "alpha 2";
        String      beta1   = "beta 1";
        String      beta2   = "beta 2";
        EqualsDemo2 objA    = new EqualsDemo2( alpha1, beta1 );
        EqualsDemo2 objB    = new EqualsDemo2( alpha1, beta1 );
        
        assertEquals( objA, objA );
        assertNotEquals( objA, new Object() );
        assertNotEquals( objA, null );
        assertEquals( objA, objB );
        assertEquals( objB, objA );
        assertEquals( objA.hashCode(), objB.hashCode() );
        
        // field alpha
        
        objA = new EqualsDemo2( alpha1, beta1 );
        objB = new EqualsDemo2( null, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objB = new EqualsDemo2( alpha2, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objA = new EqualsDemo2( null, beta1 );
        objB = new EqualsDemo2( alpha1, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objA = new EqualsDemo2( null, beta1 );
        objB = new EqualsDemo2( null, beta1 );
        assertEquals( objA, objB );
        assertEquals( objB, objA );
        assertEquals( objA.hashCode(), objB.hashCode() );
        
        // field beta
        
        objA = new EqualsDemo2( alpha1, beta1 );
        objB = new EqualsDemo2( alpha1, null );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objB = new EqualsDemo2( alpha1, beta2 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objA = new EqualsDemo2( alpha1, null );
        objB = new EqualsDemo2( alpha1, beta2 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objA = new EqualsDemo2( alpha1, null );
        objB = new EqualsDemo2( alpha1, null );
        assertEquals( objA, objB );
        assertEquals( objB, objA );
        assertEquals( objA.hashCode(), objB.hashCode() );

        objA = new EqualsDemo2( null, null );
        objB = new EqualsDemo2( null, null );
        assertEquals( objA, objB );
        assertEquals( objB, objA );
        assertEquals( objA.hashCode(), objB.hashCode() );
    }
    
    @Test
    public void verifyHash()
    {
        // Verify the reliability of the hashCode method.
        // Unequal objects are not required to have unequal hashcodes,
        // but, in this limited test, if they don't it may indicate
        // a problem with the hash algorithm and should be
        // investigated.
        String      alpha1  = "alpha 1";
        String      alpha2  = "alpha 2";
        String      beta1   = "beta 1";
        String      beta2   = "beta 2";
        
        EqualsDemo2 objA    = new EqualsDemo2( alpha1, beta1 );
        EqualsDemo2 objB    = new EqualsDemo2( alpha1, beta2 );        
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB = new EqualsDemo2( alpha2, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB = new EqualsDemo2( alpha2, beta2 );
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB    = new EqualsDemo2( alpha1, null );        
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB    = new EqualsDemo2( alpha2, null );        
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB    = new EqualsDemo2( null, beta1 );        
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB    = new EqualsDemo2( null, beta2 );        
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
        
        objB    = new EqualsDemo2( null, null );        
        assertNotEquals( objA, objB );
        assertNotEquals( objA.hashCode(), objB.hashCode() );
    }
}
