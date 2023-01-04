package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verify that a JUnit test 
 * can have multiple <em>before-each</em> methods.
 * 
 * @author Jack Straub
 *
 */
class MultipleBeforeEachDemo
{
    @BeforeEach
    void setUp() throws Exception
    {
        System.out.println( "setUp" );
    }
    
    @BeforeEach
    void beforeEach()
    {
        System.out.println( "beforeEach" );
    }

    @Test
    void test()
    {
        assertEquals( 5, 5 );
    }
}
