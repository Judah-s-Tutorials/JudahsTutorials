package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.LengthFeedback;

/**
 * This application
 * deserializes the file
 * created by {@linkplain FBCompTA}.
 * First it uses the deserialized data
 * to instantiate a LengthFeedback object.
 * Then it uses a BufferedImage
 * to take a picture of
 * the LengthFeedback display,
 * and compares it to the BufferedImage
 * encapsulated by
 * the deserialized data.
 * <p>
 * This application assumes
 * that FBCompTA has been used
 * to create a data file.
 * 
 * @author Jack Straub
 */
public class FBCompTADemo
{
    /** The data file to read. */
    private static final    File    dataFile    = FBCompTA.getDataFile();
    /** The contents of the deserialized data file. */
    private final FBCompTADetail    detail      = getDetail();
    /** The expected image; extracted from the target data file. */
    private final BufferedImage     expImage    =
        detail.getBufferedImage();
    /** LengthFeedBack object to use in this demo. */
    private final LengthFeedback    feedback    = getFeedback();
    /** 
     * Label to display the result of comparing the expected image,
     * extracted from the deserialized data file, with the actual
     * image, constructed dynamically using configuration data
     * extracted from the deserialized data file.
     */
    private final JLabel            result      = 
        new JLabel( "", SwingConstants.CENTER );
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new FBCompTADemo() );
    }
    
    /**
     * Constructor.
     * Creates and shows
     * the application GUI.
     * <p>
     * Precondition: this constructor must be invoked from the EDT.
     */
    public FBCompTADemo()
    {
        String  title   = "FeedbackCompTA Demo";
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  cPane   = new JPanel( new BorderLayout() );
        cPane.add( getComponentPanel(), BorderLayout.NORTH );
        frame.setContentPane( cPane );
        frame.pack();
        cPane.add( getMainPanel(), BorderLayout.CENTER );
        cPane.add( result, BorderLayout.SOUTH );
        
        compare();
        frame.pack();
        frame.setLocation( 300, 300 );
        frame.setVisible( true );
    }
    
    /**
     * Builds the main panel
     * to use in the application GUI.
     * This consists of 
     * the panel containing the expected image
     * and the panel containing the actual image
     * arranged horizontally.
     * 
     * @return  this application GUI's main panel
     */
    private JPanel getMainPanel()
    {
        Border      border  =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getExpectedPanel() );
        panel.add( Box.createRigidArea( new Dimension( 3, 0 ) ) );
        panel.add( getActualPanel() );
        return panel;
    }
    
    /**
     * Gets a panel containing the feedback label.
     * 
     * @return  a panel containing the feedback label
     */
    private JPanel getComponentPanel()
    {
        JPanel  panel   = new JPanel();
        panel.add( feedback );
        return panel;
    }
    
    /**
     * Gets the panel
     * containing the expected image
     * used in this GUI.
     * This consists of a descriptive label
     * followed by the expected image label
     * arranged vertically.
     * 
     * @return  the panel containing the expected image
     */
    private JPanel getExpectedPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( panel, BoxLayout.Y_AXIS );
        ImageIcon   icon        = new ImageIcon( expImage );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Expected:" ) );
        panel.add( new JLabel( icon ) );
        return panel;
    }
    
    /**
     * Gets the panel
     * containing the actual image
     * used in this GUI.
     * This consists of a descriptive label
     * followed by the actual image label
     * arranged vertically.
     * 
     * @return  the panel containing the expected image
     */
    private JPanel getActualPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Actual:" ) );
        ImageIcon   icon     = new ImageIcon( getActualImage() );
        panel.add( new JLabel( icon ) );
        return panel;
    }
    
    /**
     * Gets a picture of the actual image
     * displayed in this GUI's LengthFeedback component.
     * 
     * @return  
     *      a picture of the actual image displayed in the 
     *      LengthFeedback component
     */
    private BufferedImage getActualImage()
    {
        int             width       = expImage.getWidth();
        int             height      = expImage.getHeight();
        int             type        = expImage.getType();
        BufferedImage   actImage    = 
            new BufferedImage( width, height, type );
        Graphics        graphics    = actImage.createGraphics();
        feedback.paintComponent( graphics );
        
        return actImage;
    }
    
    /**
     * Gets a properly sized
     * LengthFeedback object
     * to use in this demo.
     * <p>
     * Precondition:
     *      The target data file has been deserialized,
     *      and the expected image extracted
     *      and stored in the expImage field.
     * 
     * @return  properly sized LengthFeedback object
     */
    private LengthFeedback getFeedback()
    {
        LengthFeedback  feedback    =
            new LengthFeedback( () -> detail.getPropertyValue() );
        int             width       = expImage.getWidth();
        int             height      = expImage.getHeight();
        Dimension       dim         = new Dimension( width, height );
        feedback.setPreferredSize( dim );
        return feedback;
    }
    
    /**
     * Reads and deserializes 
     * the target file.
     * The contents of the file
     * are returned in a FBCompTADetail object.
     * 
     * @return  
     *      the contents of the target file
     *      encapsulated in a FBCompTADetail object
     */
    private FBCompTADetail getDetail()
    {
        FBCompTADetail  detail  = null;
        try (
            FileInputStream fileStream = 
                new FileInputStream( dataFile );
            ObjectInputStream inStream = 
                new ObjectInputStream( fileStream );
        )
        {
            Object  obj     = inStream.readObject();
            detail = (FBCompTADetail)obj;
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
        return detail;
    }
    
    /**
     * Compares the expected image to the actual image
     * and displays the result
     * in the "result" label.
     */
    private void compare()
    {
        int             width       = feedback.getWidth();
        int             height      = feedback.getHeight();
        int             type        = expImage.getType();
        BufferedImage   actImage    = 
            new BufferedImage( width, height, type );
        Graphics        graphics    = actImage.createGraphics();
        feedback.paintComponent( graphics );
        if ( FBCompTAUtils.equals( expImage, actImage ) )
            result.setText( "Success" );
        else
            result.setText( "Fail" );
        
    }
}
