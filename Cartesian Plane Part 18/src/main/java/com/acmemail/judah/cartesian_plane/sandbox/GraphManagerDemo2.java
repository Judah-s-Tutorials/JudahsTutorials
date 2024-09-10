package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.IntConsumer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.GraphManager;
import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This is a simple application
 * to demonstrate how to configure a GraphPanel.
 * The center/right of the application frame
 * is a JPanel ({@link #canvas}) displaying graph
 * via an encapsulated Temp1.
 * The left of the application frame
 * contains controls
 * that allow the operator
 * to adjust the margins of canvas,
 * and the weight of the text
 * drawn on its x- and y-axes,
 * 
 * @author Jack Straub
 */
public class GraphManagerDemo2
{
    /** 
     * The profile shared with the Temp1.
     * @see Canvas
     */
    private final Profile   profile     = new Profile();
    private final Canvas    canvas      = new Canvas( profile );
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( GraphManagerDemo2::new );
    }
    
    /**
     * Constructor.
     * Creates and configures the application GUI.
     * Must be invoked via the EDT.
     */
    private GraphManagerDemo2()
    {
        JPanel      controls    = getControlPanel();
        
        JFrame      frame       = new JFrame( "Temp1 Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        contentPane.add( canvas, BorderLayout.CENTER );
        contentPane.add( controls, BorderLayout.WEST );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Assembles a panel containing the controls
     * that manage a few of the properties
     * of the application's grid.
     * 
     * @return  the assembled panel
     */
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel( new GridLayout( 5, 2 ) );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        panel.add( new JLabel( "Bold", SwingConstants.RIGHT ) );
        JCheckBox   bold    = new JCheckBox();
        panel.add( bold );
        bold.addItemListener( e -> {
            boolean             selected    = bold.isSelected();
            GraphPropertySet    props       = profile.getMainWindow();
            props.setBold( selected );
            canvas.repaint();
        });
        
        panel.add( new JLabel( "Top Margin", SwingConstants.RIGHT ) );
        panel.add( getSpinner( 
            canvas.topMargin, i -> canvas.topMargin = i ) 
        );
        
        panel.add( new JLabel( "Left Margin", SwingConstants.RIGHT ) );
        panel.add( getSpinner( 
            canvas.leftMargin, i -> canvas.leftMargin = i ) 
        );
        
        panel.add( new JLabel( "Bottom Margin", SwingConstants.RIGHT ) );
        panel.add( getSpinner( 
            canvas.bottomMargin, i -> canvas.bottomMargin = i ) 
        );
        
        panel.add( new JLabel( "Right Margin", SwingConstants.RIGHT ) );
        panel.add( getSpinner( 
            canvas.rightMargin, i -> canvas.rightMargin = i ) 
        );
        
        // This prevents the BoxLayout in the main panel from
        // stretching the height of the main panel's components.
        JPanel  basePanel   = new JPanel();
        basePanel.add( panel );
        
        return basePanel;
    }
    
    /**
     * Create and configure a JSpinner to be used
     * for modifying a property.
     * The spinner is outfitted with a SpinnerNumberModel
     * and the width of its constituent text field is reduced.
     * It is provided with a change listener
     * the invokes the given consumer
     * and repaints the {@link #canvas}.
     * 
     * @param value     the initial value of the property
     * @param setter    
     *      consumer to change the property value
     *      when the value of the spinner changes
     *      
     * @return  the created spinner
     */
    private JSpinner getSpinner( int value, IntConsumer setter )
    {
        SpinnerNumberModel  model       = 
            new SpinnerNumberModel( value, 0, Integer.MAX_VALUE, 1 );
        JSpinner            spinner     = new JSpinner( model );
        JComponent          editor      = spinner.getEditor();
        if ( !(editor instanceof DefaultEditor) )
            throw new ComponentException( "Unexpected editor type" );
        JTextField  textField   = 
            ((DefaultEditor)editor).getTextField();
        textField.setColumns( 6 );

        spinner.addChangeListener( e -> {
            int             newValue    = model.getNumber().intValue();
            setter.accept( newValue );
            canvas.repaint();
        });
        return spinner;
    }

    /**
     * An object of this class is a JPanel
     * that displays a graph
     * under the control of a Temp1.
     * 
     * @author Jack Straub
     */
    @SuppressWarnings("serial")
    private static class Canvas extends JPanel
    {
        /** 
         *  Path to the image to tile the window.
         *  @see #tile() 
         */
        private static final String imagePath   = "sandbox/Bricks3.png";
        /** Background image for tiling the window. */
        private final   BufferedImage background;
        
        /** 
         * Rectangle that defines the margins of the graph. The rectangle
         * is reconfigured every time the paintComponent method 
         * is invoked.
         */
        private final   Rectangle     rect      = new Rectangle();
        /** 
         * Left margin of the grid. Expected to be modified
         * from the outer class.
         */
        public int      leftMargin              = 100;
        /** 
         * Top margin of the grid. Expected to be modified
         * from the outer class.
         */
        public int      topMargin               = 50;
        /** 
         * Right margin of the grid. Expected to be modified
         * from the outer class.
         */
        public int      rightMargin             = 25;
        /** 
         * Bottom margin of the grid. Expected to be modified
         * from the outer class.
         */
        public int      bottomMargin            = 25;
        
        /** 
         * Responsible for drawing the graph contained
         * in the application frame.
         * It is initialized in the constructor,
         * and utilized in the {@link #paintComponent(Graphics)} method.
         */
        private final GraphManager  drawManager;
        
        /** 
         * Current width of the window. Updated every time
         * the {@link #paintComponent(Graphics)} method
         * is invoked.
         */
        private int         width;
        /** 
         * Current height of the window. Updated every time
         * the {@link #paintComponent(Graphics)} method
         * is invoked.
         */
        private int         height;
        /** 
         * Graphics context that controls drawing in this window.
         * Updated every time the {@link #paintComponent(Graphics)} method
         * is invoked.
         */
        private Graphics2D  gtx;
        
        /**
         * Constructor.
         * Fully configures the state of this window.
         * 
         * @param profile   
         *      profile to share with the encapsulated Temp1
         */
        public Canvas( Profile profile )
        {
            drawManager = new GraphManager( profile );
            background = getBackgroundImage();
            Dimension   dim = new Dimension( 300, 500 );
            setPreferredSize( dim );
        }

        /**
         * Redraw the content of this window.
         * The background is tiled,
         * and the Temp1 is updated
         * with the values of the graph margins.
         * 
         * @param graphics  graphics context for drawing
         */
        @Override
        public void paintComponent( Graphics graphics )
        {
            width = getWidth();
            height = getHeight();
            gtx = (Graphics2D)graphics.create();
            tile();
//            gtx.setColor( Color.WHITE );
//            gtx.fillRect( 0, 0, width, height );
            
            rect.x = leftMargin;
            rect.y = topMargin;
            rect.width = width - leftMargin - rightMargin;
            rect.height = height - topMargin - bottomMargin;
            drawManager.refresh( gtx, rect );
            drawManager.drawAll();

            gtx.dispose();
        }
        
        /**
         * Tiles this window with its background.
         * 
         * @see Canvas
         * @see #getBackgroundImage()
         */
        private void tile()
        {
            int imageWidth  = background.getWidth();
            int imageHeight = background.getHeight();
        
            for ( int row = 0 ; row < height ; row += imageHeight )
                for ( int col = 0 ; col < width ; col += imageWidth )
                    gtx.drawImage( background, col, row, this );
        }
        
        /**
         * Constructs the background image 
         * for tiling this window.
         * Based on the image specified by {@link #imagePath}.
         * 
         * @return  the image specified by {@link #imagePath}
         */
        private BufferedImage getBackgroundImage()
        {
            Class<?>    clazz       = getClass();
            ClassLoader loader      = clazz.getClassLoader();
            InputStream inStream    = 
                loader.getResourceAsStream( imagePath );
            if ( inStream == null )
            {
                System.err.println( imagePath + " not found" );
                System.exit( 1 );
            }
            
            BufferedImage   image   = null;
            try
            {
                image = ImageIO.read( inStream );
            }
            catch ( IOException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
            return image;
        }
    }
}
