package com.acmemail.judah.cartesian_plane.components;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.input.Equation;

public class ParameterPanel extends JPanel
{
    private JFormattedTextField start       = new JFormattedTextField();
    private JFormattedTextField end         = new JFormattedTextField();
    private JFormattedTextField step        = new JFormattedTextField();
    private JFormattedTextField precision   = new JFormattedTextField();
    private JFormattedTextField radius      = new JFormattedTextField();
    private JFormattedTextField theta       = new JFormattedTextField();
    private JFormattedTextField param       = new JFormattedTextField();
    
    private Equation            equation    = null;
    
    public ParameterPanel()
    {
        super( new GridLayout( 8, 2 ) );
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
        add( new JLabel( "Radius" ) );
        add( new JLabel( "Theta" ) );
        add( radius );
        add( theta );
        add( new JLabel( "Param" ) );
        add( new JLabel( "" ) );
        add( param );
        add( new JLabel( "" ) );
    }
    
    public void load( Equation equation )
    {
        start.setValue( equation.getRangeStart() );
        end.setValue( equation.getRangeEnd() );
        step.setValue( equation.getRangeStep() );
        radius.setValue( equation.getRadiusName() );
        theta.setValue( equation.getThetaName() );
        param.setValue( equation.getParamName() );
        precision.setValue( equation.getPrecision() );
    }
}
