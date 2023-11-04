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

public class FBCompTADemo
{
    private static final    File    dataFile    = FBCompTA.getDataFile();
    private final FBCompTADetail    detail      = getDetail();
    private final BufferedImage     expImage    =
        detail.getBufferedImage();
    private final LengthFeedback    feedback    = getFeedback();
    private final JLabel            result      = 
        new JLabel( "", SwingConstants.CENTER );
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new FBCompTADemo() );
    }
    
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
        
        frame.setLocation( 300, 300 );
        frame.pack();
        compare();
        frame.pack();
        frame.setVisible( true );
    }
    
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
    
    private JPanel getComponentPanel()
    {
        JPanel  panel   = new JPanel();
        panel.add( feedback );
        return panel;
    }
    
    private JPanel getExpectedPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        ImageIcon   icon        = new ImageIcon( expImage );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Expected:" ) );
        panel.add( new JLabel( icon ) );
        return panel;
    }
    
    private JPanel getActualPanel()
    {
        Border      border      = 
            BorderFactory.createLineBorder( Color.BLACK, 1 );
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( new JLabel( "Actual:" ) );
        ImageIcon   icon     = new ImageIcon( getActualImage() );
        panel.add( new JLabel( icon ) );
        return panel;
    }
    
    private BufferedImage getActualImage()
    {
        int             width       = feedback.getWidth();
        int             height      = feedback.getHeight();
        int             type        = expImage.getType();
        BufferedImage   actImage    = 
            new BufferedImage( width, height, type );
        Graphics        graphics    = actImage.createGraphics();
        feedback.paintComponent( graphics );
        
        return actImage;
    }
    
    private LengthFeedback getFeedback()
    {
        LengthFeedback  feedback    =
            new LengthFeedback( () -> detail.getPropertyValue() );
        Dimension       dim         =
            new Dimension( expImage.getWidth(), expImage.getHeight() );
        feedback.setPreferredSize( dim );
        return feedback;
    }
    
    private FBCompTADetail getDetail()
    {
        FBCompTADetail  detail  = null;
        try (
            FileInputStream fileStream = new FileInputStream( dataFile );
            ObjectInputStream inStream = 
                new ObjectInputStream( fileStream );
        )
        {
            Object  obj     = inStream.readObject();
            if ( !(obj instanceof FBCompTADetail) )
            {
                String  className   = obj.getClass().getName();
                String  error       = 
                    "Unexpected object type: " + className;
                System.err.println( error );
                System.exit( 1 );
            }
            detail = (FBCompTADetail)obj;
        }
        catch ( IOException | ClassNotFoundException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return detail;
    }
    
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
