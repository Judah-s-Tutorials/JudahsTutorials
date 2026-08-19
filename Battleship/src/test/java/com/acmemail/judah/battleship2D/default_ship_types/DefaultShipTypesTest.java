package com.acmemail.judah.battleship2D.default_ship_types;

import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_BREADTH;
import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_BATTLESHIP_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_BREADTH;
import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_CARRIER_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_BREADTH;
import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_CRUISER_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_DESTROYER_BREADTH;
import static com.acmemail.judah.battleship.Constants.DEF_DESTROYER_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_DESTROYER_NAME;
import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_BREADTH;
import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_LEN;
import static com.acmemail.judah.battleship.Constants.DEF_SUBMARINE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.battleship2D.ShipType2D;

/**
 * Verifies that each default ship type class
 * (Battleship, Carrier, Cruiser, Destroyer, Submarine)
 * is wired to the correct name, length, and breadth
 * from {@link com.acmemail.judah.battleship.Constants}.
 */
class DefaultShipTypesTest
{
    @ParameterizedTest
    @MethodSource( "allDefaultTypes" )
    public void testDefaultType(
        Supplier<ShipType2D> typeSupplier,
        String                expName,
        int                   expLength,
        int                   expBreadth
    )
    {
        ShipType2D  type    = typeSupplier.get();
        assertEquals( expName, type.typeName() );
        assertEquals( expLength, type.length() );
        assertEquals( expBreadth, type.breadth() );
    }

    private static Stream<Arguments> allDefaultTypes()
    {
        return Stream.of(
            Arguments.of(
                (Supplier<ShipType2D>)Battleship::getType,
                DEF_BATTLESHIP_NAME, DEF_BATTLESHIP_LEN, DEF_BATTLESHIP_BREADTH
            ),
            Arguments.of(
                (Supplier<ShipType2D>)Carrier::getType,
                DEF_CARRIER_NAME, DEF_CARRIER_LEN, DEF_CARRIER_BREADTH
            ),
            Arguments.of(
                (Supplier<ShipType2D>)Cruiser::getType,
                DEF_CRUISER_NAME, DEF_CRUISER_LEN, DEF_CRUISER_BREADTH
            ),
            Arguments.of(
                (Supplier<ShipType2D>)Destroyer::getType,
                DEF_DESTROYER_NAME, DEF_DESTROYER_LEN, DEF_DESTROYER_BREADTH
            ),
            Arguments.of(
                (Supplier<ShipType2D>)Submarine::getType,
                DEF_SUBMARINE_NAME, DEF_SUBMARINE_LEN, DEF_SUBMARINE_BREADTH
            )
        );
    }
}
