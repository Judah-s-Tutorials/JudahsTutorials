package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class ButtonImageDemo1
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ButtonImageDemo1    demo    = new ButtonImageDemo1();
        SwingUtilities.invokeLater( () -> demo.build() );
    }

    private void build()
    {
        JFrame      frame       = new JFrame( "Button Image Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      panel       = new JPanel( new BorderLayout() );       
        Border      border      = 
            BorderFactory.createEmptyBorder( 25, 25, 25, 25 );
        panel.setBorder( border );
        
        Dimension   rigidDim    = new Dimension( 10, 0 );
        panel.add( Box.createRigidArea( rigidDim ) );
        panel.add( getButtonPanel(), BorderLayout.WEST );
        panel.add( getCheckBoxPanel(), BorderLayout.EAST );
        
        frame.setContentPane( panel );
        frame.setLocation( 300, 100 );
        frame.pack();
        frame.setVisible( true );
        
        ImageIcon   icon    = new ImageIcon( "images/Forward.png" );
    }
    
    private JPanel getButtonPanel()
    {
        ImageIcon   fwdIcon     = getImageIcon( "images/Forward.png" );
        ImageIcon   backIcon    = getImageIcon( "images/Backward.png" );
        Dimension   rigidDim    = new Dimension( 0, 10 );
        
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      emptyBorder = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border      bevelBorder =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border      border      = 
            BorderFactory.createCompoundBorder( bevelBorder, emptyBorder );
        panel.setLayout( layout );        
        panel.setBorder( border );
        
        JButton     fwdButton1  = new JButton( fwdIcon );
        panel.add( fwdButton1 );
        panel.add( new JLabel( "JButton with icon" ) );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JButton     fwdButton2  = getLabeledButton( "Forward", fwdIcon );
        panel.add( fwdButton2 );
        panel.add( new JLabel( "JButton with label and icon" ) );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JButton     backButton1 = new JButton( backIcon );
        panel.add( backButton1 );
        panel.add( new JLabel( "JButton with icon" ) );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JButton     backButton2 = getLabeledButton( "Back", backIcon );
        panel.add( backButton2 );
        panel.add( new JLabel( "JButton with text and icon" ) );
        
        return panel;
    }
    
    private JPanel getCheckBoxPanel()
    {
        ImageIcon   stopIcon    = getImageIcon( "images/Stop.png" );
        ImageIcon   stopIconRO  = getImageIcon( "images/StopRollover.png" );
        ImageIcon   goIcon      = getImageIcon( "images/Go.png" );
        ImageIcon   goIconSel   = getImageIcon( "images/GoSelected.png" );
        Dimension   rigidDim    = new Dimension( 0, 10 );

        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      emptyBorder = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border      bevelBorder =
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border      border      = 
            BorderFactory.createCompoundBorder( bevelBorder, emptyBorder );
        panel.setLayout( layout );        
        panel.setBorder( border );
     
        JCheckBox   checkBox1   = getCheckBox( null, goIcon, goIconSel );
        panel.add( checkBox1 );
        panel.add( new JLabel( "JCheckBox with icon" ) );
        panel.add( Box.createRigidArea( rigidDim ) );
      
        JCheckBox   checkBox2   = getCheckBox( "Go", goIcon, goIconSel );
        panel.add( checkBox2 );
        panel.add( new JLabel( "JCheckBox with text and icon" ) );
        panel.add( Box.createRigidArea( rigidDim ) );
       
        JCheckBox   checkBox3   = getCheckBox( "Stop", stopIcon, "Go", goIconSel );
        panel.add( checkBox3 );
        panel.add( new JLabel( "JCheckBox, 2 states" ) );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JCheckBox   checkBox4   = getCheckBox( "Stop", stopIcon, "Go", goIconSel );
        panel.add( checkBox4 );
        checkBox4.setRolloverIcon( stopIconRO );
        checkBox4.setPressedIcon( goIcon );
        panel.add( new JLabel( "JCheckBox, 4 states" ) );
       
        return panel;
    }
    
    private void 
    itemStateChange( ItemEvent evt, String text, String selectedText )
    {
        JCheckBox   cBox    = (JCheckBox)evt.getSource();
        String      label   = cBox.isSelected() ? selectedText : text;
        cBox.setText( label );
    }
    
    private JButton getLabeledButton( String label, ImageIcon icon )
    {
        JButton button  = new JButton( label, icon );
        button.setHorizontalTextPosition( SwingConstants.LEFT );
        button.setIconTextGap( 5 );
        return button;
    }
    
    private JCheckBox getCheckBox( 
        String text, 
        ImageIcon icon, 
        ImageIcon selectedIcon
    )
    {
        Border      bevelBorder = 
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border      emptyBorder =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border      =
            BorderFactory.createCompoundBorder( bevelBorder, emptyBorder );
        
        JCheckBox   checkBox    = new JCheckBox( icon );
        checkBox.setSelectedIcon( selectedIcon );
        checkBox.setText( text );
        checkBox.setBorder( border );
        checkBox.setBorderPainted( true );
        
        return checkBox;
    }
    
    private JCheckBox getCheckBox( 
        String text, 
        ImageIcon icon, 
        String selectedText,
        ImageIcon selectedIcon
    )
    {
        Border      bevelBorder = 
            BorderFactory.createBevelBorder( BevelBorder.RAISED );
        Border      emptyBorder =
            BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        Border      border      =
            BorderFactory.createCompoundBorder( bevelBorder, emptyBorder );
       
        JCheckBox   checkBox    = new JCheckBox( icon );
        checkBox.setSelectedIcon( selectedIcon );
        checkBox.setBorder( border );
        checkBox.setBorderPainted( true );
        if ( text!= null )
            checkBox.setText( text );
        
        checkBox.addItemListener( e -> 
            itemStateChange( e, text, selectedText )
        );
        
        return checkBox;
    }
    
    private ImageIcon getImageIcon( String path )
    {
        File        file    = new File( path );
        ImageIcon   icon    = null;
        try
        {
            BufferedImage   image   = ImageIO.read( file );
            icon = new ImageIcon( image );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return icon;
    }
}
