package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.FontEditor;

public class GraphicsCaptureDemo2
{
    private JPanel      copyFromPanel;
    private Robot       robot;
    private CopyToPanel copyToPanel;
    private ActivityLog log;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        GraphicsCaptureDemo2    demo    = new GraphicsCaptureDemo2();
        SwingUtilities.invokeLater( () -> demo.buildAll() );
    }
    
    private void buildAll()
    {
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            exc.printStackTrace();
            System.err.println( "Failed to instantiate rogot" );
            System.exit( 1 );
        }
        log = new ActivityLog();
        log.setLocation( 600, 100 );
        buildCopyFromFrame();
        buildCopyToFrame();
        log.append( "initialization complete" );
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
        JScrollPane     scrollPane  = new JScrollPane( copyToPanel );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setContentPane( contentPane );
        frame.setLocation( 300, 300 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        int         textWidth   = 8;
        JPanel      panel       = new JPanel();
        JTextField  widthField  = new JTextField( "400", textWidth );
        JTextField  heightField = new JTextField( "150", textWidth );
        JTextField  xcoField    = new JTextField( "0", textWidth );
        JTextField  ycoField    = new JTextField( "0", textWidth );
        JPanel      textPanel   = new JPanel( new GridLayout( 2, 2 ) );
        
        textPanel.add( new JLabel( "X Location:" ) );
        textPanel.add( xcoField );
        textPanel.add( new JLabel( "Y Location:" ) );
        textPanel.add( ycoField );
        textPanel.add( new JLabel( "Width:" ) );
        textPanel.add( widthField );
        textPanel.add( new JLabel( "Height:" ) );
        textPanel.add( heightField );

        JButton copyButton      = new JButton( "Copy" );
        JButton exitButton      = new JButton( "Exit" );
        JButton locateButton    = new JButton( "Locate" );
        locateButton.setToolTipText( "Print window size and location" );
        
        panel.add( textPanel );
        panel.add( locateButton );
        panel.add( copyButton );
        panel.add( exitButton );
        
        copyButton.addActionListener( e ->
            makeCopy( xcoField, ycoField, widthField, heightField )
        );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        locateButton.addActionListener( e -> locate() );
        
        return panel;
    }
    
    private void makeCopy( 
        JTextField strXco,
        JTextField strYco,
        JTextField strWidth, 
        JTextField strHeight
    )
    {
        int xco     = getIntVal( strXco, 0 );
        int yco     = getIntVal( strYco, 0 );
        int width   = getIntVal( strWidth, 0 );
        int height  = getIntVal( strHeight, 0 );

        Rectangle       rect    = new Rectangle( xco, yco, width, height );
        BufferedImage   image   = robot.createScreenCapture( rect );
        copyToPanel.setImage( image );
        copyToPanel.repaint();
    }
    
    private void locate()
    {
        String      color   = "\"color: black;\"";
        String      indent  = "&nbsp;&nbsp;&nbsp;&nbsp;";
        Window[]    windows = Window.getWindows();
        for ( Window window : windows )
        {
            String  title   = "";
            if ( window instanceof JFrame )
                title = ((JFrame)window).getTitle();
            else if ( window instanceof JDialog )
                title = ((JDialog)window).getTitle();
            else
                title = "(Unknown)";
            String  type    = window.getClass().getSimpleName();
            String  loc     = window.getLocation().toString();
            String  size    = window.getSize().toString();
            log.append( "Window: " + title, color );
            log.append( indent + "type: " + type, color );
            log.append( indent + "location: " + loc, color );
            log.append( indent + "size: " + size, color );
        }
    }
    
    private int getIntVal( JTextField textField, int defValue )
    {
        int     val     = defValue;
        String  text    = textField.getText();
        try
        {
            val = Integer.parseInt( text );
        }
        catch ( NumberFormatException exc )
        {
            String  msg = "\"" + text + "\" is not a valid integer";
            log.append( msg, "\"color: red; font-style: italic;\"" );
            textField.setText( "## ERROR" );
        }
        return val;
    }
    
    @SuppressWarnings("serial")
    private class CopyToPanel extends JPanel
    {
        private static final int    margin      = 50;
        private final GreekTile     greekTile   = new GreekTile();
        private final BufferedImage tile        = greekTile.getTile();
        
        private BufferedImage   copy    = null;
        private Graphics2D      gtx;
        
        public CopyToPanel()
        {
            Dimension   size    = new Dimension( 500, 300 );
            setMinimumSize( size );
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
            if ( copy != null )
            {
                int         dimWidth    = ( copy.getWidth() + 2 * margin );
                int         dimHeight   = ( copy.getHeight() + 2 * margin );
                Dimension   dim         = new Dimension( dimWidth, dimHeight );
                setPreferredSize( dim );
                revalidate();
            }
            
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
