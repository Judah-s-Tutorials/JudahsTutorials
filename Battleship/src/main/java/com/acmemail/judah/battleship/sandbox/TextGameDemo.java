package com.acmemail.judah.battleship.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Fleet.Proto;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.ui.text.GamePhase;

public class TextGameDemo
{
    public static void main( String[] args )
    {
        BasicProvisioner    prov    = new BasicProvisioner();
        prov.setCols( 15 );
        prov.setRows( 17 );
        prov.addDefaultTypesToRegister();
        prov.addDefaultTypesToDeploy();
        prov.addPlayer( "Todd" );
        Grid2D.reset( prov.getRows(), prov.getCols() );

        Fleet   fleet   = new Fleet();
        prov.getToDeploy().forEach(
            t -> fleet.addToBeDeployed( t, null )
        );
        Configurator.nextState();
        config( fleet );
        prov.getPlayers().forEach( Grid2D::new );
        GamePhase   game    = new GamePhase( fleet );
        game.exec();
    }
    
    private static void config( Fleet fleet )
    {
        int         rows            = Grid2D.getNumRows();
        int         cols            = Grid2D.getNumCols();
        int         maxLen          = getMaxLen( fleet );
        int         verticalRow     = rows / 2 - maxLen / 2;
        int         horizontalCol   = cols / 2;
        int         nextRow         = 2;
        int         nextCol         = 2;
        List<Proto> toBeDeployed    =
            new ArrayList<>( fleet.getToBeDeployedProtos() );
        int         shipCount       = toBeDeployed.size();
        
        // Implement a simple algorithm to deploy all ships;
        // throw if it fails.
        for ( int inx = 0 ; inx < shipCount ; ++inx )
        {
            Proto       proto   = toBeDeployed.get( inx );
            ShipType2D  type    = proto.getType();
            Ship2D      ship;
            if ( inx % 2 == 0 )
            {
                GridCoords  coords  = new GridCoords( nextCol, verticalRow );
                ship = new Ship2D( type, coords, Orientation.VERTICAL );
                nextCol += 2;
            }
            else
            {
                GridCoords  coords  = new GridCoords( horizontalCol, nextRow );
                ship = new Ship2D( type, coords, Orientation.HORIZONTAL );
                nextRow += 2;
            }
            Result      result  = fleet.deploy( ship, proto );
            if ( !result.getStatus() )
            {
                String  message = String.join( "\n", result.getMessages() );
                throw new BattleshipException( message );
            }
        }
    }
    
    private static int getMaxLen( Fleet fleet )
    {
        int     maxLen      = 
            fleet.getToBeDeployedProtos().stream()
                .map( p -> p.getType() )
                .mapToInt( t -> t.length() )
                .max().orElseThrow();
        return maxLen;
    }
}
