package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ParameterPanel extends JPanel
{
    private JFormattedTextField start       = new JFormattedTextField();
    private JFormattedTextField end         = new JFormattedTextField();
    private JFormattedTextField step        = new JFormattedTextField();
    private JFormattedTextField precision   = new JFormattedTextField();
    
    public ParameterPanel()
    {
        super( new GridLayout( 4, 2 ) );
        Border  border  = BorderFactory.createEmptyBorder( 3, 3, 3, 3 );
        setBorder( border );
        
        start.setColumns( 5 );
        end.setColumns( 5 );
        step.setColumns( 5 );
        precision.setColumns( 5 );
        
        add( new JLabel( "Start" ) );
        add( new JLabel( "End" ) );
        add( start );
        add( end );
        add( new JLabel( "Step" ) );
        add( new JLabel( "Prec" ) );
        add( step );
        add( precision );
    }
}
