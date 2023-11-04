package com.acmemail.judah.sandbox.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class RadioButtonDemo1
{
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
        SwingUtilities.invokeLater( () -> new RadioButtonDemo1().build() );
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
        float   origSize    = origFont.getSize();
        Font        smallerFont = origFont.deriveFont( origSize * .75f );
        Font        smallestFont = origFont.deriveFont( origSize * .5f );
        Font        largerFont = origFont.deriveFont( origSize * 1.5f );
        Font        largestFont = origFont.deriveFont( origSize * 2 );
        Stream.of( 
            smallestFont, 
            smallerFont, 
            origFont, 
            largerFont, 
            largestFont
        ).forEach( f -> {
            PRadioButton<Font>    button  = new PRadioButton<>( f );
            button.addActionListener( e -> demoLabel.setFont( button.get() ) );
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
}
