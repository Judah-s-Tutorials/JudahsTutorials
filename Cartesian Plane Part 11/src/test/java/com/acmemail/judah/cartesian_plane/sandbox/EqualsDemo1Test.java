package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EqualsDemo1Test
{
    @Test
    void testEquals()
    {
        String      alpha1  = "alpha 1";
        String      alpha2  = "alpha 2";
        String      beta1   = "beta 1";
        String      beta2   = "beta 2";
        EqualsDemo1 objA    = new EqualsDemo1( alpha1, beta1 );
        EqualsDemo1 objB    = new EqualsDemo1( alpha1, beta1 );
        
        assertEquals( objA, objA );
        assertNotEquals( objA, new Object() );
        assertNotEquals( objA, null );
        assertEquals( objA, objB );
        assertEquals( objA.hashCode(), objB.hashCode() );
        
        objB = new EqualsDemo1( null, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        objB = new EqualsDemo1( alpha2, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        objB = new EqualsDemo1( alpha1, null );
        assertNotEquals( objB, objA );
        assertNotEquals( objA, objB );
        objB = new EqualsDemo1( alpha1, beta2 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );
        
        objA = new EqualsDemo1( null, beta1 );
        objB = new EqualsDemo1( null, beta1 );
        assertEquals( objA, objB );
        assertEquals( objA.hashCode(), objB.hashCode() );
        objB = new EqualsDemo1( alpha1, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );

        objA = new EqualsDemo1( alpha1, null );
        objB = new EqualsDemo1( alpha1, null );
        assertEquals( objA, objB );
        assertEquals( objA.hashCode(), objB.hashCode() );
        objB = new EqualsDemo1( alpha1, beta1 );
        assertNotEquals( objA, objB );
        assertNotEquals( objB, objA );

        objA = new EqualsDemo1( null, null );
        objB = new EqualsDemo1( null, null );
        assertEquals( objA, objB );
        assertEquals( objA.hashCode(), objB.hashCode() );
    }
}
