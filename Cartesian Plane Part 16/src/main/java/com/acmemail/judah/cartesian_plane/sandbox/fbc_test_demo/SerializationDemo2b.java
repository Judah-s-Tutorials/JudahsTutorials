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

/**
 * This application reads
 * and displays
 * the serial data
 * created by {@linkplain SerializationDemo2a}.
 * 
 * @author Jack Straub
 */
public class SerializationDemo2b
{
    /** The directory in which all test data is stored. */
    private static final    File        testDir     =
        SerializationDemo2a.getTestDir();
    /** List of data files read from the test data directory. */
    private final   File[]  testFiles   = 
        testDir.listFiles( f -> f.getName().endsWith( ".ser" ) );
    
    /** The top-level window used in this application. */
    private final   JFrame  frame       = new JFrame();
    /** 
     * The label that displays the target property
     * (length, spacing or stroke)
     * from the deserialized detail object.
     */
    private final   JLabel  propLabel   = new JLabel( "XX.XX" );
    /** 
     * The label that displays the weight property
     * from the deserialized detail object.
     */
    private final   JLabel  weightLabel = new JLabel( " XX.XX" );
    /** 
     * The label that displays the image
     * from the deserialized detail object.
     */
    private final   JLabel  imageLabel  = new JLabel();
    /** The group that manages the radio buttons used in this GUI. */
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
    
    /**
     * Constructs the GUI
     * for this application.
     * The content pane 
     * is a JPanel
     * containing three additional panels
     * in the West, Center and East
     * regions of a BorderLayout.
     * The left panel
     * contains a button for each file
     * contained in the test directory.
     * The center panel
     * contains data describing
     * the detail object stored in the file
     * and the right panel contains the object
     * stored in the file.
     */
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
        group.selectIndex( 0 );
        frame.setVisible( true );
    }
    
    /**
     * Creates the panel
     * containing the image
     * associated with a given file.
     * 
     * @return  the image panel
     */
    private JPanel getImagePanel()
    {
        JPanel  panel   = new JPanel();
        panel.add( imageLabel );
        return panel;
    }

    /**
     * Creates the panel
     * with a GridLayout
     * containing the data
     * associated with a given file,
     * i.e. the property value
     * and weight.
     * 
     * @return  the image panel
     */
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
    
    /**
     * Gets the panel containing the radio buttons
     * for this GUI.
     * There is one radio button
     * for each file found in the data directory.
     * The buttons are laid out vertically.
     * Each button is a PRadioButton<File> object,
     * where the encapsulated property
     * is the File object
     * associated with test data
     * for a single FBCompDetail object.
     * 
     * @return  the radio button panel
     */
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
    
    /**
     * Reads a data file,
     * and updates the GUI
     * with the data obtained.
     * This method is invoked
     * when the operator
     * selects a radio button
     * in the application's GUI.
     * The ActionEvent propagated by the GUI
     * identifies which button was selected,
     * and the selected button
     * is a PRadioButton<File> object
     * whose property identifies
     * the file to read.
     * 
     * @param evt   
     *      object that identifies the radio button selection event
     */
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
    
    /**
     * Updates the application GUI
     * with the image 
     * and associated data
     * encapsulated in a given FBCompTADetail object.
     * 
     * @param detail the given FBCompTADetail object
     */
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
