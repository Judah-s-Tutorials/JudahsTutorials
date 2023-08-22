package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Application to demonstrate
 * the simplest way
 * to use a JColorChooser.
 * Use <em>JColorChooser.showDialog</em>
 * to display the modal dialog.
 * If the operator clicks OK
 * the selected color is returned;
 * if the operator clicks Cancel
 * null is returned.
 * 
 * @author Jack Straub
 */
public class JColorChooserDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  title   = "JColorChooser Demo";
        Color   color   = null;
        do
        {
            color = JColorChooser.showDialog( null, title, Color.BLUE );
            System.out.println( color );
        } while ( color != null );
    }
}
