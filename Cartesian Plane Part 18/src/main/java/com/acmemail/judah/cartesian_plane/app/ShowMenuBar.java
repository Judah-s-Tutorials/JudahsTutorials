package com.acmemail.judah.cartesian_plane.app;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.CPMenuBar;

public class ShowMenuBar
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    
    private static void buildGUI()
    {
        JFrame      frame       = new JFrame( "Show Menu Bar" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        Border      border      =
            BorderFactory.createEmptyBorder( 0, 15, 15, 15 );
        contentPane.setBorder( border );

        JLabel      label       = new JLabel( "Show Menu Bar Demo" );
        Font        font        = label.getFont();
        float       size        = font.getSize() * 3f;
        Font        bigFont     = font.deriveFont( size );
        label.setFont( bigFont );
        
        contentPane.add( label, BorderLayout.CENTER );
        contentPane.add( new CPMenuBar( frame ), BorderLayout.NORTH );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 200, 200 );
        frame.setVisible( true );
    }
}
