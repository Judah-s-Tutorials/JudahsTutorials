package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class GridBagLayoutDemo1
{    
    private static final String FOG         = "Fog early, then sunny";
    private static final String LIGHT_RAIN  = "Light rain";
    private static final String CLOUDY      = "Cloudy";
    private static final String SUNNY       = "Sunny";
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "GridLayout Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = new JPanel( new GridBagLayout() );
        
        addRow0( mainPanel );
        addRow1( mainPanel );
        addRowN( mainPanel, 2, "San Francisco", 65, 55, FOG );
        addRowN( mainPanel, 3, "Seattle", 70.5, 49.3, CLOUDY );
        addRowN( mainPanel, 4, "New York", 90.3, 70, LIGHT_RAIN );
        addRowN( mainPanel, 5, "Phoenix", 110.4, 95.3, SUNNY );

        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    private static void addRow0( JPanel panel )
    {
        GridBagConstraints  gbc     = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 6;
        gbc.ipady = 6;
        gbc.weightx = .5;
        gbc.gridheight = 2;
        JLabel  city    = getLabel( "City" );
        city.setVerticalAlignment( SwingConstants.CENTER );
        city.setVerticalTextPosition( SwingConstants.CENTER );
        panel.add( city, gbc );
        
        gbc.gridx++;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        panel.add( getLabel( "High Temp" ), gbc );
        
        gbc.gridx += 2;
        panel.add( getLabel( "Low Temp" ), gbc );
        
        gbc.gridx += 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        JLabel  remarks = getLabel( "Remarks" );
        remarks.setVerticalAlignment( SwingConstants.CENTER );
        remarks.setVerticalTextPosition( SwingConstants.CENTER );
        panel.add( remarks, gbc );
    }
    
    private static void addRow1( JPanel panel )
    {
        GridBagConstraints  gbc     = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .5;
        gbc.ipadx = 6;
        gbc.ipady = 6;
        panel.add( getLabel( "F" ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( "C" ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( "F" ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( "C" ), gbc );
    }
    
    private static void addRowN(
        JPanel panel,
        int row, 
        String city,
        double high, 
        double low, 
        String rem
    )
    {
        double  highC       = (high - 32) * 5./9.;
        double  lowC        = (low - 32) * 5./9.;
        String  fmt         = "%4.1f\u00b0";
        String  strHigh     = String.format( fmt, high );
        String  strLow      = String.format( fmt, low );
        String  strHighC    = String.format( fmt, highC );
        String  strLowC     = String.format( fmt, lowC );
        
        GridBagConstraints  gbc     = new GridBagConstraints();
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .5;
        gbc.ipadx = 6;
        gbc.ipady = 6;
        panel.add( getLabel( city ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( strHigh ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( strLow ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( strHighC ), gbc );
        
        ++gbc.gridx;
        panel.add( getLabel( strLowC ), gbc );
        
        ++gbc.gridx;
        JTextField  text    = new JTextField( rem, 20 );
        panel.add( text, gbc );
    }
    
    private static JLabel getLabel( String text )
    {
        return getLabel( text, null );
    }
    
    private static JLabel getLabel( String text, Color color )
    {
        final Border    border  = 
            BorderFactory.createLineBorder( Color.BLACK );
        JLabel          label   = new JLabel( text );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setBorder( border );
        if ( color != null )
            label.setBackground( color );
        return label;
    }
}
