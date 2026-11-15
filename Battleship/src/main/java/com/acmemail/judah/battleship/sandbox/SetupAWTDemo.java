package com.acmemail.judah.battleship.sandbox;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.ui.awt.SetupPhaseAWT;

public class SetupAWTDemo
{
    private static SetupPhaseAWT    mainWindow;
    private static BasicProvisioner provisioner = new BasicProvisioner();
    public static void main(String[] args)
    {
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
