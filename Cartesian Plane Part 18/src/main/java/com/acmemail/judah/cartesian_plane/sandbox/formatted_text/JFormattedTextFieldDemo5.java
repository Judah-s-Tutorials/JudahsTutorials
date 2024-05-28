package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultFormatter;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.Result;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Application that demonstrates how we can use
 * JFormattedTextFields to format and validate
 * an expression from our Equation facility.
 * Pushing the print button
 * logs the current state
 * of the formatted text field
 * and the encapsulated equation.
 * You can also invoke the print facility
 * by typing alt-R when 
 * the text field has focus.
 * 
 * @author Jack Straub
 */
public class JFormattedTextFieldDemo5
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
    /** Initial expression to be stored in the "y=" text field. */
    private static final String     yExpr           = "mx + b";
    
    /** Activity log. */
    private final ActivityLog           log;
    
    /** Text field to hold y-expression. */
    private final   JFormattedTextField field1  = 
        new JFormattedTextField( new ExprFormatter() );
    /** Source equation for y-expression. */
    private final   Equation    equation    = new Exp4jEquation();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new JFormattedTextFieldDemo5() );
    }
    
    /**
     * Constructor.
     * Builds and displays the application GUI.
     * Initializes the encapsulated equation.
     */
    public JFormattedTextFieldDemo5()
    {
        // Declare variables to be used in equation
        equation.setVar( "x", 0 );
        equation.setVar( "y", 0 );
        equation.setVar( "a", 0 );
        equation.setVar( "b", 0 );
        equation.setVar( "c", 0 );
        equation.setVar( "m", 0 );
        // Initialize the y-expression
        equation.setYExpression( yExpr );
        
        String      title       = "JFormattedTextField Demo 5";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getTextPanel(), BorderLayout.CENTER );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
        
        log = new ActivityLog( frame );
        Dimension   frameSize   = frame.getPreferredSize();
        log.setLocation( 110 + frameSize.width, 100 );
    }
    
    /**
     * Gets the panel containing the text field.
     *   
     * @return  the panel containing the text field
     */
    private JPanel getTextPanel()
    {
        JPanel  panel   = new JPanel( new GridLayout( 1, 2, 3, 0 ) );
        Border  border  = 
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        panel.setBorder( border );
        
        field1.setValue( equation.getYExpression() );
        field1.addPropertyChangeListener( "value", this::commit );
        field1.addPropertyChangeListener( "value", this::valueChanged );
        field1.setInputVerifier( new ExprVerifier() );
        
        KeyAdapter  kAdapter    = new KeyAdapter() {
            @Override
            public void keyPressed( KeyEvent evt )
            {
                int keyCode = evt.getKeyCode();
                if ( evt.isAltDown() && keyCode == KeyEvent.VK_R )
                    printAction( null );
            }
        };

        field1.setToolTipText( "alt-R to print" );
        field1.addKeyListener( kAdapter );

        panel.add( new JLabel( "y=" ) );
        panel.add( field1 );
        
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
        JButton print   = new JButton( "Print" );
        print.addActionListener( this::printAction );        
        exit.addActionListener( e -> System.exit( 1 ) );        
        panel.add( print );
        panel.add( exit );
        
        return panel;
    }
    
    /**
     * Listens for commits in the JFormattedTextField.
     * Commits occur when the text field's "value" property
     * changes.
     * 
     * @param evt   property-change event object
     * 
     * @throws ComponentException
     *      if the text field does not contain a valid expression
     */
    private void commit( PropertyChangeEvent evt )
    {
        Object  src = evt.getSource();
        if ( src instanceof JFormattedTextField )
        {
            JFormattedTextField textField   = (JFormattedTextField)src;
            Result              result      =
                equation.setYExpression( textField.getText() );
            if ( !result.isSuccess() )
                throw new ComponentException( "Invalid expression" );
            textField.setFont( committedFont );
        }
    }
    
    /**
     * Invoked when a JFormattedTextField "value"
     * property changes.
     * Logs the current state of the application.
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
     * Invoked when the operator clicks the "Print" button
     * or types alt-R in the JFormattedTextField.
     * 
     * @param evt   object describing the action; not used
     */
    private void printAction( ActionEvent evt )
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "<span style='color: red;'>")
            .append( "***" ).append( "<br>" )
            .append( "yExpression: " )
            .append( equation.getYExpression() ).append( "<br>" )
            .append( "text: " ).append( field1.getText() ).append( "," )
            .append( "value: " ).append( field1.getValue() )
            .append( "<br>" ).append( "***************" )
            .append( "</span>" );
        log.append( bldr.toString() );
    }
    
    /**
     * Formatter to ensure that the JFormattedTextField's text
     * is a valid expression.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    class ExprFormatter extends DefaultFormatter
    {
        /**
         * Constructor.
         * Only necessary to put the formatter in insert mode.
         */
        public ExprFormatter()
        {
            setOverwriteMode( false );
        }
        
        /**
         * Validates the text field's text,
         * which is expected to be a valid expression.
         * The color of the text is changed to red
         * for invalid data, 
         * and black for valid data.
         * 
         * @param the given text
         * 
         * @return the given text
         * 
         * @throws ParseException
         *      if the given text is not a valid expression
         */
        @Override
        public Object stringToValue( String text )
            throws ParseException
        {
            JFormattedTextField fmtField    = getFormattedTextField();
            if ( !equation.isValidExpression( text ) )
            {
                fmtField.setForeground( invalidColor );
                fmtField.setFont( uncommittedFont );
                throw new ParseException( "Invalid expression", 0 );
            }
            fmtField.setForeground( validColor );
            if ( fmtField.getValue().equals( text ) )
                fmtField.setFont( committedFont );
            else
                fmtField.setFont( uncommittedFont );
            
            return text;
        }
    }
    
    /**
     * InputVerifier for the formatted text field.
     * 
     * @author Jack Straub
     */
    private static class ExprVerifier extends InputVerifier
    {
        /**
         * Validates the string entered into a text field.
         * The text is expected to be a valid expression.
         * Returns true if the text is valid.
         * 
         * @return 
         *      true if the text field's text 
         *      constitutes a valid expression
         *      
         * @throws ComponentException
         *      if the source of the text is not a JFormattedTextField
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