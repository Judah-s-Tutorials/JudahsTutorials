package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class ClientPropertyDemo1
{
    private static final String fontProperty   = "fontProperty";
    private JLabel      demoLabel;
    private ButtonGroup fontButtonGroup;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new ClientPropertyDemo1().build() );
    }
    
    private void build()
    {
        JFrame      frame       = new JFrame( "Radio Button Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );

        JPanel      centerPanel = new JPanel();
        Border      border      = 
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        centerPanel.setBorder( border );
        demoLabel = new JLabel( "Radio Button Demo 1" );
        centerPanel.setPreferredSize( new Dimension( 300, 75 ) );
        centerPanel.add( demoLabel );
        contentPane.add( centerPanel, BorderLayout.CENTER );
        
        contentPane.add( getRadioButtonPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getRadioButtonPanel()
    {
        JPanel      buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        fontButtonGroup = new ButtonGroup();
        Font        origFont    = demoLabel.getFont();
        float       origSize    = origFont.getSize();
        
        Stream.of( .5f, .75f, 1f, 1.5f, 2f )
            .forEach( s -> {
                JRadioButton    button      = new JRadioButton();
                Font            nextFont    = 
                    origFont.deriveFont( s * origSize );
                button.putClientProperty( fontProperty, nextFont );
            button.addActionListener( this::fontButtonActionPerformed );
            fontButtonGroup.add( button );
            buttonPanel.add( button );
        });

        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = new BoxLayout( mainPanel, BoxLayout.X_AXIS );
        mainPanel.setLayout( mainLayout );
        mainPanel.add( new JLabel( "Smaller" ) );
        mainPanel.add( buttonPanel );
        mainPanel.add( new JLabel( "Larger" ) );
        
        Border  empty       =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border  etched      = 
            BorderFactory.createEtchedBorder( EtchedBorder.RAISED );
        Border  compound    = 
            BorderFactory.createCompoundBorder( empty, etched );
        Border  mainBorder  =
            BorderFactory.createCompoundBorder( compound, empty );
        Border  titled      =
            BorderFactory.createTitledBorder( "Choose Font Size" );
        mainPanel.setBorder( mainBorder );
        buttonPanel.setBorder( titled );

        return mainPanel;
    }
    
    private void fontButtonActionPerformed( ActionEvent evt )
    {
        final String    invSource   = "Invalid source component";
        final String    valNotFound = "Client property not found";
        final String    invVal      = "Invalid client property";
        Object  source  = evt.getSource();
        if ( !(source instanceof JComponent ) )
            throw new IllegalArgumentException( invSource );
        JComponent  comp        = (JComponent)source;
        Object      clientVal   = comp.getClientProperty( fontProperty );
        if ( clientVal == null )
            throw new IllegalArgumentException( valNotFound );
        if ( !(clientVal instanceof Font) )
            throw new IllegalArgumentException( invVal );
        
        demoLabel.setFont( (Font)clientVal );
    }
}
