package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.FontEditor;

public class GraphicsCaptureDemo1
{
    private JPanel      copyFromPanel;
    private CopyToPanel copyToPanel;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        GraphicsCaptureDemo1    demo    = new GraphicsCaptureDemo1();
        SwingUtilities.invokeLater( () -> demo.buildAll() );
    }
    
    private void buildAll()
    {
        buildCopyFromFrame();
        buildCopyToFrame();
    }
    
    private void buildCopyFromFrame()
    {
        JFrame  frame       = new JFrame( "Image to Copy" );
        
        Border  border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        copyFromPanel = new FontEditor().getPanel();
        copyFromPanel.setBorder( border );
        
        frame.setLocation( 200, 200 );
        frame.setContentPane( copyFromPanel );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void buildCopyToFrame()
    {
        JFrame  frame       = new JFrame( "Copied Image" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
        JPanel  contentPane = new JPanel( new BorderLayout() );
        Border  outerBorder = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border  innerBorder = 
            BorderFactory.createRaisedBevelBorder();
        Border  border      =
            BorderFactory.createCompoundBorder( outerBorder, innerBorder );
        contentPane.setBorder( border );
        
        copyToPanel = new CopyToPanel();
        contentPane.add( copyToPanel, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        JTextField  widthField  = new JTextField( "400", 10 );
        JTextField  heightField = new JTextField( "150", 10 );
        JButton     copyButton  = new JButton( "Copy" );
        JButton     exitButton  = new JButton( "Exit" );
        
        panel.add( new JLabel( "Width:" ) );
        panel.add( widthField );
        panel.add( new JLabel( "Height:" ) );
        panel.add( heightField );
        panel.add( copyButton );
        panel.add( exitButton );
        
        copyButton.addActionListener( e ->
            makeCopy( widthField.getText(), heightField.getText() )
        );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        
        return panel;
    }
    
    private void makeCopy( String strWidth, String strHeight )
    {
        int width   = 50;
        int height  = 50;
        try
        {
            width = Integer.parseInt( strWidth );
            height = Integer.parseInt( strHeight );
        }
        catch ( NumberFormatException exc )
        {
            System.err.println( "Error: " + exc.getMessage() );
        }
        
        BufferedImage   image   = 
            new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D      gtx     = image.createGraphics();
        gtx.fillRect( 0, 0, width, height );
        
        copyFromPanel.paint( gtx );
        copyToPanel.setImage( image );
        copyToPanel.repaint();
        gtx.dispose();
    }
    
    @SuppressWarnings("serial")
    private class CopyToPanel extends JPanel
    {
        private final GreekTile     greekTile   = new GreekTile();
        private final BufferedImage tile        = greekTile.getTile();
        private BufferedImage       copy        = null;
        private Graphics2D          gtx         = null;
        
        public CopyToPanel()
        {
            Dimension   size    = new Dimension( 500, 300 );
            setPreferredSize( size );
        }
        
        public void setImage( BufferedImage image )
        {
            this.copy = image;
        }
        
        @Override
        public void paint( Graphics graphics )
        {
            super.paint( graphics );
            gtx = (Graphics2D)graphics.create();
            
            int width       = getWidth();
            int height      = getHeight();
            int tileWidth   = tile.getWidth();
            int tileHeight  = tile.getHeight();
            
            greekTile.resetTileOffset();
            for ( int yco=0 ; yco <= height ; yco+=tileHeight )
            {
                int randomOff   = greekTile.getTileOffset();
                int rowStart    = -randomOff;
                for ( int xco = rowStart ; xco <= width ; xco += tileWidth )
                    gtx.drawImage( tile, xco, yco, this );
            }
            
            if ( copy != null )
                gtx.drawImage( copy, 50, 50, this );
        }
    }
}
