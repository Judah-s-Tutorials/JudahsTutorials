package com.acmemail.judah.cartesian_plane.sandbox.ocr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * This application incorporates a simple demonstration
 * of scaling in a Swing application.
 * The {@link #paintComponent(Graphics)} method
 * applies a scaling factor under the control of the operator
 * and draws some lines of text,
 * incorporating both alpha and numeric characters.
 * <p>
 * For emphasis,
 * the scaling logic is encapsulated in the {@link #applyScale()} method,
 * which creates a scaling operation
 * and concatenates it with the translation operation
 * that is typically present in the graphics context
 * of a Swing application.
 * 
 * @author Jack Straub
 * 
 * @see ScalingDemo2
 */
public class ScalingDemo1 extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;
    
    /** Strings to render as text in the application FUI. */
    private static final String[]   text    =
    {
        "Winkin, blinkin, and nod one night",
        "Set sail in a wooden shoe.",
        "Sailed on a river of crystal light",
        "Into a sea of dew."
    };

    /** Background color of the principal GUI window. */
    private final Color         bgColor     = Color.WHITE;
    /** Color for drawing text in the application window. */
    private final Color         textColor   = Color.BLACK;
    /** Font family for drawing text in the application window. */
    private final String        fontFamily  = "Courier New";
    /** Color for text in the application window. */
    private final int           fontSize    = 12;
    /** 
     * Scale factor; updated at the operator's discretion.
     * See {@link #getSpinnerPanel()}.
     */
    private float               scaleFactor = 1.0f;
    
    /** 
     * Current width of the application window. 
     * Set in {@link #paintComponent(Graphics)},
     * declared here for convenience.
     */
    private int                 width;
    /** 
     * Current height of the application window. 
     * Set in {@link #paintComponent(Graphics)},
     * declared here for convenience.
     */
    private int                 height;
    /** 
     * Working graphics context derived from the parameter
     * to the {@link #paintComponent(Graphics)} method.
     * Created at the start of the 
     * {@link #paintComponent(Graphics)} method,
     * and disposed at the end.
     */
    private Graphics2D          gtx;
    /** 
     * Current font object of the application window. 
     * Set in {@link #paintComponent(Graphics)},
     * declared here for convenience.
     */
    private Font                font;
    /** 
     * Font-render-context derived from the application's
     * graphics context. 
     * Assigned in {@link #paintComponent(Graphics)},
     * declared here for convenience.
     */
    private FontRenderContext   frc;
    /** 
     * Convenience variable to control line spacing
     * of the text drawn in the application's principal window.
     * Initialized in the {@link #paintComponent(Graphics)} method.
     */
    private int                 currLine;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            ScalingDemo1    demo  = new ScalingDemo1();
            demo.build();
        });
    }
    
    /**
     * Constructor.
     * Establishes the initial size and font
     * of the principal application window.
     */
    public ScalingDemo1()
    {
        Dimension   dim     = new Dimension( 500, 300 );
        setPreferredSize( dim );
        
        Font    font    = 
            new Font( fontFamily, Font.BOLD, fontSize );
        setFont( font );
    }

    /**
     * Builds and displays the application GUI.
     */
    private void build()
    {
        JFrame      frame       = new JFrame( "Scale Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        contentPane.add( this, BorderLayout.CENTER );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 100, 200 );
        frame.setVisible( true );
    }
    
    /**
     * Creates the control panel for the application GUI.
     * This consists of an exit button
     * and a spinner for setting the scale factor.
     * 
     * @return  the control panel for the application GUI
     */
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Border      border  = 
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        JButton     exitButton  = new JButton( "Exit" );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        
        panel.add( getSpinnerPanel() );
        panel.add( exitButton );
        
        JPanel  outerPanel  = new JPanel();
        outerPanel.add( panel );
        return outerPanel;
    }
    
    /**
     * Creates a panel consisting of
     * a spinner to control the application's scale factor
     * and a descriptive label.
     * @return
     */
    private JPanel getSpinnerPanel()
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
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        width = getWidth();
        height = getHeight();
        gtx = (Graphics2D)graphics.create();
        
        gtx.setColor( bgColor );
        gtx.fillRect( 0, 0, width, height );
        
        applyScale();
        
        font = gtx.getFont();
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( textColor );
        currLine = 1;
        drawAlphaText();
        ++currLine;
        drawNumericText();
        
        gtx.dispose();
    }
    
    /**
     * Applies the scaling transform 
     * to this window's graphics context.
     */
    private void applyScale()
    {
        AffineTransform     transform       = new AffineTransform();
        transform.scale( scaleFactor, scaleFactor );
        gtx.transform( transform );
    }
    
    /**
     * Draws the alphabetic text in the principal application window.
     * <p>
     * Precondition: 
     * currLine set to the number of the line of text
     * to start drawing in the application window.
     * <p>
     * Postcondition:
     * currLine updated by the number of lines
     * drawn in the application window.
     */
    private void drawAlphaText()
    {
        float   yco = font.getSize() * 2 * currLine;
        float   xco = 10;
        for ( String str : text )
        {
            TextLayout  layout  = new TextLayout( str, font, frc );
            Rectangle2D bounds  = layout.getBounds();
            layout.draw( gtx, xco, yco );
            yco += bounds.getHeight() * 1.5;
            ++currLine;
        }
    }
    
    /**
     * Draws the alphabetic text in the principal application window.
     * <p>
     * Precondition: 
     * currLine set to the number of the line of text
     * to start drawing in the application window.
     * <p>
     * Postcondition:
     * currLine updated by the number of lines
     * drawn in the application window.
     */
    private void drawNumericText()
    {
        float   yOffset = font.getSize() * 1.5f;
        float   ycoBase = yOffset * currLine;
        float   yco     = ycoBase;
        for ( int inx = 0 ; inx < 5 ; ++inx )
        {
            float   xco     = 10;
            for ( float num = -2.123f + inx; num < 3 + inx ; num += 1 )
            {
                String      text    = String.format( "%7.3f", num );
                TextLayout  layout  = new TextLayout( text, font, frc );
                Rectangle2D bounds  = layout.getBounds();
                layout.draw( gtx, xco, yco );
                xco += (1.3 * bounds.getWidth());
            }
            currLine++;
            yco += yOffset;
        }
    }
}