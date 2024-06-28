package com.acmemail.judah.cartesian_plane.sandbox.ocr;

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
import java.awt.image.AffineTransformOp;
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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class ScalingDemoOrig extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;

    private final ScaledDialog  dialog      = new ScaledDialog();
    
    private Color               bgColor     = Color.WHITE;
    private Color               textColor   = Color.BLACK;
    private float               fontSize    = 12;
    private float               scaleFactor = 1.0f;
    
    private BufferedImage       image;
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
            ScalingDemoOrig    demo  = new ScalingDemoOrig();
            demo.build();
        });
    }
    
    public ScalingDemoOrig()
    {
        Dimension   dim     = new Dimension( 300, 400 );
        setPreferredSize( dim );
        
        Font    font    = 
            new Font( "Courier New", Font.BOLD, (int)fontSize );
        setFont( font );
    }

    private void build()
    {
        int         defWidth    = 300;
        int         defHeight   = 300;
        int         type        = BufferedImage.TYPE_INT_RGB;
        image = new BufferedImage( defWidth, defHeight, type );
        
        JFrame      frame       = new JFrame( "Scale Demo" );
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
        applyAction( null );
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
        
        panel.add( getSpinnerPanel() );
        panel.add( bgButton );
        panel.add( fgButton );
        panel.add( applyButton );
        panel.add( exitButton );
        
        JPanel  outerPanel  = new JPanel();
        outerPanel.add( panel );
        return outerPanel;
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
    
    private void applyAction( ActionEvent evt )
    {
        image = getImage();
        Graphics        graphics    = image.getGraphics();
        paintComponent( graphics );
        dialog.update();
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
        gtx.fillRect( 0, 0, width, height );
        
        font = gtx.getFont().deriveFont( fontSize );
        frc = gtx.getFontRenderContext();
        
        gtx.setColor( textColor );
        drawAlphaText( 3 );
        drawNumericText( 5 );
        
        gtx.dispose();
    }
    
    private void drawAlphaText( int offset )
    {
        String      text    = "Winken, Blinken and Nod";
        TextLayout  layout  = new TextLayout( text, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       yco     = (float)(offset * bounds.getHeight());
        float       xco     = 
            (float)(width / 2 - bounds.getWidth() / 2);
        layout.draw( gtx, xco, yco );
    }
    
    private void drawNumericText( int offset )
    {
        float   textXco = 10F;
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
    
    private BufferedImage getImage()
    {
        int     width       = getWidth();
        int     height      = getHeight();
        int     type        = BufferedImage.TYPE_INT_RGB;
        BufferedImage   image   = new BufferedImage( width, height, type );
        paintComponent( image.getGraphics() );
        return image;
    }
    
    @SuppressWarnings("serial")
    private class ScaledPanel extends JPanel
    {
        public ScaledPanel()
        {
            Dimension   size        = new Dimension( 100, 100 );
            setPreferredSize( size );
        }
        
        @Override
        public void paintComponent( Graphics gtx )
        {
            super.paintComponent( gtx );
            BufferedImage       scaledImage     = scaleImage();
            AffineTransform     transform       = new AffineTransform();
            transform.scale( scaleFactor, scaleFactor );
            AffineTransformOp   scaleOp         = 
                new AffineTransformOp( 
                    transform, 
                    AffineTransformOp.TYPE_BICUBIC
                );
            scaleOp.filter( image, scaledImage );
            gtx.drawImage( scaledImage, 0, 0, this );
        }
        
        private BufferedImage scaleImage()
        {
            int             imageType       = image.getType();
            int             scaledWidth     = 
                (int)(image.getWidth() * scaleFactor + .5);
            int             scaledHeight    = 
                (int)(image.getHeight() * scaleFactor + .5);
            BufferedImage   scaledImage     = 
                new BufferedImage( scaledWidth, scaledHeight, imageType );
            return scaledImage;
        }
    }
    
    @SuppressWarnings({ "serial" })
    private class ScaledDialog extends JDialog
    {
        private final JPanel    scaledPanel     = new ScaledPanel();
        public ScaledDialog()
        {
            JPanel      contentPane     = new JPanel( new BorderLayout() );
            JScrollPane scrollPane      = new JScrollPane( scaledPanel );
            contentPane.add( scrollPane, BorderLayout.CENTER );
            setContentPane( contentPane );
            setModal( false );
            pack();
            setVisible( true );
        }
        
        public void update()
        {
            String  strFactor   = 
                String.format( "ScaleFactor: %.02f", scaleFactor );
            setTitle( strFactor );
            scaledPanel.repaint();
        }
    }

}
