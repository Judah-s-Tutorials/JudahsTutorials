package com.acmemail.judah.battleship.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GlassPaneDemo
{
    private final JFrame        appFrame    = new JFrame( "GlassPaneDemo" );
    private final GlassPanel    glassPane   = new GlassPanel();
    private final JCheckBox     checkBox    = new JCheckBox( "Make Visible" );
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new GlassPaneDemo() );
    }
    
    public GlassPaneDemo()
    {
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        appFrame.setContentPane( new MainPanel() );
        appFrame.setGlassPane( glassPane );
        appFrame.pack();
        appFrame.setVisible( true );
//        glassPane.setVisible( true );
    }
    
    private static JPanel getNorthBorder()
    {
        Color   lightBlue   = new Color( 0xADD8E6 );
        int     rows        = 2;
        int     cols        = 10;
        int     cells       = rows * cols;
        JPanel  panel       = new JPanel( new GridLayout( rows, cols ) );
        panel.setBackground( lightBlue );
        for ( int inx = 0 ; inx < cells ; ++inx )
            panel.add( new JLabel( "North Border" ) );
        return panel;
    }
    
    private static JPanel getEastBorder()
    {
        Color   lightRed    = new Color( 0x90EE90 );
        int     rows        = 10;
        int     cols        = 1;
        int     cells       = rows * cols;
        JPanel  panel       = new JPanel( new GridLayout( rows, cols ) );
        panel.setBackground( lightRed );
        for ( int inx = 0 ; inx < cells ; ++inx )
            panel.add( new JLabel( "East Border" ) );
        return panel;
    }
    
    private static JPanel getSouthBorder()
    {
        Color   yellow      = Color.YELLOW;
        int     rows        = 3;
        int     cols        = 5;
        int     cells       = rows * cols;
        JPanel  panel       = new JPanel( new GridLayout( rows, cols ) );
        panel.setBackground( yellow );
        for ( int inx = 0 ; inx < cells ; ++inx )
            panel.add( new JLabel( "South Border" ) );
        return panel;
    }
    
    private static JPanel getWestBorder()
    {
        Color   lightGreen  = new Color( 0x90EE90 );
        int     rows        = 10;
        int     cols        = 2;
        int     cells       = rows * cols;
        JPanel  panel       = new JPanel( new GridLayout( rows, cols ) );
        panel.setBackground( lightGreen );
        for ( int inx = 0 ; inx < cells ; ++inx )
            panel.add( new JLabel( "West Border" ) );
        return panel;
    }
    
    private JPanel getCenterBorder()
    {
        Color       cyan        = Color.CYAN;
        int         width       = 300;
        int         height      = 300;
        Dimension   prefSize    = new Dimension( width, height );
        JPanel      panel       = new JPanel();
        panel.setBackground( cyan );
        panel.setPreferredSize( prefSize );
        panel.add( checkBox );
        return panel;
    }
    
    private static MouseListener getMouseListenerA( String source )
    {
        MouseListener   listener    = new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent evt )
            {
                StringBuilder   bldr    = new StringBuilder();
                int             button  = evt.getButton();
                int             xco     = evt.getX();
                int             yco     = evt.getY();
                bldr.append( "Mouse button #" ).append( button )
                    .append( " clicked at (" ).append( xco )
                    .append( "," ).append( yco )
                    .append( ") in " ).append( source );
                System.out.println( bldr );
            }
        };
        return listener;
    }
    
    private MouseListener getMouseListenerB( String source )
    {
        MouseListener   listener    = new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent evt )
            {
                StringBuilder   bldr    = new StringBuilder();
                int             button  = evt.getButton();
                int             xco     = evt.getX();
                int             yco     = evt.getY();
                bldr.append( "Mouse button #" ).append( button )
                    .append( " clicked at (" ).append( xco )
                    .append( "," ).append( yco )
                    .append( ") in " ).append( source );
                System.out.println( bldr );
                
                Component   comp        = appFrame.getGlassPane();
                int         glassXco    = comp.getX();
                int         glassYco    = comp.getY();
                int         glassWidth  = comp.getWidth();
                int         glassHeight = comp.getWidth();
                String      glassName   = comp.getClass().getSimpleName();
                String      format      ="%s: (%d,%d) %d X %d (%s)%n";
                System.out.printf( 
                    format, 
                    glassName, 
                    glassXco, 
                    glassYco, 
                    glassWidth, 
                    glassHeight,
                    comp.isVisible()
                );
                comp.repaint();
            }
        };
        return listener;
    }

    @SuppressWarnings("serial")
    private class MainPanel extends JPanel
    {
        public MainPanel()
        {
            super( new BorderLayout() );
            add( getNorthBorder(), BorderLayout.NORTH );
            add( getEastBorder(), BorderLayout.EAST );
            add( getSouthBorder(), BorderLayout.SOUTH );
            add( getWestBorder(), BorderLayout.WEST );
            add( getCenterBorder(), BorderLayout.CENTER );
            
            addMouseListener(  getMouseListenerB( "Main panel" ) );
        }
        
        @Override
        public void paintComponent( Graphics graphics  )
        {
            super.paintComponent( graphics );
            Container   parent          = checkBox.getParent();
            int         parentWidth     = parent.getWidth();
            int         parentHeight    = parent.getHeight();
            int         cbWidth         = checkBox.getWidth();
            int         cbHeight        = checkBox.getHeight();
            int         cbXco           = (parentWidth - cbWidth) / 2;
            int         cbYco           = (parentHeight - cbHeight) / 2;
            checkBox.setLocation( cbXco, cbYco );
        }
    }

    @SuppressWarnings("serial")
    private static class GlassPanel extends JPanel
    {
        public GlassPanel()
        {
            addMouseListener( getMouseListenerA( "Main panel" ) );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            super.paintComponent( graphics );
            String  dimensions  = getWidth() + "," + getHeight();
            String  message     = "Glass pane: " + dimensions;
            graphics.setColor( Color.RED );
            graphics.drawOval( 50,  50,  25,  25 );
            System.out.println( message );
        }
    }
}
