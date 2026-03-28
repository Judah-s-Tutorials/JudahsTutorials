package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ShipTypeTest
{
    private static final String TEST_NAME   = "TestType";
    private static final int    TEST_LENGTH = 7;

    private static ShipType testType;

    @BeforeAll
    public static void beforeAll()
    {
        // Guard against double-registration when running alongside other test classes
        if ( ShipType.getShipType( "Battleship" ) == null )
            ShipType.registerDefaultTypes();

        if ( ShipType.getShipType( TEST_NAME ) == null )
            testType = new ConcreteShipType( TEST_NAME, TEST_LENGTH );
        else
            testType = ShipType.getShipType( TEST_NAME );
    }

    @Test
    void testShipType()
    {
        // Valid construction — testType was created (or retrieved) in @BeforeAll
        assertNotNull( testType );
        assertEquals( TEST_NAME,   testType.getTypeName() );
        assertEquals( TEST_LENGTH, testType.getLength() );

        // Duplicate name must throw BattleshipException
        assertThrows( BattleshipException.class,
            () -> new ConcreteShipType( TEST_NAME, TEST_LENGTH ) );
    }

    @Test
    void testGetTypeName()
    {
        assertEquals( "Battleship", ShipType.getShipType( "Battleship" ).getTypeName() );
        assertEquals( "Carrier",    ShipType.getShipType( "Carrier"    ).getTypeName() );
        assertEquals( "Destroyer",  ShipType.getShipType( "Destroyer"  ).getTypeName() );
        assertEquals( "Cruiser",    ShipType.getShipType( "Cruiser"    ).getTypeName() );
        assertEquals( "Submarine",  ShipType.getShipType( "Submarine"  ).getTypeName() );
    }

    @Test
    void testGetLength()
    {
        assertEquals( 4, ShipType.getShipType( "Battleship" ).getLength() );
        assertEquals( 5, ShipType.getShipType( "Carrier"    ).getLength() );
        assertEquals( 2, ShipType.getShipType( "Destroyer"  ).getLength() );
        assertEquals( 3, ShipType.getShipType( "Cruiser"    ).getLength() );
        assertEquals( 3, ShipType.getShipType( "Submarine"  ).getLength() );
    }

    @Test
    void testGetImage()
    {
        // getImage() is documented to return null (not yet implemented)
        assertNull( testType.getImage() );
    }

    @Test
    void testToString()
    {
        assertEquals( "Battleship (4)", ShipType.getShipType( "Battleship" ).toString() );
        assertEquals( "Carrier (5)",    ShipType.getShipType( "Carrier"    ).toString() );
        assertEquals( TEST_NAME + " (" + TEST_LENGTH + ")", testType.toString() );
    }

    @Test
    void testEqualsHashObject()
    {
        ShipType    battleship  = ShipType.getShipType( "Battleship" );
        ShipType    carrier     = ShipType.getShipType( "Carrier" );

        // Same instance is equal to itself
        assertEquals( battleship, battleship );

        // Different instances are not equal
        assertNotEquals( battleship, carrier );

        // Not equal to null
        assertNotEquals( battleship, null );

        // Not equal to an object of a different type
        assertNotEquals( battleship, "Battleship" );

        // hashCode is consistent for the same object
        assertEquals( battleship.hashCode(), battleship.hashCode() );
    }

    @Test
    void testGetShipType()
    {
        ShipType    battleship  = ShipType.getShipType( "Battleship" );
        assertNotNull( battleship );
        assertEquals( "Battleship", battleship.getTypeName() );

        // Unknown name returns null
        assertNull( ShipType.getShipType( "UnknownShip" ) );
    }

    @Test
    void testRegisterDefaultTypes()
    {
        // All five default types must be present after registerDefaultTypes()
        assertNotNull( ShipType.getShipType( "Battleship" ) );
        assertNotNull( ShipType.getShipType( "Carrier"    ) );
        assertNotNull( ShipType.getShipType( "Destroyer"  ) );
        assertNotNull( ShipType.getShipType( "Cruiser"    ) );
        assertNotNull( ShipType.getShipType( "Submarine"  ) );
    }

    @Test
    void testGetTypes()
    {
        Collection<ShipType>    types   = ShipType.getTypes();
        assertNotNull( types );
        // At least the 5 default types must be present
        assertTrue( types.size() >= 5 );
        assertTrue( types.contains( ShipType.getShipType( "Battleship" ) ) );
        assertTrue( types.contains( ShipType.getShipType( "Carrier"    ) ) );
        assertTrue( types.contains( ShipType.getShipType( "Destroyer"  ) ) );
        assertTrue( types.contains( ShipType.getShipType( "Cruiser"    ) ) );
        assertTrue( types.contains( ShipType.getShipType( "Submarine"  ) ) );
    }

    /** Concrete subclass used only for testing the abstract ShipType. */
    private static class ConcreteShipType extends ShipType
    {
        public ConcreteShipType( String name, int length )
        {
            super( name, length );
        }
    }
}
