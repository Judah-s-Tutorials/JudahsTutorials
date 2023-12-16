package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.utils.RasterVisualizerPanel;

public class ShowRaster
{
    private static final int            red     = 0x00FF0000;
    private static final int            blue    = 0x000000FF;
    private static final int            rows    = 10;
    private static final int            cols    = 20;
    private static final BufferedImage  raster  = 
        new BufferedImage( cols, rows, BufferedImage.TYPE_INT_ARGB );
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        // first two rows
        paintRows( 0, 2, red );
        // last two rows
        paintRows( rows - 2, rows, red );
        // In-between rows
        paintRows( 2, rows - 2, blue );
        // first two columns
        paintCols( 0, 2, red );
        // last two columns
        paintCols( cols - 2, cols, red );
        SwingUtilities.invokeLater( () -> makeGUI() );
    }
    
    private static void makeGUI()
    {
        RasterVisualizerPanel   rasterPanel =
            new RasterVisualizerPanel( raster );
        JScrollPane             scrollPane  = 
            new JScrollPane( rasterPanel );
        JPanel                  contentPane = new JPanel();
        contentPane.add( scrollPane );
        
        JFrame  frame   = new JFrame( "Show Raster App" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private static void paintRows( int row1, int row2, int color )
    {
        for ( int yco = row1 ; yco < row2 ; ++yco )
            for ( int xco = 0 ; xco < cols ; ++xco )
                raster.setRGB( xco, yco, color );
    }
    
    private static void paintCols( int col1, int col2, int color )
    {
        for ( int xco = col1 ; xco < col2 ; ++xco )
            for ( int yco = 0 ; yco < rows ; ++yco )
                raster.setRGB( xco, yco, color );
    }
}
