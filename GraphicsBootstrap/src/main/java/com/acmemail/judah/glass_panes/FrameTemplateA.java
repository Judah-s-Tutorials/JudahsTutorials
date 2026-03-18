package com.acmemail.judah.glass_panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FrameTemplateA
{
    private final JFrame    appFrame    = new JFrame( "Glass Pane Demo" );
    private final JCheckBox checkBox    = new JCheckBox( "Make Visible" );
    private final MainPanel mainPanel   = new MainPanel();

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new FrameTemplateA() );
    }

    public FrameTemplateA()
    {
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        appFrame.setContentPane( mainPanel );
        appFrame.setJMenuBar( getMenuBar() );
        appFrame.pack();
        appFrame.setVisible( true );
    }
    
    private JMenuBar getMenuBar()
    {
        JMenu       fileMenu    = new JMenu( "File" );
        fileMenu.add( new JMenuItem( "Open" ) );
        fileMenu.add( new JMenuItem( "Save" ) );
        
        JMenuBar    menuBar     = new JMenuBar();
        menuBar.add( fileMenu );
        return menuBar;
    }
    
    private JPanel 
    getBoundaryPanel( int rows, int cols, String label, Color color )
    {
        JPanel  panel       = new JPanel( new GridLayout( rows, cols ) );
        int     numLabels   = rows * cols;
        panel.setBackground( color );
        for ( int inx = 0 ; inx < numLabels ; ++inx )
            panel.add( new JLabel( label ) );
        return panel;
    }
    
    private JPanel getCenterPanel( Color color )
    {
        int         prefWidth   = 200;
        int         prefHeight  = 300;
        Dimension   prefSize    = new Dimension( prefWidth, prefHeight );
        JPanel  panel   = new JPanel();
        panel.setBackground( color );
        panel.setPreferredSize( prefSize );
        panel.add( checkBox );
        return panel;
    }

    @SuppressWarnings("serial")
    private class MainPanel extends JPanel
    {
        private static final Color  lightBlue   = new Color( 0xADD8E6 );
        private static final Color  lightRed    = new Color( 0xFFCCCB );
        private static final Color  lightGreen  = new Color( 0x90EE90 );
        private static final Color  lightCyan   = new Color( 0xE0FFFF );
        private static final Color  lightYellow = new Color( 0xFFFFC5 );

        public MainPanel()
        {
            super( new BorderLayout() );
            JPanel  northPanel  = 
                getBoundaryPanel( 2, 10, " North ", lightBlue );
            JPanel  eastPanel   = 
                getBoundaryPanel( 10, 1, " East ", lightRed );
            JPanel  southPanel  = 
                getBoundaryPanel( 3, 5, " South ", lightGreen );
            JPanel  westPanel   = 
                getBoundaryPanel( 10, 2, " West ", lightCyan );
            JPanel  centerPanel = getCenterPanel( lightYellow );
            
            add( northPanel, BorderLayout.NORTH );
            add( eastPanel, BorderLayout.EAST );
            add( southPanel, BorderLayout.SOUTH );
            add( westPanel, BorderLayout.WEST );
            add( centerPanel, BorderLayout.CENTER );
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
}
