package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.components.NameValidator;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Demonstrates the value displayed in a JFormattedTextField,
 * and the value actually stored in the component.
 * The "Var Name" shows the text value entered by a operator.
 * Once the value is committed,
 * it will be shown in the "Act Name" field.
 * The value is committed when you press the enter key,
 * or click in the "Dummy" field.
 * Variable names must be valid identifiers
 * as dictated by Java.
 * If you enter an invalid name in the "Var Name" field
 * it will change color,
 * and you will not be able
 * to move the cursor to the "Dummy" field.
 * 
 * @author Jack Straub
 */
public class JFormattedTextFieldDemo1
{
    /** JFormattedTextField to display in demo GUI. */
    private final JFormattedTextField varField  = 
        new JFormattedTextField( new NameFormatter() );
    /** 
     * Exit button to be displayed in the button panel. Declared as
     * an instance variable for the convenience of the constructor.
     */
    private final JButton       exit     = new JButton( "Exit" );
    /** 
     * Text field to display the actual value of the JFormattedTextField.
     * Declared as an instance variable for the convenience of the 
     * propertyChange method.
     */
    private final JTextField    actField = new JTextField();

    /** Activity dialog for displaying feedback. */
    private final ActivityLog   log;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new JFormattedTextFieldDemo1() );
    }
    
    /**
     * Constructor.
     * Initializes and displays the GUI.
     * Must be invoked from the EDT.
     */
    private JFormattedTextFieldDemo1()
    {
        String      title       = "JFormattedTextField Demo 1A";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        log = new ActivityLog( frame );
        
        Border  border      =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10);
        JPanel  cPane       = new JPanel( new BorderLayout() );
        cPane.setBorder( border );
        cPane.add( getCenterPanel(), BorderLayout.CENTER );
        cPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( cPane );
        frame.getRootPane().setDefaultButton( exit );
        frame.pack();
        frame.setLocation( 100, 100 );
        Dimension   frameSize   = frame.getPreferredSize();
        log.setLocation( 110 + frameSize.width, 100 );
        frame.setVisible( true );
    }
    
    /**
     * Get the panel to display in the center of the content pane.
     * 
     * @return the panel to display in the center of the content pane
     */
    private JPanel getCenterPanel()
    {
        JPanel              panel       = 
            new JPanel( new GridLayout( 3, 2 ) );
        varField.setColumns( 10 );
        varField.setInputVerifier( new NameVerifier() );
        varField.addPropertyChangeListener( "value", this::propertyChange );

        actField.setEditable( false );
        
        panel.add( new JLabel( "Act Name: " ) );
        panel.add( actField );
        panel.add( new JLabel( "Var Name: " ) );
        panel.add( varField );
        panel.add( new JLabel( "Dummy: " ) );
        panel.add( new JTextField() );
        return panel;
    }

    /**
     * Get the panel containing the control buttons.
     * For displaying at the bottom of the frame.
     * 
     * @return  the panel containing the control buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel  buttonPanel = new JPanel();
        JButton print       = new JButton( "Print" );
        exit.addActionListener( e -> System.exit( 0 ) );
        print.addActionListener( this::printAction );
        buttonPanel.add( print );
        buttonPanel.add( exit );
        return buttonPanel;
    }
    
    /**
     * Log the JFormattedTextField's
     * value and text properties.
     * 
     * @param evt   object to accompany event; not used.
     */
    private void printAction( ActionEvent evt )
    {
        Object  actValue    = varField.getValue();
        String  textValue   = varField.getText();
        log.append( "Actual value: " + actValue );
        log.append( "Display value: " + textValue );
        log.append( "******************" );
    }
    
    /**
     * Catches PropertyChangeEvents for the JFormattedTextField.
     * Changes to the "value" property are logged.
     * @param evt
     */
    private void propertyChange( PropertyChangeEvent evt )
    {
        if ( evt.getPropertyName().equals( "value" ) )
        {
            Object  newVal  = varField.getValue();
            String  strVal  = 
                newVal != null ? newVal.toString() : "";
            actField.setText( strVal );
            log.append( "Value changed to: \"" + strVal + "\"" );
        }
    }
    
    /**
     * Custom InputVerifier to install on JFormattedTextField.
     * 
     * @author Jack Straub
     * 
     * @see #verify(JComponent)
     */
    private static class NameVerifier extends InputVerifier
    {
        /**
         * Verifies that the String contained in a given component
         * is a valid identifier.
         * It it is valid,
         * the color of the JFormattedTextField's text 
         * is changed to BLACK,
         * and true is returned.
         * Otherwise the color of the text
         * is changed to RED
         * and false is returned.
         * 
         * @param
         *      the given component; expected to be a JFormattedTextField
         */
        @Override
        public boolean verify(JComponent comp)
        {
            JFormattedTextField textField   = (JFormattedTextField)comp;
            String              text        = textField.getText();
            boolean             status      = 
                NameValidator.isIdentifier( text );           
            return status;
        }
    }

    /**
     * Formatter to convert the JFormattedTextField's text
     * to a value.
     * 
     * @author Jack Straub
     * 
     * @throws  ParseException  if the value is not a valid identifier
     */
    @SuppressWarnings("serial")
    private static class NameFormatter extends DefaultFormatter
    {
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
