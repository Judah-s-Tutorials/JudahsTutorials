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

public class ScalingDemo1 extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;
    
    private static final String[]   text    =
    {
        "Winkin, blinkin, and nod one night",
        "Set sail in a wooden shoe.",
        "Sailed on a river of crystal light",
        "Into a sea of dew."
    };

    private Color               bgColor     = Color.WHITE;
    private Color               textColor   = Color.BLACK;
    private float               fontSize    = 12;
    private float               scaleFactor = 1.0f;
    
    private int                 width;
    private int                 height;
    private Graphics2D          gtx;
    private Font                font;
    private FontRenderContext   frc;
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
    
    public ScalingDemo1()
    {
        Dimension   dim     = new Dimension( 500, 300 );
        setPreferredSize( dim );
        
        Font    font    = 
            new Font( "Courier New", Font.BOLD, (int)fontSize );
        setFont( font );
    }

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
        
//        JPanel  outerPanel  = new JPanel();
//        outerPanel.add( panel );
        return panel;
    }
    
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
        
        font = gtx.getFont().deriveFont( fontSize );
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( textColor );
        currLine = 1;
        drawAlphaText();
        ++currLine;
        drawNumericText();
        
        gtx.dispose();
    }
    
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
    
    private void applyScale()
    {
        AffineTransform     transform       = new AffineTransform();
        transform.scale( scaleFactor, scaleFactor );
        transform.concatenate( gtx.getTransform() );
        gtx.setTransform( transform );
    }
}