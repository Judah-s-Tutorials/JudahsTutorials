package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanel;
import com.acmemail.judah.cartesian_plane.sandbox.utils.RasterVisualizerPanel;

public class ShowRasterGPP
{
    private static JFrame               frame;
    private static JDialog              dialog;
    private static BufferedImage        raster;
    private static GraphPropertiesPanel gpPanel;

    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }
    
    private static void makeGUI()
    {
        JPanel      contentPane = 
            new JPanel( new BorderLayout() );
        gpPanel = new GraphPropertiesPanel();
        contentPane.add( gpPanel, BorderLayout.CENTER );
        
        JButton open    = new JButton( "Open Dialog" );
        open.addActionListener( e -> openDialog() );
        contentPane.add( open, BorderLayout.SOUTH );
        
        frame = new JFrame( "Show Raster App" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private static void getRaster()
    {
        int             rows    = gpPanel.getHeight();
        int             cols    = gpPanel.getWidth();
        int             type    = BufferedImage.TYPE_INT_ARGB;
        raster = new BufferedImage( cols, rows, type );
        gpPanel.paintComponents( raster.createGraphics() );
//        raster = MakeRaster.get();
    }
    
    private static void openDialog()
    {
        if ( dialog != null )
            dialog.dispose();
        getRaster();
        makeDialog();
        dialog.setLocation( frame.getWidth() + 10, 100 );
        dialog.setVisible( true );
    }
    
    private static void makeDialog()
    {
        RasterVisualizerPanel   rasterPanel =
            new RasterVisualizerPanel( raster );
        rasterPanel.setPreferredSize( new Dimension( 300, 300 ) );
        JScrollPane             scrollPane  = 
            new JScrollPane( rasterPanel );
        scrollPane.setPreferredSize( new Dimension( 500, 500 ));
        JPanel                  contentPane = new JPanel();
        contentPane.add( scrollPane );
        
        dialog = new JDialog( frame, "Showing Raster" );
        dialog.setContentPane( contentPane );
        dialog.pack();
    }
}
