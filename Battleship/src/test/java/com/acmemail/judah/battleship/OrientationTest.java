package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrientationTest
{
    @Test
    void test()
    {
        Orientation[]   allConstants    = Orientation.values();
        assertEquals( 2, allConstants.length );
    }
}
