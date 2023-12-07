package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;

/**
 * This is a revision
 * of {@linkplain RadioButtonDemo3}
 * which uses PRadioButtons
 * and PButton groups
 * to organize the three groups of buttons
 * (for controlling font size, foreground color
 * and background color).
 * In addition 
 * to the functionality
 * encapsulated in {@linkplain RadioButtonDemo3}
 * this application
 * has a pushbutton which,
 * when pressed,
 * will report 
 * the three currently selected properties.
 * 
 * @author Jack Straub
 */
public class PRadioButtonDemo1
{
    private JLabel              demoLabel;
    private JPanel              demoPanel;
    private PButtonGroup<Font>  fontButtonGroup;
    private PButtonGroup<Color> fgColorButtonGroup;
    private PButtonGroup<Color> bgColorButtonGroup;
    private ActivityLog         activityLog;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new PRadioButtonDemo1().build() );
    }
    
    /**
     * Create and deploy application GUI.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "PRadioButton Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        activityLog = new ActivityLog( frame );
        JPanel      contentPane = new JPanel( new BorderLayout() );

        demoPanel = new JPanel();
        Border      border      = 
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        demoPanel.setBorder( border );
        demoLabel = new JLabel( "Radio Button Demo 3" );
        demoPanel.setPreferredSize( new Dimension( 300, 75 ) );
        demoPanel.add( demoLabel );
        contentPane.add( demoPanel, BorderLayout.CENTER );
        
        contentPane.add( getShowPropertiesPanel(), BorderLayout.NORTH );
        contentPane.add( getButtonPanels(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Gets the panel
     * containing the "Show Properties" button.
     * 
     * @return  the panel containing the "Show Properties" button
     */
    private JPanel getShowPropertiesPanel()
    {
        JPanel  panel   = new JPanel();
        JButton show    = new JButton( "Show Selected Properties" );
        show.addActionListener( e -> {
            Font    font    = fontButtonGroup.getSelectedProperty();
            int     size    = font.getSize();
            Color   fgColor = fgColorButtonGroup.getSelectedProperty();
            String  fgHex   = Integer.toHexString( fgColor.getRGB() );
            Color   bgColor = bgColorButtonGroup.getSelectedProperty();
            String  bgHex   = Integer.toHexString( bgColor.getRGB() );
            activityLog.append( "Selected font size: " + size );
            activityLog.append( "Selected foreground color: " + fgHex );
            activityLog.append( "Selected background color: " + bgHex );
            activityLog.append( "------------" );
        });
        panel.add( show );
        return panel;
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
        Font        origFont    = demoLabel.getFont();
        float       origSize    = origFont.getSize();
        fontButtonGroup = new PButtonGroup<Font>();
        DoubleStream.of( .5, .75, 1, 1.5, 2 )
            .map( d -> d * origSize )
            .mapToObj( s -> origFont.deriveFont( (float)s ) )
            .map( PRadioButton<Font>::new )
            .peek( fontButtonGroup::add )
            .peek( rb -> rb.addActionListener( this::fontSelected ) )
            .forEach( buttonPanel::add );
        int         selected    = fontButtonGroup.getButtonCount() / 2;
        fontButtonGroup.selectIndex( selected );

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
     * ActionPerformed method
     * for radio buttons
     * in fontButtonGroup.
     * 
     * @param evt   event object
     */
    private void fontSelected( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof PRadioButton )
        {
            @SuppressWarnings("unchecked")
            PRadioButton<Font>  button  = (PRadioButton<Font>)source;
            demoLabel.setFont( button.get() );
        }
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
        JPanel  buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        fgColorButtonGroup  = new PButtonGroup<Color>();
        IntStream.of( 0xD8F2DA, 0x8CD891, 0x3FBF48, 0x32993A, 0x26722B )
            .mapToObj( Color::new )
            .map( PRadioButton<Color>::new )
            .peek( fgColorButtonGroup::add )
            .peek( rb -> rb.addActionListener( this::fgColorSelected ) )
            .forEach( buttonPanel::add );

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

        int         selected    = fgColorButtonGroup.getButtonCount() / 2;
        fgColorButtonGroup.selectIndex( selected );
        
        return mainPanel;
    }
    
    /**
     * ActionPerformed method
     * for radio buttons
     * in the fgColorButtonGroup.
     * 
     * @param evt   event object
     */
    private void fgColorSelected( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof PRadioButton )
        {
            @SuppressWarnings("unchecked")
            PRadioButton<Color>  button  = (PRadioButton<Color>)source;
            demoLabel.setForeground( button.get() );
        }
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
        JPanel  buttonPanel = new JPanel( new GridLayout( 1, 5 ) );
        bgColorButtonGroup = new PButtonGroup<Color>();
        int         baseIColor  = 0x3F5ABF;
        IntStream.of( 0xC5CDEB, 0x8C9CD8, baseIColor, 0x324899, 0x263672 )
            .mapToObj( Color::new )
            .map( PRadioButton<Color>::new )
            .peek( bgColorButtonGroup::add )
            .peek( rb -> rb.addActionListener( this::bgColorSelected ) )
            .forEach( buttonPanel::add );

        JPanel      mainPanel   = new JPanel();
        BoxLayout   mainLayout  = 
            new BoxLayout( mainPanel, BoxLayout.X_AXIS );
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

        int         selected    = bgColorButtonGroup.getButtonCount() / 2;
        bgColorButtonGroup.selectIndex( selected );
        
        return mainPanel;
    }
    
    /**
     * ActionPerformed method
     * for radio buttons
     * in the bgColorButtonGroup.
     * 
     * @param evt   event object
     */
    private void bgColorSelected( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof PRadioButton )
        {
            @SuppressWarnings("unchecked")
            PRadioButton<Color>  button  = (PRadioButton<Color>)source;
            demoPanel.setBackground( button.get() );
        }
    }
}
