package com.acmemail.judah.cartesian_plane.sandbox.formatted_text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;
import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

/**
 * Simple program to display an object
 * of the PlotPanel class.
 * The panel is set up with an equation
 * in which variables x, y, a, b, and c
 * have been declared
 * (i.e., you can experiment with expressions
 * using these variable names).
 * There options to start a new equation
 * or close the current equation.
 * 
 * @author Jack Straub
 */
public class JFormattedTextFieldDemo1
{
    /** Activity log. */
    private static ActivityLog  log;
    
    /** Demo text field. */
    private final JFormattedTextField   field1  = 
        new JFormattedTextField();
    /** Demo text field. */
    private final JFormattedTextField   field2  = 
        new JFormattedTextField();
    /** Demo text field. */
    private final JFormattedTextField   field3  = 
        new JFormattedTextField();
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
     * Creates and displays the main application frame.
     */
    public JFormattedTextFieldDemo1()
    {
        String      title       = "JFormattedTextField Demo 1";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  centerPanel = new JPanel( new BorderLayout() );
        centerPanel.add( getTextPanel(), BorderLayout.NORTH );
        contentPane.add( getLeftPanel(), BorderLayout.WEST );
        contentPane.add( centerPanel, BorderLayout.CENTER );

        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
        
        log = new ActivityLog( frame );
        Dimension   frameSize   = frame.getPreferredSize();
        log.setLocation( 110 + frameSize.width, 100 );
    }
    
    private JPanel getLeftPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        panel.setBorder( border );
        panel.setLayout( layout );
        panel.add( getButtonPanel() );
        panel.add( getOptionsPanel() );
        return panel;
    }
    
    /**
     * Gets the panel containing the Exit and Print buttons.
     * 
     * @return  the panel containing the Exit and Print buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        JButton print           = new JButton( "Print" );
        panel.add( print );
        print.addActionListener( this::printAction );
        
        JButton exit            = new JButton( "Exit" );
        panel.add( exit );
        exit.addActionListener( e -> System.exit( 1 ) );
        
        return panel;
    }
    
    private JPanel getOptionsPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        String      title   = "Focus Lost Behavior";
        Border      lineB   = BorderFactory.createLineBorder( Color.BLACK );
        Border      border  = BorderFactory.createTitledBorder( lineB, title );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        PButtonGroup<Integer>   group   = new PButtonGroup<>();
        Stream.of( "REVERT", "COMMIT", "COMMIT_OR_REVERT", "PERSIST" )
            .map( s -> 
                new PRadioButton<Integer>( getTextFieldOption( s ), s )
            )
            .peek( b -> b.addActionListener( this::selectAction ) )
            .peek( group::add )
            .forEach( panel::add );
        group.selectIndex( 0 );
        return panel;
    }
    
    private void selectAction( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( !(source instanceof PRadioButton<?> ) )
            throw new ComponentException( "Invalid source" );
        @SuppressWarnings("unchecked")
        PRadioButton<Integer>   button  = (PRadioButton<Integer>)source;
        int                     option  = button.get();
        field1.setFocusLostBehavior( option );
        field2.setFocusLostBehavior( option );
        field3.setFocusLostBehavior( option );
    }
    
    /**
     * Gets the panel containing the text fields.
     * 
     * @return  the panel containing the text fields
     */
    private JPanel getTextPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        JPanel      fPanel1 = new JPanel();
        fPanel1.add( new JLabel( "Field 1" ) );
        fPanel1.add( field1 );
        panel.add( fPanel1 );

        JPanel      fPanel2 = new JPanel();
        fPanel2.add( new JLabel( "Field 2" ) );
        fPanel2.add( field2 );
        panel.add( fPanel2 );

        JPanel      fPanel3 = new JPanel();
        fPanel3.add( new JLabel( "Field 3" ) );
        fPanel3.add( field3 );
        panel.add( fPanel3 );
        
        KeyAdapter  kAdapter    = new KeyAdapter() {
            @Override
            public void keyPressed( KeyEvent evt )
            {
                int keyCode = evt.getKeyCode();
                if ( evt.isAltDown() && keyCode == KeyEvent.VK_R )
                    printAction( null );
            }
        };

        field1.setValue( Integer.valueOf( 0 ) );
        field2.setValue( Double.valueOf( 3.14 ) );
        field3.setValue( Boolean.valueOf( false ) );
        Stream.of( field1, field2, field3 )
            .peek( f -> 
                f.addPropertyChangeListener( "value", this::valueChanged )
            )
            .peek( f -> f.setToolTipText( "alt-R to print" ) )
            .peek( f -> f.setColumns( 10 ) )
            .forEach( f -> f.addKeyListener( kAdapter ) );      
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
        
        String  data    = getFieldData( (JFormattedTextField)source );
        log.append( "*** Commit" );
        log.append( data );
        log.append( "**************" );
    }
    
    /**
     * Prints the current state of the two sample JFormattedTextFields
     * 
     * @param evt   action event object; not used
     */
    private void printAction( ActionEvent evt )
    {
        log.append( "*** Print" );
        log.append( getFieldData( field1 ) );
        log.append( getFieldData( field2 ) );
        log.append( getFieldData( field3 ) );
        log.append( "**************" );
    }
    
    /**
     * Formats a string identifying the current state
     * of the given JFormattedTextField.
     * 
     * @param textField the given JFormattedTextField
     * 
     * @return
     *      string identifying the current state
     *      of the given JFormattedTextField
     */
    private String 
    getFieldData( JFormattedTextField textField )
    {
        String  fieldID = null;
        if ( textField == field1 )
            fieldID = "Field 1: ";
        else if ( textField == field2 )
            fieldID = "Field 2: ";
        else if ( textField == field3 )
            fieldID = "Field 3: ";
        else
            throw new ComponentException( "Invalid text field" );
        
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( fieldID ).append( ": " )
            .append( "text=" ).append( textField.getText() ).append( "," )
            .append( "value=" ).append( textField.getValue() );
        return bldr.toString();
    }
    
    private int getTextFieldOption( String name )
    {
        int option  = 0;
        try
        {
            Class<?>    clazz   = JFormattedTextField.class;
            Field       field   = clazz.getField( name );  
            option = field.getInt( null );
        }
        catch ( 
            NoSuchFieldException 
            | SecurityException 
            | IllegalAccessException exc )
        {
            exc.printStackTrace();
        }
        return option;
    }
}
