package com.acmemail.judah.cartesian_plane;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

public class Test
{
    public static void main(String[] args)
    {
        JColorChooser colorPane   = new JColorChooser();        
        JDialog       colorDialog = JColorChooser.createDialog(
            null, 
            "Choose a Color", 
            true, 
            colorPane, 
            null,//e -> setAndClose( JOptionPane.OK_OPTION ), 
            null//e -> setAndClose( JOptionPane.CANCEL_OPTION )
        );
        colorPane.setColor( Color.BLUE );
        colorDialog.setVisible( true );
        System.out.println( colorPane.getColor() );
    }

}
