package com.acmemail.judah.cartesian_plane.components;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;

/**
 * An object of this type is used
 * as a cell editor for a JFormattedTextField.
 * More specifically,
 * it is used to validate strings
 * that represent identifiers.
 * If forces an String to conform to the Java rules
 * for an identifier:
 * <ul>
 * <li>Must begin with an underscore or alphabetic character;</>
 * <li>
 *      Subsequent character must be underscores
 *      or alphanumeric characters.
 * </li>
 * </ul>
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class NameEditor extends DefaultCellEditor
{
    /**
     * Constructor.
     * Instantiates and configures
     * the JFormattedTextField to be used
     * as a cell editor.
     */
    public NameEditor()
    {
        super( new JFormattedTextField( new NameFormatter() ) );
        JFormattedTextField textField   =
            (JFormattedTextField)getComponent();
        textField.setInputVerifier( new IdentVerifier() );
    }
    
    /**
     * Indicates when it is acceptable
     * for editing of a cell to cease.
     * Editing may cease if the value
     * contained in the editor component
     * is valid.
     * 
     * @return  
     *      true, if the value contained 
     *      in the editor component is valid
     */
    @Override
    public boolean stopCellEditing()
    {
        Object  oValue  = getCellEditorValue();
        boolean status  = false;
        if ( NameValidator.isIdentifier( (String)oValue ) )
            status = super.stopCellEditing();
        
        JFormattedTextField field   = (JFormattedTextField)getComponent();
        System.out.println( field.getValue() );
        
        return status;
    }
    
    /**
     * Serves as an InputVerifier 
     * for the encapsulated JFormattedTextField.
     * Input is valid if the encapsulated component
     * contains a correctly formatted identifier.
     * 
     * @author Jack Straub
     */
    private static class IdentVerifier extends InputVerifier
    {
        /**
         * Returns true if the given component
         * contains a valid identifier.
         * 
         * @return
         *      true if the given component
         *      contains a valid identifier
         */
        @Override
        public boolean verify(JComponent comp)
        {
            JTextComponent  jtComp  = (JTextComponent)comp;
            String          input   = jtComp.getText();
            boolean         status  = NameValidator.isIdentifier( input );
            
            return status;
        }
    }
    
    /**
     * An object of this class
     * serves as a formatter
     * for a JFormattedTextField configured
     * to edit an identifier.
     * 
     * @author Jack Straub
     */
    private static class NameFormatter extends DefaultFormatter
    {
        /**
         * Constructor.
         * Sets the formatter's overwrite mode to false.
         */
        public NameFormatter()
        {
            this.setOverwriteMode( false );
        }
        
        /**
         * Validates a given string.
         * If the string is valid it is returned,
         * otherwise a ParseException is thrown.
         * 
         * @return  the given string, if it is valid
         * 
         * @throws ParseException if the given string is invalid
         */
        @Override
        public String stringToValue( String str )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            if ( !NameValidator.isIdentifier( str ) )
            {
                fmtField.setForeground( Color.RED );
                throw new ParseException( "Invalid name", 0 );
            }
            fmtField.setForeground( Color.BLACK );
            return str;
        }
    }
}
