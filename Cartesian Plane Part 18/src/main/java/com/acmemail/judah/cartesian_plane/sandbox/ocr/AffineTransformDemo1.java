package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class AffineTransformDemo1 extends JPanel
{
    private static final String         imageName   = 
        "images/vitruvian_man2.png";
    private static final ClassLoader    loader      = 
        AffineTransformDemo1.class.getClassLoader();
    private static final InputStream    inStream    = 
        loader.getResourceAsStream( imageName );
    
    private static final String         upArrow     = "\u21e7";
    private static final String         downArrow   = "\u21e9";
    private static final String         leftArrow   = "\u21e6";
    private static final String         rightArrow  = "\u21e8";
    
    private static final String         rotateLeft  = "\u21B6";
    private static final String         rotateRight = "\u21B7";
    
    private static final Color          bgColor     = 
        new Color( 0xCCCCCC );
    private static BufferedImage        image;
    
    private int         xcoTranslate        = 0;
    private int         ycoTranslate        = 0;
    private float       scaleFactor         = 1;
    private float       rotateFactor        = 0;
    private Graphics2D  gtx;
    private int         width;
    private int         height;

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        try
        {
            if ( inStream == null )
            {
                String  msg     = "\"" + imageName + "\" not found.";
                throw new Exception( msg );
            }
            image = ImageIO.read( inStream );
        }
        catch ( Exception exc )
        {
            JOptionPane.showMessageDialog(
                null,
                exc.getMessage(),
                "Error Encountered",
                JOptionPane.ERROR_MESSAGE
            );
            exc.printStackTrace();
            System.exit( 1 );
        }
        
        SwingUtilities.invokeLater( () -> new AffineTransformDemo1() );
    }
    
    public AffineTransformDemo1()
    {
        int         panelWidth  = image.getWidth() * 2;
        int         panelHeight = image.getHeight() * 2;
        Dimension   dim     = new Dimension( panelWidth, panelHeight );
        setPreferredSize( dim );
        
        JFrame      frame       = new JFrame( "AffineTransform Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( this, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.WEST );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 200, 100 );
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      outer   = 
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        Border      inner   =
            BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border      border  =
            BorderFactory.createCompoundBorder( outer, inner );
        panel.setBorder( border );
        panel.setLayout( layout );
        
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        exit.setAlignmentX( JButton.CENTER_ALIGNMENT );
        
        Dimension   spacing = new Dimension( 0, 10 );
        
        panel.add( getTranslatePanel() );
        panel.add( Box.createRigidArea( spacing ) );
        panel.add( getRotatePanel() );
        panel.add( Box.createRigidArea( spacing ) );
        panel.add( getScalingPanel() );
        panel.add( Box.createRigidArea( spacing ) );
        panel.add( exit );
        
        JPanel  outerPanel  = new JPanel();
        outerPanel.add( panel );
        return outerPanel;
    }
    
    private JPanel getTranslatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 3, 3 ) );
        JButton upButton    = new JButton( upArrow );
        JButton downButton  = new JButton( downArrow );
        JButton leftButton  = new JButton( leftArrow );
        JButton rightButton = new JButton( rightArrow );
        
        Font    buttonFont  = upButton.getFont().deriveFont( 24f );
        upButton.setFont( buttonFont );
        downButton.setFont( buttonFont );
        leftButton.setFont( buttonFont );
        rightButton.setFont( buttonFont );
        
        upButton.addActionListener( e -> 
            incrementAction( () -> ycoTranslate -= 2 )
        );
        downButton.addActionListener( e -> 
            incrementAction( () -> ycoTranslate += 2 )
        );
        leftButton.addActionListener( e -> 
            incrementAction( () -> xcoTranslate -= 2 )
        );
        rightButton.addActionListener( e -> 
            incrementAction( () -> xcoTranslate += 2 )
        );
        
        panel.add( new JLabel( "" ) );
        panel.add( upButton );
        panel.add( new JLabel( "" ) );
        panel.add( leftButton );
        panel.add( new JLabel( "" ) );
        panel.add( rightButton );
        panel.add( new JLabel( "" ) );
        panel.add( downButton );
        panel.add( new JLabel( "" ) );
        return panel;
    }
    
    private JPanel getRotatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 1, 2, 5, 5 ) );
        JButton leftButton  = new JButton( rotateLeft );
        JButton rightButton = new JButton( rotateRight );
        
        Font    buttonFont  = leftButton.getFont().deriveFont( 24f );
        leftButton.setFont( buttonFont );
        rightButton.setFont( buttonFont );
        float   rotateIncr  = (float)(Math.PI / 32);
        leftButton.addActionListener( e -> 
            incrementAction( () -> rotateFactor -= rotateIncr )
        );
        rightButton.addActionListener( e -> 
            incrementAction( () -> rotateFactor += rotateIncr )
        );
        
        panel.add( leftButton );
        panel.add( rightButton );
        return panel;
    }
    
    private JPanel getScalingPanel()
    {
        SpinnerNumberModel    model   =
            new SpinnerNumberModel( 1.0f, .1f, 10f, .1f );
        JSpinner        spinner = new JSpinner( model );
        spinner.addChangeListener( e -> {
            scaleFactor = model.getNumber().floatValue();
            repaint();
        });
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Scale Factor" ) );
        panel.add( spinner );
        return panel;
    }
    
    private void incrementAction( Runnable proc )
    {
        proc.run();
        repaint();
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );
        gtx = (Graphics2D)graphics.create();
        
        width = getWidth();
        height = getHeight();
        int     imageWidth  = image.getWidth();
        int     imageHeight = image.getHeight();
        gtx.setColor( bgColor );
        gtx.fillRect( 0,  0, width, height );
        
        applyTransforms();
        int         xco     = -imageWidth / 2;
        int         yco     = -imageHeight / 2;
        gtx.drawImage( image, xco, yco, this );
        
        gtx.dispose();
    }
    
    private void applyTransforms()
    {
        double              centerXco   = width / 2.0 + xcoTranslate;
        double              centerYco   = height / 2.0 + ycoTranslate;
        AffineTransform     transform   = new AffineTransform();
        transform.translate( centerXco, centerYco );
        transform.scale( scaleFactor, scaleFactor );
        transform.rotate( rotateFactor );
        gtx.transform( transform );
    }
}
