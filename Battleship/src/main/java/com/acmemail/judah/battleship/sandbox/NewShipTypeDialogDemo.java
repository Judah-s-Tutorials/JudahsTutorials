package com.acmemail.judah.battleship.sandbox;

import com.acmemail.judah.battleship.BasicProvisioner;
import com.acmemail.judah.battleship.model.ShipType2D;
import com.acmemail.judah.battleship.ui.awt.RegisterTypeDialog;

public class NewShipTypeDialogDemo
{
    public static void main(String[] args)
    {
        BasicProvisioner    provisioner = new BasicProvisioner();
        RegisterTypeDialog    dialog      = new RegisterTypeDialog( provisioner );
        ShipType2D          type;
        do
        {
            type = dialog.showDialog();
        } while ( type != null );
        System.exit( 0 );
    }

}
