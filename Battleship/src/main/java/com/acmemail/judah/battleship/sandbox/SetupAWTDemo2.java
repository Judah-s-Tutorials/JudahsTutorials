package com.acmemail.judah.battleship.sandbox;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Carrier;
import com.acmemail.judah.battleship.model.default_ship_types.Cruiser;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;
import com.acmemail.judah.battleship.ui.awt.SetupPhaseAWT;

public class SetupAWTDemo2
{
    private static final    List<ShipType2D>    defTypes    =
        List.of(
            Battleship.getType(),
            Carrier.getType(),
            Cruiser.getType(),
            Destroyer.getType(),
            Submarine.getType()
        );
    private static final    List<ShipType2D>    altTypes    =
        List.of(
            new ShipType2D( "AltType1", 1, 2, null ),
            new ShipType2D( "AltType2", 2, 4, null ),
            new ShipType2D( "AltType3", 2, 5, null )
        );
    private static SetupPhaseAWT    mainWindow;
    private static BasicProvisioner provisioner = new BasicProvisioner();
    public static void main(String[] args)
    {
        provisioner.setRows( 15 );
        provisioner.setCols( 20 );
        IntStream.range( 1, 4 )
            .mapToObj( i -> "Player-" + i )
            .forEach( provisioner::addPlayer );
        defTypes.forEach( provisioner::addToRegister );
        altTypes.forEach( provisioner::addToRegister );
        altTypes.forEach( provisioner::addToDeploy );
        altTypes.forEach( provisioner::addToDeploy );
        
        invokeAndWait();
        
        String  result  = mainWindow.showDialog() ? "OKAY" : "CANCEL";
        System.out.println( result );
        int numRows = provisioner.getRows();
        int numCols = provisioner.getCols();
        System.out.printf( "rows %d, cols %d%n", numRows, numCols );
        System.exit( 0 );
    }

    private static void invokeAndWait()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> 
                mainWindow = new SetupPhaseAWT( provisioner ) );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
}
