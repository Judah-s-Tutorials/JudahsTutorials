package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class ToolBarDemo
{
    private static final ClassLoader classLoader    = ToolBarDemo.class.getClassLoader();
    private static final String title       = "Penrose Tiles Toolbar";
    private final JToolBar      toolBar     = 
        new JToolBar( title, JToolBar.HORIZONTAL );
    private final ImageIcon canBeMapped = getIcon( "CanBeMapped.png" );
    private final ImageIcon isMapped    = getIcon( "IsMapped.png" );
    private final ImageIcon noMapping   = getIcon( "NoMapping.png" );
    
    private static final String redLight    = "RedLED.png";
    private static final String rightArrow = "RightArrow.png";
    private static final int    iconSize  = 16;
    private static final Dimension  buttonDim   = new Dimension( iconSize, iconSize );

    /** Unicode for an up-arrow. */
    private static final String         upArrow     = "\u21e7";
    /** Unicode for a down-arrow. */
    private static final String         downArrow   = "\u21e9";
    /** Unicode for a left-arrow. */
    private static final String         leftArrow   = "\u21e6";
    /** Unicode for a right-arrow. */
//    private static final String         rightArrow  = "\u21e8";
    /** Unicode for a rotate-left arrow. */
    private static final String         rotateLeft  = "\u21B6";
    /** Unicode for a rotate-right arrow. */
    private static final String         rotateRight = "\u21B7";
    
    public static void main( String[] args )
    {
        ToolBarDemo demo    = new ToolBarDemo();
        SwingUtilities.invokeLater(  () -> demo.build() );
    }

    public ToolBarDemo()
    {
        // TODO Auto-generated constructor stub
    }
    
    public void build()
    {
        JFrame  frame       = new JFrame( "Tool Bar Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel       = new JPanel( new BorderLayout() );
        makeToolBar();
        panel.add( toolBar, BorderLayout.NORTH );
        frame.setContentPane( panel );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void makeToolBar()
    {
        String[]    arr = 
        { 
            "redLED.png", 
            "DoubleLeftArrow.png",
            "DoubleRightArrow.png",
            "DoubleUpArrow.png",
            "DoubleDownArrow.png",
            "LeftArrow.png",
            "RightArrow.png",
        };
        Stream.of( arr )
            .map( ToolBarDemo::getIcon )
            .map( JButton::new )
            .forEach( toolBar::add );
    }
    
    private static ImageIcon getIcon( String path )
    {
        ImageIcon   icon    = null;
        try
        {
            URL         url     = classLoader.getResource( path );
            Image       image   = ImageIO.read( url );
            image = image.getScaledInstance( iconSize, iconSize, Image.SCALE_SMOOTH );
            icon = new ImageIcon( image );
        }
        catch ( IOException | IllegalArgumentException exc )
        {
            System.err.println( path );
            exc.printStackTrace();
            System.exit( 1 );
        }
        return icon;
    }
}
