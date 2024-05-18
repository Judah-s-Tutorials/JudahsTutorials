package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Application that demonstrates how to use a formatter
 * to validate the text in a JFormattedTextField.
 * In this application the text must represent
 * a valid unsigned integer.
 * 
 * @author Jack Straub
 * 
 * @see FormatterDemo2
 * @see UnsignedIntFormatter
 */
public class FormatterDemo1
{
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
        SwingUtilities.invokeLater( () -> new FormatterDemo1() );
    }
    
    /**
     * Constructor.
     * Builds and displays the application GUI.
     */
    public FormatterDemo1()
    {
        JFrame      frame       = new JFrame( "Formatter Demo 1" );
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
        field1.addPropertyChangeListener( "value", this::valueChanged );
        field2.addPropertyChangeListener( "value", this::valueChanged );
        field1.setHorizontalAlignment( JTextField.RIGHT );
        field2.setHorizontalAlignment( JTextField.RIGHT );

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
    private static class UnsignedIntFormatter extends DefaultFormatter
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
            int value   = -1;
            try
            {
                value = Integer.parseInt( text );
                if ( value < 0 )
                {
                    String  msg = "Value may not be negative.";
                    throw new IllegalArgumentException( msg );
                }
            }
            catch ( IllegalArgumentException exc )
            {
                throw new ParseException( exc.getMessage(), 0 );
            }
            
            return value;
        }
    }
}
