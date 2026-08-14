package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship2D.default_ship_types.Battleship;
import com.acmemail.judah.battleship2D.default_ship_types.Carrier;
import com.acmemail.judah.battleship2D.default_ship_types.Cruiser;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

class ShipTypesTest
{
    private static String       typeNameA   = "Type name A";
    private static ShipType2D   typeA       = 
        new ShipType2D( typeNameA, 10, 5, null );
    private static String       typeNameB   = "Type name B";
    private static ShipType2D   typeB       = 
        new ShipType2D( typeNameB, 10, 5, null );
    private static String       typeNameC   = "Type name C";
    private static ShipType2D   typeC       = 
        new ShipType2D( typeNameC, 10, 5, null );
    
    private static ShipType2D[] allTypes    = { typeA, typeB, typeC };

    @Test
    public void testRegister()
    {
        for ( ShipType2D type : allTypes )    
            ShipTypes.register( type );
        
        for ( ShipType2D type : allTypes ) 
        {
            String      name    = type.typeName();
            ShipType2D  test    = ShipTypes.getShipType( name );
            assertEquals( name, test.typeName() );
        }
        
        for ( ShipType2D type : allTypes )    
            assertThrows( BattleshipException.class, () -> 
                ShipTypes.register( type ) 
            );
        
        ShipType2D  expNone = ShipTypes.getShipType( "No such type" );
        assertNull( expNone );
    }

    @Test
    public void testRegisterDefaultTypes()
    {
        String[]    defNames    =
        {
            Battleship.getType().typeName(),
            Carrier.getType().typeName(),
            Cruiser.getType().typeName(),
            Destroyer.getType().typeName(),
            Submarine.getType().typeName()
        };
        ShipTypes.registerDefaultTypes();
        for ( String name : defNames )
        {
            ShipType2D  testType    = ShipTypes.getShipType( name );
            assertNotNull( testType );
            assertEquals( name, testType.typeName() );
        }
    }

}
