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

/**
 * The application is a basic demonstration
 * of what can be accomplished
 * with AffineTransforms.
 * It displays an image
 * which the operator can <em>transform</em>
 * using the controls at the left.
 * The image can be move (translated),
 * scaled, and rotate. 
 * The code to manage the transformation
 * is encapsulated in the {@link #applyTransforms()} method.
 * 
 * @author Jack Straub
 * 
 * @see 
 * <a href="https://mathworld.wolfram.com/AffineTransformation.html">
 *     Affine Transformation
 * </a>
 * at Wolfram MathWorld.
 * 
 * @see 
 * <a href="https://www.mathsisfun.com/geometry/transformations.html">
 *     Transformations
 * </a>
 * on the MathIsFun website.
 * 
 * @see 
 * <a href="https://www.mathsisfun.com/algebra/matrix-transform.html">
 *     Transformations and Matrices
 * </a>
 * on the MathIsFun website.
 */
@SuppressWarnings("serial")
public class AffineTransformDemo1 extends JPanel
{
    /** Path to displayed image in the project resources directory. */
    private static final String         imageName   = 
        "images/vitruvian_man2.png";
    /** The image to display. */
    private static final BufferedImage  image       = loadImage();;
    
    /** Unicode for an up-arrow. */
    private static final String         upArrow     = "\u21e7";
    /** Unicode for a down-arrow. */
    private static final String         downArrow   = "\u21e9";
    /** Unicode for a left-arrow. */
    private static final String         leftArrow   = "\u21e6";
    /** Unicode for a right-arrow. */
    private static final String         rightArrow  = "\u21e8";
    
    /** Unicode for a rotate-left arrow. */
    private static final String         rotateLeft  = "\u21B6";
    /** Unicode for a rotate-right arrow. */
    private static final String         rotateRight = "\u21B7";
    
    /** Background color for this component. */
    private static final Color          bgColor     = 
        new Color( 0xCCCCCC );
    
    /** 
     * X-translation factor. Modified when the 
     * left- or right-arrow button is selected.
     * See {@link #getTranslatePanel()}.
     */
    private int         xcoTranslate        = 0;
    /** 
     * Y-translation factor. Modified when the 
     * up- or down-arrow button is selected.
     * See {@link #getTranslatePanel()}.
     */
    private int         ycoTranslate        = 0;
    /** 
     * Scale factor. 
     * Modified when the JSpinner component is adjusted.
     * See {@link #getScalePanel()}.
     */
    private float       scaleFactor         = 1;
    /** 
     * Rotation factor. Modified when the 
     * left- or right-rotate button is selected.
     * See {@link #getRotatePanel()}.
     */
    private float       rotateFactor        = 0;
    
    /** Graphics context; set when the paintComponent method is invoked. */
    private Graphics2D  gtx;
    /** Component width; set when the paintComponent method is invoked. */
    private int         width;
    /** Component height; set when the paintComponent method is invoked. */
    private int         height;

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        
        SwingUtilities.invokeLater( () -> new AffineTransformDemo1() );
    }
    
    /**
     * Constructor.
     * Builds and displays the application GUI.
     */
    public AffineTransformDemo1()
    {
        // Make the initial size of this component twice the size
        // of the image to display.
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
    
    /**
     * Loads the image to display
     * from a file in the resources directory.
     * If the image fails to load
     * and error message is displayed
     * and the application is terminated.
     * 
     * @return  the loaded image
     */
    private static BufferedImage loadImage()
    {
        ClassLoader     loader      = 
            AffineTransformDemo1.class.getClassLoader();
        InputStream     inStream    = 
            loader.getResourceAsStream( imageName );
        BufferedImage   image   = null;
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
        return image;
    }
    
    /**
     * Builds the control panel used by the operator
     * to transform the displayed image.
     * 
     * @return  the initialized control panel
     */
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
        panel.add( getScalePanel() );
        panel.add( Box.createRigidArea( spacing ) );
        panel.add( exit );
        
        JPanel  outerPanel  = new JPanel();
        outerPanel.add( panel );
        return outerPanel;
    }
    
    /**
     * Initializes a panel of controls to be used
     * to translate the displayed image.
     * 
     * @return  the initialized panel
     */
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
    
    /**
     * Initializes a panel of controls to be used
     * to rotate the displayed image.
     * 
     * @return  the initialized panel
     */
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
    
    /**
     * Initializes a panel of controls to be used
     * to sale the displayed image.
     * 
     * @return  the initialized panel
     */
    private JPanel getScalePanel()
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
    
    /**
     * Executes the given procedure
     * and repaints this panel.
     * The expectation is that the given procedure
     * will modify one of the variables
     * that controls image transformation.
     * See for example {@link #xcoTranslate}.
     * 
     * @param proc  the given procedure
     */
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
    
    /**
     * Applies the translate, scale, and rotate transforms
     * to the current graphics context.
     */
    private void applyTransforms()
    {
        // The translation operation first moves the origin of
        // this component to its center, (width/2,height/2)
        // then apples the the offsets set by the operator.
        double              centerXco   = width / 2.0 + xcoTranslate;
        double              centerYco   = height / 2.0 + ycoTranslate;
        AffineTransform     transform   = new AffineTransform();
        transform.translate( centerXco, centerYco );
        transform.scale( scaleFactor, scaleFactor );
        transform.rotate( rotateFactor );
        gtx.transform( transform );
    }
}
