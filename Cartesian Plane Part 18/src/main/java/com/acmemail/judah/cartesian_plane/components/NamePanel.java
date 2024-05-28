package com.acmemail.judah.cartesian_plane.components;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * This is a simple panel for entering the name of an equation.
 * It consists of a label, and a text field for editing the name.
 * The text field is a JFormattedTextField
 * that displays uncommitted values in italics.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class NamePanel extends JPanel
{
    /** Font to use in a text field when its value is committed. */
    private static final Font       committedFont       = 
        UIManager.getFont( "FormattedTextField.font" );
    /** Font to use in a text field when its value is not committed. */
    private static final Font       uncommittedFont     =
        committedFont.deriveFont( Font.ITALIC );
    /** Reduces typing when accessing PropertyManager singleton. */
    private static final PropertyManager    pMgr        =
        PropertyManager.INSTANCE;

    /** Text field to hold the equation name. */
    private final JFormattedTextField       nameField   =
        new JFormattedTextField( new FieldFormatter() );
    
    /** The currently open equation (may be null). */
    private Equation    equation    = null;
    
    /**
     * Constructor.
     * Fully configures the panel.
     */
    public NamePanel()
    {
        BoxLayout   layout  = new BoxLayout( this, BoxLayout.Y_AXIS );
        Border      border  =
            BorderFactory.createEmptyBorder( 3, 3, 0, 3 );
        setLayout( layout );
        setBorder( border );
        add( new JLabel( "Eq. Name" ) );
        add( nameField );
        nameField.addPropertyChangeListener( "value", this::commit );
        nameField.setEnabled( false );
    }
    
    /**
     * Loads the currently open equation.
     * Pass null to indicate that there is no currently open equation.
     * 
     * @param equation  currently open equation; may be null
     */
    public void load( Equation equation )
    {
        this.equation = equation;
        if ( equation != null )
        {
            nameField.setEnabled( true );
            nameField.setValue( equation.getName() );
        }
        else
            nameField.setEnabled( false );
    }
    
    /**
     * This method fires
     * when the text field's
     * value property changes.
     * 
     * @param evt   object that accompanies a property change event
     */
    private void commit( PropertyChangeEvent evt )
    {
        if ( equation != null )
        {
            equation.setName( evt.getNewValue().toString() );
            pMgr.setProperty( CPConstants.DM_MODIFIED_PN, true );
            nameField.setFont( committedFont );
        }
    }
    
    /**
     * Formatter installed in the JFormattedTextField.
     * It's main purpose is to set the font of the nameField
     * depending on whether or not
     * the nameField's value has been committed.
     * 
     * @author Jack Straub
     */
    private static class FieldFormatter extends DefaultFormatter
    {
        /**
         * Constructor.
         * Turns off overwrite mode.
         */
        public FieldFormatter()
        {
            setOverwriteMode( false );
        }
        
        /**
         * "Converts" the given string
         * to the type of the associated text field's
         * value property.
         * Formats the text field's text
         * to indicate whether or not
         * the value has been committed.
         * 
         * @param   str     the given string
         * 
         * @return  the converted object
         */
        @Override
        public Object stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            Object              value       = null;
            if ( !str.isEmpty() )
            {
                value = fmtField.getValue();
                if ( str.equals( value ) )
                    fmtField.setFont( committedFont );
                else
                    fmtField.setFont( uncommittedFont );
            }
            return str;
        }
    }
}
