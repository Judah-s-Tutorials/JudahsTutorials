package com.acmemail.judah.glass_panes;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GlassPaneDemo1A
{
    private final JFrame    frame       = new JFrame( "Glass Pane Demo 1" );
    private final JCheckBox checkBox    = new JCheckBox( "Visible" );
    private final MainPanel mainPanel   = new MainPanel();
    private final GlassPane glassPane   = new GlassPane();
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new GlassPaneDemo1A() );
    }
    
    public GlassPaneDemo1A()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( mainPanel );
        frame.setGlassPane( glassPane );
        frame.pack();
        frame.setVisible( true );
    }
    
    @SuppressWarnings("serial")
    private class MainPanel extends JPanel
    {
        public MainPanel()
        {
            MouseListener   mListener   = new Mouser( "Main Panel" );
            addMouseListener( mListener );
            int         prefWidth   = 200;
            int         prefHeight  = 300;
            Dimension   prefSize    = new Dimension( prefWidth, prefHeight );
            setPreferredSize( prefSize );
            add( checkBox );
        }
        
        @Override
        public void paintComponent( Graphics graphics )
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
    private class GlassPane extends JPanel
    {
        public GlassPane()
        {
            setOpaque( false );
            
            MouseListener   mListener   = new Mouser( "Glass Pane" );
            addMouseListener( mListener );
            checkBox.addItemListener( new ItemListener() {
                public void itemStateChanged( ItemEvent evt )
                {
                    boolean state   = 
                        evt.getStateChange() == ItemEvent.SELECTED;
                    glassPane.setVisible( state );
                    glassPane.repaint();
                }
            });
        }
    }
    
    private class Mouser extends MouseAdapter
    {
        private final String    ident;
        
        public Mouser( String ident )
        {
            this.ident = ident;
        }
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            int     xco         = evt.getX();
            int     yco         = evt.getY();
            String  format      = "%s: mouse clicked at (%d,%d)";
            String  feedback    = String.format( format, ident, xco, yco );
            System.out.println( feedback );
        }
    }
}
