package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Encapsulates a set of components
 * for selecting a color,
 * using a JFileChooser
 * or by entering
 * an RGB value
 * into a text field.
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
 * By default
 * the components
 * are <em>not</em> assembled
 * into a single window.
 * This way the user
 * can obtain and position
 * each component as desired.
 * Alternatively,
 * the method {@link #getPanel()}
 * will return a single window
 * containing all components.
 * The components are:
 * </p>
 * <ul>
 * <li>
 *     colorButton: type JButton<br>
 *     when pushed,
 *     this button
 *     will display a ColorSelector
 *     (a blocking dialog).
 *     If the operator
 *     selects a color
 *     and pushes the dialog's OK button,
 *     the selected color
 *     will be reflected 
 *     in the text editor 
 *     and the feedback panel.
 * </li>
 * <li>
 *     textEditor: type JTextField<br>
 *     this component
 *     may be used
 *     to enter the integer RGB value
 *     of a color.
 *     Leading and trailing spaces
 *     are ignored.
 *     If the text content
 *     begins with "0x" or "#"
 *     the remainder of the text
 *     is interpreted as hexadecimal.
 *     The text is not examined
 *     until its value is "committed";
 *     this happens
 *     when the operator presses enter
 *     while the text field has focus,
 *     or the program
 *     calls the {@link #getColor} method.
 *     If an invalid value is entered,
 *     the displayed text
 *     changes to an error message
 *     upon commit.
 *     Once committed,
 *     the color encoded in the field
 *     will be displayed
 *     in the feedback component.
 *     When the {@link #getColor} method is invoked,
 *     the color returned
 *     is the color encoded
 *     by this field.
 * </li>
 * <li>
 *     feedback: type JPanel<br>
 *     this component
 *     displays the color
 *     encoded in the text editor.
 *     If the text editor
 *     does not encode
 *     a valid RGB integer,
 *     the default color is displayed.
 * </li>
 * </ul>
 * @author Jack Straub
 */
public class ColorEditor
{
    public static final String  COLOR_BUTTON_LABEL  = "Color";
    public static final String  TEXT_EDITOR_NAME    = "Text Editor";
    public static final String  FEEDBACK_NAME       = "Feedback";
    
    /** The default color, encoded as an integer string. */
    private static final String defColorString  = "0x0000FF";
    /** The default color. */
    private static final Color  defColor        = 
        Color.decode( defColorString );
    
    /** The colorButton component. */
    private final JButton       colorButton = 
        new JButton( COLOR_BUTTON_LABEL );
    /** The textEditor component. */
    private final JTextField    textEditor  = 
        new JTextField( defColorString, 10 );
    /** The feedback component. */
    private final JPanel        feedback    = new JPanel();
    /** ColorSelector dialog. */
    private final ColorSelector colorDialog = 
        new ColorSelector( defColor );
    
    /** List of ActionListeners. */
    private final List<ActionListener>  actionListeners = 
        new ArrayList<>();

    /**
     * Constructor.
     * Instantiates all components.
     * Sets the font
     * of the textEditor to MONSPACED.
     * Sets the preferred size 
     * of the feedback component
     * to that of the textEditor component..
     */
    public ColorEditor()
    {
        Dimension   editSize    = textEditor.getPreferredSize();
        feedback.setPreferredSize( editSize );
        
        Font    font    = textEditor.getFont();
        int     size    = font.getSize();
        int     style   = font.getStyle();
        String  name    = Font.MONOSPACED;
        Font    newFont = new Font( name, style, size );
        textEditor.setFont( newFont );
        
        feedback.setBackground( defColor );
        textEditor.addActionListener( e -> editColor() );
        colorButton.addActionListener( e -> selectColor() );
        
        feedback.setName( FEEDBACK_NAME );
        textEditor.setName( TEXT_EDITOR_NAME );
        colorButton.setName( COLOR_BUTTON_LABEL );
    }
    
    /**
     * Adds a given listener
     * to the list of ActionListeners
     * that are notified
     * when the selected color changes.
     * This happens
     * whenever changes to the text editor
     * are committed.
     * Note that
     * selecting a color
     * from the color selector
     * results in a change
     * being committed to the text editor.
     * 
     * @param listener  the given listener
     */
    public void addActionListener( ActionListener listener )
    {
        actionListeners.add(listener );
    }
    
    /**
     * Removes a given listener
     * from the list
     * of ActionListeners.
     * If the given listener
     * is not in the list
     * the operation
     * is silently ignored.
     * 
     * @param listener  the given listener
     */
    public void removeActionListener( ActionListener listener )
    {
        actionListeners.remove( listener );
    }
    
    /**
     * Returns a JPanel
     * with a FlowLayout
     * containing, in order, the 
     * colorButton, textEditor and feedback components.
     * 
     * @return a panel containing the components of this ColorEditor.
     */
    public JPanel getPanel()
    {
        JPanel  panel   = new JPanel();
        panel.add( colorButton );
        panel.add( textEditor );
        panel.add( feedback );
        return panel;
    }
    
    /**
     * Gets the textEditor component.
     * 
     * @return the textEditor component
     */
    public JTextField getTextEditor()
    {
        return textEditor;
    }
    
    /**
     * Gets the colorButton component.
     * 
     * @return the colorButton component
     */
    public JButton getColorButton()
    {
        return colorButton;
    }
    
    /**
     * Gets the feedback component.
     * 
     * @return the feedback component
     */
    public JPanel getFeedback()
    {
        return feedback;
    }
    
    /**
     * Returns an Optional
     * containing the color
     * encoded in the textEditor component.
     * If the text editor 
     * does not encode a valid color,
     * and empty Optional is returned.
     * 
     * @return  Optional containing the current color,
     *          or an empty Optional if there is no
     *          valid color
     */
    public Optional<Color> getColor()
    {
        Optional<Color> optColor    = Optional.empty();
        try
        {
            Color   color   = Color.decode( textEditor.getText().trim() );
            optColor = Optional.of( color );
        }
        catch ( NumberFormatException exc )
        {
            // optColor is already set to an empty Optional;
            // no further error processing is required.
        }
        
        return optColor;
    }
    
    /**
     * Sets the currently selected color
     * to a given color.
     * The text editor
     * and feedback window
     * are both updated.
     * 
     * @param color the given color
     */
    public void setColor( Color color )
    {
        int     iColor  = color.getRGB() & 0x00FFFFFF;
        String  sColor  = String.format( "0x%06x", iColor );
        textEditor.setText( sColor );
        feedback.setBackground( color );
        fireActionListeners();
    }
    
    /**
     * Displays the ColorSelector
     * and waits for the operator
     * to choose OK or Cancel.
     * If the operator chooses OK,
     * the selected color
     * is encoded in the textEditor,
     * and displayed in the feedback component.
     */
    private void selectColor()
    {
        Color   color   = colorDialog.showDialog();
        if ( color != null )
            setColor( color );
    }
    
    /**
     * Obtains the integer RGB value
     * stored in the textEditor component,
     * and attempts to interpret it
     * as a color.
     * If the encapsulated value
     * is valid,
     * it is reflected
     * in the feedback component;
     * if invalid,
     * an error message is displayed
     * in the textEditor,
     * and the feedback color
     * is changed to the default.
     */
    private void editColor()
    {
        Optional<Color> optColor    = getColor();
        if ( optColor.isPresent() )
            setColor( optColor.get() );
        else
        {
            textEditor.setText( "#Error" );
            feedback.setBackground( defColor );
            fireActionListeners();
        }
    }
    
    /**
     * Notifies all listeners 
     * in the list of ActionListeners.
     */
    private void fireActionListeners()
    {
        ActionEvent event   = 
            new ActionEvent( textEditor, ActionEvent.ACTION_FIRST, null );
        actionListeners.forEach( l -> l.actionPerformed( event ));
    }
}
