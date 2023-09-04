package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class BorderDemo1
{
    private static final int    locX    = 300;
    private static final int    locY    = 300;
    
    private List<JFrame>    allFrames   = new ArrayList<>();
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new BorderDemo1().makeGUI() );

    }
    
    private void makeGUI()
    {
        JFrame  frame       = new JFrame( "Border Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        JPanel  centerPanel = new JPanel( new GridLayout( 2, 3, 5, 5) );
        
        JButton empty       = new JButton( "Show Empty Border" );
        empty.addActionListener( e -> {
            closeAll();
            emptyBorderDemo();
        });
        centerPanel.add( empty );
        
        JButton bevel       = new JButton( "Show Bevel Border" );
        centerPanel.add( bevel );
        bevel.addActionListener( e -> {
            closeAll();
            bevelBorderDemo();
        });
        
        JButton etched      = new JButton( "Show Etched Border" );
        centerPanel.add( etched );
        etched.addActionListener( e -> {
            closeAll();
            etchedBorderDemo();
        });
        
        JButton line        = new JButton( "Show Line Border" );
        centerPanel.add( line );
        line.addActionListener( e -> {
            closeAll();
            lineBorderDemo();
        });
        
        JButton titled      = new JButton( "Show Titled Border" );
        titled.addActionListener( e -> {
            closeAll();
            titledBorderDemo();
        });
        centerPanel.add( titled );
        
        JButton compound    = new JButton( "Show Compound Border" );
        centerPanel.add( compound );
        compound.addActionListener( e -> {
            closeAll();
            compoundBorderDemo();
        });
        centerPanel.add( compound );
        
        contentPane.add( centerPanel, BorderLayout.CENTER );
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        contentPane.add( getButtonPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( locX - 150, locY - 150 );
        frame.setVisible( true );
    }

    private void emptyBorderDemo()
    {
        JFrame  frame1  = new JFrame( "Empty Border Demo" );
        JFrame  frame2  = new JFrame( "Empty Border Demo" );
        allFrames.add( frame1 );
        allFrames.add( frame2 );
        JPanel  panel1  = getMainPanel( "JPanel Without Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Empty Border" );
        Border  border  =
            BorderFactory.createEmptyBorder( 20, 20, 20, 20 );
        panel2.setBorder( border );
        
        frame1.setContentPane( getContentPane( panel1 ) );
        frame2.setContentPane( getContentPane( panel2 ) );
        frame1.pack();
        frame2.pack();
        frame1.setLocation( locX, locY );
        int locX2   = locX + frame1.getWidth() + 10;
        frame2.setLocation( locX2, locY );
        
        frame1.setVisible( true );
        frame2.setVisible( true );
    }

    private void bevelBorderDemo()
    {
        JFrame  frame1  = new JFrame( "Bevel Border Demo" );
        JFrame  frame2  = new JFrame( "Bevel Border Demo" );
        allFrames.add( frame1 );
        allFrames.add( frame2 );
        JPanel  panel1  = getMainPanel( "JPanel With Raised Bevel Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Lowered Bevel Border" );
        Border  border1 =
            BorderFactory.createRaisedBevelBorder();
        Border  border2 =
            BorderFactory.createLoweredBevelBorder();
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        
        frame1.setContentPane( getContentPane( panel1 ) );
        frame2.setContentPane( getContentPane( panel2 ) );
        frame1.pack();
        frame2.pack();
        frame1.setLocation( locX, locY );
        int locX2   = locX + frame1.getWidth() + 10;
        frame2.setLocation( locX2, locY );
        
        frame1.setVisible( true );
        frame2.setVisible( true );
    }

    private void etchedBorderDemo()
    {
        JFrame  frame1  = new JFrame( "Etched Border Demo" );
        JFrame  frame2  = new JFrame( "Etched Border Demo" );
        allFrames.add( frame1 );
        allFrames.add( frame2 );
        JPanel  panel1  = getMainPanel( "JPanel With Raised Etched Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Lowered Etched Border" );
        Border  border1 =
            BorderFactory.createEtchedBorder( EtchedBorder.RAISED );
        Border  border2 =
            BorderFactory.createEtchedBorder( EtchedBorder.LOWERED );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        
        frame1.setContentPane( getContentPane( panel1 ) );
        frame2.setContentPane( getContentPane( panel2 ) );
        frame1.pack();
        frame2.pack();
        frame1.setLocation( locX, locY );
        int locX2   = locX + frame1.getWidth() + 10;
        frame2.setLocation( locX2, locY );
        
        frame1.setVisible( true );
        frame2.setVisible( true );
    }

    private void lineBorderDemo()
    {
        JFrame  frame1  = new JFrame( "Line Border Demo" );
        JFrame  frame2  = new JFrame( "Line Border Demo" );
        allFrames.add( frame1 );
        allFrames.add( frame2 );
        JPanel  panel1  = getMainPanel( "JPanel With Thin Line Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Thick Line Border" );
        Border  border1 =
            BorderFactory.createLineBorder( Color.BLACK );
        Border  border2 =
            BorderFactory.createLineBorder( Color.BLACK, 10 );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        
        frame1.setContentPane( getContentPane( panel1 ) );
        frame2.setContentPane( getContentPane( panel2 ) );
        frame1.pack();
        frame2.pack();
        frame1.setLocation( locX, locY );
        int locX2   = locX + frame1.getWidth() + 10;
        frame2.setLocation( locX2, locY );
        
        frame1.setVisible( true );
        frame2.setVisible( true );
    }

    private void titledBorderDemo()
    {
        JFrame  frame1  = new JFrame( "Titled Border Demo" );
        JFrame  frame2  = new JFrame( "Titled Border Demo" );
        allFrames.add( frame1 );
        allFrames.add( frame2 );
        JPanel  panel1  = getMainPanel( "JPanel With Titled Border" );
        JPanel  panel2  = getMainPanel( "JPanel With Line/Titled Border" );
        Border  border1 =
            BorderFactory.createTitledBorder( "Your Title Goes Here" );
        Border  line    = BorderFactory.createLineBorder( Color.RED, 2 );
        Border  border2 =
            BorderFactory.createTitledBorder( line, "Your Title Goes Here" );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        
        // The main panel goes inside another JPanel with an empty
        // border; this makes the border around the main panel clearer.
        Border  empty           = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel  contentPane1    = new JPanel();
        contentPane1.setBorder( empty );
        contentPane1.add( panel1 );
        
        JPanel  contentPane2    = new JPanel();
        contentPane2.setBorder( empty );
        contentPane2.add( panel2 );
        
        frame1.setContentPane( getContentPane( panel1 ) );
        frame2.setContentPane( getContentPane( panel2 ) );
        frame1.pack();
        frame2.pack();
        frame1.setLocation( locX, locY );
        int locX2   = locX + frame1.getWidth() + 10;
        frame2.setLocation( locX2, locY );
        
        frame1.setVisible( true );
        frame2.setVisible( true );
    }

    private void compoundBorderDemo()
    {
        JFrame  frame1  = new JFrame( "Compound Border Demo" );
        JFrame  frame2  = new JFrame( "Compound Border Demo" );
        allFrames.add( frame1 );
        allFrames.add( frame2 );
        JPanel  panel1  = 
            getMainPanel( "JPanel With Titled Border Inside Etched" );
        JPanel  panel2  = 
            getMainPanel( "JPanel With Titled Border Outside Etched" );
        Border  etched  = BorderFactory.createEtchedBorder();
        Border  titled  = 
            BorderFactory.createTitledBorder( "Your Title Goes Here" );
        Border  border1 =
            BorderFactory.createCompoundBorder( etched, titled );
        Border  border2 =
            BorderFactory.createCompoundBorder( titled, etched );
        panel1.setBorder( border1 );
        panel2.setBorder( border2 );
        
        // The main panel goes inside another JPanel with an empty
        // border; this makes the border around the main panel clearer.
        Border  empty           = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel  contentPane1    = new JPanel();
        contentPane1.setBorder( empty );
        contentPane1.add( panel1 );
        
        JPanel  contentPane2    = new JPanel();
        contentPane2.setBorder( empty );
        contentPane2.add( panel2 );
        
        frame1.setContentPane( getContentPane( panel1 ) );
        frame2.setContentPane( getContentPane( panel2 ) );
        frame1.pack();
        frame2.pack();
        frame1.setLocation( locX, locY );
        int locX2   = locX + frame1.getWidth() + 10;
        frame2.setLocation( locX2, locY );
        
        frame1.setVisible( true );
        frame2.setVisible( true );
    }
    
    private JPanel getMainPanel( String caption )
    {
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        JPanel  centerPanel = new JPanel();
        centerPanel.add( new JButton( "Pause Reality" ) );
        centerPanel.add( new JButton( "Resume Reality" ) );
        mainPanel.add( centerPanel, BorderLayout.CENTER );
        
        JLabel  label   = new JLabel( caption );
        label.setHorizontalTextPosition( SwingUtilities.CENTER );
        label.setHorizontalAlignment( JLabel.CENTER );
        label.setBorder( BorderFactory.createLineBorder( Color.BLACK));
        mainPanel.add( new JLabel( caption ), BorderLayout.SOUTH );
        
        return mainPanel;
    }
    
    private JPanel getButtonPanel()
    {
        Border  border  = 
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        JPanel  panel   = new JPanel();
        panel.setBorder(border);
        
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( exit );
        return panel;
    }
    
    private JPanel getContentPane( JPanel mainPanel )
    {
        // The main panel goes inside another JPanel with an empty
        // border; this makes the border around the main panel clearer.
        Border  empty           = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel  contentPane     = new JPanel();
        contentPane.setBackground( Color.DARK_GRAY );
        contentPane.setBorder( empty );
        contentPane.add( mainPanel );
        return contentPane;
    }
    
    private void closeAll()
    {
       allFrames.stream()
           .peek( f -> f.setVisible( false ) )
           .forEach( JFrame::dispose );
       allFrames.clear();
    }
}
