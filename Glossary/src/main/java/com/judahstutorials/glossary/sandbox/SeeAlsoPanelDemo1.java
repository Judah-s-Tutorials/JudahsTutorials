package com.judahstutorials.glossary.sandbox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.judahstutorials.glossary.Definition;
import com.judahstutorials.glossary.Controls.QueryDialog;
import com.judahstutorials.glossary.Controls.SeeAlsoPanel;

/**
 * 
 * @author Jack Straub
 */
public class SeeAlsoPanelDemo1
{
    private final JFrame        frame           = 
        new JFrame( "SeeAlso Demo 1" );
    private final QueryDialog   dialog          = new QueryDialog( frame );
    private final SeeAlsoPanel  seeAlsoPanel    = new SeeAlsoPanel();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( SeeAlsoPanelDemo1::new );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application frame.
     * Must be executed on the EDT.
     */
    private SeeAlsoPanelDemo1()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( seeAlsoPanel, BorderLayout.CENTER );
        contentPane.add( getSelectPanel(), BorderLayout.SOUTH );
                
        frame.setContentPane( contentPane );
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private JPanel getSelectPanel()
    {
        JPanel  panel   = new JPanel();
        JButton select  = new JButton( "Select" );
        select.addActionListener( this::select );
        panel.add( select );
        return panel;
    }
    
    private void select( ActionEvent evt )
    {
        Definition  def     = null;
        int choice  = dialog.showDialog();
        if ( choice == JOptionPane.OK_OPTION )
            def = dialog.getSelection();
        if ( def != null )
        {
            seeAlsoPanel.setDefinition( def );
        }
        
    }
}
