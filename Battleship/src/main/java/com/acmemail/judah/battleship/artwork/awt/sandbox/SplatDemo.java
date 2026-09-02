package com.acmemail.judah.battleship.artwork.awt.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.battleship.artwork.awt.Splat;

/**
 * This is an application that allows you
 * to experiment with different splat parameter configurations.
 * Change a parameter and push the apply button
 * to see the affect of the change;
 * the application does minimal error checking,
 * so be careful when modifying parameters.
 * Colors are specified as hexadecimal integers of the form
 * {@code #AARRGGBB}. 
 * To reset the parameters to their default values,
 * push the reset button.
 * To terminate the application,
 * close the application frame
 * or push the exit button.
 * <p>
 * No facilities are provided
 * for saving/restoring parameter values.
 */
public class SplatDemo
{
    /** The panel in which the image of the splat is displayed. */
    private final SplatPanel    splatPanel   = new SplatPanel();
    /** Text boxes for displaying and modifying parameters. */
    private final InputPanel    inputPanel   = new InputPanel();
    
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new SplatDemo() );
    }
    
    /**
     * Constructor.
     * Fully configures the application frame,
     * packs it,
     * and makes it visible.
     * Must be invoked from the EDT.
     */
    public SplatDemo()
    {
        JFrame  frame       = new JFrame( "SPLAT" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        contentPane.add( splatPanel, BorderLayout.CENTER );
        contentPane.add( inputPanel, BorderLayout.WEST );
        contentPane.add( new ControlPanel(), BorderLayout.SOUTH );
        
        inputPanel.set( splatPanel.getSplat().getParams() );
        
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Encapsulation of the panel
     * that contains the Apply, Reset, and Exit buttons.
     */
    @SuppressWarnings("serial")
    private class ControlPanel extends JPanel
    {
        /**
         * Constructor.
         * Fully configures the control panel.
         */
        public ControlPanel()
        {
            JButton apply   = new JButton( "Apply" );
            JButton exit    = new JButton( "Exit" );
            JButton reset   = new JButton( "Reset" );
            
            apply.addActionListener( e -> {
                Splat           splat   = splatPanel.getSplat();
                Splat.Params    params  = inputPanel.get();
                splat.setParams( params );
                splatPanel.setSplat( splat );
            });
            
            exit.addActionListener( e -> System.exit( 0 ) );
            
            reset.addActionListener( e -> {
                Splat   splat   = new Splat();
                inputPanel.set( splat.getParams() );
                splatPanel.setSplat( splat );
            });
            
            add( reset );
            add( apply );
            add( exit );
        }
    }
    
    /**
     * Panel that contains the text fields
     * for displaying and modifying parameters.
     * The layout consists
     * of a vertical display
     * of labels and text boxes
     * encapsulated in {@linkplain PropertyPanel} instances.
     * Visual aesthetics depend on the {@code PropertyPanels}
     * layout labels and text boxes horizontally,
     * with every component having the same dimensions.
     */
    @SuppressWarnings("serial")
    private class InputPanel extends JPanel
    {
        /** 
         * Identifies the {@code PropertyPanel} that encapsulates
         * the background color parameter.
         */
        private int     bgColor;
        /** 
         * Identifies the {@code PropertyPanel} that encapsulates
         * the fill color parameter.
         */
        private int     fillColor;
        /** 
         * Identifies the {@code PropertyPanel} that encapsulates
         * the edge color parameter.
         */
        private int     edgeColor;
        /** 
         * Identifies the {@code PropertyPanel} that encapsulates
         * the crown circle radius parameter.
         */
        private int     crownRadius;
        /** 
         * Identifies the {@code PropertyPanel} that encapsulates
         * the inner circle radius percentage parameter.
         */
        private int     innerRadiusPC;
        /** 
         * Identifies the {@code PropertyPanel} that encapsulates
         * the number-of-sides in polygon parameter.
         */
        private int     numSides;
        /** Map of {@code PropertyPanel} ids to {@code PropertyPanels}. */
        Map<Integer,PropertyPanel>  valueMap    = new HashMap<>();
        
        /**
         * Constructor.
         * Fully configures an instance of this class.
         */
        public InputPanel()
        {
            BoxLayout   layout  = new BoxLayout( this, BoxLayout.Y_AXIS );
            setLayout( layout );
            int         ident   = 0;
            
            bgColor = ident++;
            add( new PropertyPanel( "BG Color", bgColor ) );
            fillColor = ident++;
            add( new PropertyPanel( "Fill Color", fillColor ) );
            edgeColor = ident++;
            add( new PropertyPanel( "Edge Color", edgeColor ) );
            crownRadius = ident++;
            add( new PropertyPanel( "Crown Radius", crownRadius ) );
            innerRadiusPC = ident++;
            add( new PropertyPanel( "Inner Radius %", innerRadiusPC ) );
            numSides = ident++;
            add( new PropertyPanel( "Num Sides", numSides ) );
            
            Arrays.stream( getComponents() )
                .filter( c -> c instanceof PropertyPanel )
                .map( c -> (PropertyPanel)c )
                .forEach( p -> valueMap.put( p.getIdent(), p ) );
        }
        
        /**
         * Display the given parameter values.
         * 
         * @param params    the given parameter values
         */
        public void set( Splat.Params params )
        {
            PropertyPanel   panel   = valueMap.get( bgColor );
            panel.set( params.backgroundColor );
            panel = valueMap.get( fillColor );
            panel.set( params.fillColor );
            panel = valueMap.get( edgeColor );
            panel.set( params.edgeColor );
            panel = valueMap.get( crownRadius );
            panel.set( params.crownRadius );
            panel = valueMap.get( innerRadiusPC );
            panel.set( params.innerRadiusPC );
            panel = valueMap.get( numSides );
            panel.set( params.numSides );
        }
        
        /**
         * Transfer the values in this window
         * into a {@code Splat.Params} object.
         * Error detection is limited to 
         * preventing the application from crashing
         * when the operator enters an invalid value.
         * No error reporting is performed.
         * 
         * @return 
         *      a {@code Splat.Params} object 
         *      containing modified parameter values
         */
        public Splat.Params get()
        {
            Splat.Params params = splatPanel.getSplat().getParams();
            PropertyPanel   panel   = valueMap.get( bgColor );
            params.backgroundColor = panel.asColor();
            
            panel = valueMap.get( fillColor );
            params.fillColor = panel.asColor();
            
            panel = valueMap.get( edgeColor );
            params.edgeColor = panel.asColor();
            
            panel = valueMap.get( crownRadius );
            params.crownRadius = panel.asDouble();
            
            panel = valueMap.get( innerRadiusPC );
            params.innerRadiusPC = panel.asDouble();

            panel = valueMap.get( numSides );
            params.numSides = panel.asInt();
            
            return params;
        }
    }
    
    /**
     * Panel that contains the label and text box
     * for displaying and modifying a single parameter.
     * Components are laid out horizontally 
     * in a {@code GridLayout}.
     * Every text box is the same size,
     * and is intended to be wider than the accompanying label,
     * which will result in every panel 
     * having the same dimensions.
     * The client provides a unique integer ID,
     * which is stored,
     * and used as the basis for determining
     * if two {@code PropertyPanels} are equal.
     */
    @SuppressWarnings("serial")
    private static class PropertyPanel extends JPanel
    {
        /** Border around the edge of the panel. */
        private static final Border border  =
            BorderFactory.createEmptyBorder( 0, 3, 3, 3 );
        /** Encapsulated label. */
        private final JLabel        label;
        /** Encapsulated text box. */
        private final JTextField    value;
        /** Encapsulated ID. */
        private final int           ident;
        
        /**
         * Constructor.
         * Full configures this instance.
         * 
         * @param text  text for the encapsulated label
         * @param ident the encapsulated ID
         */
        public PropertyPanel( String text, int ident )
        {
            super( new GridLayout( 1, 2 ) );
            setBorder( border );
            label = new JLabel( text );
            value = new JTextField( 10 );
            this.ident = ident;
            
            add( label );
            add( value );
        }
        
        /**
         * Gets the encapsulated ID.
         * 
         * @return  the encapsulated ID
         */
        public int getIdent()
        {
            return ident;
        }
        
        /**
         * Converts a given color
         * to a hexadecimal string
         * and stores it in the encapsulated text field.
         * 
         * @param color the given color
         */
        public void set( Color color )
        {
            int     iColor  = color.getRGB();
            String  sColor  = String.format( "#%08X", iColor );
            value.setText( sColor );
        }
        
        /**
         * Converts a given integer to a string
         * and stores it in the encapsulated text field.
         * 
         * @param iValue the given integer
         */
        public void set( int iValue )
        {
            value.setText( "" + iValue );
        }
        
        /**
         * Converts a given decimal number to a string
         * and stores it in the encapsulated text field.
         * 
         * @param dValue the given decimal number
         */
        public void set( double dValue )
        {
            value.setText( "" + dValue );
        }
        
        /**
         * Reads the content of the encapsulated text field,
         * converts it to a color,
         * and returns the color.
         * The color must be encapsulated 
         * in a hexadecimal string
         * of the form #AARRGGBB.
         * If the text cannot be converted,
         * null is returned.
         * 
         * @return  the encapsulated color, or null if invalid
         */
        public Color asColor()
        {
            Color   color   = null;
            try
            {
                String  text        = value.getText();
                long    intColor    = Long.decode( text );
                color = new Color( (int)intColor, true );
            }
            catch ( NumberFormatException exc )
            {
                System.out.println( exc.getMessage() );
            }
            
            return color;
        }
        
        /**
         * Reads the content of the encapsulated text field,
         * converts it to an Integer,
         * and returns the Integer.
         * The encapsulated text field
         * must contain a properly formatted integer.
         * If the text cannot be converted,
         * null is returned.
         *
         * @return the converted integer, or null if invalid
         */
        public Integer asInt()
        {
            Integer numValue    = null;
            try
            {
                numValue = Integer.parseInt( value.getText() );
            }
            catch ( NumberFormatException ex )
            {
            }
            
            return numValue;
        }
        
        /**
         * Reads the content of the encapsulated text field,
         * converts it to an decimal number,
         * and returns the decimal number.
         * The encapsulated text field
         * must contain a properly formatted number.
         * If the text cannot be converted,
         * null is returned.
         *
         * @return the converted number, or null if invalid
         */
        public Double asDouble()
        {
            Double  numValue    = null;
            try
            {
                numValue = Double.parseDouble( value.getText() );
            }
            catch ( NumberFormatException ex )
            {
            }
            
            return numValue;
        }
        
        @Override
        public int hashCode()
        {
            return ident;
        }
        
        @Override
        public boolean equals( Object obj )
        {
            boolean result  = false;
            if ( obj instanceof PropertyPanel that )
                result = this.ident == that.ident;
            return result;
        }
    }

    /**
     * Simple panel that displays a splat
     * in the center of its window.
     */
    @SuppressWarnings("serial")
    private static class SplatPanel extends JPanel
    {
        /** Rendering hints to control the quality of drawing. */
        private static final Map<?,?>   renderingHints  =
            Map.of(
                RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_STROKE_CONTROL, 
                    RenderingHints.VALUE_STROKE_NORMALIZE,
                RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
                            
        /** The currently configured splat. */
        private Splat   splat   = new Splat();
        
        /**
         * Constructor.
         * Full configures this SplatPanel.
         */
        public SplatPanel()
        {
            Dimension   prefSize    = new Dimension( 200, 200 );
            setPreferredSize( prefSize );
        }
        
        /**
         * Gets the currently configured splat.
         * 
         * @return  the currently configured splat
         */
        public Splat getSplat()
        {
            return splat;
        }
        
        /**
         * Changed the currently configured splat,
         * causing the window to be immediately repainted.
         *  
         * @param splat the new splat
         */
        public void setSplat( Splat splat )
        {
            this.splat = splat;
            repaint();
        }
    
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            Graphics2D  gtx     = (Graphics2D)graphics;
            
            gtx.setRenderingHints( renderingHints );
            int         width   = getWidth();
            int         height  = getHeight();
            gtx.setColor( Color.LIGHT_GRAY ); 
            gtx.fillRect( 0, 0, width, height );
            
            gtx.setRenderingHint( 
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON 
            );
            
            Image   image   = splat.getImage();
            int     iWidth  = image.getWidth( this );
            int     iHeight = image.getHeight( this );
            int     xco     = width / 2 - iWidth / 2;
            int     yco     = height / 2 - iHeight / 2;
            gtx.setColor( Color.BLACK );
            gtx.drawRect( xco, yco, iWidth, iHeight );
            gtx.drawImage( image, xco, yco, this );
        }
    }
}
