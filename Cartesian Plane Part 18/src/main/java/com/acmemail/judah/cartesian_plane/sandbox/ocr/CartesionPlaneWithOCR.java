package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.GraphManager;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.sandbox.profile.ProfileEditor;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * This class represents a dialog
 * that the operator can use
 * to edit the properties
 * encapsulated in a {@link Profile}.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class CartesionPlaneWithOCR extends JFrame
{
    /** Tesseract segmentation modes. */
    private static final String[]   segModes    =
    {
        "0. OSD only",
        "1. Page segmentation with OSD",
        "2. (not implemented)",
        "3. Page segmentation, no OSD (default)",
        "4. Assume single column text, variable sizes",
        "5. Assume single column text, uniform sizes",
        "6. Assume single uniform block of text",
        "7. Treat image as single text line",
        "8. Treat image as single word",
        "9. Treat image as single word in circle",
        "10. Treat the image as single character",
        "11. Sparse text;  no ordering",
        "12. Sparse text with OSD",
        "13. Raw line",
    };
    /** Tesseract engine modes. */
    private static final String[]   engModes    =
    {
        "0. Legacy engine only",
        "1. Neural nets LSTM engine only",
        "2. Legacy + LSTM engines",
        "3. Whatever is available"
    };
    
    private static final String dataPathStr     = 
        System.getenv( "TESSDATA_PREFIX" );
    /** Tesseract engine for performing character recognition. */
    private final Tesseract tesseract       = new Tesseract();
    /** The title for this dialog. */
    private static final String frameTitle  = "Profile Editor";
    /** The component that the sample graph is drawn on. */
    private final Canvas            canvas;
    private final Profile           profile;
    private final ProfileEditor     editor;
    
    private final JCheckBox         axesCheckBox        =
        new JCheckBox( "Axes" );
    private final JCheckBox         gridLinesCheckBox   =
        new JCheckBox( "Grid Lines" );
    private final JCheckBox         ticMajorCheckBox    =
        new JCheckBox( "Major Tics" );
    private final JCheckBox         ticMinorCheckBox    =
        new JCheckBox( "Minor Tics" );
    private final JCheckBox         horLabelCheckBox    =
        new JCheckBox( "Horizontal Labels" );
    private final JCheckBox         vertLabelCheckBox   =
        new JCheckBox( "Vertical Labels" );
    
    private float                   scaleFactor         = 1;
    private int                     segmentationMode    = 6;
    private int                     engineMode          = 1;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () ->
            new CartesionPlaneWithOCR()
        );
    }
    

    /**
     * Constructor.
     * Initializes the editor dialog.
     * 
     * @param profile   profile to edit
     */
    public CartesionPlaneWithOCR()
    {
        super( frameTitle );
        tesseract.setDatapath( dataPathStr );
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode( 6 );
        tesseract.setOcrEngineMode( 1 );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        profile = new Profile();
        editor = new ProfileEditor( profile );
        canvas = new Canvas( profile );
        
        JPanel      contentPane     = new JPanel( new BorderLayout() );
        contentPane.add( BorderLayout.CENTER, canvas );
        contentPane.add( BorderLayout.WEST, editor );
        contentPane.add( BorderLayout.SOUTH, getControlPanel() );
        
        setContentPane( contentPane );
        contentPane.setFocusable( true );
        pack();
        setVisible( true );

        GUIUtils.center( this );
    }

    /**
     * Applies the most recent Profile edits
     * to the PropertyManager.
     */
    public void apply()
    {
        editor.apply();
        canvas.repaint();
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.add( getCheckboxPanel() );
        panel.add( getScalingPanel() );
        panel.add( getButtonPanel() );
        return panel;
    }
    
    private JPanel getCheckboxPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        Stream.of(
            axesCheckBox,
            gridLinesCheckBox,
            ticMajorCheckBox,
            ticMinorCheckBox,
            horLabelCheckBox,
            vertLabelCheckBox
        )
            .peek( panel::add )
            .forEach( cbox -> 
                cbox.addChangeListener( e -> canvas.repaint() )
            );
        return panel;
    }
    
    private JPanel getScalingPanel()
    {
        Dimension           spacer          = new Dimension( 3, 0 );
        SpinnerNumberModel  scaleModel      =
            new SpinnerNumberModel( 1.0f, .1f, 10f, .1f );
        JSpinner            scaleSpinner    = new JSpinner( scaleModel );
        scaleSpinner.addChangeListener( e -> {
            scaleFactor = scaleModel.getNumber().floatValue();
            repaint();
        });
        
        JComboBox<String>   tessSegModes    = new JComboBox<>( segModes );
        tessSegModes.addActionListener( e -> {
            segmentationMode = tessSegModes.getSelectedIndex();
            repaint();
        });
        tessSegModes.setSelectedIndex( segmentationMode );
        tessSegModes.setEditable( false );
        
        JComboBox<String>   tessEngModes    = new JComboBox<>( engModes );
        tessEngModes.addActionListener( e -> {
            engineMode = tessEngModes.getSelectedIndex();
            repaint();
        });
        tessEngModes.setSelectedIndex( engineMode );
        tessEngModes.setEditable( false );
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Scale Factor" ) );
        panel.add( scaleSpinner );
        panel.add( Box.createRigidArea( spacer ) );
        panel.add( new JLabel( "Seg Mode" ) );
        panel.add( tessSegModes );
        panel.add( Box.createRigidArea( spacer ) );
        panel.add( new JLabel( "Eng Mode" ) );
        panel.add( tessEngModes );
        
        // The extra panel prevents the BoxLayout from stretching
        // main scale panel across the window.
        JPanel  outerPanel  = new JPanel();
        outerPanel.add( panel );
        return outerPanel;
    }
    
    /**
     * Creates a panel with a FlowLayout
     * that contains the application control buttons
     * (Apply, Cancel, etc.).
     * 
     * @return  a panel containing the dialog control buttons
     */
    private JPanel getButtonPanel()
    {
        JPanel      panel   = new JPanel();
        JButton     ocr    = new JButton( "OCR" );
        JButton     apply   = new JButton( "Apply" );
        JButton     exit    = new JButton( "Exit" );

        ocr.addActionListener( this::ocrAction );
        exit.addActionListener( e -> System.exit( 0  ) );
        apply.addActionListener( e -> editor.apply() );
        apply.addActionListener( e -> canvas.repaint() );

        panel.add( ocr );
        panel.add( apply );
        panel.add( exit );

        return panel;
    }
    
    private void ocrAction( ActionEvent evt )
    {
        BufferedImage   image   = getScaledImage();
        try
        {
            tesseract.setPageSegMode( segmentationMode );
            tesseract.setOcrEngineMode( engineMode );
            String  text    = tesseract.doOCR( image );
            parseAndShowText( text );
        }
        catch ( TesseractException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                null, 
                exc.getMessage(), 
                "Tesseract error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        catch ( Throwable exc )
        {
            // This catch block is here because of a bug in Tesseract
            // when trying to set the OCR engine mode.
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                null, 
                exc.getMessage(), 
                "Unmanaged Tesseract Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void parseAndShowText( String text )
    {
        StringBuilder   bldr    = new StringBuilder();
        String[]        tokens  = text.split( "\\s" );
        String          token   = null;
        for ( String tok : tokens )
        {
            token = tok;
            try
            {
                double  num = Double.parseDouble( token );
                String  str = String.format( "%.3f", num );
                bldr.append( "(" )
                    .append( str )
                    .append( ")," );
            }
            catch ( NumberFormatException exc )
            {
                bldr.append( "[" )
                    .append( token )
                    .append( "]," );
            }
        }
        
        JOptionPane.showMessageDialog( null, bldr.toString() );
    }
    
    /**
     * Creates a buffered image
     * from the main application window.
     */
    private BufferedImage getScaledImage()
    {
        int             imageWidth      = getWidth();
        int             imageHeight     = getHeight();
        int             imageType       = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image           = 
            new BufferedImage( imageWidth, imageHeight, imageType );
        canvas.paintComponent( image.getGraphics() );
        BufferedImage   scaledImage     = scaleImage( image );
        return scaledImage;
    }
    
    /**
     * Create a new BufferedImage by applying a scale factor
     * to the principal BufferedImage located in the outer class.
     * 
     * @return  the scaled  BufferedImage
     */
    private BufferedImage scaleImage( BufferedImage image )
    {
        // Create a buffer big enough to hold the scaled image
        int             imageType       = image.getType();
        int             scaledWidth     = 
            (int)(image.getWidth() * scaleFactor + .5);
        int             scaledHeight    = 
            (int)(image.getHeight() * scaleFactor + .5);
        BufferedImage   scaledImage     = 
            new BufferedImage( scaledWidth, scaledHeight, imageType );

        // Scale the image
        AffineTransform     transform       = new AffineTransform();
        transform.scale( scaleFactor, scaleFactor );
        AffineTransformOp   scaleOp         = 
            new AffineTransformOp( 
                transform, 
                AffineTransformOp.TYPE_BICUBIC
            );
        scaleOp.filter( image, scaledImage );
        return scaledImage;
    }

    
    @SuppressWarnings("serial")
    private class Canvas extends JComponent
    {
        /** 
         * Encapsulates to various drawing operations.
         * It is initialized in the constructor,
         * and utilized in the {@link #paintComponent(Graphics)} method.
         */
        private final GraphManager  drawManager;

        /** 
         * Graphics context.
         * Initialized using a copy of the graphics context
         * every time {@link #paintComponent(Graphics)}.
         */
        private Graphics2D          gtx;
        
        /**
         * Constructor.
         * Initializes all aspects of this components GUI.
         * Specifically,
         * its size is set to 50%
         * or the screen size.
         * 
         * @param profile   
         *      the Profile containing the properties that control the drawing
         *      
         * @see GraphManager
         */
        public Canvas( Profile profile )
        {
            drawManager = new GraphManager( this.getVisibleRect(), profile );
            
            Dimension   screenSize  = 
                Toolkit.getDefaultToolkit().getScreenSize();
            int         targetWidth     = (int)(.5 * screenSize.width + .5);
            int         targetHeight    = (int)(.5 * screenSize.height + .5);
            
            Dimension   canvasSize  =
                new Dimension( targetWidth, targetHeight );
            setPreferredSize( canvasSize );
        }
        
        /**
         * Draws the background components of a graph
         * using the given graphics context.
         * 
         * @param   graphics    given graphics context
         */
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            gtx = (Graphics2D)graphics.create();
            
            drawManager.refresh( gtx, this.getVisibleRect() );
            drawManager.drawBackground();
            if ( axesCheckBox.isSelected() )
                drawManager.drawAxes();
            if ( gridLinesCheckBox.isSelected() )
                drawManager.drawGridLines();
            if ( ticMajorCheckBox.isSelected() )
                drawManager.drawMajorTics();
            if ( ticMinorCheckBox.isSelected() )
                drawManager.drawMinorTics();
            if ( horLabelCheckBox.isSelected() )
                drawManager.drawHorizontalLabels();
            if ( vertLabelCheckBox.isSelected() )
                drawManager.drawVerticalLabels();
            
            gtx.dispose();
        }
    }
}
