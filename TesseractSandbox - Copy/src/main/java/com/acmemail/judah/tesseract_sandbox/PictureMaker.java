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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PictureMaker extends JPanel
{
    private static final long serialVersionUID = -6779305390811349326L;
    private final Color bgColor         = Color.LIGHT_GRAY;
    private final Color textColor1      = Color.BLACK;
    private final Color textColor2      = Color.BLUE;
    private final Color rectColor       = Color.ORANGE;
    private final int   rectMargin      = 10;

    private float               fontSize    = 10;
    private int                 width;
    private int                 height;
    private Graphics2D          gtx;
    private Rectangle2D         rect;
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
            PictureMaker    maker   = new PictureMaker();
            maker.build();
        });
    }
    
    private void build()
    {
        JFrame      frame       = new JFrame( "Picture Maker" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JPanel      buttonPanel = new JPanel();
        JButton     exit        = new JButton( "Exit" );
        JButton     save        = new JButton( "Save" );
        
        JFormattedTextField textField   = 
            new JFormattedTextField( Float.valueOf( fontSize) );
        textField.setColumns( 5 );
        textField.addActionListener( e -> {
            fontSize = Float.parseFloat( textField.getText() );
            repaint();
        });
        
        exit.addActionListener( e -> System.exit( 0 ) );
        save.addActionListener( this::saveAction );
        buttonPanel.add( textField );
        buttonPanel.add( exit );
        buttonPanel.add( save );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        contentPane.add( this, BorderLayout.CENTER );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    public PictureMaker()
    {
        Dimension   dim     = new Dimension( 400, 400 );
        setPreferredSize( dim );
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
        rect = new Rectangle2D.Double(
            rectMargin,
            rectMargin,
            width - 2 * rectMargin,
            height / 2
        );
        gtx.setColor( rectColor );
        gtx.fill( rect );
        
        gtx.setColor( textColor1 );
        double  offset  = rect.getHeight() / 3.;
        double  yco     = rect.getY() + offset;
        drawAlphaText( (float)yco );
        yco += offset;
        drawNumericText( (float)yco );
        
        gtx.setColor( textColor2 );
        yco = rect.getMaxY() + offset;
        drawAlphaText( (float)yco );
        yco += offset;
        drawNumericText( (float)yco );
        
        gtx.dispose();
    }
    
    private void drawAlphaText( float yco )
    {
        String      text    = "Winken, Blinken and Nod";
        TextLayout  layout  = new TextLayout( text, font, frc );
        Rectangle2D bounds  = layout.getBounds();
        float       xco     = 
            (float)(rect.getCenterX() - bounds.getWidth() / 2);
        layout.draw( gtx, xco, yco );
    }
    
    private void drawNumericText( float yco )
    {
        float   textXco = 
            (float)(rect.getX() + 0.05 * rect.getWidth());
        for ( float num = -2.123f; num < 3 ; num += 1 )
        {
            String  text    = String.format( "%.3f", num );
            TextLayout  layout  = new TextLayout( text, font, frc );
            Rectangle2D bounds  = layout.getBounds();
            layout.draw( gtx, textXco, yco );
            textXco += (float)(1.3 * bounds.getWidth());
        }
    }
    
    private void saveAction( ActionEvent evt )
    {
        BufferedImage   image   = getImage();
        File            file    = getPath();
        if ( file != null )
            saveImage( file, image );
    }
    
    private File getPath()
    {
        JFileChooser    chooser     = new JFileChooser();
        String          strUserDir  = System.getProperty( "user.dir" );
        File            userDir     = new File( strUserDir );
        chooser.setCurrentDirectory( userDir );
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter( "Images", "jpg", "gif", "png" );
        chooser.setFileFilter(filter);
        
        File        path    = null;
        int         rVal    = chooser.showOpenDialog( this );
        if( rVal == JFileChooser.APPROVE_OPTION )
            path = chooser.getSelectedFile();
        return path;
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
    
    private void saveImage( File path, BufferedImage image )
    {
        try
        {
            String  name    = path.getName();
            int     extInx  = name.lastIndexOf( '.' );
            if ( extInx < 2 | extInx > name.length() - 2 )
            {
                String  msg = 
                    "Invalid file name \""
                    + name + "\"";
                throw new IOException( msg );
            }
            String              type    = name.substring( extInx + 1 );
            FileOutputStream    outStr  = new FileOutputStream( path );
            ImageIO.write( image, type, outStr );
        }
        catch ( IOException exc )
        {
            JOptionPane.showMessageDialog( 
                this, 
                exc.getMessage(), 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE 
            );
            exc.printStackTrace();
        }
    }
}
