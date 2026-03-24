package com.acmemail.judah.battleship;

import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

/**
 * An instance of this class is used to request a ship configuration
 * from the operator.
 */
public class Placer
{
    private static JOptionPaneI jOptionPane = new JOptionPaneS();
    static final String title   = "Place Ship";

    private ShipType    type;
    private GridCoords  coords;
    private Orientation orientation;
    
    public static JOptionPaneI getJOptionPaneInterface()
    {
        return jOptionPane;
    }
    
    public static void setJOptionPaneInterface( JOptionPaneI jOptionPane )
    {
        Placer.jOptionPane = jOptionPane;
    }

    /**
     * 
     */
    public Placer()
    {
//        this( new JOptionPaneS() );
    }
    
//    public Placer( JOptionPaneI jOptionPane )
//    {
//        this.jOptionPane = jOptionPane;
//    }

    public Ship placeShip()
    {
        type = null;
        coords = null;
        orientation = null;
        
        Ship    ship    = null;
        if ( (type = getType()) == null )
            ship = null;
        else if ( (orientation = getOrientation() ) == null )
            ship = null;
        else
            ship = getShip();
        return ship;
    }
    
    private static ShipType getType()
    {
        final String    noTypes     = "No ship types to select";
        final String    prompt      = "Select ship type";
        final int       messageType = JOptionPane.QUESTION_MESSAGE;
        
        Collection<ShipType>    allTypes    = ShipType.getTypes();
        int                     numTypes    = allTypes.size();
        Object                  option      = null;
        if ( numTypes == 0 )
            showErrorMessage( noTypes );
        else
        {
            ShipType[]              options     = 
                allTypes.toArray( new ShipType[numTypes] );
            option  = jOptionPane.showInputDialog(
                    null, 
                    prompt,
                    title,
                    messageType,
                    null,
                    options, 
                    options[0]
                );
        }
        return (ShipType)option;
    }
    
    private static GridCoords parseCoords( String strCoords )
    {
        String          worker      = strCoords.trim().toUpperCase();
        GridCoords      coords      = null;
        int             row         = -1;
        int             col         = -1;
        String[]        parsed      = Label.parseRowCol( worker );
        
        GridCoords      temp        = null;
        String          rowStr      = parsed[0];
        String          colStr      = parsed[1];
        List<String>    rowErrors   = Label.validateRowStr( rowStr );
        List<String>    colErrors   = Label.validateColStr( colStr );
        if ( rowErrors.size() > 0 )
            showErrorMessage( "Invalid row format", rowErrors );
        else if ( colErrors.size() > 0 )
            showErrorMessage( "Invalid column format", colErrors );
        else
        {
            row = Label.alphaToDecimal( rowStr );
            col = Label.colStrToInt( colStr );
            temp = new GridCoords( col, row );
            List<String>    boundsErrors    = Grid.evaluateBounds( temp );
            if ( boundsErrors.size() > 0 )
                showErrorMessage( "Invalid coordinates", rowErrors );
            else
                coords =  temp;
        }
        return coords;
    }
    
    private static Orientation getOrientation()
    {
        final String        prompt      = "Select Orientation";
        final Orientation[] options     = Orientation.values();
        final int           messageType = JOptionPane.QUESTION_MESSAGE;
        Object              option      =
            jOptionPane.showInputDialog(
                null, 
                prompt,
                title,
                messageType,
                null,
                options, 
                options[0]
            );
        if ( option != null && !(option instanceof Orientation) )
        {
            String  name    = option.getClass().getSimpleName();
            String  message = "Malfunction; Invalid Orientation: " + name;
            throw new BattleshipException( message );
        }
        Orientation orientation = (Orientation)option;
        return orientation;
    }
    
    private Ship getShip()
    {
        final String    prompt      = "Enter Ship Coordinates";
        boolean         valid       = false;
        boolean         canceled    = false;
        Ship            ship        = null;
        while ( !valid && !canceled )
        {
            GridCoords  coords  = null;
            String      input   = 
                jOptionPane.showInputDialog( null, prompt );
            if ( input == null )
                canceled = true;
            else if ( (coords = parseCoords( input )) == null )
                valid = false;
            else
            {
                ship = new Ship( type, coords, orientation );
                List<String>    errors  = Grid.evaluateBounds( ship );
                if ( errors.size() != 0 )
                {
                    String  message = "Ship coordinates out of bounds";
                    showErrorMessage( message, errors );
                    ship = null;
                    valid = false;
                }
                else
                {
                    this.coords = coords;
                    valid = true;
                }
            }
        }
        return ship;
    }
    
    private static void showErrorMessage( String message )
    {
        showErrorMessage( message, "" );
    }
    
    private static void showErrorMessage( String message, String input )
    {
        final String    title       = "Parse Error";
        final int       messageType = JOptionPane.ERROR_MESSAGE;
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( message );
        if ( input != null && !input.isEmpty() )
            bldr.append( ": \"" )
                .append( input )
                .append( "\"" );
        jOptionPane.showMessageDialog( 
            null, 
            message, 
            title, 
            messageType 
        );
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
        jOptionPane.showMessageDialog( null, bldr, title, errorIcon );
    }
}
