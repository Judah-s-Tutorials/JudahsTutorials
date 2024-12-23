package com.gmail.johnstraub1954.penrose;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DartDemo1
{
    /** Unicode for an up-arrow. */
    private static final String         upArrow     = "\u21e7";
    /** Unicode for a down-arrow. */
    private static final String         downArrow   = "\u21e9";
    /** Unicode for a left-arrow. */
    private static final String         leftArrow   = "\u21e6";
    /** Unicode for a right-arrow. */
    private static final String         rightArrow  = "\u21e8";
    /** Unicode for a rotate-left arrow. */
    private static final String         rotateLeft  = "\u21B6";
    /** Unicode for a rotate-right arrow. */
    private static final String         rotateRight = "\u21B7";
    
    private static double       longSide    = 150;
    private Canvas  canvas;
    
    public static void main(String[] args)
    {
        DartDemo1   demo2 = new DartDemo1();
        SwingUtilities.invokeLater( () -> demo2.build() );
    }
    
    public void build()
    {
        JFrame  frame   = new JFrame( "Dart Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new JPanel( new BorderLayout() );
        canvas = new Canvas( longSide );
        pane.add( canvas, BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( pane );
        frame.setLocation( 350, 100 );
        frame.pack();
        
        canvas.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed( MouseEvent evt )
                {
                    int xco = evt.getX();
                    int yco = evt.getY();
                    System.out.println( canvas.dart.contains( xco, yco ) );
                }
            }
        );
        
        frame.setVisible( true );
    }
    
    private JPanel getControlPanel()
    {
        final Dimension rigidDim    = new Dimension( 5, 0 );
        JPanel  panel   = new JPanel();
        panel.add( getTranslatePanel() );
        panel.add( Box.createRigidArea( rigidDim ) );
        panel.add( getRotatePanel() );
        panel.add( Box.createRigidArea( rigidDim ) );
        
        JButton exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        panel.add( exit );
        return panel;
    }
    
    private JPanel getTranslatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 2, 3 ) );
        JButton upButton    = new JButton( upArrow );
        JButton downButton  = new JButton( downArrow );
        JButton leftButton  = new JButton( leftArrow );
        JButton rightButton = new JButton( rightArrow );
        
        upButton.addActionListener( e -> 
            action( () -> canvas.getDart().move( 0, -8 ) )
        );
        downButton.addActionListener( e -> 
            action( () -> canvas.getDart().move( 0, 8 ) )
        );
        leftButton.addActionListener( e -> 
            action( () -> canvas.getDart().move( -8, 0 ) )
        );
        rightButton.addActionListener( e -> 
            action( () -> canvas.getDart().move( 8, 0 ) )
        );
        
        panel.add( new JLabel( "" ) );
        panel.add( upButton );
        panel.add( new JLabel( "" ) );
        panel.add( leftButton );
        panel.add( downButton );
        panel.add( rightButton );
        return panel;
    }
    
    private JPanel getRotatePanel()
    {
        JPanel  panel       = new JPanel( new GridLayout( 1, 2, 5, 5 ) );
        JButton leftButton  = new JButton( rotateLeft );
        JButton rightButton = new JButton( rotateRight );
        
        float   rotateIncr  = (float)(Math.PI / 32);
        leftButton.addActionListener( e -> 
            action( () -> canvas.getDart().rotate( -rotateIncr ) )
        );
        rightButton.addActionListener( e -> 
            action( () -> canvas.getDart().rotate( rotateIncr ) )
        );
        
        panel.add( leftButton );
        panel.add( rightButton );
        return panel;
    }
    
    private void action( Runnable runner )
    {
        runner.run();
        canvas.repaint();
    }

    private static class Canvas extends JPanel
    {
        private static final long serialVersionUID = 1L;

        private final PDart dart;
        
        private Graphics2D  gtx;
        private int         width;
        private int         height;
        
        public Canvas( double longSide )
        {
            setPreferredSize( new Dimension( 500, 500 ) );
            dart = new PDart( longSide );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            gtx = (Graphics2D)graphics.create();
            width = getWidth();
            height = getHeight();
            
            gtx.setColor( new Color( 200, 200, 200 ) );
            gtx.fillRect( 0, 0, width, height );
            
            dart.render( gtx );
            Rectangle2D rect    = dart.getBounds();
            gtx.setColor( Color.YELLOW );
            gtx.draw( rect );

            gtx.dispose();
        }
        
        public PDart getDart()
        {
            return dart;
        }
    }
}
