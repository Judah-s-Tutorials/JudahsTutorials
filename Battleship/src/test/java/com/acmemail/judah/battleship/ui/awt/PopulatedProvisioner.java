package com.acmemail.judah.battleship.ui.awt;

import java.util.List;
import java.util.stream.IntStream;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

/**
 * This subclass of BasicProvisioner
 * is instantiated with default test data.
 */
public class PopulatedProvisioner extends BasicProvisioner
{
    /** List of all default ship types. */
    private static final    List<ShipType2D>    defTypes    =
        List.of(
            Battleship.getType(),
            Carrier.getType(),
            Cruiser.getType(),
            Destroyer.getType(),
            Submarine.getType()
        );
    /** List of non-default ship types. */
    private static final    List<ShipType2D>    altTypes    =
        List.of(
            new ShipType2D( "AltType1", 1, 2, null ),
            new ShipType2D( "AltType2", 2, 4, null ),
            new ShipType2D( "AltType3", 2, 5, null )
        );

    /**
     * Default constructor.
     * Populates this Provisioner with default data.
     */
    public PopulatedProvisioner()
    {
        this.setRows( 15 );
        this.setCols( 20 );
        IntStream.range( 1, 4 )
            .mapToObj( i -> "Player-" + i )
            .forEach( this::addPlayer );
        defTypes.forEach( this::addToRegister );
        altTypes.forEach( this::addToRegister );
        defTypes.forEach( this::addToDeploy );
        altTypes.forEach( this::addToDeploy );
    }
}
