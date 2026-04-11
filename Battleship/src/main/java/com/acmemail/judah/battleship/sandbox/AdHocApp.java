package com.acmemail.judah.battleship.sandbox;

import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;

import java.awt.BorderLayout;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Grid;
import com.acmemail.judah.battleship.GridCoords;
import com.acmemail.judah.battleship.Label;
import com.acmemail.judah.battleship.Orientation;
import com.acmemail.judah.battleship.Ship;
import com.acmemail.judah.battleship.ShipType;
import com.acmemail.judah.battleship.artwork.GraphicalGrid;

public class AdHocApp
{
    private static GraphicalGrid    graphicalGrid;
    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        double  d1  = 0xba;
        double  d2  = 0x8e;
        double  d3  = 0x23;
        System.out.printf( "%f, %f, %f%n", d1/255, d2/255, d3/255 );
        Grid    grid    = Grid.getHomeGrid();
        
        Ship        ship;
        GridCoords  coords;
        ShipType.registerDefaultTypes();
        ShipType    battleship  = ShipType.getShipType( "Battleship" );
        ShipType    destroyer   = ShipType.getShipType( "Destroyer" );
        ShipType    submarine   = ShipType.getShipType( "Submarine" );
        
        coords = new GridCoords( 6, 0 );
        ship = new Ship( battleship, coords, HORIZONTAL );
        Fleet.add( ship );
//        
//        coords = new GridCoords( 3, 3 );
//        ship = new Ship( destroyer, coords, VERTICAL );
//        Fleet.add( ship );
//        
//        coords = new GridCoords( 5, 4 );
//        ship = new Ship( submarine, coords, HORIZONTAL );
//        Fleet.add( ship );
//        
//        grid.attack( 10, 8 );
//        grid.attack( 4, 3 );
//        grid.attack( 5, 7 );
//        grid.attack( 7, 4 );
//        grid.attack( 2, 1 );
//        grid.attack( 3, 1 );

        SwingUtilities.invokeLater( () -> {
            JFrame  frame   = new JFrame();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            JPanel  pane    = new JPanel( new BorderLayout() );
            frame.setContentPane( pane );
            graphicalGrid = new GraphicalGrid( grid );
            pane.add( graphicalGrid, BorderLayout.CENTER );
            frame.pack();
            frame.setVisible( true );
        });
        
        SwingUtilities.invokeLater( () -> playGhost() );
    }
    
    @SuppressWarnings("unused")
    private static void place()
    {
        final String    title   = "Place Ship";
        final String    prompt  = "Enter ship type, row ID, col ID";
        final String    seps    = " ,";
        String          command = null;
        while ( (command = getInput( prompt, title )) != null )
        {
            StringTokenizer tizer   = new StringTokenizer( command, seps );
            if ( tizer.countTokens() != 3 )
                showErrorMessage( "Invalid number of arguments", command );
            else
            {
                String      typeToken   = tizer.nextToken();
                String      rowToken    = tizer.nextToken();
                String      colToken    = tizer.nextToken();
                ShipType    shipType    = parseShipType( typeToken );
                int         row         = parseRow( rowToken );
                int         col         = parseCol( colToken );
                GridCoords  coords      = null;
                Ship        ship        = null;
                
                if ( shipType == null )
                    ;
                else if ( row < 0 )
                    ;
                else if ( col < 0 )
                    ;
                else if ( (coords = getGridCoords( col, row )) == null )
                    ;
                else if ( (ship = getShip( shipType, coords )) == null )
                    ;
                else
                    Fleet.add( ship );
            }
        }
    }
    
    private static GridCoords getGridCoords( int col, int row )
    {
        GridCoords      coords  = new GridCoords( col, row );
        List<String>    errors  = Grid.evaluateBounds( coords );
        if ( errors.size() > 0 )
        {
            coords = null;
            showErrorMessage( "Invalid grid coordinates", errors );
        }
        return coords;
    }
    
    private static ShipType parseShipType( String token )
    {
        ShipType    type    = ShipType.getShipType( token );
        if ( type == null )
            showErrorMessage( "Invalid ship type", token );
        return type;
    }
    
    private static int parseRow( String rowToken )
    {
        int row = -1;
        try
        {
            row = Label.alphaToDecimal( rowToken );
        }
        catch ( BattleshipException exc )
        {
            showErrorMessage( "Invalid row ID", rowToken );
        }
        return row;
    }
    
    private static int parseCol( String colToken )
    {
        int col = -1;
        try
        {
            col = Label.colStrToInt( colToken );
        }
        catch ( BattleshipException exc )
        {
            showErrorMessage( "Invalid column ID", colToken );
        }
        return col;
    }
    
    private static void showErrorMessage( String message, String input )
    {
        final String    title       = "Parse Error";
        final int       errorIcon   = JOptionPane.ERROR_MESSAGE;
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( message )
            .append( ": \"" )
            .append( input )
            .append( "\"" );
        JOptionPane.showMessageDialog( null, message, title, errorIcon );
    }
    
    private static 
    void showErrorMessage( String message, List<String> errors )
    {
        final String    title       = "Parse Error";
        final int       errorIcon   = JOptionPane.ERROR_MESSAGE;
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( message ).append( ": \n" );
        for ( String error : errors )
            bldr.append( error ).append( "\n" );
        JOptionPane.showMessageDialog( null, message, title, errorIcon );
    }
    
    private static String getInput( String prompt, String title )
    {
        final int   messageType = JOptionPane.QUESTION_MESSAGE;
        String      input       = 
            JOptionPane.showInputDialog( null, prompt, title, messageType );
        if ( input != null )
            input = input.trim();
        return input;
    }
    
    private static void playGhost()
    {
        int         xco = 0;
        int         yco = 1;
        Orientation orientation = HORIZONTAL;
        for ( int inx = 0 ; inx < 5 ; ++inx )
        {
            ShipType    shipType    = ShipType.getShipType( "Carrier" );
            GridCoords  gridCoords  = new GridCoords( xco, yco );
            Ship        ship        = 
                new Ship( shipType, gridCoords, orientation );
            orientation = orientation == HORIZONTAL ? VERTICAL : HORIZONTAL; 
            graphicalGrid.setGhostShip( ship );
//            graphicalGrid.update();
//            pause();
//            graphicalGrid.setGhostShip( null );
//            graphicalGrid.update();
//            pause();
//            xco += 1;
//            yco += 1;
        }
    }
    
    private static Ship getShip( ShipType type, GridCoords coords )
    {
        final String    errorMessage    = "Cannot place ship";
        final String    prompt          = "Choose orientation";
        final String    title           = "Orientation Selection";
        final int       messageType     = JOptionPane.QUESTION_MESSAGE;
        final int       optionType      = JOptionPane.CANCEL_OPTION;
        Orientation[]   options         = Orientation.values();
        Ship            ship            = null;
        int             option          = 
            JOptionPane.showOptionDialog(
                null, 
                prompt, 
                title, 
                optionType,
                messageType, 
                null,
                options,
                0
            );
        if ( option >= 0 && option < options.length )
        {
            Orientation     orientation = options[option];
            ship = new Ship( type, coords, orientation );
            List<String>    errors      = Grid.evaluateBounds(ship);
            if ( errors.size() > 0 )
            {
                ship = null;
                showErrorMessage( errorMessage, errors);
            }
        }
        return ship;
    }
    
    @SuppressWarnings("unused")
    private static void pause()
    {
        JOptionPane.showMessageDialog( null, "next" );
    }
}
