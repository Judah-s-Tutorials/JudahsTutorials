package com.acmemail.judah.battleship.sandbox;

import com.acmemail.judah.battleship.TextProvisioner;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.ui.text.ConfigPhase;

public class TextConfigDemo
{
    private static final String battleShipName  = 
        Battleship.getType().typeName();
    private static final String destroyerName   = 
        Destroyer.getType().typeName();
    private static final String threeByTwoName  = "ThreeByTwo";
    private static final String fourByThreeName = "FourByThree";
    private static final String typeThreeByTwo  = 
        "type," + threeByTwoName + ",3,2";
    private static final String typeFourByThree = 
        "type," + fourByThreeName + ",4,3";
    private static final String typeAll         = "type,default";
    
    public static void main( String[] args )
    {
        TextProvisioner   prov        = TextProvisioner.of();
        addRec( prov, "dim,12,11" );
        addRec( prov, typeAll );
        addRec( prov, typeThreeByTwo );
        addRec( prov, typeFourByThree );
        addRec( prov, "deploy," + battleShipName );
        addRec( prov, "deploy," + destroyerName );
        addRec( prov, "deploy," + destroyerName );
        addRec( prov, "deploy," + threeByTwoName );
        addRec( prov, "deploy," + fourByThreeName );
        
        Grid2D.reset( prov.getRows(), prov.getCols() );
        ConfigPhase  config = new ConfigPhase( prov );
        config.exec();
        if ( config.isQuit() )
            System.out.println( "quitting" );
    }
    
    private static void addRec( TextProvisioner prov, String rec )
    {
        System.out.println( rec + ": " );
        prov.addRec( rec );
    }
}
