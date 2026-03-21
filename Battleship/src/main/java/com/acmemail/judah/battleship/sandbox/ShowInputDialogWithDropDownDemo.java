package com.acmemail.judah.battleship.sandbox;

import java.util.Collection;

import javax.swing.JOptionPane;

import com.acmemail.judah.battleship.ShipType;

public class ShowInputDialogWithDropDownDemo
{

    private ShowInputDialogWithDropDownDemo()
    {
    }

    public static void main( String[] args )
    {
        final String    prompt      = "Select ship type";
        final String    title       = "Ship Selection";
        final int       messageType = JOptionPane.QUESTION_MESSAGE;
        
        ShipType.registerDefaultTypes();
        Collection<ShipType>    allTypes    = ShipType.getTypes();
        int                     numTypes    = allTypes.size();
        ShipType[]              options     = 
            allTypes.toArray( new ShipType[numTypes] );
        Object                  option      =
            JOptionPane.showInputDialog(
                null, 
                prompt,
                title,
                messageType,
                null,
                options, 
                options[0]
            );
        System.out.println( option );
    }

}
