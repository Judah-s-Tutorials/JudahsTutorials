package com.acmemail.judah.cartesian_plane.sandbox.fbc_test_demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class SerializationDemo1
{
    private static final int    imageType   = BufferedImage.TYPE_INT_ARGB;
    private static final int    width       = 100;
    private static final int    height      = 75;
    private static final double length      = width * .75;
    private static final double weight      = height * .1;
    private static final Color  bgColor     = Color.ORANGE;
    private static final Color  fgColor     = Color.MAGENTA;
    
    private static final String strTestDir  = "test_data/SerialDemo1";
    private static final File   testDir     = new File( strTestDir );
    private static final String fileName    = "Demo1.ser";
    
    private FBCompTADetail  detailOrig;
    private FBCompTADetail  detailCopy;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SerializationDemo1  demo    = new SerializationDemo1();
        SwingUtilities.invokeLater( demo::makeGUI );
    }
    
    public SerializationDemo1()
    {
        if ( !testDir.exists() )
        {
            if ( !testDir.mkdirs() )
            {
                String  msg     = "Unable to create " + testDir.getName();
                System.err.println( msg );
                System.exit( 1 );
            }
        }
        writeDetail();
        readDetail();
    }
    
    public void makeGUI()
    {
        String      title       = "Serialization Demo 1";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        frame.setContentPane( getMainPanel() );
        frame.pack();
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getMainPanel()
    {
        JPanel  panel = new JPanel( new GridLayout( 2, 1 ) );
        panel.add( getDetailPanel( "Original", detailOrig ) );
        panel.add( getDetailPanel( "Copy", detailCopy ) );
        
        Border  empty   = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border  etch    = BorderFactory.createEtchedBorder();
        Border  comp    = 
            BorderFactory.createCompoundBorder( empty, etch );
        Border  border  = 
            BorderFactory.createCompoundBorder( comp, empty );
        panel.setBorder( border );
        
        return panel;
    }
    
    private JPanel getDetailPanel( String ident, FBCompTADetail detail )
    {
        JPanel      textPanel   = new JPanel();
        BoxLayout   layout      = 
            new BoxLayout( textPanel, BoxLayout.Y_AXIS );
        textPanel.setLayout( layout );
        JLabel      identLabel  = new JLabel( ident );
        String      propStr     = 
            "Prop value: " + detail.getPropertyValue();
        JLabel      propLabel   = new JLabel( propStr );
        String      weightStr   = "Weight: " + detail.getWeight();
        JLabel      weightLabel = new JLabel( weightStr );
        textPanel.add( identLabel );
        textPanel.add( propLabel );
        textPanel.add( weightLabel );
        
        JPanel      imagePanel  = new JPanel();
        Icon        icon        = 
            new ImageIcon( detail.getBufferedImage() );
        JLabel      imageLabel  = new JLabel( icon );
        imagePanel.add( imageLabel );
        
        JPanel      detPanel    = new JPanel();
        BoxLayout   detLayout   = 
            new BoxLayout( detPanel, BoxLayout.X_AXIS );
        detPanel.setLayout( detLayout );
        detPanel.add( textPanel );
        detPanel.add( imagePanel );
        
        return detPanel;

    }
    
    private void writeDetail()
    {
        BufferedImage   image   = drawImage();
        detailOrig = new FBCompTADetail( length, weight, image );
        
        File    file    = new File( testDir, fileName );
        System.out.println( "Writing: " + file.getName() );
        try ( 
            FileOutputStream fileStr = new FileOutputStream( file );
            ObjectOutputStream objStr = 
                new ObjectOutputStream( fileStr );
        )
        {
            objStr.writeObject( detailOrig );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private void readDetail()
    {
        File    file    = new File( testDir, fileName );
        System.out.println( "Reading: " + file.getName() );
        try (
            FileInputStream fileStr = new FileInputStream( file );
            ObjectInputStream objStr = new ObjectInputStream( fileStr );
        )
        {
            detailCopy = (FBCompTADetail)objStr.readObject();
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

    private BufferedImage drawImage()
    {
        
        double  centerXco   = width / 2.0;
        double  centerYco   = height / 2.0;
        double  xco1    = centerXco - length / 2.0;
        double  xco2    = xco1 + length;
        Line2D  line    = 
            new Line2D.Double( xco1, centerYco, xco2, centerYco );
        Stroke  stroke  = new BasicStroke( (float)weight );
        
        BufferedImage   image   = 
            new BufferedImage( width, height, imageType );
        Graphics2D      gtx     = image.createGraphics();
        
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );
        gtx.setColor( fgColor );
        gtx.setStroke( stroke );
        gtx.draw( line );
        
        return image;
    }
}