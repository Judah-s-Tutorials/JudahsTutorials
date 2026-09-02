package com.acmemail.judah.battleship.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.acmemail.judah.battleship.artwork.awt.GridFrame;
import com.acmemail.judah.battleship2D.Grid2D;

public class GridWindowParentDemo
{
    private static GridFrame    gridFrame;
    private static Parent       controller; 

    public static void main(String[] args)
    {
        IntStream.range( 1, 6 )
            .mapToObj( i -> "Opponent " + i )
            .forEach( Grid2D::new );
        gridFrame = GridFrame.getFrame( () -> new Parent() );
        controller = (Parent)gridFrame.getClient();

        controller.repaint();
    }
        
    public static class Parent extends JPanel
    {
        /** Default serial version UID. */
        private static final long serialVersionUID = 1L;
        
        private final int       border1Width    = 5;
        private final int       border2Width    = 5;
        private final Color     border2Color    = Color.BLUE;
        private final Border    selectedBorder  =
            BorderFactory.createLineBorder( Color.YELLOW, border2Width );

        private JPanel  selectedWindow          = null;
        private Border  selectedWindowBorder    = null;

        public Parent()
        {
            Grid2D  home    = Grid2D.getHomeGrid();
            setGUILayout();
            setMaxDimensions();
            add( getTitleComponent( home ) );
            for ( Grid2D grid : Grid2D.getAllGrids() )
            {
                if ( grid != home )
                    add( getTitleComponent( grid ) );
            }
        }
        
        private void setGUILayout()
        {
            List<Grid2D>    allGrids    = Grid2D.getAllGrids();
            int             gridCount   = allGrids.size();
            int             guiLayoutNumRows;
            int             guiLayoutNumCols;
            if ( gridCount <= 3 )
            {
                guiLayoutNumRows = 1;
                guiLayoutNumCols = gridCount;
            }
            else if ( gridCount == 4 )
            {
                guiLayoutNumRows = 2;
                guiLayoutNumCols = 2;
            }
            else
            {
                guiLayoutNumRows = (int)Math.ceil( gridCount  / 3.);
                guiLayoutNumCols = 3;
            }

            setLayout( new GridLayout( guiLayoutNumRows, guiLayoutNumCols ) );
        }
        
        private void setMaxDimensions()
        {
            GraphicsConfiguration config    = 
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
            Insets      insets = 
                Toolkit.getDefaultToolkit().getScreenInsets( config );
            Rectangle   bounds = config.getBounds();
            
            int maxWidth    = bounds.width - insets.left - insets.right;
            int maxHeight   = bounds.height - insets.top - insets.bottom;
            setMaximumSize( new Dimension( maxWidth, maxHeight ) );
        }
    
        private JPanel getBorderPanel()
        {
            JPanel  panel   = new JPanel();
            Border  outerBorder = 
                BorderFactory.createEmptyBorder( 
                    border1Width, 
                    border1Width, 
                    border1Width, 
                    border1Width 
                );
            Border  innerBorder =
                BorderFactory.createLineBorder( border2Color, border2Width );
            Border  border      =
                BorderFactory.createCompoundBorder( innerBorder, outerBorder );
            panel.setBorder( border );
            return panel;
        }

        private JComponent getTitleComponent( Grid2D grid)
        {
            // Title and controller should live inside the border
            // The border should be inside a scroll pane
            // The border component should never be resized
            JPanel  borderPanel = getBorderPanel();
            BoxLayout   layout = new BoxLayout( borderPanel, BoxLayout.Y_AXIS );
            borderPanel.setLayout( layout );

            String      name        = grid.getName();
            DemoWindow  demoWindow  = new DemoWindow( name ); 
            borderPanel.add( getTitle( name ) );
            borderPanel.add( demoWindow );
            
            // The dummy panel is to prevent the border panel from being resized
            JPanel  dummyPanel  = new JPanel();
            dummyPanel.add( borderPanel );
            JScrollPane pane    = new JScrollPane( dummyPanel );
            pane.addMouseListener(
                new MouseAdapter()
                {
                    @Override
                    public void mouseClicked( MouseEvent evt )
                    {
                        if ( evt.getButton() == MouseEvent.BUTTON1 )
                        {
                            selectWindow( borderPanel );
                            demoWindow.requestFocusInWindow();
                        }
                    }
                }
            );

            return pane;
        }

        /**
         * Highlight the given window
         * and restore the border of the previously
         * selected window, if any.
         *
         * @param window    the window to select
         */
        private void selectWindow( JPanel window )
        {
            if ( selectedWindow != null )
                selectedWindow.setBorder( selectedWindowBorder );

            selectedWindowBorder = window.getBorder();
            window.setBorder( selectedBorder );
            selectedWindow = window;
            repaint();
        }
    }

    private static JLabel   getTitle( String name )
    {
        String  titleText   =
            "<HTML><BODY style='font-size: 150%;'>"
            + name 
            + "</BODY></HTML>";
        JLabel  title   = new JLabel( titleText );
        return title;
    }
    
    @SuppressWarnings("serial")
    public static class DemoWindow extends JPanel
    {
        private static final double maxScale    = 3;
        private static final double minScale    = .1;
        private static final int    baseWidth   = 600;
        private static final int    baseHeight  = 500;
        private final double    scaleIncrement  = .5;
        private double          scaleFactor = 1;

        public DemoWindow( String label )
        {
            setFocusable( true );
            addKeyListener( new KeyDetector( label ) );
        }

        @Override
        public Dimension getPreferredSize()
        {
            int width   = (int)Math.round( baseWidth * scaleFactor );
            int height  = (int)Math.round( baseHeight * scaleFactor );
            return new Dimension( width, height );
        }

        @Override
        public void paintComponent( Graphics graph )
        {
            super.paintComponent( graph );

            Graphics2D  gtx             = (Graphics2D)graph;
            int         winWidth        = getWidth();
            int         winHeight       = getHeight();
            gtx.setColor( Color.LIGHT_GRAY );
            gtx.fillRect( 0, 0, winWidth, winHeight );

            gtx.scale( scaleFactor, scaleFactor );

            // Content geometry is computed from the fixed logical
            // (unscaled) size, then projected onto the panel's
            // actual (scaled) size by the transform above, so the
            // drawing keeps the same proportions at every scale.
            int         width           = baseWidth;
            int         height          = baseHeight;
            int         horizontalInset = (int)(width / 3. + .5);
            int         verticalInset   = (int)(height / 3. + .5);
            int         rectWidth       = width - 2 * horizontalInset;
            int         rectHeight      = height - 2 * verticalInset;
            Stroke      stroke          = new BasicStroke( 3 );
            gtx.setStroke( stroke );
            gtx.setColor( Color.red );
            gtx.drawRect( horizontalInset, verticalInset, rectWidth, rectHeight );
            
            FontRenderContext   context     = gtx.getFontRenderContext();
            Font                font        = gtx.getFont();
            String              label       = "Testing ... Testing";
            Rectangle2D         strBounds   = 
                font.getStringBounds( label, context );
            int                 strXco      = 
                (int)((width - strBounds.getWidth()) / 2 + .5);
            int                 strYco      = 
                (int)((height - strBounds.getHeight()) / 2 + .5) + font.getSize();
            gtx.setColor( Color.BLACK );
            gtx.drawString( label, strXco, strYco );
        }
        
        private class KeyDetector extends KeyAdapter
        {
            private final String    label;
            public KeyDetector( String label )
            {
                this.label = label;
            }
            
            @Override
            public void keyPressed( KeyEvent evt )
            {
                if ( evt.isControlDown() )
                {
                    boolean repaint = true;
                    int key = evt.getKeyCode();
                    if ( isPlus( key ) )
                    {
                        double  testFactor  = scaleFactor + scaleIncrement;
                        scaleFactor = Math.min( testFactor, maxScale );
                    }
                    else if ( isMinus( key ) )
                    {
                        double  testFactor  = scaleFactor - scaleIncrement;
                        scaleFactor = Math.max( testFactor, minScale );
                    }
                    else if ( isZero( key ) )
                    {
                        scaleFactor = 1;
                    }
                    else
                    {
                        System.out.println( "never mind - " + label );
                        repaint = false;
                    }
                    if ( repaint )
                    {
                        revalidate();
                        repaint();
                    }
                }
                else
                    System.out.println( "not ^" );
            }
            private static boolean isPlus( int key )
            {
                boolean result  =
                    key == KeyEvent.VK_PLUS || 
                    key == KeyEvent.VK_EQUALS;
                return result;
            }
            private static boolean isMinus( int key )
            {
                boolean result  =
                    key == KeyEvent.VK_MINUS || 
                    key == KeyEvent.VK_UNDERSCORE;
                return result;
            }
            private static boolean isZero( int key )
            {
                boolean result  =
                    key == KeyEvent.VK_0;
                return result;
            }
        }
    }
}
