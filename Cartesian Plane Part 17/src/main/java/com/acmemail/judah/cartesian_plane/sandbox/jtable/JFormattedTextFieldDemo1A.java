package com.acmemail.judah.cartesian_plane.sandbox.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.OptionalInt;

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
import javax.swing.text.JTextComponent;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

public class JFormattedTextFieldDemo1A
{
//    /** Formatter for use with JFormattedTextField. */
//    private final IdentFormatter      formatter = new IdentFormatter();
    /** JFormattedTextField to display in demo GUI. */
    private final JFormattedTextField fmtField  = 
        new JFormattedTextField();
    /** 
     * Exit button to be displayed in the button panel. Declared as
     * an instance variable for the convenience of the constructor.
     */
    private final JButton exit                  = new JButton( "Exit" );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new JFormattedTextFieldDemo1A() );
    }
    
    /**
     * Constructor.
     * Initializes and displays the GUI.
     * Must be invoked from the EDT.
     */
    private JFormattedTextFieldDemo1A()
    {
        String      title       = "JFormattedTextField Demo 1A";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        Border  border      =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10);
        JPanel  cPane       = new JPanel( new BorderLayout() );
        cPane.setBorder( border );
        cPane.add( getCenterPanel(), BorderLayout.CENTER );
        
        JPanel  buttonPanel = new JPanel();
        exit.addActionListener( e -> System.exit( 0 ) );
        buttonPanel.add( exit );
        cPane.add( buttonPanel, BorderLayout.SOUTH );
        
        frame.setContentPane( cPane );
        frame.getRootPane().setDefaultButton( exit );
        frame.pack();
        frame.setLocation( 100, 100 );
        frame.setVisible( true );
    }
    
    private JPanel getCenterPanel()
    {
        JPanel              panel       = 
            new JPanel( new GridLayout( 3, 2 ) );
        fmtField.setColumns( 10 );
        fmtField.setInputVerifier( new IdentVerifier() );
        
        JTextField          textField   = new JTextField();
        textField.setEditable( false );
        
        panel.add( new JLabel( "Acc Name: " ) );
        panel.add( textField );
        panel.add( new JLabel( "Var Name: " ) );
        panel.add( fmtField );
        panel.add( new JLabel( "Dummy: " ) );
        panel.add( new JTextField() );
        return panel;
    }
    
    /**
     * Determines if a given string
     * is a valid variable name.
     * Given that underscore is an alphabetic character,
     * a valid variable name is one that
     * begins with an alphabetic character,
     * and whose remaining characters are alphanumeric.
     * 
     * @param name  the given string
     * 
     * @return  true if the given string is a valid variable name
     */
    private static boolean isIdentifier( String name )
    {
        boolean status  = false;
        int     len     = name.length();
        if ( len == 0 )
            status = true; // invalid
        else if ( !isAlpha( name.charAt( 0 ) ) )
            ; // invalid
        else
        {   
            OptionalInt optional    =
                name.chars()
                .filter( c -> !isAlphanumeric( c ) )
                .findAny();
            status = optional.isEmpty();
        }
        
        return status;
    }
    
    /**
     * Determine if a given character is alphabetic:
     * _, or [a-z] or [A-Z].
     * 
     * @param ccc   the given character
     * 
     * @return  true if the given character is alphabetic.
     */
    private static boolean isAlpha( char ccc )
    {
        boolean result  =
            ccc == '_'
            || (ccc >= 'A' && ccc <= 'Z')
            || (ccc >= 'a' && ccc <= 'z');
        return result;
    }
    
    /**
     * Determine if a given character is alphanumeric:
     * _, or [a-z], or [A-Z] or [-,9].
     * 
     * @param ccc   the given character
     * 
     * @return  true if the given character is alphanumeric.
     */
    private static boolean isAlphanumeric( int ccc )
    {
        boolean result  =
            ccc == '_'
            || (ccc >= 'A' && ccc <= 'Z')
            || (ccc >= 'a' && ccc <= 'z')
            || (ccc >= '0' && ccc <= '9');
        return result;
    }
    
    /**
     * Determines if a given string
     * is a valid variable name.
     * Given that underscore is an alphabetic character,
     * a valid variable name is one that
     * begins with an alphabetic character,
     * and whose remaining characters are alphanumeric.
     * 
     * @param name  the given string
     * 
     * @return  true if the given string is a valid variable name
     */
    @SuppressWarnings("serial")
    private static class IdentFormatter extends DefaultFormatter
    {
//        private final JFormattedTextField   textField;
//        
//        public IdentFormatter( JFormattedTextField textField )
//        {
//            this.textField = textField;
//        }
        
        @Override
        public String stringToValue( String input )
            throws ParseException
        {
            boolean status  = input == null ?
                true : isIdentifier( input.toString() );
            if ( !status )
            {
                String  msg = 
                    "\"" + input + "\" is not a valid identifier";
                throw new ParseException( msg, 0 );
            }
            
            return input;
        }
    }

    private static class IdentVerifier extends InputVerifier
    {
        @Override
        public boolean verify(JComponent comp)
        {
            if ( !(comp instanceof JTextComponent) )
                throw new ComponentException( "Invalid component" );
            JTextComponent  jtComp  = (JTextComponent)comp;
            String          input   = jtComp.getText();
            boolean         status  = isIdentifier( input );
            
            if ( !status )
                jtComp.setForeground( Color.RED );
            else
                jtComp.setForeground( Color.BLACK );
            
            return status;
        }
    }
}
