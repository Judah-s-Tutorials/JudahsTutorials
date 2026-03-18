package com.acmemail.judah.glass_panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CoordSysDemo
{
    private JFrame  appFrame;
    
    public CoordSysDemo()
    {
    }

    public static void main(String[] args)
    {
        CoordSysDemo    demo    = new CoordSysDemo();
        SwingUtilities.invokeLater( () -> demo.createGUI() );
    }

    private void createGUI()
    {
        appFrame = new JFrame( "Coord Sys Demo" );
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        appFrame.setContentPane( getMainPanel() );
        appFrame.pack();
        appFrame.setVisible( true );
    }
    
    private JPanel getMainPanel()
    {
        JPanel  leftPanel   = getPanel( "Left", 100, 200, Color.BLUE );
        JPanel  rightPanel  = getPanel( "Right", 100, 200, Color.GREEN );
        JPanel  centerPanel = getPanel( "Center", 150, 150, Color.YELLOW );
        JPanel  topPanel    = getPanel( "Top", 500, 50, Color.RED );
        JPanel  mainPanel = new JPanel( new BorderLayout() );
        mainPanel.add( leftPanel, BorderLayout.WEST );
        mainPanel.add( rightPanel, BorderLayout.EAST );
        mainPanel.add( centerPanel, BorderLayout.CENTER );
        mainPanel.add( topPanel, BorderLayout.NORTH );
        
        mainPanel.setName( "Main Panel" );
        mainPanel.addMouseListener( new Mouser() );
        return mainPanel;
    }
    
    private JPanel 
    getPanel( String name, int width, int height, Color color )
    {
        JPanel      panel   = new JPanel();
        Dimension   size    = new Dimension( width, height );
        panel.setBackground( color );
        panel.setPreferredSize( size );
        panel.setName( name );
        return panel;
    }
    
    private class Mouser extends MouseAdapter
    {
        private JPanel  mainPanel   = null;
        private int     mainXco     = 0;
        private int     mainYco     = 0;
        private Point   mainPoint   = null;
        
        @Override
        public void mouseClicked( MouseEvent evt )
        {
            Object  obj = evt.getSource();
            if ( obj instanceof JPanel )
            {
                mainPanel = (JPanel)obj;
                mainXco = evt.getX();
                mainYco = mainPanel.getY();
                mainPoint = new Point( mainXco, mainYco );
                Component[] children    = mainPanel.getComponents();
                printCoords( mainPanel );
                for ( Component child : children )
                    printCoords( child );
                System.out.println( "********************" );
            }
        }
        
        private void printCoords( Component comp )
        {
            Point   compPoint   = SwingUtilities.convertPoint(
                mainPanel,
                mainPoint,
                comp
            );
            int     compXco     = compPoint.x;
            int     compYco     = compPoint.y;
            String  compName    = comp.getName();
            String  format      = "%s: (%d, %d)";
            String  descrip     = String.format( format, compName, compXco, compYco );
            System.out.println( descrip );
        }
    }
}
