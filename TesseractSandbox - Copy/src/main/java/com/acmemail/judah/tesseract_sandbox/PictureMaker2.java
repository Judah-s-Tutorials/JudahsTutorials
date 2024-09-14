package com.acmemail.judah.tesseract_sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class PictureMaker2 extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;

    private final TessDialog    dialog      = new TessDialog();
    
    private Color               bgColor     = Color.WHITE;
    private Color               textColor   = Color.BLACK;
    private int                 margin      = 10;
    private float               fontSize    = 10;
    private float               scaleFactor = 1;
    private int                 width;
    private int                 height;
    private Graphics2D          gtx;
    private Font                font;
    private FontRenderContext   frc;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            PictureMaker2    maker   = new PictureMaker2();
            maker.build();
        });
    }
    
    public PictureMaker2()
    {
        Dimension   dim     = new Dimension( 300, 400 );
        setPreferredSize( dim );
        
        Font    font    = 
            new Font( "Courier New", Font.BOLD, (int)fontSize );
        setFont( font );
    }

    private void build()
    {
        JFrame      frame       = new JFrame( "Picture Maker" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        contentPane.add( getControlPanel(), BorderLayout.WEST );
        contentPane.add( this, BorderLayout.CENTER );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 100, 200 );
        Dimension   frameSize   = frame.getPreferredSize();
        dialog.setLocation( (int)frameSize.width + 110, 200 );
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border  = 
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        panel.setLayout( layout );
        panel.setBorder( border );
        
        JButton     fgButton    = new JButton( "Text Color" );
        JButton     bgButton    = new JButton( "BG Color" );
        JButton     applyButton = new JButton( "Apply" );
        JButton     exitButton  = new JButton( "Exit" );
        fgButton.addActionListener( e -> 
            setColor( () -> textColor, c -> textColor = c )
        );
        bgButton.addActionListener( e -> 
            setColor( () -> bgColor, c -> bgColor = c )
        );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        applyButton.addActionListener( this::applyAction );
        
        panel.add( getSizeSpinnerPanel() );
        panel.add( getScaleSpinnerPanel() );
        panel.add( getMarginSpinnerPanel() );
        panel.add( bgButton );
        panel.add( fgButton );
        panel.add( applyButton );
        panel.add( exitButton );
        
        JPanel  outerPanel  = new JPanel();
        outerPanel.add( panel );
        return outerPanel;
    }
    
    private JPanel getSizeSpinnerPanel()
    {
        SpinnerNumberModel    model   =
            new SpinnerNumberModel( 10, 5, 24, 1 );
        JSpinner        spinner = new JSpinner( model );
        spinner.addChangeListener( e -> {
            fontSize = model.getNumber().floatValue();
            repaint();
        });
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Font Size:" ) );
        panel.add( spinner );
        return panel;
    }
    
    private JPanel getMarginSpinnerPanel()
    {
        SpinnerNumberModel    model   =
            new SpinnerNumberModel( 10, 5, 50, 1 );
        JSpinner        spinner = new JSpinner( model );
        spinner.addChangeListener( e -> {
            margin = model.getNumber().intValue();
            repaint();
        });
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Margin:" ) );
        panel.add( spinner );
        return panel;
    }
    
    private JPanel getScaleSpinnerPanel()
    {
        SpinnerNumberModel    model   =
            new SpinnerNumberModel( scaleFactor, .1f, 10f, .1f );
        JSpinner        spinner = new JSpinner( model );
        spinner.addChangeListener( e -> {
            scaleFactor = model.getNumber().floatValue();
            repaint();
        });
        
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        panel.add( new JLabel( "Scale:" ) );
        panel.add( spinner );
        return panel;
    }
    
    private void applyAction( ActionEvent evt )
    {
        int             width       = getWidth();
        int             height      = getHeight();
        int             type        = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image       = 
            new BufferedImage( width, height, type );
        Graphics        graphics    = image.getGraphics();
        paintComponent( graphics );
        dialog.update( image );
    }
    
    private void 
    setColor( Supplier<Color> getter, Consumer<Color> setter )
    {
        Color   color   = JColorChooser.showDialog(
            this, 
            "Select a Color", 
            getter.get()
        );
        if ( color != null )
        {
            setter.accept( color );
            repaint();
        }
    }
    
    @Override
    public void paintComponent( Graphics graphics )
    {
        width = getWidth();
        height = getHeight();
        gtx = (Graphics2D)graphics.create();
        
        gtx.setColor( bgColor );
        applyScale();
        gtx.fillRect( 0, 0, width, height );
        
        font = gtx.getFont().deriveFont( fontSize );
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( textColor );
        drawAlphaText( 2 );
        drawNumericText( 4 );
        
        gtx.dispose();
    }
    
    private void applyScale()
    {
        AffineTransform     transform       = new AffineTransform();
        transform.scale( scaleFactor, scaleFactor );
        transform.concatenate( gtx.getTransform() );
        gtx.setTransform( transform );
    }
    
    private void drawAlphaText( int offset )
    {
        String      text    = "Winken, Blinken and Nod";
        TextLayout  layout  = new TextLayout( text, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       yco     = (float)(offset * bounds.getHeight());
        float       xco     = margin;
        layout.draw( gtx, xco, yco );
    }
    
    private void drawNumericText( int offset )
    {
        float   textXco = margin;
        for ( float num = -2.123f; num < 3 ; num += 1 )
        {
            String  text    = String.format( "%.3f", num );
            TextLayout  layout  = new TextLayout( text, font, frc );
            Rectangle2D bounds  = layout.getBounds();
            float   yco     = (float)(offset * bounds.getHeight());
            layout.draw( gtx, textXco, yco );
            textXco += (float)(1.3 * bounds.getWidth());
        }
    }
    
    @SuppressWarnings("serial")
    private class TessDialog extends JDialog
    {
        private TessBitmapPanel tessPanel   = new TessBitmapPanel();
        public TessDialog()
        {
            setContentPane( tessPanel );
            setModal( false );
            pack();
            setVisible( true );
        }
        
        public void update( BufferedImage image )
        {
            tessPanel.update( image );
            tessPanel.repaint();
        }
    }

}
