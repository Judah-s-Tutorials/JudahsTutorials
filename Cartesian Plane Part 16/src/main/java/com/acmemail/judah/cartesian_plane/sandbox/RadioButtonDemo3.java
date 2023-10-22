package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

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

/**
 * This is a minor variation
 * on {@linkplain RadioButtonDemo2}.
 * It has three sets of radio buttons,
 * one for selecting font size,
 * one for selecting background color
 * and one for selecting foreground color.
 * 
 * @author Jack Straub
 */
public class RadioButtonDemo3
{
    private JLabel      demoLabel;
    private JPanel      demoPanel;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new RadioButtonDemo3().build() );
    }
    
    /**
     * Create and deploy application GUI.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "Radio Button Demo 3" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );

        demoPanel = new JPanel();
        Border      border      = 
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        demoPanel.setBorder( border );
        demoLabel = new JLabel( "Radio Button Demo 3" );
        demoPanel.setPreferredSize( new Dimension( 300, 75 ) );
        demoPanel.add( demoLabel );
        contentPane.add( demoPanel, BorderLayout.CENTER );
        
        contentPane.add( getButtonPanels(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Obtains a panel
     * that contains
     * all the radio buttons,
     * arranged by type
     * (font size, foreground color and background color).
     * 
     * @return  a panel containing all the application's radio buttons
     */
    private JPanel getButtonPanels()
    {
        JPanel  panel   = new JPanel( new GridLayout( 3,1 ) );
        panel.add( getFontButtonPanel() );
        panel.add( getForegroundColorPanel() );
        panel.add( getBGColorPanel() );
        return panel;
    }
    
    /**
     * Gets a pair of nested panels
     * containing those radio buttons
     * that control font size.
     * 
     * @return  
     *      a panel containing the radio buttons
     *      that control font size.
     */
    private JPanel getFontButtonPanel()
    {
        JPanel      buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        ButtonGroup buttonGroup = new ButtonGroup();
        Font        origFont    = demoLabel.getFont();
        float       origSize    = origFont.getSize();
        DoubleStream.of( .5, .75, 1, 1.5, 2 )
            .map( d -> d * origSize )
            .mapToObj( s -> origFont.deriveFont( (float)s ) )
            .forEach( f -> {
                JRadioButton    button  = new JRadioButton();
                button.addActionListener( e -> demoLabel.setFont( f ) );
                buttonGroup.add( button );
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
    
    /**
     * Gets a pair of nested panels
     * containing those radio buttons
     * that control foreground color.
     * 
     * @return  
     *      a panel containing the radio buttons
     *      that control foreground color
     */
    private JPanel getForegroundColorPanel()
    {
        JPanel      buttonPanel         = new JPanel( new GridLayout( 1, 5 ) );
        ButtonGroup fgColorButtonGroup  = new ButtonGroup();
        int         baseIColor          = 0x3FBF48;
        IntStream.of( 0xD8F2DA, 0x8CD891, baseIColor, 0x32993A, 0x26722B )
            .mapToObj( Color::new )
            .forEach( c -> {
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
        
        demoLabel.setForeground( new Color( baseIColor ) );
        
        return mainPanel;
    }
    
    /**
     * Gets a pair of nested panels
     * containing those radio buttons
     * that control background color.
     * 
     * @return  
     *      a panel containing the radio buttons
     *      that control background color.
     */
    private JPanel getBGColorPanel()
    {
        JPanel      buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        ButtonGroup buttonGroup = new ButtonGroup();
        int         baseIColor  = 0x3F5ABF;
        IntStream.of( 0xC5CDEB, 0x8C9CD8, baseIColor, 0x324899, 0x263672 )
            .mapToObj( Color::new )
            .forEach( c -> {
                JRadioButton    button  = new JRadioButton();
                button.addActionListener( e -> demoPanel.setBackground( c ) );
                buttonGroup.add( button );
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
        
        demoPanel.setBackground( new Color( baseIColor ) );
        
        return mainPanel;
    }
}
