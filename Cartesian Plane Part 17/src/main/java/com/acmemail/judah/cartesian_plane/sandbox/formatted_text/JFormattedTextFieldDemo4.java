package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Application that builds on {@linkplain FormatterDemo2}.
 * The stringToValue method in the UnsignedIntFormatter class
 * changes the color of a text field's text 
 * depending on whether the text field 
 * contains valid data (black)
 * or invalid data (red). 
 * 
 * @author Jack Straub
 * 
 * @see FormatterDemo1
 * @see UnsignedIntFormatter
 */
public class JFormattedTextFieldDemo4
{
    /** Font to use in a text field when its value is committed. */
    private static final Font       committedFont   = 
        UIManager.getFont( "FormattedTextField.font" );
    /** Font to use in a text field when its value is not committed. */
    private static final Font       uncommittedFont =
        committedFont.deriveFont( Font.ITALIC );
    /** Color to use for valid text. */
    private static final Color      validColor      =
        UIManager.getColor( "FormattedTextField.foreground" );
    /** Color to use for invalid text. */
    private static final Color      invalidColor    = Color.RED;
    
    /** Activity log. */
    private static final ActivityLog  log   = new ActivityLog();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new JFormattedTextFieldDemo4() );
    }
    
    /**
     * Constructor.
     * Builds and displays the application GUI.
     */
    public JFormattedTextFieldDemo4()
    {
        String      title       = "JFormattedTextField Demo 3";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getTextPanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
        
        Dimension   frameSize   = frame.getPreferredSize();
        log.setLocation( 110 + frameSize.width, 100 );
    }
    
    /**
     * Gets the panel containing the text fields.
     *   
     * @return  the panel containing the text fields
     */
    private JPanel getTextPanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 2, 2, 3, 0 ) );
        Border  border  = 
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        panel.setBorder( border );
        
        JFormattedTextField field1  = 
            new JFormattedTextField( new UnsignedIntFormatter() );
        JFormattedTextField field2  = 
            new JFormattedTextField( new UnsignedIntFormatter() );
        field1.setValue( 0 );
        field2.setValue( 0 );
        field1.addPropertyChangeListener( "value", this::commit );
        field2.addPropertyChangeListener( "value", this::commit );
        field1.addPropertyChangeListener( "value", this::valueChanged );
        field2.addPropertyChangeListener( "value", this::valueChanged );
        field1.setHorizontalAlignment( JTextField.RIGHT );
        field2.setHorizontalAlignment( JTextField.RIGHT );
        field1.setInputVerifier( new UnsignedIntVerifier() );
        field2.setInputVerifier( new UnsignedIntVerifier() );

        panel.add( new JLabel( "First Unsigned Value:" ) );
        panel.add( field1 );
        panel.add( new JLabel( "Second Unsigned Value:" ) );
        panel.add( field2 );
        
        return panel;
    }
    
    /**
     * Gets the panel containing the control buttons.
     *   
     * @return  the panel containing the control buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel  panel   = new JPanel();
        Border  border  = 
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 1 ) );        
        panel.add( exit );
        
        return panel;
    }
    
    /**
     * Listens for changes to the "value" property
     * of a JFormattedTextField.
     * 
     * @param evt   property-change event object
     * 
     * @throws ComponentException
     *      if source of the event is not a JFormattedTextField
     *      or the property name is not "value"
     */
    private void commit( PropertyChangeEvent evt )
    {
        Object  src = evt.getSource();
        if ( !evt.getPropertyName().equals( "value" ) )
            throw new ComponentException( "Invalid property" );
        if ( !(src instanceof JFormattedTextField) )
            throw new ComponentException( "Invalid component" );
        ((JFormattedTextField)src).setFont( committedFont );
    }
    
    /**
     * Invoked when a JFormattedTextField "value"
     * property changes.
     * 
     * @param evt   object accompanying a property-changed event
     */
    private void valueChanged( PropertyChangeEvent evt )
    {
        Object  source  = evt.getSource();
        if ( !(source instanceof JFormattedTextField) )
            throw new ComponentException( "Invalid source" );
        if ( !evt.getPropertyName().equals( "value" ) )
            throw new ComponentException( "Invalid property" );  
        JFormattedTextField textField   = (JFormattedTextField)source;
        
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "text=" ).append( textField.getText() )
            .append( ",value=" ).append( textField.getValue() );
        log.append( "*** Commit" );
        log.append( bldr.toString() );
        log.append( "**************" );
    }
    
    /**
     * Formatter to ensure that all values
     * are unsigned integers.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    static class UnsignedIntFormatter extends DefaultFormatter
    {
        /**
         * Constructor.
         * Only necessary to put the formatter in insert mode.
         */
        public UnsignedIntFormatter()
        {
            setOverwriteMode( false );
        }
        
        /**
         * Converts the given text to an unsigned integer.
         * The color of the text is changed to red
         * for invalid data, 
         * and black for valid data.
         * 
         * @param the given text
         * 
         * @return the converted integer
         * 
         * @throws ParseException
         *      if the given text is not a valid unsigned integer
         */
        @Override
        public Object stringToValue( String text )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            int                 value       = -1;
            try
            {
                value = Integer.parseInt( text );
                if ( value < 0 )
                {
                    String  msg = "Value may not be negative.";
                    throw new IllegalArgumentException( msg );
                }
                fmtField.setForeground( validColor );
                if ( fmtField.getValue().equals( value ) )
                    fmtField.setFont( committedFont );
                else
                    fmtField.setFont( uncommittedFont );
            }
            catch ( IllegalArgumentException exc )
            {
                fmtField.setForeground( invalidColor );
                fmtField.setFont( uncommittedFont );
                throw new ParseException( exc.getMessage(), 0 );
            }
            
            return value;
        }
        
        /**
         * Converts the given Integer value
         * to a String formatted with group separators.
         * 
         * returns value formatted as text with group separators
         * 
         * @throws ParseException
         *      if the given value is not an Integer
         */
        @Override
        public String valueToString( Object value )
            throws ParseException
        {
            if ( !(value instanceof Integer) )
                throw new ParseException( "Non-integer value", 0 );
            String  text    = String.format( "%,d", value );
            return text;
        }
    }
    
    /**
     * InputVerifier for the managed check box.
     * 
     * @author Jack Straub
     */
    private static class UnsignedIntVerifier extends InputVerifier
    {
        /**
         * Validates the string entered into a text field.
         * If the string is empty it is ignored, 
         * and true is returned.
         * Otherwise returns true if the string
         * constitutes a valid expression.
         * 
         * @return 
         *      true if the string in a text field is empty,
         *      or constitutes a valid expression
         */
        @Override
        public boolean verify( JComponent comp )
        {
            if ( !(comp instanceof JFormattedTextField) )
                throw new ComponentException( "Invalid component" );
            JFormattedTextField textField   = (JFormattedTextField)comp;
            String              text        = textField.getText();
            boolean             status      = true;
            try
            {
                textField.getFormatter().stringToValue( text );
            }
            catch ( ParseException exc )
            {
                status = false;
            }
            return status;
        }
    }
}