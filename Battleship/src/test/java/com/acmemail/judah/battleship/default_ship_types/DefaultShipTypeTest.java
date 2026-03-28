package com.acmemail.judah.battleship.default_ship_types;

import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_DESTROYER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_DESTROYER_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.ShipType;

class DefaultShipTypeTest
{
    private static  ShipType   battleship;
    private static  ShipType   carrier;
    private static  ShipType   cruiser;
    private static  ShipType   destroyer;
    private static  ShipType   submarine;

    @BeforeAll
    public static void beforeAll()
    {
        if ( (battleship = getShipType( DEF_BATTLESHIP_NAME )) == null )
            battleship = new Battleship();
        if ( (carrier = getShipType( DEF_CARRIER_NAME )) == null )
            carrier = new Carrier();
        if ( (cruiser = getShipType( DEF_CRUISER_NAME )) == null )
            cruiser = new Cruiser();
        if ( (destroyer = getShipType( DEF_DESTROYER_NAME )) == null )
            destroyer = new Destroyer();
        if ( (submarine = getShipType( DEF_SUBMARINE_NAME )) == null )
            submarine = new Submarine();
    }
    
    @Test
    public void testBattleShip()
    {
        assertEquals( DEF_BATTLESHIP_LEN, battleship.getLength() );
        assertEquals( DEF_BATTLESHIP_NAME, battleship.getTypeName() );
    }

    @Test
    public void testCarrier()
    {
        assertEquals( DEF_CARRIER_LEN, carrier.getLength() );
        assertEquals( DEF_CARRIER_NAME, carrier.getTypeName() );
    }

    @Test
    public void testCruiser()
    {
        assertEquals( DEF_CRUISER_LEN, cruiser.getLength() );
        assertEquals( DEF_CRUISER_NAME, cruiser.getTypeName() );
    }

    @Test
    public void testDestroyer()
    {
        assertEquals( DEF_DESTROYER_LEN, destroyer.getLength() );
        assertEquals( DEF_DESTROYER_NAME, destroyer.getTypeName() );
    }

    @Test
    public void testSubmarine()
    {
        assertEquals( DEF_SUBMARINE_LEN, submarine.getLength() );
        assertEquals( DEF_SUBMARINE_NAME, submarine.getTypeName() );
    }
    
    private static ShipType getShipType( String name )
    {
        ShipType    shipType    = ShipType.getShipType( name );
        return shipType;
    }
}
