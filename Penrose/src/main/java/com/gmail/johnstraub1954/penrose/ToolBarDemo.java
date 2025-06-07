package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class ToolBarDemo
{
    private static final ClassLoader classLoader    = ToolBarDemo.class.getClassLoader();
    private static final String title       = "Penrose Tiles Toolbar";
    private final JToolBar      toolBar     = 
        new JToolBar( title, JToolBar.HORIZONTAL );
    private static final int    iconSize  = 16;
    private static final Dimension  buttonDim   = new Dimension( iconSize, iconSize );
    private final PCanvas       canvas  = PCanvas.getDefaultCanvas();

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
    
    private static final String redLEDToolTip       = 
        "No compatible shapes selected";
    private static final String yellowLEDToolTip    = 
        "Compatible shapes selected";
    private static final String greenLEDToolTip     = "Ready to snap";

    private static final String doubleLeftArrowToolTip    = 
        "Shift selected shapes left";
    private static final String doubleRightArrowToolTip    = 
        "Shift selected shapes right";
    private static final String doubleUpArrowToolTip    = 
        "Shift selected shapes up";
    private static final String doubleDownArrowToolTip    = 
        "Shift selected shapes down";
    private static final String leftArrowToolTip    = 
        "Source select previous side";
    private static final String rightArrowToolTip    = 
        "Source select next side";
    private static final String rotateRightToolTip    = 
        "Rotate right (selected)";
    private static final String rotateLeftToolTip    = 
        "Rotate left (selected)";
    private static final String sourceSelectLeftToolTip    = 
        "Source select previous side";
    private static final String sourceSelectRightToolTip    = 
        "Source select next side";
    private static final String destinationSelectLeftToolTip    = 
        "Destination select previous side";
    private static final String destinationSelectRightToolTip    = 
        "Destination select next side";
    private static final String snapToolTip    = 
        "Snap selected shapes together";
    private static final String exitToolTip    = 
        "Exit";
    private final ButtonDesc[]          buttonDescs =
    {
        new ButtonDesc( 
            "redLED.png", 
            redLEDToolTip, 
            () -> new JLabel(), 
            null
        ),
        new ButtonDesc( 
            "yellowLED.png", 
            yellowLEDToolTip, 
            () -> new JLabel(), 
            null 
        ),
        new ButtonDesc( 
            "greenLED.png", 
            greenLEDToolTip, 
            () -> new JLabel(), 
            null 
        ),
        new ButtonDesc( 
            "DoubleLeftArrow.png", 
            doubleLeftArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( -4, 0 ) ) 
        ),
        new ButtonDesc( 
            "DoubleRightArrow.png", 
            doubleRightArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( 4, 0 ) ) 
        ),
        new ButtonDesc( 
            "DoubleUpArrow.png",
            doubleUpArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( 0, -4 ) ) 
        ),
        new ButtonDesc( 
            "DoubleDownArrow.png",
            doubleDownArrowToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.move( 0, 4 ) ) 
        ),
        new ButtonDesc( 
            "RotateRight16.png", 
            rotateRightToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.rotate( PShape.D18 ) )
        ),
        new ButtonDesc( 
            "RotateLeft16.png", 
            rotateLeftToolTip, 
            () -> new JButton(), 
            e -> action( p -> p.rotate( -PShape.D18 ) )
        ),
        new ButtonDesc( 
            "SourceSelectLeft.png", 
            sourceSelectLeftToolTip, 
            () -> new JButton(), 
            null 
        ),
        new ButtonDesc( 
            "SourceSelectRight.png", 
            sourceSelectRightToolTip, 
            () -> new JButton(), 
            null 
        ),
        new ButtonDesc( 
            "DestinationSelectLeft.png", 
            destinationSelectLeftToolTip, 
            () -> new JButton(), 
            null 
        ),
        new ButtonDesc( 
            "DestinationSelectRight.png", 
            destinationSelectRightToolTip, 
            () -> new JButton(), 
            null 
        ),
        new ButtonDesc( 
            "Snap.png", 
            snapToolTip, 
            () -> new JButton(), 
            null 
        ),
        new ButtonDesc( 
            "Exit.png", 
            exitToolTip, 
            () -> new JButton(), 
            e -> System.exit( 0 ) 
        ),
    };

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
        for ( ButtonDesc desc : buttonDescs )
            toolBar.add( desc.getComponent() );
    }
    
    private static ImageIcon getIcon( String path )
    {
        ImageIcon   icon    = null;
        try
        {
            URL         url     = classLoader.getResource( path );
            if ( url == null )
                throw new FileNotFoundException();
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
    
    private void action( Consumer<PShape> consumer )
    {
        List<PShape>    list    = canvas.getSelected();
        list.forEach( consumer );
        canvas.repaint();
    }

    
    public static class ButtonDesc
    {
        public final JComponent component;
        
        public ButtonDesc( 
            String path, 
            String toolTip, 
            Supplier<JComponent> getter,
            ActionListener action
        )
        {
            component = getter.get();
            Icon    icon    = getIcon( path );
            component.setToolTipText( toolTip );
            if ( component instanceof AbstractButton )
            {
                AbstractButton  button  = (AbstractButton)component;
                button.set
                button.addActionListener( action );
                button.setIcon( icon );
            }
            else if ( component instanceof JLabel )
            {
                JLabel  label   = (JLabel)component;
                label.setIcon( icon );
            }
            else
                ;
        }
        
        public JComponent getComponent()
        {
            return component;
        }
    }
}
