package com.acmemail.judah.tesseract_sandbox;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class ShowFontNameSelector
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( ShowFontNameSelector::new );
    }
    
    public ShowFontNameSelector()
    {
        JFrame              frame   = new JFrame( "Show FontNameSelector" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        FontNameSelector    dialog  = new FontNameSelector( frame );
        JPanel              pane    = new JPanel();
        Border              border  =
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JButton             show    = new JButton( "Show Dialog" );
        JButton             exit    = new JButton( "Exit" );
        show.addActionListener( e -> showAction( dialog ) );
        exit.addActionListener( e -> System.exit( 0 ) );
        
        pane.setBorder( border );
        pane.add( show );
        pane.add( exit );
        frame.setContentPane( pane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void showAction( FontNameSelector dialog )
    {
        String  selected    = dialog.open();
        JOptionPane.showMessageDialog( null, selected );
    }
}
