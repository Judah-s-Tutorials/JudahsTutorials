package com.acmemail.judah.battleship.fleet;

import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.StatusMessages;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.ShipTypes;

class FleetProvisionData
{
    public static final ShipType2D      TYPE3X1    =
        new ShipType2D( "3X1", 3, 1, null );
    public static final ShipType2D      TYPE3X2    =
        new ShipType2D( "3X2", 3, 2, null );
    public static final ShipType2D      TYPE2X2    =
        new ShipType2D( "2X2", 2, 2, null );
    public static final ShipType2D      TYPE4X1    =
        new ShipType2D( "4X1", 4, 1, null );
    public static final ShipType2D[]    ALL_TYPES  =
    { TYPE3X1, TYPE3X2, TYPE2X2, TYPE4X1 };
    
    public static final GridCoords      ADHOC_COORDS = new GridCoords( 0, 0 );
    public static final String          ADHOC_NAME   = "adhocName";
    public static final Orientation     ADHOC_ORIENT = Orientation.HORIZONTAL;
    public static final Ship2D          ADHOC_SHIP1;

    static
    {
        ShipTypes.register( TYPE3X1 );
        ShipTypes.register( TYPE3X2 );
        ShipTypes.register( TYPE2X2 );
        ShipTypes.register( TYPE4X1 );
        ADHOC_SHIP1 =
            new Ship2D(
                TYPE3X1,
                "Ad hoc test ship 1",
                new GridCoords( 0, 0 ),
                Orientation.HORIZONTAL
            );
    }
    
    public static Fleet getProvisionedFleet()
    {
        Fleet   fleet   = new Fleet();
        for ( ShipType2D type : ALL_TYPES )
            fleet.addToBeDeployed( type, type.typeName() );
        return fleet;
    }
    
    public static void deployAll( Fleet fleet )
    {
        final Orientation horizontal    = Orientation.HORIZONTAL;
        
        List<Fleet.Proto>   workingList = 
            new ArrayList<>( fleet.getToBeDeployed() );
        int                 nextYco     = 0;
        for ( Fleet.Proto proto : workingList )
        {
            GridCoords  coords  = new GridCoords( 0, nextYco );
            Ship2D      ship    =
                fleet.getShip( coords, ADHOC_NAME, horizontal, proto );
            Result      result  = fleet.deploy( ship, proto );
            if ( !result.getStatus() )
            {
                List<String>    messages    = result.getMessages();
                String          allMessages = String.join( "\n", messages );
                throw new BattleshipException( allMessages );
            }
            nextYco += ship.getBounds().height;
        }
        if ( !fleet.isFullyDeployed() )
            throw new BattleshipException( StatusMessages.MALFUNCTION );
    }
}
