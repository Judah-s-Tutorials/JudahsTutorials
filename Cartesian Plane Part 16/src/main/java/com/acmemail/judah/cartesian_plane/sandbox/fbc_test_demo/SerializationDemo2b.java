package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.PButtonGroup;
import com.acmemail.judah.cartesian_plane.components.PRadioButton;

public class SerializationDemo2b
{
    private static final    File        testDir     =
        SerializationDemo2a.getTestDir();
    private final   File[]  testFiles   = 
        testDir.listFiles( f -> f.getName().endsWith( ".ser" ) );
    
    private final   JFrame  frame       = new JFrame();
    private final   JLabel  propLabel   = new JLabel( "XX.XX" );
    private final   JLabel  weightLabel = new JLabel( " XX.XX" );
    private final   JLabel  imageLabel  = new JLabel();
    private final   PButtonGroup<File>  group   = new PButtonGroup<>();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> 
            new SerializationDemo2b().makeGUI()
        );
    }
    
    public SerializationDemo2b()
    {
        if ( testFiles.length == 0 )
        {
            System.err.println( "no data files to process" );
            System.exit( 1 );
        }
    }
    
    private void makeGUI()
    {
        String  title       = "Serialization Demo 2";
        frame.setTitle( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = new JPanel( new BorderLayout() );
        mainPanel.add( getRadioButtonPanel(), BorderLayout.WEST );
        mainPanel.add( getDetailPanel(), BorderLayout.CENTER );
        mainPanel.add( getImagePanel(), BorderLayout.EAST );
        
        frame.setContentPane( mainPanel );
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getImagePanel()
    {
        JPanel  panel   = new JPanel();
        panel.add( imageLabel );
        return panel;
    }

    private JPanel getDetailPanel()
    {
        JPanel      panel   = new JPanel( new GridLayout( 2, 2 ));
        
        propLabel.setHorizontalAlignment( SwingUtilities.LEFT );
        weightLabel.setHorizontalAlignment( SwingUtilities.LEFT );
        
        panel.add( new JLabel( "Prop Value: ", SwingUtilities.RIGHT ) );
        panel.add( propLabel );
        panel.add( new JLabel( "Weight: ", SwingUtilities.RIGHT ) );
        panel.add( weightLabel );

        JPanel      innerPanel  = new JPanel();
        Border      emptyBorder =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border      lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        Border      border  =
            BorderFactory.createCompoundBorder( emptyBorder, lineBorder );
        innerPanel.setBorder( border );
        innerPanel.add( panel );
        return innerPanel;
    }
    
    private JPanel getRadioButtonPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        Border      emptyBorder = 
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border      lineBorder  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        Border      border      =
            BorderFactory.createCompoundBorder( emptyBorder, lineBorder );
        panel.setBorder( border );
        
        Stream.of( testFiles )
            .map( f -> new PRadioButton<File>( f, f.getName() ) )
            .peek( group::add )
            .peek( panel::add )
            .forEach( b -> b.addActionListener( this::showFile ) );
        
        return panel;
    }
    
    private void showFile( ActionEvent evt )
    {
        File    file    = group.getSelectedProperty();
        try (
            FileInputStream fileStr = new FileInputStream( file );
            ObjectInputStream objStr = new ObjectInputStream( fileStr );
        )
        {
            FBCompTADetail detail   =
                (FBCompTADetail)objStr.readObject();
            updateGUI( detail );
        }
        catch ( 
            IOException 
            | ClassNotFoundException
            | ClassCastException exc
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void updateGUI( FBCompTADetail detail )
    {
        final String    numFmt  = "%3.1f";
        double  prop        = detail.getPropertyValue();
        String  strProp     = String.format( numFmt, prop );
        double  weight      = detail.getWeight();
        String  strWeight   = String.format( numFmt, weight );
        Icon    icon        = new ImageIcon( detail.getBufferedImage() );
        
        propLabel.setText( strProp );
        weightLabel.setText( strWeight );
        imageLabel.setIcon( icon );
        frame.pack();
    }
}
