package com.gmail.johnstraub1954.penrose.app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.gmail.johnstraub1954.penrose.PDart;
import com.gmail.johnstraub1954.penrose.PKite;
import com.gmail.johnstraub1954.penrose.PShape;

public class KiteDartIconMaker
{
    private static final String directoryName       = "src/main/resources";
    private static final File   directoryFile       = new File( directoryName );
    private static final String kiteFileName        = "KiteIcon.png";
    private static final String dartFileName        = "DartIcon.png";
    private static final String fileType            = "PNG";
    private static final Color  transparentColor    = Color.YELLOW;
    private static final int    longSide            = 50;
    private static final int    windowSide          = 62;
    
    private static int          eyeballStatus       = JOptionPane.CANCEL_OPTION;

    public static void main( String[] args )
    {
        KiteDartIconMaker    maker    = new KiteDartIconMaker();
        maker.execute();
        System.exit( 0 );
    }

    private void execute()
    {
        shrink( new PKite( longSide, 0, 0 ), kiteFileName );
        shrink( new PDart( longSide, 0, 0 ), dartFileName );
    }

    private void shrink( PShape shape, String path )
    {
        System.out.println( shape.getBounds() );
        int             iColor      = transparentColor.getRGB();
        int             imageType   = BufferedImage.TYPE_INT_ARGB;
        BufferedImage   image       = 
            new BufferedImage( windowSide, windowSide, imageType );
        Graphics2D      gtx     = image.createGraphics();
        gtx.setRenderingHint( 
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        
        gtx.setColor( transparentColor );
        for ( int yco = 0 ; yco < windowSide ; ++yco )
        {
            for ( int xco = 0 ; xco < windowSide ; ++xco )
            {
                image.setRGB( xco, yco, iColor );
            }
        }
        shape.render( gtx );
        
        for ( int yco = 0 ; yco < windowSide ; ++yco )
        {
            for ( int xco = 0 ; xco < windowSide ; ++xco )
            { 
                int rgb = image.getRGB( xco, yco );
                if ( rgb == iColor )
                    image.setRGB( xco, yco, iColor & 0xFFFFFF );
            }
        }
        
        int     hint            = Image.SCALE_AREA_AVERAGING;
        Image   reducedImage    = image.getScaledInstance( 16, 16, hint );
        image = new BufferedImage( 16, 16, imageType );
        gtx = image.createGraphics();
        gtx.drawImage( reducedImage, 0, 0, null );

        if ( showImage( image ) )
            writeImage( image, path );
    }
    
    private void writeImage( BufferedImage icon, String name )
    {
        File    destination = new File( directoryFile, name );
        try
        {
            ImageIO.write( icon, fileType, destination );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private static boolean showImage( BufferedImage image )
    {
        Icon    icon        = new ImageIcon( image );
        JDialog dialog      = new JDialog();
        JPanel  panel       = new JPanel( new BorderLayout() );
        JPanel  controls    = new JPanel();
        JLabel  label   = new JLabel( icon );
        JButton okay        = new JButton( "OK" );
        JButton cancel      = new JButton( "Cancel" );
        
        panel.add( label, BorderLayout.CENTER );
        controls.add( okay );
        controls.add( cancel );
        panel.add( controls, BorderLayout.SOUTH );
        okay.addActionListener( e -> { 
            eyeballStatus = JOptionPane.OK_OPTION;
            dialog.setVisible( false );
        });
        cancel.addActionListener( e -> { 
            eyeballStatus = JOptionPane.OK_OPTION;
            dialog.setVisible( false );
        });

        dialog.setContentPane( panel );
        dialog.setTitle( "Shape Maker" );
        dialog.setModal( true );
        dialog.pack();
        dialog.setVisible( true );

        boolean result  = eyeballStatus == JOptionPane.OK_OPTION;
        return result;
    }
}
