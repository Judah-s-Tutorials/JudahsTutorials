package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

/**
 * Dialog to allow a user
 * to select a color.
 * <p>
 * THIS CLASS
 * INSTANTIATES AND CONFIGURES
 * SWING COMPONENTS;
 * IT MUST BE INSTANTIATED
 * IN THE CONTEXT OF THE
 * AWT EVENT DISPATCH THREAD.
 * (See, for example,
 * SwingUtilities.invokeLater, SwingUtilities.invokeAndWait).
 * </p>
 * <p>
 * Most of the work
 * done by this class
 * is accomplished via 
 * a <em>JFileChooser</em> instance.
 * </p>
 * 
 * @author Jack Straub
 * 
 * @see #showDialog()
 */
public class ColorSelector
{
    /** Default dialog title. */
    private static final String defTitle    = "Color Selector";
    
    /** The dialog created by JColorChooser.makeDialog. */
    private final JDialog       dialog;

    /** 
     * The color selected by the operator when the dialog's OK
     * button is pressed. If the cancel button is pressed this
     * value will be null. It is set in the ActionListeners passed
     * to JColorChooser.makeDialog.
     */
    private Color               selectedColor;
    
    /**
     * Default constructor.
     * Creates a ColorSelector
     * with no parent,
     * a default title
     * and an initial color of blue.
     * 
     * @see #ColorSelector(Window, String, Color)
     */
    public ColorSelector()
    {
        this( null, defTitle, Color.BLUE );
    }
    
    /**
     * Default constructor.
     * Creates a ColorSelector
     * with no parent,
     * a default title
     * and the given initial color.
     * 
     * @param color the given initial color
     * 
     * @see #ColorSelector(Window, String, Color)
     */
    public ColorSelector( Color color )
    {
        this( null, defTitle, color );
    }
    
    /**
     * Constructor.
     * Creates a ColorSelector
     * with the given owner, title
     * and initial color.
     * 
     * @param owner the given owner
     * @param title the given title
     * @param color the given color
     */
    public ColorSelector( Window owner, String title, Color color )
    {
        JColorChooser colorPane = new JColorChooser();
        dialog =
            JColorChooser.createDialog(
                owner, 
                title, 
                true, 
                colorPane, 
                e -> selectedColor = colorPane.getColor(), 
                e -> selectedColor = null
            );
        colorPane.setColor( color );
        Dimension   dialogSize  = dialog.getPreferredSize();
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        int     xco     = screenSize.width / 2 - dialogSize.width / 2;
        int     yco     = screenSize.height / 2 - dialogSize.height / 2;
        dialog.setLocation( xco, yco );
    }
    
    /**
     * Makes this dialog visible
     * and blocks
     * until it is dismissed
     * by the operator.
     * If the operator selects OK
     * the selected color will be returned,
     * otherwise null is returned.
     * 
     * @return the selected color if the OK button is pressed,
     *         otherwise null
     */
    public Color showDialog()
    {
        dialog.setVisible( true );
        return selectedColor;
    }
}
