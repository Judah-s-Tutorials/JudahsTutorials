package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.model.Orientation;

class OrientationTest
{
    @Test
    void test()
    {
        Orientation[]   allConstants    = Orientation.values();
        assertEquals( 2, allConstants.length );
    }
}
