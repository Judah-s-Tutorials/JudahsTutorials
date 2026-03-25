package com.acmemail.judah.battleship;

import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * An instance of this class is used to request a ship configuration
 * from the operator.
 * It is designed to be the sole source of interaction with the operator
 * for this purpose.
 * <p>
 * This class is designed to be used with Mockito
 * in order to improve testability of operator interaction.
 * To that end:
 * <ol>
 * <li>All operator interaction is conducted via JOptionPane.</li>
 * <li>
 *     Access to JOptionPane is provided by 
 *     interface {@linkplain JOptionPaneI}.
 * </li>
 * <li>
 * 
 * <li>
 *     During production,
 *     the functionality of JOPtionPaneI is provided by
 *     a concrete class which calls directly into JOptionPane.
 * </li>
 * <li>
 *     During testing,
 *     the functionality of JOPtionPaneI is provided by
 *     class which "mocks" JOptionPane.
 *     JOptionPane.showInputDialog, for example,
 *     is replaced by a method that returns a string
 *     provided by the testing facility,
 *     and which does not display a physical dialog.
 * </li>
 * </ol>
 * <p>
 * A single instance of this class can be used
 * to place multiple ships, 
 * for example:
 * <pre style="margin-left:2em;">Placer placer = new Placer();
 * placer.placeShip();
 * placer.placeShip();
 * placer.placeShip();</pre>
 * 
 * @author Jack
 * 
 * @see JOptionPaneI
 * @see JOptionPaneS
 * @see The <a href="https://site.mockito.org/">Mockito Website</a>
 * @see #setJOptionPaneInterface(JOptionPaneI)
 * @see #getJOptionPaneInterface()
 */
public class Placer
{
    /**  
     * The interface to JOptionPane.
     * @see #setJOptionPaneInterface(JOptionPaneI)
     */
    private static JOptionPaneI jOptionPane = new JOptionPaneS();
    /** The title of all dialogs that interact with the operator. */
    private static final String title   = "Place Ship";

    /**  The ShipType of the ship to place. */
    private ShipType    type;
    /**  The grid coordinates of the ship to place. */
    private GridCoords  coords;
    /**  The orientation of the ship to place. */
    private Orientation orientation;
    
    /**
     * Gets the implementation of JOptionPaneI
     * currently being used to interact with the operator.
     * 
     * @return  the JOptionPaneI implementation currently in use
     */
    public static JOptionPaneI getJOptionPaneInterface()
    {
        return jOptionPane;
    }
    
    /**
     * Sets the implementation of JOptionPaneI
     * that is used to provide operator input
     * to the given object.
     * 
     * @param jOptionPane   the given object
     */
    public static void setJOptionPaneInterface( JOptionPaneI jOptionPane )
    {
        Placer.jOptionPane = jOptionPane;
    }

    /**
     * The default constructor; not used.
     */
    public Placer()
    {
    }

    /**
     * Interacts with the operator to configure a ship
     * to be added to the fleet/grid.
     * The configured ship is returned to the caller.
     * The operator can cancel the operation at any step,
     * in which case null is returned to the caller.
     * 
     * @return  the configured ship, or null, if the operation is canceled
     */
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
        else if ( (ship = getShip()) != null )
            Fleet.add(ship);
        return ship;
    }
    
    /**
     * Displays a dialog containing a list
     * of all available ship types,
     * and allows the operator to select one.
     * The selected type is returned to the caller
     * unless the operation is canceled,
     * in which case null is returned.
     *  
     * @return the ship type selected by the operator, or null if canceled
     * 
     * @see ShipType
     * @see ShipType()
     * @see ShipType#registerDefaultTypes()
     */
    private static ShipType getType()
    {
        final String    noTypes     = "No ship types to select";
        final String    prompt      = "Select ship type";
        final int       messageType = JOptionPane.QUESTION_MESSAGE;
        
        Collection<ShipType>    allTypes    = ShipType.getTypes();
        int                     numTypes    = allTypes.size();
        Object                  option      = null;
        if ( numTypes == 0 )
            throw new BattleshipException( noTypes );
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
    
    /**
     * Parses a string containing row/column coordinates,
     * validates the coordinates,
     * and converts them into x- and y-coordinates.
     * if possible, a GridCoords object containing the x- and
     * y-coordinates is returned to the caller.
     * Null is returned 
     * if the input cannot be successfully parsed and converted.
     * For a description of the valid formats
     * for the row/column coordinates 
     * see {@linkplain Label#parseRowCol(String)}.
     * 
     * @param strCoords the row/column coordinates to parse
     * 
     * @return
     *      a GridCoords object containing the converted x- and
     *      y-coordinates, or null if the input cannot be parsed
     *      
     * @see Label
     * @see Label#parseRowCol(String)
     */
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
    
    /**
     * Displays a dialog containing the
     * two possible options for configuring
     * the orientation of a ship,
     * allow the operator to choose one.
     * The chosen orientation is returned
     * unless the operator cancels the operation,
     * in which case null is returned.
     * 
     * @return  the selected orientation or null if canceled
     */
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
    
    /**
     * Prompts the operator for the grid coordinates
     * of the position of the new ship,
     * then creates the ship. 
     * The coordinates entered by the operator
     * are validated;
     * if not valid,
     * an error message is displayed
     * and the operator is repeatedly prompted
     * to enter coordinates
     * until valid coordinates are entered
     * or the operation is canceled.
     * The created ship is returned
     * unless the operation is canceled,
     * in which case null is returned.
     * Valid coordinates are converted to
     * x- and y- grid coordinates
     * and stored in {@linkplain #coords}.
     * <p>
     * Precondition:
     * the type of the ship has been determined
     * and stored in {@linkplain #type}.
     * <p>
     * Precondition:
     * the orientation of the ship has been determined
     * and stored in {@linkplain #orientation}.
     * 
     * @return  the created ship, or null if the operation is canceled
     */
    private Ship getShip()
    {
        final String    prompt      = "Enter Ship Coordinates";
        boolean         valid       = false;
        boolean         canceled    = false;
        Ship            ship        = null;
        while ( !valid && !canceled )
        {
            coords = null;
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
                    valid = true;
                }
            }
        }
        return ship;
    }
    
    /**
     * Displays a dialog containing a given message 
     * list of errors,
     * and waits for the operator
     * to dismiss the dialog.
     * 
     * @param message   the given message
     * @param errors    the given list of errors
     */
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
