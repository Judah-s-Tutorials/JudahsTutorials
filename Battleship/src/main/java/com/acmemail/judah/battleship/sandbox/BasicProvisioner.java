package com.acmemail.judah.battleship.sandbox;

import static com.acmemail.judah.battleship.StatusMessages.DUP_SHIP_TYPE;
import static com.acmemail.judah.battleship.StatusMessages.SHIP_TYPE_NOT_FOUND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Provisioner;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

public class BasicProvisioner implements Provisioner
{
    private int     numRows     = 15;
    private int     numCols     = 10;
    
    private List<ShipType2D>    toDeploy    = new ArrayList<>();
    private List<ShipType2D>    toRegister  = new ArrayList<>();
    private List<String>        players     = new ArrayList<>();
    
    @Override
    public Integer getRows()
    {
        return numRows;
    }

    @Override
    public Integer getCols()
    {
        return numCols;
    }
    
    public void setRows( int numRows )
    {
        this.numRows = numRows;
    }
    
    public void setCols( int numCols )
    {
        this.numCols = numCols;
    }
    
    public void registerAll()
    {
        ShipType2D[]    defaultTypes    =
        {
            Battleship.getType(),
            Carrier.getType(),
            Cruiser.getType(),
            Destroyer.getType(),
            Submarine.getType(),
        };
        Arrays.stream( defaultTypes ).forEach( this::register );
    }
    
    public void register( ShipType2D type )
    {
        if ( toRegister.contains( type ) )
            throw new BattleshipException( DUP_SHIP_TYPE );
        else
            toRegister.add( type );
    }
    
    public void deploy( ShipType2D type )
    {
        if ( !toRegister.contains( type ) )
            throw new BattleshipException( SHIP_TYPE_NOT_FOUND );
        toDeploy.add( type );
    }
    
    public void addPlayer( String name )
    {
        if ( players.contains( name ) )
            throw new BattleshipException( name );
        players.add( name );
    }

    @Override
    public List<ShipType2D> getToRegister()
    {
        List<ShipType2D>    list    = Collections.unmodifiableList( toRegister );
        return list;
    }

    @Override
    public List<ShipType2D> getToDeploy()
    {
        List<ShipType2D>    list    = Collections.unmodifiableList( toDeploy );
        return list;
    }

    @Override
    public List<String> getPlayers()
    {
        List<String>    list    = Collections.unmodifiableList( players );
        return list;
    }

}
