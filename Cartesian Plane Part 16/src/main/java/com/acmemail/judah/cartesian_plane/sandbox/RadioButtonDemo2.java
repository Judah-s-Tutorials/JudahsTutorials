package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class RadioButtonDemo2
{
    private JLabel      demoLabel;
    private JPanel      demoPanel;
    private ButtonGroup fontButtonGroup;
    private ButtonGroup fgColorButtonGroup;
    private ButtonGroup bgColorButtonGroup;
    private Font        origFont;
    private Font        smallerFont;
    private Font        smallestFont;
    private Font        largerFont;
    private Font        largestFont;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new RadioButtonDemo2().build() );
    }
    
    private void build()
    {
        JFrame      frame       = new JFrame( "Radio Button Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );

        demoPanel = new JPanel();
        Border      border      = 
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        demoPanel.setBorder( border );
        demoLabel = new JLabel( "Radio Button Demo 2" );
        demoPanel.setPreferredSize( new Dimension( 300, 75 ) );
        demoPanel.add( demoLabel );
        contentPane.add( demoPanel, BorderLayout.CENTER );
        
        contentPane.add( getButtonPanels(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getButtonPanels()
    {
        JPanel  panel   = new JPanel( new GridLayout( 3,1 ) );
        panel.add( getFontButtonPanel() );
        panel.add( getLabelColorPanel() );
        panel.add( getBGColorPanel() );
        return panel;
    }
    
    private JPanel getFontButtonPanel()
    {
        JPanel      buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        fontButtonGroup = new ButtonGroup();
        origFont    = demoLabel.getFont();
        float   origSize    = origFont.getSize();
        smallerFont = origFont.deriveFont( origSize * .75f );
        smallestFont = origFont.deriveFont( origSize * .5f );
        largerFont = origFont.deriveFont( origSize * 1.5f );
        largestFont = origFont.deriveFont( origSize * 2 );
        Stream.of( 
            smallestFont, 
            smallerFont, 
            origFont, 
            largerFont, 
            largestFont
        ).forEach( f -> {
            JRadioButton    button  = new JRadioButton();
            button.addActionListener( e -> demoLabel.setFont( f ) );
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
            BorderFactory.createTitledBorder( "Font Size" );
        buttonPanel.setBorder( titled );
        mainPanel.setBorder( mainBorder );
        
        return mainPanel;
    }
    
    private JPanel getLabelColorPanel()
    {
        JPanel      buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        fgColorButtonGroup = new ButtonGroup();
        Color       origColor       = new Color( 0xCD5C5C );
        Color       lighterColor    = new Color( 0xE9967A );
        Color       lightestColor   = new Color( 0xFFA07A );
        Color       darkerColor     = new Color( 0xB22222 );
        Color       darkestColor    = new Color( 0x8B0000 );
        origFont    = demoLabel.getFont();
        Stream.of( 
            lightestColor, 
            lighterColor, 
            origColor, 
            darkerColor, 
            darkestColor
        ).forEach( c -> {
            JRadioButton    button  = new JRadioButton();
            button.addActionListener( e -> demoLabel.setForeground( c ) );
            fgColorButtonGroup.add( button );
            buttonPanel.add( button );
        });

        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = new BoxLayout( mainPanel, BoxLayout.X_AXIS );
        mainPanel.setLayout( mainLayout );
        mainPanel.add( new JLabel( "Lighter" ) );
        mainPanel.add( buttonPanel );
        mainPanel.add( new JLabel( "Darker" ) );
        
        Border  empty       =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border  etched      = 
            BorderFactory.createEtchedBorder( EtchedBorder.RAISED );
        Border  compound    = 
            BorderFactory.createCompoundBorder( empty, etched );
        Border  mainBorder  =
            BorderFactory.createCompoundBorder( compound, empty );
        Border  titled      =
            BorderFactory.createTitledBorder( "Foreground Color" );
        buttonPanel.setBorder( titled );
        mainPanel.setBorder( mainBorder );
        
        demoLabel.setForeground( origColor );
        
        return mainPanel;
    }
    
    private JPanel getBGColorPanel()
    {
        JPanel      buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        fgColorButtonGroup = new ButtonGroup();
        Color       origColor       = new Color( 0x32CD32 );
        Color       lighterColor    = new Color( 0x00FF00 );
        Color       lightestColor   = new Color( 0xADFF2F );
        Color       darkerColor     = new Color( 0x3CB371 );
        Color       darkestColor    = new Color( 0x006400 );
        origFont    = demoLabel.getFont();
        Stream.of( 
            lightestColor, 
            lighterColor, 
            origColor, 
            darkerColor, 
            darkestColor
        ).forEach( c -> {
            JRadioButton    button  = new JRadioButton();
            button.addActionListener( e -> demoPanel.setBackground( c ) );
            fgColorButtonGroup.add( button );
            buttonPanel.add( button );
        });

        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = new BoxLayout( mainPanel, BoxLayout.X_AXIS );
        mainPanel.setLayout( mainLayout );
        mainPanel.add( new JLabel( "Lighter" ) );
        mainPanel.add( buttonPanel );
        mainPanel.add( new JLabel( "Darker" ) );
        
        Border  empty       =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border  etched      = 
            BorderFactory.createEtchedBorder( EtchedBorder.RAISED );
        Border  compound    = 
            BorderFactory.createCompoundBorder( empty, etched );
        Border  titled      =
            BorderFactory.createTitledBorder( "Background Color" );
        Border  mainBorder  =
            BorderFactory.createCompoundBorder( compound, empty );
        buttonPanel.setBorder( titled );
        mainPanel.setBorder( mainBorder );
        
        demoPanel.setBackground( origColor );
        
        return mainPanel;
    }
}
