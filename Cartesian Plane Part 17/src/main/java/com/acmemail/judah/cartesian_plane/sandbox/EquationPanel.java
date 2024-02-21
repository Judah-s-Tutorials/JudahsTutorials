package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class EquationPanel extends JPanel
{
    private static final String[]   plotTypes   =
    { "Y Plot", "XY Plot", "R Plot", "T Plot" };

    private JFormattedTextField xEquals = new JFormattedTextField();
    private JFormattedTextField yEquals = new JFormattedTextField();
    private JComboBox<String>   plots   = new JComboBox<>( plotTypes );
    
    public EquationPanel()
    {
        super();
        BoxLayout   layout  = new BoxLayout( this, BoxLayout.X_AXIS );
        setLayout( layout );
        
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );
        
        xEquals.setColumns( 5 );
        yEquals.setColumns( 5 );
        
        add( getXYPanel() );
        add( getComboPanel() );
    }
    
    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        panel.add( getXYPanel() );
        panel.add( getComboPanel() );
        return panel;
    }
    
    private JPanel getXYPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        panel.add( getXYPanel( "x=", xEquals ) );
        panel.add( getXYPanel( "y=", yEquals ) );
        return panel;
    }
    
    private JPanel getXYPanel( String text, JComponent comp )
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        JLabel  label   = new JLabel( text );
        panel.add( label );
        panel.add( comp );
        return panel;
        
    }
    
    private JPanel  getComboPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        panel.setLayout( layout );
        
        JButton     button  = new JButton( "Plot" );
        panel.add( plots );
        panel.add( button );
        return panel;
    }
}
