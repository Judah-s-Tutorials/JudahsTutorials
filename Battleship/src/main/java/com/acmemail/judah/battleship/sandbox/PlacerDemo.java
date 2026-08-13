package com.acmemail.judah.battleship.sandbox;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Placer;
import com.acmemail.judah.battleship.Ship;
import com.acmemail.judah.battleship.ShipType;
import com.acmemail.judah.battleship.artwork.GraphicalGrid;
import com.acmemail.judah.battleship2D.Grid;

public class PlacerDemo
{
    private static  GraphicalGrid    graphicalGrid;

    private PlacerDemo()
    {
    }

    public static void main(String[] args)
    {
        ShipType.registerDefaultTypes();
        try
        {
            SwingUtilities.invokeAndWait( () -> showGUI() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        SwingUtilities.invokeLater( () -> placeShips() );
    }

    private static void showGUI()
    {
        Grid             grid           = Grid.getHomeGrid();
        graphicalGrid = new GraphicalGrid( grid );

        JFrame  frame   = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        frame.setContentPane( pane );
        pane.add( graphicalGrid, BorderLayout.CENTER );
        frame.pack();
        frame.setVisible( true );
    }
    
    private static void placeShips()
    {
        int     okOption        = JOptionPane.OK_OPTION;
        Placer  placer          = new Placer();
        int     continueOption  = okOption;
        while ( continueOption == okOption )
        {
            Ship    ship    = placer.placeShip();
            System.out.println( ship );
            if ( ship != null )
            {
                Fleet.add( ship );
                graphicalGrid.update();
            }
            continueOption = JOptionPane.showConfirmDialog( null, "Next?" );
        }
        System.exit( 0 );
    }
}
